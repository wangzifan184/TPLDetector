/*    */ package org.codehaus.activemq.store.cache;
/*    */ 
/*    */ import javax.jms.JMSException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.codehaus.activemq.message.ConsumerInfo;
/*    */ import org.codehaus.activemq.message.MessageAck;
/*    */ import org.codehaus.activemq.service.MessageContainer;
/*    */ import org.codehaus.activemq.service.MessageIdentity;
/*    */ import org.codehaus.activemq.service.SubscriberEntry;
/*    */ import org.codehaus.activemq.service.Subscription;
/*    */ import org.codehaus.activemq.store.MessageStore;
/*    */ import org.codehaus.activemq.store.TopicMessageStore;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CacheTopicMessageStore
/*    */   extends CacheMessageStore
/*    */   implements TopicMessageStore
/*    */ {
/* 40 */   private static final Log log = LogFactory.getLog(CacheTopicMessageStore.class);
/*    */   private final TopicMessageStore longTermStore;
/*    */   
/*    */   public CacheTopicMessageStore(CachePersistenceAdapter adapter, TopicMessageStore longTermStore, MessageCache cache) {
/* 44 */     super(adapter, (MessageStore)longTermStore, cache);
/* 45 */     this.longTermStore = longTermStore;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLastAcknowledgedMessageIdentity(Subscription subscription, MessageIdentity messageIdentity) throws JMSException {
/* 52 */     this.longTermStore.setLastAcknowledgedMessageIdentity(subscription, messageIdentity);
/*    */   }
/*    */   
/*    */   public MessageIdentity getLastestMessageIdentity() throws JMSException {
/* 56 */     return this.longTermStore.getLastestMessageIdentity();
/*    */   }
/*    */   
/*    */   public void recoverSubscription(Subscription subscription, MessageIdentity lastDispatchedMessage) throws JMSException {
/* 60 */     this.longTermStore.recoverSubscription(subscription, lastDispatchedMessage);
/*    */   }
/*    */   
/*    */   public void setSubscriberEntry(ConsumerInfo info, SubscriberEntry subscriberEntry) throws JMSException {
/* 64 */     this.longTermStore.setSubscriberEntry(info, subscriberEntry);
/*    */   }
/*    */   
/*    */   public SubscriberEntry getSubscriberEntry(ConsumerInfo info) throws JMSException {
/* 68 */     return this.longTermStore.getSubscriberEntry(info);
/*    */   }
/*    */   
/*    */   public void setMessageContainer(MessageContainer container) {
/* 72 */     this.longTermStore.setMessageContainer(container);
/*    */   }
/*    */   
/*    */   public void incrementMessageCount(MessageIdentity messageId) throws JMSException {
/* 76 */     this.longTermStore.incrementMessageCount(messageId);
/*    */   }
/*    */   
/*    */   public void decrementMessageCountAndMaybeDelete(MessageIdentity messageIdentity, MessageAck ack) throws JMSException {
/* 80 */     this.longTermStore.decrementMessageCountAndMaybeDelete(messageIdentity, ack);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\cache\CacheTopicMessageStore.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */