/*    */ package org.codehaus.activemq.message;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.Serializable;
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
/*    */ public class ResponseReceipt
/*    */   extends Receipt
/*    */ {
/*    */   private Serializable result;
/*    */   private byte[] resultBytes;
/*    */   
/*    */   public int getPacketType() {
/* 40 */     return 25;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Serializable getResult() throws JMSException {
/* 47 */     if (this.result == null) {
/* 48 */       if (this.resultBytes == null) {
/* 49 */         return null;
/*    */       }
/*    */       try {
/* 52 */         this.result = (Serializable)(new ObjectInputStream(new ByteArrayInputStream(this.resultBytes))).readObject();
/*    */       }
/* 54 */       catch (Exception e) {
/* 55 */         throw (JMSException)(new JMSException("Invalid network mesage received.")).initCause(e);
/*    */       } 
/*    */     } 
/* 58 */     return this.result;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setResult(Serializable result) {
/* 65 */     this.result = result;
/* 66 */     this.resultBytes = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setResultBytes(byte[] resultBytes) {
/* 73 */     this.resultBytes = resultBytes;
/* 74 */     this.result = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] getResultBytes() throws IOException {
/* 81 */     if (this.resultBytes == null) {
/* 82 */       if (this.result == null) {
/* 83 */         return null;
/*    */       }
/* 85 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 86 */       ObjectOutputStream os = new ObjectOutputStream(baos);
/* 87 */       os.writeObject(this.result);
/* 88 */       os.close();
/* 89 */       this.resultBytes = baos.toByteArray();
/*    */     } 
/* 91 */     return this.resultBytes;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ResponseReceipt.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */