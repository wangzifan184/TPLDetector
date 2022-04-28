/*     */ package org.codehaus.activemq.service.impl;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;
/*     */ import org.codehaus.activemq.broker.BrokerClient;
/*     */ import org.codehaus.activemq.service.Dispatcher;
/*     */ import org.codehaus.activemq.service.MessageContainerManager;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DispatcherImpl
/*     */   implements Dispatcher
/*     */ {
/*  37 */   private SynchronizedBoolean started = new SynchronizedBoolean(false);
/*  38 */   private DispatchWorker worker = new DispatchWorker();
/*     */ 
/*     */   
/*     */   private MessageContainerManager containerManager;
/*     */ 
/*     */   
/*     */   private Thread runner;
/*     */ 
/*     */ 
/*     */   
/*     */   public void register(MessageContainerManager mcm) {
/*  49 */     this.containerManager = mcm;
/*  50 */     this.worker.register(mcm);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void wakeup(Subscription sub) {
/*  60 */     this.worker.wakeup();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void wakeup() {
/*  68 */     this.worker.wakeup();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addActiveSubscription(BrokerClient client, Subscription sub) {
/*  78 */     this.worker.addActiveSubscription(client, sub);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeActiveSubscription(BrokerClient client, Subscription sub) {
/*  88 */     this.worker.removeActiveSubscription(client, sub);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/*  97 */     if (this.started.commit(false, true)) {
/*  98 */       this.worker.start();
/*  99 */       this.runner = new Thread(this.worker, "Dispatch Worker");
/* 100 */       this.runner.setDaemon(true);
/* 101 */       this.runner.setPriority(6);
/* 102 */       this.runner.start();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 112 */     this.worker.stop();
/* 113 */     this.started.set(false);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\DispatcherImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */