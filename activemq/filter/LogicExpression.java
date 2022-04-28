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
/*    */ public abstract class LogicExpression
/*    */   extends BinaryExpression
/*    */   implements BooleanExpression
/*    */ {
/*    */   public static BooleanExpression createOR(BooleanExpression lvalue, BooleanExpression rvalue) {
/* 31 */     return new LogicExpression(lvalue, rvalue) {
/*    */         protected Object evaluate(Boolean lv, Boolean rv) {
/* 33 */           return (lv.booleanValue() || rv.booleanValue()) ? Boolean.TRUE : Boolean.FALSE;
/*    */         }
/*    */         
/*    */         public String getExpressionSymbol() {
/* 37 */           return "OR";
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   public static BooleanExpression createAND(BooleanExpression lvalue, BooleanExpression rvalue) {
/* 43 */     return new LogicExpression(lvalue, rvalue) {
/*    */         protected Object evaluate(Boolean lv, Boolean rv) {
/* 45 */           return (lv.booleanValue() && rv.booleanValue()) ? Boolean.TRUE : Boolean.FALSE;
/*    */         }
/*    */         
/*    */         public String getExpressionSymbol() {
/* 49 */           return "AND";
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LogicExpression(BooleanExpression left, BooleanExpression right) {
/* 59 */     super(left, right);
/*    */   }
/*    */   
/*    */   public Object evaluate(Message message) throws JMSException {
/* 63 */     Boolean lv = (Boolean)this.left.evaluate(message);
/* 64 */     if (lv == null) {
/* 65 */       return null;
/*    */     }
/* 67 */     Boolean rv = (Boolean)this.right.evaluate(message);
/* 68 */     if (rv == null) {
/* 69 */       return null;
/*    */     }
/* 71 */     return evaluate(lv, rv);
/*    */   }
/*    */   
/*    */   protected abstract Object evaluate(Boolean paramBoolean1, Boolean paramBoolean2);
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\filter\LogicExpression.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */