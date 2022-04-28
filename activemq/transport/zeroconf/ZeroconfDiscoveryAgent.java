/*     */ package org.codehaus.activemq.transport.zeroconf;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import javax.jmdns.JmDNS;
/*     */ import javax.jmdns.ServiceInfo;
/*     */ import javax.jmdns.ServiceListener;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.ConfigurationException;
/*     */ import org.codehaus.activemq.NotStartedException;
/*     */ import org.codehaus.activemq.transport.DiscoveryAgent;
/*     */ import org.codehaus.activemq.transport.DiscoveryAgentSupport;
/*     */ import org.codehaus.activemq.transport.DiscoveryEvent;
/*     */ import org.codehaus.activemq.util.JMSExceptionHelper;
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
/*     */ public class ZeroconfDiscoveryAgent
/*     */   extends DiscoveryAgentSupport
/*     */   implements ServiceListener
/*     */ {
/*  49 */   private static final Log log = LogFactory.getLog(ZeroconfDiscoveryAgent.class);
/*     */   
/*     */   private JmDNS jmdns;
/*     */   private InetAddress localAddress;
/*     */   private String localhost;
/*     */   private String type;
/*  55 */   private int weight = 0;
/*  56 */   private int priority = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() throws JMSException {
/*  61 */     if (this.type == null) {
/*  62 */       throw new ConfigurationException("You must specify a type of service to discover");
/*     */     }
/*  64 */     if (!this.type.endsWith(".")) {
/*  65 */       log.warn("The type '" + this.type + "' should end with '.' to be a valid Zeroconf type");
/*  66 */       this.type += ".";
/*     */     } 
/*     */     try {
/*  69 */       if (this.jmdns == null) {
/*  70 */         this.jmdns = createJmDNS();
/*     */       }
/*  72 */       if (getDiscoveryListener() != null) {
/*  73 */         log.info("Discovering service of type: " + this.type);
/*  74 */         this.jmdns.addServiceListener(this.type, this);
/*     */       }
/*     */     
/*  77 */     } catch (IOException e) {
/*  78 */       JMSExceptionHelper.newJMSException("Failed to start JmDNS service: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stop() throws JMSException {
/*  83 */     this.jmdns.unregisterAllServices();
/*  84 */     this.jmdns.close();
/*     */   }
/*     */   
/*     */   public void registerService(String name, Map details) throws JMSException {
/*  88 */     if (this.jmdns == null) {
/*  89 */       throw new NotStartedException();
/*     */     }
/*     */     try {
/*  92 */       this.jmdns.registerService(createServiceInfo(name, details));
/*     */     }
/*  94 */     catch (IOException e) {
/*  95 */       JMSExceptionHelper.newJMSException("Could not register service: " + name + ". Reason: " + e, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addService(JmDNS jmDNS, String type, String name) {
/* 103 */     if (log.isDebugEnabled()) {
/* 104 */       log.debug("addService with type: " + type + " name: " + name);
/*     */     }
/* 106 */     jmDNS.requestServiceInfo(type, name);
/*     */   }
/*     */   
/*     */   public void removeService(JmDNS jmDNS, String type, String name) {
/* 110 */     if (log.isDebugEnabled()) {
/* 111 */       log.debug("removeService with type: " + type + " name: " + name);
/*     */     }
/* 113 */     DiscoveryEvent event = new DiscoveryEvent((DiscoveryAgent)this, name);
/* 114 */     getDiscoveryListener().removeService(event);
/*     */   }
/*     */   
/*     */   public void resolveService(JmDNS jmDNS, String type, String name, ServiceInfo serviceInfo) {
/* 118 */     if (log.isDebugEnabled()) {
/* 119 */       log.debug("removeService with type: " + type + " name: " + name + " info: " + serviceInfo);
/*     */     }
/*     */     
/* 122 */     Map map = new HashMap();
/* 123 */     if (serviceInfo != null) {
/* 124 */       Enumeration iter = serviceInfo.getPropertyNames();
/* 125 */       while (iter.hasMoreElements()) {
/* 126 */         String key = iter.nextElement();
/* 127 */         String value = serviceInfo.getPropertyString(key);
/* 128 */         map.put(key, value);
/*     */       } 
/*     */     } 
/* 131 */     DiscoveryEvent event = new DiscoveryEvent((DiscoveryAgent)this, name, map);
/* 132 */     getDiscoveryListener().addService(event);
/*     */   }
/*     */   
/*     */   public String getType() {
/* 136 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/* 140 */     this.type = type;
/*     */   }
/*     */   
/*     */   public int getPriority() {
/* 144 */     return this.priority;
/*     */   }
/*     */   
/*     */   public void setPriority(int priority) {
/* 148 */     this.priority = priority;
/*     */   }
/*     */   
/*     */   public int getWeight() {
/* 152 */     return this.weight;
/*     */   }
/*     */   
/*     */   public void setWeight(int weight) {
/* 156 */     this.weight = weight;
/*     */   }
/*     */   
/*     */   public JmDNS getJmdns() {
/* 160 */     return this.jmdns;
/*     */   }
/*     */   
/*     */   public void setJmdns(JmDNS jmdns) {
/* 164 */     this.jmdns = jmdns;
/*     */   }
/*     */ 
/*     */   
/*     */   public InetAddress getLocalAddress() throws UnknownHostException {
/* 169 */     if (this.localAddress == null) {
/* 170 */       this.localAddress = createLocalAddress();
/*     */     }
/* 172 */     return this.localAddress;
/*     */   }
/*     */   
/*     */   public void setLocalAddress(InetAddress localAddress) {
/* 176 */     this.localAddress = localAddress;
/*     */   }
/*     */   
/*     */   public String getLocalhost() {
/* 180 */     return this.localhost;
/*     */   }
/*     */   
/*     */   public void setLocalhost(String localhost) {
/* 184 */     this.localhost = localhost;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ServiceInfo createServiceInfo(String name, Map map) {
/* 190 */     name = name + "." + this.type;
/* 191 */     int port = MapHelper.getInt(map, "port", 0);
/*     */     
/* 193 */     if (log.isDebugEnabled()) {
/* 194 */       log.debug("Registering service type: " + this.type + " name: " + name + " details: " + map);
/*     */     }
/* 196 */     return new ServiceInfo(this.type, name, port, this.weight, this.priority, new Hashtable(map));
/*     */   }
/*     */   
/*     */   protected JmDNS createJmDNS() throws IOException {
/* 200 */     return new JmDNS(getLocalAddress());
/*     */   }
/*     */   
/*     */   protected InetAddress createLocalAddress() throws UnknownHostException {
/* 204 */     if (this.localhost != null) {
/* 205 */       return InetAddress.getByName(this.localhost);
/*     */     }
/* 207 */     return InetAddress.getLocalHost();
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\zeroconf\ZeroconfDiscoveryAgent.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */