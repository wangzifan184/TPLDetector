/*     */ package org.codehaus.activemq.transport.gnet;
/*     */ 
/*     */ import java.net.URI;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.geronimo.network.SelectorManager;
/*     */ import org.apache.geronimo.pool.ClockPool;
/*     */ import org.apache.geronimo.pool.ThreadPool;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.transport.TransportChannel;
/*     */ import org.codehaus.activemq.transport.TransportChannelFactorySupport;
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
/*     */ public class GTransportChannelFactory
/*     */   extends TransportChannelFactorySupport
/*     */ {
/*     */   private static ThreadPool threadPool;
/*     */   private static ClockPool clockPool;
/*     */   private static SelectorManager selectorManager;
/*     */   
/*     */   public static void init(SelectorManager sm, ThreadPool tp, ClockPool cp) throws IllegalArgumentException {
/*  46 */     if (sm == null || tp == null || cp == null) {
/*  47 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/*  50 */     selectorManager = sm;
/*  51 */     threadPool = tp;
/*  52 */     clockPool = cp;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void init() throws Exception {
/*  58 */     if (threadPool != null) {
/*     */       return;
/*     */     }
/*     */     
/*  62 */     threadPool = new ThreadPool();
/*  63 */     threadPool.setKeepAliveTime(1000L);
/*  64 */     threadPool.setPoolSize(5);
/*  65 */     threadPool.setPoolName("C Pool");
/*     */     
/*  67 */     clockPool = new ClockPool();
/*  68 */     clockPool.setPoolName("C Clock");
/*     */     
/*  70 */     selectorManager = new SelectorManager();
/*  71 */     selectorManager.setThreadPool(threadPool);
/*  72 */     selectorManager.setThreadName("C Manager");
/*  73 */     selectorManager.setTimeout(1000L);
/*     */     
/*  75 */     threadPool.doStart();
/*  76 */     clockPool.doStart();
/*  77 */     selectorManager.doStart();
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
/*     */   public TransportChannel create(WireFormat wireFormat, URI remoteLocation) throws JMSException {
/*  90 */     return create(null, remoteLocation);
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
/*     */   public TransportChannel create(WireFormat wireFormat, URI remoteLocation, URI localLocation) throws JMSException {
/*     */     try {
/* 105 */       init();
/* 106 */       return populateProperties((TransportChannel)new GTransportChannel(wireFormat, remoteLocation, localLocation, selectorManager, threadPool, clockPool), remoteLocation);
/*     */     }
/* 108 */     catch (Exception e) {
/* 109 */       JMSException ex = new JMSException(e.getMessage());
/* 110 */       ex.setLinkedException(e);
/* 111 */       throw ex;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean requiresEmbeddedBroker() {
/* 116 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\gnet\GTransportChannelFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */