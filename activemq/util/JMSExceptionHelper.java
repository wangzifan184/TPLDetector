/*    */ package org.codehaus.activemq.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.jms.JMSException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
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
/*    */ public class JMSExceptionHelper
/*    */ {
/* 33 */   private static final Log log = LogFactory.getLog(JMSExceptionHelper.class);
/*    */   
/*    */   public static JMSException newJMSException(String message, Throwable cause) {
/* 36 */     if (cause instanceof Exception) {
/* 37 */       return newJMSException(message, (Exception)cause);
/*    */     }
/*    */ 
/*    */     
/* 41 */     return newJMSException(message, new Exception(cause));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static JMSException newJMSException(String message, Exception cause) {
/* 47 */     log.trace(message, cause);
/*    */     
/* 49 */     JMSException jmsEx = new JMSException(message);
/* 50 */     jmsEx.setLinkedException(cause);
/* 51 */     jmsEx.initCause(cause);
/* 52 */     return jmsEx;
/*    */   }
/*    */   
/*    */   public static JMSException newJMSException(Throwable e) {
/* 56 */     if (e instanceof JMSException) {
/* 57 */       return (JMSException)e;
/*    */     }
/*    */     
/* 60 */     return newJMSException(e.getMessage(), e);
/*    */   }
/*    */ 
/*    */   
/*    */   public static IOException newIOException(JMSException e) {
/* 65 */     IOException answer = new IOException(e.getMessage());
/* 66 */     answer.setStackTrace(e.getStackTrace());
/* 67 */     return answer;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activem\\util\JMSExceptionHelper.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */