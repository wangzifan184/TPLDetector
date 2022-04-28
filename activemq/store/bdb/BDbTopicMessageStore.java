/*     */ package org.codehaus.activemq.store.bdb;
/*     */ 
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
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.ConsumerInfo;
/*     */ import org.codehaus.activemq.message.MessageAck;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.service.MessageIdentity;
/*     */ import org.codehaus.activemq.service.SubscriberEntry;
/*     */ import org.codehaus.activemq.service.Subscription;
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
/*     */ public class BDbTopicMessageStore
/*     */   extends BDbMessageStore
/*     */   implements TopicMessageStore
/*     */ {
/*  48 */   private static final Log log = LogFactory.getLog(BDbTopicMessageStore.class);
/*     */   
/*     */   private Database subscriptionDatabase;
/*     */   
/*     */   public BDbTopicMessageStore(Database database, SecondaryDatabase secondaryDatabase, SecondaryConfig secondaryConfig, SequenceNumberCreator sequenceNumberCreator, WireFormat wireFormat, Database subscriptionDatabase) {
/*  53 */     super(database, secondaryDatabase, secondaryConfig, sequenceNumberCreator, wireFormat);
/*  54 */     this.subscriptionDatabase = subscriptionDatabase;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void incrementMessageCount(MessageIdentity messageId) {}
/*     */ 
/*     */   
/*     */   public void decrementMessageCountAndMaybeDelete(MessageIdentity messageIdentity, MessageAck ack) {}
/*     */ 
/*     */   
/*     */   public void setLastAcknowledgedMessageIdentity(Subscription subscription, MessageIdentity messageIdentity) throws JMSException {
/*  66 */     checkClosed();
/*     */     try {
/*  68 */       doSetLastAcknowledgedMessageIdentity(subscription, messageIdentity);
/*     */     }
/*  70 */     catch (DatabaseException e) {
/*  71 */       throw JMSExceptionHelper.newJMSException("Failed to update last acknowledge messageID for : " + messageIdentity + ". Reason: " + e, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void recoverSubscription(Subscription subscription, MessageIdentity lastDispatchedMessage) throws JMSException {
/*  78 */     checkClosed();
/*  79 */     SecondaryCursor cursor = null;
/*     */     try {
/*  81 */       DatabaseEntry lastAckKey = getLastAcknowledgedMessageID(subscription, lastDispatchedMessage);
/*  82 */       if (lastAckKey != null) {
/*  83 */         cursor = getSecondaryDatabase().openSecondaryCursor(BDbHelper.getTransaction(), getCursorConfig());
/*  84 */         DatabaseEntry valueEntry = new DatabaseEntry();
/*  85 */         OperationStatus status = cursor.getSearchKey(lastAckKey, valueEntry, LockMode.DEFAULT);
/*  86 */         if (status != OperationStatus.SUCCESS) {
/*  87 */           log.error("Could not find the last acknowledged record for: " + subscription + ". Status: " + status);
/*     */         } else {
/*     */           
/*     */           while (true)
/*     */           {
/*  92 */             status = cursor.getNext(lastAckKey, valueEntry, LockMode.DEFAULT);
/*  93 */             if (status != OperationStatus.SUCCESS) {
/*  94 */               if (status != OperationStatus.NOTFOUND) {
/*  95 */                 log.warn("Strange result when iterating to end of collection: " + status);
/*     */               }
/*     */               
/*     */               break;
/*     */             } 
/* 100 */             ActiveMQMessage message = extractMessage(valueEntry);
/* 101 */             subscription.addMessage(getContainer(), message);
/*     */           }
/*     */         
/*     */         } 
/*     */       } 
/* 106 */     } catch (DatabaseException e) {
/* 107 */       throw JMSExceptionHelper.newJMSException("Unable to recover topic subscription for: " + subscription + ". Reason: " + e, e);
/*     */     
/*     */     }
/* 110 */     catch (IOException e) {
/* 111 */       throw JMSExceptionHelper.newJMSException("Unable to recover topic subscription for: " + subscription + ". Reason: " + e, e);
/*     */     }
/*     */     finally {
/*     */       
/* 115 */       if (cursor != null) {
/*     */         try {
/* 117 */           cursor.close();
/*     */         }
/* 119 */         catch (DatabaseException e) {
/* 120 */           log.warn("Caught exception closing cursor: " + e, (Throwable)e);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public MessageIdentity getLastestMessageIdentity() throws JMSException {
/* 127 */     checkClosed();
/* 128 */     SecondaryCursor cursor = null;
/*     */     try {
/* 130 */       cursor = getSecondaryDatabase().openSecondaryCursor(BDbHelper.getTransaction(), getCursorConfig());
/* 131 */       DatabaseEntry keyEntry = new DatabaseEntry();
/* 132 */       DatabaseEntry valueEntry = new DatabaseEntry();
/* 133 */       OperationStatus status = cursor.getLast(keyEntry, valueEntry, LockMode.DEFAULT);
/* 134 */       if (status == OperationStatus.SUCCESS) {
/* 135 */         if (log.isDebugEnabled()) {
/* 136 */           log.debug("Loaded last sequence number of: " + BDbHelper.longFromBytes(keyEntry.getData()));
/*     */         }
/* 138 */         return new MessageIdentity(null, keyEntry);
/*     */       } 
/* 140 */       if (status != OperationStatus.NOTFOUND) {
/* 141 */         log.error("Could not find the last sequence number. Status: " + status);
/*     */       }
/* 143 */       return null;
/*     */     }
/* 145 */     catch (DatabaseException e) {
/* 146 */       throw JMSExceptionHelper.newJMSException("Unable to load the last sequence number. Reason: " + e, e);
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
/*     */   public SubscriberEntry getSubscriberEntry(ConsumerInfo info) throws JMSException {
/* 161 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSubscriberEntry(ConsumerInfo info, SubscriberEntry subscriberEntry) throws JMSException {}
/*     */ 
/*     */   
/*     */   public synchronized void stop() throws JMSException {
/* 169 */     JMSException firstException = BDbPersistenceAdapter.closeDatabase(this.subscriptionDatabase, null);
/* 170 */     this.subscriptionDatabase = null;
/* 171 */     super.stop();
/* 172 */     if (firstException != null) {
/* 173 */       throw JMSExceptionHelper.newJMSException("Unable to close the subscription database: " + firstException, firstException);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DatabaseEntry getLastAcknowledgedMessageID(Subscription subscription, MessageIdentity lastDispatchedMessage) throws DatabaseException {
/* 181 */     DatabaseEntry key = createKey(subscription.getPersistentKey());
/* 182 */     DatabaseEntry value = new DatabaseEntry();
/* 183 */     OperationStatus status = this.subscriptionDatabase.get(null, key, value, null);
/* 184 */     if (status == OperationStatus.SUCCESS) {
/* 185 */       return value;
/*     */     }
/* 187 */     if (status == OperationStatus.NOTFOUND) {
/*     */       
/* 189 */       if (lastDispatchedMessage != null) {
/* 190 */         return doSetLastAcknowledgedMessageIdentity(subscription, lastDispatchedMessage);
/*     */       }
/*     */     } else {
/*     */       
/* 194 */       log.warn("Unexpected status return from querying lastAcknowledgeSequenceNumber for: " + subscription + " status: " + status);
/*     */     } 
/* 196 */     return null;
/*     */   }
/*     */   
/*     */   protected DatabaseEntry doSetLastAcknowledgedMessageIdentity(Subscription subscription, MessageIdentity messageIdentity) throws DatabaseException {
/* 200 */     Transaction transaction = BDbHelper.getTransaction();
/* 201 */     DatabaseEntry key = createKey(subscription.getPersistentKey());
/* 202 */     DatabaseEntry value = getSequenceNumberKey(messageIdentity);
/* 203 */     this.subscriptionDatabase.put(transaction, key, value);
/* 204 */     return value;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\bdb\BDbTopicMessageStore.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */