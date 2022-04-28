/*     */ package org.codehaus.activemq.message;
/*     */ 
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConnectionInfo
/*     */   extends AbstractPacket
/*     */ {
/*     */   String clientId;
/*     */   String userName;
/*     */   String password;
/*     */   String hostName;
/*     */   String clientVersion;
/*     */   int wireFormatVersion;
/*     */   long startTime;
/*     */   boolean started;
/*     */   boolean closed;
/*     */   Properties properties;
/*     */   
/*     */   public int getPacketType() {
/*  49 */     return 22;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  59 */     boolean result = false;
/*  60 */     if (obj != null && obj instanceof ConnectionInfo) {
/*  61 */       ConnectionInfo info = (ConnectionInfo)obj;
/*  62 */       result = (this.clientId == info.clientId);
/*     */     } 
/*  64 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  71 */     return (this.clientId != null) ? this.clientId.hashCode() : super.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClientId() {
/*  79 */     return this.clientId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClientId(String newClientId) {
/*  86 */     this.clientId = newClientId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHostName() {
/*  93 */     return this.hostName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHostName(String newHostName) {
/* 100 */     this.hostName = newHostName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPassword() {
/* 107 */     return this.password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPassword(String newPassword) {
/* 114 */     this.password = newPassword;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Properties getProperties() {
/* 121 */     return this.properties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperties(Properties newProperties) {
/* 128 */     this.properties = newProperties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getStartTime() {
/* 135 */     return this.startTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStartTime(long newStartTime) {
/* 142 */     this.startTime = newStartTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUserName() {
/* 149 */     return this.userName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUserName(String newUserName) {
/* 156 */     this.userName = newUserName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStarted() {
/* 163 */     return this.started;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStarted(boolean started) {
/* 170 */     this.started = started;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClosed() {
/* 177 */     return this.closed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClosed(boolean closed) {
/* 184 */     this.closed = closed;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClientVersion() {
/* 190 */     return this.clientVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClientVersion(String clientVersion) {
/* 196 */     this.clientVersion = clientVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getWireFormatVersion() {
/* 202 */     return this.wireFormatVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWireFormatVersion(int wireFormatVersion) {
/* 208 */     this.wireFormatVersion = wireFormatVersion;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ConnectionInfo.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */