/*     */ package org.codehaus.activemq.message;
/*     */ 
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
/*     */ public class MessageAck
/*     */   extends AbstractPacket
/*     */ {
/*     */   static final int MESSAGE_READ_INDEX = 0;
/*     */   static final int XA_TRANSACTED_INDEX = 1;
/*     */   static final int PERSISTENT_INDEX = 2;
/*     */   private String consumerId;
/*     */   private String messageID;
/*     */   private ActiveMQDestination destination;
/*     */   private String transactionId;
/*     */   private boolean messageRead;
/*     */   private boolean xaTransacted;
/*     */   private boolean persistent;
/*     */   private transient MessageIdentity messageIdentity;
/*     */   
/*     */   public int getPacketType() {
/*  49 */     return 15;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  56 */     String str = super.toString();
/*  57 */     str = str + " consumerId = " + this.consumerId;
/*  58 */     str = str + " , messageId = " + this.messageID;
/*  59 */     str = str + " ,read = " + this.messageRead;
/*  60 */     str = str + " ,trans = " + this.transactionId;
/*  61 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTransactionId() {
/*  69 */     return this.transactionId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTransactionId(String newTransactionId) {
/*  76 */     this.transactionId = newTransactionId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPartOfTransaction() {
/*  84 */     return (this.transactionId != null && this.transactionId.length() > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessageID() {
/*  92 */     return this.messageID;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageID(String messageID) {
/*  99 */     this.messageID = messageID;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMessageRead() {
/* 106 */     return this.messageRead;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageRead(boolean messageRead) {
/* 113 */     this.messageRead = messageRead;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getConsumerId() {
/* 120 */     return this.consumerId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConsumerId(String consumerId) {
/* 127 */     this.consumerId = consumerId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isXaTransacted() {
/* 134 */     return this.xaTransacted;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setXaTransacted(boolean xaTransacted) {
/* 141 */     this.xaTransacted = xaTransacted;
/*     */   }
/*     */   
/*     */   public MessageIdentity getMessageIdentity() {
/* 145 */     if (this.messageIdentity == null) {
/* 146 */       this.messageIdentity = new MessageIdentity(this.messageID);
/*     */     }
/* 148 */     return this.messageIdentity;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQDestination getDestination() {
/* 154 */     return this.destination;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestination(ActiveMQDestination destination) {
/* 160 */     this.destination = destination;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPersistent() {
/* 166 */     return this.persistent;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPersistent(boolean persistent) {
/* 172 */     this.persistent = persistent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTemporary() {
/* 179 */     return (!this.persistent || (this.destination != null && this.destination.isTemporary()));
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\MessageAck.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */