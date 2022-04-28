/*    */ package org.codehaus.activemq.util;
/*    */ 
/*    */ import javax.jms.JMSException;
/*    */ import org.codehaus.activemq.store.PersistenceAdapter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TransactionTemplate
/*    */ {
/*    */   private PersistenceAdapter persistenceAdapter;
/*    */   
/*    */   public TransactionTemplate(PersistenceAdapter persistenceAdapter) {
/* 34 */     this.persistenceAdapter = persistenceAdapter;
/*    */   }
/*    */   
/*    */   public void run(Callback task) throws JMSException {
/* 38 */     this.persistenceAdapter.beginTransaction();
/* 39 */     Throwable throwable = null;
/*    */     try {
/* 41 */       task.execute();
/*    */     }
/* 43 */     catch (Throwable t) {
/* 44 */       throwable = t;
/*    */     } 
/* 46 */     if (throwable == null) {
/* 47 */       this.persistenceAdapter.commitTransaction();
/*    */     } else {
/*    */       
/* 50 */       this.persistenceAdapter.rollbackTransaction();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activem\\util\TransactionTemplate.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */