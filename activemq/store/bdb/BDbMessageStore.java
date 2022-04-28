/*     */ package org.codehaus.activemq.store.bdb;
/*     */ 
/*     */ import com.sleepycat.je.CursorConfig;
/*     */ import com.sleepycat.je.Database;
/*     */ import com.sleepycat.je.DatabaseEntry;
/*     */ import com.sleepycat.je.DatabaseException;
/*     */ import com.sleepycat.je.LockMode;
/*     */ import com.sleepycat.je.OperationStatus;
/*     */ import com.sleepycat.je.SecondaryConfig;
/*     */ import com.sleepycat.je.SecondaryCursor;
/*     */ import com.sleepycat.je.SecondaryDatabase;
/*     */ import com.sleepycat.je.Transaction;
/*     */ import java.io.IOException;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.AlreadyClosedException;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.MessageAck;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.service.MessageContainer;
/*     */ import org.codehaus.activemq.service.MessageIdentity;
/*     */ import org.codehaus.activemq.service.QueueMessageContainer;
/*     */ import org.codehaus.activemq.store.MessageStore;
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
/*     */ public class BDbMessageStore
/*     */   implements MessageStore
/*     */ {
/*  40 */   private static final Log log = LogFactory.getLog(BDbMessageStore.class);
/*     */   
/*     */   private Database database;
/*     */   
/*     */   private WireFormat wireFormat;
/*     */   private SecondaryDatabase secondaryDatabase;
/*     */   private SecondaryConfig secondaryConfig;
/*     */   private SequenceNumberCreator sequenceNumberCreator;
/*     */   private MessageContainer container;
/*     */   private CursorConfig cursorConfig;
/*     */   
/*     */   public BDbMessageStore(Database database, SecondaryDatabase secondaryDatabase, SecondaryConfig secondaryConfig, SequenceNumberCreator sequenceNumberCreator, WireFormat wireFormat) {
/*  52 */     this.database = database;
/*  53 */     this.secondaryDatabase = secondaryDatabase;
/*  54 */     this.secondaryConfig = secondaryConfig;
/*  55 */     this.sequenceNumberCreator = sequenceNumberCreator;
/*  56 */     this.wireFormat = wireFormat;
/*     */   }
/*     */   
/*     */   public void setMessageContainer(MessageContainer container) {
/*  60 */     this.container = container;
/*     */   }
/*     */   
/*     */   public MessageIdentity addMessage(ActiveMQMessage message) throws JMSException {
/*  64 */     checkClosed();
/*  65 */     String messageID = message.getJMSMessageID();
/*     */     try {
/*  67 */       Transaction transaction = BDbHelper.getTransaction();
/*  68 */       DatabaseEntry key = createKey(messageID);
/*  69 */       DatabaseEntry value = new DatabaseEntry(asBytes(message));
/*  70 */       this.database.put(transaction, key, value);
/*     */       
/*  72 */       MessageIdentity answer = message.getJMSMessageIdentity();
/*  73 */       answer.setSequenceNumber(this.sequenceNumberCreator.getLastKey());
/*  74 */       return answer;
/*     */     }
/*  76 */     catch (DatabaseException e) {
/*  77 */       throw JMSExceptionHelper.newJMSException("Failed to broker message: " + messageID + " in container: " + e, e);
/*     */     }
/*  79 */     catch (IOException e) {
/*  80 */       throw JMSExceptionHelper.newJMSException("Failed to broker message: " + messageID + " in container: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public ActiveMQMessage getMessage(MessageIdentity identity) throws JMSException {
/*  85 */     checkClosed();
/*  86 */     ActiveMQMessage answer = null;
/*  87 */     String messageID = identity.getMessageID();
/*     */     try {
/*  89 */       DatabaseEntry key = createKey(messageID);
/*  90 */       DatabaseEntry value = new DatabaseEntry();
/*  91 */       if (this.database.get(null, key, value, null) == OperationStatus.SUCCESS) {
/*  92 */         answer = extractMessage(value);
/*     */       }
/*  94 */       return answer;
/*     */     }
/*  96 */     catch (DatabaseException e) {
/*  97 */       throw JMSExceptionHelper.newJMSException("Failed to peek next message after: " + messageID + " from container: " + e, e);
/*     */     }
/*  99 */     catch (IOException e) {
/* 100 */       throw JMSExceptionHelper.newJMSException("Failed to broker message: " + messageID + " in container: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void removeMessage(MessageIdentity identity, MessageAck ack) throws JMSException {
/* 105 */     checkClosed();
/* 106 */     String messageID = identity.getMessageID();
/*     */     try {
/* 108 */       Transaction transaction = BDbHelper.getTransaction();
/*     */ 
/*     */       
/* 111 */       DatabaseEntry sequenceNumber = getSequenceNumberKey(identity);
/*     */ 
/*     */ 
/*     */       
/* 115 */       this.sequenceNumberCreator.setDeleteKey(sequenceNumber);
/*     */       
/* 117 */       OperationStatus status = this.secondaryDatabase.delete(transaction, sequenceNumber);
/* 118 */       if (status != OperationStatus.SUCCESS) {
/* 119 */         log.error("Could not delete sequenece number for: " + identity + " status: " + status);
/*     */       }
/*     */     }
/* 122 */     catch (DatabaseException e) {
/* 123 */       throw JMSExceptionHelper.newJMSException("Failed to delete message: " + messageID + " from container: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void recover(QueueMessageContainer container) throws JMSException {
/* 128 */     checkClosed();
/* 129 */     SecondaryCursor cursor = null;
/*     */     try {
/* 131 */       cursor = this.secondaryDatabase.openSecondaryCursor(BDbHelper.getTransaction(), this.cursorConfig);
/* 132 */       DatabaseEntry sequenceNumberEntry = new DatabaseEntry();
/* 133 */       DatabaseEntry keyEntry = new DatabaseEntry();
/* 134 */       DatabaseEntry valueEntry = new DatabaseEntry();
/* 135 */       OperationStatus status = cursor.getFirst(sequenceNumberEntry, keyEntry, valueEntry, LockMode.DEFAULT);
/* 136 */       while (status == OperationStatus.SUCCESS) {
/* 137 */         String messageID = extractString(keyEntry);
/* 138 */         container.recoverMessageToBeDelivered(new MessageIdentity(messageID, sequenceNumberEntry));
/* 139 */         status = cursor.getNext(sequenceNumberEntry, keyEntry, valueEntry, LockMode.DEFAULT);
/*     */       } 
/* 141 */       if (status != OperationStatus.NOTFOUND) {
/* 142 */         log.warn("Unexpected status code while recovering: " + status);
/*     */       }
/*     */     }
/* 145 */     catch (DatabaseException e) {
/* 146 */       throw JMSExceptionHelper.newJMSException("Failed to recover container. Reason: " + e, e);
/*     */     } finally {
/*     */       
/* 149 */       if (cursor != null) {
/*     */         try {
/* 151 */           cursor.close();
/*     */         }
/* 153 */         catch (DatabaseException e) {
/* 154 */           log.warn("Caught exception closing cursor: " + e, (Throwable)e);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() throws JMSException {}
/*     */ 
/*     */   
/*     */   public void stop() throws JMSException {
/* 166 */     JMSException firstException = BDbPersistenceAdapter.closeDatabase((Database)this.secondaryDatabase, null);
/* 167 */     firstException = BDbPersistenceAdapter.closeDatabase(this.database, firstException);
/*     */     
/* 169 */     this.secondaryDatabase = null;
/* 170 */     this.database = null;
/*     */     
/* 172 */     if (firstException != null) {
/* 173 */       throw firstException;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SecondaryDatabase getSecondaryDatabase() {
/* 180 */     return this.secondaryDatabase;
/*     */   }
/*     */   
/*     */   protected Database getDatabase() {
/* 184 */     return this.database;
/*     */   }
/*     */   
/*     */   public CursorConfig getCursorConfig() {
/* 188 */     return this.cursorConfig;
/*     */   }
/*     */   
/*     */   public MessageContainer getContainer() {
/* 192 */     return this.container;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void checkClosed() throws AlreadyClosedException {
/* 197 */     if (this.database == null) {
/* 198 */       throw new AlreadyClosedException("Berkeley DB MessageStore");
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
/*     */   protected DatabaseEntry getSequenceNumberKey(MessageIdentity identity) throws DatabaseException {
/* 212 */     DatabaseEntry sequenceNumber = (DatabaseEntry)identity.getSequenceNumber();
/* 213 */     if (sequenceNumber == null) {
/* 214 */       sequenceNumber = findSequenceNumber(identity.getMessageID());
/*     */     }
/* 216 */     return sequenceNumber;
/*     */   }
/*     */   
/*     */   protected DatabaseEntry createKey(String messageID) {
/* 220 */     DatabaseEntry key = new DatabaseEntry(asBytes(messageID));
/* 221 */     return key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DatabaseEntry findSequenceNumber(String messageID) throws DatabaseException {
/* 232 */     log.warn("Having to table scan to find the sequence number for messageID: " + messageID);
/*     */     
/* 234 */     SecondaryCursor cursor = null;
/*     */     try {
/* 236 */       cursor = this.secondaryDatabase.openSecondaryCursor(BDbHelper.getTransaction(), this.cursorConfig);
/* 237 */       DatabaseEntry sequenceNumberEntry = new DatabaseEntry();
/* 238 */       DatabaseEntry keyEntry = new DatabaseEntry();
/* 239 */       DatabaseEntry valueEntry = new DatabaseEntry();
/* 240 */       OperationStatus status = cursor.getFirst(sequenceNumberEntry, keyEntry, valueEntry, LockMode.DEFAULT);
/* 241 */       while (status == OperationStatus.SUCCESS) {
/* 242 */         String value = extractString(keyEntry);
/* 243 */         if (messageID.equals(value)) {
/* 244 */           return sequenceNumberEntry;
/*     */         }
/* 246 */         status = cursor.getNext(sequenceNumberEntry, keyEntry, valueEntry, LockMode.DEFAULT);
/*     */       } 
/*     */     } finally {
/*     */       
/* 250 */       if (cursor != null) {
/*     */         try {
/* 252 */           cursor.close();
/*     */         }
/* 254 */         catch (DatabaseException e) {
/* 255 */           log.warn("Caught exception closing cursor: " + e, (Throwable)e);
/*     */         } 
/*     */       }
/*     */     } 
/* 259 */     return null;
/*     */   }
/*     */   
/*     */   protected String extractString(DatabaseEntry entry) {
/* 263 */     return new String(entry.getData(), entry.getOffset(), entry.getSize());
/*     */   }
/*     */ 
/*     */   
/*     */   protected ActiveMQMessage extractMessage(DatabaseEntry value) throws IOException {
/* 268 */     synchronized (this.wireFormat) {
/* 269 */       return (ActiveMQMessage)this.wireFormat.fromBytes(value.getData(), value.getOffset(), value.getSize());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] asBytes(ActiveMQMessage message) throws IOException, JMSException {
/* 275 */     synchronized (this.wireFormat) {
/* 276 */       return this.wireFormat.toBytes((Packet)message);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected byte[] asBytes(String messageID) {
/* 281 */     return messageID.getBytes();
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\bdb\BDbMessageStore.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */