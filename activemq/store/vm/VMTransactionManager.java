/*    */ package org.codehaus.activemq.store.vm;
/*    */ 
/*    */ import org.codehaus.activemq.broker.Broker;
/*    */ import org.codehaus.activemq.service.impl.TransactionManagerImpl;
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
/*    */ public class VMTransactionManager
/*    */   extends TransactionManagerImpl
/*    */ {
/*    */   public VMTransactionManager(Broker broker) {
/* 30 */     this(broker, new VMPreparedTransactionStoreImpl());
/*    */   }
/*    */   
/*    */   public VMTransactionManager(Broker broker, PreparedTransactionStore preparedTransactions) {
/* 34 */     super(broker, preparedTransactions);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\vm\VMTransactionManager.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */