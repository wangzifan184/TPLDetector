/*    */ package org.codehaus.activemq.message;
/*    */ 
/*    */ import javax.jms.Destination;
/*    */ import javax.jms.Queue;
/*    */ import org.codehaus.activemq.management.JMSDestinationStats;
/*    */ import org.codehaus.activemq.management.JMSQueueStatsImpl;
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
/*    */ public class ActiveMQQueue
/*    */   extends ActiveMQDestination
/*    */   implements Queue
/*    */ {
/*    */   public ActiveMQQueue() {}
/*    */   
/*    */   public ActiveMQQueue(String name) {
/* 66 */     super(name);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getQueueName() {
/* 78 */     return getPhysicalName();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getDestinationType() {
/* 86 */     return 3;
/*    */   }
/*    */   
/*    */   protected Destination createDestination(String name) {
/* 90 */     return new ActiveMQQueue(name);
/*    */   }
/*    */   
/*    */   protected JMSDestinationStats createDestinationStats() {
/* 94 */     return (JMSDestinationStats)new JMSQueueStatsImpl();
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ActiveMQQueue.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */