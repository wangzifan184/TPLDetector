/*     */ package org.codehaus.activemq.ra;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import javax.jms.Connection;
/*     */ import javax.jms.ConnectionFactory;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.QueueConnection;
/*     */ import javax.jms.QueueConnectionFactory;
/*     */ import javax.jms.TopicConnection;
/*     */ import javax.jms.TopicConnectionFactory;
/*     */ import javax.naming.Reference;
/*     */ import javax.resource.Referenceable;
/*     */ import javax.resource.ResourceException;
/*     */ import javax.resource.spi.ConnectionManager;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
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
/*     */ public class ActiveMQConnectionFactory
/*     */   implements ConnectionFactory, QueueConnectionFactory, TopicConnectionFactory, Referenceable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -5754338187296859149L;
/*  44 */   private static final Log log = LogFactory.getLog(ActiveMQConnectionFactory.class);
/*     */ 
/*     */   
/*     */   private transient ConnectionManager manager;
/*     */   
/*     */   private transient ActiveMQManagedConnectionFactory factory;
/*     */   
/*     */   private Reference reference;
/*     */   
/*     */   private final ActiveMQConnectionRequestInfo info;
/*     */ 
/*     */   
/*     */   public ActiveMQConnectionFactory(ActiveMQManagedConnectionFactory factory, ConnectionManager manager, ActiveMQConnectionRequestInfo info) {
/*  57 */     this.factory = factory;
/*  58 */     this.manager = manager;
/*  59 */     this.info = info;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection createConnection() throws JMSException {
/*  66 */     return createConnection(this.info.copy());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection createConnection(String userName, String password) throws JMSException {
/*  73 */     ActiveMQConnectionRequestInfo i = this.info.copy();
/*  74 */     i.setUserName(userName);
/*  75 */     i.setPassword(password);
/*  76 */     return createConnection(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Connection createConnection(ActiveMQConnectionRequestInfo info) throws JMSException {
/*     */     try {
/*  86 */       return (Connection)this.manager.allocateConnection(this.factory, info);
/*     */     }
/*  88 */     catch (ResourceException e) {
/*     */       
/*  90 */       if (e.getCause() instanceof JMSException) {
/*  91 */         throw (JMSException)e.getCause();
/*     */       }
/*  93 */       log.debug("Connection could not be created:", (Throwable)e);
/*  94 */       throw new JMSException(e.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reference getReference() {
/* 102 */     return this.reference;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReference(Reference reference) {
/* 109 */     this.reference = reference;
/*     */   }
/*     */   
/*     */   public QueueConnection createQueueConnection() throws JMSException {
/* 113 */     return (QueueConnection)createConnection();
/*     */   }
/*     */   
/*     */   public QueueConnection createQueueConnection(String userName, String password) throws JMSException {
/* 117 */     return (QueueConnection)createConnection(userName, password);
/*     */   }
/*     */   
/*     */   public TopicConnection createTopicConnection() throws JMSException {
/* 121 */     return (TopicConnection)createConnection();
/*     */   }
/*     */   
/*     */   public TopicConnection createTopicConnection(String userName, String password) throws JMSException {
/* 125 */     return (TopicConnection)createConnection(userName, password);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ra\ActiveMQConnectionFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */