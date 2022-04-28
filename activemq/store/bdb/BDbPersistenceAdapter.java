/*     */ package org.codehaus.activemq.store.bdb;
/*     */ 
/*     */ import com.sleepycat.je.Database;
/*     */ import com.sleepycat.je.DatabaseConfig;
/*     */ import com.sleepycat.je.DatabaseException;
/*     */ import com.sleepycat.je.Environment;
/*     */ import com.sleepycat.je.SecondaryConfig;
/*     */ import com.sleepycat.je.SecondaryDatabase;
/*     */ import com.sleepycat.je.SecondaryKeyCreator;
/*     */ import com.sleepycat.je.Transaction;
/*     */ import com.sleepycat.je.TransactionConfig;
/*     */ import java.io.File;
/*     */ import java.util.Map;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.DefaultWireFormat;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.service.impl.PersistenceAdapterSupport;
/*     */ import org.codehaus.activemq.store.MessageStore;
/*     */ import org.codehaus.activemq.store.PreparedTransactionStore;
/*     */ import org.codehaus.activemq.store.TopicMessageStore;
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
/*     */ public class BDbPersistenceAdapter
/*     */   extends PersistenceAdapterSupport
/*     */ {
/*  51 */   private static final Log log = LogFactory.getLog(BDbPersistenceAdapter.class);
/*     */   
/*     */   private Environment environment;
/*     */   private WireFormat wireFormat;
/*     */   private DatabaseConfig config;
/*     */   private TransactionConfig transactionConfig;
/*  57 */   private File directory = new File("ActiveMQ");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BDbPersistenceAdapter newInstance(File directory) throws JMSException {
/*  68 */     return new BDbPersistenceAdapter(directory);
/*     */   }
/*     */ 
/*     */   
/*     */   public BDbPersistenceAdapter() {
/*  73 */     this(null, (WireFormat)new DefaultWireFormat());
/*     */   }
/*     */   
/*     */   public BDbPersistenceAdapter(File directory) {
/*  77 */     this();
/*  78 */     this.directory = directory;
/*     */   }
/*     */   
/*     */   public BDbPersistenceAdapter(Environment environment, WireFormat wireFormat) {
/*  82 */     this(environment, wireFormat, BDbHelper.createDatabaseConfig(), new TransactionConfig());
/*     */   }
/*     */   
/*     */   public BDbPersistenceAdapter(Environment environment, WireFormat wireFormat, DatabaseConfig config, TransactionConfig transactionConfig) {
/*  86 */     this.environment = environment;
/*  87 */     this.wireFormat = wireFormat;
/*  88 */     this.config = config;
/*  89 */     this.transactionConfig = transactionConfig;
/*     */   }
/*     */   
/*     */   public Map getInitialDestinations() {
/*  93 */     return null;
/*     */   }
/*     */   
/*     */   public MessageStore createQueueMessageStore(String destinationName) throws JMSException {
/*     */     try {
/*  98 */       Database database = createDatabase("Queue_" + destinationName);
/*  99 */       SequenceNumberCreator sequenceNumberCreator = new SequenceNumberCreator();
/* 100 */       SecondaryConfig secondaryConfig = createSecondaryConfig(sequenceNumberCreator);
/* 101 */       SecondaryDatabase secondaryDatabase = createSecondaryDatabase("Queue_Index_" + destinationName, database, secondaryConfig);
/* 102 */       sequenceNumberCreator.initialise(secondaryDatabase);
/* 103 */       return new BDbMessageStore(database, secondaryDatabase, secondaryConfig, sequenceNumberCreator, this.wireFormat.copy());
/*     */     }
/* 105 */     catch (DatabaseException e) {
/* 106 */       throw JMSExceptionHelper.newJMSException("Could not create Queue MessageContainer for destination: " + destinationName + ". Reason: " + e, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public TopicMessageStore createTopicMessageStore(String destinationName) throws JMSException {
/*     */     try {
/* 113 */       Database database = createDatabase("Topic_" + destinationName);
/* 114 */       SequenceNumberCreator sequenceNumberCreator = new SequenceNumberCreator();
/* 115 */       SecondaryConfig secondaryConfig = createSecondaryConfig(sequenceNumberCreator);
/* 116 */       SecondaryDatabase secondaryDatabase = createSecondaryDatabase("Topic_Index_" + destinationName, database, secondaryConfig);
/* 117 */       sequenceNumberCreator.initialise(secondaryDatabase);
/* 118 */       Database subscriptionDatabase = createDatabase("ConsumeAck_" + destinationName);
/* 119 */       return new BDbTopicMessageStore(database, secondaryDatabase, secondaryConfig, sequenceNumberCreator, this.wireFormat.copy(), subscriptionDatabase);
/*     */     }
/* 121 */     catch (DatabaseException e) {
/* 122 */       throw JMSExceptionHelper.newJMSException("Could not create Topic MessageContainer for destination: " + destinationName + ". Reason: " + e, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public PreparedTransactionStore createPreparedTransactionStore() throws JMSException {
/*     */     try {
/* 129 */       return new BDbPreparedTransactionStore(createDatabase("XaPrepareTxnDb"));
/*     */     }
/* 131 */     catch (DatabaseException e) {
/* 132 */       throw JMSExceptionHelper.newJMSException("Could not create XA Prepare Transaction Database. Reason: " + e, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void beginTransaction() throws JMSException {
/*     */     try {
/* 139 */       if (BDbHelper.getTransactionCount() == 0) {
/* 140 */         Transaction transaction = this.environment.beginTransaction(BDbHelper.getTransaction(), this.transactionConfig);
/* 141 */         BDbHelper.pushTransaction(transaction);
/*     */       } else {
/*     */         
/* 144 */         Transaction transaction = BDbHelper.getTransaction();
/* 145 */         BDbHelper.pushTransaction(transaction);
/*     */       }
/*     */     
/* 148 */     } catch (DatabaseException e) {
/* 149 */       throw JMSExceptionHelper.newJMSException("Failed to begin transaction: " + e, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void commitTransaction() throws JMSException {
/* 155 */     if (BDbHelper.getTransactionCount() == 1) {
/* 156 */       Transaction transaction = BDbHelper.getTransaction();
/* 157 */       if (transaction == null) {
/* 158 */         log.warn("Attempt to commit transaction when non in progress");
/*     */       } else {
/*     */         
/*     */         try {
/* 162 */           transaction.commit();
/*     */         }
/* 164 */         catch (DatabaseException e) {
/* 165 */           throw JMSExceptionHelper.newJMSException("Failed to commit transaction: " + transaction + ": " + e, e);
/*     */         } finally {
/*     */           
/* 168 */           BDbHelper.popTransaction();
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 173 */       BDbHelper.popTransaction();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void rollbackTransaction() {
/* 178 */     Transaction transaction = BDbHelper.getTransaction();
/* 179 */     if (transaction != null) {
/* 180 */       if (BDbHelper.getTransactionCount() == 1) {
/*     */         try {
/* 182 */           transaction.abort();
/*     */         }
/* 184 */         catch (DatabaseException e) {
/* 185 */           log.warn("Cannot rollback transaction due to: " + e, (Throwable)e);
/*     */         } finally {
/*     */           
/* 188 */           BDbHelper.popTransaction();
/*     */         } 
/*     */       } else {
/*     */         
/* 192 */         BDbHelper.popTransaction();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() throws JMSException {
/* 199 */     if (this.environment == null) {
/* 200 */       this.directory.mkdirs();
/*     */       
/* 202 */       log.info("Creating Berkeley DB based message store in directory: " + this.directory.getAbsolutePath());
/*     */       
/*     */       try {
/* 205 */         this.environment = BDbHelper.createEnvironment(this.directory);
/*     */       }
/* 207 */       catch (DatabaseException e) {
/* 208 */         throw JMSExceptionHelper.newJMSException("Failed to open Berkeley DB persistent store at directory: " + this.directory + ". Reason: " + e, e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void stop() throws JMSException {
/* 215 */     if (this.environment != null) {
/*     */       try {
/* 217 */         this.environment.close();
/*     */       }
/* 219 */       catch (DatabaseException e) {
/* 220 */         throw JMSExceptionHelper.newJMSException("Failed to close environment. Reason: " + e, e);
/*     */       } finally {
/*     */         
/* 223 */         this.environment = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public File getDirectory() {
/* 231 */     return this.directory;
/*     */   }
/*     */   
/*     */   public void setDirectory(File directory) {
/* 235 */     this.directory = directory;
/*     */   }
/*     */   
/*     */   public WireFormat getWireFormat() {
/* 239 */     return this.wireFormat;
/*     */   }
/*     */   
/*     */   public void setWireFormat(WireFormat wireFormat) {
/* 243 */     this.wireFormat = wireFormat;
/*     */   }
/*     */   
/*     */   public TransactionConfig getTransactionConfig() {
/* 247 */     return this.transactionConfig;
/*     */   }
/*     */   
/*     */   public void setTransactionConfig(TransactionConfig transactionConfig) {
/* 251 */     this.transactionConfig = transactionConfig;
/*     */   }
/*     */   
/*     */   public Environment getEnvironment() {
/* 255 */     return this.environment;
/*     */   }
/*     */   
/*     */   public void setEnvironment(Environment environment) {
/* 259 */     this.environment = environment;
/*     */   }
/*     */   
/*     */   public DatabaseConfig getConfig() {
/* 263 */     return this.config;
/*     */   }
/*     */   
/*     */   public void setConfig(DatabaseConfig config) {
/* 267 */     this.config = config;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Database createDatabase(String name) throws DatabaseException {
/* 275 */     if (log.isTraceEnabled()) {
/* 276 */       log.trace("Opening database: " + name);
/*     */     }
/* 278 */     return this.environment.openDatabase(null, name, this.config);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SecondaryDatabase createSecondaryDatabase(String name, Database database, SecondaryConfig secondaryConfig) throws DatabaseException {
/* 284 */     if (log.isTraceEnabled()) {
/* 285 */       log.trace("Opening secondary database: " + name);
/*     */     }
/* 287 */     return this.environment.openSecondaryDatabase(null, name, database, secondaryConfig);
/*     */   }
/*     */   
/*     */   public static JMSException closeDatabase(Database db, JMSException firstException) {
/* 291 */     if (db != null) {
/*     */       
/* 293 */       if (log.isTraceEnabled()) {
/*     */         try {
/* 295 */           log.trace("Closing database: " + db.getDatabaseName());
/*     */         }
/* 297 */         catch (DatabaseException e) {
/* 298 */           log.trace("Closing database: " + db + " but could not get the name: " + e);
/*     */         } 
/*     */       }
/*     */       
/*     */       try {
/* 303 */         db.close();
/*     */       }
/* 305 */       catch (DatabaseException e) {
/* 306 */         if (firstException == null) {
/* 307 */           firstException = JMSExceptionHelper.newJMSException("Failed to close database. Reason: " + e, (Exception)e);
/*     */         }
/*     */       } 
/*     */     } 
/* 311 */     return firstException;
/*     */   }
/*     */   
/*     */   protected SecondaryConfig createSecondaryConfig(SecondaryKeyCreator keyGenerator) {
/* 315 */     SecondaryConfig answer = new SecondaryConfig();
/* 316 */     answer.setKeyCreator(keyGenerator);
/* 317 */     answer.setAllowCreate(true);
/* 318 */     answer.setAllowPopulate(true);
/* 319 */     answer.setTransactional(true);
/* 320 */     return answer;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\bdb\BDbPersistenceAdapter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */