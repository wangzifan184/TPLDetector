/*    */ package org.codehaus.activemq.store.jdbm;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.jms.JMSException;
/*    */ import javax.transaction.xa.XAException;
/*    */ import jdbm.btree.BTree;
/*    */ import jdbm.helper.Tuple;
/*    */ import jdbm.helper.TupleBrowser;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.codehaus.activemq.message.ActiveMQXid;
/*    */ import org.codehaus.activemq.service.Transaction;
/*    */ import org.codehaus.activemq.service.TransactionManager;
/*    */ import org.codehaus.activemq.store.PreparedTransactionStore;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JdbmPreparedTransactionStore
/*    */   implements PreparedTransactionStore
/*    */ {
/* 40 */   private static final Log log = LogFactory.getLog(JdbmPreparedTransactionStore.class);
/*    */   
/*    */   private BTree database;
/*    */   
/*    */   public JdbmPreparedTransactionStore(BTree database) {
/* 45 */     this.database = database;
/*    */   }
/*    */   
/*    */   public ActiveMQXid[] getXids() throws XAException {
/*    */     try {
/* 50 */       List list = new ArrayList();
/* 51 */       Tuple tuple = new Tuple();
/* 52 */       TupleBrowser iter = this.database.browse();
/* 53 */       while (iter.getNext(tuple)) {
/* 54 */         list.add(tuple.getKey());
/*    */       }
/* 56 */       ActiveMQXid[] answer = new ActiveMQXid[list.size()];
/* 57 */       list.toArray(answer);
/* 58 */       return answer;
/*    */     }
/* 60 */     catch (IOException e) {
/* 61 */       throw new XAException("Failed to recover Xids. Reason: " + e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void remove(ActiveMQXid xid) throws XAException {
/*    */     try {
/* 67 */       this.database.remove(xid);
/*    */     }
/* 69 */     catch (IOException e) {
/* 70 */       throw new XAException("Failed to remove: " + xid + ". Reason: " + e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void put(ActiveMQXid xid, Transaction transaction) throws XAException {
/*    */     try {
/* 76 */       this.database.insert(xid, transaction, true);
/*    */     }
/* 78 */     catch (IOException e) {
/* 79 */       throw new XAException("Failed to add: " + xid + " for transaction: " + transaction + ". Reason: " + e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void loadPreparedTransactions(TransactionManager transactionManager) throws XAException {
/* 84 */     log.info("Recovering prepared transactions");
/*    */     
/*    */     try {
/* 87 */       Tuple tuple = new Tuple();
/* 88 */       TupleBrowser iter = this.database.browse();
/* 89 */       while (iter.getNext(tuple)) {
/* 90 */         ActiveMQXid xid = (ActiveMQXid)tuple.getKey();
/* 91 */         Transaction transaction = (Transaction)tuple.getValue();
/* 92 */         transactionManager.loadTransaction(xid, transaction);
/*    */       }
/*    */     
/* 95 */     } catch (IOException e) {
/* 96 */       log.error("Failed to recover prepared transactions: " + e, e);
/* 97 */       throw new XAException("Failed to recover prepared transactions. Reason: " + e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void start() throws JMSException {}
/*    */   
/*    */   public void stop() throws JMSException {}
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\jdbm\JdbmPreparedTransactionStore.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */