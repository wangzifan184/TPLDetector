/*    */ package org.codehaus.activemq;
/*    */ 
/*    */ import javax.jms.JMSException;
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
/*    */ public class DuplicateDurableSubscriptionException
/*    */   extends JMSException
/*    */ {
/*    */   private String clientID;
/*    */   private String consumerName;
/*    */   
/*    */   public DuplicateDurableSubscriptionException(ConsumerInfo info) {
/* 36 */     super("Duplicate JMS subscription for clientID: " + info.getClientId() + " and consumer: " + info.getConsumerName(), "AMQ-1000");
/* 37 */     this.clientID = info.getClientId();
/* 38 */     this.consumerName = info.getConsumerName();
/*    */   }
/*    */   
/*    */   public String getClientID() {
/* 42 */     return this.clientID;
/*    */   }
/*    */   
/*    */   public String getConsumerName() {
/* 46 */     return this.consumerName;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\DuplicateDurableSubscriptionException.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */