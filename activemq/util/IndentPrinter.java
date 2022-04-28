/*    */ package org.codehaus.activemq.util;
/*    */ 
/*    */ import java.io.PrintWriter;
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
/*    */ public class IndentPrinter
/*    */ {
/*    */   private int indentLevel;
/*    */   private String indent;
/*    */   private PrintWriter out;
/*    */   
/*    */   public IndentPrinter() {
/* 34 */     this(new PrintWriter(System.out), "  ");
/*    */   }
/*    */   
/*    */   public IndentPrinter(PrintWriter out) {
/* 38 */     this(out, "  ");
/*    */   }
/*    */   
/*    */   public IndentPrinter(PrintWriter out, String indent) {
/* 42 */     this.out = out;
/* 43 */     this.indent = indent;
/*    */   }
/*    */   
/*    */   public void println(Object value) {
/* 47 */     this.out.print(value.toString());
/* 48 */     this.out.println();
/*    */   }
/*    */   
/*    */   public void println(String text) {
/* 52 */     this.out.print(text);
/* 53 */     this.out.println();
/*    */   }
/*    */   
/*    */   public void print(String text) {
/* 57 */     this.out.print(text);
/*    */   }
/*    */   
/*    */   public void printIndent() {
/* 61 */     for (int i = 0; i < this.indentLevel; i++) {
/* 62 */       this.out.print(this.indent);
/*    */     }
/*    */   }
/*    */   
/*    */   public void println() {
/* 67 */     this.out.println();
/*    */   }
/*    */   
/*    */   public void incrementIndent() {
/* 71 */     this.indentLevel++;
/*    */   }
/*    */   
/*    */   public void decrementIndent() {
/* 75 */     this.indentLevel--;
/*    */   }
/*    */   
/*    */   public int getIndentLevel() {
/* 79 */     return this.indentLevel;
/*    */   }
/*    */   
/*    */   public void setIndentLevel(int indentLevel) {
/* 83 */     this.indentLevel = indentLevel;
/*    */   }
/*    */   
/*    */   public void flush() {
/* 87 */     this.out.flush();
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activem\\util\IndentPrinter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */