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
/*    */ 
/*    */ 
/*    */ public class TransactionInfo
/*    */   extends AbstractPacket
/*    */   implements TransactionType
/*    */ {
/*    */   private int type;
/*    */   private String transactionId;
/*    */   
/*    */   public String getTransactionId() {
/* 36 */     return this.transactionId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setTransactionId(String transactionId) {
/* 43 */     this.transactionId = transactionId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getPacketType() {
/* 53 */     return 19;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 64 */     boolean result = false;
/* 65 */     if (obj != null && obj instanceof TransactionInfo) {
/* 66 */       TransactionInfo info = (TransactionInfo)obj;
/* 67 */       result = (this.transactionId == info.transactionId);
/*    */     } 
/* 69 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 76 */     return (this.transactionId != null) ? this.transactionId.hashCode() : super.hashCode();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getType() {
/* 83 */     return this.type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setType(int newType) {
/* 90 */     this.type = newType;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\TransactionInfo.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */