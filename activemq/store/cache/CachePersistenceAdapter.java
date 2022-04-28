/*    */ package org.codehaus.activemq.store.cache;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ import javax.jms.JMSException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.codehaus.activemq.service.impl.PersistenceAdapterSupport;
/*    */ import org.codehaus.activemq.store.MessageStore;
/*    */ import org.codehaus.activemq.store.PersistenceAdapter;
/*    */ import org.codehaus.activemq.store.PreparedTransactionStore;
/*    */ import org.codehaus.activemq.store.TopicMessageStore;
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
/*    */ 
/*    */ 
/*    */ public abstract class CachePersistenceAdapter
/*    */   extends PersistenceAdapterSupport
/*    */ {
/* 42 */   private static final Log log = LogFactory.getLog(CachePersistenceAdapter.class);
/*    */   
/*    */   private PersistenceAdapter longTermPersistence;
/*    */   
/*    */   public CachePersistenceAdapter() {}
/*    */   
/*    */   public CachePersistenceAdapter(PersistenceAdapter longTermPersistence) throws IOException {
/* 49 */     this.longTermPersistence = longTermPersistence;
/*    */   }
/*    */   
/*    */   public Map getInitialDestinations() {
/* 53 */     return this.longTermPersistence.getInitialDestinations();
/*    */   }
/*    */   
/*    */   public MessageStore createQueueMessageStore(String destinationName) throws JMSException {
/* 57 */     MessageStore longtermStore = this.longTermPersistence.createQueueMessageStore(destinationName);
/* 58 */     CacheMessageStore store = new CacheMessageStore(this, longtermStore, createMessageCache(destinationName));
/* 59 */     return store;
/*    */   }
/*    */   
/*    */   public TopicMessageStore createTopicMessageStore(String destinationName) throws JMSException {
/* 63 */     TopicMessageStore longtermStore = this.longTermPersistence.createTopicMessageStore(destinationName);
/* 64 */     CacheTopicMessageStore store = new CacheTopicMessageStore(this, longtermStore, new SimpleMessageCache());
/* 65 */     return store;
/*    */   }
/*    */   
/*    */   public PreparedTransactionStore createPreparedTransactionStore() throws JMSException {
/* 69 */     return this.longTermPersistence.createPreparedTransactionStore();
/*    */   }
/*    */   
/*    */   public void beginTransaction() throws JMSException {
/* 73 */     this.longTermPersistence.beginTransaction();
/*    */   }
/*    */   
/*    */   public void commitTransaction() throws JMSException {
/* 77 */     this.longTermPersistence.commitTransaction();
/*    */   }
/*    */   
/*    */   public void rollbackTransaction() {
/* 81 */     this.longTermPersistence.rollbackTransaction();
/*    */   }
/*    */   
/*    */   public void start() throws JMSException {
/* 85 */     this.longTermPersistence.start();
/*    */   }
/*    */   
/*    */   public void stop() throws JMSException {
/* 89 */     this.longTermPersistence.stop();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public PersistenceAdapter getLongTermPersistence() {
/* 95 */     return this.longTermPersistence;
/*    */   }
/*    */   
/*    */   public void setLongTermPersistence(PersistenceAdapter longTermPersistence) {
/* 99 */     this.longTermPersistence = longTermPersistence;
/*    */   }
/*    */   
/*    */   protected abstract MessageCache createMessageCache(String paramString);
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\cache\CachePersistenceAdapter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */