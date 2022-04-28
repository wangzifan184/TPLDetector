/*     */ package org.codehaus.activemq.service.impl;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.jms.Destination;
/*     */ import javax.jms.JMSException;
/*     */ import org.codehaus.activemq.broker.BrokerClient;
/*     */ import org.codehaus.activemq.filter.DestinationFilter;
/*     */ import org.codehaus.activemq.message.ActiveMQDestination;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.ConsumerInfo;
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
/*     */ public class InitialImageMessageContainerManager
/*     */   extends ProxyMessageContainerManager
/*     */ {
/*     */   private Map cache;
/*     */   private boolean topic;
/*     */   private DestinationFilter destinationFilter;
/*     */   
/*     */   public InitialImageMessageContainerManager(MessageContainerManager delegate, Map cache, DestinationFilter destinationFilter) {
/*  60 */     this(delegate, cache, true, destinationFilter);
/*     */   }
/*     */   
/*     */   public InitialImageMessageContainerManager(MessageContainerManager delegate, Map cache, boolean topic, DestinationFilter destinationFilter) {
/*  64 */     super(delegate);
/*  65 */     this.cache = cache;
/*  66 */     this.topic = topic;
/*  67 */     this.destinationFilter = destinationFilter;
/*     */   }
/*     */   
/*     */   public void addMessageConsumer(BrokerClient client, ConsumerInfo info) throws JMSException {
/*  71 */     super.addMessageConsumer(client, info);
/*     */ 
/*     */     
/*  74 */     ActiveMQDestination destination = info.getDestination();
/*  75 */     if (isValid(destination)) {
/*  76 */       if (destination.isWildcard()) {
/*  77 */         DestinationFilter filter = DestinationFilter.parseFilter((Destination)destination);
/*  78 */         sendMatchingInitialImages(client, info, filter);
/*     */       } else {
/*     */         
/*  81 */         ActiveMQMessage message = null;
/*  82 */         synchronized (this.cache) {
/*  83 */           message = (ActiveMQMessage)this.cache.get(destination);
/*     */         } 
/*  85 */         if (message != null) {
/*  86 */           sendMessage(client, message);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public void sendMessage(BrokerClient client, ActiveMQMessage message) throws JMSException {
/*  93 */     ActiveMQDestination destination = message.getJMSActiveMQDestination();
/*  94 */     if (isValid(destination)) {
/*  95 */       this.cache.put(destination, message);
/*     */     }
/*  97 */     super.sendMessage(client, message);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sendMatchingInitialImages(BrokerClient client, ConsumerInfo info, DestinationFilter filter) throws JMSException {
/* 103 */     synchronized (this.cache) {
/* 104 */       for (Iterator iter = this.cache.entrySet().iterator(); iter.hasNext(); ) {
/* 105 */         Map.Entry entry = iter.next();
/* 106 */         Destination destination = (Destination)entry.getKey();
/* 107 */         if (filter.matches(destination)) {
/* 108 */           ActiveMQMessage message = (ActiveMQMessage)entry.getValue();
/* 109 */           sendMessage(client, message);
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
/*     */ 
/*     */   
/*     */   protected boolean isValid(ActiveMQDestination destination) {
/* 125 */     return (destination.isTopic() == this.topic && (this.destinationFilter == null || this.destinationFilter.matches((Destination)destination)));
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\InitialImageMessageContainerManager.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */