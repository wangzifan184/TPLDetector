/*     */ package org.codehaus.activemq.util;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IdGenerator
/*     */ {
/*  32 */   private static final Log log = LogFactory.getLog(IdGenerator.class);
/*     */   private static final String UNIQUE_STUB;
/*     */   private static int instanceCount;
/*     */   private static String hostName;
/*     */   private String seed;
/*     */   private long sequence;
/*     */   
/*     */   static {
/*  40 */     String stub = "";
/*  41 */     boolean canAccessSystemProps = true;
/*     */     try {
/*  43 */       SecurityManager sm = System.getSecurityManager();
/*  44 */       if (sm != null) {
/*  45 */         sm.checkPropertiesAccess();
/*     */       }
/*     */     }
/*  48 */     catch (SecurityException se) {
/*  49 */       canAccessSystemProps = false;
/*     */     } 
/*  51 */     if (canAccessSystemProps) {
/*     */       try {
/*  53 */         hostName = InetAddress.getLocalHost().getHostName();
/*  54 */         ServerSocket ss = new ServerSocket(0);
/*  55 */         stub = "ID:" + hostName + "-" + ss.getLocalPort() + "-" + System.currentTimeMillis() + "-";
/*  56 */         Thread.sleep(100L);
/*  57 */         ss.close();
/*     */       }
/*  59 */       catch (Exception ioe) {
/*  60 */         log.warn("could not generate unique stub", ioe);
/*     */       } 
/*     */     } else {
/*     */       
/*  64 */       hostName = "localhost";
/*  65 */       stub = "ID:" + hostName + "-1-" + System.currentTimeMillis() + "-";
/*     */     } 
/*  67 */     UNIQUE_STUB = stub;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getHostName() {
/*  76 */     return hostName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IdGenerator() {
/*  83 */     synchronized (UNIQUE_STUB) {
/*  84 */       this.seed = UNIQUE_STUB + instanceCount++ + ":";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String generateId() {
/*  94 */     return this.seed + this.sequence++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSeed() {
/* 101 */     return this.seed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getSeedFromId(String id) {
/* 111 */     String result = id;
/* 112 */     if (id != null) {
/* 113 */       int index = id.lastIndexOf(':');
/* 114 */       if (index > 0 && index + 1 < id.length()) {
/* 115 */         result = id.substring(0, index + 1);
/*     */       }
/*     */     } 
/* 118 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getCountFromId(String id) {
/* 128 */     long result = -1L;
/* 129 */     if (id != null) {
/* 130 */       int index = id.lastIndexOf(':');
/*     */       
/* 132 */       if (index > 0 && index + 1 < id.length()) {
/* 133 */         String numStr = id.substring(index + 1, id.length());
/* 134 */         result = Long.parseLong(numStr);
/*     */       } 
/*     */     } 
/* 137 */     return result;
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
/*     */   public static int compare(String id1, String id2) {
/* 149 */     int result = -1;
/* 150 */     String seed1 = getSeedFromId(id1);
/* 151 */     String seed2 = getSeedFromId(id2);
/* 152 */     if (seed1 != null && seed2 != null) {
/* 153 */       result = seed1.compareTo(seed2);
/* 154 */       if (result == 0) {
/* 155 */         long count1 = getCountFromId(id1);
/* 156 */         long count2 = getCountFromId(id2);
/* 157 */         result = (int)(count1 - count2);
/*     */       } 
/*     */     } 
/* 160 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activem\\util\IdGenerator.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */