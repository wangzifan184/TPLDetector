/*     */ package org.codehaus.activemq.service.boundedvm;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.broker.BrokerClient;
/*     */ import org.codehaus.activemq.filter.Filter;
/*     */ import org.codehaus.activemq.message.ActiveMQDestination;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.ConsumerInfo;
/*     */ import org.codehaus.activemq.message.MessageAck;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.util.MemoryBoundedQueue;
/*     */ import org.codehaus.activemq.message.util.MemoryBoundedQueueManager;
/*     */ import org.codehaus.activemq.service.MessageContainer;
/*     */ import org.codehaus.activemq.service.MessageIdentity;
/*     */ import org.codehaus.activemq.service.QueueListEntry;
/*     */ import org.codehaus.activemq.service.Service;
/*     */ import org.codehaus.activemq.service.impl.DefaultQueueList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TransientQueueBoundedMessageContainer
/*     */   implements MessageContainer, Service, Runnable
/*     */ {
/*     */   private MemoryBoundedQueueManager queueManager;
/*     */   private ActiveMQDestination destination;
/*     */   private SynchronizedBoolean started;
/*     */   private MemoryBoundedQueue queue;
/*     */   private Thread worker;
/*     */   private DefaultQueueList subscriptions;
/*     */   private Log log;
/*     */   
/*     */   public TransientQueueBoundedMessageContainer(MemoryBoundedQueueManager queueManager, ActiveMQDestination destination) {
/*  63 */     this.queueManager = queueManager;
/*  64 */     this.destination = destination;
/*  65 */     this.queue = queueManager.getMemoryBoundedQueue("TRANSIENT_QUEUE:-" + destination.getPhysicalName());
/*  66 */     this.started = new SynchronizedBoolean(false);
/*  67 */     this.subscriptions = new DefaultQueueList();
/*  68 */     this.log = LogFactory.getLog("TransientQueueBoundedMessageContainer:- " + destination);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInactive() {
/*  75 */     return (this.subscriptions.isEmpty() && this.queue.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TransientQueueSubscription addConsumer(Filter filter, ConsumerInfo info, BrokerClient client) throws JMSException {
/*  89 */     TransientQueueSubscription ts = findMatch(info);
/*  90 */     if (ts == null) {
/*  91 */       MemoryBoundedQueue queue = this.queueManager.getMemoryBoundedQueue(client.toString() + info.getConsumerId());
/*  92 */       ts = new TransientQueueSubscription(client, queue, filter, info);
/*  93 */       this.subscriptions.add(ts);
/*     */     } 
/*  95 */     return ts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeConsumer(ConsumerInfo info) throws JMSException {
/* 105 */     TransientQueueSubscription ts = findMatch(info);
/* 106 */     if (ts != null) {
/* 107 */       this.subscriptions.remove(ts);
/*     */       
/* 109 */       List list = ts.getUndeliveredMessages();
/* 110 */       for (ListIterator i = list.listIterator(list.size()); i.hasPrevious(); ) {
/* 111 */         ActiveMQMessage message = i.previous();
/* 112 */         message.setJMSRedelivered(true);
/* 113 */         this.queue.enqueueFirstNoBlock((Packet)message);
/*     */       } 
/* 115 */       list.clear();
/* 116 */       ts.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/* 124 */     if (this.started.commit(false, true)) {
/* 125 */       this.worker = new Thread(this, "TransientQueueDispatcher");
/* 126 */       this.worker.setPriority(6);
/* 127 */       this.worker.start();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enqueue(ActiveMQMessage message) {
/* 137 */     this.queue.enqueue((Packet)message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enqueueFirst(ActiveMQMessage message) {
/* 146 */     this.queue.enqueueFirstNoBlock((Packet)message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 155 */     this.started.set(false);
/* 156 */     this.queue.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws JMSException {
/* 165 */     if (this.started.get()) {
/* 166 */       stop();
/*     */     }
/* 168 */     this.queue.close();
/* 169 */     QueueListEntry entry = this.subscriptions.getFirstEntry();
/* 170 */     while (entry != null) {
/* 171 */       TransientQueueSubscription ts = (TransientQueueSubscription)entry.getElement();
/* 172 */       ts.close();
/* 173 */       entry = this.subscriptions.getNextEntry(entry);
/*     */     } 
/* 175 */     this.subscriptions.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 182 */     boolean dispatched = false;
/* 183 */     boolean targeted = false;
/* 184 */     ActiveMQMessage message = null;
/*     */     try {
/* 186 */       while (this.started.get()) {
/* 187 */         dispatched = false;
/* 188 */         targeted = false;
/* 189 */         if (!this.subscriptions.isEmpty()) {
/* 190 */           message = (ActiveMQMessage)this.queue.dequeue(2000L);
/* 191 */           if (message != null) {
/* 192 */             if (!message.isExpired()) {
/* 193 */               QueueListEntry entry = this.subscriptions.getFirstEntry();
/* 194 */               while (entry != null) {
/* 195 */                 TransientQueueSubscription ts = (TransientQueueSubscription)entry.getElement();
/* 196 */                 if (ts.isTarget(message)) {
/* 197 */                   targeted = true;
/* 198 */                   if (ts.canAcceptMessages()) {
/* 199 */                     ts.doDispatch(message);
/* 200 */                     message = null;
/* 201 */                     dispatched = true;
/* 202 */                     this.subscriptions.rotate();
/*     */                     break;
/*     */                   } 
/*     */                 } 
/* 206 */                 entry = this.subscriptions.getNextEntry(entry);
/*     */               }
/*     */             
/*     */             } else {
/*     */               
/* 211 */               if (this.log.isDebugEnabled()) {
/* 212 */                 this.log.debug("expired message: " + message);
/*     */               }
/* 214 */               message = null;
/*     */             } 
/*     */           }
/*     */         } 
/* 218 */         if (!dispatched) {
/* 219 */           if (message != null) {
/* 220 */             if (targeted) {
/* 221 */               this.queue.enqueueFirstNoBlock((Packet)message);
/*     */             }
/*     */             else {
/*     */               
/* 225 */               this.queue.enqueueNoBlock((Packet)message);
/*     */             } 
/*     */           }
/* 228 */           Thread.sleep(250L);
/*     */         }
/*     */       
/*     */       } 
/* 232 */     } catch (Exception e) {
/* 233 */       stop();
/* 234 */       this.log.warn("stop dispatching", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private TransientQueueSubscription findMatch(ConsumerInfo info) throws JMSException {
/* 240 */     TransientQueueSubscription result = null;
/* 241 */     QueueListEntry entry = this.subscriptions.getFirstEntry();
/* 242 */     while (entry != null) {
/* 243 */       TransientQueueSubscription ts = (TransientQueueSubscription)entry.getElement();
/* 244 */       if (ts.getConsumerInfo().equals(info)) {
/* 245 */         result = ts;
/*     */         break;
/*     */       } 
/* 248 */       entry = this.subscriptions.getNextEntry(entry);
/*     */     } 
/* 250 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQDestination getDestination() {
/* 257 */     return this.destination;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDestinationName() {
/* 264 */     return this.destination.getPhysicalName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageIdentity addMessage(ActiveMQMessage msg) throws JMSException {
/* 274 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete(MessageIdentity messageIdentity, MessageAck ack) throws JMSException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQMessage getMessage(MessageIdentity messageIdentity) throws JMSException {
/* 291 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerMessageInterest(MessageIdentity messageIdentity) throws JMSException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unregisterMessageInterest(MessageIdentity messageIdentity, MessageAck ack) throws JMSException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsMessage(MessageIdentity messageIdentity) throws JMSException {
/* 315 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean hasActiveSubscribers() {
/* 319 */     return !this.subscriptions.isEmpty();
/*     */   }
/*     */   
/*     */   protected void clear() {
/* 323 */     this.queue.clear();
/*     */   }
/*     */   
/*     */   protected void removeExpiredMessages() {
/* 327 */     long currentTime = System.currentTimeMillis();
/* 328 */     List list = this.queue.getContents();
/* 329 */     for (int i = 0; i < list.size(); i++) {
/* 330 */       ActiveMQMessage msg = list.get(i);
/* 331 */       if (msg.isExpired(currentTime)) {
/* 332 */         this.queue.remove((Packet)msg);
/* 333 */         if (this.log.isDebugEnabled())
/* 334 */           this.log.debug("expired message: " + msg); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\boundedvm\TransientQueueBoundedMessageContainer.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */