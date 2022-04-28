/*    */ package org.codehaus.activemq.management;
/*    */ 
/*    */ import javax.management.j2ee.statistics.JCAConnectionStats;
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
/*    */ public class JCAConnectionStatsImpl
/*    */   extends StatsImpl
/*    */   implements JCAConnectionStats
/*    */ {
/*    */   private String connectionFactory;
/*    */   private String managedConnectionFactory;
/*    */   private TimeStatistic waitTime;
/*    */   private TimeStatistic useTime;
/*    */   
/*    */   public JCAConnectionStatsImpl(String connectionFactory, String managedConnectionFactory, TimeStatistic waitTime, TimeStatistic useTime) {
/* 35 */     this.connectionFactory = connectionFactory;
/* 36 */     this.managedConnectionFactory = managedConnectionFactory;
/* 37 */     this.waitTime = waitTime;
/* 38 */     this.useTime = useTime;
/*    */ 
/*    */     
/* 41 */     addStatistic("waitTime", (Statistic)waitTime);
/* 42 */     addStatistic("useTime", (Statistic)useTime);
/*    */   }
/*    */   
/*    */   public String getConnectionFactory() {
/* 46 */     return this.connectionFactory;
/*    */   }
/*    */   
/*    */   public String getManagedConnectionFactory() {
/* 50 */     return this.managedConnectionFactory;
/*    */   }
/*    */   
/*    */   public TimeStatistic getWaitTime() {
/* 54 */     return this.waitTime;
/*    */   }
/*    */   
/*    */   public TimeStatistic getUseTime() {
/* 58 */     return this.useTime;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\management\JCAConnectionStatsImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */