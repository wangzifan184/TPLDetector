/*     */ package org.codehaus.activemq.service.boundedvm;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.CopyOnWriteArrayList;
/*     */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ import org.codehaus.activemq.message.util.BoundedPacketQueue;
/*     */ import org.codehaus.activemq.service.MessageContainer;
/*     */ import org.codehaus.activemq.service.MessageIdentity;
/*     */ import org.codehaus.activemq.service.Service;
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
/*     */ public class TransientTopicBoundedMessageContainer
/*     */   implements MessageContainer, Service, Runnable
/*     */ {
/*     */   private SynchronizedBoolean started;
/*     */   private BrokerClient client;
/*     */   private BoundedPacketQueue queue;
/*     */   private Thread worker;
/*     */   private CopyOnWriteArrayList subscriptions;
/*     */   private Log log;
/*     */   
/*     */   public TransientTopicBoundedMessageContainer(BrokerClient client, BoundedPacketQueue queue) {
/*  60 */     this.client = client;
/*  61 */     this.queue = queue;
/*  62 */     this.started = new SynchronizedBoolean(false);
/*  63 */     this.subscriptions = new CopyOnWriteArrayList();
/*  64 */     this.log = LogFactory.getLog("TransientTopicBoundedMessageContainer:- " + client);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInactive() {
/*  71 */     return this.subscriptions.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BrokerClient getBrokerClient() {
/*  78 */     return this.client;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConsumer(Filter filter, ConsumerInfo info) {
/*  88 */     TransientTopicSubscription ts = findMatch(info);
/*  89 */     if (ts == null) {
/*  90 */       ts = new TransientTopicSubscription(filter, info);
/*  91 */       this.subscriptions.add(ts);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeConsumer(ConsumerInfo info) {
/* 101 */     TransientTopicSubscription ts = findMatch(info);
/* 102 */     if (ts != null) {
/* 103 */       this.subscriptions.remove(ts);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/* 111 */     if (this.started.commit(false, true)) {
/* 112 */       this.worker = new Thread(this, "TransientTopicDispatcher");
/* 113 */       this.worker.setPriority(6);
/* 114 */       this.worker.start();
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
/*     */   public boolean targetAndDispatch(BrokerClient sender, ActiveMQMessage message) throws JMSException {
/* 127 */     boolean result = false;
/* 128 */     if (!this.client.isClusteredConnection() || !sender.isClusteredConnection()) {
/* 129 */       List tmpList = null;
/* 130 */       for (Iterator i = this.subscriptions.iterator(); i.hasNext(); ) {
/* 131 */         TransientTopicSubscription ts = i.next();
/* 132 */         if (ts.isTarget(message)) {
/* 133 */           if (tmpList == null) {
/* 134 */             tmpList = new ArrayList();
/*     */           }
/* 136 */           tmpList.add(ts);
/*     */         } 
/*     */       } 
/* 139 */       dispatchToQueue(message, tmpList);
/* 140 */       result = (tmpList != null);
/*     */     } 
/* 142 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 149 */     this.started.set(false);
/* 150 */     this.queue.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 157 */     if (this.started.get()) {
/* 158 */       stop();
/*     */     }
/* 160 */     this.queue.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 167 */     int count = 0;
/* 168 */     ActiveMQMessage message = null;
/* 169 */     while (this.started.get()) {
/*     */       try {
/* 171 */         message = (ActiveMQMessage)this.queue.dequeue(2000L);
/* 172 */         if (message != null && !message.isExpired()) {
/* 173 */           this.client.dispatch(message);
/* 174 */           if (++count == 250) {
/* 175 */             count = 0;
/* 176 */             Thread.yield();
/*     */           }
/*     */         
/*     */         } 
/* 180 */       } catch (Exception e) {
/* 181 */         stop();
/* 182 */         this.log.warn("stop dispatching", e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void dispatchToQueue(ActiveMQMessage message, List list) throws JMSException {
/* 188 */     if (list != null && !list.isEmpty()) {
/* 189 */       int[] ids = new int[list.size()];
/* 190 */       for (int i = 0; i < list.size(); i++) {
/* 191 */         TransientTopicSubscription ts = list.get(i);
/* 192 */         ids[i] = ts.getConsumerInfo().getConsumerNo();
/*     */       } 
/* 194 */       message = message.shallowCopy();
/* 195 */       message.setConsumerNos(ids);
/*     */       try {
/* 197 */         this.queue.enqueue((Packet)message);
/*     */       }
/* 199 */       catch (InterruptedException e) {
/* 200 */         this.log.warn("queue interuppted, closing", e);
/* 201 */         close();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private TransientTopicSubscription findMatch(ConsumerInfo info) {
/* 207 */     TransientTopicSubscription result = null;
/* 208 */     for (Iterator i = this.subscriptions.iterator(); i.hasNext(); ) {
/* 209 */       TransientTopicSubscription ts = i.next();
/* 210 */       if (ts.getConsumerInfo().equals(info)) {
/* 211 */         result = ts;
/*     */         break;
/*     */       } 
/*     */     } 
/* 215 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasConsumerFor(ActiveMQDestination destination) {
/* 223 */     for (Iterator i = this.subscriptions.iterator(); i.hasNext(); ) {
/* 224 */       TransientTopicSubscription ts = i.next();
/* 225 */       ConsumerInfo info = ts.getConsumerInfo();
/* 226 */       if (info.getDestination().matches(destination)) {
/* 227 */         return true;
/*     */       }
/*     */     } 
/* 230 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDestinationName() {
/* 237 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageIdentity addMessage(ActiveMQMessage msg) throws JMSException {
/* 245 */     return null;
/*     */   }
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
/* 261 */     return null;
/*     */   }
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
/* 284 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\boundedvm\TransientTopicBoundedMessageContainer.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */