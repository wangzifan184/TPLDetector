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
/*    */ public class ReceiptReader
/*    */   extends AbstractPacketReader
/*    */ {
/*    */   public int getPacketType() {
/* 38 */     return 16;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Packet createPacket() {
/* 46 */     return new Receipt();
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
/* 60 */     Receipt info = (Receipt)packet;
/* 61 */     info.setCorrelationId(dataIn.readUTF());
/* 62 */     info.setBrokerName(dataIn.readUTF());
/* 63 */     info.setClusterName(dataIn.readUTF());
/* 64 */     info.setFailed(dataIn.readBoolean());
/* 65 */     info.setException((Throwable)readObject(dataIn));
/* 66 */     info.setBrokerMessageCapacity(dataIn.readByte());
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ReceiptReader.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */