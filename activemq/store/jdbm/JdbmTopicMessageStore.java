/*     */ package org.codehaus.activemq.store.jdbm;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.jms.JMSException;
/*     */ import jdbm.btree.BTree;
/*     */ import jdbm.helper.Tuple;
/*     */ import jdbm.helper.TupleBrowser;
/*     */ import org.codehaus.activemq.AlreadyClosedException;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.ConsumerInfo;
/*     */ import org.codehaus.activemq.message.MessageAck;
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
/*     */ public class JdbmTopicMessageStore
/*     */   extends JdbmMessageStore
/*     */   implements TopicMessageStore
/*     */ {
/*  40 */   private static final Integer ONE = new Integer(1);
/*     */   
/*     */   private BTree ackDatabase;
/*     */   private BTree messageCounts;
/*     */   private BTree subscriberDetails;
/*     */   
/*     */   public JdbmTopicMessageStore(BTree messageTable, BTree orderedIndex, BTree ackDatabase, BTree subscriberDetails, BTree messageCounts) {
/*  47 */     super(messageTable, orderedIndex);
/*  48 */     this.ackDatabase = ackDatabase;
/*  49 */     this.subscriberDetails = subscriberDetails;
/*  50 */     this.messageCounts = messageCounts;
/*     */   }
/*     */   
/*     */   public synchronized void incrementMessageCount(MessageIdentity messageId) throws JMSException {
/*     */     try {
/*  55 */       Integer number = (Integer)getMessageCounts().find(messageId);
/*  56 */       if (number == null) {
/*  57 */         number = ONE;
/*     */       } else {
/*     */         
/*  60 */         number = new Integer(number.intValue() + 1);
/*     */       } 
/*  62 */       getMessageCounts().insert(messageId, number, true);
/*     */     }
/*  64 */     catch (IOException e) {
/*  65 */       throw JMSExceptionHelper.newJMSException("Failed to increment messageCount for  messageID: " + messageId + ". Reason: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void decrementMessageCountAndMaybeDelete(MessageIdentity messageIdentity, MessageAck ack) throws JMSException {
/*     */     try {
/*  71 */       Integer number = (Integer)getMessageCounts().find(messageIdentity);
/*  72 */       if (number == null || number.intValue() <= 1) {
/*  73 */         removeMessage(messageIdentity, ack);
/*  74 */         if (number != null) {
/*  75 */           getMessageCounts().remove(messageIdentity);
/*     */         }
/*     */       } else {
/*     */         
/*  79 */         getMessageCounts().insert(messageIdentity, new Integer(number.intValue() - 1), true);
/*  80 */         number = ONE;
/*     */       }
/*     */     
/*  83 */     } catch (IOException e) {
/*  84 */       throw JMSExceptionHelper.newJMSException("Failed to increment messageCount for  messageID: " + messageIdentity + ". Reason: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void setLastAcknowledgedMessageIdentity(Subscription subscription, MessageIdentity messageIdentity) throws JMSException {
/*  89 */     String key = subscription.getPersistentKey();
/*     */     try {
/*  91 */       getAckDatabase().insert(key, messageIdentity, true);
/*     */     }
/*  93 */     catch (IOException e) {
/*  94 */       throw JMSExceptionHelper.newJMSException("Failed to set ack messageID: " + messageIdentity + " for consumerId: " + key + ". Reason: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void recoverSubscription(Subscription subscription, MessageIdentity lastDispatchedMessage) throws JMSException {
/*     */     try {
/* 100 */       MessageIdentity lastAcked = getLastAcknowledgedMessageIdentity(subscription);
/* 101 */       if (lastAcked == null) {
/*     */ 
/*     */ 
/*     */         
/* 105 */         setLastAcknowledgedMessageIdentity(subscription, lastDispatchedMessage);
/*     */         return;
/*     */       } 
/* 108 */       Object lastAckedSequenceNumber = lastAcked.getSequenceNumber();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 113 */       Tuple tuple = getOrderedIndex().findGreaterOrEqual(lastAckedSequenceNumber);
/*     */       
/* 115 */       TupleBrowser iter = getOrderedIndex().browse();
/* 116 */       while (iter.getNext(tuple)) {
/* 117 */         Long sequenceNumber = (Long)tuple.getKey();
/* 118 */         if (sequenceNumber.compareTo(lastAckedSequenceNumber) > 0) {
/* 119 */           ActiveMQMessage message = null;
/*     */ 
/*     */           
/* 122 */           message = getMessageBySequenceNumber(sequenceNumber);
/* 123 */           if (message != null) {
/* 124 */             subscription.addMessage(getContainer(), message);
/*     */           }
/*     */         }
/*     */       
/*     */       } 
/* 129 */     } catch (IOException e) {
/* 130 */       throw JMSExceptionHelper.newJMSException("Failed to recover subscription: " + subscription + ". Reason: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized MessageIdentity getLastestMessageIdentity() throws JMSException {
/* 135 */     return new MessageIdentity(null, new Long(getLastSequenceNumber()));
/*     */   }
/*     */   
/*     */   public SubscriberEntry getSubscriberEntry(ConsumerInfo info) throws JMSException {
/* 139 */     Object key = info.getConsumerKey();
/*     */     try {
/* 141 */       return (SubscriberEntry)this.subscriberDetails.find(key);
/*     */     }
/* 143 */     catch (IOException e) {
/* 144 */       throw JMSExceptionHelper.newJMSException("Failed to lookup subscription for info: " + info + ". Reason: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setSubscriberEntry(ConsumerInfo info, SubscriberEntry subscriberEntry) throws JMSException {
/* 149 */     Object key = info.getConsumerKey();
/*     */     try {
/* 151 */       this.subscriberDetails.insert(key, subscriberEntry, true);
/*     */     }
/* 153 */     catch (IOException e) {
/* 154 */       throw JMSExceptionHelper.newJMSException("Failed to lookup subscription for info: " + info + ". Reason: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void stop() throws JMSException {
/* 159 */     JMSException firstException = closeTable(this.ackDatabase, null);
/* 160 */     firstException = closeTable(this.messageCounts, firstException);
/* 161 */     this.ackDatabase = null;
/* 162 */     this.messageCounts = null;
/* 163 */     super.stop();
/* 164 */     if (firstException != null) {
/* 165 */       throw firstException;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BTree getMessageCounts() throws AlreadyClosedException {
/* 172 */     if (this.messageCounts == null) {
/* 173 */       throw new AlreadyClosedException("JDBM TopicMessageStore");
/*     */     }
/* 175 */     return this.messageCounts;
/*     */   }
/*     */   
/*     */   protected BTree getAckDatabase() throws AlreadyClosedException {
/* 179 */     if (this.ackDatabase == null) {
/* 180 */       throw new AlreadyClosedException("JDBM TopicMessageStore");
/*     */     }
/* 182 */     return this.ackDatabase;
/*     */   }
/*     */   
/*     */   protected MessageIdentity getLastAcknowledgedMessageIdentity(Subscription subscription) throws IOException, AlreadyClosedException {
/* 186 */     return (MessageIdentity)getAckDatabase().find(subscription.getPersistentKey());
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\jdbm\JdbmTopicMessageStore.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */