/*     */ package org.codehaus.activemq.store.cache;
/*     */ 
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.MessageAck;
/*     */ import org.codehaus.activemq.service.MessageIdentity;
/*     */ import org.codehaus.activemq.service.QueueMessageContainer;
/*     */ import org.codehaus.activemq.store.MessageStore;
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
/*     */ public class CacheMessageStore
/*     */   implements MessageStore, CacheMessageStoreAware
/*     */ {
/*  38 */   private static final Log log = LogFactory.getLog(CacheMessageStore.class);
/*     */   
/*     */   private final CachePersistenceAdapter peristenceAdapter;
/*     */   private final MessageStore longTermStore;
/*     */   private final MessageCache cache;
/*     */   
/*     */   public CacheMessageStore(CachePersistenceAdapter adapter, MessageStore longTermStore, MessageCache cache) {
/*  45 */     this.peristenceAdapter = adapter;
/*  46 */     this.longTermStore = longTermStore;
/*  47 */     this.cache = cache;
/*     */ 
/*     */     
/*  50 */     setCacheMessageStore(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageIdentity addMessage(ActiveMQMessage message) throws JMSException {
/*  57 */     MessageIdentity messageIdentity = this.longTermStore.addMessage(message);
/*  58 */     this.cache.put(messageIdentity.getMessageID(), message);
/*  59 */     return messageIdentity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeMessage(MessageIdentity identity, MessageAck ack) throws JMSException {
/*  66 */     this.longTermStore.removeMessage(identity, ack);
/*  67 */     this.cache.remove(identity.getMessageID());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQMessage getMessage(MessageIdentity identity) throws JMSException {
/*  75 */     ActiveMQMessage answer = null;
/*  76 */     answer = this.cache.get(identity.getMessageID());
/*  77 */     if (answer != null) {
/*  78 */       return answer;
/*     */     }
/*  80 */     answer = this.longTermStore.getMessage(identity);
/*  81 */     this.cache.put(identity.getMessageID(), answer);
/*  82 */     return answer;
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
/*  95 */     this.longTermStore.recover(container);
/*     */   }
/*     */   
/*     */   public void start() throws JMSException {
/*  99 */     this.longTermStore.start();
/*     */   }
/*     */   
/*     */   public void stop() throws JMSException {
/* 103 */     this.longTermStore.stop();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageStore getLongTermStore() {
/* 110 */     return this.longTermStore;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCacheMessageStore(CacheMessageStore store) {
/* 119 */     if (this.longTermStore instanceof CacheMessageStoreAware)
/* 120 */       ((CacheMessageStoreAware)this.longTermStore).setCacheMessageStore(store); 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\cache\CacheMessageStore.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */