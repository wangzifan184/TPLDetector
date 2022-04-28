/*     */ package org.codehaus.activemq.service.impl;
/*     */ 
/*     */ import javax.jms.JMSException;
/*     */ import org.codehaus.activemq.message.MessageAck;
/*     */ import org.codehaus.activemq.service.MessageContainer;
/*     */ import org.codehaus.activemq.service.MessageIdentity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class MessagePointer
/*     */ {
/*     */   private MessageContainer container;
/*     */   private MessageIdentity messageIdentity;
/*     */   private boolean dispatched;
/*     */   private boolean read;
/*     */   private boolean redelivered;
/*     */   
/*     */   public MessagePointer(MessageContainer container, MessageIdentity messageIdentity) throws JMSException {
/*  50 */     this.container = container;
/*  51 */     this.messageIdentity = messageIdentity;
/*  52 */     this.container.registerMessageInterest(this.messageIdentity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/*  61 */     this.dispatched = false;
/*  62 */     this.read = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() throws JMSException {
/*  72 */     this.container.unregisterMessageInterest(this.messageIdentity, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete(MessageAck ack) throws JMSException {
/*  84 */     clear();
/*  85 */     this.container.delete(this.messageIdentity, ack);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageContainer getContainer() {
/*  92 */     return this.container;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContainer(MessageContainer container) {
/*  99 */     this.container = container;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDispatched() {
/* 106 */     return this.dispatched;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDispatched(boolean dispatched) {
/* 113 */     this.dispatched = dispatched;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRead() {
/* 120 */     return this.read;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRead(boolean read) {
/* 127 */     this.read = read;
/*     */   }
/*     */   
/*     */   public MessageIdentity getMessageIdentity() {
/* 131 */     return this.messageIdentity;
/*     */   }
/*     */   
/*     */   public void setMessageIdentity(MessageIdentity messageIdentity) {
/* 135 */     this.messageIdentity = messageIdentity;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRedelivered() {
/* 141 */     return this.redelivered;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRedelivered(boolean redelivered) {
/* 147 */     this.redelivered = redelivered;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\MessagePointer.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */