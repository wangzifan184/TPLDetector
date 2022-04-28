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
/*    */ public class ConsumerInfoWriter
/*    */   extends AbstractPacketWriter
/*    */ {
/*    */   public int getPacketType() {
/* 39 */     return 17;
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
/* 52 */     ConsumerInfo info = (ConsumerInfo)packet;
/* 53 */     writeUTF(info.getConsumerId(), dataOut);
/* 54 */     writeUTF(info.getClientId(), dataOut);
/* 55 */     writeUTF(info.getSessionId(), dataOut);
/* 56 */     writeUTF(info.getSelector(), dataOut);
/* 57 */     writeUTF(info.getConsumerName(), dataOut);
/* 58 */     dataOut.writeInt(info.getConsumerNo());
/* 59 */     dataOut.writeShort(info.getPrefetchNumber());
/* 60 */     dataOut.writeLong(info.getStartTime());
/* 61 */     dataOut.writeBoolean(info.isStarted());
/* 62 */     dataOut.writeBoolean(info.isReceiptRequired());
/* 63 */     dataOut.writeBoolean(info.isNoLocal());
/* 64 */     dataOut.writeBoolean(info.isBrowser());
/* 65 */     ActiveMQDestination.writeToStream(info.getDestination(), dataOut);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ConsumerInfoWriter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */