/*     */ package org.codehaus.activemq.selector;
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
/*     */ public class TokenMgrError
/*     */   extends Error
/*     */ {
/*     */   static final int LEXICAL_ERROR = 0;
/*     */   static final int STATIC_LEXER_ERROR = 1;
/*     */   static final int INVALID_LEXICAL_STATE = 2;
/*     */   static final int LOOP_DETECTED = 3;
/*     */   int errorCode;
/*     */   
/*     */   protected static final String addEscapes(String str) {
/*  40 */     StringBuffer retval = new StringBuffer();
/*     */     
/*  42 */     for (int i = 0; i < str.length(); i++) {
/*  43 */       char ch; switch (str.charAt(i)) {
/*     */         case '\000':
/*     */           break;
/*     */         case '\b':
/*  47 */           retval.append("\\b");
/*     */           break;
/*     */         case '\t':
/*  50 */           retval.append("\\t");
/*     */           break;
/*     */         case '\n':
/*  53 */           retval.append("\\n");
/*     */           break;
/*     */         case '\f':
/*  56 */           retval.append("\\f");
/*     */           break;
/*     */         case '\r':
/*  59 */           retval.append("\\r");
/*     */           break;
/*     */         case '"':
/*  62 */           retval.append("\\\"");
/*     */           break;
/*     */         case '\'':
/*  65 */           retval.append("\\'");
/*     */           break;
/*     */         case '\\':
/*  68 */           retval.append("\\\\");
/*     */           break;
/*     */         default:
/*  71 */           if ((ch = str.charAt(i)) < ' ' || ch > '~') {
/*  72 */             String s = "0000" + Integer.toString(ch, 16);
/*  73 */             retval.append("\\u" + s.substring(s.length() - 4, s.length()));
/*     */             break;
/*     */           } 
/*  76 */           retval.append(ch);
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/*  81 */     return retval.toString();
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
/*     */   private static final String LexicalError(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, char curChar) {
/*  97 */     return "Lexical error at line " + errorLine + ", column " + errorColumn + ".  Encountered: " + (EOFSeen ? "<EOF> " : ("\"" + addEscapes(String.valueOf(curChar)) + "\"" + " (" + curChar + "), ")) + "after : \"" + addEscapes(errorAfter) + "\"";
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
/*     */   public String getMessage() {
/* 114 */     return super.getMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenMgrError() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenMgrError(String message, int reason) {
/* 125 */     super(message);
/* 126 */     this.errorCode = reason;
/*     */   }
/*     */   
/*     */   public TokenMgrError(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, char curChar, int reason) {
/* 130 */     this(LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar), reason);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\selector\TokenMgrError.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */