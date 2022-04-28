/*     */ package org.codehaus.activemq.service.boundedvm;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;
/*     */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.jms.Destination;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.broker.BrokerClient;
/*     */ import org.codehaus.activemq.filter.AndFilter;
/*     */ import org.codehaus.activemq.filter.DestinationMap;
/*     */ import org.codehaus.activemq.filter.Filter;
/*     */ import org.codehaus.activemq.filter.FilterFactory;
/*     */ import org.codehaus.activemq.filter.FilterFactoryImpl;
/*     */ import org.codehaus.activemq.filter.NoLocalFilter;
/*     */ import org.codehaus.activemq.message.ActiveMQDestination;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.ConsumerInfo;
/*     */ import org.codehaus.activemq.message.MessageAck;
/*     */ import org.codehaus.activemq.message.util.MemoryBoundedQueue;
/*     */ import org.codehaus.activemq.message.util.MemoryBoundedQueueManager;
/*     */ import org.codehaus.activemq.service.MessageContainer;
/*     */ import org.codehaus.activemq.service.MessageContainerManager;
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
/*     */ public class TransientQueueBoundedMessageManager
/*     */   implements MessageContainerManager, Runnable
/*     */ {
/*     */   private static final int GARBAGE_COLLECTION_CAPACITY_LIMIT = 20;
/*  58 */   private static final Log log = LogFactory.getLog(TransientQueueBoundedMessageManager.class);
/*     */   
/*     */   private MemoryBoundedQueueManager queueManager;
/*     */   
/*     */   private ConcurrentHashMap containers;
/*     */   
/*     */   private ConcurrentHashMap subscriptions;
/*     */   
/*     */   private FilterFactory filterFactory;
/*     */   
/*     */   private SynchronizedBoolean started;
/*     */   private SynchronizedBoolean doingGarbageCollection;
/*     */   private Map destinations;
/*     */   private DestinationMap destinationMap;
/*     */   private Thread garbageCollectionThread;
/*     */   
/*     */   public TransientQueueBoundedMessageManager(MemoryBoundedQueueManager mgr) {
/*  75 */     this.queueManager = mgr;
/*  76 */     this.containers = new ConcurrentHashMap();
/*  77 */     this.destinationMap = new DestinationMap();
/*  78 */     this.destinations = (Map)new ConcurrentHashMap();
/*  79 */     this.subscriptions = new ConcurrentHashMap();
/*  80 */     this.filterFactory = (FilterFactory)new FilterFactoryImpl();
/*  81 */     this.started = new SynchronizedBoolean(false);
/*  82 */     this.doingGarbageCollection = new SynchronizedBoolean(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() throws JMSException {
/*  91 */     if (this.started.commit(false, true)) {
/*  92 */       for (Iterator i = this.containers.values().iterator(); i.hasNext(); ) {
/*  93 */         TransientQueueBoundedMessageContainer container = i.next();
/*  94 */         container.start();
/*     */       } 
/*  96 */       this.garbageCollectionThread = new Thread(this);
/*  97 */       this.garbageCollectionThread.setName("TQMCMGarbageCollector");
/*  98 */       this.garbageCollectionThread.start();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() throws JMSException {
/* 108 */     if (this.started.commit(true, false)) {
/* 109 */       for (Iterator i = this.containers.values().iterator(); i.hasNext(); ) {
/* 110 */         TransientQueueBoundedMessageContainer container = i.next();
/* 111 */         container.stop();
/*     */       } 
/* 113 */       if (this.garbageCollectionThread != null) {
/* 114 */         this.garbageCollectionThread.interrupt();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 123 */     while (this.started.get()) {
/* 124 */       doGarbageCollection();
/*     */       try {
/* 126 */         Thread.sleep(2000L);
/*     */       }
/* 128 */       catch (InterruptedException e) {}
/*     */     } 
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
/*     */   public synchronized void addMessageConsumer(BrokerClient client, ConsumerInfo info) throws JMSException {
/* 141 */     ActiveMQDestination destination = info.getDestination();
/* 142 */     if (destination.isQueue()) {
/* 143 */       TransientQueueBoundedMessageContainer container = (TransientQueueBoundedMessageContainer)this.containers.get(destination);
/*     */       
/* 145 */       if (container == null) {
/* 146 */         MemoryBoundedQueue queue = this.queueManager.getMemoryBoundedQueue(client.toString());
/* 147 */         container = new TransientQueueBoundedMessageContainer(this.queueManager, destination);
/* 148 */         addContainer(container);
/* 149 */         if (this.started.get()) {
/* 150 */           container.start();
/*     */         }
/*     */       } 
/* 153 */       TransientQueueSubscription ts = container.addConsumer(createFilter(info), info, client);
/* 154 */       if (ts != null) {
/* 155 */         this.subscriptions.put(info.getConsumerId(), ts);
/*     */       }
/* 157 */       String name = destination.getPhysicalName();
/* 158 */       if (!this.destinations.containsKey(name)) {
/* 159 */         this.destinations.put(name, destination);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void removeMessageConsumer(BrokerClient client, ConsumerInfo info) throws JMSException {
/* 170 */     ActiveMQDestination destination = info.getDestination();
/* 171 */     if (destination.isQueue()) {
/* 172 */       for (Iterator i = this.containers.values().iterator(); i.hasNext(); ) {
/* 173 */         TransientQueueBoundedMessageContainer container = i.next();
/* 174 */         if (container != null) {
/* 175 */           container.removeConsumer(info);
/*     */         }
/*     */       } 
/* 178 */       this.subscriptions.remove(info.getConsumerId());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteSubscription(String clientId, String subscriberName) throws JMSException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendMessage(BrokerClient client, ActiveMQMessage message) throws JMSException {
/* 198 */     if (message != null && message.getJMSActiveMQDestination().isQueue() && message.isTemporary()) {
/* 199 */       if (this.queueManager.getCurrentCapacity() <= 20) {
/* 200 */         doGarbageCollection();
/*     */       }
/* 202 */       Set set = this.destinationMap.get(message.getJMSActiveMQDestination());
/* 203 */       for (Iterator i = set.iterator(); i.hasNext(); ) {
/* 204 */         TransientQueueBoundedMessageContainer container = i.next();
/* 205 */         container.enqueue(message);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void acknowledgeMessage(BrokerClient client, MessageAck ack) throws JMSException {
/* 216 */     TransientQueueSubscription ts = (TransientQueueSubscription)this.subscriptions.get(ack.getConsumerId());
/* 217 */     if (ts != null) {
/* 218 */       ActiveMQMessage message = ts.acknowledgeMessage(ack.getMessageID());
/* 219 */       if (message != null && !ack.isMessageRead()) {
/* 220 */         message.setJMSRedelivered(true);
/* 221 */         Set set = this.destinationMap.get(message.getJMSActiveMQDestination());
/* 222 */         Iterator i = set.iterator(); if (i.hasNext()) {
/* 223 */           TransientQueueBoundedMessageContainer container = i.next();
/* 224 */           container.enqueueFirst(message);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void acknowledgeTransactedMessage(BrokerClient client, String transactionId, MessageAck ack) throws JMSException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void redeliverMessage(BrokerClient client, MessageAck ack) throws JMSException {
/* 247 */     TransientQueueSubscription ts = (TransientQueueSubscription)this.subscriptions.get(ack.getConsumerId());
/* 248 */     if (ts != null) {
/* 249 */       ActiveMQMessage message = ts.acknowledgeMessage(ack.getMessageID());
/* 250 */       if (message != null) {
/* 251 */         message.setJMSRedelivered(true);
/* 252 */         Set set = this.destinationMap.get(message.getJMSActiveMQDestination());
/* 253 */         Iterator i = set.iterator(); if (i.hasNext()) {
/* 254 */           TransientQueueBoundedMessageContainer container = i.next();
/* 255 */           container.enqueueFirst(message);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void poll() throws JMSException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void commitTransaction(BrokerClient client, String transactionId) throws JMSException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rollbackTransaction(BrokerClient client, String transactionId) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageContainer getContainer(String physicalName) throws JMSException {
/* 294 */     Object key = this.destinations.get(physicalName);
/* 295 */     if (key != null) {
/* 296 */       return (MessageContainer)this.containers.get(key);
/*     */     }
/* 298 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map getDestinations() {
/* 305 */     return Collections.unmodifiableMap(this.destinations);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Filter createFilter(ConsumerInfo info) throws JMSException {
/*     */     AndFilter andFilter;
/* 316 */     Filter filter = this.filterFactory.createFilter((Destination)info.getDestination(), info.getSelector());
/* 317 */     if (info.isNoLocal()) {
/* 318 */       andFilter = new AndFilter(filter, (Filter)new NoLocalFilter(info.getClientId()));
/*     */     }
/* 320 */     return (Filter)andFilter;
/*     */   }
/*     */   
/*     */   private void doGarbageCollection() {
/* 324 */     if (this.doingGarbageCollection.commit(true, false)) {
/* 325 */       if (this.queueManager.getCurrentCapacity() <= 20) {
/* 326 */         for (Iterator iterator = this.containers.values().iterator(); iterator.hasNext(); ) {
/* 327 */           TransientQueueBoundedMessageContainer container = iterator.next();
/* 328 */           container.removeExpiredMessages();
/*     */         } 
/*     */       }
/*     */       
/* 332 */       if (this.queueManager.getCurrentCapacity() <= 20) {
/* 333 */         for (Iterator iterator = this.containers.values().iterator(); iterator.hasNext(); ) {
/* 334 */           TransientQueueBoundedMessageContainer container = iterator.next();
/* 335 */           if (!container.hasActiveSubscribers()) {
/* 336 */             container.clear();
/*     */           }
/*     */         } 
/*     */       }
/* 340 */       for (Iterator i = this.containers.values().iterator(); i.hasNext(); ) {
/* 341 */         TransientQueueBoundedMessageContainer container = i.next();
/* 342 */         if (container.isInactive()) {
/*     */           try {
/* 344 */             container.close();
/* 345 */             log.info("closed inactive transient queue container: " + container.getDestinationName());
/*     */           }
/* 347 */           catch (JMSException e) {
/* 348 */             log.warn("failure closing container", (Throwable)e);
/*     */           } 
/* 350 */           removeContainer(container);
/*     */         } 
/*     */       } 
/*     */       
/* 354 */       this.doingGarbageCollection.set(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   private synchronized void addContainer(TransientQueueBoundedMessageContainer container) {
/* 359 */     this.containers.put(container.getDestination(), container);
/* 360 */     this.destinationMap.put(container.getDestination(), container);
/*     */   }
/*     */   
/*     */   private synchronized void removeContainer(TransientQueueBoundedMessageContainer container) {
/* 364 */     this.containers.remove(container.getDestination());
/* 365 */     this.destinationMap.remove(container.getDestination(), container);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Destination createDestination(String destinationName) {
/* 370 */     return null;
/*     */   }
/*     */   
/*     */   protected MessageContainer createContainer(String destinationName) throws JMSException {
/* 374 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\boundedvm\TransientQueueBoundedMessageManager.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */