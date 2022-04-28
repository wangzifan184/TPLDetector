/*     */ package org.codehaus.activemq.store.jdbc;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.jms.JMSException;
/*     */ import javax.transaction.xa.XAException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.ActiveMQXid;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.service.Transaction;
/*     */ import org.codehaus.activemq.service.TransactionManager;
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
/*     */ 
/*     */ public class JDBCPreparedTransactionStore
/*     */   implements PreparedTransactionStore
/*     */ {
/*  40 */   private static final Log log = LogFactory.getLog(JDBCMessageStore.class);
/*     */   
/*     */   private final WireFormat wireFormat;
/*     */   private final JDBCAdapter adapter;
/*     */   private final JDBCPersistenceAdapter persistenceAdapter;
/*     */   
/*     */   public JDBCPreparedTransactionStore(JDBCPersistenceAdapter persistenceAdapter, JDBCAdapter adapter, WireFormat wireFormat) {
/*  47 */     this.persistenceAdapter = persistenceAdapter;
/*  48 */     this.adapter = adapter;
/*  49 */     this.wireFormat = wireFormat;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQXid[] getXids() throws XAException {
/*  55 */     List list = new ArrayList();
/*     */     
/*  57 */     Connection c = null;
/*     */     try {
/*  59 */       c = this.persistenceAdapter.getConnection();
/*  60 */       this.adapter.doGetXids(c, list);
/*  61 */     } catch (SQLException e) {
/*  62 */       log.error("Failed to recover prepared transaction log: " + e, e);
/*  63 */       throw new XAException("Failed to recover container. Reason: " + e);
/*     */     } finally {
/*  65 */       this.persistenceAdapter.returnConnection(c);
/*     */     } 
/*     */     
/*  68 */     ActiveMQXid[] answer = new ActiveMQXid[list.size()];
/*  69 */     list.toArray((Object[])answer);
/*  70 */     return answer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(ActiveMQXid xid) throws XAException {
/*  76 */     Connection c = null;
/*     */     try {
/*  78 */       c = this.persistenceAdapter.getConnection();
/*  79 */       this.adapter.doRemoveXid(c, xid);
/*  80 */     } catch (SQLException e) {
/*  81 */       throw new XAException("Failed to remove prepared transaction: " + xid + ". Reason: " + e);
/*     */     } finally {
/*  83 */       this.persistenceAdapter.returnConnection(c);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(ActiveMQXid xid, Transaction transaction) throws XAException {
/*     */     byte[] data;
/*  91 */     String id = xid.toLocalTransactionId();
/*     */     
/*     */     try {
/*  94 */       data = xid.toBytes();
/*  95 */     } catch (Exception e) {
/*  96 */       throw new XAException("Failed to store prepared transaction: " + xid + ". Reason: " + e);
/*     */     } 
/*     */ 
/*     */     
/* 100 */     Connection c = null;
/*     */     try {
/* 102 */       c = this.persistenceAdapter.getConnection();
/* 103 */       this.adapter.doAddXid(c, xid, data);
/* 104 */     } catch (SQLException e) {
/* 105 */       throw new XAException("Failed to store prepared transaction: " + xid + ". Reason: " + e);
/*     */     } finally {
/* 107 */       this.persistenceAdapter.returnConnection(c);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadPreparedTransactions(TransactionManager transactionManager) throws XAException {
/* 113 */     Connection c = null;
/*     */     try {
/* 115 */       c = this.persistenceAdapter.getConnection();
/* 116 */       this.adapter.doLoadPreparedTransactions(c, transactionManager);
/* 117 */     } catch (SQLException e) {
/* 118 */       log.error("Failed to recover prepared transaction log: " + e, e);
/* 119 */       throw new XAException("Failed to recover prepared transaction log. Reason: " + e);
/*     */     } finally {
/* 121 */       this.persistenceAdapter.returnConnection(c);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void start() throws JMSException {}
/*     */   
/*     */   public synchronized void stop() throws JMSException {}
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\jdbc\JDBCPreparedTransactionStore.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */