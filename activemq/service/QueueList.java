/*    */ package org.codehaus.activemq.service;
/*    */ 
/*    */ import javax.jms.JMSException;
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
/*    */ public interface QueueList
/*    */ {
/* 29 */   public static final Object[] EMPTY_ARRAY = new Object[0];
/*    */   
/*    */   Object getFirst() throws JMSException;
/*    */   
/*    */   Object getLast() throws JMSException;
/*    */   
/*    */   Object removeFirst() throws JMSException;
/*    */   
/*    */   void rotate() throws JMSException;
/*    */   
/*    */   Object removeLast() throws JMSException;
/*    */   
/*    */   QueueListEntry addFirst(Object paramObject) throws JMSException;
/*    */   
/*    */   QueueListEntry addLast(Object paramObject) throws JMSException;
/*    */   
/*    */   boolean contains(Object paramObject) throws JMSException;
/*    */   
/*    */   int size() throws JMSException;
/*    */   
/*    */   boolean isEmpty() throws JMSException;
/*    */   
/*    */   QueueListEntry add(Object paramObject) throws JMSException;
/*    */   
/*    */   boolean remove(Object paramObject) throws JMSException;
/*    */   
/*    */   void clear() throws JMSException;
/*    */   
/*    */   Object get(int paramInt) throws JMSException;
/*    */   
/*    */   Object set(int paramInt, Object paramObject) throws JMSException;
/*    */   
/*    */   void add(int paramInt, Object paramObject) throws JMSException;
/*    */   
/*    */   Object remove(int paramInt) throws JMSException;
/*    */   
/*    */   int indexOf(Object paramObject) throws JMSException;
/*    */   
/*    */   int lastIndexOf(Object paramObject) throws JMSException;
/*    */   
/*    */   QueueListEntry getFirstEntry() throws JMSException;
/*    */   
/*    */   QueueListEntry getLastEntry() throws JMSException;
/*    */   
/*    */   QueueListEntry getNextEntry(QueueListEntry paramQueueListEntry) throws JMSException;
/*    */   
/*    */   QueueListEntry getPrevEntry(QueueListEntry paramQueueListEntry) throws JMSException;
/*    */   
/*    */   QueueListEntry addBefore(Object paramObject, QueueListEntry paramQueueListEntry) throws JMSException;
/*    */   
/*    */   void remove(QueueListEntry paramQueueListEntry) throws JMSException;
/*    */   
/*    */   Object[] toArray() throws JMSException;
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\QueueList.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */