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
/*    */ public class ExpressionFilter
/*    */   implements Filter
/*    */ {
/*    */   private Expression expression;
/*    */   
/*    */   public ExpressionFilter(Expression expression) {
/* 35 */     this.expression = expression;
/*    */   }
/*    */   
/*    */   public boolean matches(Message message) throws JMSException {
/* 39 */     Object value = this.expression.evaluate(message);
/* 40 */     if (value != null && value instanceof Boolean) {
/* 41 */       return ((Boolean)value).booleanValue();
/*    */     }
/* 43 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isWildcard() {
/* 47 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Expression getExpression() {
/* 54 */     return this.expression;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\filter\ExpressionFilter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */