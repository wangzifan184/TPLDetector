/*     */ package org.codehaus.activemq.service.impl;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.broker.BrokerClient;
/*     */ import org.codehaus.activemq.filter.Filter;
/*     */ import org.codehaus.activemq.message.ConsumerInfo;
/*     */ import org.codehaus.activemq.message.MessageAck;
/*     */ import org.codehaus.activemq.service.Dispatcher;
/*     */ import org.codehaus.activemq.service.MessageContainer;
/*     */ import org.codehaus.activemq.service.QueueListEntry;
/*     */ import org.codehaus.activemq.service.RedeliveryPolicy;
/*     */ import org.codehaus.activemq.service.TopicMessageContainer;
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
/*     */ public class DurableTopicSubscription
/*     */   extends SubscriptionImpl
/*     */ {
/*  46 */   private static final Log log = LogFactory.getLog(DurableTopicSubscription.class);
/*     */   
/*     */   private String persistentKey;
/*     */   
/*     */   public DurableTopicSubscription(Dispatcher dispatcher, BrokerClient client, ConsumerInfo info, Filter filter, RedeliveryPolicy redeliveryPolicy) {
/*  51 */     super(dispatcher, client, info, filter, redeliveryPolicy);
/*     */   }
/*     */   
/*     */   public synchronized void messageConsumed(MessageAck ack) throws JMSException {
/*  55 */     if (!ack.isMessageRead() && !isBrowser()) {
/*  56 */       super.messageConsumed(ack);
/*     */     } else {
/*     */       
/*  59 */       Map lastMessagePointersPerContainer = new HashMap();
/*     */ 
/*     */       
/*  62 */       boolean found = false;
/*  63 */       QueueListEntry queueEntry = this.messagePtrs.getFirstEntry();
/*  64 */       while (queueEntry != null) {
/*  65 */         MessagePointer pointer = (MessagePointer)queueEntry.getElement();
/*     */         
/*  67 */         this.messagePtrs.remove(queueEntry);
/*  68 */         lastMessagePointersPerContainer.put(pointer.getContainer(), pointer);
/*  69 */         this.unconsumedMessagesDispatched.decrement();
/*     */         
/*  71 */         if (pointer.getMessageIdentity().equals(ack.getMessageIdentity())) {
/*  72 */           found = true;
/*     */           break;
/*     */         } 
/*  75 */         queueEntry = this.messagePtrs.getNextEntry(queueEntry);
/*     */       } 
/*  77 */       if (!found) {
/*  78 */         log.warn("Did not find a matching message for identity: " + ack.getMessageIdentity());
/*     */       }
/*     */ 
/*     */       
/*  82 */       for (Iterator iter = lastMessagePointersPerContainer.entrySet().iterator(); iter.hasNext(); ) {
/*  83 */         Map.Entry entry = iter.next();
/*  84 */         TopicMessageContainer container = (TopicMessageContainer)entry.getKey();
/*  85 */         MessagePointer pointer = (MessagePointer)entry.getValue();
/*  86 */         container.setLastAcknowledgedMessageID(this, pointer.getMessageIdentity());
/*     */       } 
/*     */ 
/*     */       
/*  90 */       this.dispatch.wakeup(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void redeliverMessage(MessageContainer container, MessageAck ack) throws JMSException {
/*  96 */     MessagePointer pointer = new MessagePointer(container, ack.getMessageIdentity());
/*  97 */     pointer.setRedelivered(true);
/*  98 */     this.messagePtrs.add(pointer);
/*  99 */     this.unconsumedMessagesDispatched.increment();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPersistentKey() {
/* 104 */     if (this.persistentKey == null) {
/* 105 */       this.persistentKey = "[" + getClientId() + ":" + getSubscriberName() + "]";
/*     */     }
/* 107 */     return this.persistentKey;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\DurableTopicSubscription.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */