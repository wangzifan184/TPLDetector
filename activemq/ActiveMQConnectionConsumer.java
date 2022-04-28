/*     */ package org.codehaus.activemq;
/*     */ 
/*     */ import javax.jms.ConnectionConsumer;
/*     */ import javax.jms.IllegalStateException;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.ServerSession;
/*     */ import javax.jms.ServerSessionPool;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.ConsumerInfo;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.util.MemoryBoundedQueue;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ActiveMQConnectionConsumer
/*     */   implements ConnectionConsumer, ActiveMQMessageDispatcher
/*     */ {
/*     */   private ActiveMQConnection connection;
/*     */   private ServerSessionPool sessionPool;
/*     */   private ConsumerInfo consumerInfo;
/*     */   private boolean closed;
/*     */   private int maximumMessages;
/*     */   protected MemoryBoundedQueue messageQueue;
/*     */   
/*     */   protected ActiveMQConnectionConsumer(ActiveMQConnection theConnection, ServerSessionPool theSessionPool, ConsumerInfo theConsumerInfo, int theMaximumMessages) throws JMSException {
/*  82 */     this.connection = theConnection;
/*  83 */     this.sessionPool = theSessionPool;
/*  84 */     this.consumerInfo = theConsumerInfo;
/*  85 */     this.maximumMessages = theMaximumMessages;
/*  86 */     this.connection.addConnectionConsumer(this);
/*  87 */     this.consumerInfo.setStarted(true);
/*  88 */     this.connection.syncSendPacket((Packet)this.consumerInfo);
/*     */     
/*  90 */     String queueName = this.connection.clientID + ":" + theConsumerInfo.getConsumerName() + ":" + theConsumerInfo.getConsumerNo();
/*     */     
/*  92 */     this.messageQueue = this.connection.getMemoryBoundedQueue(queueName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTarget(ActiveMQMessage message) {
/* 102 */     return message.isConsumerTarget(this.consumerInfo.getConsumerNo());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispatch(ActiveMQMessage message) {
/* 111 */     if (message.isConsumerTarget(this.consumerInfo.getConsumerNo())) {
/* 112 */       message.setConsumerId(this.consumerInfo.getConsumerId());
/*     */       try {
/* 114 */         if (this.sessionPool != null) {
/* 115 */           dispatchToSession(message);
/*     */         } else {
/* 117 */           dispatchToQueue(message);
/*     */         } 
/* 119 */       } catch (JMSException jmsEx) {
/* 120 */         this.connection.handleAsyncException(jmsEx);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void dispatchToQueue(ActiveMQMessage message) throws JMSException {
/* 130 */     this.messageQueue.enqueue((Packet)message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQMessage receive(long timeout) throws JMSException {
/*     */     try {
/* 139 */       ActiveMQMessage message = (ActiveMQMessage)this.messageQueue.dequeue(timeout);
/* 140 */       return message;
/*     */     }
/* 142 */     catch (InterruptedException ioe) {
/* 143 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void dispatchToSession(ActiveMQMessage message) throws JMSException {
/* 153 */     ServerSession serverSession = this.sessionPool.getServerSession();
/* 154 */     ActiveMQSession session = (ActiveMQSession)serverSession.getSession();
/*     */     
/* 156 */     session.dispatch(message);
/* 157 */     serverSession.start();
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
/*     */   public ServerSessionPool getServerSessionPool() throws JMSException {
/* 169 */     if (this.closed) {
/* 170 */       throw new IllegalStateException("The Connection Consumer is closed");
/*     */     }
/* 172 */     return this.sessionPool;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws JMSException {
/* 188 */     if (!this.closed) {
/* 189 */       this.closed = true;
/* 190 */       this.consumerInfo.setStarted(false);
/* 191 */       this.connection.asyncSendPacket((Packet)this.consumerInfo);
/* 192 */       this.connection.removeConnectionConsumer(this);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ActiveMQConnectionConsumer.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */