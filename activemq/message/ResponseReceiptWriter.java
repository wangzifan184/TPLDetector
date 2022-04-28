/*    */ package org.codehaus.activemq.message;
/*    */ 
/*    */ import java.io.DataOutput;
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
/*    */ 
/*    */ public class ResponseReceiptWriter
/*    */   extends ReceiptWriter
/*    */ {
/*    */   public int getPacketType() {
/* 38 */     return 25;
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
/*    */   public void writePacket(Packet packet, DataOutput dataOut) throws IOException {
/* 51 */     super.writePacket(packet, dataOut);
/* 52 */     ResponseReceipt info = (ResponseReceipt)packet;
/* 53 */     byte[] data = info.getResultBytes();
/* 54 */     if (data == null) {
/* 55 */       dataOut.writeInt(0);
/*    */     } else {
/*    */       
/* 58 */       dataOut.writeInt(data.length);
/* 59 */       dataOut.write(data);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ResponseReceiptWriter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */