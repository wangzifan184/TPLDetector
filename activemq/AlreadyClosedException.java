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
/*    */ public class AlreadyClosedException
/*    */   extends JMSException
/*    */ {
/*    */   public AlreadyClosedException(String description) {
/* 30 */     super("Cannot use " + description + " as it has already been closed", "AMQ-1001");
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\AlreadyClosedException.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */