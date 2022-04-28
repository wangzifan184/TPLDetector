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
/*    */ public class ProducerInfoWriter
/*    */   extends AbstractPacketWriter
/*    */ {
/*    */   public int getPacketType() {
/* 39 */     return 18;
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
/*    */   public void writePacket(Packet packet, DataOutput dataOut) throws IOException {
/* 51 */     super.writePacket(packet, dataOut);
/* 52 */     ProducerInfo info = (ProducerInfo)packet;
/* 53 */     writeUTF(info.getProducerId(), dataOut);
/* 54 */     writeUTF(info.getClientId(), dataOut);
/* 55 */     writeUTF(info.getSessionId(), dataOut);
/* 56 */     dataOut.writeLong(info.getStartTime());
/* 57 */     dataOut.writeBoolean(info.isStarted());
/* 58 */     ActiveMQDestination.writeToStream(info.getDestination(), dataOut);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ProducerInfoWriter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */