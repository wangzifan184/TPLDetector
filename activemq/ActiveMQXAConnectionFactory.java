/*     */ package org.codehaus.activemq;
/*     */ 
/*     */ import javax.jms.Connection;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.QueueConnection;
/*     */ import javax.jms.TopicConnection;
/*     */ import javax.jms.XAConnection;
/*     */ import javax.jms.XAConnectionFactory;
/*     */ import javax.jms.XAQueueConnection;
/*     */ import javax.jms.XAQueueConnectionFactory;
/*     */ import javax.jms.XATopicConnection;
/*     */ import javax.jms.XATopicConnectionFactory;
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
/*     */ public class ActiveMQXAConnectionFactory
/*     */   extends ActiveMQConnectionFactory
/*     */   implements XAConnectionFactory, XAQueueConnectionFactory, XATopicConnectionFactory
/*     */ {
/*     */   public ActiveMQXAConnectionFactory() {}
/*     */   
/*     */   public ActiveMQXAConnectionFactory(String brokerURL) {
/*  61 */     super(brokerURL);
/*     */   }
/*     */   
/*     */   public ActiveMQXAConnectionFactory(String userName, String password, String brokerURL) {
/*  65 */     super(userName, password, brokerURL);
/*     */   }
/*     */   
/*     */   public XAConnection createXAConnection() throws JMSException {
/*  69 */     return createActiveMQXAConnection(this.userName, this.password);
/*     */   }
/*     */   
/*     */   public XAConnection createXAConnection(String userName, String password) throws JMSException {
/*  73 */     return createActiveMQXAConnection(userName, password);
/*     */   }
/*     */   
/*     */   public XAQueueConnection createXAQueueConnection() throws JMSException {
/*  77 */     return createActiveMQXAConnection(this.userName, this.password);
/*     */   }
/*     */   
/*     */   public XAQueueConnection createXAQueueConnection(String userName, String password) throws JMSException {
/*  81 */     return createActiveMQXAConnection(userName, password);
/*     */   }
/*     */   
/*     */   public XATopicConnection createXATopicConnection() throws JMSException {
/*  85 */     return createActiveMQXAConnection(this.userName, this.password);
/*     */   }
/*     */   
/*     */   public XATopicConnection createXATopicConnection(String userName, String password) throws JMSException {
/*  89 */     return createActiveMQXAConnection(userName, password);
/*     */   }
/*     */   
/*     */   public Connection createConnection() throws JMSException {
/*  93 */     return createActiveMQXAConnection(this.userName, this.password);
/*     */   }
/*     */   
/*     */   public Connection createConnection(String userName, String password) throws JMSException {
/*  97 */     return createActiveMQXAConnection(userName, password);
/*     */   }
/*     */   
/*     */   public QueueConnection createQueueConnection() throws JMSException {
/* 101 */     return createActiveMQXAConnection(this.userName, this.password);
/*     */   }
/*     */   
/*     */   public QueueConnection createQueueConnection(String userName, String password) throws JMSException {
/* 105 */     return createActiveMQXAConnection(userName, password);
/*     */   }
/*     */   
/*     */   public TopicConnection createTopicConnection() throws JMSException {
/* 109 */     return createActiveMQXAConnection(this.userName, this.password);
/*     */   }
/*     */   
/*     */   public TopicConnection createTopicConnection(String userName, String password) throws JMSException {
/* 113 */     return createActiveMQXAConnection(userName, password);
/*     */   }
/*     */   
/*     */   protected ActiveMQXAConnection createActiveMQXAConnection(String userName, String password) throws JMSException {
/* 117 */     ActiveMQXAConnection connection = new ActiveMQXAConnection(this, userName, password, createTransportChannel(this.brokerURL));
/* 118 */     if (this.clientID != null && this.clientID.length() > 0) {
/* 119 */       connection.setClientID(this.clientID);
/*     */     }
/* 121 */     return connection;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ActiveMQXAConnectionFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */