/*    */ package org.codehaus.activemq.store.vm;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import javax.jms.JMSException;
/*    */ import org.codehaus.activemq.service.impl.PersistenceAdapterSupport;
/*    */ import org.codehaus.activemq.store.MessageStore;
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
/*    */ public class VMPersistenceAdapter
/*    */   extends PersistenceAdapterSupport
/*    */ {
/*    */   public Map getInitialDestinations() {
/* 36 */     return new HashMap();
/*    */   }
/*    */   
/*    */   public static VMPersistenceAdapter newInstance(File file) {
/* 40 */     return new VMPersistenceAdapter();
/*    */   }
/*    */   
/*    */   public MessageStore createQueueMessageStore(String destinationName) throws JMSException {
/* 44 */     return new VMMessageStore();
/*    */   }
/*    */   
/*    */   public TopicMessageStore createTopicMessageStore(String destinationName) throws JMSException {
/* 48 */     return new VMTopicMessageStore();
/*    */   }
/*    */   
/*    */   public PreparedTransactionStore createPreparedTransactionStore() throws JMSException {
/* 52 */     return new VMPreparedTransactionStoreImpl();
/*    */   }
/*    */   
/*    */   public void beginTransaction() {}
/*    */   
/*    */   public void commitTransaction() {}
/*    */   
/*    */   public void rollbackTransaction() {}
/*    */   
/*    */   public void start() throws JMSException {}
/*    */   
/*    */   public void stop() throws JMSException {}
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\vm\VMPersistenceAdapter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */