/*     */ package org.codehaus.activemq.filter;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
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
/*     */ public abstract class ComparisonExpression
/*     */   extends BinaryExpression
/*     */   implements BooleanExpression
/*     */ {
/*     */   public static BooleanExpression createBetween(Expression value, Expression left, Expression right) {
/*  35 */     return LogicExpression.createAND(createGreaterThanEqual(value, left), createLessThanEqual(value, right));
/*     */   }
/*     */   
/*     */   public static BooleanExpression createNotBetween(Expression value, Expression left, Expression right) {
/*  39 */     return LogicExpression.createOR(createLessThan(value, left), createGreaterThan(value, right));
/*     */   }
/*     */   
/*  42 */   private static final HashSet REGEXP_CONTROL_CHARS = new HashSet();
/*     */   
/*     */   static {
/*  45 */     REGEXP_CONTROL_CHARS.add(new Character('.'));
/*  46 */     REGEXP_CONTROL_CHARS.add(new Character('\\'));
/*  47 */     REGEXP_CONTROL_CHARS.add(new Character('['));
/*  48 */     REGEXP_CONTROL_CHARS.add(new Character(']'));
/*  49 */     REGEXP_CONTROL_CHARS.add(new Character('^'));
/*  50 */     REGEXP_CONTROL_CHARS.add(new Character('$'));
/*  51 */     REGEXP_CONTROL_CHARS.add(new Character('?'));
/*  52 */     REGEXP_CONTROL_CHARS.add(new Character('*'));
/*  53 */     REGEXP_CONTROL_CHARS.add(new Character('+'));
/*  54 */     REGEXP_CONTROL_CHARS.add(new Character('{'));
/*  55 */     REGEXP_CONTROL_CHARS.add(new Character('}'));
/*  56 */     REGEXP_CONTROL_CHARS.add(new Character('|'));
/*  57 */     REGEXP_CONTROL_CHARS.add(new Character('('));
/*  58 */     REGEXP_CONTROL_CHARS.add(new Character(')'));
/*  59 */     REGEXP_CONTROL_CHARS.add(new Character(':'));
/*  60 */     REGEXP_CONTROL_CHARS.add(new Character('&'));
/*  61 */     REGEXP_CONTROL_CHARS.add(new Character('<'));
/*  62 */     REGEXP_CONTROL_CHARS.add(new Character('>'));
/*  63 */     REGEXP_CONTROL_CHARS.add(new Character('='));
/*  64 */     REGEXP_CONTROL_CHARS.add(new Character('!'));
/*     */   }
/*     */ 
/*     */   
/*     */   static class LikeExpression
/*     */     extends UnaryExpression
/*     */     implements BooleanExpression
/*     */   {
/*     */     Pattern likePattern;
/*     */     
/*     */     public LikeExpression(Expression right, String like, int escape) {
/*  75 */       super(right);
/*     */       
/*  77 */       StringBuffer regexp = new StringBuffer(like.length() * 2);
/*  78 */       regexp.append("\\A");
/*  79 */       for (int i = 0; i < like.length(); i++) {
/*  80 */         char c = like.charAt(i);
/*  81 */         if (escape == (Character.MAX_VALUE & c)) {
/*  82 */           i++;
/*  83 */           if (i >= like.length()) {
/*     */             break;
/*     */           }
/*     */ 
/*     */           
/*  88 */           char t = like.charAt(i);
/*  89 */           regexp.append("\\x");
/*  90 */           regexp.append(Integer.toHexString(Character.MAX_VALUE & t));
/*     */         }
/*  92 */         else if (c == '%') {
/*  93 */           regexp.append(".*?");
/*     */         }
/*  95 */         else if (c == '_') {
/*  96 */           regexp.append(".");
/*     */         }
/*  98 */         else if (ComparisonExpression.REGEXP_CONTROL_CHARS.contains(new Character(c))) {
/*  99 */           regexp.append("\\x");
/* 100 */           regexp.append(Integer.toHexString(Character.MAX_VALUE & c));
/*     */         } else {
/*     */           
/* 103 */           regexp.append(c);
/*     */         } 
/*     */       } 
/* 106 */       regexp.append("\\z");
/*     */       
/* 108 */       System.out.println("regexp: " + like + ": " + regexp);
/* 109 */       this.likePattern = Pattern.compile(regexp.toString(), 32);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getExpressionSymbol() {
/* 116 */       return "LIKE";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object evaluate(Message message) throws JMSException {
/* 124 */       Object rv = getRight().evaluate(message);
/*     */       
/* 126 */       if (rv == null) {
/* 127 */         return null;
/*     */       }
/*     */       
/* 130 */       if (!(rv instanceof String)) {
/* 131 */         throw new RuntimeException("LIKE can only operate on String identifiers.  LIKE attemped on: '" + rv.getClass());
/*     */       }
/*     */       
/* 134 */       return this.likePattern.matcher((String)rv).matches() ? Boolean.TRUE : Boolean.FALSE;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static BooleanExpression createLike(Expression left, String right, String escape) {
/* 140 */     if (escape != null && escape.length() != 1) {
/* 141 */       throw new RuntimeException("The ESCAPE string litteral is invalid.  It can only be one character.  Litteral used: " + escape);
/*     */     }
/* 143 */     int c = -1;
/* 144 */     if (escape != null) {
/* 145 */       c = Character.MAX_VALUE & escape.charAt(0);
/*     */     }
/*     */     
/* 148 */     return new LikeExpression(left, right, c);
/*     */   }
/*     */   
/*     */   public static BooleanExpression createNotLike(Expression left, String right, String escape) {
/* 152 */     return UnaryExpression.createNOT(createLike(left, right, escape));
/*     */   }
/*     */   
/*     */   public static BooleanExpression createInFilter(Expression left, List elements) {
/* 156 */     if (elements.isEmpty()) {
/* 157 */       return ConstantExpression.FALSE;
/*     */     }
/*     */     
/* 160 */     BooleanExpression answer = createEqual(left, elements.get(0));
/* 161 */     for (int i = 1; i < elements.size(); i++) {
/* 162 */       answer = LogicExpression.createOR(answer, createEqual(left, elements.get(i)));
/*     */     }
/* 164 */     return answer;
/*     */   }
/*     */   
/*     */   public static BooleanExpression createNotInFilter(Expression left, List elements) {
/* 168 */     if (elements.isEmpty()) {
/* 169 */       return ConstantExpression.TRUE;
/*     */     }
/*     */     
/* 172 */     BooleanExpression answer = createEqual(left, elements.get(0));
/* 173 */     for (int i = 1; i < elements.size(); i++) {
/* 174 */       answer = LogicExpression.createOR(answer, createEqual(left, elements.get(i)));
/*     */     }
/* 176 */     return UnaryExpression.createNOT(answer);
/*     */   }
/*     */   
/*     */   public static BooleanExpression createIsNull(Expression left) {
/* 180 */     return createEqual(left, ConstantExpression.NULL);
/*     */   }
/*     */   
/*     */   public static BooleanExpression createIsNotNull(Expression left) {
/* 184 */     return createNotEqual(left, ConstantExpression.NULL);
/*     */   }
/*     */   
/*     */   public static BooleanExpression createNotEqual(Expression left, Expression right) {
/* 188 */     return UnaryExpression.createNOT(createEqual(left, right));
/*     */   }
/*     */   
/*     */   public static BooleanExpression createEqual(Expression left, Expression right) {
/* 192 */     return new ComparisonExpression(left, right)
/*     */       {
/*     */         public Object evaluate(Message message) throws JMSException {
/* 195 */           Object obj1 = this.left.evaluate(message);
/* 196 */           Object obj2 = this.right.evaluate(message);
/*     */           
/* 198 */           Comparable lv = (obj1 instanceof Comparable) ? (Comparable)obj1 : null;
/* 199 */           Comparable rv = (obj2 instanceof Comparable) ? (Comparable)obj2 : null;
/*     */           
/* 201 */           if ((((lv == null) ? 1 : 0) ^ ((rv == null) ? 1 : 0)) != 0) {
/* 202 */             return Boolean.FALSE;
/*     */           }
/* 204 */           if (lv == rv) {
/* 205 */             return Boolean.TRUE;
/*     */           }
/* 207 */           return compare(lv, rv);
/*     */         }
/*     */         
/*     */         protected boolean asBoolean(int answer) {
/* 211 */           return (answer == 0);
/*     */         }
/*     */         
/*     */         public String getExpressionSymbol() {
/* 215 */           return "=";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static BooleanExpression createGreaterThan(Expression left, Expression right) {
/* 221 */     return new ComparisonExpression(left, right) {
/*     */         protected boolean asBoolean(int answer) {
/* 223 */           return (answer > 0);
/*     */         }
/*     */         
/*     */         public String getExpressionSymbol() {
/* 227 */           return ">";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static BooleanExpression createGreaterThanEqual(Expression left, Expression right) {
/* 233 */     return new ComparisonExpression(left, right) {
/*     */         protected boolean asBoolean(int answer) {
/* 235 */           return (answer >= 0);
/*     */         }
/*     */         
/*     */         public String getExpressionSymbol() {
/* 239 */           return ">=";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static BooleanExpression createLessThan(Expression left, Expression right) {
/* 245 */     return new ComparisonExpression(left, right)
/*     */       {
/*     */         protected boolean asBoolean(int answer) {
/* 248 */           return (answer < 0);
/*     */         }
/*     */         
/*     */         public String getExpressionSymbol() {
/* 252 */           return "<";
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static BooleanExpression createLessThanEqual(Expression left, Expression right) {
/* 259 */     return new ComparisonExpression(left, right)
/*     */       {
/*     */         protected boolean asBoolean(int answer) {
/* 262 */           return (answer <= 0);
/*     */         }
/*     */         
/*     */         public String getExpressionSymbol() {
/* 266 */           return "<=";
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ComparisonExpression(Expression left, Expression right) {
/* 276 */     super(left, right);
/*     */   }
/*     */   
/*     */   public Object evaluate(Message message) throws JMSException {
/* 280 */     Comparable lv = (Comparable)this.left.evaluate(message);
/* 281 */     if (lv == null) {
/* 282 */       return null;
/*     */     }
/* 284 */     Comparable rv = (Comparable)this.right.evaluate(message);
/* 285 */     if (rv == null) {
/* 286 */       return null;
/*     */     }
/* 288 */     return compare(lv, rv);
/*     */   }
/*     */   
/*     */   protected Boolean compare(Comparable lv, Comparable rv) {
/* 292 */     Class lc = lv.getClass();
/* 293 */     Class rc = rv.getClass();
/*     */ 
/*     */     
/* 296 */     if (lc != rc) {
/* 297 */       if (lc == Integer.class) {
/* 298 */         if (rc == Long.class) {
/* 299 */           lv = new Long(((Integer)lv).longValue());
/*     */         }
/* 301 */         else if (rc == Double.class) {
/* 302 */           lv = new Double(((Long)lv).doubleValue());
/*     */         } else {
/*     */           
/* 305 */           throw new RuntimeException("You cannot compare a '" + lc.getName() + "' and a '" + rc.getName() + "'");
/*     */         } 
/*     */       }
/*     */       
/* 309 */       if (lc == Long.class) {
/* 310 */         if (rc == Integer.class) {
/* 311 */           rv = new Long(((Integer)rv).longValue());
/*     */         }
/* 313 */         else if (rc == Double.class) {
/* 314 */           lv = new Double(((Long)lv).doubleValue());
/*     */         } else {
/*     */           
/* 317 */           throw new RuntimeException("You cannot compare a '" + lc.getName() + "' and a '" + rc.getName() + "'");
/*     */         } 
/*     */       }
/*     */       
/* 321 */       if (lc == Double.class) {
/* 322 */         if (rc == Integer.class) {
/* 323 */           rv = new Double(((Integer)rv).doubleValue());
/*     */         }
/* 325 */         else if (rc == Long.class) {
/* 326 */           rv = new Double(((Long)rv).doubleValue());
/*     */         } else {
/*     */           
/* 329 */           throw new RuntimeException("You cannot compare a '" + lc.getName() + "' and a '" + rc.getName() + "'");
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 334 */     return asBoolean(lv.compareTo(rv)) ? Boolean.TRUE : Boolean.FALSE;
/*     */   }
/*     */   
/*     */   protected abstract boolean asBoolean(int paramInt);
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\filter\ComparisonExpression.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */