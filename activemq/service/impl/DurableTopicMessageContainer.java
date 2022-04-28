/*     */ package org.codehaus.activemq.service.impl;
/*     */ 
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.ConsumerInfo;
/*     */ import org.codehaus.activemq.message.MessageAck;
/*     */ import org.codehaus.activemq.service.MessageContainer;
/*     */ import org.codehaus.activemq.service.MessageIdentity;
/*     */ import org.codehaus.activemq.service.Subscription;
/*     */ import org.codehaus.activemq.service.TopicMessageContainer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DurableTopicMessageContainer
/*     */   implements TopicMessageContainer
/*     */ {
/*  41 */   private static final Log log = LogFactory.getLog(DurableTopicMessageContainer.class);
/*     */   
/*     */   private TopicMessageStore messageStore;
/*     */   private String destinationName;
/*     */   private MessageIdentity lastMessageIdentity;
/*     */   
/*     */   public DurableTopicMessageContainer(TopicMessageStore messageStore, String destinationName) {
/*  48 */     this.messageStore = messageStore;
/*  49 */     this.destinationName = destinationName;
/*     */   }
/*     */   
/*     */   public String getDestinationName() {
/*  53 */     return this.destinationName;
/*     */   }
/*     */   
/*     */   public MessageIdentity addMessage(ActiveMQMessage message) throws JMSException {
/*  57 */     MessageIdentity answer = this.messageStore.addMessage(message);
/*  58 */     this.lastMessageIdentity = answer;
/*  59 */     return answer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete(MessageIdentity messageID, MessageAck ack) throws JMSException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsMessage(MessageIdentity messageIdentity) throws JMSException {
/*  69 */     return (getMessage(messageIdentity) != null);
/*     */   }
/*     */   
/*     */   public ActiveMQMessage getMessage(MessageIdentity messageID) throws JMSException {
/*  73 */     return this.messageStore.getMessage(messageID);
/*     */   }
/*     */   
/*     */   public void registerMessageInterest(MessageIdentity messageIdentity) throws JMSException {
/*  77 */     this.messageStore.incrementMessageCount(messageIdentity);
/*     */   }
/*     */   
/*     */   public void unregisterMessageInterest(MessageIdentity messageIdentity, MessageAck ack) throws JMSException {
/*  81 */     this.messageStore.decrementMessageCountAndMaybeDelete(messageIdentity, ack);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLastAcknowledgedMessageID(Subscription subscription, MessageIdentity messageIdentity) throws JMSException {
/*  86 */     this.messageStore.setLastAcknowledgedMessageIdentity(subscription, messageIdentity);
/*     */   }
/*     */   
/*     */   public void recoverSubscription(Subscription subscription) throws JMSException {
/*  90 */     this.messageStore.recoverSubscription(subscription, this.lastMessageIdentity);
/*     */   }
/*     */   
/*     */   public void storeSubscription(ConsumerInfo info, Subscription subscription) throws JMSException {
/*  94 */     this.messageStore.setSubscriberEntry(info, subscription.getSubscriptionEntry());
/*     */   }
/*     */   
/*     */   public void start() throws JMSException {
/*  98 */     this.messageStore.setMessageContainer((MessageContainer)this);
/*  99 */     this.lastMessageIdentity = this.messageStore.getLastestMessageIdentity();
/* 100 */     this.messageStore.start();
/*     */   }
/*     */   
/*     */   public void stop() throws JMSException {
/* 104 */     this.messageStore.stop();
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\DurableTopicMessageContainer.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */