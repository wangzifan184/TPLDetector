/*     */ package org.codehaus.activemq.filter;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
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
/*     */ public class MultiExpressionEvaluator
/*     */ {
/*  76 */   HashMap rootExpressions = new HashMap();
/*  77 */   HashMap cachedExpressions = new HashMap();
/*     */   
/*  79 */   int view = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class CacheExpression
/*     */     extends UnaryExpression
/*     */   {
/*  87 */     short refCount = 0;
/*  88 */     int cview = MultiExpressionEvaluator.this.view - 1;
/*     */     
/*     */     Object cachedValue;
/*     */     
/*     */     public CacheExpression(Expression realExpression) {
/*  93 */       super(realExpression);
/*  94 */       this.cachedHashCode = realExpression.hashCode();
/*     */     }
/*     */     
/*     */     int cachedHashCode;
/*     */     private final MultiExpressionEvaluator this$0;
/*     */     
/*     */     public Object evaluate(Message message) throws JMSException {
/* 101 */       if (MultiExpressionEvaluator.this.view == this.cview) {
/* 102 */         return this.cachedValue;
/*     */       }
/* 104 */       this.cachedValue = this.right.evaluate(message);
/* 105 */       this.cview = MultiExpressionEvaluator.this.view;
/* 106 */       return this.cachedValue;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 110 */       return this.cachedHashCode;
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 114 */       return ((CacheExpression)o).right.equals(this.right);
/*     */     }
/*     */     
/*     */     public String getExpressionSymbol() {
/* 118 */       return null;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 122 */       return this.right.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class ExpressionListenerSet
/*     */   {
/*     */     Expression expression;
/*     */ 
/*     */     
/* 133 */     ArrayList listeners = new ArrayList();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addExpressionListner(Expression selector, ExpressionListener c) {
/* 150 */     ExpressionListenerSet data = (ExpressionListenerSet)this.rootExpressions.get(selector.toString());
/* 151 */     if (data == null) {
/* 152 */       data = new ExpressionListenerSet();
/* 153 */       data.expression = addToCache(selector);
/* 154 */       this.rootExpressions.put(selector.toString(), data);
/*     */     } 
/* 156 */     data.listeners.add(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeEventListner(String selector, ExpressionListener c) {
/* 164 */     String expKey = selector;
/* 165 */     ExpressionListenerSet d = (ExpressionListenerSet)this.rootExpressions.get(expKey);
/* 166 */     if (d == null)
/*     */     {
/* 168 */       return false;
/*     */     }
/* 170 */     if (!d.listeners.remove(c))
/*     */     {
/* 172 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 176 */     if (d.listeners.size() == 0) {
/*     */       
/* 178 */       removeFromCache((CacheExpression)d.expression);
/* 179 */       this.rootExpressions.remove(expKey);
/*     */     } 
/* 181 */     return true;
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
/*     */ 
/*     */   
/*     */   private CacheExpression addToCache(Expression expr) {
/* 196 */     CacheExpression n = (CacheExpression)this.cachedExpressions.get(expr);
/* 197 */     if (n == null) {
/* 198 */       n = new CacheExpression(expr);
/* 199 */       this.cachedExpressions.put(expr, n);
/* 200 */       if (expr instanceof UnaryExpression) {
/*     */ 
/*     */         
/* 203 */         UnaryExpression un = (UnaryExpression)expr;
/* 204 */         un.setRight(addToCache(un.getRight()));
/*     */       
/*     */       }
/* 207 */       else if (expr instanceof BinaryExpression) {
/*     */ 
/*     */         
/* 210 */         BinaryExpression bn = (BinaryExpression)expr;
/* 211 */         bn.setRight(addToCache(bn.getRight()));
/* 212 */         bn.setLeft(addToCache(bn.getLeft()));
/*     */       } 
/*     */     } 
/*     */     
/* 216 */     n.refCount = (short)(n.refCount + 1);
/* 217 */     return n;
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
/*     */   private void removeFromCache(CacheExpression cn) {
/* 229 */     cn.refCount = (short)(cn.refCount - 1);
/* 230 */     Expression realExpr = cn.getRight();
/* 231 */     if (cn.refCount == 0) {
/* 232 */       this.cachedExpressions.remove(realExpr);
/*     */     }
/* 234 */     if (realExpr instanceof UnaryExpression) {
/* 235 */       UnaryExpression un = (UnaryExpression)realExpr;
/* 236 */       removeFromCache((CacheExpression)un.getRight());
/*     */     } 
/* 238 */     if (realExpr instanceof BinaryExpression) {
/* 239 */       BinaryExpression bn = (BinaryExpression)realExpr;
/* 240 */       removeFromCache((CacheExpression)bn.getRight());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void evaluate(Message message) {
/* 252 */     Collection expressionListeners = this.rootExpressions.values();
/* 253 */     for (Iterator iter = expressionListeners.iterator(); iter.hasNext(); ) {
/* 254 */       ExpressionListenerSet els = iter.next();
/*     */       try {
/* 256 */         Object result = els.expression.evaluate(message);
/* 257 */         for (Iterator iterator = els.listeners.iterator(); iterator.hasNext(); ) {
/* 258 */           ExpressionListener l = iterator.next();
/* 259 */           l.evaluateResultEvent(els.expression, message, result);
/*     */         }
/*     */       
/* 262 */       } catch (Throwable e) {
/* 263 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   static interface ExpressionListener {
/*     */     void evaluateResultEvent(Expression param1Expression, Message param1Message, Object param1Object);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\filter\MultiExpressionEvaluator.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */