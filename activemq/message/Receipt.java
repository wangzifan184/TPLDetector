/*     */ package org.codehaus.activemq.message;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Receipt
/*     */   extends AbstractPacket
/*     */ {
/*     */   private String correlationId;
/*     */   private String brokerName;
/*     */   private String clusterName;
/*     */   private Throwable exception;
/*     */   private boolean failed;
/*  32 */   private int brokerMessageCapacity = 100;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getException() {
/*  39 */     return this.exception;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setException(Throwable exception) {
/*  46 */     this.exception = exception;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPacketType() {
/*  56 */     return 16;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReceipt() {
/*  63 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCorrelationId() {
/*  70 */     return this.correlationId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCorrelationId(String newCorrelationId) {
/*  77 */     this.correlationId = newCorrelationId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFailed() {
/*  84 */     return this.failed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailed(boolean newFailed) {
/*  91 */     this.failed = newFailed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  99 */     String str = super.toString();
/* 100 */     str = str + " correlationId = " + this.correlationId + " failed = " + this.failed + " exp = " + this.exception;
/* 101 */     str = str + " , brokerName = " + this.brokerName;
/* 102 */     str = str + " , brokerMessageCapacity = " + this.brokerMessageCapacity;
/* 103 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBrokerMessageCapacity() {
/* 110 */     return this.brokerMessageCapacity;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBrokerMessageCapacity(int brokerMessageCapacity) {
/* 116 */     this.brokerMessageCapacity = brokerMessageCapacity;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBrokerName() {
/* 122 */     return this.brokerName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBrokerName(String brokerName) {
/* 128 */     this.brokerName = brokerName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClusterName() {
/* 134 */     return this.clusterName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClusterName(String clusterName) {
/* 140 */     this.clusterName = clusterName;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\Receipt.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */