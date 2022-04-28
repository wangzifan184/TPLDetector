/*    */ package org.codehaus.activemq.service;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public class MessageIdentity
/*    */   implements Comparable, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -5754338187296859149L;
/*    */   private String messageID;
/*    */   private Object sequenceNumber;
/*    */   
/*    */   public MessageIdentity() {}
/*    */   
/*    */   public MessageIdentity(String messageID) {
/* 43 */     this.messageID = messageID;
/*    */   }
/*    */   
/*    */   public MessageIdentity(String messageID, Object sequenceNumber) {
/* 47 */     this.messageID = messageID;
/* 48 */     this.sequenceNumber = sequenceNumber;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 52 */     return (this.messageID != null) ? (this.messageID.hashCode() ^ 0xCAFEBABE) : -1;
/*    */   }
/*    */   
/*    */   public boolean equals(Object that) {
/* 56 */     return (that instanceof MessageIdentity && equals((MessageIdentity)that));
/*    */   }
/*    */   
/*    */   public boolean equals(MessageIdentity that) {
/* 60 */     return this.messageID.equals(that.messageID);
/*    */   }
/*    */   
/*    */   public int compareTo(Object object) {
/* 64 */     if (this == object) {
/* 65 */       return 0;
/*    */     }
/*    */     
/* 68 */     if (object instanceof MessageIdentity) {
/* 69 */       MessageIdentity that = (MessageIdentity)object;
/* 70 */       return this.messageID.compareTo(that.messageID);
/*    */     } 
/*    */     
/* 73 */     return -1;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 79 */     return super.toString() + "[id=" + this.messageID + "; sequenceNo=" + this.sequenceNumber + "]";
/*    */   }
/*    */   
/*    */   public String getMessageID() {
/* 83 */     return this.messageID;
/*    */   }
/*    */   
/*    */   public void setMessageID(String messageID) {
/* 87 */     this.messageID = messageID;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getSequenceNumber() {
/* 94 */     return this.sequenceNumber;
/*    */   }
/*    */   
/*    */   public void setSequenceNumber(Object sequenceNumber) {
/* 98 */     this.sequenceNumber = sequenceNumber;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\MessageIdentity.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */