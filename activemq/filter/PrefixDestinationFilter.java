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
/*    */ public class PrefixDestinationFilter
/*    */   extends DestinationFilter
/*    */ {
/*    */   private String[] prefixes;
/*    */   
/*    */   public PrefixDestinationFilter(String[] prefixes) {
/* 39 */     this.prefixes = prefixes;
/*    */   }
/*    */   
/*    */   public boolean matches(Destination destination) {
/* 43 */     String[] path = DestinationPath.getDestinationPaths(destination);
/* 44 */     int length = this.prefixes.length;
/* 45 */     if (path.length >= length) {
/* 46 */       for (int i = 0, size = length - 1; i < size; i++) {
/* 47 */         if (!this.prefixes[i].equals(path[i])) {
/* 48 */           return false;
/*    */         }
/*    */       } 
/* 51 */       return true;
/*    */     } 
/* 53 */     return false;
/*    */   }
/*    */   
/*    */   public String getText() {
/* 57 */     return DestinationPath.toString(this.prefixes);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 61 */     return super.toString() + "[destination: " + getText() + "]";
/*    */   }
/*    */   
/*    */   public boolean isWildcard() {
/* 65 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\filter\PrefixDestinationFilter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */