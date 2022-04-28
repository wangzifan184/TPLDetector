/*     */ package org.codehaus.activemq.store.jdbc;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.MessageAck;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.service.MessageIdentity;
/*     */ import org.codehaus.activemq.service.QueueMessageContainer;
/*     */ import org.codehaus.activemq.store.MessageStore;
/*     */ import org.codehaus.activemq.util.JMSExceptionHelper;
/*     */ import org.codehaus.activemq.util.LongSequenceGenerator;
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
/*     */ public class JDBCMessageStore
/*     */   implements MessageStore
/*     */ {
/*  42 */   private static final Log log = LogFactory.getLog(JDBCMessageStore.class);
/*     */   
/*     */   protected final WireFormat wireFormat;
/*     */   protected final String destinationName;
/*     */   protected final LongSequenceGenerator sequenceGenerator;
/*     */   protected final JDBCAdapter adapter;
/*     */   protected final JDBCPersistenceAdapter persistenceAdapter;
/*     */   
/*     */   public JDBCMessageStore(JDBCPersistenceAdapter persistenceAdapter, JDBCAdapter adapter, WireFormat wireFormat, String destinationName) {
/*  51 */     this.persistenceAdapter = persistenceAdapter;
/*  52 */     this.adapter = adapter;
/*  53 */     this.sequenceGenerator = adapter.getSequenceGenerator();
/*  54 */     this.wireFormat = wireFormat;
/*  55 */     this.destinationName = destinationName;
/*     */   }
/*     */ 
/*     */   
/*     */   public MessageIdentity addMessage(ActiveMQMessage message) throws JMSException {
/*     */     byte[] data;
/*  61 */     String messageID = message.getJMSMessageID();
/*     */     
/*     */     try {
/*  64 */       data = this.wireFormat.toBytes((Packet)message);
/*  65 */     } catch (IOException e) {
/*  66 */       throw JMSExceptionHelper.newJMSException("Failed to broker message: " + messageID + " in container: " + e, e);
/*     */     } 
/*     */     
/*  69 */     long seq = this.sequenceGenerator.getNextSequenceId();
/*     */ 
/*     */     
/*  72 */     Connection c = null;
/*     */     try {
/*  74 */       c = this.persistenceAdapter.getConnection();
/*  75 */       this.adapter.doAddMessage(c, seq, messageID, this.destinationName, data);
/*  76 */     } catch (SQLException e) {
/*  77 */       throw JMSExceptionHelper.newJMSException("Failed to broker message: " + messageID + " in container: " + e, e);
/*     */     } finally {
/*  79 */       this.persistenceAdapter.returnConnection(c);
/*     */     } 
/*     */     
/*  82 */     MessageIdentity answer = message.getJMSMessageIdentity();
/*  83 */     answer.setSequenceNumber(new Long(seq));
/*  84 */     return answer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQMessage getMessage(MessageIdentity identity) throws JMSException {
/*  90 */     long id = getMessageSequenceId(identity);
/*     */ 
/*     */     
/*  93 */     Connection c = null;
/*     */     try {
/*  95 */       c = this.persistenceAdapter.getConnection();
/*  96 */       byte[] data = this.adapter.doGetMessage(c, id);
/*  97 */       ActiveMQMessage answer = (ActiveMQMessage)this.wireFormat.fromBytes(data);
/*  98 */       answer.setJMSMessageIdentity(identity);
/*  99 */       return answer;
/* 100 */     } catch (IOException e) {
/* 101 */       throw JMSExceptionHelper.newJMSException("Failed to broker message: " + identity.getMessageID() + " in container: " + e, e);
/* 102 */     } catch (SQLException e) {
/* 103 */       throw JMSExceptionHelper.newJMSException("Failed to broker message: " + identity.getMessageID() + " in container: " + e, e);
/*     */     } finally {
/* 105 */       this.persistenceAdapter.returnConnection(c);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long getMessageSequenceId(MessageIdentity identity) throws JMSException {
/* 115 */     Object sequenceNumber = identity.getSequenceNumber();
/* 116 */     if (sequenceNumber != null && sequenceNumber.getClass() == Long.class) {
/* 117 */       return ((Long)sequenceNumber).longValue();
/*     */     }
/*     */     
/* 120 */     Connection c = null;
/*     */     try {
/* 122 */       c = this.persistenceAdapter.getConnection();
/* 123 */       Long rc = this.adapter.getMessageSequenceId(c, identity.getMessageID());
/* 124 */       if (rc == null)
/* 125 */         throw new JMSException("Could not locate message in database with message id: " + identity.getMessageID()); 
/* 126 */       return rc.longValue();
/* 127 */     } catch (SQLException e) {
/* 128 */       throw JMSExceptionHelper.newJMSException("Failed to broker message: " + identity.getMessageID() + " in container: " + e, e);
/*     */     } finally {
/* 130 */       this.persistenceAdapter.returnConnection(c);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeMessage(MessageIdentity identity, MessageAck ack) throws JMSException {
/* 136 */     long seq = getMessageSequenceId(identity);
/*     */ 
/*     */     
/* 139 */     Connection c = null;
/*     */     try {
/* 141 */       c = this.persistenceAdapter.getConnection();
/* 142 */       this.adapter.doRemoveMessage(c, seq);
/* 143 */     } catch (SQLException e) {
/* 144 */       throw JMSExceptionHelper.newJMSException("Failed to broker message: " + identity.getMessageID() + " in container: " + e, e);
/*     */     } finally {
/* 146 */       this.persistenceAdapter.returnConnection(c);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void recover(final QueueMessageContainer container) throws JMSException {
/* 154 */     Connection c = null;
/*     */     try {
/* 156 */       c = this.persistenceAdapter.getConnection();
/* 157 */       this.adapter.doRecover(c, this.destinationName, new JDBCAdapter.MessageListResultHandler() { private final QueueMessageContainer val$container;
/*     */             public void onMessage(long seq, String messageID) throws JMSException {
/* 159 */               container.recoverMessageToBeDelivered(new MessageIdentity(messageID, new Long(seq)));
/*     */             }
/*     */             private final JDBCMessageStore this$0; }
/*     */         );
/* 163 */     } catch (SQLException e) {
/* 164 */       throw JMSExceptionHelper.newJMSException("Failed to recover container. Reason: " + e, e);
/*     */     } finally {
/* 166 */       this.persistenceAdapter.returnConnection(c);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void start() throws JMSException {}
/*     */   
/*     */   public void stop() throws JMSException {}
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\jdbc\JDBCMessageStore.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */