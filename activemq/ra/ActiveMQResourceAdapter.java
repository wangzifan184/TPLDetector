/*     */ package org.codehaus.activemq.ra;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import javax.jms.Connection;
/*     */ import javax.jms.ConnectionFactory;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.XAConnection;
/*     */ import javax.jms.XASession;
/*     */ import javax.resource.NotSupportedException;
/*     */ import javax.resource.ResourceException;
/*     */ import javax.resource.spi.ActivationSpec;
/*     */ import javax.resource.spi.BootstrapContext;
/*     */ import javax.resource.spi.ResourceAdapter;
/*     */ import javax.resource.spi.ResourceAdapterInternalException;
/*     */ import javax.resource.spi.endpoint.MessageEndpointFactory;
/*     */ import javax.transaction.xa.XAResource;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.ActiveMQConnectionFactory;
/*     */ import org.codehaus.activemq.ActiveMQXAConnectionFactory;
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
/*     */ public class ActiveMQResourceAdapter
/*     */   implements ResourceAdapter
/*     */ {
/*  48 */   private static final Log log = LogFactory.getLog(ActiveMQResourceAdapter.class);
/*     */   
/*     */   private static final String ASF_ENDPOINT_WORKER_TYPE = "asf";
/*     */   
/*     */   private static final String POLLING_ENDPOINT_WORKER_TYPE = "polling";
/*     */   private BootstrapContext bootstrapContext;
/*  54 */   private HashMap endpointWorkers = new HashMap();
/*  55 */   private final ActiveMQConnectionRequestInfo info = new ActiveMQConnectionRequestInfo();
/*  56 */   private String endpointWorkerType = "polling";
/*     */   private Connection physicalConnection;
/*     */   private ConnectionFactory connectionFactory;
/*     */   
/*     */   public ActiveMQResourceAdapter() {
/*  61 */     this(null);
/*     */   }
/*     */   
/*     */   public ActiveMQResourceAdapter(ConnectionFactory connectionFactory) {
/*  65 */     this.connectionFactory = connectionFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start(BootstrapContext bootstrapContext) throws ResourceAdapterInternalException {
/*  73 */     this.bootstrapContext = bootstrapContext;
/*     */     
/*     */     try {
/*  76 */       this.physicalConnection = makeConnection(this.connectionFactory, this.info);
/*  77 */       this.physicalConnection.start();
/*     */     }
/*  79 */     catch (JMSException e) {
/*  80 */       throw new ResourceAdapterInternalException("Could not establish a connection to the server.", e);
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
/*     */ 
/*     */   
/*     */   public static Connection makeConnection(ConnectionFactory connectionFactory, ActiveMQConnectionRequestInfo info) throws JMSException {
/*     */     ActiveMQConnectionFactory activeMQConnectionFactory;
/*  96 */     if (connectionFactory == null) {
/*  97 */       if (info.isXa()) {
/*  98 */         ActiveMQXAConnectionFactory activeMQXAConnectionFactory = new ActiveMQXAConnectionFactory(info.getServerUrl());
/*     */       } else {
/*     */         
/* 101 */         activeMQConnectionFactory = new ActiveMQConnectionFactory(info.getServerUrl());
/*     */       } 
/*     */     }
/* 104 */     Connection physicalConnection = activeMQConnectionFactory.createConnection(info.getUserName(), info.getPassword());
/* 105 */     if (info.getClientid() != null) {
/* 106 */       physicalConnection.setClientID(info.getClientid());
/*     */     }
/* 108 */     return physicalConnection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getPhysicalConnection() {
/* 115 */     return this.physicalConnection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 122 */     this.bootstrapContext = null;
/* 123 */     if (this.physicalConnection != null) {
/*     */       try {
/* 125 */         this.physicalConnection.close();
/* 126 */         this.physicalConnection = null;
/*     */       }
/* 128 */       catch (JMSException e) {
/* 129 */         log.debug("Error occured during ResourceAdapter stop: ", (Throwable)e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BootstrapContext getBootstrapContext() {
/* 138 */     return this.bootstrapContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endpointActivation(MessageEndpointFactory endpointFactory, ActivationSpec activationSpec) throws ResourceException {
/* 149 */     if (activationSpec.getResourceAdapter() != this) {
/* 150 */       throw new ResourceException("Activation spec not initialized with this ResourceAdapter instance");
/*     */     }
/*     */     
/* 153 */     if (activationSpec.getClass().equals(ActiveMQActivationSpec.class)) {
/*     */       ActiveMQBaseEndpointWorker worker;
/* 155 */       ActiveMQEndpointActivationKey key = new ActiveMQEndpointActivationKey(endpointFactory, (ActiveMQActivationSpec)activationSpec);
/*     */       
/* 157 */       if (this.endpointWorkers.containsKey(key)) {
/* 158 */         throw new IllegalStateException("Endpoint previously activated");
/*     */       }
/*     */ 
/*     */       
/* 162 */       if ("polling".equals(getEndpointWorkerType())) {
/* 163 */         worker = new ActiveMQPollingEndpointWorker(this, key);
/* 164 */       } else if ("asf".equals(getEndpointWorkerType())) {
/* 165 */         worker = new ActiveMQAsfEndpointWorker(this, key);
/*     */       } else {
/* 167 */         throw new NotSupportedException("That type of EndpointWorkerType is not supported: " + getEndpointWorkerType());
/*     */       } 
/*     */       
/* 170 */       this.endpointWorkers.put(key, worker);
/* 171 */       worker.start();
/*     */     }
/*     */     else {
/*     */       
/* 175 */       throw new NotSupportedException("That type of ActicationSpec not supported: " + activationSpec.getClass());
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
/*     */   public void endpointDeactivation(MessageEndpointFactory endpointFactory, ActivationSpec activationSpec) {
/* 187 */     if (activationSpec.getClass().equals(ActiveMQActivationSpec.class)) {
/* 188 */       ActiveMQEndpointActivationKey key = new ActiveMQEndpointActivationKey(endpointFactory, (ActiveMQActivationSpec)activationSpec);
/* 189 */       ActiveMQBaseEndpointWorker worker = (ActiveMQBaseEndpointWorker)this.endpointWorkers.get(key);
/* 190 */       if (worker == null) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 196 */         worker.stop();
/*     */       }
/* 198 */       catch (InterruptedException e) {
/*     */ 
/*     */         
/* 201 */         Thread.currentThread().interrupt();
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
/*     */   public XAResource[] getXAResources(ActivationSpec[] activationSpecs) throws ResourceException {
/*     */     try {
/* 215 */       Connection connection = getPhysicalConnection();
/* 216 */       if (connection instanceof XAConnection) {
/* 217 */         XASession session = ((XAConnection)connection).createXASession();
/* 218 */         XAResource xaResource = session.getXAResource();
/* 219 */         return new XAResource[] { xaResource };
/*     */       } 
/* 221 */       return new XAResource[0];
/*     */     }
/* 223 */     catch (JMSException e) {
/* 224 */       throw new ResourceException(e);
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
/*     */   
/*     */   public String getClientid() {
/* 238 */     return this.info.getClientid();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPassword() {
/* 245 */     return this.info.getPassword();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getServerUrl() {
/* 252 */     return this.info.getServerUrl();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUserName() {
/* 259 */     return this.info.getUserName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClientid(String clientid) {
/* 266 */     this.info.setClientid(clientid);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPassword(String password) {
/* 273 */     this.info.setPassword(password);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServerUrl(String url) {
/* 280 */     this.info.setServerUrl(url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUserName(String userid) {
/* 287 */     this.info.setUserName(userid);
/*     */   }
/*     */   
/*     */   public Boolean isXA() {
/* 291 */     return Boolean.valueOf(this.info.isXa());
/*     */   }
/*     */   
/*     */   public void setXA(Boolean xa) {
/* 295 */     this.info.setXa(xa.booleanValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEndpointWorkerType() {
/* 302 */     return this.endpointWorkerType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEndpointWorkerType(String endpointWorkerType) {
/* 308 */     this.endpointWorkerType = endpointWorkerType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQConnectionRequestInfo getInfo() {
/* 315 */     return this.info;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ra\ActiveMQResourceAdapter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */