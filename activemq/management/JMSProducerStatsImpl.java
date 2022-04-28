/*    */ package org.codehaus.activemq.management;
/*    */ 
/*    */ import javax.jms.Destination;
/*    */ import javax.management.j2ee.statistics.JMSProducerStats;
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
/*    */ public class JMSProducerStatsImpl
/*    */   extends JMSEndpointStatsImpl
/*    */   implements JMSProducerStats
/*    */ {
/*    */   private String destination;
/*    */   
/*    */   public JMSProducerStatsImpl(JMSSessionStatsImpl sessionStats, Destination destination) {
/* 34 */     super(sessionStats);
/* 35 */     if (destination != null) {
/* 36 */       this.destination = destination.toString();
/*    */     }
/*    */   }
/*    */   
/*    */   public JMSProducerStatsImpl(CountStatisticImpl messageCount, CountStatisticImpl pendingMessageCount, CountStatisticImpl expiredMessageCount, TimeStatisticImpl messageWaitTime, TimeStatisticImpl messageRateTime, String destination) {
/* 41 */     super(messageCount, pendingMessageCount, expiredMessageCount, messageWaitTime, messageRateTime);
/* 42 */     this.destination = destination;
/*    */   }
/*    */   
/*    */   public String getDestination() {
/* 46 */     return this.destination;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 50 */     StringBuffer buffer = new StringBuffer();
/* 51 */     buffer.append("producer ");
/* 52 */     buffer.append(this.destination);
/* 53 */     buffer.append(" { ");
/* 54 */     buffer.append(super.toString());
/* 55 */     buffer.append(" }");
/* 56 */     return buffer.toString();
/*    */   }
/*    */   
/*    */   public void dump(IndentPrinter out) {
/* 60 */     out.printIndent();
/* 61 */     out.print("producer ");
/* 62 */     out.print(this.destination);
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


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\management\JMSProducerStatsImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */