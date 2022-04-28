/*     */ package org.codehaus.activemq.transport.ember;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.jms.JMSException;
/*     */ import pyrasun.eio.EIOEvent;
/*     */ import pyrasun.eio.EIOEventDescriptor;
/*     */ import pyrasun.eio.EIOGlobalContext;
/*     */ import pyrasun.eio.EIOPoolingStrategy;
/*     */ import pyrasun.eio.services.EmberServiceController;
/*     */ import pyrasun.eio.services.EmberServiceControllerImpl;
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
/*     */ public class EmberSupport
/*     */ {
/*     */   private EIOGlobalContext context;
/*  38 */   private String key = "emberIo";
/*  39 */   private int maxEm = 1;
/*     */ 
/*     */   
/*     */   private EmberServiceControllerImpl controller;
/*     */ 
/*     */   
/*     */   private EIOPoolingStrategy ioPoolingStrategy;
/*     */ 
/*     */   
/*  48 */   private String poolingStrategyName = "SELECTOR_READER";
/*     */ 
/*     */   
/*     */   public EmberSupport() {}
/*     */   
/*     */   public EmberSupport(EIOGlobalContext context, EIOPoolingStrategy ioPoolingStrategy) {
/*  54 */     this.context = context;
/*  55 */     this.ioPoolingStrategy = ioPoolingStrategy;
/*     */   }
/*     */   
/*     */   public EIOGlobalContext getContext() throws IOException {
/*  59 */     if (this.context == null) {
/*  60 */       this.context = new EIOGlobalContext(this.maxEm, this.key);
/*     */     }
/*  62 */     return this.context;
/*     */   }
/*     */   
/*     */   public String getKey() {
/*  66 */     return this.key;
/*     */   }
/*     */   
/*     */   protected EmberServiceController getController() throws IOException {
/*  70 */     if (this.controller == null) {
/*  71 */       this.controller = new EmberServiceControllerImpl(getContext());
/*     */     }
/*  73 */     return (EmberServiceController)this.controller;
/*     */   }
/*     */   
/*     */   protected EIOPoolingStrategy getIoPoolingStrategy() {
/*  77 */     if (this.ioPoolingStrategy == null) {
/*  78 */       this.ioPoolingStrategy = getPoolingStrategyByName(this.poolingStrategyName);
/*     */     }
/*  80 */     return this.ioPoolingStrategy;
/*     */   }
/*     */   
/*     */   protected EIOPoolingStrategy getPoolingStrategyByName(String name) {
/*  84 */     EIOPoolingStrategy strategy = EIOPoolingStrategy.getStrategyByName(name);
/*     */ 
/*     */     
/*  87 */     EIOEventDescriptor evRead = strategy.getEventDescriptor(EIOEvent.READ);
/*  88 */     evRead.setPoolSize(1);
/*     */     
/*  90 */     EIOEventDescriptor evWrite = strategy.getEventDescriptor(EIOEvent.WRITE);
/*  91 */     evWrite.setPoolSize(1);
/*     */     
/*  93 */     EIOEventDescriptor evProcess = strategy.getEventDescriptor(EIOEvent.PROCESS);
/*  94 */     evProcess.setPoolSize(1);
/*  95 */     return strategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JMSException createJMSException(String message, Exception ex) {
/* 102 */     JMSException jmsEx = new JMSException(message + ex.getMessage());
/* 103 */     jmsEx.setLinkedException(ex);
/* 104 */     return jmsEx;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\ember\EmberSupport.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */