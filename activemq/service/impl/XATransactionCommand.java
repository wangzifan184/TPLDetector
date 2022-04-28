/*     */ package org.codehaus.activemq.service.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.util.Map;
/*     */ import javax.transaction.xa.XAException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.broker.Broker;
/*     */ import org.codehaus.activemq.message.ActiveMQXid;
/*     */ import org.codehaus.activemq.service.Transaction;
/*     */ import org.codehaus.activemq.store.PreparedTransactionStore;
/*     */ import org.codehaus.activemq.util.SerializationHelper;
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
/*     */ public class XATransactionCommand
/*     */   extends AbstractTransaction
/*     */ {
/*  39 */   private static final Log log = LogFactory.getLog(TransactionManagerImpl.class);
/*     */   
/*     */   private ActiveMQXid xid;
/*     */   
/*     */   private transient Map xaTxs;
/*     */   
/*     */   private transient PreparedTransactionStore preparedTransactions;
/*     */ 
/*     */   
/*     */   public XATransactionCommand() {
/*  49 */     super(null);
/*     */   }
/*     */   
/*     */   public XATransactionCommand(Broker broker, ActiveMQXid xid, Map xaTxs, PreparedTransactionStore preparedTransactions) {
/*  53 */     super(broker);
/*  54 */     this.xid = xid;
/*  55 */     this.xaTxs = xaTxs;
/*  56 */     this.preparedTransactions = preparedTransactions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initialise(Map xaTxs, PreparedTransactionStore preparedTransactions) {
/*  67 */     this.xaTxs = xaTxs;
/*  68 */     this.preparedTransactions = preparedTransactions;
/*     */   }
/*     */   
/*     */   public void commit(boolean onePhase) throws XAException {
/*  72 */     switch (getState()) {
/*     */       
/*     */       case 0:
/*  75 */         if (!onePhase) {
/*  76 */           XAException xAException = new XAException("Cannot do 2 phase commit if the transaction has not been prepared.");
/*  77 */           xAException.errorCode = -6;
/*  78 */           throw xAException;
/*     */         } 
/*  80 */         setState((byte)3);
/*  81 */         this.xaTxs.remove(this.xid);
/*     */         return;
/*     */       case 1:
/*  84 */         if (!onePhase) {
/*  85 */           XAException xAException = new XAException("Cannot do 2 phase commit if the transaction has not been prepared.");
/*  86 */           xAException.errorCode = -6;
/*  87 */           throw xAException;
/*     */         } 
/*     */         
/*     */         try {
/*  91 */           prePrepare();
/*     */         }
/*  93 */         catch (XAException e) {
/*  94 */           throw e;
/*     */         }
/*  96 */         catch (Throwable e) {
/*  97 */           log.warn("COMMIT FAILED: ", e);
/*  98 */           rollback();
/*     */           
/* 100 */           XAException xAException = new XAException("ONE PHASE COMMIT FAILED: Transaction rolled back.");
/* 101 */           xAException.errorCode = 104;
/* 102 */           xAException.initCause(e);
/* 103 */           throw xAException;
/*     */         } 
/*     */         
/* 106 */         setState((byte)3);
/* 107 */         this.xaTxs.remove(this.xid);
/*     */         
/*     */         try {
/* 110 */           postCommit();
/*     */         }
/* 112 */         catch (Throwable e) {
/*     */ 
/*     */           
/* 115 */           log.warn("POST COMMIT FAILED: ", e);
/* 116 */           XAException xAException = new XAException("POST COMMIT FAILED");
/* 117 */           xAException.errorCode = -3;
/* 118 */           xAException.initCause(e);
/* 119 */           throw xAException;
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 2:
/* 126 */         setState((byte)3);
/* 127 */         this.xaTxs.remove(this.xid);
/*     */         
/*     */         try {
/* 130 */           postCommit();
/*     */         }
/* 132 */         catch (Throwable e) {
/*     */ 
/*     */           
/* 135 */           log.warn("POST COMMIT FAILED: ", e);
/* 136 */           XAException xAException = new XAException("POST COMMIT FAILED");
/* 137 */           xAException.errorCode = -3;
/* 138 */           xAException.initCause(e);
/* 139 */           throw xAException;
/*     */         } 
/*     */         return;
/*     */     } 
/* 143 */     XAException xae = new XAException("Cannot call commit now.");
/* 144 */     xae.errorCode = -6;
/* 145 */     throw xae;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void rollback() throws XAException {
/* 151 */     switch (getState()) {
/*     */       
/*     */       case 0:
/* 154 */         this.xaTxs.remove(this.xid);
/* 155 */         setState((byte)3);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 1:
/* 160 */         this.xaTxs.remove(this.xid);
/* 161 */         setState((byte)3);
/*     */         
/*     */         try {
/* 164 */           postRollback();
/*     */         }
/* 166 */         catch (Throwable e) {
/*     */ 
/*     */           
/* 169 */           log.warn("POST ROLLBACK FAILED: ", e);
/* 170 */           XAException xae = new XAException("POST ROLLBACK FAILED");
/* 171 */           xae.errorCode = -3;
/* 172 */           xae.initCause(e);
/* 173 */           throw xae;
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 179 */         this.preparedTransactions.remove(this.xid);
/* 180 */         this.xaTxs.remove(this.xid);
/* 181 */         setState((byte)3);
/*     */         
/*     */         try {
/* 184 */           postRollback();
/*     */         }
/* 186 */         catch (Throwable e) {
/*     */ 
/*     */           
/* 189 */           log.warn("POST ROLLBACK FAILED: ", e);
/* 190 */           XAException xae = new XAException("POST ROLLBACK FAILED");
/* 191 */           xae.errorCode = -3;
/* 192 */           xae.initCause(e);
/* 193 */           throw xae;
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int prepare() throws XAException {
/* 201 */     switch (getState()) {
/*     */       
/*     */       case 0:
/* 204 */         this.xaTxs.remove(this.xid);
/* 205 */         setState((byte)3);
/* 206 */         return 3;
/*     */       
/*     */       case 1:
/*     */         try {
/* 210 */           prePrepare();
/*     */         }
/* 212 */         catch (XAException e) {
/* 213 */           throw e;
/*     */         }
/* 215 */         catch (Throwable e) {
/* 216 */           log.warn("PREPARE FAILED: ", e);
/* 217 */           rollback();
/*     */ 
/*     */           
/* 220 */           XAException xAException = new XAException("PREPARE FAILED: Transaction rollback.");
/* 221 */           xAException.errorCode = 104;
/* 222 */           xAException.initCause(e);
/* 223 */           throw xAException;
/*     */         } 
/*     */ 
/*     */         
/* 227 */         setState((byte)2);
/* 228 */         this.preparedTransactions.put(this.xid, this);
/*     */         
/* 230 */         return 0;
/*     */     } 
/* 232 */     XAException xae = new XAException("Cannot call prepare now.");
/* 233 */     xae.errorCode = -6;
/* 234 */     throw xae;
/*     */   }
/*     */ 
/*     */   
/*     */   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
/* 239 */     super.readExternal(in);
/* 240 */     this.xid = ActiveMQXid.read(in);
/*     */   }
/*     */   
/*     */   public void writeExternal(ObjectOutput out) throws IOException {
/* 244 */     super.writeExternal(out);
/* 245 */     this.xid.writeExternal(out);
/*     */   }
/*     */   
/*     */   public static Transaction fromBytes(byte[] data) throws IOException, ClassNotFoundException {
/* 249 */     return (Transaction)SerializationHelper.deserialize(data);
/*     */   }
/*     */   
/*     */   public byte[] toBytes() throws IOException {
/* 253 */     return SerializationHelper.serialize(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\XATransactionCommand.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */