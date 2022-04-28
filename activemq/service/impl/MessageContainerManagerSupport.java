/*     */ package org.codehaus.activemq.service.impl;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.jms.Destination;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Message;
/*     */ import org.codehaus.activemq.broker.BrokerClient;
/*     */ import org.codehaus.activemq.message.ActiveMQDestination;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.service.Dispatcher;
/*     */ import org.codehaus.activemq.service.MessageContainer;
/*     */ import org.codehaus.activemq.service.MessageContainerManager;
/*     */ import org.codehaus.activemq.service.Subscription;
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
/*     */ public abstract class MessageContainerManagerSupport
/*     */   implements MessageContainerManager
/*     */ {
/*     */   protected Dispatcher dispatcher;
/*  40 */   protected Map messageContainers = (Map)new ConcurrentHashMap();
/*  41 */   private Map destinations = (Map)new ConcurrentHashMap();
/*     */   private boolean maintainDestinationStats = true;
/*     */   
/*     */   public MessageContainerManagerSupport(Dispatcher dispatcher) {
/*  45 */     this.dispatcher = dispatcher;
/*  46 */     dispatcher.register(this);
/*     */   }
/*     */   
/*     */   public Map getDestinations() {
/*  50 */     return Collections.unmodifiableMap(this.destinations);
/*     */   }
/*     */   
/*     */   public void start() throws JMSException {
/*  54 */     this.dispatcher.start();
/*     */   }
/*     */   
/*     */   public void stop() throws JMSException {
/*  58 */     this.dispatcher.stop();
/*  59 */     JMSException firstException = null;
/*     */     try {
/*  61 */       this.dispatcher.stop();
/*     */     }
/*  63 */     catch (JMSException e) {
/*  64 */       firstException = e;
/*     */     } 
/*     */ 
/*     */     
/*  68 */     for (Iterator iter = this.messageContainers.values().iterator(); iter.hasNext(); ) {
/*  69 */       MessageContainer container = iter.next();
/*     */       try {
/*  71 */         container.stop();
/*     */       }
/*  73 */       catch (JMSException e) {
/*  74 */         if (firstException == null) {
/*  75 */           firstException = e;
/*     */         }
/*     */       } 
/*     */     } 
/*  79 */     if (firstException != null) {
/*  80 */       throw firstException;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized MessageContainer getContainer(String destinationName) throws JMSException {
/*  86 */     MessageContainer container = (MessageContainer)this.messageContainers.get(destinationName);
/*  87 */     if (container == null) {
/*  88 */       container = createContainer(destinationName);
/*  89 */       container.start();
/*  90 */       this.messageContainers.put(destinationName, container);
/*     */       
/*  92 */       this.destinations.put(destinationName, createDestination(destinationName));
/*     */     } 
/*  94 */     return container;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMaintainDestinationStats() {
/* 101 */     return this.maintainDestinationStats;
/*     */   }
/*     */   
/*     */   public void setMaintainDestinationStats(boolean maintainDestinationStats) {
/* 105 */     this.maintainDestinationStats = maintainDestinationStats;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Destination createDestination(String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract MessageContainer createContainer(String paramString) throws JMSException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadContainer(String destinationName, Destination destination) throws JMSException {
/* 125 */     this.destinations.put(destinationName, destination);
/*     */     
/* 127 */     MessageContainer container = createContainer(destinationName);
/* 128 */     container.start();
/* 129 */     this.messageContainers.put(destinationName, container);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateAcknowledgeStats(BrokerClient client, Subscription subscription) {
/* 139 */     if (isMaintainDestinationStats()) {
/*     */       
/* 141 */       String name = subscription.getDestination().getPhysicalName();
/* 142 */       ActiveMQDestination destination = (ActiveMQDestination)this.destinations.get(name);
/* 143 */       destination.getStats().onMessageAck();
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
/*     */   protected void updateSendStats(BrokerClient client, ActiveMQMessage message) throws JMSException {
/* 155 */     if (isMaintainDestinationStats()) {
/*     */       
/* 157 */       String name = message.getJMSActiveMQDestination().getPhysicalName();
/* 158 */       ActiveMQDestination destination = (ActiveMQDestination)this.destinations.get(name);
/* 159 */       if (destination != null)
/* 160 */         destination.getStats().onMessageSend((Message)message); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\MessageContainerManagerSupport.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */