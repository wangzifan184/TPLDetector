/*     */ package org.codehaus.activemq;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import javax.jms.Connection;
/*     */ import javax.jms.ConnectionFactory;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.QueueConnection;
/*     */ import javax.jms.QueueConnectionFactory;
/*     */ import javax.jms.TopicConnection;
/*     */ import javax.jms.TopicConnectionFactory;
/*     */ import javax.management.j2ee.statistics.Stats;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.broker.Broker;
/*     */ import org.codehaus.activemq.broker.BrokerConnector;
/*     */ import org.codehaus.activemq.broker.BrokerContainer;
/*     */ import org.codehaus.activemq.broker.BrokerContainerFactory;
/*     */ import org.codehaus.activemq.broker.BrokerContext;
/*     */ import org.codehaus.activemq.broker.impl.BrokerClientImpl;
/*     */ import org.codehaus.activemq.broker.impl.BrokerConnectorImpl;
/*     */ import org.codehaus.activemq.broker.impl.BrokerContainerFactoryImpl;
/*     */ import org.codehaus.activemq.jndi.JNDIBaseStorable;
/*     */ import org.codehaus.activemq.management.JMSStatsImpl;
/*     */ import org.codehaus.activemq.management.StatsCapable;
/*     */ import org.codehaus.activemq.message.ActiveMQDestination;
/*     */ import org.codehaus.activemq.message.ActiveMQQueue;
/*     */ import org.codehaus.activemq.message.ActiveMQTopic;
/*     */ import org.codehaus.activemq.message.ConnectionInfo;
/*     */ import org.codehaus.activemq.message.ConsumerInfo;
/*     */ import org.codehaus.activemq.message.DefaultWireFormat;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.service.Service;
/*     */ import org.codehaus.activemq.transport.TransportChannel;
/*     */ import org.codehaus.activemq.transport.TransportChannelFactory;
/*     */ import org.codehaus.activemq.transport.TransportChannelProvider;
/*     */ import org.codehaus.activemq.transport.vm.VmTransportChannel;
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
/*     */ public class ActiveMQConnectionFactory
/*     */   extends JNDIBaseStorable
/*     */   implements ConnectionFactory, QueueConnectionFactory, TopicConnectionFactory, Service, StatsCapable
/*     */ {
/*  74 */   private static final Log log = LogFactory.getLog(ActiveMQConnectionFactory.class);
/*     */   
/*  76 */   private BrokerContext brokerContext = BrokerContext.getInstance();
/*     */   
/*     */   private BrokerContainerFactory brokerContainerFactory;
/*     */   
/*     */   protected BrokerContainer brokerContainer;
/*     */   
/*     */   protected String userName;
/*     */   
/*     */   protected String password;
/*     */   
/*     */   protected String brokerURL;
/*     */   protected String clientID;
/*     */   protected String brokerName;
/*     */   private boolean useEmbeddedBroker;
/*     */   protected boolean useAsyncSend = true;
/*  91 */   private List startedEmbeddedBrokers = new ArrayList();
/*     */   
/*  93 */   private JMSStatsImpl stats = new JMSStatsImpl();
/*  94 */   private WireFormat wireFormat = (WireFormat)new DefaultWireFormat();
/*  95 */   private IdGenerator idGenerator = new IdGenerator();
/*     */   
/*     */   private int connectionCount;
/*     */   
/*     */   private String brokerXmlConfig;
/*     */ 
/*     */   
/*     */   public ActiveMQConnectionFactory() {
/* 103 */     this.userName = "defaultUser";
/* 104 */     this.password = "defaultPassword";
/* 105 */     this.brokerURL = "tcp://localhost:61616";
/*     */   }
/*     */ 
/*     */   
/*     */   public ActiveMQConnectionFactory(String brokerURL) {
/* 110 */     this();
/* 111 */     this.brokerURL = brokerURL;
/*     */   }
/*     */   
/*     */   public ActiveMQConnectionFactory(String userName, String password, String brokerURL) {
/* 115 */     this.userName = userName;
/* 116 */     this.password = password;
/* 117 */     this.brokerURL = brokerURL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQConnectionFactory(BrokerContainer container) {
/* 127 */     this(container, "vm://" + container.getBroker().getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQConnectionFactory(BrokerContainer container, String brokerURL) {
/* 137 */     this();
/* 138 */     this.brokerContainer = container;
/* 139 */     this.useEmbeddedBroker = true;
/* 140 */     this.brokerURL = brokerURL;
/*     */   }
/*     */ 
/*     */   
/*     */   public Stats getStats() {
/* 145 */     return (Stats)this.stats;
/*     */   }
/*     */   
/*     */   public JMSStatsImpl getFactoryStats() {
/* 149 */     return this.stats;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBrokerURL() {
/* 156 */     return this.brokerURL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBrokerURL(String brokerURL) {
/* 163 */     this.brokerURL = brokerURL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClientID() {
/* 170 */     return this.clientID;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClientID(String clientID) {
/* 177 */     this.clientID = clientID;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPassword() {
/* 184 */     return this.password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPassword(String password) {
/* 191 */     this.password = password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUserName() {
/* 198 */     return this.userName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUserName(String userName) {
/* 205 */     this.userName = userName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUseEmbeddedBroker() {
/* 214 */     return this.useEmbeddedBroker;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseEmbeddedBroker(boolean useEmbeddedBroker) {
/* 223 */     this.useEmbeddedBroker = useEmbeddedBroker;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBrokerName() {
/* 232 */     if (this.brokerName == null)
/*     */     {
/* 234 */       this.brokerName = this.idGenerator.generateId();
/*     */     }
/* 236 */     return this.brokerName;
/*     */   }
/*     */   
/*     */   public void setBrokerName(String brokerName) {
/* 240 */     this.brokerName = brokerName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUseAsyncSend() {
/* 247 */     return this.useAsyncSend;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseAsyncSend(boolean useAsyncSend) {
/* 254 */     this.useAsyncSend = useAsyncSend;
/*     */   }
/*     */   
/*     */   public WireFormat getWireFormat() {
/* 258 */     return this.wireFormat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWireFormat(WireFormat wireFormat) {
/* 268 */     this.wireFormat = wireFormat;
/*     */   }
/*     */   
/*     */   public String getBrokerXmlConfig() {
/* 272 */     return this.brokerXmlConfig;
/*     */   }
/*     */   
/*     */   public BrokerContainer getBrokerContainer() {
/* 276 */     return this.brokerContainer;
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
/*     */   public void setBrokerXmlConfig(String brokerXmlConfig) {
/* 290 */     this.brokerXmlConfig = brokerXmlConfig;
/*     */   }
/*     */   
/*     */   public BrokerContainerFactory getBrokerContainerFactory() throws JMSException {
/* 294 */     if (this.brokerContainerFactory == null) {
/* 295 */       this.brokerContainerFactory = createBrokerContainerFactory();
/*     */     }
/* 297 */     return this.brokerContainerFactory;
/*     */   }
/*     */   
/*     */   public void setBrokerContainerFactory(BrokerContainerFactory brokerContainerFactory) {
/* 301 */     this.brokerContainerFactory = brokerContainerFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BrokerContext getBrokerContext() {
/* 309 */     return this.brokerContext;
/*     */   }
/*     */   
/*     */   public void setBrokerContext(BrokerContext brokerContext) {
/* 313 */     this.brokerContext = brokerContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection createConnection() throws JMSException {
/* 323 */     return createConnection(this.userName, this.password);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection createConnection(String userName, String password) throws JMSException {
/* 333 */     ActiveMQConnection connection = new ActiveMQConnection(this, userName, password, createTransportChannel(this.brokerURL));
/* 334 */     connection.setUseAsyncSend(isUseAsyncSend());
/* 335 */     if (this.clientID != null && this.clientID.length() > 0) {
/* 336 */       connection.setClientID(this.clientID);
/*     */     }
/* 338 */     return connection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QueueConnection createQueueConnection() throws JMSException {
/* 348 */     return createQueueConnection(this.userName, this.password);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QueueConnection createQueueConnection(String userName, String password) throws JMSException {
/* 358 */     return (QueueConnection)createConnection(userName, password);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TopicConnection createTopicConnection() throws JMSException {
/* 368 */     return createTopicConnection(this.userName, this.password);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TopicConnection createTopicConnection(String userName, String password) throws JMSException {
/* 378 */     return (TopicConnection)createConnection(userName, password);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() throws JMSException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void stop() throws JMSException {
/* 392 */     for (Iterator iter = this.startedEmbeddedBrokers.iterator(); iter.hasNext(); ) {
/* 393 */       String uri = iter.next();
/* 394 */       this.brokerContext.deregisterConnector(uri);
/*     */     } 
/* 396 */     if (this.brokerContainer != null) {
/* 397 */       this.brokerContainer.stop();
/* 398 */       this.brokerContainer = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Broker getEmbeddedBroker() throws JMSException {
/* 404 */     if (isUseEmbeddedBroker()) {
/* 405 */       return getContainer(getBrokerName()).getBroker();
/*     */     }
/* 407 */     return null;
/*     */   }
/*     */   
/*     */   public static synchronized void registerBroker(String theURLString, BrokerConnector brokerConnector) {
/* 411 */     BrokerContext.getInstance().registerConnector(theURLString, brokerConnector);
/*     */   }
/*     */   
/*     */   public static synchronized void unregisterBroker(String theURLString) {
/* 415 */     BrokerContext.getInstance().deregisterConnector(theURLString);
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
/*     */   protected void buildFromProperties(Properties props) {
/* 429 */     this.userName = props.getProperty("userName", this.userName);
/* 430 */     this.password = props.getProperty("password", this.password);
/* 431 */     this.brokerURL = props.getProperty("brokerURL", this.brokerURL);
/* 432 */     this.brokerName = props.getProperty("brokerName", this.brokerName);
/* 433 */     this.clientID = props.getProperty("clientID");
/* 434 */     this.useAsyncSend = getBoolean(props, "useAsyncSend", true);
/* 435 */     this.useEmbeddedBroker = getBoolean(props, "useEmbeddedBroker");
/* 436 */     this.brokerXmlConfig = props.getProperty("brokerXmlConfig", this.brokerXmlConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void populateProperties(Properties props) {
/* 445 */     props.put("userName", this.userName);
/* 446 */     props.put("password", this.password);
/* 447 */     props.put("brokerURL", this.brokerURL);
/* 448 */     props.put("brokerName", this.brokerName);
/* 449 */     if (this.clientID != null) {
/* 450 */       props.put("clientID", this.clientID);
/*     */     }
/* 452 */     props.put("useAsyncSend", this.useAsyncSend ? "true" : "false");
/* 453 */     props.put("useEmbeddedBroker", this.useEmbeddedBroker ? "true" : "false");
/* 454 */     if (this.brokerXmlConfig != null) {
/* 455 */       props.put("brokerXmlConfig", this.brokerXmlConfig);
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
/*     */   protected boolean getBoolean(Properties props, String key) {
/* 467 */     return getBoolean(props, key, false);
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
/*     */   protected boolean getBoolean(Properties props, String key, boolean defaultValue) {
/* 479 */     String value = props.getProperty(key);
/* 480 */     return (value != null) ? value.equalsIgnoreCase("true") : defaultValue;
/*     */   }
/*     */   
/*     */   protected BrokerContainerFactory createBrokerContainerFactory() throws JMSException {
/* 484 */     if (this.brokerXmlConfig != null) {
/* 485 */       return XmlConfigHelper.createBrokerContainerFactory(this.brokerXmlConfig);
/*     */     }
/* 487 */     return (BrokerContainerFactory)new BrokerContainerFactoryImpl();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TransportChannel createTransportChannel(String theURLString) throws JMSException {
/* 494 */     URI uri = createURI(theURLString);
/*     */     
/* 496 */     TransportChannelFactory factory = TransportChannelProvider.getFactory(uri);
/*     */ 
/*     */     
/* 499 */     BrokerConnector brokerConnector = null;
/* 500 */     boolean created = false;
/* 501 */     boolean embedServer = (isUseEmbeddedBroker() || factory.requiresEmbeddedBroker());
/* 502 */     if (embedServer) {
/* 503 */       synchronized (this) {
/* 504 */         brokerConnector = this.brokerContext.getConnectorByURL(theURLString);
/* 505 */         if (brokerConnector == null) {
/* 506 */           brokerConnector = createBrokerConnector(theURLString);
/* 507 */           this.brokerContext.registerConnector(theURLString, brokerConnector);
/* 508 */           this.startedEmbeddedBrokers.add(theURLString);
/* 509 */           created = true;
/*     */         } 
/*     */       } 
/*     */     }
/* 513 */     TransportChannel transportChannel = factory.create(getWireFormat(), uri);
/* 514 */     if (embedServer) {
/* 515 */       return ensureServerIsAvailable(uri, transportChannel, brokerConnector, created);
/*     */     }
/* 517 */     return transportChannel;
/*     */   }
/*     */   
/*     */   protected synchronized BrokerContainer getContainer(String brokerName) throws JMSException {
/* 521 */     if (this.brokerContainer == null) {
/* 522 */       this.brokerContainer = this.brokerContext.getBrokerContainerByName(brokerName, getBrokerContainerFactory());
/*     */     }
/* 524 */     return this.brokerContainer;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BrokerConnector createBrokerConnector(String url) throws JMSException {
/* 529 */     BrokerConnectorImpl brokerConnectorImpl = new BrokerConnectorImpl(getContainer(getBrokerName()), url, getWireFormat());
/* 530 */     brokerConnectorImpl.start();
/*     */ 
/*     */     
/* 533 */     log.info("Embedded JMS Broker has started");
/*     */     try {
/* 535 */       Thread.sleep(1000L);
/*     */     }
/* 537 */     catch (InterruptedException e) {
/* 538 */       System.out.println("Caught: " + e);
/* 539 */       e.printStackTrace();
/*     */     } 
/* 541 */     return (BrokerConnector)brokerConnectorImpl;
/*     */   }
/*     */ 
/*     */   
/*     */   protected TransportChannel ensureServerIsAvailable(URI remoteLocation, TransportChannel channel, BrokerConnector brokerConnector, boolean created) throws JMSException {
/* 546 */     ensureVmServerIsAvailable(channel, brokerConnector);
/* 547 */     if (channel.isMulticast()) {
/* 548 */       return ensureMulticastChannelIsAvailable(remoteLocation, channel, brokerConnector, created);
/*     */     }
/* 550 */     return channel;
/*     */   }
/*     */   
/*     */   private void ensureVmServerIsAvailable(TransportChannel channel, BrokerConnector brokerConnector) throws JMSException {
/* 554 */     if (channel instanceof VmTransportChannel && brokerConnector instanceof org.codehaus.activemq.transport.TransportChannelListener) {
/* 555 */       VmTransportChannel answer = (VmTransportChannel)channel;
/* 556 */       answer.connect(brokerConnector);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected TransportChannel ensureMulticastChannelIsAvailable(URI remoteLocation, TransportChannel channel, BrokerConnector brokerConnector, boolean created) throws JMSException {
/* 561 */     if (created) {
/* 562 */       BrokerConnectorImpl brokerImpl = (BrokerConnectorImpl)brokerConnector;
/*     */       
/* 564 */       BrokerClientImpl client = new BrokerClientImpl();
/* 565 */       client.initialize((BrokerConnector)brokerImpl, channel);
/* 566 */       channel.start();
/* 567 */       String brokerClientID = createMulticastClientID();
/* 568 */       channel.setClientID(brokerClientID);
/*     */ 
/*     */ 
/*     */       
/* 572 */       ConnectionInfo info = new ConnectionInfo();
/* 573 */       info.setHostName(IdGenerator.getHostName());
/* 574 */       info.setClientId(brokerClientID);
/* 575 */       info.setStarted(true);
/* 576 */       client.consumeConnectionInfo(info);
/*     */       
/* 578 */       ConsumerInfo consumerInfo = new ConsumerInfo();
/* 579 */       consumerInfo.setDestination((ActiveMQDestination)new ActiveMQTopic(">"));
/* 580 */       consumerInfo.setNoLocal(true);
/* 581 */       consumerInfo.setClientId(brokerClientID);
/* 582 */       consumerInfo.setConsumerId(this.idGenerator.generateId());
/* 583 */       consumerInfo.setId(consumerInfo.getConsumerId());
/* 584 */       consumerInfo.setStarted(true);
/* 585 */       client.consumeConsumerInfo(consumerInfo);
/*     */       
/* 587 */       consumerInfo = new ConsumerInfo();
/* 588 */       consumerInfo.setDestination((ActiveMQDestination)new ActiveMQQueue(">"));
/* 589 */       consumerInfo.setNoLocal(true);
/* 590 */       consumerInfo.setClientId(brokerClientID);
/* 591 */       consumerInfo.setConsumerId(this.idGenerator.generateId());
/* 592 */       consumerInfo.setId(consumerInfo.getConsumerId());
/* 593 */       consumerInfo.setStarted(true);
/* 594 */       client.consumeConsumerInfo(consumerInfo);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 599 */     URI localURI = createURI("vm", remoteLocation);
/* 600 */     TransportChannel localChannel = TransportChannelProvider.create(getWireFormat(), localURI);
/* 601 */     ensureVmServerIsAvailable(localChannel, brokerConnector);
/* 602 */     return localChannel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String createMulticastClientID() {
/* 610 */     return this.idGenerator.generateId();
/*     */   }
/*     */   
/*     */   protected URI createURI(String protocol, URI uri) throws JMSException {
/*     */     try {
/* 615 */       return new URI(protocol, uri.getRawSchemeSpecificPart(), uri.getFragment());
/*     */     }
/* 617 */     catch (URISyntaxException e) {
/* 618 */       JMSException jmsEx = new JMSException("the URL string is badly formated:", e.getMessage());
/* 619 */       jmsEx.setLinkedException(e);
/* 620 */       throw jmsEx;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected URI createURI(String uri) throws JMSException {
/*     */     try {
/* 627 */       if (uri == null) {
/* 628 */         throw new JMSException("The connection URI must be specified!");
/*     */       }
/* 630 */       return new URI(uri);
/*     */     }
/* 632 */     catch (URISyntaxException e) {
/* 633 */       JMSException jmsEx = new JMSException("the URL string is badly formated:", e.getMessage());
/* 634 */       jmsEx.setLinkedException(e);
/* 635 */       throw jmsEx;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   synchronized void onConnectionClose(ActiveMQConnection connection) throws JMSException {
/* 646 */     if (--this.connectionCount <= 0)
/*     */     {
/* 648 */       stop();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   synchronized void onConnectionCreate(ActiveMQConnection connection) {
/* 654 */     this.connectionCount++;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ActiveMQConnectionFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */