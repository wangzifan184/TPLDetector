/*    */ package org.codehaus.activemq.selector;
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
/*    */ public class Token
/*    */ {
/*    */   public int kind;
/*    */   public int beginLine;
/*    */   public int beginColumn;
/*    */   public int endLine;
/*    */   public int endColumn;
/*    */   public String image;
/*    */   public Token next;
/*    */   public Token specialToken;
/*    */   
/*    */   public final String toString() {
/* 57 */     return this.image;
/*    */   }
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
/*    */   public static final Token newToken(int ofKind) {
/* 73 */     switch (ofKind) {
/*    */     
/* 75 */     }  return new Token();
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\selector\Token.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */