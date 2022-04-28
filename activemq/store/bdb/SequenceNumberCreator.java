/*     */ package org.codehaus.activemq.store.bdb;
/*     */ 
/*     */ import com.sleepycat.je.CursorConfig;
/*     */ import com.sleepycat.je.DatabaseEntry;
/*     */ import com.sleepycat.je.DatabaseException;
/*     */ import com.sleepycat.je.LockMode;
/*     */ import com.sleepycat.je.OperationStatus;
/*     */ import com.sleepycat.je.SecondaryCursor;
/*     */ import com.sleepycat.je.SecondaryDatabase;
/*     */ import com.sleepycat.je.SecondaryKeyCreator;
/*     */ import javax.jms.JMSException;
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
/*     */ public class SequenceNumberCreator
/*     */   implements SecondaryKeyCreator
/*     */ {
/*  37 */   private static final Log log = LogFactory.getLog(SequenceNumberCreator.class);
/*     */   
/*  39 */   private long counter = 1L;
/*  40 */   private ThreadLocal lastKeyStore = new ThreadLocal();
/*  41 */   private ThreadLocal deleteKeyStore = new ThreadLocal();
/*     */ 
/*     */   
/*     */   public synchronized void initialise(SecondaryDatabase database) throws JMSException, DatabaseException {
/*  45 */     this.counter = queryLatestKeyInDatabase(database);
/*     */   }
/*     */   
/*     */   public boolean createSecondaryKey(SecondaryDatabase secondaryDatabase, DatabaseEntry keyEntry, DatabaseEntry valueEntry, DatabaseEntry resultEntry) throws DatabaseException {
/*  49 */     DatabaseEntry nextKey = this.deleteKeyStore.get();
/*  50 */     if (nextKey != null) {
/*  51 */       resultEntry.setData(nextKey.getData());
/*  52 */       this.deleteKeyStore.set(null);
/*     */     } else {
/*     */       
/*  55 */       long value = 1L;
/*  56 */       synchronized (this) {
/*  57 */         value = ++this.counter;
/*     */       } 
/*     */       
/*  60 */       resultEntry.setData(BDbHelper.asBytes(value));
/*     */     } 
/*  62 */     this.lastKeyStore.set(resultEntry);
/*  63 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DatabaseEntry getLastKey() {
/*  70 */     return this.lastKeyStore.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDeleteKey(DatabaseEntry nextKey) {
/*  79 */     this.deleteKeyStore.set(nextKey);
/*     */   }
/*     */   
/*     */   protected long queryLatestKeyInDatabase(SecondaryDatabase database) throws JMSException, DatabaseException {
/*  83 */     CursorConfig cursorConfig = null;
/*  84 */     SecondaryCursor cursor = null;
/*     */     try {
/*  86 */       cursor = database.openSecondaryCursor(BDbHelper.getTransaction(), cursorConfig);
/*  87 */       DatabaseEntry sequenceNumberEntry = new DatabaseEntry();
/*  88 */       DatabaseEntry keyEntry = new DatabaseEntry();
/*  89 */       DatabaseEntry valueEntry = new DatabaseEntry();
/*  90 */       OperationStatus status = cursor.getLast(sequenceNumberEntry, keyEntry, valueEntry, LockMode.DEFAULT);
/*  91 */       long answer = 1L;
/*  92 */       if (status != OperationStatus.NOTFOUND) {
/*  93 */         if (status == OperationStatus.SUCCESS) {
/*  94 */           answer = extractLong(sequenceNumberEntry);
/*     */         } else {
/*     */           
/*  97 */           throw new JMSException("Invalid status code: " + status + " cannot read last sequence number");
/*     */         } 
/*     */       }
/* 100 */       return answer;
/*     */     } finally {
/*     */       
/* 103 */       if (cursor != null) {
/*     */         try {
/* 105 */           cursor.close();
/*     */         }
/* 107 */         catch (DatabaseException e) {
/* 108 */           log.warn("Error closing cursor: " + e, (Throwable)e);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected long extractLong(DatabaseEntry entry) {
/* 115 */     return BDbHelper.longFromBytes(entry.getData());
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\bdb\SequenceNumberCreator.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */