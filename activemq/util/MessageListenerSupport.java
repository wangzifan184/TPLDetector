/*    */ package org.codehaus.activemq.util;
/*    */ 
/*    */ import javax.jms.ExceptionListener;
/*    */ import javax.jms.JMSException;
/*    */ import javax.jms.Message;
/*    */ import javax.jms.MessageListener;
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
/*    */ public abstract class MessageListenerSupport
/*    */   implements MessageListener
/*    */ {
/*    */   private ExceptionListener exceptionListener;
/*    */   
/*    */   public void onMessage(Message message) {
/*    */     try {
/* 39 */       processMessage(message);
/*    */     }
/* 41 */     catch (JMSException e) {
/* 42 */       onJMSException(e, message);
/*    */     }
/* 44 */     catch (Exception e) {
/*    */       
/* 46 */       JMSException jmsEx = JMSExceptionHelper.newJMSException(e.getMessage(), e);
/* 47 */       onJMSException(jmsEx, message);
/*    */     } 
/*    */   }
/*    */   
/*    */   public ExceptionListener getExceptionListener() {
/* 52 */     return this.exceptionListener;
/*    */   }
/*    */   
/*    */   public void setExceptionListener(ExceptionListener exceptionListener) {
/* 56 */     this.exceptionListener = exceptionListener;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract void processMessage(Message paramMessage) throws Exception;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onJMSException(JMSException e, Message message) {
/* 76 */     if (this.exceptionListener != null) {
/* 77 */       this.exceptionListener.onException(e);
/*    */     } else {
/*    */       
/* 80 */       throw new RuntimeException("Failed to process message: " + message + " Reason: " + e, e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activem\\util\MessageListenerSupport.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */