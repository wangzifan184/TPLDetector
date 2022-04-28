/*    */ package org.codehaus.activemq.store.bdbn;
/*    */ 
/*    */ import javax.jms.JMSException;
/*    */ import org.codehaus.activemq.message.ConsumerInfo;
/*    */ import org.codehaus.activemq.message.MessageAck;
/*    */ import org.codehaus.activemq.service.MessageIdentity;
/*    */ import org.codehaus.activemq.service.SubscriberEntry;
/*    */ import org.codehaus.activemq.service.Subscription;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BDbTopicMessageStore
/*    */   extends BDbMessageStore
/*    */   implements TopicMessageStore
/*    */ {
/*    */   public void incrementMessageCount(MessageIdentity messageId) throws JMSException {}
/*    */   
/*    */   public void decrementMessageCountAndMaybeDelete(MessageIdentity messageIdentity, MessageAck ack) throws JMSException {}
/*    */   
/*    */   public void setLastAcknowledgedMessageIdentity(Subscription subscription, MessageIdentity messageIdentity) throws JMSException {}
/*    */   
/*    */   public void recoverSubscription(Subscription subscription, MessageIdentity lastDispatchedMessage) {}
/*    */   
/*    */   public MessageIdentity getLastestMessageIdentity() throws JMSException {
/* 54 */     return null;
/*    */   }
/*    */   
/*    */   public SubscriberEntry getSubscriberEntry(ConsumerInfo info) throws JMSException {
/* 58 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setSubscriberEntry(ConsumerInfo info, SubscriberEntry subscriberEntry) throws JMSException {}
/*    */ 
/*    */   
/*    */   public void stop() throws JMSException {
/* 66 */     super.stop();
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\bdbn\BDbTopicMessageStore.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */