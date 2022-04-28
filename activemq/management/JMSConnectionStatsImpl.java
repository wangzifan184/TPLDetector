/*    */ package org.codehaus.activemq.management;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.management.j2ee.statistics.JMSConnectionStats;
/*    */ import javax.management.j2ee.statistics.JMSSessionStats;
/*    */ import org.codehaus.activemq.ActiveMQSession;
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
/*    */ public class JMSConnectionStatsImpl
/*    */   extends StatsImpl
/*    */   implements JMSConnectionStats
/*    */ {
/*    */   private List sessions;
/*    */   private boolean transactional;
/*    */   
/*    */   public JMSConnectionStatsImpl(List sessions, boolean transactional) {
/* 37 */     this.sessions = sessions;
/* 38 */     this.transactional = transactional;
/*    */   }
/*    */ 
/*    */   
/*    */   public JMSSessionStats[] getSessions() {
/* 43 */     Object[] sessionArray = this.sessions.toArray();
/* 44 */     int size = sessionArray.length;
/* 45 */     JMSSessionStats[] answer = new JMSSessionStats[size];
/* 46 */     for (int i = 0; i < size; i++) {
/* 47 */       ActiveMQSession session = (ActiveMQSession)sessionArray[i];
/* 48 */       answer[i] = session.getSessionStats();
/*    */     } 
/* 50 */     return answer;
/*    */   }
/*    */   
/*    */   public boolean isTransactional() {
/* 54 */     return this.transactional;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 58 */     StringBuffer buffer = new StringBuffer("connection{ ");
/* 59 */     JMSSessionStats[] array = getSessions();
/* 60 */     for (int i = 0; i < array.length; i++) {
/* 61 */       if (i > 0) {
/* 62 */         buffer.append(", ");
/*    */       }
/* 64 */       buffer.append(Integer.toString(i));
/* 65 */       buffer.append(" = ");
/* 66 */       buffer.append(array[i]);
/*    */     } 
/* 68 */     buffer.append(" }");
/* 69 */     return buffer.toString();
/*    */   }
/*    */   
/*    */   public void dump(IndentPrinter out) {
/* 73 */     out.printIndent();
/* 74 */     out.println("connection {");
/* 75 */     out.incrementIndent();
/* 76 */     JMSSessionStats[] array = getSessions();
/* 77 */     for (int i = 0; i < array.length; i++) {
/* 78 */       JMSSessionStatsImpl sessionStat = (JMSSessionStatsImpl)array[i];
/* 79 */       out.printIndent();
/* 80 */       out.println("session {");
/* 81 */       out.incrementIndent();
/* 82 */       sessionStat.dump(out);
/* 83 */       out.decrementIndent();
/* 84 */       out.printIndent();
/* 85 */       out.println("}");
/*    */     } 
/* 87 */     out.decrementIndent();
/* 88 */     out.printIndent();
/* 89 */     out.println("}");
/* 90 */     out.flush();
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\management\JMSConnectionStatsImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */