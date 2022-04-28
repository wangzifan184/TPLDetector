/*     */ package org.codehaus.activemq.transport;
/*     */ 
/*     */ import java.net.URI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TransportStatusEvent
/*     */ {
/*     */   public static final int CONNECTED = 1;
/*     */   public static final int DISCONNECTED = 2;
/*     */   public static final int RECONNECTED = 3;
/*     */   public static final int FAILED = 4;
/*     */   public static final int STOPPED = 5;
/*     */   private URI remoteURI;
/*     */   private int channelStatus;
/*     */   
/*     */   public String toString() {
/*  63 */     return "channel: " + this.remoteURI + " has " + getStatusAsString(this.channelStatus);
/*     */   }
/*     */   
/*     */   private String getStatusAsString(int status) {
/*  67 */     String result = null;
/*  68 */     switch (status)
/*     */     { case 1:
/*  70 */         result = "connected";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  87 */         return result;case 2: result = "disconnected"; return result;case 3: result = "reconnected"; return result;case 4: result = "failed"; return result;case 5: result = "stopped"; return result; }  result = "unknown"; return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getChannelStatus() {
/*  94 */     return this.channelStatus;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChannelStatus(int channelStatus) {
/* 101 */     this.channelStatus = channelStatus;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getRemoteURI() {
/* 108 */     return this.remoteURI;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoteURI(URI remoteURI) {
/* 115 */     this.remoteURI = remoteURI;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\TransportStatusEvent.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */