/*     */ package org.codehaus.activemq.ra;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import javax.jms.Connection;
/*     */ import javax.jms.ConnectionConsumer;
/*     */ import javax.jms.ConnectionMetaData;
/*     */ import javax.jms.Destination;
/*     */ import javax.jms.ExceptionListener;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Queue;
/*     */ import javax.jms.QueueConnection;
/*     */ import javax.jms.QueueSession;
/*     */ import javax.jms.ServerSessionPool;
/*     */ import javax.jms.Session;
/*     */ import javax.jms.Topic;
/*     */ import javax.jms.TopicConnection;
/*     */ import javax.jms.TopicSession;
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
/*     */ public class JMSConnectionProxy
/*     */   implements Connection, QueueConnection, TopicConnection
/*     */ {
/*     */   private ActiveMQManagedConnection managedConnection;
/*  47 */   private ArrayList sessions = new ArrayList();
/*     */   
/*     */   public JMSConnectionProxy(ActiveMQManagedConnection managedConnection) {
/*  50 */     this.managedConnection = managedConnection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws JMSException {
/*  60 */     this.managedConnection.proxyClosedEvent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cleanup() {
/*  67 */     this.managedConnection = null;
/*  68 */     for (Iterator iter = this.sessions.iterator(); iter.hasNext(); ) {
/*  69 */       JMSSessionProxy p = iter.next();
/*  70 */       p.cleanup();
/*  71 */       iter.remove();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Connection getConnection() throws JMSException {
/*  79 */     if (this.managedConnection == null) {
/*  80 */       throw new JMSException("Connection is closed.");
/*     */     }
/*  82 */     return this.managedConnection.getPhysicalConnection();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Session createSession(boolean transacted, int acknowledgeMode) throws JMSException {
/*  93 */     return createSessionProxy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JMSSessionProxy createSessionProxy() {
/* 100 */     JMSSessionProxy p = new JMSSessionProxy(this.managedConnection);
/* 101 */     this.sessions.add(p);
/* 102 */     return p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QueueSession createQueueSession(boolean transacted, int acknowledgeMode) throws JMSException {
/* 113 */     return createSessionProxy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TopicSession createTopicSession(boolean transacted, int acknowledgeMode) throws JMSException {
/* 124 */     return createSessionProxy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClientID() throws JMSException {
/* 132 */     return getConnection().getClientID();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExceptionListener getExceptionListener() throws JMSException {
/* 140 */     return getConnection().getExceptionListener();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConnectionMetaData getMetaData() throws JMSException {
/* 148 */     return getConnection().getMetaData();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClientID(String clientID) throws JMSException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExceptionListener(ExceptionListener listener) throws JMSException {
/* 165 */     getConnection().setExceptionListener(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() throws JMSException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() throws JMSException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConnectionConsumer createConnectionConsumer(Queue queue, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
/* 194 */     throw new JMSException("Not Supported.");
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
/*     */   public ConnectionConsumer createConnectionConsumer(Topic topic, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
/* 208 */     throw new JMSException("Not Supported.");
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
/*     */   public ConnectionConsumer createConnectionConsumer(Destination destination, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
/* 222 */     throw new JMSException("Not Supported.");
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
/*     */   public ConnectionConsumer createDurableConnectionConsumer(Topic topic, String subscriptionName, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
/* 237 */     throw new JMSException("Not Supported.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQManagedConnection getManagedConnection() {
/* 244 */     return this.managedConnection;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ra\JMSConnectionProxy.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */