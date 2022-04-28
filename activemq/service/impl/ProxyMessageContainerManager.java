/*     */ package org.codehaus.activemq.service.impl;
/*     */ 
/*     */ import java.util.Map;
/*     */ import javax.jms.JMSException;
/*     */ import org.codehaus.activemq.broker.BrokerClient;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.ConsumerInfo;
/*     */ import org.codehaus.activemq.message.MessageAck;
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
/*     */ public abstract class ProxyMessageContainerManager
/*     */   implements MessageContainerManager
/*     */ {
/*     */   private MessageContainerManager delegate;
/*     */   
/*     */   public ProxyMessageContainerManager() {}
/*     */   
/*     */   public ProxyMessageContainerManager(MessageContainerManager delegate) {
/*  44 */     this.delegate = delegate;
/*     */   }
/*     */   
/*     */   public Map getDestinations() {
/*  48 */     return getDelegate().getDestinations();
/*     */   }
/*     */ 
/*     */   
/*     */   public void acknowledgeMessage(BrokerClient client, MessageAck ack) throws JMSException {
/*  53 */     getDelegate().acknowledgeMessage(client, ack);
/*     */   }
/*     */   
/*     */   public void acknowledgeTransactedMessage(BrokerClient client, String transactionId, MessageAck ack) throws JMSException {
/*  57 */     getDelegate().acknowledgeTransactedMessage(client, transactionId, ack);
/*     */   }
/*     */   
/*     */   public void redeliverMessage(BrokerClient client, MessageAck ack) throws JMSException {
/*  61 */     getDelegate().redeliverMessage(client, ack);
/*     */   }
/*     */   
/*     */   public void addMessageConsumer(BrokerClient client, ConsumerInfo info) throws JMSException {
/*  65 */     getDelegate().addMessageConsumer(client, info);
/*     */   }
/*     */   
/*     */   public void commitTransaction(BrokerClient client, String transactionId) throws JMSException {
/*  69 */     getDelegate().commitTransaction(client, transactionId);
/*     */   }
/*     */   
/*     */   public void deleteSubscription(String clientId, String subscriberName) throws JMSException {
/*  73 */     getDelegate().deleteSubscription(clientId, subscriberName);
/*     */   }
/*     */   
/*     */   public void poll() throws JMSException {
/*  77 */     getDelegate().poll();
/*     */   }
/*     */   
/*     */   public void removeMessageConsumer(BrokerClient client, ConsumerInfo info) throws JMSException {
/*  81 */     getDelegate().removeMessageConsumer(client, info);
/*     */   }
/*     */   
/*     */   public void rollbackTransaction(BrokerClient client, String transactionId) {
/*  85 */     getDelegate().rollbackTransaction(client, transactionId);
/*     */   }
/*     */   
/*     */   public void sendMessage(BrokerClient client, ActiveMQMessage message) throws JMSException {
/*  89 */     getDelegate().sendMessage(client, message);
/*     */   }
/*     */   
/*     */   public MessageContainer getContainer(String physicalName) throws JMSException {
/*  93 */     return getDelegate().getContainer(physicalName);
/*     */   }
/*     */   
/*     */   public void start() throws JMSException {
/*  97 */     getDelegate().start();
/*     */   }
/*     */   
/*     */   public void stop() throws JMSException {
/* 101 */     getDelegate().stop();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected MessageContainerManager getDelegate() {
/* 107 */     return this.delegate;
/*     */   }
/*     */   
/*     */   protected void setDelegate(MessageContainerManager delegate) {
/* 111 */     this.delegate = delegate;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\ProxyMessageContainerManager.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */