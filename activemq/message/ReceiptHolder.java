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
/*    */ public class ReceiptHolder
/*    */ {
/*    */   private Receipt receipt;
/* 27 */   private Object lock = new Object();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean notified;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setReceipt(Receipt r) {
/* 42 */     synchronized (this.lock) {
/* 43 */       this.receipt = r;
/* 44 */       this.notified = true;
/* 45 */       this.lock.notify();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Receipt getReceipt() {
/* 55 */     return getReceipt(0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Receipt getReceipt(int timeout) {
/* 65 */     synchronized (this.lock) {
/* 66 */       if (!this.notified) {
/*    */         try {
/* 68 */           this.lock.wait(timeout);
/*    */         }
/* 70 */         catch (InterruptedException e) {
/* 71 */           e.printStackTrace();
/*    */         } 
/*    */       }
/*    */     } 
/* 75 */     return this.receipt;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() {
/* 82 */     synchronized (this.lock) {
/* 83 */       this.notified = true;
/* 84 */       this.lock.notifyAll();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ReceiptHolder.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */