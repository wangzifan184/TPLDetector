/*     */ package org.codehaus.activemq.axis;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import javax.jms.ConnectionFactory;
/*     */ import javax.jms.QueueConnectionFactory;
/*     */ import javax.jms.TopicConnectionFactory;
/*     */ import org.apache.axis.components.jms.BeanVendorAdapter;
/*     */ import org.apache.axis.transport.jms.JMSURLHelper;
/*     */ import org.codehaus.activemq.ActiveMQConnectionFactory;
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
/*     */ public class ActiveMQVendorAdapter
/*     */   extends BeanVendorAdapter
/*     */ {
/*  36 */   protected static final String QCF_CLASS = ActiveMQConnectionFactory.class.getName();
/*  37 */   protected static final String TCF_CLASS = QCF_CLASS;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String BROKER_URL = "brokerURL";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String DEFAULT_USERNAME = "defaultUser";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String DEFAULT_PASSWORD = "defaultPassword";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String EMBEDDED_BROKER = "embeddedBroker";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QueueConnectionFactory getQueueConnectionFactory(HashMap properties) throws Exception {
/*  63 */     properties = (HashMap)properties.clone();
/*  64 */     properties.put("transport.jms.ConnectionFactoryClass", QCF_CLASS);
/*  65 */     return super.getQueueConnectionFactory(properties);
/*     */   }
/*     */ 
/*     */   
/*     */   public TopicConnectionFactory getTopicConnectionFactory(HashMap properties) throws Exception {
/*  70 */     properties = (HashMap)properties.clone();
/*  71 */     properties.put("transport.jms.ConnectionFactoryClass", TCF_CLASS);
/*  72 */     return super.getTopicConnectionFactory(properties);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addVendorConnectionFactoryProperties(JMSURLHelper jmsUrl, HashMap properties) {
/*  77 */     if (jmsUrl.getPropertyValue("brokerURL") != null) {
/*  78 */       properties.put("brokerURL", jmsUrl.getPropertyValue("brokerURL"));
/*     */     }
/*     */     
/*  81 */     if (jmsUrl.getPropertyValue("defaultUser") != null) {
/*  82 */       properties.put("defaultUser", jmsUrl.getPropertyValue("defaultUser"));
/*     */     }
/*  84 */     if (jmsUrl.getPropertyValue("defaultPassword") != null) {
/*  85 */       properties.put("defaultPassword", jmsUrl.getPropertyValue("defaultPassword"));
/*     */     }
/*  87 */     if (jmsUrl.getPropertyValue("embeddedBroker") != null) {
/*  88 */       properties.put("embeddedBroker", jmsUrl.getPropertyValue("embeddedBroker"));
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isMatchingConnectionFactory(ConnectionFactory connectionFactory, JMSURLHelper jmsURL, HashMap properties) {
/*  93 */     String brokerURL = null;
/*  94 */     boolean embeddedBroker = false;
/*     */     
/*  96 */     if (connectionFactory instanceof ActiveMQConnectionFactory) {
/*  97 */       ActiveMQConnectionFactory amqConnectionFactory = (ActiveMQConnectionFactory)connectionFactory;
/*     */ 
/*     */ 
/*     */       
/* 101 */       brokerURL = amqConnectionFactory.getBrokerURL();
/* 102 */       embeddedBroker = amqConnectionFactory.isUseEmbeddedBroker();
/*     */     } 
/*     */ 
/*     */     
/* 106 */     String propertyBrokerURL = (String)properties.get("brokerURL");
/* 107 */     if (!brokerURL.equals(propertyBrokerURL)) {
/* 108 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 112 */     String tmpEmbeddedBroker = (String)properties.get("embeddedBroker");
/* 113 */     boolean propertyEmbeddedBroker = false;
/* 114 */     if (tmpEmbeddedBroker != null) {
/* 115 */       propertyEmbeddedBroker = Boolean.valueOf(tmpEmbeddedBroker).booleanValue();
/*     */     }
/* 117 */     if (embeddedBroker != propertyEmbeddedBroker) {
/* 118 */       return false;
/*     */     }
/* 120 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\axis\ActiveMQVendorAdapter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */