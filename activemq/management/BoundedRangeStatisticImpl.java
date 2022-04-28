/*    */ package org.codehaus.activemq.management;
/*    */ 
/*    */ import javax.management.j2ee.statistics.BoundedRangeStatistic;
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
/*    */ public class BoundedRangeStatisticImpl
/*    */   extends RangeStatisticImpl
/*    */   implements BoundedRangeStatistic
/*    */ {
/*    */   private long lowerBound;
/*    */   private long upperBound;
/*    */   
/*    */   public BoundedRangeStatisticImpl(String name, String unit, String description, long lowerBound, long upperBound) {
/* 32 */     super(name, unit, description);
/* 33 */     this.lowerBound = lowerBound;
/* 34 */     this.upperBound = upperBound;
/*    */   }
/*    */   
/*    */   public long getLowerBound() {
/* 38 */     return this.lowerBound;
/*    */   }
/*    */   
/*    */   public long getUpperBound() {
/* 42 */     return this.upperBound;
/*    */   }
/*    */   
/*    */   protected void appendFieldDescription(StringBuffer buffer) {
/* 46 */     buffer.append(" lowerBound: ");
/* 47 */     buffer.append(Long.toString(this.lowerBound));
/* 48 */     buffer.append(" upperBound: ");
/* 49 */     buffer.append(Long.toString(this.upperBound));
/* 50 */     super.appendFieldDescription(buffer);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\management\BoundedRangeStatisticImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */