/*     */ package org.codehaus.activemq.store.journal;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.journal.RecordLocation;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.MessageAck;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.service.MessageIdentity;
/*     */ import org.codehaus.activemq.service.QueueMessageContainer;
/*     */ import org.codehaus.activemq.store.MessageStore;
/*     */ import org.codehaus.activemq.store.PersistenceAdapter;
/*     */ import org.codehaus.activemq.store.cache.CacheMessageStore;
/*     */ import org.codehaus.activemq.store.cache.CacheMessageStoreAware;
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
/*     */ public class JournalMessageStore
/*     */   implements MessageStore, CacheMessageStoreAware
/*     */ {
/*     */   private final JournalPersistenceAdapter peristenceAdapter;
/*     */   private final MessageStore longTermStore;
/*  47 */   private static final Log log = LogFactory.getLog(JournalMessageStore.class);
/*     */   private final String destinationName;
/*     */   private final TransactionTemplate transactionTemplate;
/*     */   
/*     */   private static final class AckData {
/*     */     AckData(MessageAck ack, RecordLocation location) {
/*  53 */       this.ack = ack;
/*  54 */       this.location = location;
/*     */     }
/*     */ 
/*     */     
/*     */     private final RecordLocation location;
/*     */     
/*     */     private final MessageAck ack;
/*     */   }
/*     */   
/*  63 */   private HashMap addedMessageLocations = new HashMap();
/*  64 */   private ArrayList removedMessageLocations = new ArrayList();
/*     */ 
/*     */   
/*  67 */   private MessageStore cacheMessageStore = this;
/*     */   
/*     */   private boolean sync = true;
/*     */   
/*     */   public JournalMessageStore(JournalPersistenceAdapter adapter, MessageStore checkpointStore, String destinationName, boolean sync) {
/*  72 */     this.peristenceAdapter = adapter;
/*  73 */     this.longTermStore = checkpointStore;
/*  74 */     this.destinationName = destinationName;
/*  75 */     this.sync = sync;
/*  76 */     this.transactionTemplate = new TransactionTemplate((PersistenceAdapter)adapter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageIdentity addMessage(ActiveMQMessage message) throws JMSException {
/*  84 */     boolean sync = message.isReceiptRequired();
/*  85 */     RecordLocation location = this.peristenceAdapter.writePacket(this.destinationName, (Packet)message, sync);
/*  86 */     synchronized (this) {
/*  87 */       this.addedMessageLocations.put(message.getJMSMessageIdentity(), location);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  92 */     MessageIdentity messageIdentity = message.getJMSMessageIdentity();
/*  93 */     messageIdentity.setSequenceNumber(location);
/*  94 */     return messageIdentity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeMessage(MessageIdentity identity, MessageAck ack) throws JMSException {
/* 102 */     RecordLocation ackLocation = this.peristenceAdapter.writePacket(this.destinationName, (Packet)ack, this.sync);
/*     */     
/* 104 */     synchronized (this) {
/* 105 */       RecordLocation addLocation = (RecordLocation)this.addedMessageLocations.remove(identity);
/* 106 */       if (addLocation == null) {
/* 107 */         this.removedMessageLocations.add(new AckData(ack, ackLocation));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RecordLocation checkpoint() throws JMSException {
/*     */     final ArrayList addedMessageIdentitys, removedMessageLocations;
/* 117 */     final RecordLocation[] rc = { null };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     synchronized (this) {
/* 123 */       addedMessageIdentitys = new ArrayList(this.addedMessageLocations.keySet());
/* 124 */       removedMessageLocations = this.removedMessageLocations;
/* 125 */       this.removedMessageLocations = new ArrayList();
/*     */     } 
/*     */     
/* 128 */     this.transactionTemplate.run(new Callback() {
/*     */           private final ArrayList val$addedMessageIdentitys;
/*     */           
/*     */           public void execute() throws Throwable {
/* 132 */             Iterator iterator = addedMessageIdentitys.iterator();
/* 133 */             while (iterator.hasNext()) {
/* 134 */               MessageIdentity identity = iterator.next();
/*     */               
/* 136 */               ActiveMQMessage msg = JournalMessageStore.this.getCacheMessage(identity);
/* 137 */               JournalMessageStore.this.longTermStore.addMessage(msg);
/* 138 */               synchronized (this) {
/* 139 */                 RecordLocation location = (RecordLocation)JournalMessageStore.this.addedMessageLocations.remove(identity);
/* 140 */                 if (rc[0] == null || rc[0].compareTo(location) < 0) {
/* 141 */                   rc[0] = location;
/*     */                 }
/*     */               } 
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 148 */             iterator = removedMessageLocations.iterator();
/* 149 */             while (iterator.hasNext()) {
/* 150 */               JournalMessageStore.AckData data = (JournalMessageStore.AckData)iterator.next();
/* 151 */               JournalMessageStore.this.longTermStore.removeMessage(data.ack.getMessageIdentity(), data.ack);
/*     */               
/* 153 */               if (rc[0] == null || rc[0].compareTo(data.location) < 0)
/* 154 */                 rc[0] = data.location; 
/*     */             } 
/*     */           }
/*     */           
/*     */           private final RecordLocation[] val$rc;
/*     */           private final ArrayList val$removedMessageLocations;
/*     */           private final JournalMessageStore this$0;
/*     */         });
/* 162 */     return rc[0];
/*     */   }
/*     */   
/*     */   private ActiveMQMessage getCacheMessage(MessageIdentity identity) throws JMSException {
/* 166 */     return this.cacheMessageStore.getMessage(identity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQMessage getMessage(MessageIdentity identity) throws JMSException {
/* 173 */     ActiveMQMessage answer = null;
/*     */     
/* 175 */     Object location = identity.getSequenceNumber();
/* 176 */     if (location == null)
/*     */     {
/* 178 */       synchronized (this) {
/* 179 */         location = this.addedMessageLocations.get(identity);
/*     */       } 
/*     */     }
/*     */     
/* 183 */     if (location != null && location instanceof RecordLocation) {
/* 184 */       answer = (ActiveMQMessage)this.peristenceAdapter.readPacket((RecordLocation)location);
/* 185 */       if (answer != null) {
/* 186 */         return answer;
/*     */       }
/*     */     } 
/*     */     
/* 190 */     return this.longTermStore.getMessage(identity);
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
/*     */   public synchronized void recover(QueueMessageContainer container) throws JMSException {
/* 203 */     this.longTermStore.recover(container);
/*     */   }
/*     */   
/*     */   public void start() throws JMSException {
/* 207 */     this.longTermStore.start();
/*     */   }
/*     */   
/*     */   public void stop() throws JMSException {
/* 211 */     this.longTermStore.stop();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageStore getLongTermStore() {
/* 218 */     return this.longTermStore;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCacheMessageStore(CacheMessageStore store) {
/* 225 */     this.cacheMessageStore = (MessageStore)store;
/*     */     
/* 227 */     if (this.longTermStore instanceof CacheMessageStoreAware)
/* 228 */       ((CacheMessageStoreAware)this.longTermStore).setCacheMessageStore(store); 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\journal\JournalMessageStore.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */