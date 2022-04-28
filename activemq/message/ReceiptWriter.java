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
/*    */ 
/*    */ public class ReceiptWriter
/*    */   extends AbstractPacketWriter
/*    */ {
/*    */   public int getPacketType() {
/* 39 */     return 16;
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
/* 52 */     super.writePacket(packet, dataOut);
/* 53 */     Receipt info = (Receipt)packet;
/* 54 */     writeUTF(info.getCorrelationId(), dataOut);
/* 55 */     writeUTF(info.getBrokerName(), dataOut);
/* 56 */     writeUTF(info.getClusterName(), dataOut);
/* 57 */     dataOut.writeBoolean(info.isFailed());
/* 58 */     writeObject(info.getException(), dataOut);
/* 59 */     dataOut.writeByte(info.getBrokerMessageCapacity());
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ReceiptWriter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */