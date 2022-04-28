/*     */ package org.codehaus.activemq.service.impl;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedInt;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Message;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.broker.BrokerClient;
/*     */ import org.codehaus.activemq.broker.BrokerConnector;
/*     */ import org.codehaus.activemq.filter.Filter;
/*     */ import org.codehaus.activemq.message.ActiveMQDestination;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.BrokerInfo;
/*     */ import org.codehaus.activemq.message.ConsumerInfo;
/*     */ import org.codehaus.activemq.message.MessageAck;
/*     */ import org.codehaus.activemq.service.Dispatcher;
/*     */ import org.codehaus.activemq.service.MessageContainer;
/*     */ import org.codehaus.activemq.service.MessageIdentity;
/*     */ import org.codehaus.activemq.service.QueueList;
/*     */ import org.codehaus.activemq.service.QueueListEntry;
/*     */ import org.codehaus.activemq.service.RedeliveryPolicy;
/*     */ import org.codehaus.activemq.service.SubscriberEntry;
/*     */ import org.codehaus.activemq.service.Subscription;
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
/*     */ public class SubscriptionImpl
/*     */   implements Subscription
/*     */ {
/*  51 */   private static final Log log = LogFactory.getLog(SubscriptionImpl.class);
/*     */   private String clientId;
/*     */   private String subscriberName;
/*     */   private ActiveMQDestination destination;
/*     */   private String selector;
/*     */   private int prefetchLimit;
/*     */   private boolean noLocal;
/*     */   private boolean active;
/*     */   private int consumerNumber;
/*     */   private String consumerId;
/*     */   private boolean browser;
/*     */   protected Dispatcher dispatch;
/*     */   protected String brokerName;
/*     */   protected String clusterName;
/*     */   private MessageIdentity lastMessageIdentity;
/*     */   Filter filter;
/*  67 */   protected SynchronizedInt unconsumedMessagesDispatched = new SynchronizedInt(0);
/*  68 */   QueueList messagePtrs = new DefaultQueueList();
/*     */ 
/*     */   
/*     */   private boolean usePrefetch = false;
/*     */ 
/*     */   
/*     */   private SubscriberEntry subscriberEntry;
/*     */   
/*     */   private ConsumerInfo activeConsumer;
/*     */   
/*     */   private BrokerClient activeClient;
/*     */   
/*     */   private RedeliveryPolicy redeliveryPolicy;
/*     */ 
/*     */   
/*     */   public SubscriptionImpl(Dispatcher dispatcher, BrokerClient client, ConsumerInfo info, Filter filter, RedeliveryPolicy redeliveryPolicy) {
/*  84 */     this.dispatch = dispatcher;
/*  85 */     this.filter = filter;
/*  86 */     this.redeliveryPolicy = redeliveryPolicy;
/*  87 */     setActiveConsumer(client, info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setActiveConsumer(BrokerClient client, ConsumerInfo info) {
/*  97 */     if (info != null) {
/*  98 */       this.clientId = info.getClientId();
/*  99 */       this.subscriberName = info.getConsumerName();
/* 100 */       this.noLocal = info.isNoLocal();
/* 101 */       this.destination = info.getDestination();
/* 102 */       this.selector = info.getSelector();
/* 103 */       this.prefetchLimit = info.getPrefetchNumber();
/* 104 */       this.consumerNumber = info.getConsumerNo();
/* 105 */       this.consumerId = info.getConsumerId();
/* 106 */       this.browser = info.isBrowser();
/*     */     } 
/* 108 */     this.activeClient = client;
/* 109 */     this.activeConsumer = info;
/* 110 */     if (client != null) {
/* 111 */       BrokerConnector brokerConnector = client.getBrokerConnector();
/* 112 */       if (brokerConnector != null) {
/* 113 */         BrokerInfo brokerInfo = brokerConnector.getBrokerInfo();
/* 114 */         if (brokerInfo != null) {
/* 115 */           this.brokerName = brokerInfo.getBrokerName();
/* 116 */           this.clusterName = brokerInfo.getClusterName();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 126 */     String str = "SubscriptionImpl(" + hashCode() + ")[" + this.consumerId + "]" + this.clientId + ": " + this.subscriberName + " : " + this.destination;
/*     */     
/* 128 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void clear() throws JMSException {
/* 137 */     QueueListEntry entry = this.messagePtrs.getFirstEntry();
/* 138 */     while (entry != null) {
/* 139 */       MessagePointer pointer = (MessagePointer)entry.getElement();
/* 140 */       pointer.clear();
/* 141 */       entry = this.messagePtrs.getNextEntry(entry);
/*     */     } 
/* 143 */     this.messagePtrs.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void reset() throws JMSException {
/* 150 */     QueueListEntry entry = this.messagePtrs.getFirstEntry();
/* 151 */     while (entry != null) {
/* 152 */       MessagePointer pointer = (MessagePointer)entry.getElement();
/* 153 */       if (pointer.isDispatched()) {
/* 154 */         pointer.reset();
/* 155 */         pointer.setRedelivered(true);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 160 */         entry = this.messagePtrs.getNextEntry(entry);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClientId() {
/* 168 */     return this.clientId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClientId(String clientId) {
/* 175 */     this.clientId = clientId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter getFilter() {
/* 182 */     return this.filter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFilter(Filter filter) {
/* 189 */     this.filter = filter;
/*     */   }
/*     */   
/*     */   public boolean isWildcard() {
/* 193 */     return this.filter.isWildcard();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPersistentKey() {
/* 198 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isSameDurableSubscription(ConsumerInfo info) throws JMSException {
/* 202 */     if (isDurableTopic()) {
/* 203 */       return (equal(this.clientId, info.getClientId()) && equal(this.subscriberName, info.getConsumerName()));
/*     */     }
/* 205 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNoLocal() {
/* 212 */     return this.noLocal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNoLocal(boolean noLocal) {
/* 219 */     this.noLocal = noLocal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubscriberName() {
/* 226 */     return this.subscriberName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSubscriberName(String subscriberName) {
/* 233 */     this.subscriberName = subscriberName;
/*     */   }
/*     */   
/*     */   public RedeliveryPolicy getRedeliveryPolicy() {
/* 237 */     return this.redeliveryPolicy;
/*     */   }
/*     */   
/*     */   public void setRedeliveryPolicy(RedeliveryPolicy redeliveryPolicy) {
/* 241 */     this.redeliveryPolicy = redeliveryPolicy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTarget(ActiveMQMessage message) throws JMSException {
/* 252 */     boolean result = false;
/* 253 */     if (message != null && (
/* 254 */       this.activeClient == null || this.brokerName == null || this.clusterName == null || !this.activeClient.isClusteredConnection() || !message.isEntryCluster(this.clusterName) || message.isEntryBroker(this.brokerName))) {
/*     */ 
/*     */       
/* 257 */       result = this.filter.matches((Message)message);
/*     */       
/* 259 */       if (this.noLocal && result && 
/* 260 */         clientIDsEqual(message)) {
/* 261 */         result = false;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 266 */     return result;
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
/*     */   public synchronized void addMessage(MessageContainer container, ActiveMQMessage message) throws JMSException {
/* 279 */     if (log.isDebugEnabled()) {
/* 280 */       log.debug("Adding to subscription: " + this + " message: " + message);
/*     */     }
/* 282 */     MessagePointer pointer = new MessagePointer(container, message.getJMSMessageIdentity());
/* 283 */     this.messagePtrs.add(pointer);
/* 284 */     this.dispatch.wakeup(this);
/* 285 */     this.lastMessageIdentity = message.getJMSMessageIdentity();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void messageConsumed(MessageAck ack) throws JMSException {
/* 295 */     doMessageConsume(ack, true);
/*     */   }
/*     */   
/*     */   public synchronized void onAcknowledgeTransactedMessageBeforeCommit(MessageAck ack) throws JMSException {
/* 299 */     doMessageConsume(ack, false);
/*     */   }
/*     */   
/*     */   public synchronized void redeliverMessage(MessageContainer container, MessageAck ack) throws JMSException {
/* 303 */     QueueListEntry entry = this.messagePtrs.getFirstEntry();
/* 304 */     while (entry != null) {
/* 305 */       MessagePointer pointer = (MessagePointer)entry.getElement();
/* 306 */       if (pointer.getMessageIdentity().getMessageID().equals(ack.getMessageID())) {
/*     */         break;
/*     */       }
/* 309 */       entry = this.messagePtrs.getNextEntry(entry);
/*     */     } 
/* 311 */     if (entry != null) {
/* 312 */       MessagePointer pointer = (MessagePointer)entry.getElement();
/* 313 */       if (pointer != null) {
/* 314 */         this.unconsumedMessagesDispatched.increment();
/*     */         
/* 316 */         pointer.reset();
/* 317 */         pointer.setRedelivered(true);
/* 318 */         this.dispatch.wakeup(this);
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
/*     */   public synchronized ActiveMQMessage[] getMessagesToDispatch() throws JMSException {
/* 330 */     if (this.usePrefetch) {
/* 331 */       return getMessagesWithPrefetch();
/*     */     }
/* 333 */     List tmpList = new ArrayList();
/* 334 */     QueueListEntry entry = this.messagePtrs.getFirstEntry();
/* 335 */     while (entry != null) {
/* 336 */       MessagePointer pointer = (MessagePointer)entry.getElement();
/* 337 */       if (!pointer.isDispatched()) {
/* 338 */         ActiveMQMessage msg = pointer.getContainer().getMessage(pointer.getMessageIdentity());
/* 339 */         if (msg != null) {
/* 340 */           if (pointer.isDispatched() || pointer.isRedelivered())
/*     */           {
/* 342 */             msg.setJMSRedelivered(true);
/*     */           }
/* 344 */           pointer.setDispatched(true);
/* 345 */           tmpList.add(msg);
/*     */         }
/*     */         else {
/*     */           
/* 349 */           log.info("Message probably expired: " + msg);
/* 350 */           QueueListEntry discarded = entry;
/* 351 */           entry = this.messagePtrs.getPrevEntry(discarded);
/* 352 */           this.messagePtrs.remove(discarded);
/*     */         } 
/*     */       } 
/* 355 */       entry = this.messagePtrs.getNextEntry(entry);
/*     */     } 
/* 357 */     ActiveMQMessage[] messages = new ActiveMQMessage[tmpList.size()];
/* 358 */     return tmpList.<ActiveMQMessage>toArray(messages);
/*     */   }
/*     */   
/*     */   public synchronized SubscriberEntry getSubscriptionEntry() {
/* 362 */     if (this.subscriberEntry == null) {
/* 363 */       this.subscriberEntry = createSubscriptionEntry();
/*     */     }
/* 365 */     return this.subscriberEntry;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SubscriberEntry createSubscriptionEntry() {
/* 371 */     SubscriberEntry answer = new SubscriberEntry();
/* 372 */     answer.setClientID(this.clientId);
/* 373 */     answer.setConsumerName(this.subscriberName);
/* 374 */     answer.setDestination(this.destination.getPhysicalName());
/* 375 */     answer.setSelector(this.selector);
/* 376 */     return answer;
/*     */   }
/*     */   
/*     */   protected synchronized ActiveMQMessage[] getMessagesWithPrefetch() throws JMSException {
/* 380 */     List tmpList = new ArrayList();
/* 381 */     QueueListEntry entry = this.messagePtrs.getFirstEntry();
/* 382 */     int count = 0;
/* 383 */     int maxNumberToDispatch = this.prefetchLimit - this.unconsumedMessagesDispatched.get();
/* 384 */     while (entry != null && count < maxNumberToDispatch) {
/* 385 */       MessagePointer pointer = (MessagePointer)entry.getElement();
/* 386 */       if (!pointer.isDispatched()) {
/* 387 */         ActiveMQMessage msg = pointer.getContainer().getMessage(pointer.getMessageIdentity());
/* 388 */         if (msg != null) {
/* 389 */           if (pointer.isDispatched() || pointer.isRedelivered())
/*     */           {
/* 391 */             msg.setJMSRedelivered(true);
/*     */           }
/* 393 */           pointer.setDispatched(true);
/* 394 */           tmpList.add(msg);
/* 395 */           this.unconsumedMessagesDispatched.increment();
/* 396 */           count++;
/*     */         }
/*     */         else {
/*     */           
/* 400 */           log.info("Message probably expired: " + msg);
/* 401 */           QueueListEntry discarded = entry;
/* 402 */           entry = this.messagePtrs.getPrevEntry(discarded);
/* 403 */           this.messagePtrs.remove(discarded);
/*     */         } 
/*     */       } 
/* 406 */       entry = this.messagePtrs.getNextEntry(entry);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 416 */     ActiveMQMessage[] messages = new ActiveMQMessage[tmpList.size()];
/* 417 */     return tmpList.<ActiveMQMessage>toArray(messages);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isAtPrefetchLimit() throws JMSException {
/* 427 */     if (this.usePrefetch) {
/* 428 */       int underlivedMessageCount = this.messagePtrs.size() - this.unconsumedMessagesDispatched.get();
/* 429 */       return (underlivedMessageCount >= this.prefetchLimit);
/*     */     } 
/*     */     
/* 432 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isReadyToDispatch() throws JMSException {
/* 443 */     boolean answer = (this.active && this.messagePtrs.size() > 0);
/* 444 */     return answer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQDestination getDestination() {
/* 451 */     return this.destination;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSelector() {
/* 458 */     return this.selector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isActive() {
/* 465 */     return this.active;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setActive(boolean active) throws JMSException {
/* 472 */     this.active = active;
/* 473 */     if (!active) {
/* 474 */       reset();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getConsumerNumber() {
/* 482 */     return this.consumerNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getConsumerId() {
/* 489 */     return this.consumerId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDurableTopic() throws JMSException {
/* 499 */     return (this.destination.isTopic() && this.subscriberName != null && this.subscriberName.length() > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBrowser() throws JMSException {
/* 509 */     return this.browser;
/*     */   }
/*     */   
/*     */   public MessageIdentity getLastMessageIdentity() throws JMSException {
/* 513 */     return this.lastMessageIdentity;
/*     */   }
/*     */   
/*     */   public void setLastMessageIdentifier(MessageIdentity messageIdentity) throws JMSException {
/* 517 */     this.lastMessageIdentity = messageIdentity;
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
/*     */   protected synchronized void doMessageConsume(MessageAck ack, boolean remove) throws JMSException {
/* 530 */     int count = 0;
/* 531 */     boolean found = false;
/* 532 */     QueueListEntry entry = this.messagePtrs.getFirstEntry();
/* 533 */     while (entry != null) {
/* 534 */       MessagePointer pointer = (MessagePointer)entry.getElement();
/* 535 */       if (remove) {
/* 536 */         this.messagePtrs.remove(entry);
/* 537 */         if (ack.isMessageRead() && !this.browser) {
/* 538 */           pointer.delete(ack);
/*     */         }
/*     */       } 
/* 541 */       count++;
/*     */ 
/*     */       
/* 544 */       if (remove && !ack.isPartOfTransaction()) {
/* 545 */         this.unconsumedMessagesDispatched.decrement();
/*     */       }
/* 547 */       if (pointer.getMessageIdentity().equals(ack.getMessageIdentity())) {
/* 548 */         if (!remove && ack.isPartOfTransaction())
/*     */         {
/*     */           
/* 551 */           this.unconsumedMessagesDispatched.decrement();
/*     */         }
/* 553 */         found = true;
/*     */         break;
/*     */       } 
/* 556 */       entry = this.messagePtrs.getNextEntry(entry);
/*     */     } 
/* 558 */     if (!found && log.isDebugEnabled()) {
/* 559 */       log.debug("Did not find a matching message for identity: " + ack.getMessageIdentity());
/*     */     }
/* 561 */     this.dispatch.wakeup(this);
/*     */   }
/*     */   
/*     */   protected boolean clientIDsEqual(ActiveMQMessage message) {
/* 565 */     String msgClientID = message.getJMSClientID();
/* 566 */     String producerClientID = message.getProducerID();
/* 567 */     String subClientID = this.clientId;
/* 568 */     if (producerClientID != null && producerClientID.equals(subClientID)) {
/* 569 */       return true;
/*     */     }
/* 571 */     if (msgClientID == null || subClientID == null) {
/* 572 */       return false;
/*     */     }
/*     */     
/* 575 */     return msgClientID.equals(subClientID);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static final boolean equal(Object left, Object right) {
/* 580 */     return (left == right || (left != null && right != null && left.equals(right)));
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\SubscriptionImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */