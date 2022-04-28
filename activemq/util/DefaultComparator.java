/*    */ package org.codehaus.activemq.util;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
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
/*    */ public class DefaultComparator
/*    */   implements Comparator, Serializable
/*    */ {
/*    */   static final long serialVersionUID = 1L;
/*    */   
/*    */   public int compare(Object o1, Object o2) {
/* 33 */     Comparable c1 = (Comparable)o1;
/* 34 */     return c1.compareTo(o2);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activem\\util\DefaultComparator.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */