/*    */ package org.codehaus.activemq.message;
/*    */ 
/*    */ import javax.jms.JMSException;
/*    */ import javax.jms.TemporaryTopic;
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
/*    */ public class ActiveMQTemporaryTopic
/*    */   extends ActiveMQTopic
/*    */   implements TemporaryTopic
/*    */ {
/*    */   public ActiveMQTemporaryTopic() {}
/*    */   
/*    */   public ActiveMQTemporaryTopic(String name) {
/* 59 */     super(name);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void delete() throws JMSException {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getDestinationType() {
/* 79 */     return 4;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ActiveMQTemporaryTopic.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */