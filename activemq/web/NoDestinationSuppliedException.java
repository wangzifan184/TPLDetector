/*    */ package org.codehaus.activemq.web;
/*    */ 
/*    */ import javax.servlet.ServletException;
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
/*    */ public class NoDestinationSuppliedException
/*    */   extends ServletException
/*    */ {
/*    */   public NoDestinationSuppliedException() {
/* 30 */     super("Could not perform the JMS operation as no Destination was supplied");
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\web\NoDestinationSuppliedException.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */