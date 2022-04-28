/*    */ package org.codehaus.activemq.service.impl;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.codehaus.activemq.broker.BrokerClient;
/*    */ import org.codehaus.activemq.filter.Filter;
/*    */ import org.codehaus.activemq.message.ConsumerInfo;
/*    */ import org.codehaus.activemq.service.Dispatcher;
/*    */ import org.codehaus.activemq.service.RedeliveryPolicy;
/*    */ import org.codehaus.activemq.service.Subscription;
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
/*    */ public class DurableTopicSubscriptionContainerImpl
/*    */   extends SubscriptionContainerImpl
/*    */ {
/*    */   public DurableTopicSubscriptionContainerImpl(RedeliveryPolicy redeliveryPolicy) {
/* 38 */     super(redeliveryPolicy);
/*    */   }
/*    */   
/*    */   public DurableTopicSubscriptionContainerImpl(Map subscriptions, RedeliveryPolicy redeliveryPolicy) {
/* 42 */     super(subscriptions, redeliveryPolicy);
/*    */   }
/*    */   
/*    */   protected Subscription createSubscription(Dispatcher dispatcher, BrokerClient client, ConsumerInfo info, Filter filter) {
/* 46 */     return new DurableTopicSubscription(dispatcher, client, info, filter, getRedeliveryPolicy());
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\DurableTopicSubscriptionContainerImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */