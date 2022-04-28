/*     */ package org.codehaus.activemq.store.howl;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.DefaultWireFormat;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.service.impl.PersistenceAdapterSupport;
/*     */ import org.codehaus.activemq.store.MessageStore;
/*     */ import org.codehaus.activemq.store.PersistenceAdapter;
/*     */ import org.codehaus.activemq.store.PreparedTransactionStore;
/*     */ import org.codehaus.activemq.store.TopicMessageStore;
/*     */ import org.codehaus.activemq.store.jdbm.JdbmPersistenceAdapter;
/*     */ import org.codehaus.activemq.util.JMSExceptionHelper;
/*     */ import org.objectweb.howl.log.Configuration;
/*     */ import org.objectweb.howl.log.LogConfigurationException;
/*     */ import org.objectweb.howl.log.Logger;
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
/*     */ 
/*     */ public class HowlPersistenceAdapter
/*     */   extends PersistenceAdapterSupport
/*     */ {
/*  51 */   private static final Log log = LogFactory.getLog(HowlPersistenceAdapter.class);
/*     */   
/*     */   private PersistenceAdapter longTermPersistence;
/*     */   private Configuration configuration;
/*  55 */   private int maximumTotalCachedMessages = 10000;
/*  56 */   private int maximumCachedMessagesPerStore = 100;
/*     */ 
/*     */   
/*     */   private int cachedMessageCount;
/*     */ 
/*     */   
/*     */   private File directory;
/*     */ 
/*     */   
/*     */   private Logger transactionLog;
/*     */ 
/*     */   
/*     */   public static HowlPersistenceAdapter newInstance(File directory) throws JMSException {
/*  69 */     return new HowlPersistenceAdapter(directory, (PersistenceAdapter)JdbmPersistenceAdapter.newInstance(directory));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HowlPersistenceAdapter(File directory, PersistenceAdapter longTermPersistence) {
/*  76 */     this.directory = directory;
/*  77 */     this.longTermPersistence = longTermPersistence;
/*     */   }
/*     */   
/*     */   public Map getInitialDestinations() {
/*  81 */     return this.longTermPersistence.getInitialDestinations();
/*     */   }
/*     */   
/*     */   public MessageStore createQueueMessageStore(String destinationName) throws JMSException {
/*  85 */     MessageStore checkpointStore = this.longTermPersistence.createQueueMessageStore(destinationName);
/*  86 */     return new HowlMessageStore(this, checkpointStore, this.transactionLog, (WireFormat)new DefaultWireFormat());
/*     */   }
/*     */ 
/*     */   
/*     */   public TopicMessageStore createTopicMessageStore(String destinationName) throws JMSException {
/*  91 */     return this.longTermPersistence.createTopicMessageStore(destinationName);
/*     */   }
/*     */ 
/*     */   
/*     */   public PreparedTransactionStore createPreparedTransactionStore() throws JMSException {
/*  96 */     return this.longTermPersistence.createPreparedTransactionStore();
/*     */   }
/*     */ 
/*     */   
/*     */   public void beginTransaction() throws JMSException {}
/*     */ 
/*     */   
/*     */   public void commitTransaction() throws JMSException {}
/*     */ 
/*     */   
/*     */   public void rollbackTransaction() {}
/*     */   
/*     */   public void start() throws JMSException {
/* 109 */     if (this.transactionLog == null) {
/* 110 */       if (this.directory != null) {
/* 111 */         this.directory.mkdirs();
/*     */       }
/*     */       try {
/* 114 */         this.transactionLog = createTransactionLog();
/*     */       }
/* 116 */       catch (Exception e) {
/* 117 */         throw JMSExceptionHelper.newJMSException("Failed to create Howl based message store due to: " + e, e);
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/* 122 */       log.info("Using Howl transaction log in directory: " + getLogFileDir());
/*     */       
/* 124 */       this.transactionLog.open();
/*     */     }
/* 126 */     catch (Exception e) {
/* 127 */       throw JMSExceptionHelper.newJMSException("Failed to open Howl transaction log: " + e, e);
/*     */     } 
/* 129 */     this.longTermPersistence.start();
/*     */   }
/*     */   
/*     */   public void stop() throws JMSException {
/*     */     try {
/* 134 */       this.transactionLog.close();
/*     */     }
/* 136 */     catch (Exception e) {
/* 137 */       throw JMSExceptionHelper.newJMSException("Failed to close Howl transaction log due to: " + e, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean hasCacheCapacity(HowlMessageStore messageStore) {
/* 149 */     if (this.cachedMessageCount < this.maximumTotalCachedMessages) {
/* 150 */       this.cachedMessageCount++;
/* 151 */       return true;
/*     */     } 
/* 153 */     return false;
/*     */   }
/*     */   
/*     */   public synchronized void onMessageRemove(HowlMessageStore messageStore) {
/* 157 */     this.cachedMessageCount--;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PersistenceAdapter getLongTermPersistence() {
/* 163 */     return this.longTermPersistence;
/*     */   }
/*     */   
/*     */   public void setLongTermPersistence(PersistenceAdapter longTermPersistence) {
/* 167 */     this.longTermPersistence = longTermPersistence;
/*     */   }
/*     */   
/*     */   public int getMaximumCachedMessagesPerStore() {
/* 171 */     return this.maximumCachedMessagesPerStore;
/*     */   }
/*     */   
/*     */   public void setMaximumCachedMessagesPerStore(int maximumCachedMessagesPerStore) {
/* 175 */     this.maximumCachedMessagesPerStore = maximumCachedMessagesPerStore;
/*     */   }
/*     */   
/*     */   public int getMaximumTotalCachedMessages() {
/* 179 */     return this.maximumTotalCachedMessages;
/*     */   }
/*     */   
/*     */   public void setMaximumTotalCachedMessages(int maximumTotalCachedMessages) {
/* 183 */     this.maximumTotalCachedMessages = maximumTotalCachedMessages;
/*     */   }
/*     */   
/*     */   public File getDirectory() {
/* 187 */     return this.directory;
/*     */   }
/*     */   
/*     */   public void setDirectory(File directory) {
/* 191 */     this.directory = directory;
/*     */   }
/*     */   
/*     */   public Configuration getConfiguration() throws LogConfigurationException, IOException {
/* 195 */     if (this.configuration == null) {
/* 196 */       this.configuration = createConfiguration();
/*     */     }
/* 198 */     return this.configuration;
/*     */   }
/*     */   
/*     */   public void setConfiguration(Configuration configuration) {
/* 202 */     this.configuration = configuration;
/*     */   }
/*     */   
/*     */   public Logger getTransactionLog() {
/* 206 */     return this.transactionLog;
/*     */   }
/*     */   
/*     */   public void setTransactionLog(Logger transactionLog) {
/* 210 */     this.transactionLog = transactionLog;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBufferClassName() throws LogConfigurationException, IOException {
/* 217 */     return getConfiguration().getBufferClassName();
/*     */   }
/*     */   
/*     */   public int getBufferSize() throws LogConfigurationException, IOException {
/* 221 */     return getConfiguration().getBufferSize();
/*     */   }
/*     */   
/*     */   public int getFlushSleepTime() throws LogConfigurationException, IOException {
/* 225 */     return getConfiguration().getFlushSleepTime();
/*     */   }
/*     */   
/*     */   public String getLogFileDir() throws LogConfigurationException, IOException {
/* 229 */     return getConfiguration().getLogFileDir();
/*     */   }
/*     */   
/*     */   public String getLogFileExt() throws LogConfigurationException, IOException {
/* 233 */     return getConfiguration().getLogFileExt();
/*     */   }
/*     */   
/*     */   public String getLogFileName() throws LogConfigurationException, IOException {
/* 237 */     return getConfiguration().getLogFileName();
/*     */   }
/*     */   
/*     */   public int getMaxBlocksPerFile() throws LogConfigurationException, IOException {
/* 241 */     return getConfiguration().getMaxBlocksPerFile();
/*     */   }
/*     */   
/*     */   public int getMaxBuffers() throws LogConfigurationException, IOException {
/* 245 */     return getConfiguration().getMaxBuffers();
/*     */   }
/*     */   
/*     */   public int getMaxLogFiles() throws LogConfigurationException, IOException {
/* 249 */     return getConfiguration().getMaxLogFiles();
/*     */   }
/*     */   
/*     */   public int getMinBuffers() throws LogConfigurationException, IOException {
/* 253 */     return getConfiguration().getMinBuffers();
/*     */   }
/*     */   
/*     */   public int getThreadsWaitingForceThreshold() throws LogConfigurationException, IOException {
/* 257 */     return getConfiguration().getThreadsWaitingForceThreshold();
/*     */   }
/*     */   
/*     */   public boolean isChecksumEnabled() throws LogConfigurationException, IOException {
/* 261 */     return getConfiguration().isChecksumEnabled();
/*     */   }
/*     */   
/*     */   public void setBufferClassName(String s) throws LogConfigurationException, IOException {
/* 265 */     getConfiguration().setBufferClassName(s);
/*     */   }
/*     */   
/*     */   public void setBufferSize(int i) throws LogConfigurationException, IOException {
/* 269 */     getConfiguration().setBufferSize(i);
/*     */   }
/*     */   
/*     */   public void setChecksumEnabled(boolean b) throws LogConfigurationException, IOException {
/* 273 */     getConfiguration().setChecksumEnabled(b);
/*     */   }
/*     */   
/*     */   public void setFlushSleepTime(int i) throws LogConfigurationException, IOException {
/* 277 */     getConfiguration().setFlushSleepTime(i);
/*     */   }
/*     */   
/*     */   public void setLogFileDir(String s) throws LogConfigurationException, IOException {
/* 281 */     getConfiguration().setLogFileDir(s);
/*     */   }
/*     */   
/*     */   public void setLogFileExt(String s) throws LogConfigurationException, IOException {
/* 285 */     getConfiguration().setLogFileExt(s);
/*     */   }
/*     */   
/*     */   public void setLogFileName(String s) throws LogConfigurationException, IOException {
/* 289 */     getConfiguration().setLogFileName(s);
/*     */   }
/*     */   
/*     */   public void setMaxBlocksPerFile(int i) throws LogConfigurationException, IOException {
/* 293 */     getConfiguration().setMaxBlocksPerFile(i);
/*     */   }
/*     */   
/*     */   public void setMaxBuffers(int i) throws LogConfigurationException, IOException {
/* 297 */     getConfiguration().setMaxBuffers(i);
/*     */   }
/*     */   
/*     */   public void setMaxLogFiles(int i) throws LogConfigurationException, IOException {
/* 301 */     getConfiguration().setMaxLogFiles(i);
/*     */   }
/*     */   
/*     */   public void setMinBuffers(int i) throws LogConfigurationException, IOException {
/* 305 */     getConfiguration().setMinBuffers(i);
/*     */   }
/*     */   
/*     */   public void setThreadsWaitingForceThreshold(int i) throws LogConfigurationException, IOException {
/* 309 */     getConfiguration().setThreadsWaitingForceThreshold(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Logger createTransactionLog() throws IOException, LogConfigurationException {
/* 317 */     return new Logger(getConfiguration());
/*     */   }
/*     */   
/*     */   protected Configuration createConfiguration() throws IOException, LogConfigurationException {
/* 321 */     String[] names = { "org/codehaus/activemq/howl.properties", "org/codehaus/activemq/defaultHowl.properties" };
/*     */     
/* 323 */     Configuration answer = null;
/* 324 */     for (int i = 0; i < names.length; i++) {
/* 325 */       InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(names[i]);
/* 326 */       if (in == null) {
/* 327 */         in = getClass().getClassLoader().getResourceAsStream(names[i]);
/*     */       }
/* 329 */       if (in != null) {
/* 330 */         Properties properties = new Properties();
/* 331 */         properties.load(in);
/* 332 */         answer = new Configuration(properties);
/*     */       } 
/*     */     } 
/* 335 */     if (answer == null) {
/* 336 */       log.warn("Could not find file: " + names[0] + " or: " + names[1] + " on the classpath to initialise Howl");
/* 337 */       answer = new Configuration();
/*     */     } 
/* 339 */     if (this.directory != null) {
/* 340 */       answer.setLogFileDir(this.directory.getAbsolutePath());
/*     */     }
/* 342 */     return answer;
/*     */   }
/*     */   
/*     */   public HowlPersistenceAdapter() {}
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\howl\HowlPersistenceAdapter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */