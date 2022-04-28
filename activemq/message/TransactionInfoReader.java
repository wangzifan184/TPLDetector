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
/*    */ 
/*    */ public class TransactionInfoReader
/*    */   extends AbstractPacketReader
/*    */ {
/*    */   public int getPacketType() {
/* 38 */     return 19;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Packet createPacket() {
/* 46 */     return new TransactionInfo();
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
/* 59 */     super.buildPacket(packet, dataIn);
/* 60 */     TransactionInfo info = (TransactionInfo)packet;
/* 61 */     info.setTransactionId(dataIn.readUTF());
/* 62 */     info.setType(dataIn.readByte());
/* 63 */     info.setReceiptRequired(dataIn.readBoolean());
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\TransactionInfoReader.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */