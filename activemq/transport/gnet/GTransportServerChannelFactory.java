/*    */ package org.codehaus.activemq.transport.gnet;
/*    */ 
/*    */ import java.net.URI;
/*    */ import javax.jms.JMSException;
/*    */ import org.apache.geronimo.network.SelectorManager;
/*    */ import org.apache.geronimo.pool.ClockPool;
/*    */ import org.apache.geronimo.pool.ThreadPool;
/*    */ import org.codehaus.activemq.message.WireFormat;
/*    */ import org.codehaus.activemq.transport.TransportServerChannel;
/*    */ import org.codehaus.activemq.transport.TransportServerChannelFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GTransportServerChannelFactory
/*    */   implements TransportServerChannelFactory
/*    */ {
/*    */   private static ThreadPool threadPool;
/*    */   private static ClockPool clockPool;
/*    */   private static SelectorManager selectorManager;
/*    */   
/*    */   public static void init(SelectorManager sm, ThreadPool tp, ClockPool cp) throws IllegalArgumentException {
/* 46 */     if (sm == null || tp == null || cp == null) {
/* 47 */       throw new IllegalArgumentException();
/*    */     }
/*    */     
/* 50 */     selectorManager = sm;
/* 51 */     threadPool = tp;
/* 52 */     clockPool = cp;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static void init() throws Exception {
/* 58 */     if (threadPool != null) {
/*    */       return;
/*    */     }
/*    */     
/* 62 */     threadPool = new ThreadPool();
/* 63 */     threadPool.setKeepAliveTime(1000L);
/* 64 */     threadPool.setPoolSize(5);
/* 65 */     threadPool.setPoolName("S Pool");
/*    */     
/* 67 */     clockPool = new ClockPool();
/* 68 */     clockPool.setPoolName("S Clock");
/*    */     
/* 70 */     selectorManager = new SelectorManager();
/* 71 */     selectorManager.setThreadPool(threadPool);
/* 72 */     selectorManager.setThreadName("S Manager");
/* 73 */     selectorManager.setTimeout(1000L);
/*    */     
/* 75 */     threadPool.doStart();
/* 76 */     clockPool.doStart();
/* 77 */     selectorManager.doStart();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TransportServerChannel create(WireFormat wireFormat, URI bindAddress) throws JMSException {
/*    */     try {
/* 91 */       init();
/* 92 */       return new GTransportServerChannel(wireFormat, bindAddress, selectorManager, threadPool, clockPool);
/*    */     }
/* 94 */     catch (Exception e) {
/* 95 */       e.printStackTrace();
/* 96 */       JMSException ex = new JMSException(e.getMessage());
/* 97 */       ex.setLinkedException(e);
/* 98 */       throw ex;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\gnet\GTransportServerChannelFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */