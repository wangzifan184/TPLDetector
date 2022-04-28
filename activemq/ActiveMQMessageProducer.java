/*     */ package org.codehaus.activemq;
/*     */ 
/*     */ import javax.jms.Destination;
/*     */ import javax.jms.IllegalStateException;
/*     */ import javax.jms.InvalidDestinationException;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Message;
/*     */ import javax.jms.MessageProducer;
/*     */ import javax.management.j2ee.statistics.Stats;
/*     */ import org.codehaus.activemq.management.JMSProducerStatsImpl;
/*     */ import org.codehaus.activemq.management.StatsCapable;
/*     */ import org.codehaus.activemq.message.ActiveMQDestination;
/*     */ import org.codehaus.activemq.util.IdGenerator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ActiveMQMessageProducer
/*     */   implements MessageProducer, StatsCapable
/*     */ {
/*     */   protected ActiveMQSession session;
/*     */   protected String producerId;
/*     */   private IdGenerator idGenerator;
/*     */   protected boolean closed;
/*     */   private boolean disableMessageID;
/*     */   private boolean disableMessageTimestamp;
/*     */   private int defaultDeliveryMode;
/*     */   private int defaultPriority;
/*     */   private long defaultTimeToLive;
/*     */   protected ActiveMQDestination defaultDestination;
/*     */   private long startTime;
/*     */   private JMSProducerStatsImpl stats;
/*     */   private boolean reuseMessageId;
/*     */   
/*     */   protected ActiveMQMessageProducer(ActiveMQSession theSession, ActiveMQDestination destination) throws JMSException {
/*  84 */     this.session = theSession;
/*  85 */     this.defaultDestination = destination;
/*  86 */     this.idGenerator = new IdGenerator();
/*  87 */     this.disableMessageID = false;
/*  88 */     this.disableMessageTimestamp = false;
/*  89 */     this.defaultDeliveryMode = 2;
/*  90 */     this.defaultPriority = 4;
/*  91 */     this.defaultTimeToLive = 0L;
/*  92 */     this.startTime = System.currentTimeMillis();
/*  93 */     this.session.addProducer(this);
/*  94 */     this.stats = new JMSProducerStatsImpl(theSession.getSessionStats(), (Destination)destination);
/*     */   }
/*     */   
/*     */   public Stats getStats() {
/*  98 */     return (Stats)this.stats;
/*     */   }
/*     */   
/*     */   public JMSProducerStatsImpl getProducerStats() {
/* 102 */     return this.stats;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDisableMessageID(boolean value) throws JMSException {
/* 125 */     checkClosed();
/* 126 */     this.disableMessageID = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getDisableMessageID() throws JMSException {
/* 137 */     checkClosed();
/* 138 */     return this.disableMessageID;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDisableMessageTimestamp(boolean value) throws JMSException {
/* 161 */     checkClosed();
/* 162 */     this.disableMessageTimestamp = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getDisableMessageTimestamp() throws JMSException {
/* 173 */     checkClosed();
/* 174 */     return this.disableMessageTimestamp;
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
/*     */ 
/*     */   
/*     */   public void setDeliveryMode(int newDeliveryMode) throws JMSException {
/* 193 */     if (newDeliveryMode != 2 && newDeliveryMode != 1) {
/* 194 */       throw new IllegalStateException("unkown delivery mode: " + newDeliveryMode);
/*     */     }
/* 196 */     checkClosed();
/* 197 */     this.defaultDeliveryMode = newDeliveryMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDeliveryMode() throws JMSException {
/* 208 */     checkClosed();
/* 209 */     return this.defaultDeliveryMode;
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
/*     */ 
/*     */   
/*     */   public void setPriority(int newDefaultPriority) throws JMSException {
/* 228 */     if (newDefaultPriority < 0 || newDefaultPriority > 9) {
/* 229 */       throw new IllegalStateException("default priority must be a value between 0 and 9");
/*     */     }
/* 231 */     checkClosed();
/* 232 */     this.defaultPriority = newDefaultPriority;
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
/*     */   public int getPriority() throws JMSException {
/* 244 */     checkClosed();
/* 245 */     return this.defaultPriority;
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
/*     */   public void setTimeToLive(long timeToLive) throws JMSException {
/* 261 */     if (timeToLive < 0L) {
/* 262 */       throw new IllegalStateException("cannot set a negative timeToLive");
/*     */     }
/* 264 */     checkClosed();
/* 265 */     this.defaultTimeToLive = timeToLive;
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
/*     */   public long getTimeToLive() throws JMSException {
/* 278 */     checkClosed();
/* 279 */     return this.defaultTimeToLive;
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
/*     */   public Destination getDestination() throws JMSException {
/* 291 */     checkClosed();
/* 292 */     return (Destination)this.defaultDestination;
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
/*     */   public void close() throws JMSException {
/* 307 */     this.session.removeProducer(this);
/* 308 */     this.closed = true;
/*     */   }
/*     */   
/*     */   protected void checkClosed() throws IllegalStateException {
/* 312 */     if (this.closed) {
/* 313 */       throw new IllegalStateException("The producer is closed");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void send(Message message) throws JMSException {
/* 336 */     send((Destination)this.defaultDestination, message, this.defaultDeliveryMode, this.defaultPriority, this.defaultTimeToLive);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void send(Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
/* 361 */     send((Destination)this.defaultDestination, message, deliveryMode, priority, timeToLive);
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
/*     */   public void send(Destination destination, Message message) throws JMSException {
/* 389 */     send(destination, message, this.defaultDeliveryMode, this.defaultPriority, this.defaultTimeToLive);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
/* 415 */     checkClosed();
/* 416 */     if (destination == null) {
/* 417 */       throw new InvalidDestinationException("Dont understand null destinations");
/*     */     }
/* 419 */     this.session.send(this, destination, message, deliveryMode, priority, timeToLive, this.reuseMessageId);
/* 420 */     this.stats.onMessage(message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isResuseMessageId() {
/* 427 */     return this.reuseMessageId;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReuseMessageId(boolean reuseMessageId) {
/* 433 */     this.reuseMessageId = reuseMessageId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getProducerId() {
/* 441 */     return this.producerId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setProducerId(String producerId) {
/* 448 */     this.producerId = producerId;
/*     */   }
/*     */   
/*     */   protected long getStartTime() {
/* 452 */     return this.startTime;
/*     */   }
/*     */   
/*     */   protected IdGenerator getIdGenerator() {
/* 456 */     return this.idGenerator;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ActiveMQMessageProducer.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */