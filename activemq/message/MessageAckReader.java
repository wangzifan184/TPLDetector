/*    */ package org.codehaus.activemq.message;
/*    */ 
/*    */ import java.io.DataInput;
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
/*    */ public class MessageAckReader
/*    */   extends AbstractPacketReader
/*    */ {
/*    */   public int getPacketType() {
/* 39 */     return 15;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Packet createPacket() {
/* 47 */     return new MessageAck();
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
/* 60 */     super.buildPacket(packet, dataIn);
/* 61 */     MessageAck ack = (MessageAck)packet;
/* 62 */     ack.setConsumerId(dataIn.readUTF());
/* 63 */     ack.setMessageID(dataIn.readUTF());
/* 64 */     ack.setTransactionId(dataIn.readUTF());
/*    */     
/* 66 */     BitArray ba = new BitArray();
/* 67 */     ba.readFromStream(dataIn);
/* 68 */     ack.setMessageRead(ba.get(0));
/* 69 */     ack.setXaTransacted(ba.get(1));
/* 70 */     ack.setPersistent(ba.get(2));
/* 71 */     ack.setDestination(ActiveMQDestination.readFromStream(dataIn));
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\MessageAckReader.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */