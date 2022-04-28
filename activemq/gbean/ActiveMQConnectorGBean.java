/*     */ package org.codehaus.activemq.gbean;
/*     */ 
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.geronimo.gbean.GAttributeInfo;
/*     */ import org.apache.geronimo.gbean.GBeanInfo;
/*     */ import org.apache.geronimo.gbean.GBeanInfoFactory;
/*     */ import org.apache.geronimo.gbean.GBeanLifecycle;
/*     */ import org.apache.geronimo.gbean.GConstructorInfo;
/*     */ import org.apache.geronimo.gbean.GReferenceInfo;
/*     */ import org.apache.geronimo.gbean.WaitingException;
/*     */ import org.codehaus.activemq.ActiveMQConnectionFactory;
/*     */ import org.codehaus.activemq.broker.BrokerConnector;
/*     */ import org.codehaus.activemq.broker.impl.BrokerConnectorImpl;
/*     */ import org.codehaus.activemq.message.DefaultWireFormat;
/*     */ import org.codehaus.activemq.message.WireFormat;
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
/*     */ public class ActiveMQConnectorGBean
/*     */   implements GBeanLifecycle
/*     */ {
/*  45 */   private Log log = LogFactory.getLog(getClass().getName());
/*     */   
/*     */   private BrokerConnector brokerConnector;
/*     */   private ActiveMQContainer container;
/*  49 */   private WireFormat wireFormat = (WireFormat)new DefaultWireFormat();
/*  50 */   private String url = "tcp://localhost:61616";
/*     */   
/*     */   public ActiveMQConnectorGBean(ActiveMQContainer container) {
/*  53 */     this.container = container;
/*     */   }
/*     */   public static final GBeanInfo GBEAN_INFO;
/*     */   public String getUrl() {
/*  57 */     return this.url;
/*     */   }
/*     */   
/*     */   public void setUrl(String url) {
/*  61 */     this.url = url;
/*     */   }
/*     */   
/*     */   public WireFormat getWireFormat() {
/*  65 */     return this.wireFormat;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWireFormat(WireFormat wireFormat) {
/*  70 */     if (wireFormat == null) {
/*  71 */       this.wireFormat = (WireFormat)new DefaultWireFormat();
/*     */     } else {
/*     */       
/*  74 */       this.wireFormat = wireFormat;
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void doStart() throws WaitingException, Exception {
/*  79 */     if (this.brokerConnector == null) {
/*  80 */       this.brokerConnector = createBrokerConnector();
/*  81 */       this.brokerConnector.start();
/*  82 */       ActiveMQConnectionFactory.registerBroker(this.url, this.brokerConnector);
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void doStop() throws WaitingException, Exception {
/*  87 */     if (this.brokerConnector != null) {
/*  88 */       ActiveMQConnectionFactory.unregisterBroker(this.url);
/*  89 */       BrokerConnector temp = this.brokerConnector;
/*  90 */       this.brokerConnector = null;
/*  91 */       temp.stop();
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void doFail() {
/*  96 */     if (this.brokerConnector != null) {
/*  97 */       BrokerConnector temp = this.brokerConnector;
/*  98 */       this.brokerConnector = null;
/*     */       try {
/* 100 */         temp.stop();
/*     */       }
/* 102 */       catch (JMSException e) {
/* 103 */         this.log.info("Caught while closing due to failure: " + e, (Throwable)e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected BrokerConnector createBrokerConnector() throws Exception {
/* 109 */     return (BrokerConnector)new BrokerConnectorImpl(this.container.getBrokerContainer(), this.url, this.wireFormat);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 115 */     GBeanInfoFactory infoFactory = new GBeanInfoFactory("ActiveMQ Message Broker Connector", ActiveMQConnectorGBean.class.getName());
/* 116 */     infoFactory.addAttribute(new GAttributeInfo("Url", String.class.getName(), true));
/* 117 */     infoFactory.addAttribute(new GAttributeInfo("WireFormat", WireFormat.class.getName(), true));
/* 118 */     infoFactory.addReference(new GReferenceInfo("ActiveMQContainer", ActiveMQContainer.class));
/* 119 */     infoFactory.setConstructor(new GConstructorInfo(new String[] { "ActiveMQContainer" }));
/* 120 */     GBEAN_INFO = infoFactory.getBeanInfo();
/*     */   }
/*     */   
/*     */   public static GBeanInfo getGBeanInfo() {
/* 124 */     return GBEAN_INFO;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\gbean\ActiveMQConnectorGBean.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */