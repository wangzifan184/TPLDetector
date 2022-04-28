/*    */ package org.codehaus.activemq.service.impl;
/*    */ 
/*    */ import java.util.Map;
/*    */ import javax.transaction.xa.XAException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.codehaus.activemq.broker.Broker;
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
/*    */ public class LocalTransactionCommand
/*    */   extends AbstractTransaction
/*    */ {
/*    */   private static final long serialVersionUID = -5754338187296859149L;
/* 32 */   private static final Log log = LogFactory.getLog(LocalTransactionCommand.class);
/*    */   
/*    */   private Map localTxs;
/*    */   
/*    */   private Object txid;
/*    */   
/*    */   public LocalTransactionCommand(Broker broker, Map localTxs, Object txid) {
/* 39 */     super(broker);
/* 40 */     this.localTxs = localTxs;
/* 41 */     this.txid = txid;
/*    */   }
/*    */ 
/*    */   
/*    */   public void commit(boolean onePhase) throws XAException {
/*    */     try {
/* 47 */       prePrepare();
/*    */     }
/* 49 */     catch (XAException e) {
/* 50 */       throw e;
/*    */     }
/* 52 */     catch (Throwable e) {
/* 53 */       log.warn("COMMIT FAILED: ", e);
/* 54 */       rollback();
/*    */       
/* 56 */       XAException xae = new XAException("COMMIT FAILED: Transaction rolled back.");
/* 57 */       xae.errorCode = 104;
/* 58 */       xae.initCause(e);
/* 59 */       throw xae;
/*    */     } 
/*    */     
/* 62 */     setState((byte)3);
/* 63 */     this.localTxs.remove(this.txid);
/*    */     
/*    */     try {
/* 66 */       postCommit();
/*    */     }
/* 68 */     catch (Throwable e) {
/*    */ 
/*    */       
/* 71 */       log.warn("POST COMMIT FAILED: ", e);
/* 72 */       XAException xae = new XAException("POST COMMIT FAILED");
/* 73 */       xae.errorCode = -3;
/* 74 */       xae.initCause(e);
/* 75 */       throw xae;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void rollback() throws XAException {
/* 81 */     setState((byte)3);
/* 82 */     this.localTxs.remove(this.txid);
/*    */     
/*    */     try {
/* 85 */       postRollback();
/*    */     }
/* 87 */     catch (Throwable e) {
/* 88 */       log.warn("POST ROLLBACK FAILED: ", e);
/* 89 */       XAException xae = new XAException("POST ROLLBACK FAILED");
/* 90 */       xae.errorCode = -3;
/* 91 */       xae.initCause(e);
/* 92 */       throw xae;
/*    */     } 
/*    */   }
/*    */   
/*    */   public int prepare() throws XAException {
/* 97 */     XAException xae = new XAException("Prepare not implemented on Local Transactions.");
/* 98 */     xae.errorCode = -3;
/* 99 */     throw xae;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\LocalTransactionCommand.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */