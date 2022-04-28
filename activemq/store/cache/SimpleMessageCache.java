/*    */ package org.codehaus.activemq.store.cache;
/*    */ 
/*    */ import org.codehaus.activemq.message.ActiveMQMessage;
/*    */ import org.codehaus.activemq.util.LRUCache;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleMessageCache
/*    */   implements MessageCache
/*    */ {
/*    */   private final LRUCache messages;
/*    */   
/*    */   public SimpleMessageCache() {
/* 34 */     this(1000);
/*    */   }
/*    */   
/*    */   public SimpleMessageCache(int cacheSize) {
/* 38 */     this.messages = new LRUCache(cacheSize);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized ActiveMQMessage get(String msgid) {
/* 48 */     return (ActiveMQMessage)this.messages.get(msgid);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void put(String messageID, ActiveMQMessage message) {
/* 58 */     this.messages.put(messageID, message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void remove(String messageID) {
/* 67 */     this.messages.remove(messageID);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\cache\SimpleMessageCache.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */