/*    */ package org.codehaus.activemq.management;
/*    */ 
/*    */ import javax.management.j2ee.statistics.Statistic;
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
/*    */ public class StatisticImpl
/*    */   implements Statistic, Resettable
/*    */ {
/*    */   private String name;
/*    */   private String unit;
/*    */   private String description;
/*    */   private long startTime;
/*    */   private long lastSampleTime;
/*    */   
/*    */   public StatisticImpl(String name, String unit, String description) {
/* 35 */     this.name = name;
/* 36 */     this.unit = unit;
/* 37 */     this.description = description;
/* 38 */     this.startTime = System.currentTimeMillis();
/* 39 */     this.lastSampleTime = this.startTime;
/*    */   }
/*    */   
/*    */   public synchronized void reset() {
/* 43 */     this.startTime = System.currentTimeMillis();
/* 44 */     this.lastSampleTime = this.startTime;
/*    */   }
/*    */   
/*    */   protected synchronized void updateSampleTime() {
/* 48 */     this.lastSampleTime = System.currentTimeMillis();
/*    */   }
/*    */   
/*    */   public synchronized String toString() {
/* 52 */     StringBuffer buffer = new StringBuffer();
/* 53 */     buffer.append(this.name);
/* 54 */     buffer.append("{");
/* 55 */     appendFieldDescription(buffer);
/* 56 */     buffer.append(" }");
/* 57 */     return buffer.toString();
/*    */   }
/*    */   
/*    */   public String getName() {
/* 61 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getUnit() {
/* 65 */     return this.unit;
/*    */   }
/*    */   
/*    */   public String getDescription() {
/* 69 */     return this.description;
/*    */   }
/*    */   
/*    */   public synchronized long getStartTime() {
/* 73 */     return this.startTime;
/*    */   }
/*    */   
/*    */   public synchronized long getLastSampleTime() {
/* 77 */     return this.lastSampleTime;
/*    */   }
/*    */   
/*    */   protected synchronized void appendFieldDescription(StringBuffer buffer) {
/* 81 */     buffer.append(" unit: ");
/* 82 */     buffer.append(this.unit);
/* 83 */     buffer.append(" startTime: ");
/*    */     
/* 85 */     buffer.append(this.startTime);
/* 86 */     buffer.append(" lastSampleTime: ");
/*    */     
/* 88 */     buffer.append(this.lastSampleTime);
/* 89 */     buffer.append(" description: ");
/* 90 */     buffer.append(this.description);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\management\StatisticImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */