/*     */ package org.codehaus.activemq.store.bdb;
/*     */ 
/*     */ import com.sleepycat.je.Cursor;
/*     */ import com.sleepycat.je.CursorConfig;
/*     */ import com.sleepycat.je.Database;
/*     */ import com.sleepycat.je.DatabaseEntry;
/*     */ import com.sleepycat.je.DatabaseException;
/*     */ import com.sleepycat.je.LockMode;
/*     */ import com.sleepycat.je.OperationStatus;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.jms.JMSException;
/*     */ import javax.transaction.xa.XAException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.ActiveMQXid;
/*     */ import org.codehaus.activemq.service.Transaction;
/*     */ import org.codehaus.activemq.service.TransactionManager;
/*     */ import org.codehaus.activemq.service.impl.XATransactionCommand;
/*     */ import org.codehaus.activemq.store.PreparedTransactionStore;
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
/*     */ public class BDbPreparedTransactionStore
/*     */   implements PreparedTransactionStore
/*     */ {
/*  45 */   private static final Log log = LogFactory.getLog(BDbMessageStore.class);
/*     */   
/*     */   private Database database;
/*     */   private CursorConfig cursorConfig;
/*     */   
/*     */   public BDbPreparedTransactionStore(Database database) {
/*  51 */     this.database = database;
/*     */   }
/*     */   
/*     */   public ActiveMQXid[] getXids() throws XAException {
/*  55 */     checkClosed();
/*  56 */     Cursor cursor = null;
/*     */     try {
/*  58 */       cursor = this.database.openCursor(BDbHelper.getTransaction(), this.cursorConfig);
/*  59 */       List list = new ArrayList();
/*  60 */       DatabaseEntry keyEntry = new DatabaseEntry();
/*  61 */       DatabaseEntry valueEntry = new DatabaseEntry();
/*  62 */       OperationStatus status = cursor.getFirst(keyEntry, valueEntry, LockMode.DEFAULT);
/*  63 */       while (status == OperationStatus.SUCCESS) {
/*  64 */         list.add(extractXid(keyEntry));
/*     */       }
/*     */       
/*  67 */       if (status != OperationStatus.NOTFOUND) {
/*  68 */         log.warn("Unexpected status code while recovering: " + status);
/*     */       }
/*  70 */       ActiveMQXid[] answer = new ActiveMQXid[list.size()];
/*  71 */       list.toArray(answer);
/*  72 */       return answer;
/*     */     }
/*  74 */     catch (DatabaseException e) {
/*  75 */       log.error("Failed to recover prepared transaction log: " + e, (Throwable)e);
/*  76 */       throw new XAException("Failed to recover prepared transaction log. Reason: " + e);
/*     */     }
/*  78 */     catch (IOException e) {
/*  79 */       log.error("Failed to recover prepared transaction log: " + e, e);
/*  80 */       throw new XAException("Failed to recover prepared transaction log. Reason: " + e);
/*     */     } finally {
/*     */       
/*  83 */       if (cursor != null) {
/*     */         try {
/*  85 */           cursor.close();
/*     */         }
/*  87 */         catch (DatabaseException e) {
/*  88 */           log.warn("Caught exception closing cursor: " + e, (Throwable)e);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void remove(ActiveMQXid xid) throws XAException {
/*  95 */     checkClosed();
/*     */     try {
/*  97 */       DatabaseEntry key = new DatabaseEntry(asBytes(xid));
/*  98 */       OperationStatus status = this.database.delete(BDbHelper.getTransaction(), key);
/*  99 */       if (status != OperationStatus.SUCCESS) {
/* 100 */         log.error("Could not delete sequenece number for: " + xid + " status: " + status);
/*     */       }
/*     */     }
/* 103 */     catch (DatabaseException e) {
/* 104 */       throw new XAException("Failed to remove prepared transaction: " + xid + ". Reason: " + e);
/*     */     }
/* 106 */     catch (IOException e) {
/* 107 */       throw new XAException("Failed to remove prepared transaction: " + xid + ". Reason: " + e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void put(ActiveMQXid xid, Transaction transaction) throws XAException {
/* 112 */     checkClosed();
/*     */     try {
/* 114 */       DatabaseEntry key = new DatabaseEntry(asBytes(xid));
/* 115 */       DatabaseEntry value = new DatabaseEntry(asBytes(transaction));
/* 116 */       this.database.put(BDbHelper.getTransaction(), key, value);
/*     */     }
/* 118 */     catch (Exception e) {
/* 119 */       throw new XAException("Failed to store prepared transaction: " + xid + ". Reason: " + e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void loadPreparedTransactions(TransactionManager transactionManager) throws XAException {
/* 124 */     checkClosed();
/* 125 */     Cursor cursor = null;
/*     */     try {
/* 127 */       cursor = this.database.openCursor(BDbHelper.getTransaction(), this.cursorConfig);
/* 128 */       DatabaseEntry keyEntry = new DatabaseEntry();
/* 129 */       DatabaseEntry valueEntry = new DatabaseEntry();
/* 130 */       OperationStatus status = cursor.getFirst(keyEntry, valueEntry, LockMode.DEFAULT);
/* 131 */       while (status == OperationStatus.SUCCESS) {
/* 132 */         ActiveMQXid xid = extractXid(keyEntry);
/* 133 */         Transaction transaction = extractTransaction(valueEntry);
/* 134 */         transactionManager.loadTransaction(xid, transaction);
/* 135 */         status = cursor.getNext(keyEntry, valueEntry, LockMode.DEFAULT);
/*     */       } 
/* 137 */       if (status != OperationStatus.NOTFOUND) {
/* 138 */         log.warn("Unexpected status code while recovering: " + status);
/*     */       }
/*     */     }
/* 141 */     catch (Exception e) {
/* 142 */       log.error("Failed to recover prepared transaction log: " + e, e);
/* 143 */       throw new XAException("Failed to recover prepared transaction log. Reason: " + e);
/*     */     } finally {
/*     */       
/* 146 */       if (cursor != null) {
/*     */         try {
/* 148 */           cursor.close();
/*     */         }
/* 150 */         catch (DatabaseException e) {
/* 151 */           log.warn("Caught exception closing cursor: " + e, (Throwable)e);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() throws JMSException {}
/*     */   
/*     */   public synchronized void stop() throws JMSException {
/* 161 */     if (this.database != null) {
/* 162 */       JMSException exception = BDbPersistenceAdapter.closeDatabase(this.database, null);
/* 163 */       this.database = null;
/* 164 */       if (exception != null) {
/* 165 */         throw exception;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ActiveMQXid extractXid(DatabaseEntry entry) throws IOException {
/* 173 */     return ActiveMQXid.fromBytes(entry.getData());
/*     */   }
/*     */   
/*     */   protected Transaction extractTransaction(DatabaseEntry entry) throws IOException, ClassNotFoundException {
/* 177 */     return XATransactionCommand.fromBytes(entry.getData());
/*     */   }
/*     */ 
/*     */   
/*     */   private byte[] asBytes(ActiveMQXid xid) throws IOException {
/* 182 */     return xid.toBytes();
/*     */   }
/*     */   
/*     */   private byte[] asBytes(Transaction transaction) throws IOException, JMSException {
/* 186 */     if (transaction instanceof XATransactionCommand) {
/* 187 */       XATransactionCommand packetTask = (XATransactionCommand)transaction;
/* 188 */       return packetTask.toBytes();
/*     */     } 
/*     */     
/* 191 */     throw new IOException("Unsupported transaction type: " + transaction);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void checkClosed() throws XAException {
/* 196 */     if (this.database == null)
/* 197 */       throw new XAException("Prepared Transaction Store is already closed"); 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\bdb\BDbPreparedTransactionStore.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */