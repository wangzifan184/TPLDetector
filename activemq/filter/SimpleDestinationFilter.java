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
/*    */ public class SimpleDestinationFilter
/*    */   extends DestinationFilter
/*    */ {
/*    */   private Destination destination;
/*    */   
/*    */   public SimpleDestinationFilter(Destination destination) {
/* 34 */     this.destination = destination;
/*    */   }
/*    */   
/*    */   public boolean matches(Destination destination) {
/* 38 */     return this.destination.equals(destination);
/*    */   }
/*    */   
/*    */   public boolean isWildcard() {
/* 42 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\filter\SimpleDestinationFilter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */