/*     */ package org.codehaus.activemq.message;
/*     */ 
/*     */ import javax.jms.Destination;
/*     */ import javax.jms.Topic;
/*     */ import org.codehaus.activemq.management.JMSDestinationStats;
/*     */ import org.codehaus.activemq.management.JMSTopicStatsImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ActiveMQTopic
/*     */   extends ActiveMQDestination
/*     */   implements Topic
/*     */ {
/*     */   public ActiveMQTopic() {}
/*     */   
/*     */   public ActiveMQTopic(String name) {
/*  79 */     super(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTopicName() {
/*  91 */     return getPhysicalName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDestinationType() {
/*  99 */     return 1;
/*     */   }
/*     */   
/*     */   protected Destination createDestination(String name) {
/* 103 */     return new ActiveMQTopic(name);
/*     */   }
/*     */   
/*     */   protected JMSDestinationStats createDestinationStats() {
/* 107 */     return (JMSDestinationStats)new JMSTopicStatsImpl();
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ActiveMQTopic.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */