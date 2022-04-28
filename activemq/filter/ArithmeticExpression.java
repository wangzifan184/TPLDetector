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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ArithmeticExpression
/*     */   extends BinaryExpression
/*     */ {
/*     */   protected static final int INTEGER = 1;
/*     */   protected static final int LONG = 2;
/*     */   protected static final int DOUBLE = 3;
/*     */   
/*     */   public ArithmeticExpression(Expression left, Expression right) {
/*  40 */     super(left, right);
/*     */   }
/*     */   
/*     */   public static Expression createPlus(Expression left, Expression right) {
/*  44 */     return new ArithmeticExpression(left, right) {
/*     */         protected Object evaluate(Object lvalue, Object rvalue) {
/*  46 */           if (lvalue instanceof String) {
/*  47 */             String text = (String)lvalue;
/*  48 */             String answer = text + rvalue;
/*  49 */             System.out.println("lvalue: " + lvalue + " rvalue: " + rvalue + " result: " + answer);
/*  50 */             return answer;
/*     */           } 
/*  52 */           if (lvalue instanceof Number) {
/*  53 */             return plus((Number)lvalue, asNumber(rvalue));
/*     */           }
/*  55 */           throw new RuntimeException("Cannot call plus operation on: " + lvalue + " and: " + rvalue);
/*     */         }
/*     */         
/*     */         public String getExpressionSymbol() {
/*  59 */           return "+";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static Expression createMinus(Expression left, Expression right) {
/*  65 */     return new ArithmeticExpression(left, right) {
/*     */         protected Object evaluate(Object lvalue, Object rvalue) {
/*  67 */           if (lvalue instanceof Number) {
/*  68 */             return minus((Number)lvalue, asNumber(rvalue));
/*     */           }
/*  70 */           throw new RuntimeException("Cannot call minus operation on: " + lvalue + " and: " + rvalue);
/*     */         }
/*     */         
/*     */         public String getExpressionSymbol() {
/*  74 */           return "-";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static Expression createMultiply(Expression left, Expression right) {
/*  80 */     return new ArithmeticExpression(left, right)
/*     */       {
/*     */         protected Object evaluate(Object lvalue, Object rvalue) {
/*  83 */           if (lvalue instanceof Number) {
/*  84 */             return multiply((Number)lvalue, asNumber(rvalue));
/*     */           }
/*  86 */           throw new RuntimeException("Cannot call multiply operation on: " + lvalue + " and: " + rvalue);
/*     */         }
/*     */         
/*     */         public String getExpressionSymbol() {
/*  90 */           return "*";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static Expression createDivide(Expression left, Expression right) {
/*  96 */     return new ArithmeticExpression(left, right)
/*     */       {
/*     */         protected Object evaluate(Object lvalue, Object rvalue) {
/*  99 */           if (lvalue instanceof Number) {
/* 100 */             return divide((Number)lvalue, asNumber(rvalue));
/*     */           }
/* 102 */           throw new RuntimeException("Cannot call divide operation on: " + lvalue + " and: " + rvalue);
/*     */         }
/*     */         
/*     */         public String getExpressionSymbol() {
/* 106 */           return "/";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static Expression createMod(Expression left, Expression right) {
/* 112 */     return new ArithmeticExpression(left, right)
/*     */       {
/*     */         protected Object evaluate(Object lvalue, Object rvalue) {
/* 115 */           if (lvalue instanceof Number) {
/* 116 */             return mod((Number)lvalue, asNumber(rvalue));
/*     */           }
/* 118 */           throw new RuntimeException("Cannot call mod operation on: " + lvalue + " and: " + rvalue);
/*     */         }
/*     */         
/*     */         public String getExpressionSymbol() {
/* 122 */           return "%";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   protected Number plus(Number left, Number right) {
/* 128 */     switch (numberType(left, right)) {
/*     */       case 1:
/* 130 */         return new Integer(left.intValue() + right.intValue());
/*     */       case 2:
/* 132 */         return new Long(left.longValue() + right.longValue());
/*     */     } 
/* 134 */     return new Double(left.doubleValue() + right.doubleValue());
/*     */   }
/*     */ 
/*     */   
/*     */   protected Number minus(Number left, Number right) {
/* 139 */     switch (numberType(left, right)) {
/*     */       case 1:
/* 141 */         return new Integer(left.intValue() - right.intValue());
/*     */       case 2:
/* 143 */         return new Long(left.longValue() - right.longValue());
/*     */     } 
/* 145 */     return new Double(left.doubleValue() - right.doubleValue());
/*     */   }
/*     */ 
/*     */   
/*     */   protected Number multiply(Number left, Number right) {
/* 150 */     switch (numberType(left, right)) {
/*     */       case 1:
/* 152 */         return new Integer(left.intValue() * right.intValue());
/*     */       case 2:
/* 154 */         return new Long(left.longValue() * right.longValue());
/*     */     } 
/* 156 */     return new Double(left.doubleValue() * right.doubleValue());
/*     */   }
/*     */ 
/*     */   
/*     */   protected Number divide(Number left, Number right) {
/* 161 */     return new Double(left.doubleValue() / right.doubleValue());
/*     */   }
/*     */   
/*     */   protected Number mod(Number left, Number right) {
/* 165 */     return new Double(left.doubleValue() % right.doubleValue());
/*     */   }
/*     */   
/*     */   private int numberType(Number left, Number right) {
/* 169 */     if (isDouble(left) || isDouble(right)) {
/* 170 */       return 3;
/*     */     }
/* 172 */     if (left instanceof Long || right instanceof Long) {
/* 173 */       return 2;
/*     */     }
/*     */     
/* 176 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isDouble(Number n) {
/* 181 */     return (n instanceof Float || n instanceof Double);
/*     */   }
/*     */   
/*     */   protected Number asNumber(Object value) {
/* 185 */     if (value instanceof Number) {
/* 186 */       return (Number)value;
/*     */     }
/*     */     
/* 189 */     throw new RuntimeException("Cannot convert value: " + value + " into a number");
/*     */   }
/*     */ 
/*     */   
/*     */   public Object evaluate(Message message) throws JMSException {
/* 194 */     Object lvalue = this.left.evaluate(message);
/* 195 */     if (lvalue == null) {
/* 196 */       return null;
/*     */     }
/* 198 */     Object rvalue = this.right.evaluate(message);
/* 199 */     if (rvalue == null) {
/* 200 */       return null;
/*     */     }
/* 202 */     return evaluate(lvalue, rvalue);
/*     */   }
/*     */   
/*     */   protected abstract Object evaluate(Object paramObject1, Object paramObject2);
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\filter\ArithmeticExpression.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */