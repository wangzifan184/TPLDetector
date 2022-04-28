/*     */ package org.codehaus.activemq.store.journal;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.Channel;
/*     */ import EDU.oswego.cs.dl.util.concurrent.ClockDaemon;
/*     */ import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;
/*     */ import EDU.oswego.cs.dl.util.concurrent.LinkedQueue;
/*     */ import EDU.oswego.cs.dl.util.concurrent.QueuedExecutor;
/*     */ import EDU.oswego.cs.dl.util.concurrent.ThreadFactory;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.journal.InvalidRecordLocationException;
/*     */ import org.codehaus.activemq.journal.Journal;
/*     */ import org.codehaus.activemq.journal.JournalEventListener;
/*     */ import org.codehaus.activemq.journal.RecordLocation;
/*     */ import org.codehaus.activemq.journal.impl.JournalImpl;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.DefaultWireFormat;
/*     */ import org.codehaus.activemq.message.MessageAck;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.service.impl.PersistenceAdapterSupport;
/*     */ import org.codehaus.activemq.store.MessageStore;
/*     */ import org.codehaus.activemq.store.PersistenceAdapter;
/*     */ import org.codehaus.activemq.store.PreparedTransactionStore;
/*     */ import org.codehaus.activemq.store.TopicMessageStore;
/*     */ import org.codehaus.activemq.store.jdbm.JdbmPersistenceAdapter;
/*     */ import org.codehaus.activemq.util.JMSExceptionHelper;
/*     */ import org.codehaus.activemq.util.TransactionTemplate;
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
/*     */ public class JournalPersistenceAdapter
/*     */   extends PersistenceAdapterSupport
/*     */   implements JournalEventListener
/*     */ {
/*  67 */   private static final Log log = LogFactory.getLog(JournalPersistenceAdapter.class);
/*     */   private Journal journal;
/*     */   private PersistenceAdapter longTermPersistence;
/*  70 */   private File directory = new File("logs");
/*  71 */   private WireFormat wireFormat = (WireFormat)new DefaultWireFormat();
/*     */   private TransactionTemplate transactionTemplate;
/*     */   private boolean sync = true;
/*  74 */   private final ConcurrentHashMap messageStores = new ConcurrentHashMap();
/*     */   
/*     */   private boolean performingRecovery;
/*     */   private static final int PACKET_RECORD_TYPE = 0;
/*     */   private static final int COMMAND_RECORD_TYPE = 1;
/*  79 */   private Channel checkpointRequests = (Channel)new LinkedQueue();
/*     */ 
/*     */ 
/*     */   
/*     */   private QueuedExecutor checkpointExecutor;
/*     */ 
/*     */   
/*     */   ClockDaemon clockDaemon;
/*     */ 
/*     */   
/*     */   private Object clockTicket;
/*     */ 
/*     */ 
/*     */   
/*     */   public static JournalPersistenceAdapter newInstance(File directory) throws IOException, JMSException {
/*  94 */     return new JournalPersistenceAdapter(directory, (PersistenceAdapter)JdbmPersistenceAdapter.newInstance(directory), new DefaultWireFormat());
/*     */   }
/*     */   
/*     */   public JournalPersistenceAdapter() {
/*  98 */     this.checkpointExecutor = new QueuedExecutor((Channel)new LinkedQueue());
/*  99 */     this.checkpointExecutor.setThreadFactory(new ThreadFactory() {
/*     */           public Thread newThread(Runnable runnable) {
/* 101 */             Thread answer = new Thread(runnable, "Checkpoint Worker");
/* 102 */             answer.setDaemon(true);
/* 103 */             answer.setPriority(10);
/* 104 */             return answer;
/*     */           }
/*     */           private final JournalPersistenceAdapter this$0;
/*     */         });
/*     */   }
/*     */   public JournalPersistenceAdapter(File directory, PersistenceAdapter longTermPersistence, DefaultWireFormat wireFormat) throws IOException {
/* 110 */     this();
/* 111 */     this.directory = directory;
/* 112 */     this.longTermPersistence = longTermPersistence;
/* 113 */     this.wireFormat = (WireFormat)wireFormat;
/*     */   }
/*     */   
/*     */   public Map getInitialDestinations() {
/* 117 */     return this.longTermPersistence.getInitialDestinations();
/*     */   }
/*     */   
/*     */   public MessageStore createQueueMessageStore(String destinationName) throws JMSException {
/* 121 */     MessageStore checkpointStore = this.longTermPersistence.createQueueMessageStore(destinationName);
/* 122 */     JournalMessageStore store = new JournalMessageStore(this, checkpointStore, destinationName, this.sync);
/* 123 */     this.messageStores.put(destinationName, store);
/* 124 */     return store;
/*     */   }
/*     */   
/*     */   public TopicMessageStore createTopicMessageStore(String destinationName) throws JMSException {
/* 128 */     return this.longTermPersistence.createTopicMessageStore(destinationName);
/*     */   }
/*     */   
/*     */   public PreparedTransactionStore createPreparedTransactionStore() throws JMSException {
/* 132 */     return this.longTermPersistence.createPreparedTransactionStore();
/*     */   }
/*     */   
/*     */   public void beginTransaction() throws JMSException {
/* 136 */     this.longTermPersistence.beginTransaction();
/*     */   }
/*     */   
/*     */   public void commitTransaction() throws JMSException {
/* 140 */     this.longTermPersistence.commitTransaction();
/*     */   }
/*     */   
/*     */   public void rollbackTransaction() {
/* 144 */     this.longTermPersistence.rollbackTransaction();
/*     */   }
/*     */   
/*     */   public synchronized void start() throws JMSException {
/* 148 */     this.longTermPersistence.start();
/* 149 */     if (this.journal == null) {
/*     */       try {
/* 151 */         log.info("Opening journal.");
/* 152 */         this.journal = createJournal();
/* 153 */         log.info("Opened journal: " + this.journal);
/* 154 */         this.journal.setJournalEventListener(this);
/*     */       }
/* 156 */       catch (Exception e) {
/* 157 */         throw JMSExceptionHelper.newJMSException("Failed to open transaction journal: " + e, e);
/*     */       } 
/*     */       try {
/* 160 */         recover();
/*     */       }
/* 162 */       catch (Exception e) {
/* 163 */         throw JMSExceptionHelper.newJMSException("Failed to recover transactions from journal: " + e, e);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 168 */     this.clockTicket = getClockDaemon().executePeriodically(60000L, new Runnable() { private final JournalPersistenceAdapter this$0;
/*     */           public void run() {
/* 170 */             JournalPersistenceAdapter.this.checkpoint();
/*     */           }
/*     */         },  false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void stop() throws JMSException {
/* 178 */     if (this.clockTicket != null)
/*     */     {
/* 180 */       ClockDaemon.cancel(this.clockTicket);
/*     */     }
/*     */ 
/*     */     
/* 184 */     checkpoint();
/* 185 */     this.checkpointExecutor.shutdownAfterProcessingCurrentlyQueuedTasks();
/*     */     
/* 187 */     JMSException firstException = null;
/* 188 */     if (this.journal != null) {
/*     */       try {
/* 190 */         this.journal.close();
/* 191 */         this.journal = null;
/*     */       }
/* 193 */       catch (Exception e) {
/* 194 */         firstException = JMSExceptionHelper.newJMSException("Failed to close Howl transaction log due to: " + e, e);
/*     */       } 
/*     */     }
/* 197 */     this.longTermPersistence.stop();
/*     */     
/* 199 */     if (firstException != null) {
/* 200 */       throw firstException;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PersistenceAdapter getLongTermPersistence() {
/* 207 */     return this.longTermPersistence;
/*     */   }
/*     */   
/*     */   public void setLongTermPersistence(PersistenceAdapter longTermPersistence) {
/* 211 */     this.longTermPersistence = longTermPersistence;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getDirectory() {
/* 218 */     return this.directory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDirectory(File directory) {
/* 225 */     this.directory = directory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSync() {
/* 232 */     return this.sync;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSync(boolean sync) {
/* 239 */     this.sync = sync;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WireFormat getWireFormat() {
/* 246 */     return this.wireFormat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWireFormat(WireFormat wireFormat) {
/* 253 */     this.wireFormat = wireFormat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Journal createJournal() throws IOException {
/* 260 */     return (Journal)new JournalImpl(this.directory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void overflowNotification(RecordLocation safeLocation) {
/* 270 */     checkpoint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkpoint() {
/*     */     try {
/* 279 */       this.checkpointRequests.put(Boolean.TRUE);
/* 280 */       this.checkpointExecutor.execute(new Runnable()
/*     */           {
/*     */             private final JournalPersistenceAdapter this$0;
/*     */             
/*     */             public void run() {
/*     */               try {
/* 286 */                 boolean requested = false;
/* 287 */                 while (JournalPersistenceAdapter.this.checkpointRequests.poll(0L) != null) {
/* 288 */                   requested = true;
/*     */                 }
/* 290 */                 if (!requested) {
/*     */                   return;
/*     */                 }
/*     */               }
/* 294 */               catch (InterruptedException e1) {
/*     */                 return;
/*     */               } 
/*     */               
/* 298 */               JournalPersistenceAdapter.log.info("Checkpoint started.");
/* 299 */               Iterator iterator = JournalPersistenceAdapter.this.messageStores.values().iterator();
/* 300 */               RecordLocation newMark = null;
/* 301 */               while (iterator.hasNext()) {
/*     */                 try {
/* 303 */                   JournalMessageStore ms = iterator.next();
/* 304 */                   RecordLocation mark = ms.checkpoint();
/* 305 */                   if (mark != null && (newMark == null || newMark.compareTo(mark) < 0)) {
/* 306 */                     newMark = mark;
/*     */                   }
/*     */                 }
/* 309 */                 catch (Exception e) {
/* 310 */                   JournalPersistenceAdapter.log.error("Failed to checkpoint a message store: " + e, e);
/*     */                 } 
/*     */               } 
/*     */               try {
/* 314 */                 if (newMark != null) {
/* 315 */                   JournalPersistenceAdapter.this.journal.setMark(newMark, true);
/*     */                 }
/*     */               }
/* 318 */               catch (Exception e) {
/* 319 */                 JournalPersistenceAdapter.log.error("Failed to mark the Journal: " + e, e);
/*     */               } 
/* 321 */               JournalPersistenceAdapter.log.info("Checkpoint done.");
/*     */             }
/*     */           });
/*     */     }
/* 325 */     catch (InterruptedException e) {
/* 326 */       log.warn("Request to start checkpoint failed: " + e, e);
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
/*     */   public RecordLocation writePacket(String destination, Packet packet, boolean sync) throws JMSException {
/*     */     try {
/* 339 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 340 */       DataOutputStream os = new DataOutputStream(baos);
/* 341 */       os.writeByte(0);
/* 342 */       os.writeUTF(destination);
/* 343 */       this.wireFormat.writePacket(packet, os);
/* 344 */       os.close();
/* 345 */       byte[] data = baos.toByteArray();
/* 346 */       return this.journal.write(data, sync);
/*     */     
/*     */     }
/* 349 */     catch (IOException e) {
/* 350 */       throw createWriteException(packet, e);
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
/*     */   public RecordLocation writeCommand(String command, boolean sync) throws JMSException {
/*     */     try {
/* 363 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 364 */       DataOutputStream os = new DataOutputStream(baos);
/* 365 */       os.writeByte(1);
/* 366 */       os.writeUTF(command);
/* 367 */       os.close();
/* 368 */       byte[] data = baos.toByteArray();
/* 369 */       return this.journal.write(data, sync);
/*     */     
/*     */     }
/* 372 */     catch (IOException e) {
/* 373 */       throw createWriteException(command, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Packet readPacket(RecordLocation location) throws JMSException {
/*     */     try {
/* 384 */       byte[] data = this.journal.read(location);
/*     */       
/* 386 */       DataInputStream is = new DataInputStream(new ByteArrayInputStream(data));
/* 387 */       byte type = is.readByte();
/* 388 */       if (type != 0) {
/* 389 */         throw new IOException("Record is not a packet type.");
/*     */       }
/* 391 */       String destination = is.readUTF();
/* 392 */       Packet packet = this.wireFormat.readPacket(is);
/* 393 */       is.close();
/* 394 */       return packet;
/*     */     
/*     */     }
/* 397 */     catch (InvalidRecordLocationException e) {
/* 398 */       throw createReadException(location, e);
/*     */     }
/* 400 */     catch (IOException e) {
/* 401 */       throw createReadException(location, e);
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
/*     */ 
/*     */ 
/*     */   
/*     */   private void recover() throws IllegalStateException, InvalidRecordLocationException, IOException, JMSException {
/* 416 */     RecordLocation pos = null;
/* 417 */     int transactionCounter = 0;
/*     */     
/* 419 */     log.info("Journal Recovery Started.");
/*     */ 
/*     */     
/* 422 */     while ((pos = this.journal.getNextRecordLocation(pos)) != null) {
/* 423 */       byte[] data = this.journal.read(pos);
/*     */ 
/*     */       
/* 426 */       String destination = null;
/* 427 */       Packet packet = null;
/* 428 */       DataInputStream is = new DataInputStream(new ByteArrayInputStream(data)); try {
/*     */         JournalMessageStore store;
/* 430 */         byte type = is.readByte();
/* 431 */         switch (type) {
/*     */ 
/*     */           
/*     */           case 0:
/* 435 */             destination = is.readUTF();
/* 436 */             packet = this.wireFormat.readPacket(is);
/*     */ 
/*     */             
/* 439 */             store = (JournalMessageStore)createQueueMessageStore(destination);
/* 440 */             if (packet instanceof ActiveMQMessage) {
/* 441 */               ActiveMQMessage msg = (ActiveMQMessage)packet;
/*     */               try {
/* 443 */                 store.getLongTermStore().addMessage(msg);
/* 444 */                 transactionCounter++;
/*     */               }
/* 446 */               catch (Throwable e) {
/* 447 */                 log.error("Recovery Failure: Could not add message: " + msg.getJMSMessageIdentity().getMessageID() + ", reason: " + e, e);
/*     */               }  break;
/*     */             } 
/* 450 */             if (packet instanceof MessageAck) {
/* 451 */               MessageAck ack = (MessageAck)packet;
/*     */               try {
/* 453 */                 store.getLongTermStore().removeMessage(ack.getMessageIdentity(), ack);
/* 454 */                 transactionCounter++;
/*     */               }
/* 456 */               catch (Throwable e) {
/* 457 */                 log.error("Recovery Failure: Could not remove message: " + ack.getMessageIdentity().getMessageID() + ", reason: " + e, e);
/*     */               } 
/*     */               break;
/*     */             } 
/* 461 */             log.error("Unknown type of packet in transaction log which will be discarded: " + packet);
/*     */             break;
/*     */ 
/*     */           
/*     */           case 1:
/*     */             break;
/*     */           
/*     */           default:
/* 469 */             log.error("Unknown type of record in transaction log which will be discarded: " + type);
/*     */             break;
/*     */         } 
/*     */       
/*     */       } finally {
/* 474 */         is.close();
/*     */       } 
/*     */     } 
/*     */     
/* 478 */     RecordLocation location = writeCommand("RECOVERED", true);
/* 479 */     this.journal.setMark(location, true);
/*     */     
/* 481 */     log.info("Journal Recovered: " + transactionCounter + " message(s) in transactions recovered.");
/*     */   }
/*     */   
/*     */   private JMSException createReadException(RecordLocation location, Exception e) {
/* 485 */     return JMSExceptionHelper.newJMSException("Failed to read to journal for: " + location + ". Reason: " + e, e);
/*     */   }
/*     */   
/*     */   protected JMSException createWriteException(Packet packet, Exception e) {
/* 489 */     return JMSExceptionHelper.newJMSException("Failed to write to journal for: " + packet + ". Reason: " + e, e);
/*     */   }
/*     */   
/*     */   protected JMSException createWriteException(String command, Exception e) {
/* 493 */     return JMSExceptionHelper.newJMSException("Failed to write to journal for command: " + command + ". Reason: " + e, e);
/*     */   }
/*     */   
/*     */   protected JMSException createRecoveryFailedException(Exception e) {
/* 497 */     return JMSExceptionHelper.newJMSException("Failed to recover from journal. Reason: " + e, e);
/*     */   }
/*     */   
/*     */   public ClockDaemon getClockDaemon() {
/* 501 */     if (this.clockDaemon == null) {
/* 502 */       this.clockDaemon = new ClockDaemon();
/* 503 */       this.clockDaemon.setThreadFactory(new ThreadFactory() { private final JournalPersistenceAdapter this$0;
/*     */             public Thread newThread(Runnable runnable) {
/* 505 */               Thread thread = new Thread(runnable, "Checkpoint Timmer");
/* 506 */               thread.setDaemon(true);
/* 507 */               return thread;
/*     */             } }
/*     */         );
/*     */     } 
/* 511 */     return this.clockDaemon;
/*     */   }
/*     */   
/*     */   public void setClockDaemon(ClockDaemon clockDaemon) {
/* 515 */     this.clockDaemon = clockDaemon;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\journal\JournalPersistenceAdapter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */