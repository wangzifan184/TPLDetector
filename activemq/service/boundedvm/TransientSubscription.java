/*    */ package org.codehaus.activemq.service.boundedvm;
/*    */ 
/*    */ import javax.jms.JMSException;
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
/*    */ public abstract class TransientSubscription
/*    */ {
/*    */   protected Filter filter;
/*    */   protected ConsumerInfo consumerInfo;
/*    */   
/*    */   public TransientSubscription(Filter filter, ConsumerInfo info) {
/* 44 */     this.filter = filter;
/* 45 */     this.consumerInfo = info;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract boolean isTarget(ActiveMQMessage paramActiveMQMessage) throws JMSException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ConsumerInfo getConsumerInfo() {
/* 62 */     return this.consumerInfo;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setConsumerInfo(ConsumerInfo consumerInfo) {
/* 68 */     this.consumerInfo = consumerInfo;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Filter getFilter() {
/* 74 */     return this.filter;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setFilter(Filter filter) {
/* 80 */     this.filter = filter;
/*    */   }
/*    */   
/*    */   public void close() {}
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\boundedvm\TransientSubscription.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */