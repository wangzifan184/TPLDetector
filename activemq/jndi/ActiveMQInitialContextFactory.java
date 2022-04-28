/*     */ package org.codehaus.activemq.jndi;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Queue;
/*     */ import javax.jms.Topic;
/*     */ import javax.naming.CommunicationException;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.spi.InitialContextFactory;
/*     */ import org.codehaus.activemq.ActiveMQConnectionFactory;
/*     */ import org.codehaus.activemq.broker.Broker;
/*     */ import org.codehaus.activemq.message.ActiveMQQueue;
/*     */ import org.codehaus.activemq.message.ActiveMQTopic;
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
/*     */ public class ActiveMQInitialContextFactory
/*     */   implements InitialContextFactory
/*     */ {
/*  48 */   private String queuePrefix = "queue.";
/*  49 */   private String topicPrefix = "topic.";
/*     */ 
/*     */   
/*     */   public Context getInitialContext(Hashtable environment) throws NamingException {
/*  53 */     ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
/*  54 */     ActiveMQConnectionFactory factory = createConnectionFactory(environment);
/*  55 */     concurrentHashMap.put("ConnectionFactory", factory);
/*  56 */     concurrentHashMap.put("QueueConnectionFactory", factory);
/*  57 */     concurrentHashMap.put("TopicConnectionFactory", factory);
/*     */     
/*  59 */     createQueues((Map)concurrentHashMap, environment);
/*  60 */     createTopics((Map)concurrentHashMap, environment);
/*     */     
/*     */     try {
/*  63 */       Broker broker = factory.getEmbeddedBroker();
/*  64 */       if (broker != null) {
/*  65 */         concurrentHashMap.put("destinations", broker.getDestinationContext(environment));
/*     */       }
/*     */     }
/*  68 */     catch (JMSException e) {
/*  69 */       CommunicationException exception = new CommunicationException("Failed to access embedded broker: " + e);
/*  70 */       exception.setRootCause((Throwable)e);
/*  71 */       throw exception;
/*     */     } 
/*  73 */     return new ReadOnlyContext(environment, (Map)concurrentHashMap);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTopicPrefix() {
/*  79 */     return this.topicPrefix;
/*     */   }
/*     */   
/*     */   public void setTopicPrefix(String topicPrefix) {
/*  83 */     this.topicPrefix = topicPrefix;
/*     */   }
/*     */   
/*     */   public String getQueuePrefix() {
/*  87 */     return this.queuePrefix;
/*     */   }
/*     */   
/*     */   public void setQueuePrefix(String queuePrefix) {
/*  91 */     this.queuePrefix = queuePrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void createQueues(Map data, Hashtable environment) {
/*  97 */     for (Iterator iter = environment.entrySet().iterator(); iter.hasNext(); ) {
/*  98 */       Map.Entry entry = iter.next();
/*  99 */       String key = entry.getKey().toString();
/* 100 */       if (key.startsWith(this.queuePrefix)) {
/* 101 */         String jndiName = key.substring(this.queuePrefix.length());
/* 102 */         data.put(jndiName, createQueue(entry.getValue().toString()));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void createTopics(Map data, Hashtable environment) {
/* 108 */     for (Iterator iter = environment.entrySet().iterator(); iter.hasNext(); ) {
/* 109 */       Map.Entry entry = iter.next();
/* 110 */       String key = entry.getKey().toString();
/* 111 */       if (key.startsWith(this.topicPrefix)) {
/* 112 */         String jndiName = key.substring(this.topicPrefix.length());
/* 113 */         data.put(jndiName, createTopic(entry.getValue().toString()));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Queue createQueue(String name) {
/* 122 */     return (Queue)new ActiveMQQueue(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Topic createTopic(String name) {
/* 129 */     return (Topic)new ActiveMQTopic(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ActiveMQConnectionFactory createConnectionFactory(Hashtable environment) {
/* 136 */     ActiveMQConnectionFactory answer = new ActiveMQConnectionFactory();
/* 137 */     Properties properties = new Properties();
/* 138 */     properties.putAll(environment);
/* 139 */     answer.setProperties(properties);
/* 140 */     return answer;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\jndi\ActiveMQInitialContextFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */