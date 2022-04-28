/*     */ package org.codehaus.activemq.store.bdbn;
/*     */ 
/*     */ import com.sleepycat.bdb.CurrentTransaction;
/*     */ import com.sleepycat.db.Db;
/*     */ import com.sleepycat.db.DbEnv;
/*     */ import com.sleepycat.db.DbException;
/*     */ import com.sleepycat.db.DbTxn;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.util.LinkedList;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.util.JMSExceptionHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*  40 */   private static final Log log = LogFactory.getLog(BDbHelper.class);
/*  41 */   private static ThreadLocal threadLocalTxn = new ThreadLocal();
/*  42 */   public static final int TRANSACTION_FLAGS = Db.DB_TXN_SYNC;
/*     */   private static DbEnv cachedEnvironment;
/*     */   
/*     */   public static DbEnv createEnvironment(File dir, boolean runRecovery) throws DbException, FileNotFoundException {
/*  46 */     DbEnv env = new DbEnv(0);
/*     */ 
/*     */     
/*  49 */     int envFlags = Db.DB_INIT_TXN | Db.DB_INIT_LOCK | Db.DB_INIT_LOG | Db.DB_INIT_MPOOL | Db.DB_CREATE;
/*     */ 
/*     */     
/*  52 */     if (runRecovery) {
/*  53 */       envFlags |= Db.DB_RECOVER;
/*     */     }
/*  55 */     env.open(dir.getPath(), envFlags, 0);
/*  56 */     return env;
/*     */   }
/*     */   
/*     */   public static Db open(DbEnv environment, String name, boolean isQueue) throws FileNotFoundException, DbException, JMSException {
/*  60 */     int flags = Db.DB_CREATE;
/*  61 */     Db db = new Db(environment, 0);
/*     */     
/*  63 */     if (isQueue) {
/*  64 */       db.setFlags(Db.DB_RENUMBER);
/*     */     }
/*     */     
/*  67 */     int type = 1;
/*  68 */     if (isQueue) {
/*  69 */       type = 3;
/*     */     }
/*  71 */     String databaseName = null;
/*  72 */     DbTxn transaction = createTransaction(environment);
/*     */     try {
/*  74 */       db.open(transaction, name, databaseName, type, flags, 0);
/*  75 */       transaction = commitTransaction(transaction);
/*     */     } finally {
/*     */       
/*  78 */       rollbackTransaction(transaction);
/*     */     } 
/*  80 */     return db;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DbTxn getTransaction() {
/*  89 */     LinkedList list = threadLocalTxn.get();
/*  90 */     if (list != null && !list.isEmpty()) {
/*  91 */       return list.getFirst();
/*     */     }
/*  93 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DbTxn popTransaction() {
/* 100 */     LinkedList list = threadLocalTxn.get();
/* 101 */     if (list == null || list.isEmpty()) {
/* 102 */       log.warn("Attempt to pop transaction when no transaction in progress");
/* 103 */       return null;
/*     */     } 
/*     */     
/* 106 */     return list.removeFirst();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void pushTransaction(DbTxn transaction) {
/* 114 */     LinkedList list = threadLocalTxn.get();
/* 115 */     if (list == null) {
/* 116 */       list = new LinkedList();
/* 117 */       threadLocalTxn.set(list);
/*     */     } 
/* 119 */     list.addLast(transaction);
/*     */   }
/*     */   
/*     */   public static int getTransactionCount() {
/* 123 */     LinkedList list = threadLocalTxn.get();
/* 124 */     if (list != null) {
/* 125 */       return list.size();
/*     */     }
/* 127 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static DbTxn createTransaction(DbEnv environment) throws DbException {
/* 133 */     cachedEnvironment = environment;
/* 134 */     CurrentTransaction currentTxn = CurrentTransaction.getInstance(environment);
/* 135 */     return currentTxn.beginTxn();
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
/*     */   public static DbTxn commitTransaction(DbTxn transaction) throws JMSException {
/*     */     try {
/* 161 */       CurrentTransaction currentTxn = CurrentTransaction.getInstance(cachedEnvironment);
/* 162 */       currentTxn.commitTxn();
/* 163 */       return null;
/*     */     }
/* 165 */     catch (DbException e) {
/* 166 */       throw JMSExceptionHelper.newJMSException("Failed to commit transaction: " + transaction + " in container: " + e, e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void rollbackTransaction(DbTxn transaction) {
/* 197 */     if (transaction != null)
/*     */       try {
/* 199 */         CurrentTransaction currentTxn = CurrentTransaction.getInstance(cachedEnvironment);
/* 200 */         currentTxn.abortTxn();
/*     */       }
/* 202 */       catch (DbException e) {
/* 203 */         log.warn("Cannot rollback transaction due to: " + e, (Throwable)e);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\bdbn\BDbHelper.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */