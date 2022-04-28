/*      */ package org.codehaus.activemq.selector;
/*      */ import java.util.ArrayList;
/*      */ import org.codehaus.activemq.filter.BooleanExpression;
/*      */ import org.codehaus.activemq.filter.ComparisonExpression;
/*      */ import org.codehaus.activemq.filter.ConstantExpression;
/*      */ import org.codehaus.activemq.filter.Expression;
/*      */ 
/*      */ public class SelectorParser implements SelectorParserConstants {
/*      */   public SelectorParserTokenManager token_source;
/*      */   SimpleCharStream jj_input_stream;
/*      */   public Token token;
/*      */   public Token jj_nt;
/*      */   private int jj_ntk;
/*      */   private Token jj_scanpos;
/*      */   private Token jj_lastpos;
/*      */   private int jj_la;
/*      */   
/*      */   public SelectorParser() {
/*   19 */     this(new StringReader(""));
/*      */   }
/*      */   
/*      */   public Filter parse(String sql) throws InvalidSelectorException {
/*   23 */     ReInit(new StringReader(sql));
/*      */     
/*      */     try {
/*   26 */       return JmsSelector();
/*      */     }
/*   28 */     catch (TokenMgrError e) {
/*   29 */       throw new InvalidSelectorException(e.getMessage());
/*      */     }
/*   31 */     catch (ParseException e) {
/*   32 */       throw new InvalidSelectorException(e.getMessage());
/*      */     } 
/*      */   }
/*      */   
/*      */   private BooleanExpression asBooleanExpression(Expression value) throws ParseException {
/*   37 */     if (value instanceof BooleanExpression) {
/*   38 */       return (BooleanExpression)value;
/*      */     }
/*   40 */     throw new ParseException("Expression will not result in a boolean value: " + value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Filter JmsSelector() throws ParseException {
/*   47 */     Expression left = null;
/*   48 */     left = orExpression();
/*   49 */     if (left instanceof BooleanExpression) {
/*   50 */       return (Filter)new ExpressionFilter(left);
/*      */     }
/*   52 */     throw new ParseException("Selector does not result in a boolean value: " + left);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Expression orExpression() throws ParseException {
/*      */     BooleanExpression booleanExpression;
/*   59 */     Expression left = andExpression();
/*      */     
/*      */     while (true) {
/*   62 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 10:
/*      */           break;
/*      */         
/*      */         default:
/*      */           break;
/*      */       } 
/*   69 */       jj_consume_token(10);
/*   70 */       Expression right = andExpression();
/*   71 */       booleanExpression = LogicExpression.createOR(asBooleanExpression(left), asBooleanExpression(right));
/*      */     } 
/*   73 */     return (Expression)booleanExpression;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Expression andExpression() throws ParseException {
/*      */     BooleanExpression booleanExpression;
/*   80 */     Expression left = equalityExpression();
/*      */     
/*      */     while (true) {
/*   83 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 9:
/*      */           break;
/*      */         
/*      */         default:
/*      */           break;
/*      */       } 
/*   90 */       jj_consume_token(9);
/*   91 */       Expression right = equalityExpression();
/*   92 */       booleanExpression = LogicExpression.createAND(asBooleanExpression(left), asBooleanExpression(right));
/*      */     } 
/*   94 */     return (Expression)booleanExpression;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Expression equalityExpression() throws ParseException {
/*      */     BooleanExpression booleanExpression;
/*  101 */     Expression left = comparisonExpression();
/*      */     while (true) {
/*      */       Expression right;
/*  104 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 15:
/*      */         case 24:
/*      */         case 25:
/*      */           break;
/*      */         
/*      */         default:
/*      */           break;
/*      */       } 
/*  113 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 24:
/*  115 */           jj_consume_token(24);
/*  116 */           right = comparisonExpression();
/*  117 */           booleanExpression = ComparisonExpression.createEqual(left, right);
/*      */           continue;
/*      */         case 25:
/*  120 */           jj_consume_token(25);
/*  121 */           right = comparisonExpression();
/*  122 */           booleanExpression = ComparisonExpression.createNotEqual((Expression)booleanExpression, right);
/*      */           continue;
/*      */       } 
/*  125 */       if (jj_2_1(2)) {
/*  126 */         jj_consume_token(15);
/*  127 */         jj_consume_token(18);
/*  128 */         booleanExpression = ComparisonExpression.createIsNull((Expression)booleanExpression); continue;
/*      */       } 
/*  130 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 15:
/*  132 */           jj_consume_token(15);
/*  133 */           jj_consume_token(8);
/*  134 */           jj_consume_token(18);
/*  135 */           booleanExpression = ComparisonExpression.createIsNotNull((Expression)booleanExpression);
/*      */           continue;
/*      */       } 
/*  138 */       jj_consume_token(-1);
/*  139 */       throw new ParseException();
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  144 */     return (Expression)booleanExpression;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Expression comparisonExpression() throws ParseException {
/*      */     BooleanExpression booleanExpression;
/*  156 */     Expression left = addExpression(); while (true) {
/*      */       Expression right; ConstantExpression constantExpression; Expression low, high; String t, u;
/*      */       ArrayList list;
/*  159 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 8:
/*      */         case 11:
/*      */         case 12:
/*      */         case 14:
/*      */         case 26:
/*      */         case 27:
/*      */         case 28:
/*      */         case 29:
/*      */           break;
/*      */         
/*      */         default:
/*      */           break;
/*      */       } 
/*  173 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 26:
/*  175 */           jj_consume_token(26);
/*  176 */           right = addExpression();
/*  177 */           booleanExpression = ComparisonExpression.createGreaterThan(left, right);
/*      */           continue;
/*      */         case 27:
/*  180 */           jj_consume_token(27);
/*  181 */           right = addExpression();
/*  182 */           booleanExpression = ComparisonExpression.createGreaterThanEqual((Expression)booleanExpression, right);
/*      */           continue;
/*      */         case 28:
/*  185 */           jj_consume_token(28);
/*  186 */           right = addExpression();
/*  187 */           booleanExpression = ComparisonExpression.createLessThan((Expression)booleanExpression, right);
/*      */           continue;
/*      */         case 29:
/*  190 */           jj_consume_token(29);
/*  191 */           right = addExpression();
/*  192 */           booleanExpression = ComparisonExpression.createLessThanEqual((Expression)booleanExpression, right);
/*      */           continue;
/*      */         case 12:
/*  195 */           u = null;
/*  196 */           jj_consume_token(12);
/*  197 */           t = stringLitteral();
/*  198 */           switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */             case 13:
/*  200 */               jj_consume_token(13);
/*  201 */               u = stringLitteral();
/*      */               break;
/*      */           } 
/*      */ 
/*      */           
/*  206 */           booleanExpression = ComparisonExpression.createLike((Expression)booleanExpression, t, u);
/*      */           continue;
/*      */       } 
/*  209 */       if (jj_2_2(2)) {
/*  210 */         u = null;
/*  211 */         jj_consume_token(8);
/*  212 */         jj_consume_token(12);
/*  213 */         t = stringLitteral();
/*  214 */         switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */           case 13:
/*  216 */             jj_consume_token(13);
/*  217 */             u = stringLitteral();
/*      */             break;
/*      */         } 
/*      */ 
/*      */         
/*  222 */         booleanExpression = ComparisonExpression.createNotLike((Expression)booleanExpression, t, u); continue;
/*      */       } 
/*  224 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 11:
/*  226 */           jj_consume_token(11);
/*  227 */           low = addExpression();
/*  228 */           jj_consume_token(9);
/*  229 */           high = addExpression();
/*  230 */           booleanExpression = ComparisonExpression.createBetween((Expression)booleanExpression, low, high);
/*      */           continue;
/*      */       } 
/*  233 */       if (jj_2_3(2)) {
/*  234 */         jj_consume_token(8);
/*  235 */         jj_consume_token(11);
/*  236 */         low = addExpression();
/*  237 */         jj_consume_token(9);
/*  238 */         high = addExpression();
/*  239 */         booleanExpression = ComparisonExpression.createNotBetween((Expression)booleanExpression, low, high); continue;
/*      */       } 
/*  241 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 14:
/*  243 */           jj_consume_token(14);
/*  244 */           jj_consume_token(30);
/*  245 */           constantExpression = literal();
/*  246 */           list = new ArrayList();
/*  247 */           list.add(constantExpression);
/*      */           
/*      */           while (true) {
/*  250 */             switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */               case 31:
/*      */                 break;
/*      */               
/*      */               default:
/*      */                 break;
/*      */             } 
/*  257 */             jj_consume_token(31);
/*  258 */             constantExpression = literal();
/*  259 */             list.add(constantExpression);
/*      */           } 
/*  261 */           jj_consume_token(32);
/*  262 */           booleanExpression = ComparisonExpression.createInFilter((Expression)booleanExpression, list);
/*      */           continue;
/*      */       } 
/*  265 */       if (jj_2_4(2)) {
/*  266 */         jj_consume_token(8);
/*  267 */         jj_consume_token(14);
/*  268 */         jj_consume_token(30);
/*  269 */         ConstantExpression constantExpression1 = literal();
/*  270 */         ArrayList arrayList = new ArrayList();
/*  271 */         arrayList.add(constantExpression1);
/*      */         
/*      */         while (true) {
/*  274 */           switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */             case 31:
/*      */               break;
/*      */             
/*      */             default:
/*      */               break;
/*      */           } 
/*  281 */           jj_consume_token(31);
/*  282 */           constantExpression1 = literal();
/*  283 */           arrayList.add(constantExpression1);
/*      */         } 
/*  285 */         jj_consume_token(32);
/*  286 */         booleanExpression = ComparisonExpression.createNotInFilter((Expression)booleanExpression, arrayList); continue;
/*      */       } 
/*  288 */       jj_consume_token(-1);
/*  289 */       throw new ParseException();
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  297 */     return (Expression)booleanExpression;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Expression addExpression() throws ParseException {
/*  304 */     Expression left = multExpr();
/*      */ 
/*      */     
/*  307 */     while (jj_2_5(2147483647)) {
/*      */       Expression right;
/*      */ 
/*      */ 
/*      */       
/*  312 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 33:
/*  314 */           jj_consume_token(33);
/*  315 */           right = multExpr();
/*  316 */           left = ArithmeticExpression.createPlus(left, right);
/*      */           continue;
/*      */         case 34:
/*  319 */           jj_consume_token(34);
/*  320 */           right = multExpr();
/*  321 */           left = ArithmeticExpression.createMinus(left, right);
/*      */           continue;
/*      */       } 
/*  324 */       jj_consume_token(-1);
/*  325 */       throw new ParseException();
/*      */     } 
/*      */     
/*  328 */     return left;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Expression multExpr() throws ParseException {
/*  335 */     Expression left = unaryExpr();
/*      */     while (true) {
/*      */       Expression right;
/*  338 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 35:
/*      */         case 36:
/*      */         case 37:
/*      */           break;
/*      */         
/*      */         default:
/*      */           break;
/*      */       } 
/*  347 */       switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */         case 35:
/*  349 */           jj_consume_token(35);
/*  350 */           right = unaryExpr();
/*  351 */           left = ArithmeticExpression.createMultiply(left, right);
/*      */           continue;
/*      */         case 36:
/*  354 */           jj_consume_token(36);
/*  355 */           right = unaryExpr();
/*  356 */           left = ArithmeticExpression.createDivide(left, right);
/*      */           continue;
/*      */         case 37:
/*  359 */           jj_consume_token(37);
/*  360 */           right = unaryExpr();
/*  361 */           left = ArithmeticExpression.createMod(left, right);
/*      */           continue;
/*      */       } 
/*  364 */       jj_consume_token(-1);
/*  365 */       throw new ParseException();
/*      */     } 
/*      */     
/*  368 */     return left;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Expression unaryExpr() throws ParseException {
/*  373 */     Expression expression = null;
/*  374 */     if (jj_2_6(2147483647))
/*  375 */     { jj_consume_token(33);
/*  376 */       expression = unaryExpr(); }
/*      */     else
/*  378 */     { switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk)
/*      */       { case 34:
/*  380 */           jj_consume_token(34);
/*  381 */           expression = unaryExpr();
/*  382 */           expression = UnaryExpression.createNegate(expression);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  404 */           return expression;case 8: jj_consume_token(8); expression = unaryExpr(); return (Expression)UnaryExpression.createNOT(asBooleanExpression(expression));case 16: case 17: case 18: case 19: case 20: case 22: case 23: case 30: expression = primaryExpr(); return expression; }  jj_consume_token(-1); throw new ParseException(); }  return expression;
/*      */   }
/*      */   
/*      */   public final Expression primaryExpr() throws ParseException { ConstantExpression constantExpression;
/*      */     PropertyExpression propertyExpression;
/*  409 */     Expression expression1, left = null;
/*  410 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 16:
/*      */       case 17:
/*      */       case 18:
/*      */       case 19:
/*      */       case 20:
/*      */       case 22:
/*  417 */         return (Expression)literal();
/*      */       
/*      */       case 23:
/*  420 */         return (Expression)variable();
/*      */       
/*      */       case 30:
/*  423 */         jj_consume_token(30);
/*  424 */         expression1 = orExpression();
/*  425 */         jj_consume_token(32);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  431 */         return expression1;
/*      */     } 
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException(); } public final ConstantExpression literal() throws ParseException {
/*      */     Token t;
/*      */     String s;
/*      */     ConstantExpression.BooleanConstantExpression booleanConstantExpression;
/*  438 */     ConstantExpression left = null;
/*  439 */     switch ((this.jj_ntk == -1) ? jj_ntk() : this.jj_ntk) {
/*      */       case 22:
/*  441 */         s = stringLitteral();
/*  442 */         left = new ConstantExpression(s);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  468 */         return left;case 19: t = jj_consume_token(19); left = ConstantExpression.createInteger(t.image); return left;case 20: t = jj_consume_token(20); left = ConstantExpression.createFloat(t.image); return left;case 16: jj_consume_token(16); booleanConstantExpression = ConstantExpression.TRUE; return (ConstantExpression)booleanConstantExpression;case 17: jj_consume_token(17); booleanConstantExpression = ConstantExpression.FALSE; return (ConstantExpression)booleanConstantExpression;case 18: jj_consume_token(18); booleanConstantExpression = ConstantExpression.NULL; return (ConstantExpression)booleanConstantExpression;
/*      */     } 
/*      */     jj_consume_token(-1);
/*      */     throw new ParseException();
/*      */   }
/*      */   public final String stringLitteral() throws ParseException {
/*  474 */     StringBuffer rc = new StringBuffer();
/*  475 */     boolean first = true;
/*  476 */     Token t = jj_consume_token(22);
/*      */     
/*  478 */     String image = t.image;
/*  479 */     for (int i = 1; i < image.length() - 1; i++) {
/*  480 */       char c = image.charAt(i);
/*  481 */       if (c == '\'')
/*  482 */         i++; 
/*  483 */       rc.append(c);
/*      */     } 
/*  485 */     return rc.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final PropertyExpression variable() throws ParseException {
/*  491 */     PropertyExpression left = null;
/*  492 */     Token t = jj_consume_token(23);
/*  493 */     left = new PropertyExpression(t.image);
/*  494 */     return left;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_2_1(int xla) {
/*  499 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token;
/*  500 */     return !jj_3_1();
/*      */   }
/*      */   
/*      */   private final boolean jj_2_2(int xla) {
/*  504 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token;
/*  505 */     return !jj_3_2();
/*      */   }
/*      */   
/*      */   private final boolean jj_2_3(int xla) {
/*  509 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token;
/*  510 */     return !jj_3_3();
/*      */   }
/*      */   
/*      */   private final boolean jj_2_4(int xla) {
/*  514 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token;
/*  515 */     return !jj_3_4();
/*      */   }
/*      */   
/*      */   private final boolean jj_2_5(int xla) {
/*  519 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token;
/*  520 */     return !jj_3_5();
/*      */   }
/*      */   
/*      */   private final boolean jj_2_6(int xla) {
/*  524 */     this.jj_la = xla; this.jj_lastpos = this.jj_scanpos = this.token;
/*  525 */     return !jj_3_6();
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_54() {
/*  529 */     if (jj_scan_token(33)) return true; 
/*  530 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  531 */     if (jj_3R_11()) return true; 
/*  532 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  533 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_49() {
/*  537 */     if (jj_scan_token(28)) return true; 
/*  538 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  539 */     if (jj_3R_41()) return true; 
/*  540 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  541 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_28() {
/*  545 */     if (jj_3R_36()) return true; 
/*  546 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  547 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_59() {
/*  551 */     if (jj_scan_token(31)) return true; 
/*  552 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  553 */     if (jj_3R_25()) return true; 
/*  554 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  555 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_46() {
/*  560 */     Token xsp = this.jj_scanpos;
/*  561 */     if (jj_3R_54())
/*  562 */     { this.jj_scanpos = xsp;
/*  563 */       if (jj_3R_55()) return true; 
/*  564 */       if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false;  }
/*  565 */     else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }
/*  566 */      return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_35() {
/*  570 */     if (jj_scan_token(10)) return true; 
/*  571 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  572 */     if (jj_3R_34()) return true; 
/*  573 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  574 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_48() {
/*  578 */     if (jj_scan_token(27)) return true; 
/*  579 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  580 */     if (jj_3R_41()) return true; 
/*  581 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  582 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_25() {
/*  587 */     Token xsp = this.jj_scanpos;
/*  588 */     if (jj_3R_28())
/*  589 */     { this.jj_scanpos = xsp;
/*  590 */       if (jj_3R_29())
/*  591 */       { this.jj_scanpos = xsp;
/*  592 */         if (jj_3R_30())
/*  593 */         { this.jj_scanpos = xsp;
/*  594 */           if (jj_3R_31())
/*  595 */           { this.jj_scanpos = xsp;
/*  596 */             if (jj_3R_32())
/*  597 */             { this.jj_scanpos = xsp;
/*  598 */               if (jj_3R_33()) return true; 
/*  599 */               if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false;  }
/*  600 */             else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }  }
/*  601 */           else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }  }
/*  602 */         else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }  }
/*  603 */       else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }  }
/*  604 */     else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }
/*  605 */      return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_47() {
/*  609 */     if (jj_scan_token(26)) return true; 
/*  610 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  611 */     if (jj_3R_41()) return true; 
/*  612 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  613 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_42() {
/*  618 */     Token xsp = this.jj_scanpos;
/*  619 */     if (jj_3R_47())
/*  620 */     { this.jj_scanpos = xsp;
/*  621 */       if (jj_3R_48())
/*  622 */       { this.jj_scanpos = xsp;
/*  623 */         if (jj_3R_49())
/*  624 */         { this.jj_scanpos = xsp;
/*  625 */           if (jj_3R_50())
/*  626 */           { this.jj_scanpos = xsp;
/*  627 */             if (jj_3R_51())
/*  628 */             { this.jj_scanpos = xsp;
/*  629 */               if (jj_3_2())
/*  630 */               { this.jj_scanpos = xsp;
/*  631 */                 if (jj_3R_52())
/*  632 */                 { this.jj_scanpos = xsp;
/*  633 */                   if (jj_3_3())
/*  634 */                   { this.jj_scanpos = xsp;
/*  635 */                     if (jj_3R_53())
/*  636 */                     { this.jj_scanpos = xsp;
/*  637 */                       if (jj_3_4()) return true; 
/*  638 */                       if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false;  }
/*  639 */                     else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }  }
/*  640 */                   else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }  }
/*  641 */                 else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }  }
/*  642 */               else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }  }
/*  643 */             else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }  }
/*  644 */           else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }  }
/*  645 */         else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }  }
/*  646 */       else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }  }
/*  647 */     else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }
/*  648 */      return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_41() {
/*  652 */     if (jj_3R_11()) return true; 
/*  653 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false;
/*      */     
/*      */     while (true) {
/*  656 */       Token xsp = this.jj_scanpos;
/*  657 */       if (jj_3R_46()) { this.jj_scanpos = xsp; break; }
/*  658 */        if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*      */     } 
/*  660 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_27() {
/*  664 */     if (jj_3R_34()) return true; 
/*  665 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false;
/*      */     
/*      */     while (true) {
/*  668 */       Token xsp = this.jj_scanpos;
/*  669 */       if (jj_3R_35()) { this.jj_scanpos = xsp; break; }
/*  670 */        if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*      */     } 
/*  672 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_24() {
/*  676 */     if (jj_scan_token(30)) return true; 
/*  677 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  678 */     if (jj_3R_27()) return true; 
/*  679 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  680 */     if (jj_scan_token(32)) return true; 
/*  681 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  682 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_23() {
/*  686 */     if (jj_3R_26()) return true; 
/*  687 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  688 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_22() {
/*  692 */     if (jj_3R_25()) return true; 
/*  693 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  694 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_58() {
/*  698 */     if (jj_scan_token(31)) return true; 
/*  699 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  700 */     if (jj_3R_25()) return true; 
/*  701 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  702 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_26() {
/*  706 */     if (jj_scan_token(23)) return true; 
/*  707 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  708 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_39() {
/*  712 */     if (jj_3R_41()) return true; 
/*  713 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false;
/*      */     
/*      */     while (true) {
/*  716 */       Token xsp = this.jj_scanpos;
/*  717 */       if (jj_3R_42()) { this.jj_scanpos = xsp; break; }
/*  718 */        if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*      */     } 
/*  720 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_21() {
/*  725 */     Token xsp = this.jj_scanpos;
/*  726 */     if (jj_3R_22())
/*  727 */     { this.jj_scanpos = xsp;
/*  728 */       if (jj_3R_23())
/*  729 */       { this.jj_scanpos = xsp;
/*  730 */         if (jj_3R_24()) return true; 
/*  731 */         if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false;  }
/*  732 */       else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }  }
/*  733 */     else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }
/*  734 */      return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_57() {
/*  738 */     if (jj_scan_token(13)) return true; 
/*  739 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  740 */     if (jj_3R_36()) return true; 
/*  741 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  742 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_4() {
/*  746 */     if (jj_scan_token(8)) return true; 
/*  747 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  748 */     if (jj_scan_token(14)) return true; 
/*  749 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  750 */     if (jj_scan_token(30)) return true; 
/*  751 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  752 */     if (jj_3R_25()) return true; 
/*  753 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false;
/*      */     
/*      */     while (true) {
/*  756 */       Token xsp = this.jj_scanpos;
/*  757 */       if (jj_3R_59()) { this.jj_scanpos = xsp; break; }
/*  758 */        if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*      */     } 
/*  760 */     if (jj_scan_token(32)) return true; 
/*  761 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  762 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_6() {
/*  766 */     if (jj_scan_token(33)) return true; 
/*  767 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  768 */     if (jj_3R_12()) return true; 
/*  769 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  770 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_17() {
/*  774 */     if (jj_3R_21()) return true; 
/*  775 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  776 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_16() {
/*  780 */     if (jj_scan_token(8)) return true; 
/*  781 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  782 */     if (jj_3R_12()) return true; 
/*  783 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  784 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_53() {
/*  788 */     if (jj_scan_token(14)) return true; 
/*  789 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  790 */     if (jj_scan_token(30)) return true; 
/*  791 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  792 */     if (jj_3R_25()) return true; 
/*  793 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false;
/*      */     
/*      */     while (true) {
/*  796 */       Token xsp = this.jj_scanpos;
/*  797 */       if (jj_3R_58()) { this.jj_scanpos = xsp; break; }
/*  798 */        if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*      */     } 
/*  800 */     if (jj_scan_token(32)) return true; 
/*  801 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  802 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_45() {
/*  806 */     if (jj_scan_token(15)) return true; 
/*  807 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  808 */     if (jj_scan_token(8)) return true; 
/*  809 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  810 */     if (jj_scan_token(18)) return true; 
/*  811 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  812 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_14() {
/*  816 */     if (jj_scan_token(33)) return true; 
/*  817 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  818 */     if (jj_3R_12()) return true; 
/*  819 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  820 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_15() {
/*  824 */     if (jj_scan_token(34)) return true; 
/*  825 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  826 */     if (jj_3R_12()) return true; 
/*  827 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  828 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_36() {
/*  832 */     if (jj_scan_token(22)) return true; 
/*  833 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  834 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_1() {
/*  838 */     if (jj_scan_token(15)) return true; 
/*  839 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  840 */     if (jj_scan_token(18)) return true; 
/*  841 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  842 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_12() {
/*  847 */     Token xsp = this.jj_scanpos;
/*  848 */     if (jj_3R_14())
/*  849 */     { this.jj_scanpos = xsp;
/*  850 */       if (jj_3R_15())
/*  851 */       { this.jj_scanpos = xsp;
/*  852 */         if (jj_3R_16())
/*  853 */         { this.jj_scanpos = xsp;
/*  854 */           if (jj_3R_17()) return true; 
/*  855 */           if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false;  }
/*  856 */         else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }  }
/*  857 */       else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }  }
/*  858 */     else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }
/*  859 */      return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_44() {
/*  863 */     if (jj_scan_token(25)) return true; 
/*  864 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  865 */     if (jj_3R_39()) return true; 
/*  866 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  867 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_3() {
/*  871 */     if (jj_scan_token(8)) return true; 
/*  872 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  873 */     if (jj_scan_token(11)) return true; 
/*  874 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  875 */     if (jj_3R_41()) return true; 
/*  876 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  877 */     if (jj_scan_token(9)) return true; 
/*  878 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  879 */     if (jj_3R_41()) return true; 
/*  880 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  881 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_43() {
/*  885 */     if (jj_scan_token(24)) return true; 
/*  886 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  887 */     if (jj_3R_39()) return true; 
/*  888 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  889 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_40() {
/*  894 */     Token xsp = this.jj_scanpos;
/*  895 */     if (jj_3R_43())
/*  896 */     { this.jj_scanpos = xsp;
/*  897 */       if (jj_3R_44())
/*  898 */       { this.jj_scanpos = xsp;
/*  899 */         if (jj_3_1())
/*  900 */         { this.jj_scanpos = xsp;
/*  901 */           if (jj_3R_45()) return true; 
/*  902 */           if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false;  }
/*  903 */         else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }  }
/*  904 */       else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }  }
/*  905 */     else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }
/*  906 */      return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_52() {
/*  910 */     if (jj_scan_token(11)) return true; 
/*  911 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  912 */     if (jj_3R_41()) return true; 
/*  913 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  914 */     if (jj_scan_token(9)) return true; 
/*  915 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  916 */     if (jj_3R_41()) return true; 
/*  917 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  918 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_33() {
/*  922 */     if (jj_scan_token(18)) return true; 
/*  923 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  924 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_56() {
/*  928 */     if (jj_scan_token(13)) return true; 
/*  929 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  930 */     if (jj_3R_36()) return true; 
/*  931 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  932 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_20() {
/*  936 */     if (jj_scan_token(37)) return true; 
/*  937 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  938 */     if (jj_3R_12()) return true; 
/*  939 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  940 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_32() {
/*  944 */     if (jj_scan_token(17)) return true; 
/*  945 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  946 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_37() {
/*  950 */     if (jj_3R_39()) return true; 
/*  951 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false;
/*      */     
/*      */     while (true) {
/*  954 */       Token xsp = this.jj_scanpos;
/*  955 */       if (jj_3R_40()) { this.jj_scanpos = xsp; break; }
/*  956 */        if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*      */     } 
/*  958 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_2() {
/*  962 */     if (jj_scan_token(8)) return true; 
/*  963 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  964 */     if (jj_scan_token(12)) return true; 
/*  965 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  966 */     if (jj_3R_36()) return true; 
/*  967 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false;
/*      */     
/*  969 */     Token xsp = this.jj_scanpos;
/*  970 */     if (jj_3R_57()) { this.jj_scanpos = xsp; }
/*  971 */     else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }
/*  972 */      return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_51() {
/*  976 */     if (jj_scan_token(12)) return true; 
/*  977 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  978 */     if (jj_3R_36()) return true; 
/*  979 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false;
/*      */     
/*  981 */     Token xsp = this.jj_scanpos;
/*  982 */     if (jj_3R_56()) { this.jj_scanpos = xsp; }
/*  983 */     else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }
/*  984 */      return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_19() {
/*  988 */     if (jj_scan_token(36)) return true; 
/*  989 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  990 */     if (jj_3R_12()) return true; 
/*  991 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  992 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_31() {
/*  996 */     if (jj_scan_token(16)) return true; 
/*  997 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*  998 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_18() {
/* 1002 */     if (jj_scan_token(35)) return true; 
/* 1003 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/* 1004 */     if (jj_3R_12()) return true; 
/* 1005 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/* 1006 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3R_13() {
/* 1011 */     Token xsp = this.jj_scanpos;
/* 1012 */     if (jj_3R_18())
/* 1013 */     { this.jj_scanpos = xsp;
/* 1014 */       if (jj_3R_19())
/* 1015 */       { this.jj_scanpos = xsp;
/* 1016 */         if (jj_3R_20()) return true; 
/* 1017 */         if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false;  }
/* 1018 */       else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }  }
/* 1019 */     else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }
/* 1020 */      return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_38() {
/* 1024 */     if (jj_scan_token(9)) return true; 
/* 1025 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/* 1026 */     if (jj_3R_37()) return true; 
/* 1027 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/* 1028 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_30() {
/* 1032 */     if (jj_scan_token(20)) return true; 
/* 1033 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/* 1034 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_11() {
/* 1038 */     if (jj_3R_12()) return true; 
/* 1039 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false;
/*      */     
/*      */     while (true) {
/* 1042 */       Token xsp = this.jj_scanpos;
/* 1043 */       if (jj_3R_13()) { this.jj_scanpos = xsp; break; }
/* 1044 */        if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*      */     } 
/* 1046 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_10() {
/* 1050 */     if (jj_scan_token(34)) return true; 
/* 1051 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/* 1052 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_9() {
/* 1056 */     if (jj_scan_token(33)) return true; 
/* 1057 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/* 1058 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_55() {
/* 1062 */     if (jj_scan_token(34)) return true; 
/* 1063 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/* 1064 */     if (jj_3R_11()) return true; 
/* 1065 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/* 1066 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_34() {
/* 1070 */     if (jj_3R_37()) return true; 
/* 1071 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false;
/*      */     
/*      */     while (true) {
/* 1074 */       Token xsp = this.jj_scanpos;
/* 1075 */       if (jj_3R_38()) { this.jj_scanpos = xsp; break; }
/* 1076 */        if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/*      */     } 
/* 1078 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private final boolean jj_3_5() {
/* 1083 */     Token xsp = this.jj_scanpos;
/* 1084 */     if (jj_3R_9())
/* 1085 */     { this.jj_scanpos = xsp;
/* 1086 */       if (jj_3R_10()) return true; 
/* 1087 */       if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false;  }
/* 1088 */     else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) { return false; }
/* 1089 */      if (jj_3R_11()) return true; 
/* 1090 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/* 1091 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_29() {
/* 1095 */     if (jj_scan_token(19)) return true; 
/* 1096 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/* 1097 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_50() {
/* 1101 */     if (jj_scan_token(29)) return true; 
/* 1102 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/* 1103 */     if (jj_3R_41()) return true; 
/* 1104 */     if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) return false; 
/* 1105 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean lookingAhead = false;
/*      */ 
/*      */   
/*      */   private boolean jj_semLA;
/*      */ 
/*      */ 
/*      */   
/*      */   public SelectorParser(InputStream stream) {
/* 1118 */     this.jj_input_stream = new SimpleCharStream(stream, 1, 1);
/* 1119 */     this.token_source = new SelectorParserTokenManager(this.jj_input_stream);
/* 1120 */     this.token = new Token();
/* 1121 */     this.jj_ntk = -1;
/*      */   }
/*      */   
/*      */   public void ReInit(InputStream stream) {
/* 1125 */     this.jj_input_stream.ReInit(stream, 1, 1);
/* 1126 */     this.token_source.ReInit(this.jj_input_stream);
/* 1127 */     this.token = new Token();
/* 1128 */     this.jj_ntk = -1;
/*      */   }
/*      */   
/*      */   public SelectorParser(Reader stream) {
/* 1132 */     this.jj_input_stream = new SimpleCharStream(stream, 1, 1);
/* 1133 */     this.token_source = new SelectorParserTokenManager(this.jj_input_stream);
/* 1134 */     this.token = new Token();
/* 1135 */     this.jj_ntk = -1;
/*      */   }
/*      */   
/*      */   public void ReInit(Reader stream) {
/* 1139 */     this.jj_input_stream.ReInit(stream, 1, 1);
/* 1140 */     this.token_source.ReInit(this.jj_input_stream);
/* 1141 */     this.token = new Token();
/* 1142 */     this.jj_ntk = -1;
/*      */   }
/*      */   
/*      */   public SelectorParser(SelectorParserTokenManager tm) {
/* 1146 */     this.token_source = tm;
/* 1147 */     this.token = new Token();
/* 1148 */     this.jj_ntk = -1;
/*      */   }
/*      */   
/*      */   public void ReInit(SelectorParserTokenManager tm) {
/* 1152 */     this.token_source = tm;
/* 1153 */     this.token = new Token();
/* 1154 */     this.jj_ntk = -1;
/*      */   }
/*      */   
/*      */   private final Token jj_consume_token(int kind) throws ParseException {
/*      */     Token oldToken;
/* 1159 */     if ((oldToken = this.token).next != null) { this.token = this.token.next; }
/* 1160 */     else { this.token = this.token.next = this.token_source.getNextToken(); }
/* 1161 */      this.jj_ntk = -1;
/* 1162 */     if (this.token.kind == kind) {
/* 1163 */       return this.token;
/*      */     }
/* 1165 */     this.token = oldToken;
/* 1166 */     throw generateParseException();
/*      */   }
/*      */   
/*      */   private final boolean jj_scan_token(int kind) {
/* 1170 */     if (this.jj_scanpos == this.jj_lastpos) {
/* 1171 */       this.jj_la--;
/* 1172 */       if (this.jj_scanpos.next == null) {
/* 1173 */         this.jj_lastpos = this.jj_scanpos = this.jj_scanpos.next = this.token_source.getNextToken();
/*      */       } else {
/* 1175 */         this.jj_lastpos = this.jj_scanpos = this.jj_scanpos.next;
/*      */       } 
/*      */     } else {
/* 1178 */       this.jj_scanpos = this.jj_scanpos.next;
/*      */     } 
/* 1180 */     return (this.jj_scanpos.kind != kind);
/*      */   }
/*      */   
/*      */   public final Token getNextToken() {
/* 1184 */     if (this.token.next != null) { this.token = this.token.next; }
/* 1185 */     else { this.token = this.token.next = this.token_source.getNextToken(); }
/* 1186 */      this.jj_ntk = -1;
/* 1187 */     return this.token;
/*      */   }
/*      */   
/*      */   public final Token getToken(int index) {
/* 1191 */     Token t = this.lookingAhead ? this.jj_scanpos : this.token;
/* 1192 */     for (int i = 0; i < index; i++) {
/* 1193 */       if (t.next != null) { t = t.next; }
/* 1194 */       else { t = t.next = this.token_source.getNextToken(); }
/*      */     
/* 1196 */     }  return t;
/*      */   }
/*      */   
/*      */   private final int jj_ntk() {
/* 1200 */     if ((this.jj_nt = this.token.next) == null) {
/* 1201 */       return this.jj_ntk = (this.token.next = this.token_source.getNextToken()).kind;
/*      */     }
/* 1203 */     return this.jj_ntk = this.jj_nt.kind;
/*      */   }
/*      */   
/*      */   public final ParseException generateParseException() {
/* 1207 */     Token errortok = this.token.next;
/* 1208 */     int line = errortok.beginLine, column = errortok.beginColumn;
/* 1209 */     String mess = (errortok.kind == 0) ? tokenImage[0] : errortok.image;
/* 1210 */     return new ParseException("Parse error at line " + line + ", column " + column + ".  Encountered: " + mess);
/*      */   }
/*      */   
/*      */   public final void enable_tracing() {}
/*      */   
/*      */   public final void disable_tracing() {}
/*      */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\selector\SelectorParser.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */