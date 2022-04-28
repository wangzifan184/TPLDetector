/*    */ package org.codehaus.activemq.util;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
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
/*    */ public class LRUCache
/*    */   extends LinkedHashMap
/*    */ {
/*    */   private static final long serialVersionUID = -5754338187296859149L;
/*    */   protected static final int DEFAULT_INITIAL_CAPACITY = 1000;
/*    */   protected static final float DEFAULT_LOAD_FACTOR = 0.75F;
/*    */   private int maxSize;
/*    */   
/*    */   public LRUCache(int initialCapacity, float loadFactor, boolean accessOrder, int maxSize) {
/* 38 */     super(initialCapacity, loadFactor, accessOrder);
/* 39 */     this.maxSize = maxSize;
/*    */   }
/*    */   
/*    */   public LRUCache(int maxSize) {
/* 43 */     this(1000, 0.75F, true, maxSize);
/*    */   }
/*    */   
/*    */   public LRUCache(int maxSize, boolean accessOrder) {
/* 47 */     this(1000, 0.75F, accessOrder, maxSize);
/*    */   }
/*    */   
/*    */   protected boolean removeEldestEntry(Map.Entry eldest) {
/* 51 */     return (size() > this.maxSize);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activem\\util\LRUCache.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */