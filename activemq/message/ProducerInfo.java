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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProducerInfo
/*     */   extends AbstractPacket
/*     */ {
/*     */   private ActiveMQDestination destination;
/*     */   private String producerId;
/*     */   private String clientId;
/*     */   private String sessionId;
/*     */   private long startTime;
/*     */   private boolean started;
/*     */   
/*     */   public boolean equals(Object obj) {
/*  41 */     boolean result = false;
/*  42 */     if (obj != null && obj instanceof ProducerInfo) {
/*  43 */       ProducerInfo info = (ProducerInfo)obj;
/*  44 */       result = (this.producerId == info.producerId);
/*     */     } 
/*  46 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  53 */     return (this.producerId != null) ? this.producerId.hashCode() : super.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getProducerId() {
/*  61 */     return this.producerId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProducerId(String producerId) {
/*  68 */     this.producerId = producerId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSessionId() {
/*  75 */     return this.sessionId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSessionId(String sessionId) {
/*  82 */     this.sessionId = sessionId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPacketType() {
/*  92 */     return 18;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClientId() {
/* 100 */     return this.clientId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClientId(String newClientId) {
/* 107 */     this.clientId = newClientId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQDestination getDestination() {
/* 115 */     return this.destination;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestination(ActiveMQDestination newDestination) {
/* 122 */     this.destination = newDestination;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStarted() {
/* 130 */     return this.started;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStarted(boolean flag) {
/* 137 */     this.started = flag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getStartTime() {
/* 144 */     return this.startTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStartTime(long newStartTime) {
/* 151 */     this.startTime = newStartTime;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ProducerInfo.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */