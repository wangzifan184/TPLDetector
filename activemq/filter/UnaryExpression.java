/*     */ package org.codehaus.activemq.filter;
/*     */ 
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Message;
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
/*     */ public abstract class UnaryExpression
/*     */   implements Expression
/*     */ {
/*     */   protected Expression right;
/*     */   
/*     */   public static Expression createNegate(Expression left) {
/*  33 */     return new UnaryExpression(left) {
/*     */         public Object evaluate(Message message) throws JMSException {
/*  35 */           Object lvalue = this.right.evaluate(message);
/*  36 */           if (lvalue == null) {
/*  37 */             return null;
/*     */           }
/*  39 */           if (lvalue instanceof Number) {
/*  40 */             return UnaryExpression.negate((Number)lvalue);
/*     */           }
/*  42 */           throw new RuntimeException("Cannot call negate operation on: " + lvalue);
/*     */         }
/*     */         
/*     */         public String getExpressionSymbol() {
/*  46 */           return "-";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static abstract class BooleanUnaryExpression extends UnaryExpression implements BooleanExpression {
/*     */     public BooleanUnaryExpression(Expression left) {
/*  53 */       super(left);
/*     */     }
/*     */   }
/*     */   
/*     */   public static BooleanExpression createNOT(BooleanExpression left) {
/*  58 */     return new BooleanUnaryExpression(left) {
/*     */         public Object evaluate(Message message) throws JMSException {
/*  60 */           Boolean lvalue = (Boolean)this.right.evaluate(message);
/*  61 */           if (lvalue == null) {
/*  62 */             return null;
/*     */           }
/*  64 */           return lvalue.booleanValue() ? Boolean.FALSE : Boolean.TRUE;
/*     */         }
/*     */         
/*     */         public String getExpressionSymbol() {
/*  68 */           return "NOT";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static Number negate(Number left) {
/*  74 */     if (left instanceof Integer) {
/*  75 */       return new Integer(-left.intValue());
/*     */     }
/*  77 */     if (left instanceof Long) {
/*  78 */       return new Long(-left.longValue());
/*     */     }
/*     */     
/*  81 */     return new Double(-left.doubleValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public UnaryExpression(Expression left) {
/*  86 */     this.right = left;
/*     */   }
/*     */   
/*     */   public Expression getRight() {
/*  90 */     return this.right;
/*     */   }
/*     */   
/*     */   public void setRight(Expression expression) {
/*  94 */     this.right = expression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 101 */     return "(" + getExpressionSymbol() + " " + this.right.toString() + ")";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 110 */     return toString().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 120 */     if (o == null || !getClass().equals(o.getClass())) {
/* 121 */       return false;
/*     */     }
/* 123 */     return toString().equals(o.toString());
/*     */   }
/*     */   
/*     */   public abstract String getExpressionSymbol();
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\filter\UnaryExpression.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */