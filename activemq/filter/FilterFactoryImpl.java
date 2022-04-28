/*    */ package org.codehaus.activemq.filter;
/*    */ 
/*    */ import javax.jms.Destination;
/*    */ import javax.jms.JMSException;
/*    */ import org.codehaus.activemq.selector.SelectorParser;
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
/*    */ public class FilterFactoryImpl
/*    */   implements FilterFactory
/*    */ {
/*    */   public Filter createFilter(Destination destination, String selector) throws JMSException {
/* 34 */     Filter destinationFilter = DestinationFilter.parseFilter(destination);
/* 35 */     if (selector != null && selector.trim().length() > 0) {
/* 36 */       Filter selectorFilter = (new SelectorParser()).parse(selector);
/* 37 */       if (selectorFilter != null) {
/* 38 */         return new AndFilter(destinationFilter, selectorFilter);
/*    */       }
/*    */     } 
/* 41 */     return destinationFilter;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\filter\FilterFactoryImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */