/*     */ package org.codehaus.activemq.ra;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.Latch;
/*     */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;
/*     */ import javax.jms.ConnectionConsumer;
/*     */ import javax.jms.Destination;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.MessageListener;
/*     */ import javax.jms.Topic;
/*     */ import javax.jms.XASession;
/*     */ import javax.resource.ResourceException;
/*     */ import javax.resource.spi.endpoint.MessageEndpoint;
/*     */ import javax.resource.spi.work.Work;
/*     */ import javax.resource.spi.work.WorkEvent;
/*     */ import javax.resource.spi.work.WorkException;
/*     */ import javax.resource.spi.work.WorkListener;
/*     */ import javax.transaction.xa.XAResource;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.ActiveMQConnectionConsumer;
/*     */ import org.codehaus.activemq.ActiveMQSession;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
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
/*     */ 
/*     */ 
/*     */ public class ActiveMQPollingEndpointWorker
/*     */   extends ActiveMQBaseEndpointWorker
/*     */   implements Work
/*     */ {
/*  41 */   private static final Log log = LogFactory.getLog(ActiveMQPollingEndpointWorker.class);
/*     */   
/*     */   private static final int MAX_WORKERS = 10;
/*  44 */   private SynchronizedBoolean started = new SynchronizedBoolean(false);
/*  45 */   private SynchronizedBoolean stopping = new SynchronizedBoolean(false);
/*  46 */   private Latch stopLatch = new Latch();
/*     */   
/*     */   private ActiveMQConnectionConsumer consumer;
/*     */   
/*     */   private CircularQueue workers;
/*     */   
/*  52 */   static WorkListener debugingWorkListener = new WorkListener()
/*     */     {
/*     */       public void workAccepted(WorkEvent event) {}
/*     */       
/*     */       public void workRejected(WorkEvent event) {
/*  57 */         ActiveMQPollingEndpointWorker.log.warn("Work rejected: " + event, (Throwable)event.getException());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void workStarted(WorkEvent event) {}
/*     */ 
/*     */ 
/*     */       
/*     */       public void workCompleted(WorkEvent event) {}
/*     */     };
/*     */ 
/*     */   
/*     */   public ActiveMQPollingEndpointWorker(ActiveMQResourceAdapter adapter, ActiveMQEndpointActivationKey key) throws ResourceException {
/*  71 */     super(adapter, key);
/*     */   }
/*     */   
/*     */   public void start() throws WorkException, ResourceException {
/*  75 */     ActiveMQActivationSpec activationSpec = this.endpointActivationKey.getActivationSpec();
/*  76 */     boolean ok = false; try {
/*     */       ActiveMQTopic activeMQTopic;
/*  78 */       this.workers = new CircularQueue(10, this.stopping);
/*  79 */       for (int i = 0; i < this.workers.size(); i++) {
/*  80 */         ActiveMQSession session = (ActiveMQSession)this.adapter.getPhysicalConnection().createSession(this.transacted, 1);
/*  81 */         XAResource xaresource = null;
/*  82 */         if (session instanceof XASession) {
/*  83 */           if (!this.transacted)
/*  84 */             throw new ResourceException("You cannot use an XA Connection with a non transacted endpoint."); 
/*  85 */           xaresource = ((XASession)session).getXAResource();
/*     */         } 
/*     */         
/*  88 */         MessageEndpoint endpoint = this.endpointFactory.createEndpoint(xaresource);
/*  89 */         this.workers.returnObject(new InboundEndpointWork(session, endpoint, this.workers));
/*     */       } 
/*     */       
/*  92 */       Destination dest = null;
/*  93 */       if ("javax.jms.Queue".equals(activationSpec.getDestinationType())) {
/*  94 */         ActiveMQQueue activeMQQueue = new ActiveMQQueue(activationSpec.getDestinationName());
/*  95 */       } else if ("javax.jms.Topic".equals(activationSpec.getDestinationType())) {
/*  96 */         activeMQTopic = new ActiveMQTopic(activationSpec.getDestinationName());
/*     */       } else {
/*  98 */         throw new ResourceException("Unknown destination type: " + activationSpec.getDestinationType());
/*     */       } 
/*     */       
/* 101 */       if (emptyToNull(activationSpec.getDurableSubscriptionName()) != null) {
/* 102 */         this.consumer = (ActiveMQConnectionConsumer)this.adapter.getPhysicalConnection().createDurableConnectionConsumer((Topic)activeMQTopic, activationSpec.getDurableSubscriptionName(), emptyToNull(activationSpec.getMessageSelector()), null, 0);
/*     */       } else {
/* 104 */         this.consumer = (ActiveMQConnectionConsumer)this.adapter.getPhysicalConnection().createConnectionConsumer((Destination)activeMQTopic, emptyToNull(activationSpec.getMessageSelector()), null, 0);
/*     */       } 
/*     */       
/* 107 */       ok = true;
/* 108 */       log.debug("Started");
/*     */       
/* 110 */       this.workManager.scheduleWork(this, Long.MAX_VALUE, null, debugingWorkListener);
/* 111 */       ok = true;
/*     */     }
/* 113 */     catch (JMSException e) {
/* 114 */       throw new ResourceException("Could not start the endpoint.", e);
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 119 */       if (!ok) {
/* 120 */         safeClose((ConnectionConsumer)this.consumer);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private String emptyToNull(String value) {
/* 127 */     if ("".equals(value)) {
/* 128 */       return null;
/*     */     }
/* 130 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() throws InterruptedException {
/* 137 */     this.stopping.set(true);
/* 138 */     this.workers.notifyWaiting();
/* 139 */     if (this.started.compareTo(true) == 0) {
/* 140 */       this.stopLatch.acquire();
/*     */     }
/* 142 */     safeClose((ConnectionConsumer)this.consumer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void release() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 158 */     this.started.set(true);
/*     */     
/*     */     try {
/* 161 */       while (!this.stopping.get()) {
/* 162 */         ActiveMQMessage message = this.consumer.receive(500L);
/* 163 */         if (message != null) {
/* 164 */           InboundEndpointWork worker = (InboundEndpointWork)this.workers.get();
/*     */           
/* 166 */           if (worker == null) {
/*     */             break;
/*     */           }
/* 169 */           worker.message = message;
/* 170 */           this.workManager.scheduleWork(worker, Long.MAX_VALUE, null, debugingWorkListener);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 175 */       this.workers.drain();
/*     */     }
/* 177 */     catch (Throwable e) {
/* 178 */       log.info("dispatcher: ", e);
/*     */     } finally {
/* 180 */       this.stopLatch.release();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class InboundEndpointWork
/*     */     implements Work
/*     */   {
/*     */     private final ActiveMQSession session;
/*     */     
/*     */     private final MessageEndpoint endpoint;
/*     */     
/*     */     private final CircularQueue workers;
/*     */     
/*     */     ActiveMQMessage message;
/*     */ 
/*     */     
/*     */     public InboundEndpointWork(ActiveMQSession session, MessageEndpoint endpoint, CircularQueue workers) throws JMSException {
/* 199 */       this.session = session;
/* 200 */       this.endpoint = endpoint;
/* 201 */       this.workers = workers;
/* 202 */       session.setMessageListener((MessageListener)endpoint);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void release() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/* 214 */         this.endpoint.beforeDelivery(ActiveMQBaseEndpointWorker.ON_MESSAGE_METHOD);
/*     */         try {
/* 216 */           this.session.dispatch(this.message);
/* 217 */           this.session.run();
/*     */         } finally {
/* 219 */           this.endpoint.afterDelivery();
/*     */         }
/*     */       
/* 222 */       } catch (NoSuchMethodException e) {
/* 223 */         ActiveMQPollingEndpointWorker.log.info("worker: ", e);
/* 224 */       } catch (ResourceException e) {
/* 225 */         ActiveMQPollingEndpointWorker.log.info("worker: ", (Throwable)e);
/*     */       } finally {
/* 227 */         this.workers.returnObject(this);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ra\ActiveMQPollingEndpointWorker.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */