/*     */ package org.codehaus.activemq.service.impl;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.broker.BrokerClient;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.service.MessageContainerManager;
/*     */ import org.codehaus.activemq.service.Service;
/*     */ import org.codehaus.activemq.service.Subscription;
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
/*     */ public class DispatchWorker
/*     */   implements Runnable, Service
/*     */ {
/*  39 */   private static final Log log = LogFactory.getLog(DispatchWorker.class);
/*     */   
/*     */   private static final int POLL_TIMEOUT = 250;
/*  42 */   private Map subscriptions = (Map)new ConcurrentHashMap(1000, 0.75F);
/*  43 */   private Object lock = new Object();
/*     */ 
/*     */   
/*     */   private boolean active = true;
/*     */   
/*     */   private boolean started = false;
/*     */   
/*     */   private MessageContainerManager containerManager;
/*     */ 
/*     */   
/*     */   public void register(MessageContainerManager mcm) {
/*  54 */     this.containerManager = mcm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void wakeup() {
/*  62 */     synchronized (this.lock) {
/*  63 */       this.active = true;
/*  64 */       this.lock.notifyAll();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addActiveSubscription(BrokerClient client, Subscription sub) {
/*  75 */     if (log.isDebugEnabled()) {
/*  76 */       log.info("Adding subscription: " + sub + " to client: " + client);
/*     */     }
/*  78 */     this.subscriptions.put(sub, client);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeActiveSubscription(BrokerClient client, Subscription sub) {
/*  88 */     if (log.isDebugEnabled()) {
/*  89 */       log.info("Removing subscription: " + sub + " from client: " + client);
/*     */     }
/*  91 */     this.subscriptions.remove(sub);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 100 */     while (this.started) {
/* 101 */       doPoll();
/* 102 */       boolean dispatched = false;
/*     */       
/*     */       try {
/* 105 */         for (Iterator iter = this.subscriptions.keySet().iterator(); iter.hasNext(); ) {
/* 106 */           Subscription sub = iter.next();
/* 107 */           if (sub != null && sub.isReadyToDispatch()) {
/* 108 */             dispatched = dispatchMessages(sub, dispatched);
/*     */           }
/*     */         }
/*     */       
/* 112 */       } catch (JMSException jmsEx) {
/* 113 */         log.error("Could not dispatch to Subscription: " + jmsEx, (Throwable)jmsEx);
/*     */       } 
/* 115 */       if (!dispatched) {
/* 116 */         synchronized (this.lock) {
/* 117 */           this.active = false;
/* 118 */           if (!this.active && this.started) {
/*     */             try {
/* 120 */               this.lock.wait(250L);
/*     */             }
/* 122 */             catch (InterruptedException e) {}
/*     */           }
/*     */         } 
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
/*     */   public void start() {
/* 137 */     this.started = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 146 */     this.started = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean dispatchMessages(Subscription subscription, boolean dispatched) throws JMSException {
/* 154 */     ActiveMQMessage[] msgs = subscription.getMessagesToDispatch();
/* 155 */     if (msgs != null && msgs.length > 0) {
/* 156 */       BrokerClient client = (BrokerClient)this.subscriptions.get(subscription);
/* 157 */       if (client == null) {
/* 158 */         log.warn("Null client for subscription: " + subscription);
/*     */       } else {
/*     */         
/* 161 */         for (int i = 0; i < msgs.length; i++) {
/* 162 */           ActiveMQMessage msg = msgs[i].shallowCopy();
/*     */           
/* 164 */           if (log.isDebugEnabled()) {
/* 165 */             log.debug("Dispatching message: " + msg);
/*     */           }
/* 167 */           int[] consumerNos = new int[1];
/* 168 */           consumerNos[0] = subscription.getConsumerNumber();
/* 169 */           msg.setConsumerNos(consumerNos);
/* 170 */           client.dispatch(msg);
/* 171 */           dispatched = true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 175 */     return dispatched;
/*     */   }
/*     */   
/*     */   protected void doPoll() {
/* 179 */     if (this.containerManager != null && this.started)
/*     */       try {
/* 181 */         this.containerManager.poll();
/*     */       }
/* 183 */       catch (JMSException e) {
/* 184 */         log.error("Error polling from the ContainerManager: ", (Throwable)e);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\DispatchWorker.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */