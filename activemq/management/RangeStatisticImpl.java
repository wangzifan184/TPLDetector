/*    */ package org.codehaus.activemq.management;
/*    */ 
/*    */ import javax.management.j2ee.statistics.RangeStatistic;
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
/*    */ public class RangeStatisticImpl
/*    */   extends StatisticImpl
/*    */   implements RangeStatistic
/*    */ {
/*    */   private long highWaterMark;
/*    */   private long lowWaterMark;
/*    */   private long current;
/*    */   
/*    */   public RangeStatisticImpl(String name, String unit, String description) {
/* 33 */     super(name, unit, description);
/*    */   }
/*    */   
/*    */   public void reset() {
/* 37 */     super.reset();
/* 38 */     this.current = 0L;
/* 39 */     this.lowWaterMark = 0L;
/* 40 */     this.highWaterMark = 0L;
/*    */   }
/*    */   
/*    */   public long getHighWaterMark() {
/* 44 */     return this.highWaterMark;
/*    */   }
/*    */   
/*    */   public long getLowWaterMark() {
/* 48 */     return this.lowWaterMark;
/*    */   }
/*    */   
/*    */   public long getCurrent() {
/* 52 */     return this.current;
/*    */   }
/*    */   
/*    */   public void setCurrent(long current) {
/* 56 */     this.current = current;
/* 57 */     if (current > this.highWaterMark) {
/* 58 */       this.highWaterMark = current;
/*    */     }
/* 60 */     if (current < this.lowWaterMark || this.lowWaterMark == 0L) {
/* 61 */       this.lowWaterMark = current;
/*    */     }
/* 63 */     updateSampleTime();
/*    */   }
/*    */   
/*    */   protected void appendFieldDescription(StringBuffer buffer) {
/* 67 */     buffer.append(" current: ");
/* 68 */     buffer.append(Long.toString(this.current));
/* 69 */     buffer.append(" lowWaterMark: ");
/* 70 */     buffer.append(Long.toString(this.lowWaterMark));
/* 71 */     buffer.append(" highWaterMark: ");
/* 72 */     buffer.append(Long.toString(this.highWaterMark));
/* 73 */     super.appendFieldDescription(buffer);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\management\RangeStatisticImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */