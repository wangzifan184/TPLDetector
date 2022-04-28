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
/*    */ public class CapacityInfo
/*    */   extends AbstractPacket
/*    */ {
/*    */   private String resourceName;
/*    */   private String correlationId;
/* 30 */   private int capacity = 100;
/*    */   
/*    */   private long flowControlTimeout;
/*    */ 
/*    */   
/*    */   public int getPacketType() {
/* 36 */     return 27;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getCapacity() {
/* 44 */     return this.capacity;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCapacity(int capacity) {
/* 50 */     this.capacity = capacity;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getResourceName() {
/* 56 */     return this.resourceName;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setResourceName(String resourceName) {
/* 62 */     this.resourceName = resourceName;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getCorrelationId() {
/* 68 */     return this.correlationId;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCorrelationId(String correlationId) {
/* 74 */     this.correlationId = correlationId;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public long getFlowControlTimeout() {
/* 80 */     return this.flowControlTimeout;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setFlowControlTimeout(long flowControlTimeout) {
/* 86 */     this.flowControlTimeout = flowControlTimeout;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 93 */     return "CapacityInfo: cap=" + this.capacity + ",timeout=" + this.flowControlTimeout + ",resource=" + this.resourceName;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\CapacityInfo.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */