/*    */ package org.codehaus.activemq.store.cache;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.codehaus.activemq.store.PersistenceAdapter;
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
/*    */ 
/*    */ public class SimpleCachePersistenceAdapter
/*    */   extends CachePersistenceAdapter
/*    */ {
/* 32 */   int cacheSize = 1000;
/*    */ 
/*    */ 
/*    */   
/*    */   public SimpleCachePersistenceAdapter() {}
/*    */ 
/*    */   
/*    */   public SimpleCachePersistenceAdapter(PersistenceAdapter longTermPersistence) throws IOException {
/* 40 */     super(longTermPersistence);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected MessageCache createMessageCache(String destinationName) {
/* 47 */     return new SimpleMessageCache(this.cacheSize);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getCacheSize() {
/* 57 */     return this.cacheSize;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCacheSize(int cacheSize) {
/* 63 */     this.cacheSize = cacheSize;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\cache\SimpleCachePersistenceAdapter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */