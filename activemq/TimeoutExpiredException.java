/*    */ package org.codehaus.activemq;
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
/*    */ public class TimeoutExpiredException
/*    */   extends JMSException
/*    */ {
/*    */   public TimeoutExpiredException() {
/* 30 */     this("Could not send message as timeout expired");
/*    */   }
/*    */   
/*    */   public TimeoutExpiredException(String reason) {
/* 34 */     super(reason, "sendTimeout");
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\TimeoutExpiredException.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */