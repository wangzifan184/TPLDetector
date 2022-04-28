/*     */ package org.codehaus.activemq;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import javax.jms.ConnectionMetaData;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ActiveMQConnectionMetaData
/*     */   implements ConnectionMetaData
/*     */ {
/*     */   public String getJMSVersion() {
/*  39 */     return "1.1";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getJMSMajorVersion() {
/*  49 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getJMSMinorVersion() {
/*  59 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getJMSProviderName() {
/*  69 */     return "Protique";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getProviderVersion() {
/*  79 */     return "1.1";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getProviderMajorVersion() {
/*  89 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getProviderMinorVersion() {
/*  99 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration getJMSXPropertyNames() {
/* 109 */     Hashtable jmxProperties = new Hashtable();
/* 110 */     jmxProperties.put("JMSXGroupID", "1");
/* 111 */     jmxProperties.put("JMSXGroupSeq", "1");
/*     */     
/* 113 */     return jmxProperties.keys();
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ActiveMQConnectionMetaData.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */