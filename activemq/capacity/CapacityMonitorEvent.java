/*    */ package org.codehaus.activemq.capacity;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CapacityMonitorEvent
/*    */ {
/*    */   private String monitorName;
/*    */   private int capacity;
/*    */   
/*    */   public CapacityMonitorEvent() {}
/*    */   
/*    */   public CapacityMonitorEvent(String name, int newCapacity) {
/* 47 */     this.monitorName = name;
/* 48 */     this.capacity = newCapacity;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getCapacity() {
/* 56 */     return this.capacity;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCapacity(int capacity) {
/* 62 */     this.capacity = capacity;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMonitorName() {
/* 68 */     return this.monitorName;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setMonitorName(String monitorName) {
/* 74 */     this.monitorName = monitorName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 81 */     return this.monitorName + ": capacity = " + this.capacity;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\capacity\CapacityMonitorEvent.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */