/*    */ package org.codehaus.activemq.util;
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
/*    */ public class LongSequenceGenerator
/*    */ {
/* 29 */   long lastSequenceId = 0L;
/*    */   
/*    */   public synchronized void setLastSequenceId(long value) {
/* 32 */     this.lastSequenceId = value;
/*    */   }
/*    */   
/*    */   public synchronized long getLastSequenceId() {
/* 36 */     return this.lastSequenceId;
/*    */   }
/*    */   
/*    */   public synchronized long getNextSequenceId() {
/* 40 */     return ++this.lastSequenceId;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activem\\util\LongSequenceGenerator.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */