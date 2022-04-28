/*     */ package org.codehaus.activemq.service.impl;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.jms.JMSException;
/*     */ import javax.transaction.xa.XAException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.broker.Broker;
/*     */ import org.codehaus.activemq.broker.BrokerClient;
/*     */ import org.codehaus.activemq.message.ActiveMQXid;
/*     */ import org.codehaus.activemq.service.Transaction;
/*     */ import org.codehaus.activemq.service.TransactionManager;
/*     */ import org.codehaus.activemq.store.PreparedTransactionStore;
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
/*     */ public class TransactionManagerImpl
/*     */   implements TransactionManager
/*     */ {
/*  41 */   private static final Log log = LogFactory.getLog(TransactionManagerImpl.class);
/*     */ 
/*     */   
/*     */   private Broker broker;
/*     */   
/*     */   private PreparedTransactionStore preparedTransactions;
/*     */   
/*  48 */   private Map activeClients = (Map)new ConcurrentHashMap();
/*     */   
/*  50 */   private Map localTxs = (Map)new ConcurrentHashMap();
/*     */   
/*  52 */   private Map xaTxs = (Map)new ConcurrentHashMap();
/*     */   
/*  54 */   private final ThreadLocal contextTx = new ThreadLocal();
/*     */   
/*     */   public TransactionManagerImpl(Broker broker, PreparedTransactionStore preparedTransactions) {
/*  57 */     this.preparedTransactions = preparedTransactions;
/*  58 */     this.broker = broker;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Transaction createLocalTransaction(BrokerClient client, String txid) throws JMSException {
/*  65 */     AbstractTransaction t = new LocalTransactionCommand(this.broker, this.localTxs, txid);
/*  66 */     this.localTxs.put(txid, t);
/*  67 */     return t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Transaction createXATransaction(BrokerClient client, ActiveMQXid xid) throws XAException {
/*  74 */     AbstractTransaction t = new XATransactionCommand(this.broker, xid, this.xaTxs, this.preparedTransactions);
/*  75 */     this.xaTxs.put(xid, t);
/*  76 */     return t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Transaction getLocalTransaction(String txid) throws JMSException {
/*  83 */     Transaction tx = (Transaction)this.localTxs.get(txid);
/*  84 */     if (tx == null) {
/*  85 */       throw new JMSException("Transaction '" + txid + "' has not been started.");
/*     */     }
/*     */     
/*  88 */     return tx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Transaction getXATransaction(ActiveMQXid xid) throws XAException {
/*  95 */     Transaction tx = (Transaction)this.xaTxs.get(xid);
/*  96 */     if (tx == null) {
/*  97 */       XAException e = new XAException("Transaction '" + xid + "' has not been started.");
/*  98 */       e.errorCode = -4;
/*  99 */       throw e;
/*     */     } 
/* 101 */     return tx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQXid[] getPreparedXATransactions() throws XAException {
/* 108 */     return this.preparedTransactions.getXids();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContexTransaction(Transaction tx) {
/* 115 */     this.contextTx.set(tx);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Transaction getContexTransaction() {
/* 122 */     return this.contextTx.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cleanUpClient(BrokerClient client) throws JMSException {
/* 132 */     List list = (List)this.activeClients.remove(client);
/* 133 */     if (list != null) {
/* 134 */       for (int i = 0; i < list.size(); i++) {
/*     */         try {
/* 136 */           Object o = list.get(i);
/* 137 */           if (o instanceof String) {
/* 138 */             Transaction t = getLocalTransaction((String)o);
/* 139 */             t.rollback();
/*     */           } else {
/*     */             
/* 142 */             Transaction t = getXATransaction((ActiveMQXid)o);
/* 143 */             t.rollback();
/*     */           }
/*     */         
/* 146 */         } catch (Exception e) {
/* 147 */           log.warn("ERROR Rolling back disconnected client's transactions: ", e);
/*     */         } 
/*     */       } 
/* 150 */       list.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadTransaction(ActiveMQXid xid, Transaction transaction) throws XAException {
/* 157 */     if (transaction instanceof XATransactionCommand) {
/* 158 */       XATransactionCommand xaTransaction = (XATransactionCommand)transaction;
/* 159 */       xaTransaction.initialise(this.xaTxs, this.preparedTransactions);
/*     */     } 
/* 161 */     transaction.setBroker(this.broker);
/*     */     
/* 163 */     this.xaTxs.put(xid, transaction);
/*     */   }
/*     */   
/*     */   public void start() throws JMSException {
/* 167 */     this.preparedTransactions.start();
/*     */     try {
/* 169 */       this.preparedTransactions.loadPreparedTransactions(this);
/*     */     }
/* 171 */     catch (XAException e) {
/* 172 */       throw JMSExceptionHelper.newJMSException("Failed to recover: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stop() throws JMSException {
/* 177 */     this.preparedTransactions.stop();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addActiveTransaction(BrokerClient client, Object transactionId) {
/* 186 */     List list = (List)this.activeClients.get(client);
/* 187 */     if (list == null) {
/* 188 */       list = new ArrayList();
/* 189 */       this.activeClients.put(client, list);
/*     */     } 
/* 191 */     list.add(transactionId);
/*     */   }
/*     */   
/*     */   private void removeActiveTransaction(BrokerClient client, Object transactionId) {
/* 195 */     List list = (List)this.activeClients.get(client);
/* 196 */     if (list != null)
/* 197 */       list.remove(transactionId); 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\TransactionManagerImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */