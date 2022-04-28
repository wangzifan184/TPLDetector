/*    */ package org.codehaus.activemq.management;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import javax.management.j2ee.statistics.Statistic;
/*    */ import javax.management.j2ee.statistics.Stats;
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
/*    */ public class StatsImpl
/*    */   implements Stats, Resettable
/*    */ {
/* 33 */   private Map map = new HashMap();
/*    */ 
/*    */   
/*    */   public StatsImpl() {}
/*    */   
/*    */   public StatsImpl(Map map) {
/* 39 */     this.map = map;
/*    */   }
/*    */   
/*    */   public void reset() {
/* 43 */     Statistic[] stats = getStatistics();
/* 44 */     for (int i = 0, size = stats.length; i < size; i++) {
/* 45 */       Statistic stat = stats[i];
/* 46 */       if (stat instanceof Resettable) {
/* 47 */         Resettable r = (Resettable)stat;
/* 48 */         r.reset();
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public Statistic getStatistic(String name) {
/* 54 */     return (Statistic)this.map.get(name);
/*    */   }
/*    */   
/*    */   public String[] getStatisticNames() {
/* 58 */     Set keys = this.map.keySet();
/* 59 */     String[] answer = new String[keys.size()];
/* 60 */     keys.toArray((Object[])answer);
/* 61 */     return answer;
/*    */   }
/*    */   
/*    */   public Statistic[] getStatistics() {
/* 65 */     Collection values = this.map.values();
/* 66 */     Statistic[] answer = new Statistic[values.size()];
/* 67 */     values.toArray((Object[])answer);
/* 68 */     return answer;
/*    */   }
/*    */   
/*    */   protected void addStatistic(String name, Statistic statistic) {
/* 72 */     this.map.put(name, statistic);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\management\StatsImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */