/*    */ package org.codehaus.activemq.management;
/*    */ 
/*    */ import javax.management.j2ee.statistics.JCAConnectionPoolStats;
/*    */ import javax.management.j2ee.statistics.JCAConnectionStats;
/*    */ import javax.management.j2ee.statistics.JCAStats;
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
/*    */ public class JCAStatsImpl
/*    */   extends StatsImpl
/*    */   implements JCAStats
/*    */ {
/*    */   private JCAConnectionStats[] connectionStats;
/*    */   private JCAConnectionPoolStats[] connectionPoolStats;
/*    */   
/*    */   public JCAStatsImpl(JCAConnectionStats[] connectionStats, JCAConnectionPoolStats[] connectionPoolStats) {
/* 34 */     this.connectionStats = connectionStats;
/* 35 */     this.connectionPoolStats = connectionPoolStats;
/*    */   }
/*    */   
/*    */   public JCAConnectionStats[] getConnections() {
/* 39 */     return this.connectionStats;
/*    */   }
/*    */   
/*    */   public JCAConnectionPoolStats[] getConnectionPools() {
/* 43 */     return this.connectionPoolStats;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\management\JCAStatsImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */