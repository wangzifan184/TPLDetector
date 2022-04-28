/*    */ package org.codehaus.activemq.filter;
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
/*    */ public abstract class BinaryExpression
/*    */   implements Expression
/*    */ {
/*    */   protected Expression left;
/*    */   protected Expression right;
/*    */   
/*    */   public BinaryExpression(Expression left, Expression right) {
/* 31 */     this.left = left;
/* 32 */     this.right = right;
/*    */   }
/*    */   
/*    */   public Expression getLeft() {
/* 36 */     return this.left;
/*    */   }
/*    */   
/*    */   public Expression getRight() {
/* 40 */     return this.right;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 48 */     return "(" + this.left.toString() + " " + getExpressionSymbol() + " " + this.right.toString() + ")";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 57 */     return toString().hashCode();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 67 */     if (o == null || !getClass().equals(o.getClass())) {
/* 68 */       return false;
/*    */     }
/* 70 */     return toString().equals(o.toString());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract String getExpressionSymbol();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setRight(Expression expression) {
/* 86 */     this.right = expression;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLeft(Expression expression) {
/* 93 */     this.left = expression;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\filter\BinaryExpression.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */