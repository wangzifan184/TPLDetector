/*     */ package org.codehaus.activemq;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.util.MemoryBoundedQueue;
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
/*     */ class ActiveMQSessionExecutor
/*     */   implements Runnable
/*     */ {
/*  36 */   private static final Log log = LogFactory.getLog(ActiveMQSessionExecutor.class);
/*     */   private ActiveMQSession session;
/*     */   private MemoryBoundedQueue messageQueue;
/*     */   private boolean closed;
/*     */   private Thread runner;
/*     */   private boolean doDispatch;
/*     */   
/*     */   ActiveMQSessionExecutor(ActiveMQSession session, MemoryBoundedQueue queue) {
/*  44 */     this.session = session;
/*  45 */     this.messageQueue = queue;
/*  46 */     this.doDispatch = true;
/*     */   }
/*     */   
/*     */   void setDoDispatch(boolean value) {
/*  50 */     this.doDispatch = value;
/*     */   }
/*     */   
/*     */   void execute(ActiveMQMessage message) {
/*  54 */     this.messageQueue.enqueue((Packet)message);
/*     */   }
/*     */   
/*     */   void executeFirst(ActiveMQMessage message) {
/*  58 */     this.messageQueue.enqueueFirstNoBlock((Packet)message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*  65 */     while (!this.closed && this.doDispatch) {
/*  66 */       ActiveMQMessage message = null;
/*     */       try {
/*  68 */         message = (ActiveMQMessage)this.messageQueue.dequeue(100L);
/*     */       }
/*  70 */       catch (InterruptedException ie) {}
/*     */       
/*  72 */       if (!this.closed && 
/*  73 */         message != null) {
/*  74 */         if (this.doDispatch) {
/*  75 */           for (Iterator i = this.session.consumers.iterator(); i.hasNext(); ) {
/*  76 */             ActiveMQMessageConsumer consumer = i.next();
/*  77 */             if (message.isConsumerTarget(consumer.getConsumerNumber())) {
/*     */               try {
/*  79 */                 consumer.processMessage(message.shallowCopy());
/*     */               }
/*  81 */               catch (JMSException e) {
/*  82 */                 this.session.connection.handleAsyncException(e);
/*     */               } 
/*     */             }
/*     */           } 
/*     */           continue;
/*     */         } 
/*  88 */         this.messageQueue.enqueueFirstNoBlock((Packet)message);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   synchronized void start() {
/*  96 */     this.messageQueue.start();
/*  97 */     if (this.runner == null && this.doDispatch) {
/*  98 */       this.runner = new Thread(this, "JmsSessionDispather: " + this.session.getSessionId());
/*  99 */       this.runner.setPriority(10);
/*     */       
/* 101 */       this.runner.start();
/*     */     } 
/*     */   }
/*     */   
/*     */   synchronized void stop() {
/* 106 */     this.messageQueue.stop();
/*     */   }
/*     */   
/*     */   synchronized void close() {
/* 110 */     this.closed = true;
/* 111 */     this.messageQueue.close();
/*     */   }
/*     */   
/*     */   void clear() {
/* 115 */     this.messageQueue.clear();
/*     */   }
/*     */   
/*     */   ActiveMQMessage dequeueNoWait() {
/*     */     try {
/* 120 */       return (ActiveMQMessage)this.messageQueue.dequeueNoWait();
/*     */     }
/* 122 */     catch (InterruptedException ie) {
/* 123 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void clearMessagesInProgress() {
/* 128 */     this.messageQueue.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ActiveMQSessionExecutor.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */