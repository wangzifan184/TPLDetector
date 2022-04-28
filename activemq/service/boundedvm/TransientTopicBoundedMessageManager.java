/*     */ package org.codehaus.activemq.service.boundedvm;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;
/*     */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.jms.Destination;
/*     */ import javax.jms.JMSException;
/*     */ import org.codehaus.activemq.broker.BrokerClient;
/*     */ import org.codehaus.activemq.filter.AndFilter;
/*     */ import org.codehaus.activemq.filter.DestinationMap;
/*     */ import org.codehaus.activemq.filter.Filter;
/*     */ import org.codehaus.activemq.filter.FilterFactory;
/*     */ import org.codehaus.activemq.filter.FilterFactoryImpl;
/*     */ import org.codehaus.activemq.filter.NoLocalFilter;
/*     */ import org.codehaus.activemq.message.ActiveMQDestination;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.ConsumerInfo;
/*     */ import org.codehaus.activemq.message.MessageAck;
/*     */ import org.codehaus.activemq.message.util.BoundedPacketQueue;
/*     */ import org.codehaus.activemq.message.util.MemoryBoundedQueue;
/*     */ import org.codehaus.activemq.message.util.MemoryBoundedQueueManager;
/*     */ import org.codehaus.activemq.service.MessageContainer;
/*     */ import org.codehaus.activemq.service.MessageContainerManager;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TransientTopicBoundedMessageManager
/*     */   implements MessageContainerManager
/*     */ {
/*     */   private MemoryBoundedQueueManager queueManager;
/*     */   private ConcurrentHashMap containers;
/*     */   private DestinationMap destinationMap;
/*     */   private FilterFactory filterFactory;
/*     */   private SynchronizedBoolean started;
/*     */   private Map destinations;
/*     */   
/*     */   public TransientTopicBoundedMessageManager(MemoryBoundedQueueManager mgr) {
/*  67 */     this.queueManager = mgr;
/*  68 */     this.containers = new ConcurrentHashMap();
/*  69 */     this.destinationMap = new DestinationMap();
/*  70 */     this.destinations = (Map)new ConcurrentHashMap();
/*  71 */     this.filterFactory = (FilterFactory)new FilterFactoryImpl();
/*  72 */     this.started = new SynchronizedBoolean(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() throws JMSException {
/*  81 */     if (this.started.commit(false, true)) {
/*  82 */       for (Iterator i = this.containers.values().iterator(); i.hasNext(); ) {
/*  83 */         TransientTopicBoundedMessageContainer container = i.next();
/*  84 */         container.start();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() throws JMSException {
/*  95 */     if (this.started.commit(true, false)) {
/*  96 */       for (Iterator i = this.containers.values().iterator(); i.hasNext(); ) {
/*  97 */         TransientTopicBoundedMessageContainer container = i.next();
/*  98 */         container.stop();
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
/*     */   public synchronized void addMessageConsumer(BrokerClient client, ConsumerInfo info) throws JMSException {
/* 111 */     ActiveMQDestination destination = info.getDestination();
/* 112 */     if (destination.isTopic()) {
/* 113 */       TransientTopicBoundedMessageContainer container = (TransientTopicBoundedMessageContainer)this.containers.get(client);
/*     */       
/* 115 */       if (container == null) {
/* 116 */         MemoryBoundedQueue queue = this.queueManager.getMemoryBoundedQueue(client.toString());
/* 117 */         container = new TransientTopicBoundedMessageContainer(client, (BoundedPacketQueue)queue);
/* 118 */         this.containers.put(client, container);
/* 119 */         if (this.started.get()) {
/* 120 */           container.start();
/*     */         }
/*     */       } 
/* 123 */       container.addConsumer(createFilter(info), info);
/* 124 */       this.destinationMap.put(destination, container);
/* 125 */       String name = destination.getPhysicalName();
/* 126 */       if (!this.destinations.containsKey(name)) {
/* 127 */         this.destinations.put(name, destination);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void removeMessageConsumer(BrokerClient client, ConsumerInfo info) throws JMSException {
/* 138 */     ActiveMQDestination destination = info.getDestination();
/* 139 */     if (destination.isTopic()) {
/* 140 */       TransientTopicBoundedMessageContainer container = (TransientTopicBoundedMessageContainer)this.containers.get(client);
/*     */       
/* 142 */       if (container != null) {
/* 143 */         container.removeConsumer(info);
/* 144 */         if (container.isInactive()) {
/* 145 */           this.containers.remove(client);
/* 146 */           container.close();
/* 147 */           this.destinationMap.remove(destination, container);
/*     */         } 
/*     */ 
/*     */         
/* 151 */         if (!hasConsumerFor(destination)) {
/* 152 */           this.destinations.remove(destination.getPhysicalName());
/*     */         }
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
/*     */   public void deleteSubscription(String clientId, String subscriberName) throws JMSException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendMessage(BrokerClient client, ActiveMQMessage message) throws JMSException {
/* 174 */     if (message != null && message.getJMSActiveMQDestination().isTopic()) {
/*     */       
/* 176 */       Set set = this.destinationMap.get(message.getJMSActiveMQDestination());
/* 177 */       for (Iterator i = set.iterator(); i.hasNext(); ) {
/* 178 */         TransientTopicBoundedMessageContainer container = i.next();
/* 179 */         container.targetAndDispatch(client, message);
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
/*     */   
/*     */   public void acknowledgeMessage(BrokerClient client, MessageAck ack) throws JMSException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void acknowledgeTransactedMessage(BrokerClient client, String transactionId, MessageAck ack) throws JMSException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void redeliverMessage(BrokerClient client, MessageAck ack) throws JMSException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void poll() throws JMSException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void commitTransaction(BrokerClient client, String transactionId) throws JMSException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rollbackTransaction(BrokerClient client, String transactionId) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageContainer getContainer(String physicalName) throws JMSException {
/* 249 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map getDestinations() {
/* 256 */     return Collections.unmodifiableMap(this.destinations);
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
/* 267 */     Filter filter = this.filterFactory.createFilter((Destination)info.getDestination(), info.getSelector());
/* 268 */     if (info.isNoLocal()) {
/* 269 */       andFilter = new AndFilter(filter, (Filter)new NoLocalFilter(info.getClientId()));
/*     */     }
/* 271 */     return (Filter)andFilter;
/*     */   }
/*     */   
/*     */   protected boolean hasConsumerFor(ActiveMQDestination destination) {
/* 275 */     for (Iterator i = this.containers.values().iterator(); i.hasNext(); ) {
/* 276 */       TransientTopicBoundedMessageContainer container = i.next();
/* 277 */       if (container.hasConsumerFor(destination)) {
/* 278 */         return true;
/*     */       }
/*     */     } 
/* 281 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\boundedvm\TransientTopicBoundedMessageManager.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */