/*    */ package org.codehaus.activemq.management;
/*    */ 
/*    */ import javax.management.j2ee.statistics.BoundedRangeStatistic;
/*    */ import javax.management.j2ee.statistics.CountStatistic;
/*    */ import javax.management.j2ee.statistics.JCAConnectionPoolStats;
/*    */ import javax.management.j2ee.statistics.RangeStatistic;
/*    */ import javax.management.j2ee.statistics.Statistic;
/*    */ import javax.management.j2ee.statistics.TimeStatistic;
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
/*    */ public class JCAConnectionPoolStatsImpl
/*    */   extends JCAConnectionStatsImpl
/*    */   implements JCAConnectionPoolStats
/*    */ {
/*    */   private CountStatistic closeCount;
/*    */   private CountStatistic createCount;
/*    */   private BoundedRangeStatistic freePoolSize;
/*    */   private BoundedRangeStatistic poolSize;
/*    */   private RangeStatistic waitingThreadCount;
/*    */   
/*    */   public JCAConnectionPoolStatsImpl(String connectionFactory, String managedConnectionFactory, TimeStatistic waitTime, TimeStatistic useTime, CountStatistic closeCount, CountStatistic createCount, BoundedRangeStatistic freePoolSize, BoundedRangeStatistic poolSize, RangeStatistic waitingThreadCount) {
/* 39 */     super(connectionFactory, managedConnectionFactory, waitTime, useTime);
/* 40 */     this.closeCount = closeCount;
/* 41 */     this.createCount = createCount;
/* 42 */     this.freePoolSize = freePoolSize;
/* 43 */     this.poolSize = poolSize;
/* 44 */     this.waitingThreadCount = waitingThreadCount;
/*    */ 
/*    */     
/* 47 */     addStatistic("freePoolSize", (Statistic)freePoolSize);
/* 48 */     addStatistic("poolSize", (Statistic)poolSize);
/* 49 */     addStatistic("waitingThreadCount", (Statistic)waitingThreadCount);
/*    */   }
/*    */   
/*    */   public CountStatistic getCloseCount() {
/* 53 */     return this.closeCount;
/*    */   }
/*    */   
/*    */   public CountStatistic getCreateCount() {
/* 57 */     return this.createCount;
/*    */   }
/*    */   
/*    */   public BoundedRangeStatistic getFreePoolSize() {
/* 61 */     return this.freePoolSize;
/*    */   }
/*    */   
/*    */   public BoundedRangeStatistic getPoolSize() {
/* 65 */     return this.poolSize;
/*    */   }
/*    */   
/*    */   public RangeStatistic getWaitingThreadCount() {
/* 69 */     return this.waitingThreadCount;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\management\JCAConnectionPoolStatsImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */