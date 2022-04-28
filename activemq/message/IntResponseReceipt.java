/*    */ package org.codehaus.activemq.message;
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
/*    */ public class IntResponseReceipt
/*    */   extends Receipt
/*    */ {
/*    */   private int result;
/*    */   
/*    */   public int getPacketType() {
/* 32 */     return 26;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getResult() {
/* 39 */     return this.result;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setResult(int result) {
/* 46 */     this.result = result;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\IntResponseReceipt.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */