/*     */ package org.codehaus.activemq.transport;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedInt;
/*     */ import javax.jms.Destination;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Message;
/*     */ import javax.jms.MessageConsumer;
/*     */ import javax.jms.MessageListener;
/*     */ import javax.jms.Session;
/*     */ import javax.jms.Topic;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.ActiveMQMessageProducer;
/*     */ import org.codehaus.activemq.message.ActiveMQDestination;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.service.Service;
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
/*     */ class NetworkMessageBridge
/*     */   implements Service, MessageListener
/*     */ {
/*  51 */   private static final Log log = LogFactory.getLog(NetworkMessageBridge.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   private SynchronizedInt referenceCount = new SynchronizedInt(0);
/*     */   
/*     */   private ActiveMQDestination destination;
/*     */   
/*     */   private boolean durableTopic;
/*     */   private boolean stopped;
/*     */   private Session remoteSession;
/*     */   
/*     */   public void setDurableTopic(boolean durableTopic) {
/*  66 */     this.durableTopic = durableTopic;
/*     */   }
/*     */   private Session localSession; private MessageConsumer remoteConsumer;
/*     */   private ActiveMQMessageProducer localProducer;
/*     */   private String localBrokerName;
/*     */   
/*     */   public boolean isDurableTopic() {
/*  73 */     return this.durableTopic;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQDestination getDestination() {
/*  80 */     return this.destination;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestination(ActiveMQDestination destination) {
/*  87 */     this.destination = destination;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLocalBrokerName() {
/*  94 */     return this.localBrokerName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocalBrokerName(String localBrokerName) {
/* 101 */     this.localBrokerName = localBrokerName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Session getLocalSession() {
/* 108 */     return this.localSession;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocalSession(Session localSession) {
/* 115 */     this.localSession = localSession;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Session getRemoteSession() {
/* 122 */     return this.remoteSession;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoteSession(Session remoteSession) {
/* 129 */     this.remoteSession = remoteSession;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int incrementReferenceCount() {
/* 138 */     return this.referenceCount.increment();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int decrementReferenceCount() {
/* 147 */     return this.referenceCount.decrement();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() throws JMSException {
/* 156 */     this.localProducer = (ActiveMQMessageProducer)this.localSession.createProducer((Destination)this.destination);
/* 157 */     this.localProducer.setReuseMessageId(true);
/* 158 */     if (isDurableTopic()) {
/* 159 */       String subsName = this.destination.toString() + "@" + this.localBrokerName;
/* 160 */       this.remoteConsumer = (MessageConsumer)this.remoteSession.createDurableSubscriber((Topic)this.destination, subsName);
/*     */     } else {
/*     */       
/* 163 */       this.remoteConsumer = this.remoteSession.createConsumer((Destination)this.destination);
/*     */     } 
/* 165 */     this.remoteConsumer.setMessageListener(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 172 */     if (!this.stopped) {
/* 173 */       this.stopped = true;
/* 174 */       this.referenceCount.set(0);
/*     */       try {
/* 176 */         this.localSession.close();
/* 177 */         this.remoteSession.close();
/*     */       }
/* 179 */       catch (JMSException jmsEx) {
/* 180 */         log.warn("failure in stopping the message bridge", (Throwable)jmsEx);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMessage(Message msg) {
/*     */     try {
/* 190 */       if (!this.stopped) {
/* 191 */         ActiveMQMessage message = (ActiveMQMessage)msg;
/* 192 */         if (message != null) {
/* 193 */           message = message.shallowCopy();
/* 194 */           message.addBrokerVisited(this.localBrokerName);
/* 195 */           Destination destination = message.getJMSDestination();
/* 196 */           int deliveryMode = message.getJMSDeliveryMode();
/* 197 */           int priority = message.getJMSPriority();
/* 198 */           long timeToLive = message.getJMSExpiration() - msg.getJMSTimestamp();
/* 199 */           this.localProducer.send(destination, (Message)message, deliveryMode, priority, timeToLive);
/*     */           
/* 201 */           msg.acknowledge();
/*     */         }
/*     */       
/*     */       } 
/* 205 */     } catch (JMSException jmsEx) {
/* 206 */       log.error("NetworkMessageConsumer failed", (Throwable)jmsEx);
/* 207 */       stop();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 215 */     return this.destination.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 223 */     boolean result = false;
/* 224 */     if (obj != null && obj instanceof NetworkMessageBridge) {
/* 225 */       NetworkMessageBridge other = (NetworkMessageBridge)obj;
/* 226 */       result = (this.destination.equals(other.destination) && isDurableTopic() == other.isDurableTopic());
/*     */     } 
/* 228 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\NetworkMessageBridge.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */