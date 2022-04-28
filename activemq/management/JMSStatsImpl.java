/*    */ package org.codehaus.activemq.management;
/*    */ 
/*    */ import EDU.oswego.cs.dl.util.concurrent.CopyOnWriteArrayList;
/*    */ import java.util.List;
/*    */ import javax.management.j2ee.statistics.JMSConnectionStats;
/*    */ import javax.management.j2ee.statistics.JMSStats;
/*    */ import org.codehaus.activemq.ActiveMQConnection;
/*    */ import org.codehaus.activemq.util.IndentPrinter;
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
/*    */ public class JMSStatsImpl
/*    */   extends StatsImpl
/*    */   implements JMSStats
/*    */ {
/* 34 */   private List connections = (List)new CopyOnWriteArrayList();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JMSConnectionStats[] getConnections() {
/* 40 */     Object[] connectionArray = this.connections.toArray();
/* 41 */     int size = connectionArray.length;
/* 42 */     JMSConnectionStats[] answer = new JMSConnectionStats[size];
/* 43 */     for (int i = 0; i < size; i++) {
/* 44 */       ActiveMQConnection connection = (ActiveMQConnection)connectionArray[i];
/* 45 */       answer[i] = connection.getConnectionStats();
/*    */     } 
/* 47 */     return answer;
/*    */   }
/*    */   
/*    */   public void addConnection(ActiveMQConnection connection) {
/* 51 */     this.connections.add(connection);
/*    */   }
/*    */   
/*    */   public void removeConnection(ActiveMQConnection connection) {
/* 55 */     this.connections.remove(connection);
/*    */   }
/*    */   
/*    */   public void dump(IndentPrinter out) {
/* 59 */     out.printIndent();
/* 60 */     out.println("factory {");
/* 61 */     out.incrementIndent();
/* 62 */     JMSConnectionStats[] array = getConnections();
/* 63 */     for (int i = 0; i < array.length; i++) {
/* 64 */       JMSConnectionStatsImpl connectionStat = (JMSConnectionStatsImpl)array[i];
/* 65 */       connectionStat.dump(out);
/*    */     } 
/* 67 */     out.decrementIndent();
/* 68 */     out.printIndent();
/* 69 */     out.println("}");
/* 70 */     out.flush();
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\management\JMSStatsImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */