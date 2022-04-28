/*    */ package org.codehaus.activemq.service.boundedvm;
/*    */ 
/*    */ import javax.jms.JMSException;
/*    */ import javax.jms.Message;
/*    */ import org.codehaus.activemq.filter.Filter;
/*    */ import org.codehaus.activemq.message.ActiveMQMessage;
/*    */ import org.codehaus.activemq.message.ConsumerInfo;
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
/*    */ public class TransientTopicSubscription
/*    */   extends TransientSubscription
/*    */ {
/*    */   public TransientTopicSubscription(Filter filter, ConsumerInfo info) {
/* 43 */     super(filter, info);
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
/*    */   public boolean isTarget(ActiveMQMessage message) throws JMSException {
/* 55 */     boolean result = false;
/* 56 */     if (message != null) {
/* 57 */       result = this.filter.matches((Message)message);
/*    */       
/* 59 */       if (result && this.consumerInfo.isDurableTopic()) {
/* 60 */         result = (message.getJMSDeliveryMode() == 1);
/*    */       }
/*    */     } 
/* 63 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\boundedvm\TransientTopicSubscription.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */