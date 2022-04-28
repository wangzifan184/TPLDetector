/*     */ package org.codehaus.activemq.transport;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.ActiveMQConnection;
/*     */ import org.codehaus.activemq.ActiveMQConnectionFactory;
/*     */ import org.codehaus.activemq.broker.BrokerClient;
/*     */ import org.codehaus.activemq.broker.BrokerContainer;
/*     */ import org.codehaus.activemq.broker.ConsumerInfoListener;
/*     */ import org.codehaus.activemq.message.ActiveMQDestination;
/*     */ import org.codehaus.activemq.message.BrokerInfo;
/*     */ import org.codehaus.activemq.message.ConsumerInfo;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.Receipt;
/*     */ import org.codehaus.activemq.service.MessageContainerManager;
/*     */ import org.codehaus.activemq.service.Service;
/*     */ import org.codehaus.activemq.transport.composite.CompositeTransportChannel;
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
/*     */ public class NetworkChannel
/*     */   implements Service, ConsumerInfoListener
/*     */ {
/*  49 */   private static final Log log = LogFactory.getLog(NetworkChannel.class);
/*     */   private String uri;
/*     */   private BrokerContainer brokerContainer;
/*     */   private ActiveMQConnection localConnection;
/*     */   private ActiveMQConnection remoteConnection;
/*     */   private ConcurrentHashMap consumerMap;
/*     */   private String remoteUserName;
/*     */   private String remotePassword;
/*     */   private String remoteBrokerName;
/*     */   private String remoteClusterName;
/*  59 */   private int maximumRetries = 0;
/*  60 */   private long reconnectSleepTime = 1000L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NetworkChannel() {
/*  66 */     this.consumerMap = new ConcurrentHashMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NetworkChannel(BrokerContainer brokerContainer, String uri) {
/*  76 */     this();
/*  77 */     this.brokerContainer = brokerContainer;
/*  78 */     this.uri = uri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  85 */     return super.toString() + "[uri=" + this.uri + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() throws JMSException {
/*  92 */     Thread runner = new Thread(new Runnable() { private final NetworkChannel this$0;
/*     */           public void run() {
/*     */             try {
/*  95 */               NetworkChannel.this.initialize();
/*  96 */               NetworkChannel.this.brokerContainer.getBroker().addConsumerInfoListener(NetworkChannel.this);
/*  97 */               NetworkChannel.this.startSubscriptions();
/*  98 */               NetworkChannel.log.info("Started NetworkChannel to " + NetworkChannel.this.uri);
/*     */             }
/* 100 */             catch (JMSException jmsEx) {
/* 101 */               NetworkChannel.log.error("Failed to start NetworkChannel: " + NetworkChannel.this.uri);
/*     */             } 
/*     */           }
/*     */         },  "NetworkChannel Starter");
/* 105 */     runner.setDaemon(true);
/* 106 */     runner.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() throws JMSException {
/* 115 */     this.consumerMap.clear();
/* 116 */     if (this.remoteConnection != null) {
/* 117 */       this.remoteConnection.close();
/* 118 */       this.remoteConnection = null;
/*     */     } 
/* 120 */     if (this.localConnection != null) {
/* 121 */       this.localConnection.close();
/* 122 */       this.localConnection = null;
/*     */     } 
/* 124 */     for (Iterator i = this.consumerMap.values().iterator(); i.hasNext(); ) {
/* 125 */       NetworkMessageBridge consumer = i.next();
/* 126 */       consumer.stop();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onConsumerInfo(BrokerClient client, ConsumerInfo info) {
/* 137 */     if (!client.isClusteredConnection() && 
/* 138 */       !info.hasVisited(this.remoteBrokerName)) {
/* 139 */       if (info.isStarted()) {
/* 140 */         addConsumerInfo(info);
/*     */       } else {
/*     */         
/* 143 */         removeConsumerInfo(info);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUri() {
/* 153 */     return this.uri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUri(String uri) {
/* 162 */     this.uri = uri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRemotePassword() {
/* 169 */     return this.remotePassword;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemotePassword(String remotePassword) {
/* 176 */     this.remotePassword = remotePassword;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRemoteUserName() {
/* 183 */     return this.remoteUserName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoteUserName(String remoteUserName) {
/* 190 */     this.remoteUserName = remoteUserName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BrokerContainer getBrokerContainer() {
/* 197 */     return this.brokerContainer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBrokerContainer(BrokerContainer brokerContainer) {
/* 204 */     this.brokerContainer = brokerContainer;
/*     */   }
/*     */   
/*     */   public int getMaximumRetries() {
/* 208 */     return this.maximumRetries;
/*     */   }
/*     */   
/*     */   public void setMaximumRetries(int maximumRetries) {
/* 212 */     this.maximumRetries = maximumRetries;
/*     */   }
/*     */   
/*     */   public long getReconnectSleepTime() {
/* 216 */     return this.reconnectSleepTime;
/*     */   }
/*     */   
/*     */   public void setReconnectSleepTime(long reconnectSleepTime) {
/* 220 */     this.reconnectSleepTime = reconnectSleepTime;
/*     */   }
/*     */   
/*     */   public String getRemoteBrokerName() {
/* 224 */     return this.remoteBrokerName;
/*     */   }
/*     */   
/*     */   public void setRemoteBrokerName(String remoteBrokerName) {
/* 228 */     this.remoteBrokerName = remoteBrokerName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void addConsumerInfo(ConsumerInfo info) {
/* 234 */     addConsumerInfo(info.getDestination(), info.isDurableTopic());
/*     */   }
/*     */   
/*     */   private void addConsumerInfo(ActiveMQDestination destination, boolean durableTopic) {
/* 238 */     NetworkMessageBridge key = new NetworkMessageBridge();
/* 239 */     key.setDestination(destination);
/* 240 */     key.setDurableTopic(durableTopic);
/* 241 */     NetworkMessageBridge bridge = (NetworkMessageBridge)this.consumerMap.get(key);
/* 242 */     if (bridge == null) {
/*     */       try {
/* 244 */         bridge = key;
/* 245 */         bridge.setLocalBrokerName(this.brokerContainer.getBroker().getBrokerName());
/* 246 */         bridge.setLocalSession(this.localConnection.createSession(false, 2));
/* 247 */         bridge.setRemoteSession(this.remoteConnection.createSession(false, 2));
/* 248 */         this.consumerMap.put(bridge, bridge);
/* 249 */         bridge.start();
/* 250 */         log.info("started NetworkMessageBridge for destination: " + destination);
/*     */       }
/* 252 */       catch (JMSException jmsEx) {
/* 253 */         log.error("Failed to start NetworkMessageBridge for destination: " + destination);
/*     */       } 
/*     */     }
/* 256 */     bridge.incrementReferenceCount();
/*     */   }
/*     */   
/*     */   private void removeConsumerInfo(final ConsumerInfo info) {
/* 260 */     NetworkMessageBridge key = new NetworkMessageBridge();
/* 261 */     key.setDestination(info.getDestination());
/* 262 */     key.setDurableTopic(info.isDurableTopic());
/* 263 */     final NetworkMessageBridge bridge = (NetworkMessageBridge)this.consumerMap.get(key);
/* 264 */     if (bridge != null && 
/* 265 */       bridge.decrementReferenceCount() <= 0 && !bridge.isDurableTopic() && (bridge.getDestination().isTopic() || bridge.getDestination().isTemporary())) {
/*     */       
/* 267 */       Thread runner = new Thread(new Runnable() { private final NetworkMessageBridge val$bridge;
/*     */             public void run() {
/* 269 */               bridge.stop();
/* 270 */               NetworkChannel.this.consumerMap.remove(bridge);
/* 271 */               NetworkChannel.log.info("stopped MetworkMessageBridge for destination: " + info.getDestination());
/*     */             } private final ConsumerInfo val$info; private final NetworkChannel this$0; }
/*     */         );
/* 274 */       runner.setDaemon(true);
/* 275 */       runner.start();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void startSubscriptions() {
/* 281 */     MessageContainerManager durableTopicMCM = this.brokerContainer.getBroker().getPersistentTopicContainerManager();
/* 282 */     if (durableTopicMCM != null) {
/* 283 */       Map map = durableTopicMCM.getDestinations();
/* 284 */       startSubscriptions(map, true);
/*     */     } 
/* 286 */     for (Iterator i = this.brokerContainer.getBroker().getContainerManagerMap().values().iterator(); i.hasNext(); ) {
/* 287 */       MessageContainerManager mcm = i.next();
/* 288 */       if (mcm != durableTopicMCM) {
/* 289 */         startSubscriptions(mcm.getDestinations(), false);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void startSubscriptions(Map destinations, boolean durableTopic) {
/* 295 */     if (destinations != null) {
/* 296 */       for (Iterator i = destinations.values().iterator(); i.hasNext(); ) {
/* 297 */         ActiveMQDestination dest = i.next();
/* 298 */         addConsumerInfo(dest, durableTopic);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void initialize() throws JMSException {
/* 304 */     initializeRemote();
/* 305 */     initializeLocal();
/*     */   }
/*     */ 
/*     */   
/*     */   private void initializeRemote() throws JMSException {
/* 310 */     ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(this.remoteUserName, this.remotePassword, this.uri);
/* 311 */     factory.setUseAsyncSend(true);
/* 312 */     this.remoteConnection = (ActiveMQConnection)factory.createConnection();
/* 313 */     this.remoteConnection.setClientID(this.brokerContainer.getBroker().getBrokerName() + "_NetworkChannel");
/* 314 */     TransportChannel transportChannel = this.remoteConnection.getTransportChannel();
/* 315 */     if (transportChannel instanceof CompositeTransportChannel) {
/* 316 */       CompositeTransportChannel composite = (CompositeTransportChannel)transportChannel;
/* 317 */       composite.setMaximumRetries(this.maximumRetries);
/* 318 */       composite.setFailureSleepTime(this.reconnectSleepTime);
/*     */     } 
/* 320 */     this.remoteConnection.start();
/* 321 */     BrokerInfo info = new BrokerInfo();
/* 322 */     info.setBrokerName(this.brokerContainer.getBroker().getBrokerName());
/* 323 */     info.setClusterName(this.brokerContainer.getBroker().getBrokerClusterName());
/* 324 */     Receipt receipt = this.remoteConnection.syncSendRequest((Packet)info);
/* 325 */     this.remoteBrokerName = receipt.getBrokerName();
/* 326 */     this.remoteClusterName = receipt.getClusterName();
/*     */   }
/*     */   
/*     */   private void initializeLocal() throws JMSException {
/* 330 */     String brokerName = this.brokerContainer.getBroker().getBrokerName();
/* 331 */     ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("vm://" + brokerName);
/* 332 */     factory.setUseAsyncSend(true);
/* 333 */     factory.setBrokerName(brokerName);
/* 334 */     this.localConnection = (ActiveMQConnection)factory.createConnection();
/* 335 */     this.localConnection.start();
/* 336 */     BrokerInfo info = new BrokerInfo();
/* 337 */     info.setBrokerName(this.remoteBrokerName);
/* 338 */     info.setClusterName(this.remoteClusterName);
/* 339 */     this.localConnection.asyncSendPacket((Packet)info);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\NetworkChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */