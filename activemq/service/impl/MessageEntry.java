/*    */ package org.codehaus.activemq.service.impl;
/*    */ 
/*    */ import java.io.Externalizable;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInput;
/*    */ import java.io.ObjectOutput;
/*    */ import javax.jms.JMSException;
/*    */ import org.codehaus.activemq.message.ActiveMQMessage;
/*    */ import org.codehaus.activemq.message.DefaultWireFormat;
/*    */ import org.codehaus.activemq.message.Packet;
/*    */ import org.codehaus.activemq.message.WireFormat;
/*    */ import org.codehaus.activemq.util.JMSExceptionHelper;
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
/*    */ public class MessageEntry
/*    */   implements Externalizable
/*    */ {
/*    */   private static final long serialVersionUID = -3590625465815936811L;
/* 39 */   private static final WireFormat wireFormat = (WireFormat)new DefaultWireFormat();
/*    */ 
/*    */   
/*    */   private ActiveMQMessage message;
/*    */ 
/*    */ 
/*    */   
/*    */   public MessageEntry() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public MessageEntry(ActiveMQMessage msg) {
/* 51 */     this.message = msg;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ActiveMQMessage getMessage() {
/* 59 */     return this.message;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 66 */     return (this.message != null) ? this.message.hashCode() : super.hashCode();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 77 */     boolean result = false;
/* 78 */     if (obj != null && obj instanceof MessageEntry) {
/* 79 */       MessageEntry other = (MessageEntry)obj;
/* 80 */       result = ((this.message != null && other.message != null && this.message.equals(other.message)) || (this.message == null && other.message == null));
/*    */     } 
/*    */     
/* 83 */     return result;
/*    */   }
/*    */   
/*    */   public void writeExternal(ObjectOutput out) throws IOException {
/*    */     try {
/* 88 */       wireFormat.writePacket((Packet)this.message, out);
/*    */     }
/* 90 */     catch (JMSException e) {
/* 91 */       throw JMSExceptionHelper.newIOException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
/* 96 */     this.message = (ActiveMQMessage)wireFormat.readPacket(in);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\MessageEntry.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */