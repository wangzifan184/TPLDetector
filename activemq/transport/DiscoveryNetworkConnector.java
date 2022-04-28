/*     */ package org.codehaus.activemq.transport;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.ConfigurationException;
/*     */ import org.codehaus.activemq.broker.BrokerContainer;
/*     */ import org.codehaus.activemq.util.MapHelper;
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
/*     */ public class DiscoveryNetworkConnector
/*     */   extends NetworkConnector
/*     */   implements DiscoveryListener
/*     */ {
/*  37 */   private static final Log log = LogFactory.getLog(DiscoveryNetworkConnector.class);
/*     */   
/*     */   private DiscoveryAgent discoveryAgent;
/*  40 */   private Map localDetails = new HashMap();
/*  41 */   private Map channelMap = new HashMap();
/*     */   private String remoteUserName;
/*     */   private String remotePassword;
/*     */   
/*     */   public DiscoveryNetworkConnector(BrokerContainer brokerContainer) {
/*  46 */     super(brokerContainer);
/*     */   }
/*     */   
/*     */   public void start() throws JMSException {
/*  50 */     DiscoveryAgent discoveryAgent = getBrokerContainer().getDiscoveryAgent();
/*  51 */     if (discoveryAgent == null) {
/*  52 */       throw new ConfigurationException("Must be configured with a discoveryAgent property");
/*     */     }
/*     */     
/*  55 */     discoveryAgent.setDiscoveryListener(this);
/*     */     
/*  57 */     super.start();
/*     */   }
/*     */   
/*     */   public void addService(DiscoveryEvent event) {
/*  61 */     Map details = event.getServiceDetails();
/*  62 */     if (!getLocalBrokerName().equals(details.get("brokerName"))) {
/*  63 */       String url = MapHelper.getString(details, "connectURL");
/*  64 */       if (url != null) {
/*  65 */         addChannel(url, details);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void removeService(DiscoveryEvent event) {
/*  71 */     Map details = event.getServiceDetails();
/*  72 */     if (!getLocalBrokerName().equals(details.get("brokerName"))) {
/*  73 */       String url = MapHelper.getString(details, "connectURL");
/*  74 */       if (url != null) {
/*  75 */         removeChannel(url, details);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map getLocalDetails() {
/*  83 */     return this.localDetails;
/*     */   }
/*     */   
/*     */   public void setLocalDetails(Map localDetails) {
/*  87 */     this.localDetails = localDetails;
/*     */   }
/*     */   
/*     */   public String getRemotePassword() {
/*  91 */     return this.remotePassword;
/*     */   }
/*     */   
/*     */   public void setRemotePassword(String remotePassword) {
/*  95 */     this.remotePassword = remotePassword;
/*     */   }
/*     */   
/*     */   public String getRemoteUserName() {
/*  99 */     return this.remoteUserName;
/*     */   }
/*     */   
/*     */   public void setRemoteUserName(String remoteUserName) {
/* 103 */     this.remoteUserName = remoteUserName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void addChannel(String url, Map details) {
/* 109 */     NetworkChannel channel = (NetworkChannel)this.channelMap.get(url);
/* 110 */     if (channel == null) {
/* 111 */       channel = createNetworkChannel(url, details);
/* 112 */       channel.setUri(url);
/*     */       
/* 114 */       log.info(getLocalBrokerName() + ": Adding new NeworkChannel on: " + url + " with details: " + details);
/*     */       
/*     */       try {
/* 117 */         channel.start();
/* 118 */         this.channelMap.put(url, channel);
/*     */       }
/* 120 */       catch (JMSException e) {
/* 121 */         log.warn(getLocalBrokerName() + ": Could not start channel: " + channel + ". Reason: " + e, (Throwable)e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected synchronized void removeChannel(String url, Map details) {
/* 127 */     NetworkChannel channel = (NetworkChannel)this.channelMap.remove(url);
/* 128 */     if (channel != null) {
/* 129 */       log.info(getLocalBrokerName() + ": Removing NeworkChannel: " + channel);
/*     */       try {
/* 131 */         channel.stop();
/*     */       }
/* 133 */       catch (JMSException e) {
/* 134 */         log.info("Failed to stop channel: " + channel + ". Reason: " + e, (Throwable)e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected NetworkChannel createNetworkChannel(String url, Map details) {
/* 140 */     NetworkChannel answer = new NetworkChannel(getBrokerContainer(), url);
/* 141 */     answer.setRemoteUserName(getRemoteUserName());
/* 142 */     answer.setRemotePassword(getRemotePassword());
/* 143 */     return answer;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getLocalBrokerName() {
/* 148 */     return getBrokerContainer().getBroker().getBrokerName();
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\DiscoveryNetworkConnector.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */