/*     */ package org.codehaus.activemq.service.impl;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.jms.Destination;
/*     */ import javax.jms.IllegalStateException;
/*     */ import javax.jms.JMSException;
/*     */ import org.codehaus.activemq.DuplicateDurableSubscriptionException;
/*     */ import org.codehaus.activemq.broker.BrokerClient;
/*     */ import org.codehaus.activemq.filter.AndFilter;
/*     */ import org.codehaus.activemq.filter.DestinationMap;
/*     */ import org.codehaus.activemq.filter.Filter;
/*     */ import org.codehaus.activemq.filter.FilterFactory;
/*     */ import org.codehaus.activemq.filter.FilterFactoryImpl;
/*     */ import org.codehaus.activemq.filter.NoLocalFilter;
/*     */ import org.codehaus.activemq.message.ActiveMQDestination;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.ActiveMQTopic;
/*     */ import org.codehaus.activemq.message.ConsumerInfo;
/*     */ import org.codehaus.activemq.message.MessageAck;
/*     */ import org.codehaus.activemq.service.Dispatcher;
/*     */ import org.codehaus.activemq.service.MessageContainer;
/*     */ import org.codehaus.activemq.service.RedeliveryPolicy;
/*     */ import org.codehaus.activemq.service.Subscription;
/*     */ import org.codehaus.activemq.service.SubscriptionContainer;
/*     */ import org.codehaus.activemq.service.TopicMessageContainer;
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
/*     */ public class DurableTopicMessageContainerManager
/*     */   extends MessageContainerManagerSupport
/*     */ {
/*     */   private PersistenceAdapter persistenceAdapter;
/*     */   protected SubscriptionContainer subscriptionContainer;
/*     */   protected FilterFactory filterFactory;
/*  58 */   protected Map activeSubscriptions = (Map)new ConcurrentHashMap();
/*  59 */   private DestinationMap destinationMap = new DestinationMap();
/*     */   private boolean loadedMessageContainers;
/*     */   
/*     */   public DurableTopicMessageContainerManager(PersistenceAdapter persistenceAdapter, RedeliveryPolicy redeliveryPolicy) {
/*  63 */     this(persistenceAdapter, new DurableTopicSubscriptionContainerImpl(redeliveryPolicy), (FilterFactory)new FilterFactoryImpl(), new DispatcherImpl());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DurableTopicMessageContainerManager(PersistenceAdapter persistenceAdapter, SubscriptionContainer subscriptionContainer, FilterFactory filterFactory, Dispatcher dispatcher) {
/*  69 */     super(dispatcher);
/*  70 */     this.persistenceAdapter = persistenceAdapter;
/*  71 */     this.subscriptionContainer = subscriptionContainer;
/*  72 */     this.filterFactory = filterFactory;
/*     */   }
/*     */   
/*     */   public void addMessageConsumer(BrokerClient client, ConsumerInfo info) throws JMSException {
/*  76 */     if (info.isDurableTopic()) {
/*  77 */       doAddMessageConsumer(client, info);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeMessageConsumer(BrokerClient client, ConsumerInfo info) throws JMSException {
/*  84 */     Subscription sub = (Subscription)this.activeSubscriptions.remove(info.getConsumerId());
/*  85 */     if (sub != null) {
/*  86 */       sub.setActive(false);
/*  87 */       this.dispatcher.removeActiveSubscription(client, sub);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteSubscription(String clientId, String subscriberName) throws JMSException {
/*  99 */     boolean subscriptionFound = false;
/* 100 */     for (Iterator i = this.subscriptionContainer.subscriptionIterator(); i.hasNext(); ) {
/* 101 */       Subscription sub = i.next();
/* 102 */       if (sub.getClientId().equals(clientId) && sub.getSubscriberName().equals(subscriberName)) {
/*     */         
/* 104 */         if (sub.isActive()) {
/* 105 */           throw new JMSException("The Consummer " + subscriberName + " is still active");
/*     */         }
/*     */         
/* 108 */         this.subscriptionContainer.removeSubscription(sub.getConsumerId());
/* 109 */         sub.clear();
/* 110 */         subscriptionFound = true;
/*     */       } 
/*     */     } 
/*     */     
/* 114 */     if (!subscriptionFound) {
/* 115 */       throw new IllegalStateException("The Consumer " + subscriberName + " does not exist for client: " + clientId);
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
/* 126 */     ActiveMQDestination dest = (ActiveMQDestination)message.getJMSDestination();
/* 127 */     if (dest != null && dest.isTopic() && message.getJMSDeliveryMode() == 2) {
/* 128 */       MessageContainer container = getContainer(message.getJMSDestination().toString());
/* 129 */       Set matchingSubscriptions = this.subscriptionContainer.getSubscriptions(message.getJMSActiveMQDestination());
/*     */ 
/*     */ 
/*     */       
/* 133 */       container.addMessage(message);
/* 134 */       if (!matchingSubscriptions.isEmpty()) {
/* 135 */         for (Iterator i = matchingSubscriptions.iterator(); i.hasNext(); ) {
/* 136 */           Subscription sub = i.next();
/* 137 */           if (sub.isTarget(message)) {
/* 138 */             sub.addMessage(container, message);
/*     */           }
/*     */         } 
/* 141 */         updateSendStats(client, message);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void acknowledgeMessage(BrokerClient client, MessageAck ack) throws JMSException {
/* 154 */     if (ack.getDestination().isTopic()) {
/* 155 */       Subscription sub = (Subscription)this.activeSubscriptions.get(ack.getConsumerId());
/* 156 */       if (sub != null) {
/* 157 */         sub.messageConsumed(ack);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void acknowledgeTransactedMessage(BrokerClient client, String transactionId, MessageAck ack) throws JMSException {
/* 164 */     Subscription sub = (Subscription)this.activeSubscriptions.get(ack.getConsumerId());
/* 165 */     if (sub != null) {
/* 166 */       sub.onAcknowledgeTransactedMessageBeforeCommit(ack);
/*     */     }
/*     */   }
/*     */   
/*     */   public void redeliverMessage(BrokerClient client, MessageAck ack) throws JMSException {
/* 171 */     Subscription sub = (Subscription)this.activeSubscriptions.get(ack.getConsumerId());
/* 172 */     if (sub != null)
/*     */     {
/* 174 */       for (Iterator iter = this.messageContainers.values().iterator(); iter.hasNext(); ) {
/* 175 */         MessageContainer container = iter.next();
/* 176 */         if (container.containsMessage(ack.getMessageIdentity())) {
/* 177 */           sub.redeliverMessage(container, ack);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void poll() throws JMSException {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void commitTransaction(BrokerClient client, String transactionId) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rollbackTransaction(BrokerClient client, String transactionId) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MessageContainer createContainer(String destinationName) throws JMSException {
/* 203 */     TopicMessageContainer topicMessageContainer = this.persistenceAdapter.createTopicMessageContainer(destinationName);
/* 204 */     this.destinationMap.put((ActiveMQDestination)new ActiveMQTopic(destinationName), topicMessageContainer);
/* 205 */     return (MessageContainer)topicMessageContainer;
/*     */   }
/*     */   
/*     */   protected Destination createDestination(String destinationName) {
/* 209 */     return (Destination)new ActiveMQTopic(destinationName);
/*     */   }
/*     */   
/*     */   protected void doAddMessageConsumer(BrokerClient client, ConsumerInfo info) throws JMSException {
/* 213 */     boolean shouldRecover = false;
/* 214 */     if (info.getConsumerName() != null && info.getClientId() != null) {
/* 215 */       for (Iterator iterator = this.activeSubscriptions.values().iterator(); iterator.hasNext(); ) {
/* 216 */         Subscription subscription1 = iterator.next();
/* 217 */         if (subscription1.isSameDurableSubscription(info)) {
/* 218 */           throw new DuplicateDurableSubscriptionException(info);
/*     */         }
/*     */       } 
/*     */     }
/* 222 */     Subscription subscription = this.subscriptionContainer.getSubscription(info.getConsumerId());
/* 223 */     if (subscription != null && subscription.isDurableTopic()) {
/*     */       
/* 225 */       if (!subscription.getDestination().equals(subscription.getDestination()) || !subscription.getSelector().equals(info.getSelector())) {
/*     */         
/* 227 */         this.subscriptionContainer.removeSubscription(info.getConsumerId());
/* 228 */         subscription.clear();
/* 229 */         subscription = this.subscriptionContainer.makeSubscription(this.dispatcher, client, info, createFilter(info));
/*     */       } 
/*     */     } else {
/*     */       
/* 233 */       subscription = this.subscriptionContainer.makeSubscription(this.dispatcher, client, info, createFilter(info));
/* 234 */       shouldRecover = true;
/*     */     } 
/* 236 */     subscription.setActiveConsumer(client, info);
/* 237 */     this.activeSubscriptions.put(info.getConsumerId(), subscription);
/* 238 */     this.dispatcher.addActiveSubscription(client, subscription);
/* 239 */     if (subscription.isWildcard()) {
/* 240 */       synchronized (this) {
/* 241 */         if (!this.loadedMessageContainers) {
/* 242 */           loadAllMessageContainers();
/* 243 */           this.loadedMessageContainers = true;
/*     */         }
/*     */       
/*     */       } 
/*     */     } else {
/*     */       
/* 249 */       getContainer(subscription.getDestination().getPhysicalName());
/*     */     } 
/* 251 */     Set containers = this.destinationMap.get(subscription.getDestination());
/* 252 */     for (Iterator iter = containers.iterator(); iter.hasNext(); ) {
/* 253 */       TopicMessageContainer container = iter.next();
/* 254 */       if (container instanceof DurableTopicMessageContainer) {
/* 255 */         ((DurableTopicMessageContainer)container).storeSubscription(info, subscription);
/*     */       }
/*     */     } 
/* 258 */     if (shouldRecover) {
/* 259 */       recoverSubscriptions(subscription);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 265 */     subscription.setActive(true);
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
/*     */   protected void recoverSubscriptions(Subscription subscription) throws JMSException {
/* 277 */     if (subscription.isWildcard()) {
/* 278 */       synchronized (this) {
/* 279 */         if (!this.loadedMessageContainers) {
/* 280 */           loadAllMessageContainers();
/* 281 */           this.loadedMessageContainers = true;
/*     */         }
/*     */       
/*     */       } 
/*     */     } else {
/*     */       
/* 287 */       getContainer(subscription.getDestination().getPhysicalName());
/*     */     } 
/* 289 */     Set containers = this.destinationMap.get(subscription.getDestination());
/* 290 */     for (Iterator iter = containers.iterator(); iter.hasNext(); ) {
/* 291 */       TopicMessageContainer container = iter.next();
/* 292 */       container.recoverSubscription(subscription);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadAllMessageContainers() throws JMSException {
/* 301 */     Map destinations = this.persistenceAdapter.getInitialDestinations();
/* 302 */     if (destinations != null) {
/* 303 */       for (Iterator iter = destinations.entrySet().iterator(); iter.hasNext(); ) {
/* 304 */         Map.Entry entry = iter.next();
/* 305 */         String name = (String)entry.getKey();
/* 306 */         Destination destination = (Destination)entry.getValue();
/* 307 */         loadContainer(name, destination);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Filter createFilter(ConsumerInfo info) throws JMSException {
/*     */     AndFilter andFilter;
/* 320 */     Filter filter = this.filterFactory.createFilter((Destination)info.getDestination(), info.getSelector());
/* 321 */     if (info.isNoLocal()) {
/* 322 */       andFilter = new AndFilter(filter, (Filter)new NoLocalFilter(info.getClientId()));
/*     */     }
/* 324 */     return (Filter)andFilter;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\DurableTopicMessageContainerManager.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */