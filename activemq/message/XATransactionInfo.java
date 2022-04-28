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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XATransactionInfo
/*    */   extends AbstractPacket
/*    */   implements TransactionType
/*    */ {
/*    */   private ActiveMQXid xid;
/*    */   private int type;
/*    */   private int transactionTimeout;
/*    */   
/*    */   public int getPacketType() {
/* 40 */     return 20;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 50 */     boolean result = false;
/* 51 */     if (obj != null && obj instanceof XATransactionInfo) {
/* 52 */       XATransactionInfo info = (XATransactionInfo)obj;
/* 53 */       result = (this.xid.equals(info.xid) && this.type == info.type);
/*    */     } 
/* 55 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 62 */     return this.xid.hashCode() ^ this.type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getType() {
/* 70 */     return this.type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setType(int newType) {
/* 77 */     this.type = newType;
/*    */   }
/*    */   
/*    */   public ActiveMQXid getXid() {
/* 81 */     return this.xid;
/*    */   }
/*    */   
/*    */   public void setXid(ActiveMQXid xid) {
/* 85 */     this.xid = xid;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getTransactionTimeout() {
/* 92 */     return this.transactionTimeout;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setTransactionTimeout(int transactionTimeout) {
/* 99 */     this.transactionTimeout = transactionTimeout;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\XATransactionInfo.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */