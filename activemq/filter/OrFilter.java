/*    */ package org.codehaus.activemq.filter;
/*    */ 
/*    */ import javax.jms.JMSException;
/*    */ import javax.jms.Message;
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
/*    */ public class OrFilter
/*    */   implements Filter
/*    */ {
/*    */   private Filter left;
/*    */   private Filter right;
/*    */   
/*    */   public OrFilter(Filter left, Filter right) {
/* 36 */     this.left = left;
/* 37 */     this.right = right;
/*    */   }
/*    */   
/*    */   public boolean matches(Message message) throws JMSException {
/* 41 */     if (this.left.matches(message)) {
/* 42 */       return true;
/*    */     }
/*    */     
/* 45 */     return this.right.matches(message);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isWildcard() {
/* 50 */     return (this.left.isWildcard() || this.right.isWildcard());
/*    */   }
/*    */   
/*    */   public Filter getLeft() {
/* 54 */     return this.left;
/*    */   }
/*    */   
/*    */   public Filter getRight() {
/* 58 */     return this.right;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\filter\OrFilter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */