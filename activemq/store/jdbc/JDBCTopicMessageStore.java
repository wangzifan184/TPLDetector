/*     */ package org.codehaus.activemq.store.jdbc;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.ConsumerInfo;
/*     */ import org.codehaus.activemq.message.MessageAck;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.service.MessageContainer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JDBCTopicMessageStore
/*     */   extends JDBCMessageStore
/*     */   implements TopicMessageStore
/*     */ {
/*  44 */   private static final Log log = LogFactory.getLog(JDBCTopicMessageStore.class);
/*     */   private MessageContainer container;
/*     */   
/*     */   public JDBCTopicMessageStore(JDBCPersistenceAdapter persistenceAdapter, JDBCAdapter adapter, WireFormat wireFormat, String destinationName) {
/*  48 */     super(persistenceAdapter, adapter, wireFormat, destinationName);
/*     */   }
/*     */   
/*     */   public void setLastAcknowledgedMessageIdentity(Subscription subscription, MessageIdentity messageIdentity) throws JMSException {
/*  52 */     long seq = ((Long)messageIdentity.getSequenceNumber()).longValue();
/*     */     
/*  54 */     Connection c = null;
/*     */     try {
/*  56 */       c = this.persistenceAdapter.getConnection();
/*  57 */       this.adapter.doSetLastAck(c, this.destinationName, subscription.getPersistentKey(), seq);
/*     */     }
/*  59 */     catch (SQLException e) {
/*  60 */       throw JMSExceptionHelper.newJMSException("Failed to store ack for: " + subscription + " on message " + messageIdentity + " in container: " + e, e);
/*     */     } finally {
/*     */       
/*  63 */       this.persistenceAdapter.returnConnection(c);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageIdentity getLastestMessageIdentity() throws JMSException {
/*  71 */     return new MessageIdentity(null, new Long(this.sequenceGenerator.getLastSequenceId()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void recoverSubscription(final Subscription subscription, MessageIdentity lastDispatchedMessage) throws JMSException {
/*  79 */     Connection c = null;
/*     */     try {
/*  81 */       c = this.persistenceAdapter.getConnection();
/*  82 */       this.adapter.doRecoverSubscription(c, subscription.getPersistentKey(), this.destinationName, new JDBCAdapter.MessageListResultHandler() { private final Subscription val$subscription;
/*     */             public void onMessage(long seq, String messageID) throws JMSException {
/*  84 */               MessageIdentity messageIdentity = new MessageIdentity(messageID, new Long(seq));
/*  85 */               ActiveMQMessage message = JDBCTopicMessageStore.this.getMessage(messageIdentity);
/*  86 */               subscription.addMessage(JDBCTopicMessageStore.this.container, message);
/*     */             }
/*     */             private final JDBCTopicMessageStore this$0; }
/*     */         );
/*  90 */     } catch (SQLException e) {
/*  91 */       throw JMSExceptionHelper.newJMSException("Failed to recover subscription: " + subscription + ". Reason: " + e, e);
/*     */     } finally {
/*     */       
/*  94 */       this.persistenceAdapter.returnConnection(c);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSubscriberEntry(ConsumerInfo info, SubscriberEntry subscriberEntry) throws JMSException {
/* 102 */     String key = info.getConsumerKey();
/* 103 */     Connection c = null;
/*     */     try {
/* 105 */       c = this.persistenceAdapter.getConnection();
/* 106 */       this.adapter.doSetSubscriberEntry(c, this.destinationName, key, subscriberEntry);
/*     */     }
/* 108 */     catch (SQLException e) {
/* 109 */       throw JMSExceptionHelper.newJMSException("Failed to lookup subscription for info: " + info + ". Reason: " + e, e);
/*     */     } finally {
/*     */       
/* 112 */       this.persistenceAdapter.returnConnection(c);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SubscriberEntry getSubscriberEntry(ConsumerInfo info) throws JMSException {
/* 120 */     String key = info.getConsumerKey();
/* 121 */     Connection c = null;
/*     */     try {
/* 123 */       c = this.persistenceAdapter.getConnection();
/* 124 */       return this.adapter.doGetSubscriberEntry(c, this.destinationName, key);
/*     */     }
/* 126 */     catch (SQLException e) {
/* 127 */       throw JMSExceptionHelper.newJMSException("Failed to lookup subscription for info: " + info + ". Reason: " + e, e);
/*     */     } finally {
/*     */       
/* 130 */       this.persistenceAdapter.returnConnection(c);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageContainer(MessageContainer container) {
/* 139 */     this.container = container;
/*     */   }
/*     */   
/*     */   public void incrementMessageCount(MessageIdentity messageId) throws JMSException {}
/*     */   
/*     */   public void decrementMessageCountAndMaybeDelete(MessageIdentity messageIdentity, MessageAck ack) throws JMSException {}
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\jdbc\JDBCTopicMessageStore.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */