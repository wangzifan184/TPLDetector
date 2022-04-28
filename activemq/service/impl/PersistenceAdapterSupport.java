/*    */ package org.codehaus.activemq.service.impl;
/*    */ 
/*    */ import javax.jms.JMSException;
/*    */ import org.codehaus.activemq.service.QueueMessageContainer;
/*    */ import org.codehaus.activemq.service.TopicMessageContainer;
/*    */ import org.codehaus.activemq.store.PersistenceAdapter;
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
/*    */ public abstract class PersistenceAdapterSupport
/*    */   implements PersistenceAdapter
/*    */ {
/*    */   public TopicMessageContainer createTopicMessageContainer(String destinationName) throws JMSException {
/* 34 */     return new DurableTopicMessageContainer(createTopicMessageStore(destinationName), destinationName);
/*    */   }
/*    */   
/*    */   public QueueMessageContainer createQueueMessageContainer(String destinationName) throws JMSException {
/* 38 */     return new DurableQueueMessageContainer(this, createQueueMessageStore(destinationName), destinationName);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\PersistenceAdapterSupport.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */