/*     */ package org.codehaus.activemq.store.bdbn;
/*     */ 
/*     */ import com.sleepycat.db.Db;
/*     */ import com.sleepycat.db.DbException;
/*     */ import com.sleepycat.db.DbTxn;
/*     */ import com.sleepycat.db.Dbt;
/*     */ import java.io.IOException;
/*     */ import javax.jms.JMSException;
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
/*     */ public class BDbMessageStore
/*     */   implements MessageStore
/*     */ {
/*     */   private static final int SUCCESS = 0;
/*     */   private Db database;
/*     */   private WireFormat wireFormat;
/*     */   private MessageContainer container;
/*     */   
/*     */   public void setMessageContainer(MessageContainer container) {
/*  50 */     this.container = container;
/*     */   }
/*     */   
/*     */   public MessageIdentity addMessage(ActiveMQMessage message) throws JMSException {
/*  54 */     String messageID = message.getJMSMessageID();
/*     */     try {
/*  56 */       Dbt key = createKey(messageID);
/*  57 */       Dbt value = new Dbt(asBytes(message));
/*  58 */       this.database.put(BDbHelper.getTransaction(), key, value, 0);
/*  59 */       return new MessageIdentity(messageID);
/*     */     }
/*  61 */     catch (DbException e) {
/*  62 */       throw JMSExceptionHelper.newJMSException("Failed to broker message: " + messageID + " in container: " + e, e);
/*     */     }
/*  64 */     catch (IOException e) {
/*  65 */       throw JMSExceptionHelper.newJMSException("Failed to broker message: " + messageID + " in container: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public ActiveMQMessage getMessage(MessageIdentity identity) throws JMSException {
/*  70 */     String messageID = identity.getMessageID();
/*  71 */     ActiveMQMessage answer = null;
/*     */     try {
/*  73 */       Dbt key = createKey(messageID);
/*  74 */       Dbt value = new Dbt();
/*  75 */       if (this.database.get(null, key, value, 0) == 0) {
/*  76 */         answer = extractMessage(value);
/*     */       }
/*  78 */       return answer;
/*     */     }
/*  80 */     catch (DbException e) {
/*  81 */       throw JMSExceptionHelper.newJMSException("Failed to peek next message after: " + messageID + " from container: " + e, e);
/*     */     }
/*  83 */     catch (IOException e) {
/*  84 */       throw JMSExceptionHelper.newJMSException("Failed to peek next message after: " + messageID + " from container: " + e, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeMessage(MessageIdentity identity, MessageAck ack) throws JMSException {
/*  90 */     DbTxn transaction = null;
/*  91 */     String messageID = identity.getMessageID();
/*     */     try {
/*  93 */       this.database.delete(BDbHelper.getTransaction(), createKey(messageID), 0);
/*     */     }
/*  95 */     catch (DbException e) {
/*  96 */       throw JMSExceptionHelper.newJMSException("Failed to delete message: " + messageID + " from container: " + e, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void recover(QueueMessageContainer container) throws JMSException {}
/*     */ 
/*     */   
/*     */   public void start() throws JMSException {}
/*     */ 
/*     */   
/*     */   public void stop() throws JMSException {
/*     */     try {
/* 110 */       this.database.close(0);
/*     */     }
/* 112 */     catch (DbException e) {
/* 113 */       throw JMSExceptionHelper.newJMSException("Failed to close MessageStore. Reason: " + e, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Dbt createKey(String messageID) {
/* 121 */     Dbt key = new Dbt(asBytes(messageID));
/* 122 */     return key;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ActiveMQMessage extractMessage(Dbt value) throws IOException {
/* 127 */     synchronized (this.wireFormat) {
/* 128 */       return (ActiveMQMessage)this.wireFormat.fromBytes(value.getData());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte[] asBytes(ActiveMQMessage message) throws IOException, JMSException {
/* 135 */     synchronized (this.wireFormat) {
/* 136 */       return this.wireFormat.toBytes((Packet)message);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected byte[] asBytes(String messageID) {
/* 141 */     return messageID.getBytes();
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\bdbn\BDbMessageStore.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */