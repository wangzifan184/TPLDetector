/*    */ package org.codehaus.activemq.filter;
/*    */ 
/*    */ import javax.jms.Destination;
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
/*    */ public class WildcardDestinationFilter
/*    */   extends DestinationFilter
/*    */ {
/*    */   private String[] prefixes;
/*    */   
/*    */   public WildcardDestinationFilter(String[] prefixes) {
/* 39 */     this.prefixes = new String[prefixes.length];
/* 40 */     for (int i = 0; i < prefixes.length; i++) {
/* 41 */       String prefix = prefixes[i];
/* 42 */       if (!prefix.equals("*")) {
/* 43 */         this.prefixes[i] = prefix;
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean matches(Destination destination) {
/* 49 */     String[] path = DestinationPath.getDestinationPaths(destination);
/* 50 */     int length = this.prefixes.length;
/* 51 */     if (path.length == length) {
/* 52 */       for (int i = 0, size = length; i < size; i++) {
/* 53 */         String prefix = this.prefixes[i];
/* 54 */         if (prefix != null && !prefix.equals(path[i])) {
/* 55 */           return false;
/*    */         }
/*    */       } 
/* 58 */       return true;
/*    */     } 
/* 60 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getText() {
/* 65 */     return DestinationPath.toString(this.prefixes);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 69 */     return super.toString() + "[destination: " + getText() + "]";
/*    */   }
/*    */   
/*    */   public boolean isWildcard() {
/* 73 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\filter\WildcardDestinationFilter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */