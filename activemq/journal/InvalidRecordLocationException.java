/*    */ package org.codehaus.activemq.journal;
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
/*    */ public class InvalidRecordLocationException
/*    */   extends Exception
/*    */ {
/*    */   public InvalidRecordLocationException() {}
/*    */   
/*    */   public InvalidRecordLocationException(String msg) {
/* 38 */     super(msg);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InvalidRecordLocationException(String msg, Throwable rootCause) {
/* 46 */     super(msg, rootCause);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InvalidRecordLocationException(Throwable rootCause) {
/* 53 */     super(rootCause);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\journal\InvalidRecordLocationException.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */