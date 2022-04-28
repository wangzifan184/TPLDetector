/*    */ package org.codehaus.activemq.util;
/*    */ 
/*    */ import EDU.oswego.cs.dl.util.concurrent.Executor;
/*    */ import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
/*    */ import javax.jms.JMSException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.codehaus.activemq.service.Service;
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
/*    */ public class ExecutorHelper
/*    */ {
/* 34 */   private static final Log log = LogFactory.getLog(ExecutorHelper.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void stopExecutor(Executor executor) throws InterruptedException, JMSException {
/* 42 */     if (executor instanceof Service) {
/* 43 */       Service service = (Service)executor;
/* 44 */       service.stop();
/*    */     }
/* 46 */     else if (executor instanceof PooledExecutor) {
/* 47 */       PooledExecutor pe = (PooledExecutor)executor;
/* 48 */       pe.shutdownAfterProcessingCurrentlyQueuedTasks();
/*    */       
/* 50 */       pe.awaitTerminationAfterShutdown();
/*    */     }
/* 52 */     else if (executor != null) {
/* 53 */       log.warn("Don't know how to cleanly close down the given executor: " + executor + ". Consider deriving from this class to implement the Service interface to shut down cleanly");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activem\\util\ExecutorHelper.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */