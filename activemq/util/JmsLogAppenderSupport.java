/*     */ package org.codehaus.activemq.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.jms.Connection;
/*     */ import javax.jms.Destination;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Message;
/*     */ import javax.jms.MessageProducer;
/*     */ import javax.jms.ObjectMessage;
/*     */ import javax.jms.Session;
/*     */ import javax.jms.TextMessage;
/*     */ import javax.naming.NamingException;
/*     */ import org.apache.log4j.AppenderSkeleton;
/*     */ import org.apache.log4j.spi.LoggingEvent;
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
/*     */ public abstract class JmsLogAppenderSupport
/*     */   extends AppenderSkeleton
/*     */ {
/*     */   public static final int JMS_PUBLISH_ERROR_CODE = 61616;
/*     */   private Connection connection;
/*     */   private Session session;
/*     */   private MessageProducer producer;
/*     */   private boolean allowTextMessages = true;
/*  48 */   private String subjectPrefix = "log4j.";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection() throws JMSException, NamingException {
/*  54 */     if (this.connection == null) {
/*  55 */       this.connection = createConnection();
/*     */     }
/*  57 */     return this.connection;
/*     */   }
/*     */   
/*     */   public void setConnection(Connection connection) {
/*  61 */     this.connection = connection;
/*     */   }
/*     */   
/*     */   public Session getSession() throws JMSException, NamingException {
/*  65 */     if (this.session == null) {
/*  66 */       this.session = createSession();
/*     */     }
/*  68 */     return this.session;
/*     */   }
/*     */   
/*     */   public void setSession(Session session) {
/*  72 */     this.session = session;
/*     */   }
/*     */   
/*     */   public MessageProducer getProducer() throws JMSException, NamingException {
/*  76 */     if (this.producer == null) {
/*  77 */       this.producer = createProducer();
/*     */     }
/*  79 */     return this.producer;
/*     */   }
/*     */   
/*     */   public void setProducer(MessageProducer producer) {
/*  83 */     this.producer = producer;
/*     */   }
/*     */   
/*     */   public void close() {
/*  87 */     List errors = new ArrayList();
/*  88 */     if (this.producer != null) {
/*     */       try {
/*  90 */         this.producer.close();
/*     */       }
/*  92 */       catch (JMSException e) {
/*  93 */         errors.add(e);
/*     */       } 
/*     */     }
/*  96 */     if (this.session != null) {
/*     */       try {
/*  98 */         this.session.close();
/*     */       }
/* 100 */       catch (JMSException e) {
/* 101 */         errors.add(e);
/*     */       } 
/*     */     }
/* 104 */     if (this.connection != null) {
/*     */       try {
/* 106 */         this.connection.close();
/*     */       }
/* 108 */       catch (JMSException e) {
/* 109 */         errors.add(e);
/*     */       } 
/*     */     }
/* 112 */     for (Iterator iter = errors.iterator(); iter.hasNext(); ) {
/* 113 */       JMSException e = iter.next();
/* 114 */       getErrorHandler().error("Error closing JMS resources: " + e, (Exception)e, 61616);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean requiresLayout() {
/* 119 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void activateOptions() {
/*     */     try {
/* 125 */       getProducer();
/*     */     }
/* 127 */     catch (Exception e) {
/* 128 */       getErrorHandler().error("Could not create JMS resources: " + e, e, 61616);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Connection createConnection() throws JMSException, NamingException;
/*     */ 
/*     */   
/*     */   protected Session createSession() throws JMSException, NamingException {
/* 138 */     return getConnection().createSession(false, 1);
/*     */   }
/*     */   
/*     */   protected MessageProducer createProducer() throws JMSException, NamingException {
/* 142 */     return getSession().createProducer(null);
/*     */   }
/*     */   
/*     */   protected void append(LoggingEvent event) {
/* 146 */     System.out.println("#### Calling append with event: " + event);
/*     */     try {
/* 148 */       Message message = createMessage(event);
/* 149 */       Destination destination = getDestination(event);
/* 150 */       getProducer().send(destination, message);
/*     */     }
/* 152 */     catch (Exception e) {
/* 153 */       getErrorHandler().error("Could not send message due to: " + e, e, 61616, event);
/*     */     } 
/*     */   }
/*     */   protected Message createMessage(LoggingEvent event) throws JMSException, NamingException {
/*     */     ObjectMessage objectMessage;
/* 158 */     Message answer = null;
/* 159 */     Object value = event.getMessage();
/* 160 */     if (this.allowTextMessages && value instanceof String) {
/* 161 */       TextMessage textMessage = getSession().createTextMessage((String)value);
/*     */     } else {
/*     */       
/* 164 */       objectMessage = getSession().createObjectMessage((Serializable)value);
/*     */     } 
/* 166 */     objectMessage.setStringProperty("level", event.getLevel().toString());
/* 167 */     objectMessage.setIntProperty("levelInt", event.getLevel().toInt());
/* 168 */     objectMessage.setStringProperty("threadName", event.getThreadName());
/* 169 */     return (Message)objectMessage;
/*     */   }
/*     */   
/*     */   protected Destination getDestination(LoggingEvent event) throws JMSException, NamingException {
/* 173 */     String name = this.subjectPrefix + event.getLoggerName();
/* 174 */     return (Destination)getSession().createTopic(name);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activem\\util\JmsLogAppenderSupport.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */