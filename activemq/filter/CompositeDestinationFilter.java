/*    */ package org.codehaus.activemq.filter;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import javax.jms.Destination;
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
/*    */ public class CompositeDestinationFilter
/*    */   extends DestinationFilter
/*    */ {
/*    */   private List filters;
/*    */   
/*    */   public CompositeDestinationFilter(ActiveMQDestination destination) {
/* 36 */     List childDestinations = destination.getChildDestinations();
/* 37 */     this.filters = new ArrayList(childDestinations.size());
/* 38 */     for (Iterator iter = childDestinations.iterator(); iter.hasNext(); ) {
/* 39 */       Destination childDestination = iter.next();
/* 40 */       this.filters.add(DestinationFilter.parseFilter(childDestination));
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean matches(Destination destination) {
/* 45 */     for (Iterator iter = this.filters.iterator(); iter.hasNext(); ) {
/* 46 */       DestinationFilter filter = iter.next();
/* 47 */       if (filter.matches(destination)) {
/* 48 */         return true;
/*    */       }
/*    */     } 
/* 51 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isWildcard() {
/* 55 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\filter\CompositeDestinationFilter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */