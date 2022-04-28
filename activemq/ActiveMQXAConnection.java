/*     */ package org.codehaus.activemq;
/*     */ 
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.QueueSession;
/*     */ import javax.jms.Session;
/*     */ import javax.jms.TopicSession;
/*     */ import javax.jms.XAConnection;
/*     */ import javax.jms.XAQueueConnection;
/*     */ import javax.jms.XAQueueSession;
/*     */ import javax.jms.XASession;
/*     */ import javax.jms.XATopicConnection;
/*     */ import javax.jms.XATopicSession;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.ResponseReceipt;
/*     */ import org.codehaus.activemq.message.XATransactionInfo;
/*     */ import org.codehaus.activemq.transport.TransportChannel;
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
/*     */ public class ActiveMQXAConnection
/*     */   extends ActiveMQConnection
/*     */   implements XATopicConnection, XAQueueConnection, XAConnection
/*     */ {
/*  50 */   private static final Log log = LogFactory.getLog(ActiveMQXAConnection.class);
/*     */   private final String resourceManagerId;
/*     */   
/*     */   public ActiveMQXAConnection(ActiveMQConnectionFactory factory, String theUserName, String thePassword, TransportChannel transportChannel) throws JMSException {
/*  54 */     super(factory, theUserName, thePassword, transportChannel);
/*  55 */     this.resourceManagerId = determineResourceManagerId();
/*     */   }
/*     */   static final boolean $assertionsDisabled;
/*     */   public ActiveMQXAConnection(ActiveMQConnectionFactory factory, String theUserName, String thePassword) throws JMSException {
/*  59 */     super(factory, theUserName, thePassword);
/*  60 */     this.resourceManagerId = determineResourceManagerId();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String determineResourceManagerId() throws JMSException {
/*  68 */     XATransactionInfo info = new XATransactionInfo();
/*  69 */     info.setId(this.packetIdGenerator.generateId());
/*  70 */     info.setType(113);
/*     */     
/*  72 */     ResponseReceipt receipt = (ResponseReceipt)syncSendRequest((Packet)info);
/*  73 */     String rmId = (String)receipt.getResult();
/*  74 */     assert rmId != null;
/*  75 */     return rmId;
/*     */   }
/*     */   
/*     */   public XASession createXASession() throws JMSException {
/*  79 */     return createActiveMQXASession();
/*     */   }
/*     */   
/*     */   public QueueSession createQueueSession(boolean transacted, int acknowledgeMode) throws JMSException {
/*  83 */     return createActiveMQXASession();
/*     */   }
/*     */   
/*     */   public TopicSession createTopicSession(boolean transacted, int acknowledgeMode) throws JMSException {
/*  87 */     return createActiveMQXASession();
/*     */   }
/*     */   
/*     */   public Session createSession(boolean transacted, int acknowledgeMode) throws JMSException {
/*  91 */     return createActiveMQXASession();
/*     */   }
/*     */   
/*     */   public XATopicSession createXATopicSession() throws JMSException {
/*  95 */     return createActiveMQXASession();
/*     */   }
/*     */   
/*     */   public XAQueueSession createXAQueueSession() throws JMSException {
/*  99 */     return createActiveMQXASession();
/*     */   }
/*     */   
/*     */   protected ActiveMQXASession createActiveMQXASession() throws JMSException {
/* 103 */     checkClosed();
/* 104 */     return new ActiveMQXASession(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getResourceManagerId() {
/* 111 */     return this.resourceManagerId;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ActiveMQXAConnection.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */