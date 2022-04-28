/*    */ package org.codehaus.activemq.service.impl;
/*    */ 
/*    */ import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.codehaus.activemq.broker.BrokerClient;
/*    */ import org.codehaus.activemq.filter.DestinationMap;
/*    */ import org.codehaus.activemq.filter.Filter;
/*    */ import org.codehaus.activemq.message.ActiveMQDestination;
/*    */ import org.codehaus.activemq.message.ConsumerInfo;
/*    */ import org.codehaus.activemq.service.Dispatcher;
/*    */ import org.codehaus.activemq.service.RedeliveryPolicy;
/*    */ import org.codehaus.activemq.service.Subscription;
/*    */ import org.codehaus.activemq.service.SubscriptionContainer;
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
/*    */ public class SubscriptionContainerImpl
/*    */   implements SubscriptionContainer
/*    */ {
/*    */   private Map subscriptions;
/* 43 */   private DestinationMap destinationIndex = new DestinationMap();
/*    */   private RedeliveryPolicy redeliveryPolicy;
/*    */   
/*    */   public SubscriptionContainerImpl(RedeliveryPolicy redeliveryPolicy) {
/* 47 */     this((Map)new ConcurrentHashMap(), redeliveryPolicy);
/*    */   }
/*    */   
/*    */   public SubscriptionContainerImpl(Map subscriptions, RedeliveryPolicy redeliveryPolicy) {
/* 51 */     this.subscriptions = subscriptions;
/* 52 */     this.redeliveryPolicy = redeliveryPolicy;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 56 */     return super.toString() + "[size:" + this.subscriptions.size() + "]";
/*    */   }
/*    */   
/*    */   public RedeliveryPolicy getRedeliveryPolicy() {
/* 60 */     return this.redeliveryPolicy;
/*    */   }
/*    */   
/*    */   public Subscription getSubscription(String consumerId) {
/* 64 */     return (Subscription)this.subscriptions.get(consumerId);
/*    */   }
/*    */   
/*    */   public Subscription removeSubscription(String consumerId) {
/* 68 */     Subscription subscription = (Subscription)this.subscriptions.remove(consumerId);
/* 69 */     if (subscription != null) {
/* 70 */       this.destinationIndex.remove(subscription.getDestination(), subscription);
/*    */     }
/* 72 */     return subscription;
/*    */   }
/*    */   
/*    */   public Set getSubscriptions(ActiveMQDestination destination) {
/* 76 */     Object answer = this.destinationIndex.get(destination);
/* 77 */     if (answer instanceof Set) {
/* 78 */       return (Set)answer;
/*    */     }
/*    */     
/* 81 */     Set set = new HashSet(1);
/* 82 */     set.add(answer);
/* 83 */     return set;
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator subscriptionIterator() {
/* 88 */     return this.subscriptions.values().iterator();
/*    */   }
/*    */   
/*    */   public Subscription makeSubscription(Dispatcher dispatcher, BrokerClient client, ConsumerInfo info, Filter filter) {
/* 92 */     Subscription subscription = createSubscription(dispatcher, client, info, filter);
/* 93 */     this.subscriptions.put(info.getConsumerId(), subscription);
/* 94 */     this.destinationIndex.put(subscription.getDestination(), subscription);
/* 95 */     return subscription;
/*    */   }
/*    */   
/*    */   protected Subscription createSubscription(Dispatcher dispatcher, BrokerClient client, ConsumerInfo info, Filter filter) {
/* 99 */     return new SubscriptionImpl(dispatcher, client, info, filter, getRedeliveryPolicy());
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\SubscriptionContainerImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */