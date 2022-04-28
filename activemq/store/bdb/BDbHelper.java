/*     */ package org.codehaus.activemq.store.bdb;
/*     */ 
/*     */ import com.sleepycat.je.DatabaseConfig;
/*     */ import com.sleepycat.je.DatabaseException;
/*     */ import com.sleepycat.je.Environment;
/*     */ import com.sleepycat.je.EnvironmentConfig;
/*     */ import com.sleepycat.je.Transaction;
/*     */ import java.io.File;
/*     */ import java.util.LinkedList;
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
/*     */ public class BDbHelper
/*     */ {
/*  37 */   private static final Log log = LogFactory.getLog(BDbHelper.class);
/*  38 */   private static ThreadLocal threadLocalTxn = new ThreadLocal();
/*     */   
/*     */   public static Environment createEnvironment(File dir) throws DatabaseException {
/*  41 */     dir.mkdirs();
/*  42 */     EnvironmentConfig envConfig = new EnvironmentConfig();
/*  43 */     envConfig.setAllowCreate(true);
/*  44 */     envConfig.setTransactional(true);
/*  45 */     return new Environment(dir, envConfig);
/*     */   }
/*     */   
/*     */   public static DatabaseConfig createDatabaseConfig() {
/*  49 */     DatabaseConfig config = new DatabaseConfig();
/*  50 */     config.setTransactional(true);
/*  51 */     config.setAllowCreate(true);
/*  52 */     return config;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Transaction getTransaction() {
/*  60 */     LinkedList list = threadLocalTxn.get();
/*  61 */     if (list != null && !list.isEmpty()) {
/*  62 */       return list.getFirst();
/*     */     }
/*  64 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Transaction popTransaction() {
/*  71 */     LinkedList list = threadLocalTxn.get();
/*  72 */     if (list == null || list.isEmpty()) {
/*  73 */       log.warn("Attempt to pop transaction when no transaction in progress");
/*  74 */       return null;
/*     */     } 
/*     */     
/*  77 */     return list.removeFirst();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void pushTransaction(Transaction transaction) {
/*  85 */     LinkedList list = threadLocalTxn.get();
/*  86 */     if (list == null) {
/*  87 */       list = new LinkedList();
/*  88 */       threadLocalTxn.set(list);
/*     */     } 
/*  90 */     list.addLast(transaction);
/*     */   }
/*     */   
/*     */   public static int getTransactionCount() {
/*  94 */     LinkedList list = threadLocalTxn.get();
/*  95 */     if (list != null) {
/*  96 */       return list.size();
/*     */     }
/*  98 */     return 0;
/*     */   }
/*     */   
/*     */   public static byte[] asBytes(long v) {
/* 102 */     byte[] data = new byte[8];
/* 103 */     data[0] = (byte)(int)(v >>> 56L);
/* 104 */     data[1] = (byte)(int)(v >>> 48L);
/* 105 */     data[2] = (byte)(int)(v >>> 40L);
/* 106 */     data[3] = (byte)(int)(v >>> 32L);
/* 107 */     data[4] = (byte)(int)(v >>> 24L);
/* 108 */     data[5] = (byte)(int)(v >>> 16L);
/* 109 */     data[6] = (byte)(int)(v >>> 8L);
/* 110 */     data[7] = (byte)(int)(v >>> 0L);
/* 111 */     return data;
/*     */   }
/*     */   
/*     */   public static byte[] asBytes(Long key) {
/* 115 */     long v = key.longValue();
/* 116 */     return asBytes(v);
/*     */   }
/*     */   
/*     */   public static long longFromBytes(byte[] data) {
/* 120 */     return (data[0] << 56L) + ((data[1] & 0xFF) << 48L) + ((data[2] & 0xFF) << 40L) + ((data[3] & 0xFF) << 32L) + ((data[4] & 0xFF) << 24L) + ((data[5] & 0xFF) << 16) + ((data[6] & 0xFF) << 8) + ((data[7] & 0xFF) << 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\bdb\BDbHelper.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */