/*     */ package org.codehaus.activemq.message;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConsumerInfo
/*     */   extends AbstractPacket
/*     */ {
/*     */   private ActiveMQDestination destination;
/*     */   private String consumerId;
/*     */   private String clientId;
/*     */   private String sessionId;
/*     */   private String consumerName;
/*     */   private String selector;
/*     */   private long startTime;
/*     */   private boolean started;
/*     */   private int consumerNo;
/*     */   private boolean noLocal;
/*     */   private boolean browser;
/*  37 */   private int prefetchNumber = 100;
/*     */ 
/*     */   
/*     */   private transient String consumerKey;
/*     */ 
/*     */   
/*     */   public String getConsumerId() {
/*  44 */     return this.consumerId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConsumerId(String consumerId) {
/*  51 */     this.consumerId = consumerId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSessionId() {
/*  58 */     return this.sessionId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSessionId(String sessionId) {
/*  65 */     this.sessionId = sessionId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPacketType() {
/*  74 */     return 17;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  84 */     boolean result = false;
/*  85 */     if (obj != null && obj instanceof ConsumerInfo) {
/*  86 */       ConsumerInfo that = (ConsumerInfo)obj;
/*  87 */       result = this.consumerId.equals(that.consumerId);
/*     */     } 
/*  89 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  96 */     return this.consumerId.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 104 */     return super.toString() + " consumerId: " + this.consumerId + " clientId: " + this.clientId + " consumerName: " + this.consumerName + " destination: " + this.destination;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClientId() {
/* 111 */     return this.clientId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClientId(String newClientId) {
/* 118 */     this.clientId = newClientId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQDestination getDestination() {
/* 125 */     return this.destination;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestination(ActiveMQDestination newDestination) {
/* 132 */     this.destination = newDestination;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSelector() {
/* 139 */     return this.selector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSelector(String newSelector) {
/* 146 */     this.selector = newSelector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStarted() {
/* 153 */     return this.started;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStarted(boolean flag) {
/* 160 */     this.started = flag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getStartTime() {
/* 167 */     return this.startTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStartTime(long newStartTime) {
/* 174 */     this.startTime = newStartTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getConsumerNo() {
/* 181 */     return this.consumerNo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConsumerNo(int newConsumerNo) {
/* 188 */     this.consumerNo = newConsumerNo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getConsumerName() {
/* 195 */     return this.consumerName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConsumerName(String newconsumerName) {
/* 202 */     this.consumerName = newconsumerName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDurableTopic() {
/* 209 */     return (this.destination.isTopic() && !this.destination.isTemporary() && this.consumerName != null && this.consumerName.length() > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNoLocal() {
/* 217 */     return this.noLocal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNoLocal(boolean noLocal) {
/* 224 */     this.noLocal = noLocal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBrowser() {
/* 231 */     return this.browser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBrowser(boolean browser) {
/* 238 */     this.browser = browser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPrefetchNumber() {
/* 245 */     return this.prefetchNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefetchNumber(int prefetchNumber) {
/* 252 */     this.prefetchNumber = prefetchNumber;
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
/*     */   public String getConsumerKey() {
/* 264 */     if (this.consumerKey == null) {
/* 265 */       this.consumerKey = generateConsumerKey(this.clientId, this.consumerName);
/*     */     }
/* 267 */     return this.consumerKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String generateConsumerKey(String clientId, String consumerName) {
/* 277 */     return "[" + clientId + ":" + consumerName + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ConsumerInfo.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */