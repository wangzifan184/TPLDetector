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
/*     */ public class SessionInfo
/*     */   extends AbstractPacket
/*     */ {
/*     */   private String clientId;
/*     */   private String sessionId;
/*     */   private long startTime;
/*     */   private boolean started;
/*     */   
/*     */   public int getPacketType() {
/*  39 */     return 23;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  50 */     boolean result = false;
/*  51 */     if (obj != null && obj instanceof SessionInfo) {
/*  52 */       SessionInfo info = (SessionInfo)obj;
/*  53 */       result = (this.sessionId == info.sessionId);
/*     */     } 
/*  55 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  62 */     return (this.sessionId != null) ? this.sessionId.hashCode() : super.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSessionId() {
/*  70 */     return this.sessionId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSessionId(String sessionId) {
/*  77 */     this.sessionId = sessionId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClientId() {
/*  85 */     return this.clientId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClientId(String newClientId) {
/*  92 */     this.clientId = newClientId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStarted() {
/* 100 */     return this.started;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStarted(boolean flag) {
/* 107 */     this.started = flag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getStartTime() {
/* 114 */     return this.startTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStartTime(long newStartTime) {
/* 121 */     this.startTime = newStartTime;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\SessionInfo.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */