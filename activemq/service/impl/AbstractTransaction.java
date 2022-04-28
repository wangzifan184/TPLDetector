/*     */ package org.codehaus.activemq.service.impl;
/*     */ 
/*     */ import java.io.Externalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import javax.transaction.xa.XAException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.broker.Broker;
/*     */ import org.codehaus.activemq.service.Transaction;
/*     */ import org.codehaus.activemq.service.TransactionTask;
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
/*     */ public abstract class AbstractTransaction
/*     */   implements Transaction, Externalizable
/*     */ {
/*  42 */   private static final Log log = LogFactory.getLog(AbstractTransaction.class);
/*     */   
/*     */   public static final byte START_STATE = 0;
/*     */   
/*     */   public static final byte IN_USE_STATE = 1;
/*     */   
/*     */   public static final byte PREPARED_STATE = 2;
/*     */   public static final byte FINISHED_STATE = 3;
/*  50 */   private ArrayList prePrepareTasks = new ArrayList();
/*  51 */   private ArrayList postCommitTasks = new ArrayList();
/*  52 */   private ArrayList postRollbackTasks = new ArrayList();
/*  53 */   private byte state = 0;
/*     */   private transient Broker broker;
/*     */   
/*     */   protected AbstractTransaction(Broker broker) {
/*  57 */     this.broker = broker;
/*     */   }
/*     */   
/*     */   public Broker getBroker() {
/*  61 */     return this.broker;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBroker(Broker broker) {
/*  70 */     this.broker = broker;
/*     */   }
/*     */   
/*     */   public byte getState() {
/*  74 */     return this.state;
/*     */   }
/*     */   
/*     */   public void setState(byte state) {
/*  78 */     this.state = state;
/*     */   }
/*     */   
/*     */   public void addPostCommitTask(TransactionTask r) {
/*  82 */     this.postCommitTasks.add(r);
/*  83 */     if (this.state == 0) {
/*  84 */       this.state = 1;
/*     */     }
/*     */   }
/*     */   
/*     */   public void addPostRollbackTask(TransactionTask r) {
/*  89 */     this.postRollbackTasks.add(r);
/*  90 */     if (this.state == 0) {
/*  91 */       this.state = 1;
/*     */     }
/*     */   }
/*     */   
/*     */   public void addPrePrepareTask(TransactionTask r) {
/*  96 */     this.prePrepareTasks.add(r);
/*  97 */     if (this.state == 0) {
/*  98 */       this.state = 1;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void prePrepare() throws Throwable {
/*     */     XAException xae;
/* 106 */     switch (this.state) {
/*     */       case 0:
/*     */       case 1:
/*     */         break;
/*     */       default:
/* 111 */         xae = new XAException("Prepare cannot be called now.");
/* 112 */         xae.errorCode = -6;
/* 113 */         throw xae;
/*     */     } 
/*     */ 
/*     */     
/* 117 */     for (Iterator iter = this.prePrepareTasks.iterator(); iter.hasNext(); ) {
/* 118 */       TransactionTask r = iter.next();
/* 119 */       r.execute(this.broker);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void postCommit() throws Throwable {
/* 125 */     for (Iterator iter = this.postCommitTasks.iterator(); iter.hasNext(); ) {
/* 126 */       TransactionTask r = iter.next();
/* 127 */       r.execute(this.broker);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void postRollback() throws Throwable {
/* 133 */     for (Iterator iter = this.postRollbackTasks.iterator(); iter.hasNext(); ) {
/* 134 */       TransactionTask r = iter.next();
/* 135 */       r.execute(this.broker);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
/* 140 */     this.state = in.readByte();
/* 141 */     this.prePrepareTasks = readTaskList(in);
/* 142 */     this.postCommitTasks = readTaskList(in);
/* 143 */     this.postRollbackTasks = readTaskList(in);
/*     */   }
/*     */   
/*     */   public void writeExternal(ObjectOutput out) throws IOException {
/* 147 */     out.writeByte(this.state);
/* 148 */     writeTaskList(this.prePrepareTasks, out);
/* 149 */     writeTaskList(this.postCommitTasks, out);
/* 150 */     writeTaskList(this.postRollbackTasks, out);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 154 */     return super.toString() + "[prePrepares=" + this.prePrepareTasks + "; postCommits=" + this.postCommitTasks + "; postRollbacks=" + this.postRollbackTasks + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ArrayList readTaskList(ObjectInput in) throws IOException {
/* 161 */     int size = in.readInt();
/* 162 */     ArrayList answer = new ArrayList(size);
/* 163 */     for (int i = 0; i < size; i++) {
/* 164 */       answer.add(readTask(in));
/*     */     }
/* 166 */     return answer;
/*     */   }
/*     */   
/*     */   protected void writeTaskList(ArrayList tasks, ObjectOutput out) throws IOException {
/* 170 */     int size = tasks.size();
/* 171 */     out.writeInt(size);
/* 172 */     for (int i = 0; i < size; i++) {
/* 173 */       writeTask(tasks.get(i), out);
/*     */     }
/*     */   }
/*     */   
/*     */   protected TransactionTask readTask(ObjectInput in) throws IOException {
/* 178 */     return PacketTransactionTask.readTask(in);
/*     */   }
/*     */   
/*     */   protected void writeTask(TransactionTask task, ObjectOutput out) throws IOException {
/* 182 */     PacketTransactionTask.writeTask(task, out);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\AbstractTransaction.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */