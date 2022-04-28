/*     */ package org.codehaus.activemq.broker.impl;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.jms.JMSException;
/*     */ import javax.transaction.xa.XAException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.broker.BrokerClient;
/*     */ import org.codehaus.activemq.broker.BrokerConnector;
/*     */ import org.codehaus.activemq.broker.BrokerContainer;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.ActiveMQXid;
/*     */ import org.codehaus.activemq.message.BrokerInfo;
/*     */ import org.codehaus.activemq.message.ConnectionInfo;
/*     */ import org.codehaus.activemq.message.ConsumerInfo;
/*     */ import org.codehaus.activemq.message.DurableUnsubscribe;
/*     */ import org.codehaus.activemq.message.MessageAck;
/*     */ import org.codehaus.activemq.message.ProducerInfo;
/*     */ import org.codehaus.activemq.message.SessionInfo;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.transport.TransportChannel;
/*     */ import org.codehaus.activemq.transport.TransportChannelListener;
/*     */ import org.codehaus.activemq.transport.TransportServerChannel;
/*     */ import org.codehaus.activemq.transport.TransportServerChannelProvider;
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
/*     */ public class BrokerConnectorImpl
/*     */   implements BrokerConnector, TransportChannelListener
/*     */ {
/*     */   private BrokerInfo brokerInfo;
/*     */   private TransportServerChannel serverChannel;
/*     */   private Log log;
/*     */   private BrokerContainer container;
/*  51 */   private Map clients = Collections.synchronizedMap(new HashMap());
/*     */ 
/*     */ 
/*     */   
/*     */   static final boolean $assertionsDisabled;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BrokerConnectorImpl(BrokerContainer container, String bindAddress, WireFormat wireFormat) throws JMSException {
/*  61 */     this(container, createTransportServerChannel(wireFormat, bindAddress));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BrokerConnectorImpl(BrokerContainer container, TransportServerChannel serverChannel) {
/*  69 */     assert container != null;
/*  70 */     this.brokerInfo = new BrokerInfo();
/*  71 */     this.brokerInfo.setBrokerName(container.getBroker().getBrokerName());
/*  72 */     this.brokerInfo.setClusterName(container.getBroker().getBrokerClusterName());
/*  73 */     this.log = LogFactory.getLog(getClass().getName());
/*  74 */     this.serverChannel = serverChannel;
/*  75 */     this.container = container;
/*  76 */     this.container.addConnector(this);
/*  77 */     serverChannel.setTransportChannelListener(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BrokerInfo getBrokerInfo() {
/*  84 */     return this.brokerInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBrokerCapacity() {
/*  94 */     return this.container.getBroker().getRoundedCapacity();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TransportServerChannel getServerChannel() {
/* 101 */     return this.serverChannel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() throws JMSException {
/* 110 */     this.serverChannel.start();
/* 111 */     this.log.info("ActiveMQ connector started: " + this.serverChannel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() throws JMSException {
/* 120 */     this.container.removeConnector(this);
/* 121 */     this.serverChannel.stop();
/* 122 */     this.log.info("ActiveMQ connector stopped: " + this.serverChannel);
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
/*     */   
/*     */   public void registerClient(BrokerClient client, ConnectionInfo info) throws JMSException {
/* 136 */     this.container.registerConnection(client, info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deregisterClient(BrokerClient client, ConnectionInfo info) throws JMSException {
/* 147 */     this.container.deregisterConnection(client, info);
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
/* 159 */     if (info.getDestination() == null) {
/* 160 */       throw new JMSException("No Destination specified on consumerInfo for client: " + client + " info: " + info);
/*     */     }
/* 162 */     this.container.registerMessageConsumer(client, info);
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
/*     */   public void deregisterMessageConsumer(BrokerClient client, ConsumerInfo info) throws JMSException {
/* 174 */     this.container.deregisterMessageConsumer(client, info);
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
/* 186 */     this.container.registerMessageProducer(client, info);
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
/* 197 */     this.container.deregisterMessageProducer(client, info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerSession(BrokerClient client, SessionInfo info) throws JMSException {
/* 208 */     this.container.registerSession(client, info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deregisterSession(BrokerClient client, SessionInfo info) throws JMSException {
/* 219 */     this.container.deregisterSession(client, info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startTransaction(BrokerClient client, String transactionId) throws JMSException {
/* 230 */     this.container.startTransaction(client, transactionId);
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
/* 241 */     this.container.rollbackTransaction(client, transactionId);
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
/* 252 */     this.container.commitTransaction(client, transactionId);
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
/* 265 */     this.container.sendTransactedMessage(client, transactionId, message);
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
/* 278 */     this.container.acknowledgeTransactedMessage(client, transactionId, ack);
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
/* 289 */     this.container.sendMessage(client, message);
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
/* 300 */     this.container.acknowledgeMessage(client, ack);
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
/* 311 */     this.container.durableUnsubscribe(client, ds);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addClient(TransportChannel channel) {
/*     */     try {
/* 320 */       BrokerClient client = new BrokerClientImpl();
/* 321 */       client.initialize(this, channel);
/* 322 */       if (this.log.isDebugEnabled()) {
/* 323 */         this.log.debug("Starting new client: " + client);
/*     */       }
/* 325 */       channel.setServerSide(true);
/* 326 */       channel.start();
/* 327 */       this.clients.put(channel, client);
/*     */     }
/* 329 */     catch (JMSException e) {
/* 330 */       this.log.error("Failed to add client due to: " + e, (Throwable)e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeClient(TransportChannel channel) {
/* 338 */     BrokerClient client = (BrokerClient)this.clients.remove(channel);
/* 339 */     if (client != null) {
/* 340 */       if (this.log.isDebugEnabled()) {
/* 341 */         this.log.debug("Client leaving client: " + client);
/*     */       }
/*     */ 
/*     */       
/* 345 */       client.cleanUp();
/*     */     }
/*     */     else {
/*     */       
/* 349 */       this.log.warn("No such client for channel: " + channel);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BrokerContainer getBrokerContainer() {
/* 357 */     return this.container;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startTransaction(BrokerClient client, ActiveMQXid xid) throws XAException {
/* 366 */     this.container.startTransaction(client, xid);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQXid[] getPreparedTransactions(BrokerClient client) throws XAException {
/* 375 */     return this.container.getPreparedTransactions(client);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int prepareTransaction(BrokerClient client, ActiveMQXid xid) throws XAException {
/* 384 */     return this.container.prepareTransaction(client, xid);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rollbackTransaction(BrokerClient client, ActiveMQXid xid) throws XAException {
/* 393 */     this.container.rollbackTransaction(client, xid);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void commitTransaction(BrokerClient client, ActiveMQXid xid, boolean onePhase) throws XAException {
/* 402 */     this.container.commitTransaction(client, xid, onePhase);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getResourceManagerId(BrokerClient client) {
/* 410 */     return getBrokerInfo().getBrokerName();
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
/*     */   
/*     */   protected static TransportServerChannel createTransportServerChannel(WireFormat wireFormat, String bindAddress) throws JMSException {
/*     */     URI url;
/*     */     try {
/* 426 */       url = new URI(bindAddress);
/*     */     }
/* 428 */     catch (URISyntaxException e) {
/* 429 */       JMSException jmsEx = new JMSException("Badly formated bindAddress: " + e.getMessage());
/* 430 */       jmsEx.setLinkedException(e);
/* 431 */       throw jmsEx;
/*     */     } 
/* 433 */     return TransportServerChannelProvider.create(wireFormat, url);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\broker\impl\BrokerConnectorImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */