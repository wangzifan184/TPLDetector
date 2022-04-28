/*    */ package org.codehaus.activemq.message;
/*    */ 
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ import org.codehaus.activemq.util.BitArray;
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
/*    */ public class MessageAckWriter
/*    */   extends AbstractPacketWriter
/*    */ {
/*    */   public int getPacketType() {
/* 40 */     return 15;
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
/* 52 */     super.writePacket(packet, dataOut);
/* 53 */     MessageAck ack = (MessageAck)packet;
/* 54 */     writeUTF(ack.getConsumerId(), dataOut);
/* 55 */     writeUTF(ack.getMessageID(), dataOut);
/* 56 */     writeUTF(ack.getTransactionId(), dataOut);
/*    */     
/* 58 */     BitArray ba = new BitArray();
/* 59 */     ba.set(0, ack.isMessageRead());
/* 60 */     ba.set(1, ack.isXaTransacted());
/* 61 */     ba.set(2, ack.isPersistent());
/* 62 */     ba.writeToStream(dataOut);
/*    */     
/* 64 */     ActiveMQDestination.writeToStream(ack.getDestination(), dataOut);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\MessageAckWriter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */