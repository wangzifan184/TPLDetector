/*    */ package org.codehaus.activemq.filter;
/*    */ 
/*    */ import javax.jms.Destination;
/*    */ import javax.jms.JMSException;
/*    */ import javax.jms.Message;
/*    */ import org.codehaus.activemq.message.ActiveMQDestination;
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
/*    */ public abstract class DestinationFilter
/*    */   implements Filter
/*    */ {
/*    */   public static final String ANY_DESCENDENT = ">";
/*    */   public static final String ANY_CHILD = "*";
/*    */   
/*    */   public boolean matches(Message message) throws JMSException {
/* 38 */     return matches(message.getJMSDestination());
/*    */   }
/*    */   
/*    */   public abstract boolean matches(Destination paramDestination);
/*    */   
/*    */   public static DestinationFilter parseFilter(Destination destination) {
/* 44 */     if (destination instanceof ActiveMQDestination) {
/* 45 */       ActiveMQDestination activeDestination = (ActiveMQDestination)destination;
/* 46 */       if (activeDestination.isComposite()) {
/* 47 */         return new CompositeDestinationFilter(activeDestination);
/*    */       }
/*    */     } 
/* 50 */     String[] paths = DestinationPath.getDestinationPaths(destination);
/* 51 */     int idx = paths.length - 1;
/* 52 */     if (idx >= 0) {
/* 53 */       String lastPath = paths[idx];
/* 54 */       if (lastPath.equals(">")) {
/* 55 */         return new PrefixDestinationFilter(paths);
/*    */       }
/* 57 */       if (lastPath.equals("*")) {
/* 58 */         return new WildcardDestinationFilter(paths);
/*    */       }
/*    */     } 
/*    */ 
/*    */     
/* 63 */     return new SimpleDestinationFilter(destination);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\filter\DestinationFilter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */