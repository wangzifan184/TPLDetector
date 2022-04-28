/*     */ package org.codehaus.activemq.message;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.MessageNotWriteableException;
/*     */ import javax.jms.ObjectMessage;
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
/*     */ public class ActiveMQObjectMessage
/*     */   extends ActiveMQMessage
/*     */   implements ObjectMessage
/*     */ {
/*     */   private Serializable object;
/*     */   
/*     */   public int getPacketType() {
/*  71 */     return 8;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQMessage shallowCopy() throws JMSException {
/*  80 */     ActiveMQObjectMessage other = new ActiveMQObjectMessage();
/*  81 */     initializeOther(other);
/*  82 */     other.object = this.object;
/*  83 */     return other;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQMessage deepCopy() throws JMSException {
/*  92 */     return shallowCopy();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearBody() throws JMSException {
/* 108 */     super.clearBody();
/* 109 */     this.object = null;
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
/*     */   public void setObject(Serializable newObject) throws JMSException {
/* 131 */     if (this.readOnlyMessage) {
/* 132 */       throw new MessageNotWriteableException("The message is read-only");
/*     */     }
/* 134 */     this.object = newObject;
/*     */   }
/*     */   
/*     */   void setObjectPayload(Object newObject) {
/* 138 */     this.object = (Serializable)newObject;
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
/*     */   
/*     */   public Serializable getObject() throws JMSException {
/* 151 */     if (this.object == null) {
/*     */       try {
/* 153 */         buildBodyFromBytes();
/*     */       }
/* 155 */       catch (IOException ioe) {
/* 156 */         JMSException jmsEx = new JMSException("failed to build body from bytes");
/* 157 */         jmsEx.setLinkedException(ioe);
/* 158 */         throw jmsEx;
/*     */       } 
/*     */     }
/* 161 */     return this.object;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepareMessageBody() throws JMSException {
/*     */     try {
/* 170 */       convertBodyToBytes();
/* 171 */       this.object = null;
/*     */     }
/* 173 */     catch (IOException ioe) {
/* 174 */       JMSException jmsEx = new JMSException("failed to convert body to bytes");
/* 175 */       jmsEx.setLinkedException(ioe);
/* 176 */       throw jmsEx;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeBody(DataOutput dataOut) throws IOException {
/* 188 */     ObjectOutputStream objOut = new ObjectOutputStream((OutputStream)dataOut);
/* 189 */     objOut.writeObject(this.object);
/* 190 */     objOut.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readBody(DataInput dataIn) throws IOException {
/* 201 */     ObjectInputStream objIn = new ObjectInputStream((InputStream)dataIn);
/*     */     try {
/* 203 */       this.object = (Serializable)objIn.readObject();
/* 204 */       objIn.close();
/*     */     }
/* 206 */     catch (ClassNotFoundException ex) {
/* 207 */       throw new IOException(ex.getMessage());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ActiveMQObjectMessage.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */