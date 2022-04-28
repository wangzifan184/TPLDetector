/*     */ package org.codehaus.activemq.store.jdbm;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.jms.JMSException;
/*     */ import jdbm.btree.BTree;
/*     */ import jdbm.helper.Tuple;
/*     */ import jdbm.helper.TupleBrowser;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.AlreadyClosedException;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.MessageAck;
/*     */ import org.codehaus.activemq.service.MessageContainer;
/*     */ import org.codehaus.activemq.service.MessageIdentity;
/*     */ import org.codehaus.activemq.service.QueueMessageContainer;
/*     */ import org.codehaus.activemq.service.impl.MessageEntry;
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
/*     */ public class JdbmMessageStore
/*     */   implements MessageStore
/*     */ {
/*  42 */   private static final Log log = LogFactory.getLog(JdbmMessageStore.class);
/*     */   
/*     */   private MessageContainer container;
/*     */   private BTree messageTable;
/*     */   private BTree orderedIndex;
/*  47 */   private long lastSequenceNumber = 0L;
/*     */   
/*     */   public JdbmMessageStore(BTree messageTable, BTree orderedIndex) {
/*  50 */     this.messageTable = messageTable;
/*  51 */     this.orderedIndex = orderedIndex;
/*     */   }
/*     */   
/*     */   public void setMessageContainer(MessageContainer container) {
/*  55 */     this.container = container;
/*     */   }
/*     */   
/*     */   public synchronized MessageIdentity addMessage(ActiveMQMessage message) throws JMSException {
/*  59 */     if (log.isDebugEnabled()) {
/*  60 */       log.debug("Adding message to container: " + message);
/*     */     }
/*  62 */     MessageEntry entry = new MessageEntry(message);
/*  63 */     Object sequenceNumber = null;
/*  64 */     synchronized (this) {
/*  65 */       sequenceNumber = new Long(++this.lastSequenceNumber);
/*     */     } 
/*     */     try {
/*  68 */       String messageID = message.getJMSMessageID();
/*  69 */       getMessageTable().insert(messageID, entry, true);
/*  70 */       getOrderedIndex().insert(sequenceNumber, messageID, true);
/*     */       
/*  72 */       MessageIdentity answer = message.getJMSMessageIdentity();
/*  73 */       answer.setSequenceNumber(sequenceNumber);
/*  74 */       return answer;
/*     */     }
/*  76 */     catch (IOException e) {
/*  77 */       throw JMSExceptionHelper.newJMSException("Failed to add message: " + message + " in container: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized ActiveMQMessage getMessage(MessageIdentity identity) throws JMSException {
/*  82 */     String messageID = identity.getMessageID();
/*  83 */     ActiveMQMessage message = null;
/*     */     try {
/*  85 */       MessageEntry entry = (MessageEntry)getMessageTable().find(messageID);
/*  86 */       if (entry != null) {
/*  87 */         message = entry.getMessage();
/*  88 */         message.getJMSMessageIdentity().setSequenceNumber(identity.getSequenceNumber());
/*     */       }
/*     */     
/*  91 */     } catch (IOException e) {
/*  92 */       throw JMSExceptionHelper.newJMSException("Failed to get message for messageID: " + messageID + " " + e, e);
/*     */     } 
/*  94 */     return message;
/*     */   }
/*     */   
/*     */   public synchronized void removeMessage(MessageIdentity identity, MessageAck ack) throws JMSException {
/*  98 */     String messageID = identity.getMessageID();
/*  99 */     Object sequenceNumber = null;
/* 100 */     if (messageID == null) {
/* 101 */       throw new JMSException("Cannot remove message with null messageID for sequence number: " + identity.getSequenceNumber());
/*     */     }
/*     */     try {
/* 104 */       sequenceNumber = identity.getSequenceNumber();
/* 105 */       if (sequenceNumber == null) {
/* 106 */         sequenceNumber = findSequenceNumber(messageID);
/* 107 */         identity.setSequenceNumber(sequenceNumber);
/*     */       } 
/* 109 */       getMessageTable().remove(messageID);
/* 110 */       getOrderedIndex().remove(sequenceNumber);
/*     */     }
/* 112 */     catch (IOException e) {
/* 113 */       throw JMSExceptionHelper.newJMSException("Failed to delete message for messageID: " + messageID + " " + e, e);
/*     */     }
/* 115 */     catch (IllegalArgumentException e) {
/* 116 */       log.warn("Could not find sequence number: " + sequenceNumber + " in queue. " + e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void recover(QueueMessageContainer container) throws JMSException {
/*     */     try {
/* 122 */       Tuple tuple = new Tuple();
/* 123 */       TupleBrowser iter = getOrderedIndex().browse();
/* 124 */       while (iter.getNext(tuple)) {
/* 125 */         Long key = (Long)tuple.getKey();
/* 126 */         MessageIdentity messageIdentity = null;
/* 127 */         if (key != null) {
/* 128 */           String messageID = (String)tuple.getValue();
/* 129 */           if (messageID != null) {
/* 130 */             messageIdentity = new MessageIdentity(messageID, key);
/*     */           }
/*     */         } 
/* 133 */         if (messageIdentity != null) {
/* 134 */           container.recoverMessageToBeDelivered(messageIdentity);
/*     */           continue;
/*     */         } 
/* 137 */         log.warn("Could not find message for sequenceNumber: " + key);
/*     */       }
/*     */     
/*     */     }
/* 141 */     catch (IOException e) {
/* 142 */       throw JMSExceptionHelper.newJMSException("Failed to recover the durable queue store. Reason: " + e, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void start() throws JMSException {
/*     */     try {
/* 149 */       Tuple tuple = new Tuple();
/* 150 */       Long lastSequenceNumber = null;
/* 151 */       TupleBrowser iter = getOrderedIndex().browse();
/* 152 */       while (iter.getNext(tuple)) {
/* 153 */         lastSequenceNumber = (Long)tuple.getKey();
/*     */       }
/* 155 */       if (lastSequenceNumber != null) {
/* 156 */         this.lastSequenceNumber = lastSequenceNumber.longValue();
/* 157 */         if (log.isDebugEnabled()) {
/* 158 */           log.debug("Last sequence number is: " + lastSequenceNumber + " for: " + this);
/*     */         
/*     */         }
/*     */       }
/* 162 */       else if (log.isDebugEnabled()) {
/* 163 */         log.debug("Started empty database for: " + this);
/*     */       }
/*     */     
/*     */     }
/* 167 */     catch (IOException e) {
/* 168 */       throw JMSExceptionHelper.newJMSException("Failed to find the last sequence number. Reason: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void stop() throws JMSException {
/* 173 */     JMSException firstException = closeTable(this.orderedIndex, null);
/* 174 */     firstException = closeTable(this.messageTable, firstException);
/* 175 */     this.orderedIndex = null;
/* 176 */     this.messageTable = null;
/* 177 */     if (firstException != null) {
/* 178 */       throw firstException;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MessageContainer getContainer() {
/* 187 */     return this.container;
/*     */   }
/*     */   
/*     */   protected long getLastSequenceNumber() {
/* 191 */     return this.lastSequenceNumber;
/*     */   }
/*     */   
/*     */   protected BTree getMessageTable() throws AlreadyClosedException {
/* 195 */     if (this.messageTable == null) {
/* 196 */       throw new AlreadyClosedException("JDBM MessageStore");
/*     */     }
/* 198 */     return this.messageTable;
/*     */   }
/*     */   
/*     */   protected BTree getOrderedIndex() throws AlreadyClosedException {
/* 202 */     if (this.orderedIndex == null) {
/* 203 */       throw new AlreadyClosedException("JDBM MessageStore");
/*     */     }
/* 205 */     return this.orderedIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ActiveMQMessage getMessageBySequenceNumber(Long sequenceNumber) throws IOException, JMSException {
/* 213 */     ActiveMQMessage message = null;
/* 214 */     String messageID = (String)getOrderedIndex().find(sequenceNumber);
/* 215 */     if (messageID != null) {
/* 216 */       message = getMessage(new MessageIdentity(messageID, sequenceNumber));
/*     */     }
/* 218 */     return message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object findSequenceNumber(String messageID) throws IOException, AlreadyClosedException {
/* 228 */     log.warn("Having to table scan to find the sequence number for messageID: " + messageID);
/*     */     
/* 230 */     Tuple tuple = new Tuple();
/* 231 */     TupleBrowser iter = getOrderedIndex().browse();
/* 232 */     while (iter.getNext(tuple)) {
/* 233 */       Object value = tuple.getValue();
/* 234 */       if (messageID.equals(value)) {
/* 235 */         return tuple.getKey();
/*     */       }
/*     */     } 
/* 238 */     return null;
/*     */   }
/*     */   
/*     */   protected JMSException closeTable(BTree table, JMSException firstException) {
/* 242 */     table = null;
/* 243 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\jdbm\JdbmMessageStore.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */