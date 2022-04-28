/*     */ package org.codehaus.activemq.transport;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.ConfigurationException;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.transport.reliable.ReliableTransportChannel;
/*     */ import org.codehaus.activemq.util.Callback;
/*     */ import org.codehaus.activemq.util.ExceptionTemplate;
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
/*     */ public class DiscoveryTransportChannel
/*     */   extends ReliableTransportChannel
/*     */   implements DiscoveryListener
/*     */ {
/*  42 */   private static final Log log = LogFactory.getLog(DiscoveryTransportChannel.class);
/*     */   
/*     */   private DiscoveryAgent discoveryAgent;
/*     */   
/*     */   private String remoteUserName;
/*     */   
/*     */   private String remotePassword;
/*     */   
/*     */   public DiscoveryTransportChannel(WireFormat wireFormat, DiscoveryAgent discoveryAgent) {
/*  51 */     super(wireFormat);
/*  52 */     this.discoveryAgent = discoveryAgent;
/*     */   }
/*     */   
/*     */   public void start() throws JMSException {
/*  56 */     if (this.discoveryAgent == null) {
/*  57 */       throw new ConfigurationException("Must be configured with a discoveryAgent property");
/*     */     }
/*     */ 
/*     */     
/*  61 */     this.discoveryAgent.setDiscoveryListener(this);
/*  62 */     this.discoveryAgent.start();
/*     */     
/*  64 */     super.start();
/*     */   }
/*     */   
/*     */   public void stop() {
/*  68 */     ExceptionTemplate template = new ExceptionTemplate();
/*  69 */     template.run(new Callback() {
/*     */           public void execute() throws Throwable {
/*  71 */             DiscoveryTransportChannel.this.discoveryAgent.stop();
/*     */           } private final DiscoveryTransportChannel this$0;
/*     */         });
/*  74 */     template.run(new Callback() {
/*     */           public void execute() throws Throwable {
/*  76 */             DiscoveryTransportChannel.this.stop();
/*     */           } private final DiscoveryTransportChannel this$0;
/*     */         });
/*  79 */     Throwable e = template.getFirstException();
/*  80 */     log.warn("Failed to stop the transport channel cleanly due to: " + e, e);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void addService(DiscoveryEvent event) {
/*  85 */     Map details = event.getServiceDetails();
/*  86 */     String url = MapHelper.getString(details, "connectURL");
/*  87 */     if (url != null) {
/*     */       try {
/*  89 */         URI uri = new URI(url);
/*  90 */         List urlList = getUris();
/*  91 */         if (!urlList.contains(uri)) {
/*  92 */           log.info("Adding new broker connection URL: " + uri + " with details: " + details);
/*     */           
/*  94 */           urlList.add(uri);
/*     */         }
/*     */       
/*  97 */       } catch (URISyntaxException e) {
/*  98 */         log.warn("Could not connect to remote URI: " + url + " due to bad URI syntax: " + e, e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void removeService(DiscoveryEvent event) {
/* 104 */     Map details = event.getServiceDetails();
/* 105 */     String url = MapHelper.getString(details, "connectURL");
/* 106 */     if (url != null) {
/*     */       try {
/* 108 */         URI uri = new URI(url);
/* 109 */         synchronized (this) {
/* 110 */           List urlList = getUris();
/* 111 */           if (urlList.remove(uri)) {
/* 112 */             log.info("Removing broker connection URL: " + uri);
/*     */           }
/*     */         }
/*     */       
/* 116 */       } catch (URISyntaxException e) {
/* 117 */         log.warn("Could not remove remote URI: " + url + " due to bad URI syntax: " + e, e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DiscoveryAgent getDiscoveryAgent() {
/* 125 */     return this.discoveryAgent;
/*     */   }
/*     */   
/*     */   public void setDiscoveryAgent(DiscoveryAgent discoveryAgent) {
/* 129 */     this.discoveryAgent = discoveryAgent;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRemotePassword() {
/* 134 */     return this.remotePassword;
/*     */   }
/*     */   
/*     */   public void setRemotePassword(String remotePassword) {
/* 138 */     this.remotePassword = remotePassword;
/*     */   }
/*     */   
/*     */   public String getRemoteUserName() {
/* 142 */     return this.remoteUserName;
/*     */   }
/*     */   
/*     */   public void setRemoteUserName(String remoteUserName) {
/* 146 */     this.remoteUserName = remoteUserName;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\DiscoveryTransportChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */