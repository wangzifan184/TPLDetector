/*     */ package org.codehaus.activemq.ra;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedList;
/*     */ import javax.jms.ConnectionConsumer;
/*     */ import javax.jms.Destination;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Message;
/*     */ import javax.jms.MessageListener;
/*     */ import javax.jms.ServerSession;
/*     */ import javax.jms.ServerSessionPool;
/*     */ import javax.jms.Session;
/*     */ import javax.jms.Topic;
/*     */ import javax.jms.XASession;
/*     */ import javax.resource.ResourceException;
/*     */ import javax.resource.spi.endpoint.MessageEndpoint;
/*     */ import javax.resource.spi.work.Work;
/*     */ import javax.resource.spi.work.WorkException;
/*     */ import javax.resource.spi.work.WorkListener;
/*     */ import javax.transaction.xa.XAResource;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.ActiveMQQueue;
/*     */ import org.codehaus.activemq.message.ActiveMQTopic;
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
/*     */ public class ActiveMQAsfEndpointWorker
/*     */   extends ActiveMQBaseEndpointWorker
/*     */ {
/*  38 */   private static final Log log = LogFactory.getLog(ActiveMQAsfEndpointWorker.class);
/*     */ 
/*     */   
/*     */   private static final int MAX_MSGS_PER_SESSION = 1;
/*     */ 
/*     */   
/*     */   private static final int MAX_SESSION = 10;
/*     */   
/*     */   ConnectionConsumer consumer;
/*     */   
/*     */   private ServerSessionPoolImpl serverSessionPool;
/*     */ 
/*     */   
/*     */   public ActiveMQAsfEndpointWorker(ActiveMQResourceAdapter adapter, ActiveMQEndpointActivationKey key) throws ResourceException {
/*  52 */     super(adapter, key);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() throws WorkException, ResourceException {
/*  57 */     log.debug("Starting");
/*  58 */     boolean ok = false; try {
/*     */       ActiveMQTopic activeMQTopic;
/*  60 */       this.serverSessionPool = new ServerSessionPoolImpl();
/*  61 */       ActiveMQActivationSpec activationSpec = this.endpointActivationKey.getActivationSpec();
/*     */       
/*  63 */       Destination dest = null;
/*     */       
/*  65 */       if ("javax.jms.Queue".equals(activationSpec.getDestinationType())) {
/*  66 */         ActiveMQQueue activeMQQueue = new ActiveMQQueue(activationSpec.getDestinationName());
/*  67 */       } else if ("javax.jms.Topic".equals(activationSpec.getDestinationType())) {
/*  68 */         activeMQTopic = new ActiveMQTopic(activationSpec.getDestinationName());
/*     */       } else {
/*  70 */         throw new ResourceException("Unknown destination type: " + activationSpec.getDestinationType());
/*     */       } 
/*     */       
/*  73 */       if (emptyToNull(activationSpec.getDurableSubscriptionName()) != null) {
/*  74 */         this.consumer = this.adapter.getPhysicalConnection().createDurableConnectionConsumer((Topic)activeMQTopic, activationSpec.getDurableSubscriptionName(), emptyToNull(activationSpec.getMessageSelector()), this.serverSessionPool, 1);
/*     */       } else {
/*  76 */         this.consumer = this.adapter.getPhysicalConnection().createConnectionConsumer((Destination)activeMQTopic, emptyToNull(activationSpec.getMessageSelector()), this.serverSessionPool, 1);
/*     */       } 
/*     */       
/*  79 */       ok = true;
/*  80 */       log.debug("Started");
/*     */     }
/*  82 */     catch (JMSException e) {
/*  83 */       throw new ResourceException("Could not start the endpoint.", e);
/*     */     }
/*     */     finally {
/*     */       
/*  87 */       if (!ok) {
/*  88 */         safeClose(this.consumer);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() throws InterruptedException {
/*  97 */     safeClose(this.consumer);
/*  98 */     this.serverSessionPool.close();
/*     */   }
/*     */   
/*     */   class ServerSessionPoolImpl
/*     */     implements ServerSessionPool {
/*     */     ActiveMQAsfEndpointWorker.ServerSessionImpl ss;
/* 104 */     ArrayList idleSessions = new ArrayList();
/* 105 */     LinkedList activeSessions = new LinkedList();
/* 106 */     int sessionIds = 0;
/*     */     
/*     */     int nextUsedSession;
/*     */     
/*     */     boolean closing = false;
/*     */     private final ActiveMQAsfEndpointWorker this$0;
/*     */     
/*     */     public ActiveMQAsfEndpointWorker.ServerSessionImpl createServerSessionImpl() throws JMSException {
/* 114 */       Session session = ActiveMQAsfEndpointWorker.this.adapter.getPhysicalConnection().createSession(true, 0);
/* 115 */       return new ActiveMQAsfEndpointWorker.ServerSessionImpl(this, session);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public synchronized ServerSession getServerSession() throws JMSException {
/* 121 */       ActiveMQAsfEndpointWorker.log.debug("ServerSession requested.");
/* 122 */       if (this.closing) {
/* 123 */         throw new JMSException("Session Pool Shutting Down.");
/*     */       }
/* 125 */       if (this.idleSessions.size() > 0) {
/* 126 */         ActiveMQAsfEndpointWorker.ServerSessionImpl serverSessionImpl = this.idleSessions.remove(this.idleSessions.size() - 1);
/* 127 */         this.activeSessions.addLast(serverSessionImpl);
/* 128 */         ActiveMQAsfEndpointWorker.log.debug("Using idle session: " + serverSessionImpl);
/* 129 */         return serverSessionImpl;
/*     */       } 
/*     */       
/* 132 */       if (this.activeSessions.size() >= 10) {
/*     */ 
/*     */         
/* 135 */         ActiveMQAsfEndpointWorker.ServerSessionImpl serverSessionImpl = this.activeSessions.removeFirst();
/* 136 */         this.activeSessions.addLast(serverSessionImpl);
/* 137 */         ActiveMQAsfEndpointWorker.log.debug("Reusing an active session: " + serverSessionImpl);
/* 138 */         return serverSessionImpl;
/*     */       } 
/* 140 */       ActiveMQAsfEndpointWorker.ServerSessionImpl ss = createServerSessionImpl();
/* 141 */       this.activeSessions.addLast(ss);
/* 142 */       ActiveMQAsfEndpointWorker.log.debug("Created a new session: " + ss);
/* 143 */       return ss;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public synchronized void returnToPool(ActiveMQAsfEndpointWorker.ServerSessionImpl ss) {
/* 149 */       ActiveMQAsfEndpointWorker.log.debug("Session returned to pool: " + ss);
/* 150 */       this.idleSessions.add(ss);
/*     */     }
/*     */     
/*     */     public void close() {
/* 154 */       synchronized (this) {
/* 155 */         this.closing = true;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   class ServerSessionImpl
/*     */     implements ServerSession, Work, MessageListener
/*     */   {
/*     */     Session session;
/*     */     private final ActiveMQAsfEndpointWorker.ServerSessionPoolImpl pool;
/* 165 */     private Object runControlMutex = new Object();
/*     */     boolean workPendingFlag = false;
/*     */     boolean runningFlag = false;
/* 168 */     int runCounter = 0;
/*     */     XAResource xaResource;
/*     */     private final ActiveMQAsfEndpointWorker this$0;
/*     */     
/*     */     public ServerSessionImpl(ActiveMQAsfEndpointWorker.ServerSessionPoolImpl pool, Session session) throws JMSException {
/* 173 */       this.pool = pool;
/* 174 */       this.session = session;
/* 175 */       this.session.setMessageListener(this);
/* 176 */       if (session instanceof XASession) {
/* 177 */         this.xaResource = ((XASession)session).getXAResource();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Session getSession() throws JMSException {
/* 185 */       return this.session;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void start() throws JMSException {
/* 194 */       ActiveMQAsfEndpointWorker.log.debug("ServerSession started.");
/* 195 */       synchronized (this.runControlMutex) {
/* 196 */         this.runCounter++;
/*     */         
/* 198 */         if (this.runningFlag || this.workPendingFlag) {
/*     */           
/* 200 */           this.workPendingFlag = true;
/* 201 */           ActiveMQAsfEndpointWorker.log.debug("ServerSession allready running.");
/*     */           return;
/*     */         } 
/* 204 */         this.workPendingFlag = true;
/*     */       } 
/*     */ 
/*     */       
/* 208 */       ActiveMQAsfEndpointWorker.log.debug("ServerSession queuing request for a run.");
/*     */       try {
/* 210 */         ActiveMQAsfEndpointWorker.this.workManager.scheduleWork(this, Long.MAX_VALUE, null, (WorkListener)new Object(this));
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
/*     */       }
/* 230 */       catch (WorkException e) {
/* 231 */         throw (JMSException)(new JMSException("Work could not be started: " + e)).initCause(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       while (true) {
/* 240 */         synchronized (this.runControlMutex) {
/* 241 */           this.workPendingFlag = false;
/* 242 */           this.runningFlag = true;
/*     */         } 
/*     */         
/* 245 */         ActiveMQAsfEndpointWorker.log.debug("Running: " + this);
/* 246 */         this.session.run();
/*     */         
/* 248 */         synchronized (this.runControlMutex) {
/* 249 */           this.runCounter--;
/* 250 */           this.runningFlag = false;
/* 251 */           if (!this.workPendingFlag) {
/* 252 */             if (this.runCounter == 0) {
/* 253 */               this.pool.returnToPool(this);
/*     */             }
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void release() {
/* 265 */       ActiveMQAsfEndpointWorker.log.debug("release called");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onMessage(Message message) {
/*     */       try {
/* 274 */         MessageEndpoint endpoint = ActiveMQAsfEndpointWorker.this.endpointFactory.createEndpoint(this.xaResource);
/* 275 */         MessageListener listener = (MessageListener)endpoint;
/*     */         
/* 277 */         endpoint.beforeDelivery(ActiveMQBaseEndpointWorker.ON_MESSAGE_METHOD);
/*     */         try {
/* 279 */           listener.onMessage(message);
/*     */         } finally {
/* 281 */           endpoint.afterDelivery();
/*     */         }
/*     */       
/* 284 */       } catch (NoSuchMethodException e) {
/* 285 */         ActiveMQAsfEndpointWorker.log.info(e);
/* 286 */       } catch (ResourceException e) {
/* 287 */         ActiveMQAsfEndpointWorker.log.info(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 295 */       return "ServerSessionImpl[session=" + this.session + "]";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private String emptyToNull(String value) {
/* 301 */     if ("".equals(value)) {
/* 302 */       return null;
/*     */     }
/* 304 */     return value;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ra\ActiveMQAsfEndpointWorker.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */