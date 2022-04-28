/*    */ package org.codehaus.activemq;
/*    */ 
/*    */ import javax.jms.IllegalStateException;
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
/*    */ public class NotStartedException
/*    */   extends IllegalStateException
/*    */ {
/*    */   public NotStartedException() {
/* 30 */     super("IllegalState: This service has not yet been started", "AMQ-1003");
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\NotStartedException.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */