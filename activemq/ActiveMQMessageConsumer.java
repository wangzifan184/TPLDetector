/*     */ package org.codehaus.activemq;
/*     */ 
/*     */ import javax.jms.Destination;
/*     */ import javax.jms.IllegalStateException;
/*     */ import javax.jms.InvalidDestinationException;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Message;
/*     */ import javax.jms.MessageConsumer;
/*     */ import javax.jms.MessageListener;
/*     */ import javax.management.j2ee.statistics.Stats;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.management.JMSConsumerStatsImpl;
/*     */ import org.codehaus.activemq.management.StatsCapable;
/*     */ import org.codehaus.activemq.message.ActiveMQDestination;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.util.MemoryBoundedQueue;
/*     */ import org.codehaus.activemq.selector.SelectorParser;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ActiveMQMessageConsumer
/*     */   implements MessageConsumer, StatsCapable
/*     */ {
/*  67 */   private static final Log log = LogFactory.getLog(ActiveMQMessageConsumer.class);
/*     */   
/*     */   protected ActiveMQSession session;
/*     */   
/*     */   protected String consumerId;
/*     */   
/*     */   protected MemoryBoundedQueue messageQueue;
/*     */   
/*     */   protected String messageSelector;
/*     */   
/*     */   private MessageListener messageListener;
/*     */   
/*     */   protected String consumerName;
/*     */   
/*     */   protected ActiveMQDestination destination;
/*     */   
/*     */   private boolean closed;
/*     */   
/*     */   protected int consumerNumber;
/*     */   
/*     */   protected int prefetchNumber;
/*     */   
/*     */   protected long startTime;
/*     */   
/*     */   protected boolean noLocal;
/*     */   
/*     */   protected boolean browser;
/*     */   
/*     */   private Thread accessThread;
/*     */   private Object messageListenerGuard;
/*     */   private JMSConsumerStatsImpl stats;
/*     */   
/*     */   protected ActiveMQMessageConsumer(ActiveMQSession theSession, ActiveMQDestination dest, String name, String selector, int cnum, int prefetch, boolean noLocalValue, boolean browserValue) throws JMSException {
/* 100 */     if (dest == null) {
/* 101 */       throw new InvalidDestinationException("Do not understand a null destination");
/*     */     }
/* 103 */     if (dest.isTemporary()) {
/*     */       
/* 105 */       String physicalName = dest.getPhysicalName();
/* 106 */       if (physicalName == null) {
/* 107 */         throw new IllegalArgumentException("Physical name of Destination should be valid: " + dest);
/*     */       }
/* 109 */       String clientID = theSession.connection.getInitializedClientID();
/* 110 */       if (physicalName.indexOf(clientID) < 0) {
/* 111 */         throw new InvalidDestinationException("Cannot use a Temporary destination from another Connection");
/*     */       }
/*     */     } 
/* 114 */     if (selector != null) {
/* 115 */       selector = selector.trim();
/* 116 */       if (selector.length() > 0)
/*     */       {
/* 118 */         (new SelectorParser()).parse(selector);
/*     */       }
/*     */     } 
/* 121 */     this.session = theSession;
/* 122 */     this.destination = dest;
/* 123 */     this.consumerName = name;
/* 124 */     this.messageSelector = selector;
/*     */     
/* 126 */     this.consumerNumber = cnum;
/* 127 */     this.prefetchNumber = prefetch;
/* 128 */     this.noLocal = noLocalValue;
/* 129 */     this.browser = browserValue;
/* 130 */     this.startTime = System.currentTimeMillis();
/* 131 */     this.messageListenerGuard = new Object();
/* 132 */     String queueName = theSession.connection.clientID + ":" + name;
/* 133 */     queueName = queueName + ":" + cnum;
/* 134 */     this.messageQueue = theSession.connection.getMemoryBoundedQueue(queueName);
/* 135 */     this.stats = new JMSConsumerStatsImpl(theSession.getSessionStats(), (Destination)dest);
/* 136 */     this.session.addConsumer(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Stats getStats() {
/* 143 */     return (Stats)this.stats;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JMSConsumerStatsImpl getConsumerStats() {
/* 150 */     return this.stats;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 157 */     return "MessageConsumer: " + this.consumerId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPrefetchNumber() {
/* 164 */     return this.prefetchNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefetchNumber(int prefetchNumber) {
/* 171 */     this.prefetchNumber = prefetchNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessageSelector() throws JMSException {
/* 182 */     checkClosed();
/* 183 */     return this.messageSelector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageListener getMessageListener() throws JMSException {
/* 194 */     checkClosed();
/* 195 */     return this.messageListener;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageListener(MessageListener listener) throws JMSException {
/* 212 */     checkClosed();
/* 213 */     this.messageListener = listener;
/* 214 */     if (listener != null) {
/* 215 */       this.session.setSessionConsumerDispatchState(2);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public Message receive() throws JMSException {
/* 232 */     checkClosed();
/* 233 */     this.session.setSessionConsumerDispatchState(3);
/*     */     try {
/* 235 */       this.accessThread = Thread.currentThread();
/* 236 */       ActiveMQMessage message = (ActiveMQMessage)this.messageQueue.dequeue();
/* 237 */       this.accessThread = null;
/* 238 */       if (message != null) {
/* 239 */         messageDelivered(message, true);
/* 240 */         message = message.shallowCopy();
/*     */       } 
/* 242 */       return (Message)message;
/*     */     }
/* 244 */     catch (InterruptedException ioe) {
/* 245 */       return null;
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
/*     */ 
/*     */   
/*     */   public Message receive(long timeout) throws JMSException {
/* 261 */     checkClosed();
/* 262 */     this.session.setSessionConsumerDispatchState(3);
/*     */     try {
/* 264 */       if (timeout == 0L) {
/* 265 */         return receive();
/*     */       }
/* 267 */       this.accessThread = Thread.currentThread();
/* 268 */       ActiveMQMessage message = (ActiveMQMessage)this.messageQueue.dequeue(timeout);
/* 269 */       this.accessThread = null;
/* 270 */       if (message != null) {
/* 271 */         messageDelivered(message, true);
/* 272 */         message = message.shallowCopy();
/*     */       } 
/* 274 */       return (Message)message;
/*     */     }
/* 276 */     catch (InterruptedException ioe) {
/* 277 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message receiveNoWait() throws JMSException {
/* 288 */     checkClosed();
/* 289 */     this.session.setSessionConsumerDispatchState(3);
/*     */     try {
/* 291 */       ActiveMQMessage message = (ActiveMQMessage)this.messageQueue.dequeueNoWait();
/* 292 */       if (message != null) {
/* 293 */         messageDelivered(message, true);
/* 294 */         return (Message)message.shallowCopy();
/*     */       }
/*     */     
/* 297 */     } catch (InterruptedException ioe) {
/* 298 */       throw new JMSException("Queue is interrupted: " + ioe.getMessage());
/*     */     } 
/* 300 */     return null;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws JMSException {
/*     */     try {
/* 317 */       this.accessThread.interrupt();
/*     */     }
/* 319 */     catch (NullPointerException npe) {
/*     */     
/* 321 */     } catch (SecurityException se) {}
/*     */     
/* 323 */     this.session.removeConsumer(this);
/* 324 */     this.messageQueue.close();
/* 325 */     this.closed = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDurableSubscriber() {
/* 332 */     return (this instanceof ActiveMQTopicSubscriber && this.consumerName != null && this.consumerName.length() > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkClosed() throws IllegalStateException {
/* 339 */     if (this.closed) {
/* 340 */       throw new IllegalStateException("The Consumer is closed");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processMessage(ActiveMQMessage message) {
/* 350 */     message.setConsumerId(this.consumerId);
/* 351 */     MessageListener listener = null;
/* 352 */     synchronized (this.messageListenerGuard) {
/* 353 */       listener = this.messageListener;
/*     */     } 
/*     */     try {
/* 356 */       if (!this.closed) {
/* 357 */         if (listener != null) {
/* 358 */           listener.onMessage((Message)message.shallowCopy());
/* 359 */           messageDelivered(message, true);
/*     */         } else {
/*     */           
/* 362 */           this.messageQueue.enqueue((Packet)message);
/*     */         } 
/*     */       } else {
/*     */         
/* 366 */         messageDelivered(message, false);
/*     */       }
/*     */     
/* 369 */     } catch (Exception e) {
/* 370 */       log.warn("could not process message: " + message, e);
/*     */       
/* 372 */       messageDelivered(message, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getConsumerId() {
/* 382 */     return this.consumerId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setConsumerId(String consumerId) {
/* 389 */     this.consumerId = consumerId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getConsumerName() {
/* 396 */     return this.consumerName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setConsumerName(String value) {
/* 405 */     this.consumerName = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getConsumerNumber() {
/* 412 */     return this.consumerNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setConsumerNumber(int value) {
/* 421 */     this.consumerNumber = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isNoLocal() {
/* 428 */     return this.noLocal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isBrowser() {
/* 437 */     return this.browser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setBrowser(boolean value) {
/* 447 */     this.browser = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ActiveMQDestination getDestination() {
/* 454 */     return this.destination;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long getStartTime() {
/* 461 */     return this.startTime;
/*     */   }
/*     */   
/*     */   protected void clearMessagesInProgress() {
/* 465 */     this.messageQueue.clear();
/*     */   }
/*     */   
/*     */   private void messageDelivered(ActiveMQMessage message, boolean messageRead) {
/* 469 */     boolean read = this.browser ? false : messageRead;
/* 470 */     if (message != null) {
/* 471 */       message.setTransientConsumed((!isDurableSubscriber() && message.getJMSActiveMQDestination().isTopic()));
/* 472 */       this.session.messageDelivered((isDurableSubscriber() || this.destination.isQueue()), message, read);
/* 473 */       if (messageRead)
/* 474 */         this.stats.onMessage((Message)message); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ActiveMQMessageConsumer.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */