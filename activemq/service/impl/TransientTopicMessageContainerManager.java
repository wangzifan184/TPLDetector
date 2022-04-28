/*     */ package org.codehaus.activemq.service.impl;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.broker.BrokerClient;
/*     */ import org.codehaus.activemq.filter.FilterFactory;
/*     */ import org.codehaus.activemq.filter.FilterFactoryImpl;
/*     */ import org.codehaus.activemq.message.ActiveMQDestination;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.ConsumerInfo;
/*     */ import org.codehaus.activemq.service.Dispatcher;
/*     */ import org.codehaus.activemq.service.MessageContainer;
/*     */ import org.codehaus.activemq.service.RedeliveryPolicy;
/*     */ import org.codehaus.activemq.service.Subscription;
/*     */ import org.codehaus.activemq.service.SubscriptionContainer;
/*     */ import org.codehaus.activemq.store.PersistenceAdapter;
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
/*     */ public class TransientTopicMessageContainerManager
/*     */   extends DurableTopicMessageContainerManager
/*     */ {
/*  46 */   private static final Log log = LogFactory.getLog(TransientTopicMessageContainerManager.class);
/*     */   
/*     */   public TransientTopicMessageContainerManager(PersistenceAdapter persistenceAdapter) {
/*  49 */     this(persistenceAdapter, new SubscriptionContainerImpl(new RedeliveryPolicy()), (FilterFactory)new FilterFactoryImpl(), new DispatcherImpl());
/*     */   }
/*     */   
/*     */   public TransientTopicMessageContainerManager(PersistenceAdapter persistenceAdapter, SubscriptionContainer subscriptionContainer, FilterFactory filterFactory, Dispatcher dispatcher) {
/*  53 */     super(persistenceAdapter, subscriptionContainer, filterFactory, dispatcher);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMessageConsumer(BrokerClient client, ConsumerInfo info) throws JMSException {
/*  62 */     if (info.getDestination().isTopic()) {
/*  63 */       doAddMessageConsumer(client, info);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeMessageConsumer(BrokerClient client, ConsumerInfo info) throws JMSException {
/*  74 */     Subscription sub = (Subscription)this.activeSubscriptions.remove(info.getConsumerId());
/*  75 */     if (sub != null) {
/*  76 */       sub.setActive(false);
/*  77 */       this.dispatcher.removeActiveSubscription(client, sub);
/*  78 */       this.subscriptionContainer.removeSubscription(info.getConsumerId());
/*  79 */       sub.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendMessage(BrokerClient client, ActiveMQMessage message) throws JMSException {
/*  90 */     ActiveMQDestination destination = message.getJMSActiveMQDestination();
/*  91 */     if (destination != null && destination.isTopic()) {
/*  92 */       MessageContainer container = null;
/*  93 */       if (log.isDebugEnabled()) {
/*  94 */         log.debug("Dispaching to " + this.subscriptionContainer + " subscriptions with message: " + message);
/*     */       }
/*  96 */       Set subscriptions = this.subscriptionContainer.getSubscriptions(destination);
/*  97 */       for (Iterator i = subscriptions.iterator(); i.hasNext(); ) {
/*  98 */         Subscription sub = i.next();
/*  99 */         if (sub.isTarget(message) && (!sub.isDurableTopic() || message.getJMSDeliveryMode() == 1)) {
/* 100 */           if (container == null) {
/* 101 */             container = getContainer(message.getJMSDestination().toString());
/* 102 */             container.addMessage(message);
/*     */           } 
/* 104 */           sub.addMessage(container, message);
/*     */         } 
/*     */       } 
/* 107 */       updateSendStats(client, message);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void deleteSubscription(String clientId, String subscriberName) throws JMSException {}
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\TransientTopicMessageContainerManager.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */