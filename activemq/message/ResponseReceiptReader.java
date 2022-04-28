/*    */ package org.codehaus.activemq.message;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
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
/*    */ public class ResponseReceiptReader
/*    */   extends ReceiptReader
/*    */ {
/*    */   public int getPacketType() {
/* 37 */     return 25;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Packet createPacket() {
/* 45 */     return new ResponseReceipt();
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
/*    */   
/*    */   public void buildPacket(Packet packet, DataInput dataIn) throws IOException {
/* 58 */     super.buildPacket(packet, dataIn);
/* 59 */     ResponseReceipt info = (ResponseReceipt)packet;
/* 60 */     int size = dataIn.readInt();
/* 61 */     byte[] data = null;
/* 62 */     if (size != 0) {
/* 63 */       data = new byte[size];
/* 64 */       dataIn.readFully(data);
/*    */     } 
/* 66 */     info.setResultBytes(data);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ResponseReceiptReader.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */