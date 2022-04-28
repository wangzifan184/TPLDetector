/*    */ package org.codehaus.activemq.management;
/*    */ 
/*    */ import javax.jms.Destination;
/*    */ import javax.management.j2ee.statistics.JMSConsumerStats;
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
/*    */ public class JMSConsumerStatsImpl
/*    */   extends JMSEndpointStatsImpl
/*    */   implements JMSConsumerStats
/*    */ {
/*    */   private String origin;
/*    */   
/*    */   public JMSConsumerStatsImpl(JMSSessionStatsImpl sessionStats, Destination destination) {
/* 34 */     super(sessionStats);
/* 35 */     if (destination != null) {
/* 36 */       this.origin = destination.toString();
/*    */     }
/*    */   }
/*    */   
/*    */   public JMSConsumerStatsImpl(CountStatisticImpl messageCount, CountStatisticImpl pendingMessageCount, CountStatisticImpl expiredMessageCount, TimeStatisticImpl messageWaitTime, TimeStatisticImpl messageRateTime, String origin) {
/* 41 */     super(messageCount, pendingMessageCount, expiredMessageCount, messageWaitTime, messageRateTime);
/* 42 */     this.origin = origin;
/*    */   }
/*    */   
/*    */   public String getOrigin() {
/* 46 */     return this.origin;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 50 */     StringBuffer buffer = new StringBuffer();
/* 51 */     buffer.append("consumer ");
/* 52 */     buffer.append(this.origin);
/* 53 */     buffer.append(" { ");
/* 54 */     buffer.append(super.toString());
/* 55 */     buffer.append(" }");
/* 56 */     return buffer.toString();
/*    */   }
/*    */   
/*    */   public void dump(IndentPrinter out) {
/* 60 */     out.printIndent();
/* 61 */     out.print("consumer ");
/* 62 */     out.print(this.origin);
/* 63 */     out.println(" {");
/* 64 */     out.incrementIndent();
/*    */     
/* 66 */     super.dump(out);
/*    */     
/* 68 */     out.decrementIndent();
/* 69 */     out.printIndent();
/* 70 */     out.println("}");
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\management\JMSConsumerStatsImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */