/*     */ package org.codehaus.activemq.service.impl;
/*     */ 
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.MessageAck;
/*     */ import org.codehaus.activemq.service.MessageIdentity;
/*     */ import org.codehaus.activemq.service.QueueList;
/*     */ import org.codehaus.activemq.service.QueueListEntry;
/*     */ import org.codehaus.activemq.service.QueueMessageContainer;
/*     */ import org.codehaus.activemq.store.MessageStore;
/*     */ import org.codehaus.activemq.store.PersistenceAdapter;
/*     */ import org.codehaus.activemq.util.Callback;
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
/*     */ public class DurableQueueMessageContainer
/*     */   implements QueueMessageContainer
/*     */ {
/*  44 */   private static final Log log = LogFactory.getLog(DurableQueueMessageContainer.class);
/*     */ 
/*     */   
/*     */   private MessageStore messageStore;
/*     */   
/*     */   private String destinationName;
/*     */   
/*     */   private QueueList messagesToBeDelivered;
/*     */   
/*     */   private QueueList deliveredMessages;
/*     */   
/*     */   private PersistenceAdapter persistenceAdapter;
/*     */   
/*     */   private TransactionTemplate transactionTemplate;
/*     */ 
/*     */   
/*     */   public DurableQueueMessageContainer(PersistenceAdapter persistenceAdapter, MessageStore messageStore, String destinationName) {
/*  61 */     this(persistenceAdapter, messageStore, destinationName, new DefaultQueueList(), new DefaultQueueList());
/*     */   }
/*     */   
/*     */   public DurableQueueMessageContainer(PersistenceAdapter persistenceAdapter, MessageStore messageStore, String destinationName, QueueList messagesToBeDelivered, QueueList deliveredMessages) {
/*  65 */     this.persistenceAdapter = persistenceAdapter;
/*  66 */     this.messageStore = messageStore;
/*  67 */     this.destinationName = destinationName;
/*  68 */     this.messagesToBeDelivered = messagesToBeDelivered;
/*  69 */     this.deliveredMessages = deliveredMessages;
/*  70 */     this.transactionTemplate = new TransactionTemplate(persistenceAdapter);
/*     */   }
/*     */   
/*     */   public String getDestinationName() {
/*  74 */     return this.destinationName;
/*     */   }
/*     */   
/*     */   public MessageIdentity addMessage(ActiveMQMessage message) throws JMSException {
/*  78 */     MessageIdentity answer = this.messageStore.addMessage(message);
/*  79 */     synchronized (this) {
/*  80 */       this.messagesToBeDelivered.add(answer);
/*     */     } 
/*  82 */     return answer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void delete(MessageIdentity messageID, MessageAck ack) throws JMSException {
/*  90 */     MessageIdentity storedIdentity = null;
/*     */     
/*  92 */     synchronized (this) {
/*  93 */       QueueListEntry entry = this.deliveredMessages.getFirstEntry();
/*  94 */       while (entry != null) {
/*  95 */         MessageIdentity identity = (MessageIdentity)entry.getElement();
/*  96 */         if (messageID.equals(identity)) {
/*  97 */           this.deliveredMessages.remove(entry);
/*  98 */           storedIdentity = identity;
/*     */           break;
/*     */         } 
/* 101 */         entry = this.deliveredMessages.getNextEntry(entry);
/*     */       } 
/*     */       
/* 104 */       if (storedIdentity == null) {
/*     */ 
/*     */         
/* 107 */         entry = this.messagesToBeDelivered.getFirstEntry();
/* 108 */         while (entry != null) {
/* 109 */           MessageIdentity identity = (MessageIdentity)entry.getElement();
/* 110 */           if (messageID.equals(identity)) {
/* 111 */             this.messagesToBeDelivered.remove(entry);
/* 112 */             storedIdentity = identity;
/*     */             break;
/*     */           } 
/* 115 */           entry = this.messagesToBeDelivered.getNextEntry(entry);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 120 */     if (storedIdentity == null) {
/* 121 */       log.error("Attempt to acknowledge unknown messageID: " + messageID);
/*     */     } else {
/* 123 */       this.messageStore.removeMessage(storedIdentity, ack);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ActiveMQMessage getMessage(MessageIdentity messageID) throws JMSException {
/* 129 */     return this.messageStore.getMessage(messageID);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsMessage(MessageIdentity messageIdentity) throws JMSException {
/* 135 */     return (getMessage(messageIdentity) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerMessageInterest(MessageIdentity messageIdentity) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unregisterMessageInterest(MessageIdentity messageIdentity, MessageAck ack) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQMessage poll() throws JMSException {
/* 158 */     ActiveMQMessage message = null;
/* 159 */     MessageIdentity messageIdentity = null;
/* 160 */     synchronized (this) {
/* 161 */       messageIdentity = (MessageIdentity)this.messagesToBeDelivered.removeFirst();
/* 162 */       if (messageIdentity != null) {
/* 163 */         this.deliveredMessages.add(messageIdentity);
/*     */       }
/*     */     } 
/* 166 */     if (messageIdentity != null) {
/* 167 */       message = this.messageStore.getMessage(messageIdentity);
/*     */     }
/* 169 */     return message;
/*     */   }
/*     */   
/*     */   public ActiveMQMessage peekNext(MessageIdentity messageID) throws JMSException {
/* 173 */     ActiveMQMessage answer = null;
/* 174 */     MessageIdentity identity = null;
/* 175 */     synchronized (this) {
/* 176 */       if (messageID == null) {
/* 177 */         identity = (MessageIdentity)this.messagesToBeDelivered.getFirst();
/*     */       } else {
/*     */         
/* 180 */         int index = this.messagesToBeDelivered.indexOf(messageID);
/* 181 */         if (index >= 0 && index + 1 < this.messagesToBeDelivered.size()) {
/* 182 */           identity = (MessageIdentity)this.messagesToBeDelivered.get(index + 1);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 187 */     if (identity != null) {
/* 188 */       answer = this.messageStore.getMessage(identity);
/*     */     }
/* 190 */     return answer;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void returnMessage(MessageIdentity messageIdentity) throws JMSException {
/* 195 */     boolean result = this.deliveredMessages.remove(messageIdentity);
/* 196 */     this.messagesToBeDelivered.addFirst(messageIdentity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void reset() throws JMSException {
/* 206 */     int count = 0;
/* 207 */     MessageIdentity messageIdentity = (MessageIdentity)this.deliveredMessages.removeFirst();
/* 208 */     while (messageIdentity != null) {
/* 209 */       this.messagesToBeDelivered.add(count++, messageIdentity);
/* 210 */       messageIdentity = (MessageIdentity)this.deliveredMessages.removeFirst();
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void start() throws JMSException {
/* 215 */     final QueueMessageContainer container = this;
/* 216 */     this.transactionTemplate.run(new Callback() { private final QueueMessageContainer val$container;
/*     */           public void execute() throws Throwable {
/* 218 */             DurableQueueMessageContainer.this.messageStore.start();
/* 219 */             DurableQueueMessageContainer.this.messageStore.recover(container);
/*     */           }
/*     */           private final DurableQueueMessageContainer this$0; }
/*     */       );
/*     */   }
/*     */   
/*     */   public synchronized void recoverMessageToBeDelivered(MessageIdentity messageIdentity) throws JMSException {
/* 226 */     this.messagesToBeDelivered.add(messageIdentity);
/*     */   }
/*     */   
/*     */   public void stop() throws JMSException {
/* 230 */     this.messageStore.stop();
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\DurableQueueMessageContainer.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */