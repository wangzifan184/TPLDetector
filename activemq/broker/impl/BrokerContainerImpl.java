/*     */ package org.codehaus.activemq.broker.impl;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;
/*     */ import EDU.oswego.cs.dl.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.jms.InvalidClientIDException;
/*     */ import javax.jms.InvalidDestinationException;
/*     */ import javax.jms.JMSException;
/*     */ import javax.transaction.xa.XAException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.broker.Broker;
/*     */ import org.codehaus.activemq.broker.BrokerClient;
/*     */ import org.codehaus.activemq.broker.BrokerConnector;
/*     */ import org.codehaus.activemq.broker.BrokerContainer;
/*     */ import org.codehaus.activemq.broker.BrokerContext;
/*     */ import org.codehaus.activemq.capacity.CapacityMonitorEvent;
/*     */ import org.codehaus.activemq.capacity.CapacityMonitorEventListener;
/*     */ import org.codehaus.activemq.message.ActiveMQDestination;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.ActiveMQXid;
/*     */ import org.codehaus.activemq.message.ConnectionInfo;
/*     */ import org.codehaus.activemq.message.ConsumerInfo;
/*     */ import org.codehaus.activemq.message.DefaultWireFormat;
/*     */ import org.codehaus.activemq.message.DurableUnsubscribe;
/*     */ import org.codehaus.activemq.message.MessageAck;
/*     */ import org.codehaus.activemq.message.ProducerInfo;
/*     */ import org.codehaus.activemq.message.SessionInfo;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.security.SecurityAdapter;
/*     */ import org.codehaus.activemq.service.RedeliveryPolicy;
/*     */ import org.codehaus.activemq.service.Service;
/*     */ import org.codehaus.activemq.store.PersistenceAdapter;
/*     */ import org.codehaus.activemq.transport.DiscoveryAgent;
/*     */ import org.codehaus.activemq.transport.NetworkConnector;
/*     */ import org.codehaus.activemq.transport.TransportServerChannel;
/*     */ import org.codehaus.activemq.util.IdGenerator;
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
/*     */ public class BrokerContainerImpl
/*     */   implements BrokerContainer, CapacityMonitorEventListener
/*     */ {
/*  59 */   private static final Log log = LogFactory.getLog(BrokerContainerImpl.class);
/*     */   
/*     */   private BrokerContext context;
/*     */   
/*     */   private Broker broker;
/*     */   private Map clientIds;
/*     */   private Map consumerInfos;
/*     */   private Map producerInfos;
/*     */   private List transportConnectors;
/*     */   private Thread shutdownHook;
/*     */   private boolean stopped;
/*     */   private List networkConnectors;
/*     */   private DiscoveryAgent discoveryAgent;
/*     */   private Map localDiscoveryDetails;
/*     */   
/*     */   public BrokerContainerImpl() {
/*  75 */     this((new IdGenerator()).generateId());
/*     */   }
/*     */   
/*     */   public BrokerContainerImpl(String brokerName) {
/*  79 */     this(brokerName, BrokerContext.getInstance());
/*     */   }
/*     */   
/*     */   public BrokerContainerImpl(String brokerName, PersistenceAdapter persistenceAdapter) {
/*  83 */     this(brokerName, persistenceAdapter, BrokerContext.getInstance());
/*     */   }
/*     */   
/*     */   public BrokerContainerImpl(String brokerName, BrokerContext context) {
/*  87 */     this(new DefaultBroker(brokerName), context);
/*     */   }
/*     */   
/*     */   public BrokerContainerImpl(String brokerName, PersistenceAdapter persistenceAdapter, BrokerContext context) {
/*  91 */     this(new DefaultBroker(brokerName, persistenceAdapter), context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BrokerContainerImpl(Broker broker, BrokerContext context) {
/*  98 */     this.broker = broker;
/*  99 */     this.context = context;
/* 100 */     this.clientIds = (Map)new ConcurrentHashMap();
/* 101 */     this.consumerInfos = (Map)new ConcurrentHashMap();
/* 102 */     this.producerInfos = (Map)new ConcurrentHashMap();
/* 103 */     this.transportConnectors = (List)new CopyOnWriteArrayList();
/* 104 */     this.networkConnectors = (List)new CopyOnWriteArrayList();
/* 105 */     this.broker.addCapacityEventListener(this);
/*     */ 
/*     */     
/* 108 */     context.registerContainer(broker.getBrokerName(), this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() throws JMSException {
/* 117 */     log.info("ActiveMQ JMS Message Broker (" + this.broker.getBrokerName() + ") is starting");
/* 118 */     log.info("For help or more information please see: www.protique.com");
/* 119 */     this.broker.start();
/* 120 */     addShutdownHook();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     for (Iterator iterator1 = (new ArrayList(this.networkConnectors)).iterator(); iterator1.hasNext(); ) {
/* 127 */       Service connector = (Service)iterator1.next();
/* 128 */       connector.start();
/*     */     } 
/*     */     
/* 131 */     for (Iterator iter = (new ArrayList(this.transportConnectors)).iterator(); iter.hasNext(); ) {
/* 132 */       Service connector = (Service)iter.next();
/* 133 */       connector.start();
/*     */     } 
/*     */     
/* 136 */     if (this.discoveryAgent != null) {
/* 137 */       this.discoveryAgent.start();
/*     */       
/* 139 */       this.localDiscoveryDetails = createLocalDiscoveryDetails();
/* 140 */       this.discoveryAgent.registerService(getLocalBrokerName(), this.localDiscoveryDetails);
/*     */     } 
/*     */     
/* 143 */     log.info("ActiveMQ JMS Message Broker (" + this.broker.getBrokerName() + ") has started");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void stop() throws JMSException {
/* 152 */     if (!this.stopped) {
/* 153 */       this.stopped = true;
/*     */       
/* 155 */       log.info("ActiveMQ Message Broker (" + this.broker.getBrokerName() + ") is shutting down");
/*     */       
/* 157 */       this.context.deregisterContainer(this.broker.getBrokerName(), this);
/*     */       
/*     */       try {
/* 160 */         Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
/*     */       }
/* 162 */       catch (Exception e) {
/* 163 */         log.debug("Caught exception, must be shutting down: " + e);
/*     */       } 
/*     */       
/* 166 */       JMSException firstException = null;
/*     */       
/* 168 */       for (Iterator iterator2 = (new ArrayList(this.transportConnectors)).iterator(); iterator2.hasNext(); ) {
/* 169 */         Service connector = (Service)iterator2.next();
/*     */         try {
/* 171 */           connector.stop();
/*     */         }
/* 173 */         catch (JMSException e) {
/* 174 */           if (firstException == null) {
/* 175 */             firstException = e;
/*     */           }
/* 177 */           log.warn("Could not close transport connector: " + connector + " due to: " + e, (Throwable)e);
/*     */         } 
/*     */       } 
/* 180 */       this.transportConnectors.clear();
/*     */       
/* 182 */       for (Iterator iterator1 = (new ArrayList(this.networkConnectors)).iterator(); iterator1.hasNext(); ) {
/* 183 */         Service connector = (Service)iterator1.next();
/*     */         try {
/* 185 */           connector.stop();
/*     */         }
/* 187 */         catch (JMSException e) {
/* 188 */           if (firstException == null) {
/* 189 */             firstException = e;
/*     */           }
/* 191 */           log.warn("Could not close network connector: " + connector + " due to: " + e, (Throwable)e);
/*     */         } 
/*     */       } 
/* 194 */       this.networkConnectors.clear();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 200 */       for (Iterator iter = this.clientIds.values().iterator(); iter.hasNext(); ) {
/*     */         
/* 202 */         BrokerClient client = iter.next();
/*     */         try {
/* 204 */           client.stop();
/*     */         }
/* 206 */         catch (JMSException e) {
/* 207 */           if (firstException == null) {
/* 208 */             firstException = e;
/*     */           }
/* 210 */           log.warn("Could not close client: " + client + " due to: " + e, (Throwable)e);
/*     */         } 
/*     */       } 
/* 213 */       this.clientIds.clear();
/*     */       
/* 215 */       this.broker.removeCapacityEventListener(this);
/* 216 */       this.broker.stop();
/*     */       
/* 218 */       log.info("ActiveMQ JMS Message Broker (" + this.broker.getBrokerName() + ") stopped");
/*     */       
/* 220 */       if (firstException != null) {
/* 221 */         throw firstException;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerConnection(BrokerClient client, ConnectionInfo info) throws JMSException {
/* 234 */     String clientId = info.getClientId();
/* 235 */     if (this.clientIds.containsKey(clientId)) {
/* 236 */       throw new InvalidClientIDException("Duplicate clientId: " + info);
/*     */     }
/* 238 */     getBroker().addClient(client, info);
/* 239 */     log.info("Adding new client: " + clientId + " on transport: " + client.getChannel());
/* 240 */     this.clientIds.put(clientId, client);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deregisterConnection(BrokerClient client, ConnectionInfo info) throws JMSException {
/* 251 */     String clientId = client.getClientID();
/* 252 */     if (clientId != null) {
/* 253 */       Object answer = this.clientIds.remove(clientId);
/* 254 */       if (answer != null) {
/* 255 */         log.info("Removing client: " + clientId + " on transport: " + client.getChannel());
/* 256 */         getBroker().removeClient(client, info);
/*     */       } else {
/*     */         
/* 259 */         log.warn("Got duplicate deregisterConnection for client: " + clientId);
/*     */       } 
/*     */     } else {
/*     */       
/* 263 */       log.warn("No clientID available for client: " + client);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerMessageConsumer(BrokerClient client, ConsumerInfo info) throws JMSException {
/* 276 */     this.consumerInfos.put(info, client);
/* 277 */     getBroker().addMessageConsumer(client, info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deregisterMessageConsumer(BrokerClient client, ConsumerInfo info) throws JMSException {
/* 288 */     this.consumerInfos.remove(info);
/* 289 */     getBroker().removeMessageConsumer(client, info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerMessageProducer(BrokerClient client, ProducerInfo info) throws JMSException {
/* 301 */     ActiveMQDestination dest = info.getDestination();
/* 302 */     if (dest != null && dest.isTemporary()) {
/*     */       
/* 304 */       String clientId = ActiveMQDestination.getClientId(dest);
/* 305 */       if (clientId == null) {
/* 306 */         throw new InvalidDestinationException("Destination " + dest.getPhysicalName() + " is a temporary destination with null clientId");
/*     */       }
/*     */       
/* 309 */       if (!this.clientIds.containsKey(clientId)) {
/* 310 */         throw new InvalidDestinationException("Destination " + dest.getPhysicalName() + " is no longer valid because the client " + clientId + " no longer exists");
/*     */       }
/*     */     } 
/*     */     
/* 314 */     getBroker().addMessageProducer(client, info);
/*     */     
/* 316 */     this.producerInfos.put(info, client);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deregisterMessageProducer(BrokerClient client, ProducerInfo info) throws JMSException {
/* 327 */     getBroker().removeMessageProducer(client, info);
/*     */     
/* 329 */     this.producerInfos.remove(info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerSession(BrokerClient client, SessionInfo info) throws JMSException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deregisterSession(BrokerClient client, SessionInfo info) throws JMSException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startTransaction(BrokerClient client, String transactionId) throws JMSException {
/* 360 */     getBroker().startTransaction(client, transactionId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rollbackTransaction(BrokerClient client, String transactionId) throws JMSException {
/* 371 */     getBroker().rollbackTransaction(client, transactionId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void commitTransaction(BrokerClient client, String transactionId) throws JMSException {
/* 382 */     getBroker().commitTransaction(client, transactionId);
/*     */   }
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
/*     */   public void sendTransactedMessage(BrokerClient client, String transactionId, ActiveMQMessage message) throws JMSException {
/* 395 */     getBroker().sendTransactedMessage(client, transactionId, message);
/*     */   }
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
/*     */   public void acknowledgeTransactedMessage(BrokerClient client, String transactionId, MessageAck ack) throws JMSException {
/* 408 */     getBroker().acknowledgeTransactedMessage(client, transactionId, ack);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendMessage(BrokerClient client, ActiveMQMessage message) throws JMSException {
/* 419 */     getBroker().sendMessage(client, message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void acknowledgeMessage(BrokerClient client, MessageAck ack) throws JMSException {
/* 430 */     getBroker().acknowledgeMessage(client, ack);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void durableUnsubscribe(BrokerClient client, DurableUnsubscribe ds) throws JMSException {
/* 441 */     getBroker().deleteSubscription(ds.getClientId(), ds.getSubscriberName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startTransaction(BrokerClient client, ActiveMQXid xid) throws XAException {
/* 451 */     getBroker().startTransaction(client, xid);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQXid[] getPreparedTransactions(BrokerClient client) throws XAException {
/* 461 */     return getBroker().getPreparedTransactions(client);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int prepareTransaction(BrokerClient client, ActiveMQXid xid) throws XAException {
/* 471 */     return getBroker().prepareTransaction(client, xid);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rollbackTransaction(BrokerClient client, ActiveMQXid xid) throws XAException {
/* 481 */     getBroker().rollbackTransaction(client, xid);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void commitTransaction(BrokerClient client, ActiveMQXid xid, boolean onePhase) throws XAException {
/* 492 */     getBroker().commitTransaction(client, xid, onePhase);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void capacityChanged(CapacityMonitorEvent event) {
/* 503 */     for (Iterator i = this.producerInfos.values().iterator(); i.hasNext(); ) {
/* 504 */       BrokerClient client = i.next();
/* 505 */       client.updateBrokerCapacity(event.getCapacity());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getTransportConnectors() {
/* 514 */     return this.transportConnectors;
/*     */   }
/*     */   
/*     */   public void setTransportConnectors(List transportConnectors) {
/* 518 */     this.transportConnectors = transportConnectors;
/*     */   }
/*     */   
/*     */   public void addConnector(BrokerConnector connector) {
/* 522 */     this.transportConnectors.add(connector);
/* 523 */     this.context.registerConnector(connector.getServerChannel().getUrl(), connector);
/*     */   }
/*     */   
/*     */   public void removeConnector(BrokerConnector connector) {
/* 527 */     this.transportConnectors.remove(connector);
/* 528 */     this.context.deregisterConnector(connector.getServerChannel().getUrl());
/*     */   }
/*     */ 
/*     */   
/*     */   public void addConnector(String bindAddress) throws JMSException {
/* 533 */     addConnector(bindAddress, (WireFormat)new DefaultWireFormat());
/*     */   }
/*     */   
/*     */   public void addConnector(String bindAddress, WireFormat wireFormat) throws JMSException {
/* 537 */     addConnector(new BrokerConnectorImpl(this, bindAddress, wireFormat));
/*     */   }
/*     */   
/*     */   public void addConnector(TransportServerChannel transportConnector) {
/* 541 */     addConnector(new BrokerConnectorImpl(this, transportConnector));
/*     */   }
/*     */   
/*     */   public List getNetworkConnectors() {
/* 545 */     return this.networkConnectors;
/*     */   }
/*     */   
/*     */   public void setNetworkConnectors(List networkConnectors) {
/* 549 */     this.networkConnectors = networkConnectors;
/*     */   }
/*     */   
/*     */   public NetworkConnector addNetworkConnector(String uri) {
/* 553 */     NetworkConnector connector = addNetworkConnector();
/* 554 */     connector.addNetworkChannel(uri);
/* 555 */     return connector;
/*     */   }
/*     */   
/*     */   public NetworkConnector addNetworkConnector() {
/* 559 */     NetworkConnector connector = new NetworkConnector(this);
/* 560 */     addNetworkConnector(connector);
/* 561 */     return connector;
/*     */   }
/*     */   
/*     */   public void addNetworkConnector(NetworkConnector connector) {
/* 565 */     this.networkConnectors.add(connector);
/*     */   }
/*     */   
/*     */   public void removeNetworkConnector(NetworkConnector connector) {
/* 569 */     this.networkConnectors.remove(connector);
/*     */   }
/*     */ 
/*     */   
/*     */   public Broker getBroker() {
/* 574 */     return this.broker;
/*     */   }
/*     */   
/*     */   public PersistenceAdapter getPersistenceAdapter() {
/* 578 */     return (this.broker != null) ? this.broker.getPersistenceAdapter() : null;
/*     */   }
/*     */   
/*     */   public void setPersistenceAdapter(PersistenceAdapter persistenceAdapter) {
/* 582 */     checkBrokerSet();
/* 583 */     this.broker.setPersistenceAdapter(persistenceAdapter);
/*     */   }
/*     */   
/*     */   public DiscoveryAgent getDiscoveryAgent() {
/* 587 */     return this.discoveryAgent;
/*     */   }
/*     */   
/*     */   public void setDiscoveryAgent(DiscoveryAgent discoveryAgent) {
/* 591 */     this.discoveryAgent = discoveryAgent;
/*     */   }
/*     */   
/*     */   public SecurityAdapter getSecurityAdapter() {
/* 595 */     return (this.broker != null) ? this.broker.getSecurityAdapter() : null;
/*     */   }
/*     */   
/*     */   public void setSecurityAdapter(SecurityAdapter securityAdapter) {
/* 599 */     checkBrokerSet();
/* 600 */     this.broker.setSecurityAdapter(securityAdapter);
/*     */   }
/*     */   
/*     */   public RedeliveryPolicy getRedeliveryPolicy() {
/* 604 */     return (this.broker != null) ? this.broker.getRedeliveryPolicy() : null;
/*     */   }
/*     */   
/*     */   public void setRedeliveryPolicy(RedeliveryPolicy redeliveryPolicy) {
/* 608 */     checkBrokerSet();
/* 609 */     this.broker.setRedeliveryPolicy(redeliveryPolicy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkBrokerSet() {
/* 616 */     if (this.broker == null) {
/* 617 */       throw new IllegalStateException("Cannot set this property as we don't have a broker yet");
/*     */     }
/*     */   }
/*     */   
/*     */   protected Map createLocalDiscoveryDetails() {
/* 622 */     Map map = new HashMap();
/* 623 */     map.put("brokerName", getLocalBrokerName());
/* 624 */     map.put("connectURL", getLocalConnectionURL());
/* 625 */     return map;
/*     */   }
/*     */   
/*     */   protected String getLocalBrokerName() {
/* 629 */     return getBroker().getBrokerName();
/*     */   }
/*     */   
/*     */   protected String getLocalConnectionURL() {
/* 633 */     StringBuffer buffer = new StringBuffer("reliable:");
/* 634 */     List list = getTransportConnectors();
/* 635 */     boolean first = true;
/* 636 */     for (Iterator iter = list.iterator(); iter.hasNext(); ) {
/* 637 */       BrokerConnector brokerConnector = iter.next();
/* 638 */       TransportServerChannel connector = brokerConnector.getServerChannel();
/* 639 */       String url = connector.getUrl();
/* 640 */       if (first) {
/* 641 */         first = false;
/*     */       } else {
/*     */         
/* 644 */         buffer.append(",");
/*     */       } 
/* 646 */       buffer.append(url);
/*     */     } 
/* 648 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   protected void addShutdownHook() {
/* 652 */     this.shutdownHook = new Thread("ActiveMQ ShutdownHook") {
/*     */         public void run() {
/* 654 */           BrokerContainerImpl.this.containerShutdown();
/*     */         } private final BrokerContainerImpl this$0;
/*     */       };
/* 657 */     Runtime.getRuntime().addShutdownHook(this.shutdownHook);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void containerShutdown() {
/*     */     try {
/* 665 */       stop();
/*     */     }
/* 667 */     catch (JMSException e) {
/* 668 */       Exception linkedException = e.getLinkedException();
/* 669 */       if (linkedException != null) {
/* 670 */         log.error("Failed to shut down: " + e + ". Reason: " + linkedException, linkedException);
/*     */       } else {
/*     */         
/* 673 */         log.error("Failed to shut down: " + e, (Throwable)e);
/*     */       }
/*     */     
/* 676 */     } catch (Exception e) {
/* 677 */       log.error("Failed to shut down: " + e, e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\broker\impl\BrokerContainerImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */