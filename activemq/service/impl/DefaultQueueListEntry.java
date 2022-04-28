/*    */ package org.codehaus.activemq.service.impl;
/*    */ 
/*    */ import org.codehaus.activemq.service.QueueListEntry;
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
/*    */ public final class DefaultQueueListEntry
/*    */   implements QueueListEntry
/*    */ {
/*    */   Object element;
/*    */   DefaultQueueListEntry next;
/*    */   DefaultQueueListEntry previous;
/*    */   
/*    */   public Object getElement() {
/* 36 */     return this.element;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   DefaultQueueListEntry(Object element, DefaultQueueListEntry next, DefaultQueueListEntry previous) {
/* 47 */     this.element = element;
/* 48 */     this.next = next;
/* 49 */     this.previous = previous;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\DefaultQueueListEntry.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */