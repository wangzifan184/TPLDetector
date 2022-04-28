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
/*    */ public class NotFilter
/*    */   implements Filter
/*    */ {
/*    */   private Filter filter;
/*    */   
/*    */   public NotFilter(Filter filter) {
/* 35 */     this.filter = filter;
/*    */   }
/*    */   
/*    */   public boolean matches(Message message) throws JMSException {
/* 39 */     return !this.filter.matches(message);
/*    */   }
/*    */   
/*    */   public boolean isWildcard() {
/* 43 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\filter\NotFilter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */