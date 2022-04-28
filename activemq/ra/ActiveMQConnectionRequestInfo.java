/*     */ package org.codehaus.activemq.ra;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import javax.resource.spi.ConnectionRequestInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ActiveMQConnectionRequestInfo
/*     */   implements ConnectionRequestInfo, Serializable, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = -5754338187296859149L;
/*     */   String userName;
/*     */   String password;
/*     */   String serverUrl;
/*     */   String clientid;
/*     */   boolean xa;
/*     */   
/*     */   public ActiveMQConnectionRequestInfo copy() {
/*     */     try {
/*  40 */       return (ActiveMQConnectionRequestInfo)clone();
/*     */     }
/*  42 */     catch (CloneNotSupportedException e) {
/*  43 */       throw new RuntimeException("Could not clone: ", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  52 */     int rc = 0;
/*  53 */     if (this.userName != null) {
/*  54 */       rc ^= this.userName.hashCode();
/*     */     }
/*  56 */     if (this.password != null) {
/*  57 */       rc ^= this.password.hashCode();
/*     */     }
/*  59 */     if (this.serverUrl != null) {
/*  60 */       rc ^= this.serverUrl.hashCode();
/*     */     }
/*  62 */     if (this.clientid != null) {
/*  63 */       rc ^= this.clientid.hashCode();
/*     */     }
/*  65 */     if (this.xa) {
/*  66 */       rc ^= 0xABCDEF;
/*     */     }
/*  68 */     return rc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  76 */     if (o == null) {
/*  77 */       return false;
/*     */     }
/*  79 */     if (!getClass().equals(o.getClass())) {
/*  80 */       return false;
/*     */     }
/*  82 */     ActiveMQConnectionRequestInfo i = (ActiveMQConnectionRequestInfo)o;
/*  83 */     if ((((this.userName == null) ? 1 : 0) ^ ((i.userName == null) ? 1 : 0)) != 0) {
/*  84 */       return false;
/*     */     }
/*  86 */     if (this.userName != null && !this.userName.equals(i.userName)) {
/*  87 */       return false;
/*     */     }
/*  89 */     if ((((this.password == null) ? 1 : 0) ^ ((i.password == null) ? 1 : 0)) != 0) {
/*  90 */       return false;
/*     */     }
/*  92 */     if (this.password != null && !this.password.equals(i.password)) {
/*  93 */       return false;
/*     */     }
/*  95 */     if ((((this.serverUrl == null) ? 1 : 0) ^ ((i.serverUrl == null) ? 1 : 0)) != 0) {
/*  96 */       return false;
/*     */     }
/*  98 */     if (this.serverUrl != null && !this.serverUrl.equals(i.serverUrl)) {
/*  99 */       return false;
/*     */     }
/* 101 */     if ((((this.clientid == null) ? 1 : 0) ^ ((i.clientid == null) ? 1 : 0)) != 0) {
/* 102 */       return false;
/*     */     }
/* 104 */     if (this.clientid != null && !this.clientid.equals(i.clientid)) {
/* 105 */       return false;
/*     */     }
/* 107 */     if (this.xa != i.xa) {
/* 108 */       return false;
/*     */     }
/* 110 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getServerUrl() {
/* 117 */     return this.serverUrl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServerUrl(String url) {
/* 124 */     this.serverUrl = url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPassword() {
/* 131 */     return this.password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPassword(String password) {
/* 138 */     this.password = password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUserName() {
/* 145 */     return this.userName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUserName(String userid) {
/* 152 */     this.userName = userid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClientid() {
/* 159 */     return this.clientid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClientid(String clientid) {
/* 166 */     this.clientid = clientid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isXa() {
/* 173 */     return this.xa;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setXa(boolean xa) {
/* 180 */     this.xa = xa;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ra\ActiveMQConnectionRequestInfo.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */