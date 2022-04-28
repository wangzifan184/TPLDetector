/*    */ package org.codehaus.activemq.store.bdbn;
/*    */ 
/*    */ import com.sleepycat.db.Db;
/*    */ import com.sleepycat.db.DbEnv;
/*    */ import com.sleepycat.db.DbException;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.util.Map;
/*    */ import javax.jms.JMSException;
/*    */ import org.codehaus.activemq.service.impl.PersistenceAdapterSupport;
/*    */ import org.codehaus.activemq.store.MessageStore;
/*    */ import org.codehaus.activemq.store.PreparedTransactionStore;
/*    */ import org.codehaus.activemq.store.TopicMessageStore;
/*    */ import org.codehaus.activemq.util.JMSExceptionHelper;
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
/*    */ public class BDbPersistenceAdapter
/*    */   extends PersistenceAdapterSupport
/*    */ {
/*    */   protected DbEnv environment;
/*    */   
/*    */   public BDbPersistenceAdapter(DbEnv environment) {
/* 40 */     this.environment = environment;
/*    */   }
/*    */   
/*    */   public Map getInitialDestinations() {
/* 44 */     return null;
/*    */   }
/*    */   
/*    */   public MessageStore createQueueMessageStore(String destinationName) throws JMSException {
/* 48 */     return null;
/*    */   }
/*    */   
/*    */   public TopicMessageStore createTopicMessageStore(String destinationName) {
/* 52 */     return null;
/*    */   }
/*    */   
/*    */   public PreparedTransactionStore createPreparedTransactionStore() throws JMSException {
/* 56 */     return null;
/*    */   }
/*    */   
/*    */   public void beginTransaction() throws JMSException {
/*    */     try {
/* 61 */       BDbHelper.createTransaction(this.environment);
/*    */     }
/* 63 */     catch (DbException e) {
/* 64 */       throw JMSExceptionHelper.newJMSException("Failed to commit transaction. Reason: " + e, e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void commitTransaction() throws JMSException {
/* 69 */     BDbHelper.commitTransaction(BDbHelper.getTransaction());
/*    */   }
/*    */   
/*    */   public void rollbackTransaction() {
/* 73 */     BDbHelper.rollbackTransaction(BDbHelper.getTransaction());
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() throws JMSException {}
/*    */   
/*    */   public void stop() throws JMSException {
/*    */     try {
/* 81 */       this.environment.close(0);
/*    */     }
/* 83 */     catch (DbException e) {
/* 84 */       throw JMSExceptionHelper.newJMSException("Failed to close environment. Reason: " + e, e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Db createDatabase(String name) throws JMSException, FileNotFoundException, DbException {
/* 91 */     return BDbHelper.open(this.environment, name, false);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\bdbn\BDbPersistenceAdapter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */