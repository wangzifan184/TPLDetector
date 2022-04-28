/*      */ package org.codehaus.activemq;
/*      */ 
/*      */ import EDU.oswego.cs.dl.util.concurrent.CopyOnWriteArrayList;
/*      */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;
/*      */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedInt;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import javax.jms.Connection;
/*      */ import javax.jms.ConnectionConsumer;
/*      */ import javax.jms.ConnectionMetaData;
/*      */ import javax.jms.Destination;
/*      */ import javax.jms.ExceptionListener;
/*      */ import javax.jms.IllegalStateException;
/*      */ import javax.jms.JMSException;
/*      */ import javax.jms.Queue;
/*      */ import javax.jms.QueueConnection;
/*      */ import javax.jms.QueueSession;
/*      */ import javax.jms.ServerSessionPool;
/*      */ import javax.jms.Session;
/*      */ import javax.jms.Topic;
/*      */ import javax.jms.TopicConnection;
/*      */ import javax.jms.TopicSession;
/*      */ import javax.management.j2ee.statistics.Stats;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.codehaus.activemq.capacity.CapacityMonitorEvent;
/*      */ import org.codehaus.activemq.capacity.CapacityMonitorEventListener;
/*      */ import org.codehaus.activemq.management.JMSConnectionStatsImpl;
/*      */ import org.codehaus.activemq.management.JMSStatsImpl;
/*      */ import org.codehaus.activemq.management.StatsCapable;
/*      */ import org.codehaus.activemq.message.ActiveMQMessage;
/*      */ import org.codehaus.activemq.message.CapacityInfo;
/*      */ import org.codehaus.activemq.message.ConnectionInfo;
/*      */ import org.codehaus.activemq.message.ConsumerInfo;
/*      */ import org.codehaus.activemq.message.Packet;
/*      */ import org.codehaus.activemq.message.PacketListener;
/*      */ import org.codehaus.activemq.message.ProducerInfo;
/*      */ import org.codehaus.activemq.message.Receipt;
/*      */ import org.codehaus.activemq.message.SessionInfo;
/*      */ import org.codehaus.activemq.message.util.MemoryBoundedQueue;
/*      */ import org.codehaus.activemq.message.util.MemoryBoundedQueueManager;
/*      */ import org.codehaus.activemq.transport.TransportChannel;
/*      */ import org.codehaus.activemq.transport.TransportStatusEvent;
/*      */ import org.codehaus.activemq.transport.TransportStatusEventListener;
/*      */ import org.codehaus.activemq.util.IdGenerator;
/*      */ import org.codehaus.activemq.util.JMSExceptionHelper;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ActiveMQConnection
/*      */   implements Connection, PacketListener, ExceptionListener, TopicConnection, QueueConnection, StatsCapable, CapacityMonitorEventListener, TransportStatusEventListener
/*      */ {
/*      */   public static final String DEFAULT_USER = "defaultUser";
/*      */   public static final String DEFAULT_URL = "tcp://localhost:61616";
/*      */   public static final String DEFAULT_PASSWORD = "defaultPassword";
/*  139 */   private static final Log log = LogFactory.getLog(ActiveMQConnection.class);
/*      */   
/*      */   private static final int DEFAULT_CONNECTION_MEMORY_LIMIT = 10485760;
/*      */   
/*      */   private ActiveMQConnectionFactory factory;
/*      */   private String userName;
/*      */   private String password;
/*      */   protected String clientID;
/*  147 */   private int sendCloseTimeout = 2000;
/*      */   
/*      */   private TransportChannel transportChannel;
/*      */   
/*      */   private ExceptionListener exceptionListener;
/*      */   private ActiveMQPrefetchPolicy prefetchPolicy;
/*      */   private JMSStatsImpl factoryStats;
/*      */   private MemoryBoundedQueueManager boundedQueueManager;
/*      */   protected IdGenerator consumerIdGenerator;
/*      */   private IdGenerator clientIdGenerator;
/*      */   protected IdGenerator packetIdGenerator;
/*      */   private IdGenerator sessionIdGenerator;
/*      */   private JMSConnectionStatsImpl stats;
/*      */   private CopyOnWriteArrayList sessions;
/*      */   private CopyOnWriteArrayList messageDispatchers;
/*      */   private CopyOnWriteArrayList connectionConsumers;
/*      */   private SynchronizedInt consumerNumberGenerator;
/*      */   private ActiveMQConnectionMetaData connectionMetaData;
/*      */   private SynchronizedBoolean closed;
/*      */   private SynchronizedBoolean started;
/*      */   private boolean clientIDSet;
/*      */   private boolean isConnectionInfoSentToBroker;
/*      */   private boolean isTransportOK;
/*      */   private boolean startedTransport;
/*      */   private long startTime;
/*  172 */   private long flowControlSleepTime = 0L;
/*      */ 
/*      */   
/*      */   private boolean userSpecifiedClientID;
/*      */   
/*      */   protected boolean useAsyncSend = true;
/*      */   
/*  179 */   private int sendConnectionInfoTimeout = 30000;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ActiveMQConnection makeConnection() throws JMSException {
/*  188 */     ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
/*  189 */     return (ActiveMQConnection)factory.createConnection();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ActiveMQConnection makeConnection(String uri) throws JMSException {
/*  200 */     ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(uri);
/*  201 */     return (ActiveMQConnection)factory.createConnection();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ActiveMQConnection makeConnection(String user, String password, String uri) throws JMSException {
/*  214 */     ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(user, password, uri);
/*  215 */     return (ActiveMQConnection)factory.createConnection();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ActiveMQConnection(ActiveMQConnectionFactory factory, String theUserName, String thePassword, TransportChannel transportChannel) throws JMSException {
/*  229 */     this(factory, theUserName, thePassword);
/*  230 */     this.transportChannel = transportChannel;
/*  231 */     this.transportChannel.setPacketListener(this);
/*  232 */     this.transportChannel.setExceptionListener(this);
/*  233 */     this.transportChannel.addTransportStatusEventListener(this);
/*  234 */     this.isTransportOK = true;
/*      */   }
/*      */   
/*      */   protected ActiveMQConnection(ActiveMQConnectionFactory factory, String theUserName, String thePassword) {
/*  238 */     this.factory = factory;
/*  239 */     this.userName = theUserName;
/*  240 */     this.password = thePassword;
/*  241 */     this.clientIdGenerator = new IdGenerator();
/*  242 */     this.packetIdGenerator = new IdGenerator();
/*  243 */     this.consumerIdGenerator = new IdGenerator();
/*  244 */     this.sessionIdGenerator = new IdGenerator();
/*  245 */     this.consumerNumberGenerator = new SynchronizedInt(0);
/*  246 */     this.sessions = new CopyOnWriteArrayList();
/*  247 */     this.messageDispatchers = new CopyOnWriteArrayList();
/*  248 */     this.connectionConsumers = new CopyOnWriteArrayList();
/*  249 */     this.connectionMetaData = new ActiveMQConnectionMetaData();
/*  250 */     this.closed = new SynchronizedBoolean(false);
/*  251 */     this.started = new SynchronizedBoolean(false);
/*  252 */     this.startTime = System.currentTimeMillis();
/*  253 */     this.prefetchPolicy = new ActiveMQPrefetchPolicy();
/*  254 */     this.boundedQueueManager = new MemoryBoundedQueueManager(this.clientID, 10485760L);
/*  255 */     this.boundedQueueManager.addCapacityEventListener(this);
/*  256 */     boolean transactional = this instanceof javax.jms.XAConnection;
/*  257 */     this.factoryStats = factory.getFactoryStats();
/*  258 */     this.factoryStats.addConnection(this);
/*  259 */     this.stats = new JMSConnectionStatsImpl((List)this.sessions, transactional);
/*  260 */     factory.onConnectionCreate(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Stats getStats() {
/*  267 */     return (Stats)this.stats;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JMSConnectionStatsImpl getConnectionStats() {
/*  274 */     return this.stats;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Session createSession(boolean transacted, int acknowledgeMode) throws JMSException {
/*  293 */     checkClosed();
/*  294 */     ensureClientIDInitialised();
/*  295 */     return new ActiveMQSession(this, transacted ? 0 : acknowledgeMode);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getClientID() throws JMSException {
/*  310 */     checkClosed();
/*  311 */     return this.clientID;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClientID(String newClientID) throws JMSException {
/*  347 */     if (this.clientIDSet) {
/*  348 */       throw new IllegalStateException("The clientID has already been set");
/*      */     }
/*  350 */     if (this.isConnectionInfoSentToBroker) {
/*  351 */       throw new IllegalStateException("Setting clientID on a used Connection is not allowed");
/*      */     }
/*  353 */     checkClosed();
/*  354 */     this.clientID = newClientID;
/*  355 */     this.userSpecifiedClientID = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConnectionMetaData getMetaData() throws JMSException {
/*  366 */     checkClosed();
/*  367 */     return this.connectionMetaData;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ExceptionListener getExceptionListener() throws JMSException {
/*  380 */     checkClosed();
/*  381 */     return this.exceptionListener;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExceptionListener(ExceptionListener listener) throws JMSException {
/*  402 */     checkClosed();
/*  403 */     this.exceptionListener = listener;
/*  404 */     this.transportChannel.setExceptionListener(listener);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void start() throws JMSException {
/*  415 */     checkClosed();
/*  416 */     if (this.started.commit(false, true)) {
/*  417 */       sendConnectionInfoToBroker();
/*  418 */       for (Iterator i = this.sessions.iterator(); i.hasNext(); ) {
/*  419 */         ActiveMQSession s = i.next();
/*  420 */         s.start();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isStarted() {
/*  429 */     return this.started.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void stop() throws JMSException {
/*  456 */     checkClosed();
/*  457 */     if (this.started.commit(true, false)) {
/*  458 */       for (Iterator i = this.sessions.iterator(); i.hasNext(); ) {
/*  459 */         ActiveMQSession s = i.next();
/*  460 */         s.stop();
/*      */       } 
/*  462 */       sendConnectionInfoToBroker(2000, this.closed.get());
/*  463 */       this.transportChannel.stop();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void close() throws JMSException {
/*  498 */     this.transportChannel.setPendingStop(true);
/*  499 */     if (!this.closed.get()) {
/*  500 */       this.boundedQueueManager.removeCapacityEventListener(this);
/*      */       try {
/*  502 */         for (Iterator iterator = this.sessions.iterator(); iterator.hasNext(); ) {
/*  503 */           ActiveMQSession s = iterator.next();
/*  504 */           s.close();
/*      */         } 
/*  506 */         for (Iterator i = this.connectionConsumers.iterator(); i.hasNext(); ) {
/*  507 */           ActiveMQConnectionConsumer c = i.next();
/*  508 */           c.close();
/*      */         } 
/*      */         try {
/*  511 */           sendConnectionInfoToBroker(this.sendCloseTimeout, true);
/*      */         }
/*  513 */         catch (TimeoutExpiredException e) {
/*  514 */           log.warn("Failed to send close to broker, timeout expired of: " + this.sendCloseTimeout + " millis");
/*      */         } 
/*  516 */         this.connectionConsumers.clear();
/*  517 */         this.messageDispatchers.clear();
/*  518 */         this.transportChannel.stop();
/*      */       } finally {
/*      */         
/*  521 */         this.sessions.clear();
/*  522 */         this.started.set(false);
/*  523 */         this.factory.onConnectionClose(this);
/*      */       } 
/*  525 */       this.closed.set(true);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected synchronized void checkClosed() throws JMSException {
/*  536 */     if (!this.startedTransport) {
/*  537 */       this.startedTransport = true;
/*  538 */       this.transportChannel.start();
/*      */     } 
/*  540 */     if (this.closed.get()) {
/*  541 */       throw new IllegalStateException("The Connection is closed");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConnectionConsumer createConnectionConsumer(Destination destination, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
/*  566 */     checkClosed();
/*  567 */     ConsumerInfo info = new ConsumerInfo();
/*  568 */     info.setId(this.packetIdGenerator.generateId());
/*  569 */     info.setConsumerId(this.consumerIdGenerator.generateId());
/*  570 */     info.setDestination(ActiveMQMessageTransformation.transformDestination(destination));
/*  571 */     info.setSelector(messageSelector);
/*  572 */     return new ActiveMQConnectionConsumer(this, sessionPool, info, maxMessages);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConnectionConsumer createDurableConnectionConsumer(Topic topic, String subscriptionName, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
/*  597 */     checkClosed();
/*  598 */     ConsumerInfo info = new ConsumerInfo();
/*  599 */     info.setId(this.packetIdGenerator.generateId());
/*  600 */     info.setConsumerId(this.consumerIdGenerator.generateId());
/*  601 */     info.setDestination(ActiveMQMessageTransformation.transformDestination((Destination)topic));
/*  602 */     info.setSelector(messageSelector);
/*  603 */     info.setConsumerName(subscriptionName);
/*  604 */     return new ActiveMQConnectionConsumer(this, sessionPool, info, maxMessages);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void consume(Packet packet) {
/*  614 */     if (!this.closed.get() && packet != null) {
/*  615 */       if (packet.isJMSMessage()) {
/*  616 */         ActiveMQMessage message = (ActiveMQMessage)packet;
/*  617 */         message.setReadOnly(true);
/*  618 */         message.setProducerID(this.clientID);
/*      */         
/*      */         try {
/*  621 */           int count = 0;
/*  622 */           for (Iterator i = this.messageDispatchers.iterator(); i.hasNext(); ) {
/*  623 */             ActiveMQMessageDispatcher dispatcher = i.next();
/*  624 */             if (dispatcher.isTarget(message)) {
/*  625 */               if (count > 0)
/*      */               {
/*  627 */                 message = message.deepCopy();
/*      */               }
/*  629 */               dispatcher.dispatch(message);
/*  630 */               count++;
/*      */             }
/*      */           
/*      */           } 
/*  634 */         } catch (JMSException jmsEx) {
/*  635 */           handleAsyncException(jmsEx);
/*      */         }
/*      */       
/*  638 */       } else if (packet.getPacketType() == 27) {
/*  639 */         CapacityInfo info = (CapacityInfo)packet;
/*  640 */         this.flowControlSleepTime = info.getFlowControlTimeout();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void onException(JMSException jmsEx) {
/*  651 */     handleAsyncException(jmsEx);
/*  652 */     this.isTransportOK = false;
/*      */     try {
/*  654 */       close();
/*      */     }
/*  656 */     catch (JMSException ex) {
/*  657 */       log.warn("Got an exception closing the connection", (Throwable)ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TopicSession createTopicSession(boolean transacted, int acknowledgeMode) throws JMSException {
/*  676 */     checkClosed();
/*  677 */     return new ActiveMQSession(this, transacted ? 0 : acknowledgeMode);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConnectionConsumer createConnectionConsumer(Topic topic, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
/*  698 */     checkClosed();
/*  699 */     ConsumerInfo info = new ConsumerInfo();
/*  700 */     info.setId(this.packetIdGenerator.generateId());
/*  701 */     info.setConsumerId(this.consumerIdGenerator.generateId());
/*  702 */     info.setDestination(ActiveMQMessageTransformation.transformDestination((Destination)topic));
/*  703 */     info.setSelector(messageSelector);
/*  704 */     return new ActiveMQConnectionConsumer(this, sessionPool, info, maxMessages);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public QueueSession createQueueSession(boolean transacted, int acknowledgeMode) throws JMSException {
/*  722 */     checkClosed();
/*  723 */     return new ActiveMQSession(this, transacted ? 0 : acknowledgeMode);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConnectionConsumer createConnectionConsumer(Queue queue, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
/*  744 */     checkClosed();
/*  745 */     ConsumerInfo info = new ConsumerInfo();
/*  746 */     info.setId(this.packetIdGenerator.generateId());
/*  747 */     info.setConsumerId(this.consumerIdGenerator.generateId());
/*  748 */     info.setDestination(ActiveMQMessageTransformation.transformDestination((Destination)queue));
/*  749 */     info.setSelector(messageSelector);
/*  750 */     return new ActiveMQConnectionConsumer(this, sessionPool, info, maxMessages);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void checkClientIDWasManuallySpecified() throws JMSException {
/*  761 */     if (!this.userSpecifiedClientID) {
/*  762 */       throw new JMSException("You cannot create a durable subscriber without specifying a unique clientID on a Connection");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void statusChanged(TransportStatusEvent event) {
/*  772 */     log.info("channel status changed: " + event);
/*  773 */     if (event.getChannelStatus() == 3) {
/*  774 */       doReconnect();
/*      */     }
/*  776 */     else if (event.getChannelStatus() == 2) {
/*  777 */       clearMessagesInProgress();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void asyncSendPacket(Packet packet) throws JMSException {
/*  789 */     asyncSendPacket(packet, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void asyncSendPacket(Packet packet, boolean doSendWhileReconnecting) throws JMSException {
/*  800 */     if (this.isTransportOK && !this.closed.get() && (doSendWhileReconnecting || this.transportChannel.isTransportConnected())) {
/*  801 */       packet.setReceiptRequired(false);
/*  802 */       if (packet.isJMSMessage() && this.flowControlSleepTime > 0L) {
/*      */         try {
/*  804 */           Thread.sleep(this.flowControlSleepTime);
/*      */         }
/*  806 */         catch (InterruptedException e) {}
/*      */       }
/*      */       
/*  809 */       this.transportChannel.asyncSend(packet);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void syncSendPacket(Packet packet) throws JMSException {
/*  820 */     syncSendPacket(packet, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void syncSendPacket(Packet packet, int timeout) throws JMSException {
/*  831 */     if (this.isTransportOK && !this.closed.get()) {
/*      */       
/*  833 */       packet.setReceiptRequired(true);
/*  834 */       Receipt receipt = this.transportChannel.send(packet, timeout);
/*  835 */       if (receipt != null && 
/*  836 */         receipt.isFailed()) {
/*  837 */         Throwable e = receipt.getException();
/*  838 */         if (e != null) {
/*  839 */           throw JMSExceptionHelper.newJMSException(e);
/*      */         }
/*  841 */         throw new JMSException("syncSendPacket failed with unknown exception");
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  846 */       throw new JMSException("syncSendTimedOut");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ActiveMQPrefetchPolicy getPrefetchPolicy() {
/*  865 */     return this.prefetchPolicy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPrefetchPolicy(ActiveMQPrefetchPolicy prefetchPolicy) {
/*  872 */     this.prefetchPolicy = prefetchPolicy;
/*      */   }
/*      */   
/*      */   public int getSendCloseTimeout() {
/*  876 */     return this.sendCloseTimeout;
/*      */   }
/*      */   
/*      */   public void setSendCloseTimeout(int sendCloseTimeout) {
/*  880 */     this.sendCloseTimeout = sendCloseTimeout;
/*      */   }
/*      */   
/*      */   public int getSendConnectionInfoTimeout() {
/*  884 */     return this.sendConnectionInfoTimeout;
/*      */   }
/*      */   
/*      */   public void setSendConnectionInfoTimeout(int sendConnectionInfoTimeout) {
/*  888 */     this.sendConnectionInfoTimeout = sendConnectionInfoTimeout;
/*      */   }
/*      */   
/*      */   public Receipt syncSendRequest(Packet packet) throws JMSException {
/*  892 */     checkClosed();
/*  893 */     if (this.isTransportOK && !this.closed.get()) {
/*      */       
/*  895 */       packet.setReceiptRequired(true);
/*  896 */       if (packet.getId() == null || packet.getId().length() == 0) {
/*  897 */         packet.setId(this.packetIdGenerator.generateId());
/*      */       }
/*  899 */       Receipt receipt = this.transportChannel.send(packet);
/*  900 */       if (receipt.isFailed()) {
/*  901 */         Throwable e = receipt.getException();
/*  902 */         if (e != null) {
/*  903 */           throw (JMSException)(new JMSException(e.getMessage())).initCause(e);
/*      */         }
/*  905 */         throw new JMSException("syncSendPacket failed with unknown exception");
/*      */       } 
/*  907 */       return receipt;
/*      */     } 
/*      */     
/*  910 */     throw new JMSException("Connection closed.");
/*      */   }
/*      */ 
/*      */   
/*      */   public TransportChannel getTransportChannel() {
/*  915 */     return this.transportChannel;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getInitializedClientID() throws JMSException {
/*  923 */     ensureClientIDInitialised();
/*  924 */     return this.clientID;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addSession(ActiveMQSession session) throws JMSException {
/*  938 */     this.sessions.add(session);
/*  939 */     addMessageDispatcher(session);
/*  940 */     if (this.started.get()) {
/*  941 */       session.start();
/*      */     }
/*  943 */     SessionInfo info = createSessionInfo(session);
/*  944 */     info.setStarted(true);
/*  945 */     asyncSendPacket((Packet)info);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void removeSession(ActiveMQSession session) throws JMSException {
/*  955 */     this.sessions.remove(session);
/*  956 */     removeMessageDispatcher(session);
/*  957 */     SessionInfo info = createSessionInfo(session);
/*  958 */     info.setStarted(false);
/*  959 */     asyncSendPacket((Packet)info, false);
/*      */   }
/*      */   
/*      */   private SessionInfo createSessionInfo(ActiveMQSession session) {
/*  963 */     SessionInfo info = new SessionInfo();
/*  964 */     info.setId(this.packetIdGenerator.generateId());
/*  965 */     info.setClientId(this.clientID);
/*  966 */     info.setSessionId(session.getSessionId());
/*  967 */     info.setStartTime(session.getStartTime());
/*  968 */     return info;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addConnectionConsumer(ActiveMQConnectionConsumer connectionConsumer) throws JMSException {
/*  978 */     this.connectionConsumers.add(connectionConsumer);
/*  979 */     addMessageDispatcher(connectionConsumer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void removeConnectionConsumer(ActiveMQConnectionConsumer connectionConsumer) {
/*  988 */     this.connectionConsumers.add(connectionConsumer);
/*  989 */     removeMessageDispatcher(connectionConsumer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addMessageDispatcher(ActiveMQMessageDispatcher messageDispatch) throws JMSException {
/*  999 */     this.messageDispatchers.add(messageDispatch);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void removeMessageDispatcher(ActiveMQMessageDispatcher messageDispatcher) {
/* 1008 */     this.messageDispatchers.remove(messageDispatcher);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void handleAsyncException(JMSException jmsEx) {
/* 1017 */     if (this.exceptionListener != null) {
/* 1018 */       this.exceptionListener.onException(jmsEx);
/*      */     } else {
/*      */       
/* 1021 */       log.warn("async exception with no exception listener", (Throwable)jmsEx);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void sendConnectionInfoToBroker() throws JMSException {
/* 1026 */     sendConnectionInfoToBroker(this.sendConnectionInfoTimeout, this.closed.get());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void sendConnectionInfoToBroker(int timeout, boolean isClosed) throws JMSException {
/* 1037 */     if (!this.isConnectionInfoSentToBroker) {
/* 1038 */       this.isConnectionInfoSentToBroker = true;
/*      */     
/*      */     }
/* 1041 */     else if (!isClosed) {
/*      */       return;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1047 */     ensureClientIDInitialised();
/* 1048 */     ConnectionInfo info = new ConnectionInfo();
/* 1049 */     info.setClientId(this.clientID);
/* 1050 */     info.setHostName(IdGenerator.getHostName());
/* 1051 */     info.setUserName(this.userName);
/* 1052 */     info.setPassword(this.password);
/* 1053 */     info.setId(this.packetIdGenerator.generateId());
/* 1054 */     info.setStartTime(this.startTime);
/* 1055 */     info.setStarted(this.started.get());
/* 1056 */     info.setClosed(isClosed);
/* 1057 */     info.setClientVersion(this.connectionMetaData.getProviderVersion());
/* 1058 */     info.setWireFormatVersion(this.transportChannel.getCurrentWireFormatVersion());
/* 1059 */     syncSendPacket((Packet)info, timeout);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConnectionMemoryLimit(int newMemoryLimit) {
/* 1068 */     this.boundedQueueManager.setValueLimit(newMemoryLimit);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getConnectionMemoryLimit() {
/* 1077 */     return (int)this.boundedQueueManager.getValueLimit();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void capacityChanged(CapacityMonitorEvent event) {
/* 1087 */     CapacityInfo info = new CapacityInfo();
/* 1088 */     info.setId(this.packetIdGenerator.generateId());
/* 1089 */     info.setResourceName(event.getMonitorName());
/* 1090 */     info.setCapacity(event.getCapacity());
/*      */     
/*      */     try {
/* 1093 */       asyncSendPacket((Packet)info, false);
/*      */     }
/* 1095 */     catch (JMSException e) {
/* 1096 */       JMSException jmsEx = new JMSException("failed to send change in capacity");
/* 1097 */       jmsEx.setLinkedException((Exception)e);
/* 1098 */       handleAsyncException(jmsEx);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int getNextConsumerNumber() {
/* 1106 */     return this.consumerNumberGenerator.increment();
/*      */   }
/*      */   
/*      */   protected String generateSessionId() {
/* 1110 */     return this.sessionIdGenerator.generateId();
/*      */   }
/*      */   
/*      */   protected void ensureClientIDInitialised() {
/* 1114 */     if (this.clientID == null) {
/* 1115 */       this.clientID = this.clientIdGenerator.generateId();
/*      */     }
/* 1117 */     this.transportChannel.setClientID(this.clientID);
/* 1118 */     this.clientIDSet = true;
/*      */   }
/*      */   
/*      */   protected MemoryBoundedQueue getMemoryBoundedQueue(String name) {
/* 1122 */     return this.boundedQueueManager.getMemoryBoundedQueue(name);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void doReconnect() {
/*      */     try {
/* 1128 */       this.isConnectionInfoSentToBroker = false;
/* 1129 */       sendConnectionInfoToBroker();
/* 1130 */       for (Iterator iter = this.sessions.iterator(); iter.hasNext(); ) {
/* 1131 */         ActiveMQSession session = iter.next();
/* 1132 */         SessionInfo sessionInfo = createSessionInfo(session);
/* 1133 */         sessionInfo.setStarted(true);
/* 1134 */         asyncSendPacket((Packet)sessionInfo, false);
/*      */         
/* 1136 */         for (Iterator consumersIterator = session.consumers.iterator(); consumersIterator.hasNext(); ) {
/* 1137 */           ActiveMQMessageConsumer consumer = consumersIterator.next();
/* 1138 */           ConsumerInfo consumerInfo = session.createConsumerInfo(consumer);
/* 1139 */           consumerInfo.setStarted(true);
/* 1140 */           asyncSendPacket((Packet)consumerInfo, false);
/*      */         } 
/*      */         
/* 1143 */         for (Iterator producersIterator = session.producers.iterator(); producersIterator.hasNext(); ) {
/* 1144 */           ActiveMQMessageProducer producer = producersIterator.next();
/* 1145 */           ProducerInfo producerInfo = session.createProducerInfo(producer);
/* 1146 */           producerInfo.setStarted(true);
/* 1147 */           asyncSendPacket((Packet)producerInfo, false);
/*      */         }
/*      */       
/*      */       } 
/* 1151 */     } catch (JMSException jmsEx) {
/* 1152 */       log.error("Failed to do reconnection");
/* 1153 */       handleAsyncException(jmsEx);
/* 1154 */       this.isTransportOK = false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isUseAsyncSend() {
/* 1162 */     return this.useAsyncSend;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseAsyncSend(boolean useAsyncSend) {
/* 1169 */     this.useAsyncSend = useAsyncSend;
/*      */   }
/*      */   
/*      */   protected void clearMessagesInProgress() {
/* 1173 */     for (Iterator i = this.sessions.iterator(); i.hasNext(); ) {
/* 1174 */       ActiveMQSession session = i.next();
/* 1175 */       session.clearMessagesInProgress();
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ActiveMQConnection.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */