/*     */ package org.codehaus.activemq.service.impl;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;
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
/*     */ import org.codehaus.activemq.message.ActiveMQQueue;
/*     */ import org.codehaus.activemq.message.ConsumerInfo;
/*     */ import org.codehaus.activemq.message.MessageAck;
/*     */ import org.codehaus.activemq.service.Dispatcher;
/*     */ import org.codehaus.activemq.service.MessageContainer;
/*     */ import org.codehaus.activemq.service.QueueList;
/*     */ import org.codehaus.activemq.service.QueueListEntry;
/*     */ import org.codehaus.activemq.service.QueueMessageContainer;
/*     */ import org.codehaus.activemq.service.RedeliveryPolicy;
/*     */ import org.codehaus.activemq.service.Subscription;
/*     */ import org.codehaus.activemq.service.SubscriptionContainer;
/*     */ import org.codehaus.activemq.store.PersistenceAdapter;
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
/*     */ public class DurableQueueMessageContainerManager
/*     */   extends MessageContainerManagerSupport
/*     */ {
/*  58 */   private static final Log log = LogFactory.getLog(DurableQueueMessageContainerManager.class);
/*     */   
/*     */   private static final int MAX_MESSAGES_DISPATCHED_FROM_POLL = 50;
/*     */   private PersistenceAdapter persistenceAdapter;
/*     */   protected SubscriptionContainer subscriptionContainer;
/*     */   protected FilterFactory filterFactory;
/*  64 */   protected Map activeSubscriptions = (Map)new ConcurrentHashMap();
/*  65 */   protected Map browsers = (Map)new ConcurrentHashMap();
/*  66 */   protected DestinationMap destinationMap = new DestinationMap();
/*  67 */   private Object subscriptionMutex = new Object();
/*     */ 
/*     */ 
/*     */   
/*     */   public DurableQueueMessageContainerManager(PersistenceAdapter persistenceAdapter, RedeliveryPolicy redeliveryPolicy) {
/*  72 */     this(persistenceAdapter, new SubscriptionContainerImpl(redeliveryPolicy), (FilterFactory)new FilterFactoryImpl(), new DispatcherImpl());
/*     */   }
/*     */   
/*     */   public DurableQueueMessageContainerManager(PersistenceAdapter persistenceAdapter, SubscriptionContainer subscriptionContainer, FilterFactory filterFactory, Dispatcher dispatcher) {
/*  76 */     super(dispatcher);
/*  77 */     this.persistenceAdapter = persistenceAdapter;
/*  78 */     this.subscriptionContainer = subscriptionContainer;
/*  79 */     this.filterFactory = filterFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMessageConsumer(BrokerClient client, ConsumerInfo info) throws JMSException {
/*  88 */     if (log.isDebugEnabled()) {
/*  89 */       log.debug("Adding consumer: " + info);
/*     */     }
/*  91 */     if (info.getDestination().isQueue() && !info.getDestination().isTemporary()) {
/*     */       
/*  93 */       getContainer(info.getDestination().getPhysicalName());
/*     */       
/*  95 */       Subscription sub = this.subscriptionContainer.makeSubscription(this.dispatcher, client, info, createFilter(info));
/*  96 */       this.dispatcher.addActiveSubscription(client, sub);
/*  97 */       updateActiveSubscriptions(sub);
/*     */ 
/*     */ 
/*     */       
/* 101 */       sub.setActive(true);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeMessageConsumer(BrokerClient client, ConsumerInfo info) throws JMSException {
/* 111 */     if (log.isDebugEnabled()) {
/* 112 */       log.debug("Removing consumer: " + info);
/*     */     }
/* 114 */     if (info.getDestination() != null && info.getDestination().isQueue()) {
/* 115 */       synchronized (this.subscriptionMutex) {
/* 116 */         Subscription sub = this.subscriptionContainer.removeSubscription(info.getConsumerId());
/* 117 */         if (sub != null) {
/* 118 */           sub.setActive(false);
/* 119 */           sub.clear();
/* 120 */           this.dispatcher.removeActiveSubscription(client, sub);
/*     */           
/* 122 */           for (Iterator iter = this.messageContainers.values().iterator(); iter.hasNext(); ) {
/* 123 */             QueueMessageContainer container = iter.next();
/*     */             
/* 125 */             if (container.getDestinationName().equals(sub.getDestination().getPhysicalName())) {
/* 126 */               QueueList list = getSubscriptionList(container);
/* 127 */               list.remove(sub);
/* 128 */               if (list.isEmpty()) {
/* 129 */                 this.activeSubscriptions.remove(sub.getDestination().getPhysicalName());
/*     */               }
/* 131 */               list = getBrowserList(container);
/* 132 */               list.remove(sub);
/* 133 */               if (list.isEmpty()) {
/* 134 */                 this.browsers.remove(sub.getDestination().getPhysicalName());
/*     */               }
/*     */             } 
/*     */           } 
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
/*     */   public void deleteSubscription(String clientId, String subscriberName) throws JMSException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendMessage(BrokerClient client, ActiveMQMessage message) throws JMSException {
/* 159 */     ActiveMQDestination dest = (ActiveMQDestination)message.getJMSDestination();
/* 160 */     if (dest != null && dest.isQueue() && !message.isTemporary()) {
/* 161 */       if (log.isDebugEnabled()) {
/* 162 */         log.debug("Dispaching message: " + message);
/*     */       }
/*     */       
/* 165 */       getContainer(((ActiveMQDestination)message.getJMSDestination()).getPhysicalName());
/* 166 */       Set set = this.destinationMap.get(message.getJMSActiveMQDestination());
/* 167 */       for (Iterator i = set.iterator(); i.hasNext(); ) {
/* 168 */         QueueMessageContainer container = i.next();
/* 169 */         container.addMessage(message);
/* 170 */         this.dispatcher.wakeup();
/* 171 */         updateSendStats(client, message);
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
/*     */   public void acknowledgeMessage(BrokerClient client, MessageAck ack) throws JMSException {
/* 184 */     if (!ack.isTemporary() && ack.getDestination().isQueue()) {
/* 185 */       Subscription sub = this.subscriptionContainer.getSubscription(ack.getConsumerId());
/* 186 */       if (sub != null) {
/* 187 */         sub.messageConsumed(ack);
/* 188 */         if (ack.isMessageRead()) {
/* 189 */           updateAcknowledgeStats(client, sub);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void acknowledgeTransactedMessage(BrokerClient client, String transactionId, MessageAck ack) throws JMSException {
/* 196 */     if (!ack.isTemporary() && ack.getDestination().isQueue()) {
/* 197 */       Subscription sub = this.subscriptionContainer.getSubscription(ack.getConsumerId());
/* 198 */       if (sub != null) {
/* 199 */         sub.onAcknowledgeTransactedMessageBeforeCommit(ack);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void redeliverMessage(BrokerClient client, MessageAck ack) throws JMSException {
/* 205 */     if (!ack.isTemporary() && ack.getDestination().isQueue()) {
/* 206 */       Subscription sub = this.subscriptionContainer.getSubscription(ack.getConsumerId());
/* 207 */       if (sub != null) {
/* 208 */         sub.redeliverMessage(null, ack);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void poll() throws JMSException {
/* 219 */     synchronized (this.subscriptionMutex) {
/* 220 */       for (Iterator iter = this.activeSubscriptions.keySet().iterator(); iter.hasNext(); ) {
/* 221 */         QueueMessageContainer container = iter.next();
/*     */         
/* 223 */         QueueList browserList = (QueueList)this.browsers.get(container);
/* 224 */         doPeek(container, browserList);
/* 225 */         QueueList list = (QueueList)this.activeSubscriptions.get(container);
/* 226 */         doPoll(container, list);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void commitTransaction(BrokerClient client, String transactionId) {}
/*     */ 
/*     */   
/*     */   public void rollbackTransaction(BrokerClient client, String transactionId) {}
/*     */   
/*     */   public MessageContainer getContainer(String destinationName) throws JMSException {
/* 238 */     synchronized (this.subscriptionMutex) {
/* 239 */       return super.getContainer(destinationName);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MessageContainer createContainer(String destinationName) throws JMSException {
/* 247 */     QueueMessageContainer container = this.persistenceAdapter.createQueueMessageContainer(destinationName);
/*     */ 
/*     */     
/* 250 */     for (Iterator iter = this.subscriptionContainer.subscriptionIterator(); iter.hasNext(); ) {
/* 251 */       Subscription sub = iter.next();
/* 252 */       if (sub.isBrowser()) {
/* 253 */         updateBrowsers(container, sub);
/*     */         continue;
/*     */       } 
/* 256 */       updateActiveSubscriptions(container, sub);
/*     */     } 
/*     */ 
/*     */     
/* 260 */     ActiveMQQueue activeMQQueue = new ActiveMQQueue(destinationName);
/* 261 */     this.destinationMap.put((ActiveMQDestination)activeMQQueue, container);
/* 262 */     return (MessageContainer)container;
/*     */   }
/*     */   
/*     */   protected Destination createDestination(String destinationName) {
/* 266 */     return (Destination)new ActiveMQQueue(destinationName);
/*     */   }
/*     */   
/*     */   private void doPeek(QueueMessageContainer container, QueueList browsers) throws JMSException {
/* 270 */     if (browsers != null && browsers.size() > 0) {
/* 271 */       for (int i = 0; i < browsers.size(); i++) {
/* 272 */         SubscriptionImpl sub = (SubscriptionImpl)browsers.get(i);
/* 273 */         int count = 0;
/* 274 */         ActiveMQMessage msg = null;
/*     */         do {
/* 276 */           msg = container.peekNext(sub.getLastMessageIdentity());
/* 277 */           if (msg == null)
/* 278 */             continue;  if (sub.isTarget(msg)) {
/* 279 */             sub.addMessage((MessageContainer)container, msg);
/* 280 */             this.dispatcher.wakeup(sub);
/*     */           } else {
/*     */             
/* 283 */             sub.setLastMessageIdentifier(msg.getJMSMessageIdentity());
/*     */           }
/*     */         
/*     */         }
/* 287 */         while (msg != null && !sub.isAtPrefetchLimit() && count++ < 50);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void doPoll(QueueMessageContainer container, QueueList subList) throws JMSException {
/* 293 */     int count = 0;
/* 294 */     ActiveMQMessage msg = null;
/* 295 */     if (subList != null && subList.size() > 0) {
/*     */       do {
/* 297 */         boolean dispatched = false;
/* 298 */         msg = container.poll();
/* 299 */         if (msg == null)
/* 300 */           continue;  QueueListEntry entry = subList.getFirstEntry();
/* 301 */         boolean targeted = false;
/* 302 */         while (entry != null) {
/* 303 */           SubscriptionImpl sub = (SubscriptionImpl)entry.getElement();
/* 304 */           if (sub.isTarget(msg)) {
/* 305 */             targeted = true;
/* 306 */             if (!sub.isAtPrefetchLimit()) {
/* 307 */               sub.addMessage((MessageContainer)container, msg);
/* 308 */               dispatched = true;
/* 309 */               this.dispatcher.wakeup(sub);
/* 310 */               subList.rotate();
/*     */               break;
/*     */             } 
/*     */           } 
/* 314 */           entry = subList.getNextEntry(entry);
/*     */         } 
/* 316 */         if (!dispatched) {
/* 317 */           if (targeted)
/*     */           {
/*     */             
/* 320 */             container.returnMessage(msg.getJMSMessageIdentity());
/*     */           }
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/* 326 */       } while (msg != null && count++ < 50);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateActiveSubscriptions(Subscription subscription) throws JMSException {
/* 332 */     synchronized (this.subscriptionMutex) {
/* 333 */       boolean processedSubscriptionContainer = false;
/*     */       
/* 335 */       String subscriptionPhysicalName = subscription.getDestination().getPhysicalName();
/* 336 */       for (Iterator iter = this.messageContainers.entrySet().iterator(); iter.hasNext(); ) {
/* 337 */         Map.Entry entry = iter.next();
/* 338 */         String destinationName = (String)entry.getKey();
/* 339 */         QueueMessageContainer container = (QueueMessageContainer)entry.getValue();
/*     */         
/* 341 */         if (destinationName.equals(subscriptionPhysicalName)) {
/* 342 */           processedSubscriptionContainer = true;
/*     */         }
/* 344 */         processSubscription(subscription, container);
/*     */       } 
/* 346 */       if (!processedSubscriptionContainer) {
/* 347 */         processSubscription(subscription, (QueueMessageContainer)getContainer(subscriptionPhysicalName));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void processSubscription(Subscription subscription, QueueMessageContainer container) throws JMSException {
/* 354 */     if (subscription.isBrowser()) {
/* 355 */       updateBrowsers(container, subscription);
/*     */     } else {
/*     */       
/* 358 */       updateActiveSubscriptions(container, subscription);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateActiveSubscriptions(QueueMessageContainer container, Subscription sub) throws JMSException {
/* 365 */     if (container.getDestinationName().equals(sub.getDestination().getPhysicalName())) {
/* 366 */       container.reset();
/* 367 */       QueueList list = getSubscriptionList(container);
/* 368 */       if (!list.contains(sub)) {
/* 369 */         list.add(sub);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private QueueList getSubscriptionList(QueueMessageContainer container) {
/* 375 */     QueueList list = (QueueList)this.activeSubscriptions.get(container);
/* 376 */     if (list == null) {
/* 377 */       list = new DefaultQueueList();
/* 378 */       this.activeSubscriptions.put(container, list);
/*     */     } 
/* 380 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateBrowsers(QueueMessageContainer container, Subscription sub) throws JMSException {
/* 386 */     if (container.getDestinationName().equals(sub.getDestination().getPhysicalName())) {
/* 387 */       container.reset();
/* 388 */       QueueList list = getBrowserList(container);
/* 389 */       if (!list.contains(sub)) {
/* 390 */         list.add(sub);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private QueueList getBrowserList(QueueMessageContainer container) {
/* 396 */     QueueList list = (QueueList)this.browsers.get(container);
/* 397 */     if (list == null) {
/* 398 */       list = new DefaultQueueList();
/* 399 */       this.browsers.put(container, list);
/*     */     } 
/* 401 */     return list;
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
/* 412 */     Filter filter = this.filterFactory.createFilter((Destination)info.getDestination(), info.getSelector());
/* 413 */     if (info.isNoLocal()) {
/* 414 */       andFilter = new AndFilter(filter, (Filter)new NoLocalFilter(info.getClientId()));
/*     */     }
/* 416 */     return (Filter)andFilter;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\DurableQueueMessageContainerManager.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */