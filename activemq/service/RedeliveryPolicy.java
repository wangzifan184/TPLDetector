/*    */ package org.codehaus.activemq.service;
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
/*    */ public class RedeliveryPolicy
/*    */ {
/* 29 */   private int maximumRetryCount = 5;
/*    */   private boolean backOffMode = true;
/* 31 */   private long initialRedeliveryTimeout = 1000L;
/* 32 */   private double backOffIncreaseRate = 2.0D;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isBackOffMode() {
/* 39 */     return this.backOffMode;
/*    */   }
/*    */   
/*    */   public void setBackOffMode(boolean backOffMode) {
/* 43 */     this.backOffMode = backOffMode;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long getInitialRedeliveryTimeout() {
/* 50 */     return this.initialRedeliveryTimeout;
/*    */   }
/*    */   
/*    */   public void setInitialRedeliveryTimeout(long initialRedeliveryTimeout) {
/* 54 */     this.initialRedeliveryTimeout = initialRedeliveryTimeout;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getMaximumRetryCount() {
/* 62 */     return this.maximumRetryCount;
/*    */   }
/*    */   
/*    */   public void setMaximumRetryCount(int maximumRetryCount) {
/* 66 */     this.maximumRetryCount = maximumRetryCount;
/*    */   }
/*    */   
/*    */   public double getBackOffIncreaseRate() {
/* 70 */     return this.backOffIncreaseRate;
/*    */   }
/*    */   
/*    */   public void setBackOffIncreaseRate(double backOffIncreaseRate) {
/* 74 */     this.backOffIncreaseRate = backOffIncreaseRate;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\RedeliveryPolicy.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */