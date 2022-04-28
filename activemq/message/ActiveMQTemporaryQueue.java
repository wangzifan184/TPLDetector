/*    */ package org.codehaus.activemq.message;
/*    */ 
/*    */ import javax.jms.JMSException;
/*    */ import javax.jms.TemporaryQueue;
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
/*    */ public class ActiveMQTemporaryQueue
/*    */   extends ActiveMQQueue
/*    */   implements TemporaryQueue
/*    */ {
/*    */   public ActiveMQTemporaryQueue() {}
/*    */   
/*    */   public ActiveMQTemporaryQueue(String name) {
/* 58 */     super(name);
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
/*    */   public int getDestinationType() {
/* 77 */     return 4;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ActiveMQTemporaryQueue.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */