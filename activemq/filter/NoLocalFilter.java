/*    */ package org.codehaus.activemq.filter;
/*    */ 
/*    */ import javax.jms.JMSException;
/*    */ import javax.jms.Message;
/*    */ import org.codehaus.activemq.message.ActiveMQMessage;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NoLocalFilter
/*    */   implements Filter
/*    */ {
/*    */   private String clientId;
/*    */   
/*    */   public NoLocalFilter(String newClientId) {
/* 42 */     this.clientId = newClientId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(Message message) throws JMSException {
/* 54 */     if (message != null && message instanceof ActiveMQMessage) {
/* 55 */       ActiveMQMessage activeMQMessage = (ActiveMQMessage)message;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 61 */       String producerID = activeMQMessage.getProducerID();
/* 62 */       if (producerID != null && 
/* 63 */         producerID.equals(this.clientId)) {
/* 64 */         return false;
/*    */       }
/*    */       
/* 67 */       if (this.clientId.equals(activeMQMessage.getJMSClientID())) {
/* 68 */         return false;
/*    */       }
/*    */     } 
/* 71 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isWildcard() {
/* 75 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\filter\NoLocalFilter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */