/*     */ package org.codehaus.activemq.store.jdbm;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.jms.JMSException;
/*     */ import jdbm.RecordManager;
/*     */ import jdbm.RecordManagerFactory;
/*     */ import jdbm.btree.BTree;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.AlreadyClosedException;
/*     */ import org.codehaus.activemq.service.impl.PersistenceAdapterSupport;
/*     */ import org.codehaus.activemq.store.MessageStore;
/*     */ import org.codehaus.activemq.store.PreparedTransactionStore;
/*     */ import org.codehaus.activemq.store.TopicMessageStore;
/*     */ import org.codehaus.activemq.util.DefaultComparator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JdbmPersistenceAdapter
/*     */   extends PersistenceAdapterSupport
/*     */ {
/*  48 */   private static final Log log = LogFactory.getLog(JdbmPersistenceAdapter.class);
/*     */   
/*     */   private RecordManager manager;
/*  51 */   private File directory = new File("ActiveMQ");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Properties properties;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JdbmPersistenceAdapter newInstance(File directory) throws JMSException {
/*  62 */     return new JdbmPersistenceAdapter(directory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JdbmPersistenceAdapter(File directory) {
/*  70 */     this.directory = directory;
/*     */   }
/*     */   
/*     */   public JdbmPersistenceAdapter(RecordManager manager) {
/*  74 */     this.manager = manager;
/*     */   }
/*     */   
/*     */   public Map getInitialDestinations() {
/*  78 */     return null;
/*     */   }
/*     */   
/*     */   public MessageStore createQueueMessageStore(String destinationName) throws JMSException {
/*     */     try {
/*  83 */       BTree messageDb = createDatabase("Queue_" + destinationName);
/*  84 */       BTree sequenceDb = createDatabase("Sequence_Queue_" + destinationName);
/*  85 */       JdbmMessageStore messageStore = new JdbmMessageStore(messageDb, sequenceDb);
/*  86 */       return messageStore;
/*     */     }
/*  88 */     catch (IOException e) {
/*  89 */       throw JMSExceptionHelper.newJMSException("Failed to create a QueueMessageContainer for destination: " + destinationName + ". Reason: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public TopicMessageStore createTopicMessageStore(String destinationName) throws JMSException {
/*     */     try {
/*  95 */       BTree messageDb = createDatabase("Topic_" + destinationName);
/*  96 */       BTree sequenceDb = createDatabase("Sequence_Topic_" + destinationName);
/*  97 */       BTree consumerAckDb = createDatabase("Consumer_Acks_Topic_" + destinationName);
/*  98 */       BTree subscriberDb = createDatabase("Subscriber_" + destinationName);
/*  99 */       BTree messageCountDb = createDatabase("MessageCount_Topic_" + destinationName);
/* 100 */       JdbmTopicMessageStore messageStore = new JdbmTopicMessageStore(messageDb, sequenceDb, consumerAckDb, subscriberDb, messageCountDb);
/* 101 */       return messageStore;
/*     */     }
/* 103 */     catch (IOException e) {
/* 104 */       throw JMSExceptionHelper.newJMSException("Failed to create a TopicMessageContainer for destination: " + destinationName + ". Reason: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public PreparedTransactionStore createPreparedTransactionStore() throws JMSException {
/*     */     try {
/* 110 */       return new JdbmPreparedTransactionStore(createDatabase("XaPrepareTxnDb"));
/*     */     }
/* 112 */     catch (IOException e) {
/* 113 */       throw JMSExceptionHelper.newJMSException("Could not create XA Prepare Transaction Database. Reason: " + e, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void beginTransaction() {}
/*     */   
/*     */   public void commitTransaction() throws JMSException {
/*     */     try {
/* 122 */       this.manager.commit();
/*     */     }
/* 124 */     catch (IOException e) {
/* 125 */       throw JMSExceptionHelper.newJMSException("Could not commit transaction. Reason: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void rollbackTransaction() {
/*     */     try {
/* 131 */       this.manager.rollback();
/*     */     }
/* 133 */     catch (IOException e) {
/* 134 */       log.error("Could not rollback transaction. Reason: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void start() throws JMSException {
/* 139 */     if (this.manager == null) {
/* 140 */       this.directory.mkdirs();
/*     */       
/* 142 */       log.info("Creating JDBM based message store in directory: " + this.directory.getAbsolutePath());
/*     */       
/*     */       try {
/* 145 */         String name = this.directory.getAbsolutePath() + "/Store";
/* 146 */         if (this.properties != null) {
/* 147 */           this.manager = RecordManagerFactory.createRecordManager(name, this.properties);
/*     */         } else {
/*     */           
/* 150 */           this.manager = RecordManagerFactory.createRecordManager(name);
/*     */         }
/*     */       
/* 153 */       } catch (IOException e) {
/* 154 */         throw JMSExceptionHelper.newJMSException("Failed to create JDBM persistent store at directory: " + this.directory + ". Reason: " + e, e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void stop() throws JMSException {
/* 161 */     if (this.manager != null) {
/*     */       try {
/* 163 */         this.manager.close();
/*     */       }
/* 165 */       catch (IOException e) {
/* 166 */         throw JMSExceptionHelper.newJMSException("Failed to close PersistenceAdapter. Reason: " + e, e);
/*     */       } finally {
/*     */         
/* 169 */         this.manager = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public RecordManager getManager() {
/* 177 */     return this.manager;
/*     */   }
/*     */   
/*     */   public void setManager(RecordManager manager) {
/* 181 */     this.manager = manager;
/*     */   }
/*     */   
/*     */   public File getDirectory() {
/* 185 */     return this.directory;
/*     */   }
/*     */   
/*     */   public void setDirectory(File directory) {
/* 189 */     this.directory = directory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized BTree createDatabase(String name) throws IOException, AlreadyClosedException {
/* 195 */     if (this.manager == null) {
/* 196 */       throw new AlreadyClosedException("JDBM PersistenceAdapter");
/*     */     }
/*     */ 
/*     */     
/* 200 */     long recid = this.manager.getNamedObject(name);
/* 201 */     BTree tree = null;
/* 202 */     if (recid != 0L) {
/* 203 */       tree = BTree.load(this.manager, recid);
/*     */     } else {
/*     */       
/* 206 */       DefaultComparator defaultComparator = new DefaultComparator();
/*     */       
/* 208 */       tree = BTree.createInstance(this.manager, (Comparator)defaultComparator);
/* 209 */       this.manager.setNamedObject(name, tree.getRecid());
/*     */     } 
/* 211 */     return tree;
/*     */   }
/*     */   
/*     */   public JdbmPersistenceAdapter() {}
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\jdbm\JdbmPersistenceAdapter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */