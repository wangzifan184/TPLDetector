/*     */ package org.codehaus.activemq.broker.impl;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;
/*     */ import EDU.oswego.cs.dl.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.io.File;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.jms.Destination;
/*     */ import javax.jms.JMSException;
/*     */ import javax.naming.Context;
/*     */ import javax.transaction.xa.XAException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.broker.Broker;
/*     */ import org.codehaus.activemq.broker.BrokerClient;
/*     */ import org.codehaus.activemq.broker.ConsumerInfoListener;
/*     */ import org.codehaus.activemq.capacity.CapacityMonitor;
/*     */ import org.codehaus.activemq.capacity.DelegateCapacityMonitor;
/*     */ import org.codehaus.activemq.jndi.ReadOnlyContext;
/*     */ import org.codehaus.activemq.message.ActiveMQDestination;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.ActiveMQXid;
/*     */ import org.codehaus.activemq.message.ConnectionInfo;
/*     */ import org.codehaus.activemq.message.ConsumerInfo;
/*     */ import org.codehaus.activemq.message.MessageAck;
/*     */ import org.codehaus.activemq.message.ProducerInfo;
/*     */ import org.codehaus.activemq.message.util.MemoryBoundedQueueManager;
/*     */ import org.codehaus.activemq.security.SecurityAdapter;
/*     */ import org.codehaus.activemq.service.MessageContainerManager;
/*     */ import org.codehaus.activemq.service.RedeliveryPolicy;
/*     */ import org.codehaus.activemq.service.Transaction;
/*     */ import org.codehaus.activemq.service.TransactionManager;
/*     */ import org.codehaus.activemq.service.TransactionTask;
/*     */ import org.codehaus.activemq.service.boundedvm.TransientQueueBoundedMessageManager;
/*     */ import org.codehaus.activemq.service.boundedvm.TransientTopicBoundedMessageManager;
/*     */ import org.codehaus.activemq.service.impl.DurableQueueMessageContainerManager;
/*     */ import org.codehaus.activemq.service.impl.DurableTopicMessageContainerManager;
/*     */ import org.codehaus.activemq.service.impl.MessageAckTransactionTask;
/*     */ import org.codehaus.activemq.service.impl.RedeliverMessageTransactionTask;
/*     */ import org.codehaus.activemq.service.impl.SendMessageTransactionTask;
/*     */ import org.codehaus.activemq.store.PersistenceAdapter;
/*     */ import org.codehaus.activemq.store.PreparedTransactionStore;
/*     */ import org.codehaus.activemq.store.vm.VMPersistenceAdapter;
/*     */ import org.codehaus.activemq.store.vm.VMTransactionManager;
/*     */ import org.codehaus.activemq.util.Callback;
/*     */ import org.codehaus.activemq.util.ExceptionTemplate;
/*     */ import org.codehaus.activemq.util.JMSExceptionHelper;
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
/*     */ public class DefaultBroker
/*     */   extends DelegateCapacityMonitor
/*     */   implements Broker
/*     */ {
/*  75 */   private static final Log log = LogFactory.getLog(DefaultBroker.class);
/*     */   
/*     */   protected static final String PROPERTY_STORE_DIRECTORY = "activemq.store.dir";
/*     */   
/*     */   protected static final String PERSISTENCE_ADAPTER_PROPERTY = "activemq.persistenceAdapter";
/*  80 */   protected static final Class[] NEWINSTANCE_PARAMETER_TYPES = new Class[] { File.class };
/*     */   
/*     */   private static final long DEFAULT_MAX_MEMORY_USAGE = 20971520L;
/*     */   
/*     */   private PersistenceAdapter persistenceAdapter;
/*     */   
/*     */   private TransactionManager transactionManager;
/*     */   private MessageContainerManager[] containerManagers;
/*     */   private File tempDir;
/*     */   private MemoryBoundedQueueManager memoryManager;
/*     */   private PreparedTransactionStore preparedTransactionStore;
/*     */   private final String brokerName;
/*     */   private final String brokerClusterName;
/*     */   private Map containerManagerMap;
/*     */   private CopyOnWriteArrayList consumerInfoListeners;
/*     */   private MessageContainerManager persistentTopicMCM;
/*     */   private MessageContainerManager transientTopicMCM;
/*     */   private MessageContainerManager persistentQueueMCM;
/*     */   private MessageContainerManager transientQueueMCM;
/*     */   private SecurityAdapter securityAdapter;
/*     */   private RedeliveryPolicy redeliveryPolicy;
/*     */   
/*     */   public DefaultBroker(String brokerName, String brokerClusterName) {
/* 103 */     this.brokerName = brokerName;
/* 104 */     this.brokerClusterName = brokerClusterName;
/* 105 */     this.memoryManager = new MemoryBoundedQueueManager("Broker Memory Manager", 20971520L);
/* 106 */     setDelegate((CapacityMonitor)this.memoryManager);
/* 107 */     this.containerManagerMap = (Map)new ConcurrentHashMap();
/* 108 */     this.consumerInfoListeners = new CopyOnWriteArrayList();
/*     */   }
/*     */   
/*     */   public DefaultBroker(String brokerName) {
/* 112 */     this(brokerName, "default");
/*     */   }
/*     */   
/*     */   public DefaultBroker(String brokerName, String brokerClusterName, PersistenceAdapter persistenceAdapter) {
/* 116 */     this(brokerName, brokerClusterName);
/* 117 */     this.persistenceAdapter = persistenceAdapter;
/*     */   }
/*     */   
/*     */   public DefaultBroker(String brokerName, PersistenceAdapter persistenceAdapter) {
/* 121 */     this(brokerName);
/* 122 */     this.persistenceAdapter = persistenceAdapter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() throws JMSException {
/* 131 */     if (this.redeliveryPolicy == null) {
/* 132 */       this.redeliveryPolicy = new RedeliveryPolicy();
/*     */     }
/* 134 */     if (this.persistenceAdapter == null) {
/* 135 */       this.persistenceAdapter = createPersistenceAdapter();
/*     */     }
/* 137 */     this.persistenceAdapter.start();
/*     */     
/* 139 */     if (this.transactionManager == null) {
/* 140 */       this.preparedTransactionStore = this.persistenceAdapter.createPreparedTransactionStore();
/* 141 */       this.transactionManager = (TransactionManager)new VMTransactionManager(this, this.preparedTransactionStore);
/*     */     } 
/* 143 */     this.transactionManager.start();
/*     */ 
/*     */     
/* 146 */     if (this.containerManagerMap.isEmpty()) {
/* 147 */       makeDefaultContainerManagers();
/*     */     }
/* 149 */     getContainerManagers();
/*     */     
/* 151 */     for (int i = 0; i < this.containerManagers.length; i++) {
/* 152 */       this.containerManagers[i].start();
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
/*     */   public void stop() throws JMSException {
/* 164 */     ExceptionTemplate template = new ExceptionTemplate();
/*     */     
/* 166 */     if (this.containerManagers != null)
/* 167 */       for (int i = 0; i < this.containerManagers.length; i++) {
/* 168 */         final MessageContainerManager containerManager = this.containerManagers[i];
/* 169 */         template.run(new Callback() {
/*     */               public void execute() throws Throwable {
/* 171 */                 containerManager.stop();
/*     */               }
/*     */               private final MessageContainerManager val$containerManager; private final DefaultBroker this$0;
/*     */             });
/*     */       }  
/* 176 */     if (this.transactionManager != null) {
/* 177 */       template.run(new Callback() { private final DefaultBroker this$0;
/*     */             public void execute() throws Throwable {
/* 179 */               DefaultBroker.this.transactionManager.stop();
/*     */             } }
/*     */         );
/*     */     }
/*     */     
/* 184 */     template.run(new Callback() { private final DefaultBroker this$0;
/*     */           public void execute() throws Throwable {
/* 186 */             DefaultBroker.this.persistenceAdapter.stop();
/*     */           } }
/*     */       );
/*     */     
/* 190 */     template.throwJMSException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addClient(BrokerClient client, ConnectionInfo info) throws JMSException {
/* 197 */     if (this.securityAdapter != null) {
/* 198 */       this.securityAdapter.authorizeConnection(client, info);
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeClient(BrokerClient client, ConnectionInfo info) throws JMSException {
/* 203 */     if (this.transactionManager != null) {
/* 204 */       this.transactionManager.cleanUpClient(client);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addMessageProducer(BrokerClient client, ProducerInfo info) throws JMSException {
/* 209 */     if (this.securityAdapter != null) {
/* 210 */       this.securityAdapter.authorizeProducer(client, info);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeMessageProducer(BrokerClient client, ProducerInfo info) throws JMSException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMessageConsumer(BrokerClient client, ConsumerInfo info) throws JMSException {
/* 221 */     validateConsumer(info);
/* 222 */     if (this.securityAdapter != null) {
/* 223 */       this.securityAdapter.authorizeConsumer(client, info);
/*     */     }
/*     */     
/* 226 */     MessageContainerManager[] array = getContainerManagers();
/* 227 */     for (int i = 0; i < array.length; i++) {
/* 228 */       array[i].addMessageConsumer(client, info);
/*     */     }
/* 230 */     fireConsumerInfo(client, info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeMessageConsumer(BrokerClient client, ConsumerInfo info) throws JMSException {
/* 237 */     validateConsumer(info);
/* 238 */     for (int i = 0; i < this.containerManagers.length; i++) {
/* 239 */       this.containerManagers[i].removeMessageConsumer(client, info);
/*     */     }
/* 241 */     fireConsumerInfo(client, info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendMessage(BrokerClient client, ActiveMQMessage message) throws JMSException {
/* 249 */     checkValid();
/* 250 */     if (message.getJMSMessageID() == null) {
/* 251 */       throw new JMSException("No messageID specified for the Message");
/*     */     }
/* 253 */     ActiveMQDestination destination = message.getJMSActiveMQDestination();
/* 254 */     if (destination.isComposite()) {
/* 255 */       boolean first = true;
/*     */       
/* 257 */       for (Iterator iter = destination.getChildDestinations().iterator(); iter.hasNext(); ) {
/* 258 */         ActiveMQDestination childDestination = iter.next();
/*     */ 
/*     */         
/* 261 */         if (first) {
/* 262 */           first = false;
/*     */         } else {
/*     */           
/* 265 */           message = message.shallowCopy();
/*     */         } 
/* 267 */         message.setJMSDestination((Destination)childDestination);
/*     */         
/* 269 */         doMessageSend(client, message);
/*     */       } 
/*     */     } else {
/*     */       
/* 273 */       doMessageSend(client, message);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendTransactedMessage(BrokerClient client, String transactionId, ActiveMQMessage message) throws JMSException {
/*     */     Transaction transaction;
/* 282 */     if (message.isXaTransacted()) {
/*     */       try {
/* 284 */         transaction = this.transactionManager.getXATransaction(new ActiveMQXid(transactionId));
/*     */       }
/* 286 */       catch (XAException e) {
/* 287 */         throw (JMSException)(new JMSException(e.getMessage())).initCause(e);
/*     */       } 
/*     */     } else {
/*     */       
/* 291 */       transaction = this.transactionManager.getLocalTransaction(transactionId);
/*     */     } 
/*     */     
/* 294 */     transaction.addPostCommitTask((TransactionTask)new SendMessageTransactionTask(client, message));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void acknowledgeMessage(BrokerClient client, MessageAck ack) throws JMSException {
/* 302 */     for (int i = 0; i < this.containerManagers.length; i++) {
/* 303 */       this.containerManagers[i].acknowledgeMessage(client, ack);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void acknowledgeTransactedMessage(BrokerClient client, String transactionId, MessageAck ack) throws JMSException {
/*     */     Transaction transaction;
/* 312 */     if (ack.isXaTransacted()) {
/*     */       try {
/* 314 */         transaction = this.transactionManager.getXATransaction(new ActiveMQXid(transactionId));
/*     */       }
/* 316 */       catch (XAException e) {
/* 317 */         throw (JMSException)(new JMSException(e.getMessage())).initCause(e);
/*     */       } 
/*     */     } else {
/*     */       
/* 321 */       transaction = this.transactionManager.getLocalTransaction(transactionId);
/*     */     } 
/* 323 */     transaction.addPostCommitTask((TransactionTask)new MessageAckTransactionTask(client, ack));
/* 324 */     transaction.addPostRollbackTask((TransactionTask)new RedeliverMessageTransactionTask(client, ack));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 330 */     for (int i = 0; i < this.containerManagers.length; i++) {
/* 331 */       this.containerManagers[i].acknowledgeTransactedMessage(client, transactionId, ack);
/*     */     }
/*     */   }
/*     */   
/*     */   public void redeliverMessage(BrokerClient client, MessageAck ack) throws JMSException {
/* 336 */     for (int i = 0; i < this.containerManagers.length; i++) {
/* 337 */       this.containerManagers[i].redeliverMessage(client, ack);
/*     */     }
/*     */   }
/*     */   
/*     */   public void deleteSubscription(String clientId, String subscriberName) throws JMSException {
/* 342 */     for (int i = 0; i < this.containerManagers.length; i++) {
/* 343 */       this.containerManagers[i].deleteSubscription(clientId, subscriberName);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startTransaction(BrokerClient client, String transactionId) throws JMSException {
/* 354 */     this.transactionManager.createLocalTransaction(client, transactionId);
/*     */   }
/*     */   
/*     */   public void commitTransaction(BrokerClient client, String transactionId) throws JMSException {
/*     */     try {
/* 359 */       for (int i = 0; i < this.containerManagers.length; i++) {
/* 360 */         this.containerManagers[i].commitTransaction(client, transactionId);
/*     */       }
/* 362 */       Transaction transaction = this.transactionManager.getLocalTransaction(transactionId);
/* 363 */       transaction.commit(true);
/*     */     }
/* 365 */     catch (XAException e) {
/*     */       
/* 367 */       throw (JMSException)(new JMSException(e.getMessage())).initCause(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rollbackTransaction(BrokerClient client, String transactionId) throws JMSException {
/*     */     try {
/* 376 */       for (int i = 0; i < this.containerManagers.length; i++) {
/* 377 */         this.containerManagers[i].rollbackTransaction(client, transactionId);
/*     */       }
/* 379 */       Transaction transaction = this.transactionManager.getLocalTransaction(transactionId);
/* 380 */       transaction.rollback();
/*     */     }
/* 382 */     catch (XAException e) {
/*     */       
/* 384 */       throw (JMSException)(new JMSException(e.getMessage())).initCause(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startTransaction(BrokerClient client, ActiveMQXid xid) throws XAException {
/* 394 */     this.transactionManager.createXATransaction(client, xid);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int prepareTransaction(BrokerClient client, ActiveMQXid xid) throws XAException {
/* 403 */     Transaction transaction = this.transactionManager.getXATransaction(xid);
/* 404 */     return transaction.prepare();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rollbackTransaction(BrokerClient client, ActiveMQXid xid) throws XAException {
/* 413 */     Transaction transaction = this.transactionManager.getXATransaction(xid);
/* 414 */     transaction.rollback();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void commitTransaction(BrokerClient client, ActiveMQXid xid, boolean onePhase) throws XAException {
/* 423 */     Transaction transaction = this.transactionManager.getXATransaction(xid);
/* 424 */     transaction.commit(onePhase);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQXid[] getPreparedTransactions(BrokerClient client) throws XAException {
/* 433 */     return this.transactionManager.getPreparedXATransactions();
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
/*     */   public File getTempDir() {
/* 446 */     if (this.tempDir == null) {
/* 447 */       String dirName = System.getProperty("activemq.store.tempdir", "ActiveMQTemp");
/* 448 */       this.tempDir = new File(dirName);
/*     */     } 
/* 450 */     return this.tempDir;
/*     */   }
/*     */   
/*     */   public String getBrokerName() {
/* 454 */     return this.brokerName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBrokerClusterName() {
/* 461 */     return this.brokerClusterName;
/*     */   }
/*     */   
/*     */   public void setTempDir(File tempDir) {
/* 465 */     this.tempDir = tempDir;
/*     */   }
/*     */   
/*     */   public MessageContainerManager[] getContainerManagers() {
/* 469 */     if (this.containerManagers == null) {
/* 470 */       this.containerManagers = createContainerManagers();
/*     */     }
/* 472 */     return this.containerManagers;
/*     */   }
/*     */   
/*     */   public Map getContainerManagerMap() {
/* 476 */     return this.containerManagerMap;
/*     */   }
/*     */   
/*     */   public void setContainerManagerMap(Map containerManagerMap) {
/* 480 */     this.containerManagerMap = containerManagerMap;
/* 481 */     this.containerManagers = null;
/*     */   }
/*     */   
/*     */   public PersistenceAdapter getPersistenceAdapter() {
/* 485 */     return this.persistenceAdapter;
/*     */   }
/*     */   
/*     */   public void setPersistenceAdapter(PersistenceAdapter persistenceAdapter) {
/* 489 */     this.persistenceAdapter = persistenceAdapter;
/*     */   }
/*     */   
/*     */   public TransactionManager getTransactionManager() {
/* 493 */     return this.transactionManager;
/*     */   }
/*     */   
/*     */   public void setTransactionManager(TransactionManager transactionManager) {
/* 497 */     this.transactionManager = transactionManager;
/*     */   }
/*     */   
/*     */   public SecurityAdapter getSecurityAdapter() {
/* 501 */     return this.securityAdapter;
/*     */   }
/*     */   
/*     */   public void setSecurityAdapter(SecurityAdapter securityAdapter) {
/* 505 */     this.securityAdapter = securityAdapter;
/*     */   }
/*     */   
/*     */   public RedeliveryPolicy getRedeliveryPolicy() {
/* 509 */     return this.redeliveryPolicy;
/*     */   }
/*     */   
/*     */   public void setRedeliveryPolicy(RedeliveryPolicy redeliveryPolicy) {
/* 513 */     this.redeliveryPolicy = redeliveryPolicy;
/*     */   }
/*     */   
/*     */   public PreparedTransactionStore getPreparedTransactionStore() {
/* 517 */     return this.preparedTransactionStore;
/*     */   }
/*     */   
/*     */   public void setPreparedTransactionStore(PreparedTransactionStore preparedTransactionStore) {
/* 521 */     this.preparedTransactionStore = preparedTransactionStore;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMaximumMemoryUsage() {
/* 528 */     return this.memoryManager.getValueLimit();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaximumMemoryUsage(long maximumMemoryUsage) {
/* 535 */     this.memoryManager.setValueLimit(maximumMemoryUsage);
/*     */   }
/*     */ 
/*     */   
/*     */   public Context getDestinationContext(Hashtable environment) {
/* 540 */     ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
/* 541 */     for (Iterator iter = this.containerManagerMap.entrySet().iterator(); iter.hasNext(); ) {
/* 542 */       Map.Entry entry = iter.next();
/* 543 */       String name = entry.getKey().toString();
/* 544 */       MessageContainerManager manager = (MessageContainerManager)entry.getValue();
/* 545 */       ReadOnlyContext readOnlyContext = new ReadOnlyContext(environment, manager.getDestinations());
/* 546 */       concurrentHashMap.put(name, readOnlyContext);
/*     */     } 
/* 548 */     return (Context)new ReadOnlyContext(environment, (Map)concurrentHashMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doMessageSend(BrokerClient client, ActiveMQMessage message) throws JMSException {
/* 556 */     if (this.securityAdapter != null) {
/* 557 */       this.securityAdapter.authorizeSendMessage(client, message);
/*     */     }
/* 559 */     for (int i = 0; i < this.containerManagers.length; i++) {
/* 560 */       this.containerManagers[i].sendMessage(client, message);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PersistenceAdapter createPersistenceAdapter() throws JMSException {
/* 570 */     File directory = new File(getStoreDirectory());
/*     */ 
/*     */     
/* 573 */     PersistenceAdapter answer = null;
/* 574 */     String property = System.getProperty("activemq.persistenceAdapter");
/* 575 */     if (property != null) {
/* 576 */       answer = tryCreatePersistenceAdapter(property, directory, false);
/*     */     }
/* 578 */     if (answer == null) {
/* 579 */       answer = tryCreatePersistenceAdapter("org.codehaus.activemq.store.jdbm.JdbmPersistenceAdapter", directory, true);
/*     */     }
/* 581 */     if (answer == null) {
/* 582 */       answer = tryCreatePersistenceAdapter("org.codehaus.activemq.store.bdb.BDbPersistenceAdapter", directory, true);
/*     */     }
/* 584 */     if (answer != null) {
/* 585 */       return answer;
/*     */     }
/*     */     
/* 588 */     log.warn("Neither JDBM or BDB on the classpath or property 'activemq.persistenceAdapter' not specified so defaulting to use RAM based message persistence");
/*     */     
/* 590 */     return (PersistenceAdapter)new VMPersistenceAdapter();
/*     */   }
/*     */ 
/*     */   
/*     */   protected PersistenceAdapter tryCreatePersistenceAdapter(String className, File directory, boolean ignoreErrors) throws JMSException {
/* 595 */     Class adapterClass = loadClass(className, ignoreErrors);
/* 596 */     if (adapterClass != null) {
/*     */       
/*     */       try {
/* 599 */         Method method = adapterClass.getMethod("newInstance", NEWINSTANCE_PARAMETER_TYPES);
/* 600 */         PersistenceAdapter answer = (PersistenceAdapter)method.invoke(null, new Object[] { directory });
/* 601 */         log.info("Using persistence adapter: " + adapterClass.getName());
/* 602 */         return answer;
/*     */       }
/* 604 */       catch (InvocationTargetException e) {
/* 605 */         Throwable cause = e.getTargetException();
/* 606 */         if (cause != null) {
/* 607 */           if (cause instanceof JMSException) {
/* 608 */             throw (JMSException)cause;
/*     */           }
/*     */           
/* 611 */           if (cause instanceof Exception) {
/* 612 */             throw createInstantiateAdapterException(adapterClass, (Exception)cause);
/*     */           }
/*     */         } 
/*     */         
/* 616 */         if (!ignoreErrors) {
/* 617 */           throw createInstantiateAdapterException(adapterClass, e);
/*     */         }
/*     */       }
/* 620 */       catch (Throwable e) {
/* 621 */         if (!ignoreErrors) {
/* 622 */           throw createInstantiateAdapterException(adapterClass, e);
/*     */         }
/*     */       } 
/*     */     }
/* 626 */     return null;
/*     */   }
/*     */   
/*     */   protected JMSException createInstantiateAdapterException(Class adapterClass, Throwable e) {
/* 630 */     return JMSExceptionHelper.newJMSException("Could not instantiate instance of " + adapterClass.getName() + ". Reason: " + e, e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class loadClass(String name, boolean ignoreErrors) throws JMSException {
/*     */     try {
/* 640 */       return Thread.currentThread().getContextClassLoader().loadClass(name);
/*     */     }
/* 642 */     catch (ClassNotFoundException e) {
/*     */       try {
/* 644 */         return getClass().getClassLoader().loadClass(name);
/*     */       }
/* 646 */       catch (ClassNotFoundException e2) {
/* 647 */         if (ignoreErrors) {
/* 648 */           log.trace("Could not find class: " + name + " on the classpath");
/* 649 */           return null;
/*     */         } 
/*     */         
/* 652 */         throw JMSExceptionHelper.newJMSException("Could not find class: " + name + " on the classpath. Reason: " + e, e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getStoreDirectory() {
/* 659 */     return System.getProperty("activemq.store.dir", "ActiveMQ");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MessageContainerManager[] createContainerManagers() {
/* 668 */     int size = this.containerManagerMap.size();
/* 669 */     MessageContainerManager[] answer = new MessageContainerManager[size];
/* 670 */     this.containerManagerMap.values().toArray((Object[])answer);
/* 671 */     return answer;
/*     */   }
/*     */   
/*     */   protected void makeDefaultContainerManagers() {
/* 675 */     this.transientTopicMCM = (MessageContainerManager)new TransientTopicBoundedMessageManager(this.memoryManager);
/* 676 */     this.containerManagerMap.put("transientTopicContainer", this.transientTopicMCM);
/* 677 */     this.persistentTopicMCM = (MessageContainerManager)new DurableTopicMessageContainerManager(this.persistenceAdapter, this.redeliveryPolicy);
/* 678 */     this.containerManagerMap.put("persistentTopicContainer", this.persistentTopicMCM);
/* 679 */     this.persistentQueueMCM = (MessageContainerManager)new DurableQueueMessageContainerManager(this.persistenceAdapter, this.redeliveryPolicy);
/* 680 */     this.containerManagerMap.put("persistentQueueContainer", this.persistentQueueMCM);
/* 681 */     this.transientQueueMCM = (MessageContainerManager)new TransientQueueBoundedMessageManager(this.memoryManager);
/* 682 */     this.containerManagerMap.put("transientQueueContainer", this.transientQueueMCM);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validateConsumer(ConsumerInfo info) throws JMSException {
/* 692 */     if (info.getConsumerId() == null) {
/* 693 */       throw new JMSException("No consumerId specified for the ConsumerInfo");
/*     */     }
/*     */   }
/*     */   
/*     */   protected void checkValid() throws JMSException {
/* 698 */     if (this.containerManagers == null) {
/* 699 */       throw new JMSException("This Broker has not yet been started. Ensure start() is called before invoking action methods");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConsumerInfoListener(ConsumerInfoListener l) {
/* 709 */     this.consumerInfoListeners.add(l);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeConsumerInfoListener(ConsumerInfoListener l) {
/* 718 */     this.consumerInfoListeners.remove(l);
/*     */   }
/*     */   
/*     */   protected void fireConsumerInfo(BrokerClient client, ConsumerInfo info) {
/* 722 */     for (Iterator i = this.consumerInfoListeners.iterator(); i.hasNext(); ) {
/* 723 */       ConsumerInfoListener l = i.next();
/* 724 */       l.onConsumerInfo(client, info);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageContainerManager getPersistentTopicContainerManager() {
/* 732 */     return this.persistentTopicMCM;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageContainerManager getTransientTopicContainerManager() {
/* 739 */     return this.transientTopicMCM;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageContainerManager getPersistentQueueContainerManager() {
/* 746 */     return this.persistentQueueMCM;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageContainerManager getTransientQueueContainerManager() {
/* 753 */     return this.transientQueueMCM;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\broker\impl\DefaultBroker.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */