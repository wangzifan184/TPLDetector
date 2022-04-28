/*    */ package org.codehaus.activemq;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ActiveMQPrefetchPolicy
/*    */ {
/* 37 */   private int queuePrefetch = 10;
/* 38 */   private int queueBrowserPrefetch = 500;
/* 39 */   private int topicPrefetch = 1000;
/* 40 */   private int durableTopicPrefetch = 100;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getDurableTopicPrefetch() {
/* 47 */     return this.durableTopicPrefetch;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setDurableTopicPrefetch(int durableTopicPrefetch) {
/* 54 */     this.durableTopicPrefetch = durableTopicPrefetch;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getQueuePrefetch() {
/* 61 */     return this.queuePrefetch;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setQueuePrefetch(int queuePrefetch) {
/* 68 */     this.queuePrefetch = queuePrefetch;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getQueueBrowserPrefetch() {
/* 75 */     return this.queueBrowserPrefetch;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setQueueBrowserPrefetch(int queueBrowserPrefetch) {
/* 82 */     this.queueBrowserPrefetch = queueBrowserPrefetch;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getTopicPrefetch() {
/* 89 */     return this.topicPrefetch;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setTopicPrefetch(int topicPrefetch) {
/* 96 */     this.topicPrefetch = topicPrefetch;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ActiveMQPrefetchPolicy.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */