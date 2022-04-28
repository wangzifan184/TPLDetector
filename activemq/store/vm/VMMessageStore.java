/*    */ package org.codehaus.activemq.store.vm;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import javax.jms.JMSException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.codehaus.activemq.message.ActiveMQMessage;
/*    */ import org.codehaus.activemq.message.MessageAck;
/*    */ import org.codehaus.activemq.service.MessageIdentity;
/*    */ import org.codehaus.activemq.service.QueueMessageContainer;
/*    */ import org.codehaus.activemq.store.MessageStore;
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
/*    */ public class VMMessageStore
/*    */   implements MessageStore
/*    */ {
/* 39 */   private static final Log log = LogFactory.getLog(VMMessageStore.class);
/*    */   
/*    */   protected Map messageTable;
/*    */   
/*    */   public VMMessageStore() {
/* 44 */     this(new LinkedHashMap());
/*    */   }
/*    */   
/*    */   public VMMessageStore(LinkedHashMap messageTable) {
/* 48 */     this.messageTable = Collections.synchronizedMap(messageTable);
/*    */   }
/*    */   
/*    */   public MessageIdentity addMessage(ActiveMQMessage message) throws JMSException {
/* 52 */     this.messageTable.put(message.getJMSMessageID(), message);
/* 53 */     return message.getJMSMessageIdentity();
/*    */   }
/*    */   
/*    */   public ActiveMQMessage getMessage(MessageIdentity identity) throws JMSException {
/* 57 */     String messageID = identity.getMessageID();
/* 58 */     return (ActiveMQMessage)this.messageTable.get(messageID);
/*    */   }
/*    */   
/*    */   public void removeMessage(MessageIdentity identity, MessageAck ack) throws JMSException {
/* 62 */     String messageID = identity.getMessageID();
/* 63 */     this.messageTable.remove(messageID);
/*    */   }
/*    */ 
/*    */   
/*    */   public void recover(QueueMessageContainer container) throws JMSException {}
/*    */ 
/*    */   
/*    */   public void start() throws JMSException {}
/*    */   
/*    */   public void stop() throws JMSException {
/* 73 */     this.messageTable.clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\vm\VMMessageStore.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */