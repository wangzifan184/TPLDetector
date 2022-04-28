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
/*     */ public class ConstantExpression
/*     */   implements Expression
/*     */ {
/*     */   static class BooleanConstantExpression
/*     */     extends ConstantExpression
/*     */     implements BooleanExpression
/*     */   {
/*     */     public BooleanConstantExpression(Object value) {
/*  33 */       super(value);
/*     */     }
/*     */   }
/*     */   
/*  37 */   public static final BooleanConstantExpression NULL = new BooleanConstantExpression(null);
/*  38 */   public static final BooleanConstantExpression TRUE = new BooleanConstantExpression(Boolean.TRUE);
/*  39 */   public static final BooleanConstantExpression FALSE = new BooleanConstantExpression(Boolean.FALSE);
/*     */   
/*     */   private Object value;
/*     */   
/*     */   public static ConstantExpression createInteger(String text) {
/*  44 */     Number value = new Long(text);
/*  45 */     long l = value.longValue();
/*  46 */     if (-2147483648L <= l && l <= 2147483647L) {
/*  47 */       value = new Integer(value.intValue());
/*     */     }
/*  49 */     return new ConstantExpression(value);
/*     */   }
/*     */   
/*     */   public static ConstantExpression createFloat(String text) {
/*  53 */     Number value = new Double(text);
/*  54 */     return new ConstantExpression(value);
/*     */   }
/*     */   
/*     */   public ConstantExpression(Object value) {
/*  58 */     this.value = value;
/*     */   }
/*     */   
/*     */   public Object evaluate(Message message) throws JMSException {
/*  62 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  70 */     if (this.value == null) {
/*  71 */       return "NULL";
/*     */     }
/*  73 */     if (this.value instanceof Boolean) {
/*  74 */       return ((Boolean)this.value).booleanValue() ? "TRUE" : "FALSE";
/*     */     }
/*  76 */     if (this.value instanceof String) {
/*  77 */       return encodeString((String)this.value);
/*     */     }
/*  79 */     return this.value.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  88 */     return toString().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  98 */     if (o == null || !getClass().equals(o.getClass())) {
/*  99 */       return false;
/*     */     }
/* 101 */     return toString().equals(o.toString());
/*     */   }
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
/*     */   private String encodeString(String s) {
/* 114 */     StringBuffer b = new StringBuffer();
/* 115 */     b.append('\'');
/* 116 */     for (int i = 0; i < s.length(); i++) {
/* 117 */       char c = s.charAt(i);
/* 118 */       if (c == '\'') {
/* 119 */         b.append(c);
/*     */       }
/* 121 */       b.append(c);
/*     */     } 
/* 123 */     b.append('\'');
/* 124 */     return b.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\filter\ConstantExpression.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */