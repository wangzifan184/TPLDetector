/*     */ package org.codehaus.activemq.store.vm;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import javax.jms.JMSException;
/*     */ import org.codehaus.activemq.message.ConsumerInfo;
/*     */ import org.codehaus.activemq.message.MessageAck;
/*     */ import org.codehaus.activemq.service.MessageContainer;
/*     */ import org.codehaus.activemq.service.MessageIdentity;
/*     */ import org.codehaus.activemq.service.SubscriberEntry;
/*     */ import org.codehaus.activemq.service.Subscription;
/*     */ import org.codehaus.activemq.store.TopicMessageStore;
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
/*     */ public class VMTopicMessageStore
/*     */   extends VMMessageStore
/*     */   implements TopicMessageStore
/*     */ {
/*  38 */   private static final Integer ONE = new Integer(1);
/*     */   
/*     */   private Map ackDatabase;
/*     */   private Map messageCounts;
/*     */   private Map subscriberDatabase;
/*     */   
/*     */   public VMTopicMessageStore() {
/*  45 */     this(new LinkedHashMap(), makeMap(), makeMap(), makeMap());
/*     */   }
/*     */   
/*     */   public VMTopicMessageStore(LinkedHashMap messageTable, Map subscriberDatabase, Map ackDatabase, Map messageCounts) {
/*  49 */     super(messageTable);
/*  50 */     this.subscriberDatabase = subscriberDatabase;
/*  51 */     this.ackDatabase = ackDatabase;
/*  52 */     this.messageCounts = messageCounts;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMessageContainer(MessageContainer container) {}
/*     */   
/*     */   public synchronized void incrementMessageCount(MessageIdentity messageId) throws JMSException {
/*  59 */     Integer number = (Integer)this.messageCounts.get(messageId);
/*  60 */     if (number == null) {
/*  61 */       number = ONE;
/*     */     } else {
/*     */       
/*  64 */       number = new Integer(number.intValue() + 1);
/*     */     } 
/*  66 */     this.messageCounts.put(messageId, number);
/*     */   }
/*     */   
/*     */   public synchronized void decrementMessageCountAndMaybeDelete(MessageIdentity messageIdentity, MessageAck ack) throws JMSException {
/*  70 */     Integer number = (Integer)this.messageCounts.get(messageIdentity);
/*  71 */     if (number == null || number.intValue() <= 1) {
/*  72 */       removeMessage(messageIdentity, ack);
/*  73 */       if (number != null) {
/*  74 */         this.messageCounts.remove(messageIdentity);
/*     */       }
/*     */     } else {
/*     */       
/*  78 */       this.messageCounts.put(messageIdentity, new Integer(number.intValue() - 1));
/*  79 */       number = ONE;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setLastAcknowledgedMessageIdentity(Subscription subscription, MessageIdentity messageIdentity) throws JMSException {
/*  84 */     String consumerId = subscription.getConsumerId();
/*  85 */     this.ackDatabase.put(consumerId, messageIdentity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void recoverSubscription(Subscription subscription, MessageIdentity lastDispatchedMessage) {}
/*     */   
/*     */   public MessageIdentity getLastestMessageIdentity() throws JMSException {
/*  92 */     return null;
/*     */   }
/*     */   
/*     */   public SubscriberEntry getSubscriberEntry(ConsumerInfo info) throws JMSException {
/*  96 */     Object key = info.getConsumerKey();
/*  97 */     return (SubscriberEntry)this.subscriberDatabase.get(key);
/*     */   }
/*     */   
/*     */   public void setSubscriberEntry(ConsumerInfo info, SubscriberEntry subscriberEntry) throws JMSException {
/* 101 */     this.subscriberDatabase.put(info.getConsumerKey(), subscriberEntry);
/*     */   }
/*     */   
/*     */   public void stop() throws JMSException {
/* 105 */     this.ackDatabase.clear();
/* 106 */     this.messageCounts.clear();
/* 107 */     super.stop();
/*     */   }
/*     */   
/*     */   protected static Map makeMap() {
/* 111 */     return Collections.synchronizedMap(new HashMap());
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\vm\VMTopicMessageStore.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */