/*    */ package org.codehaus.activemq;
/*    */ 
/*    */ import javax.jms.JMSException;
/*    */ import javax.jms.Queue;
/*    */ import javax.jms.QueueReceiver;
/*    */ import org.codehaus.activemq.message.ActiveMQDestination;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ActiveMQQueueReceiver
/*    */   extends ActiveMQMessageConsumer
/*    */   implements QueueReceiver
/*    */ {
/*    */   protected ActiveMQQueueReceiver(ActiveMQSession theSession, ActiveMQDestination dest, String selector, int cnum, int prefetch) throws JMSException {
/* 72 */     super(theSession, dest, "", selector, cnum, prefetch, false, false);
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
/*    */   public Queue getQueue() throws JMSException {
/* 84 */     checkClosed();
/* 85 */     return (Queue)getDestination();
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ActiveMQQueueReceiver.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */