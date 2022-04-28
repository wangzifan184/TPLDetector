/*     */ package org.codehaus.activemq.gbean;
/*     */ 
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.geronimo.gbean.GBeanInfo;
/*     */ import org.apache.geronimo.gbean.GBeanInfoFactory;
/*     */ import org.apache.geronimo.gbean.GBeanLifecycle;
/*     */ import org.apache.geronimo.gbean.WaitingException;
/*     */ import org.apache.geronimo.system.serverinfo.ServerInfo;
/*     */ import org.codehaus.activemq.broker.BrokerContainer;
/*     */ import org.codehaus.activemq.broker.BrokerContext;
/*     */ import org.codehaus.activemq.broker.impl.BrokerContainerImpl;
/*     */ import org.codehaus.activemq.store.PersistenceAdapter;
/*     */ import org.codehaus.activemq.store.jdbm.JdbmPersistenceAdapter;
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
/*     */ public class ActiveMQContainerGBean
/*     */   implements GBeanLifecycle, ActiveMQContainer
/*     */ {
/*  41 */   private Log log = LogFactory.getLog(getClass().getName());
/*     */   
/*     */   private final String brokerName;
/*     */   
/*  45 */   private BrokerContext context = BrokerContext.getInstance();
/*     */   private BrokerContainer container;
/*     */   private final ServerInfo serverInfo;
/*     */   private final String dataDirectory;
/*     */   public static final GBeanInfo GBEAN_INFO;
/*     */   static final boolean $assertionsDisabled;
/*     */   
/*     */   public ActiveMQContainerGBean() {
/*  53 */     this.serverInfo = null;
/*  54 */     this.brokerName = null;
/*  55 */     this.dataDirectory = "/var/activemq";
/*     */   }
/*     */   
/*     */   public ActiveMQContainerGBean(ServerInfo serverInfo, String brokerName, String dataDirectory) {
/*  59 */     assert serverInfo != null;
/*  60 */     assert brokerName != null;
/*  61 */     assert dataDirectory != null;
/*  62 */     this.serverInfo = serverInfo;
/*  63 */     this.brokerName = brokerName;
/*  64 */     this.dataDirectory = dataDirectory;
/*     */   }
/*     */   
/*     */   public synchronized BrokerContainer getBrokerContainer() {
/*  68 */     return this.container;
/*     */   }
/*     */   
/*     */   public synchronized void doStart() throws WaitingException, Exception {
/*  72 */     if (this.container == null) {
/*  73 */       this.container = createContainer();
/*  74 */       this.container.start();
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void doStop() throws WaitingException, Exception {
/*  79 */     if (this.container != null) {
/*  80 */       BrokerContainer temp = this.container;
/*  81 */       this.container = null;
/*  82 */       temp.stop();
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void doFail() {
/*  87 */     if (this.container != null) {
/*  88 */       BrokerContainer temp = this.container;
/*  89 */       this.container = null;
/*     */       try {
/*  91 */         temp.stop();
/*     */       }
/*  93 */       catch (JMSException e) {
/*  94 */         this.log.info("Caught while closing due to failure: " + e, (Throwable)e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected BrokerContainer createContainer() throws Exception {
/* 100 */     BrokerContainerImpl answer = new BrokerContainerImpl(this.brokerName, this.context);
/* 101 */     JdbmPersistenceAdapter pa = new JdbmPersistenceAdapter(this.serverInfo.resolve(this.dataDirectory));
/* 102 */     answer.setPersistenceAdapter((PersistenceAdapter)pa);
/* 103 */     return (BrokerContainer)answer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 109 */     GBeanInfoFactory infoFactory = new GBeanInfoFactory("ActiveMQ Message Broker", ActiveMQContainerGBean.class.getName());
/* 110 */     infoFactory.addReference("serverInfo", ServerInfo.class);
/* 111 */     infoFactory.addAttribute("brokerName", String.class, true);
/* 112 */     infoFactory.addAttribute("dataDirectory", String.class, true);
/* 113 */     infoFactory.addAttribute("BrokerContainer", BrokerContainer.class, false);
/* 114 */     infoFactory.setConstructor(new String[] { "serverInfo", "brokerName", "dataDirectory" });
/*     */     
/* 116 */     GBEAN_INFO = infoFactory.getBeanInfo();
/*     */   }
/*     */   
/*     */   public static GBeanInfo getGBeanInfo() {
/* 120 */     return GBEAN_INFO;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\gbean\ActiveMQContainerGBean.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */