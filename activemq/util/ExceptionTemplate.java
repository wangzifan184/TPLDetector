/*    */ package org.codehaus.activemq.util;
/*    */ 
/*    */ import javax.jms.JMSException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExceptionTemplate
/*    */ {
/*    */   private Throwable firstException;
/*    */   
/*    */   public void run(Callback task) {
/*    */     try {
/* 41 */       task.execute();
/*    */     }
/* 43 */     catch (Throwable t) {
/* 44 */       if (this.firstException == null) {
/* 45 */         this.firstException = t;
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Throwable getFirstException() {
/* 57 */     return this.firstException;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void throwJMSException() throws JMSException {
/* 67 */     if (this.firstException != null) {
/* 68 */       if (this.firstException instanceof JMSException) {
/* 69 */         throw (JMSException)this.firstException;
/*    */       }
/* 71 */       if (this.firstException instanceof Exception) {
/* 72 */         throw JMSExceptionHelper.newJMSException(this.firstException.getMessage(), (Exception)this.firstException);
/*    */       }
/*    */ 
/*    */       
/* 76 */       throw JMSExceptionHelper.newJMSException(this.firstException.getMessage(), new Exception(this.firstException));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activem\\util\ExceptionTemplate.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */