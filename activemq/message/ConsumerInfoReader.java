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
/*    */ public class ConsumerInfoReader
/*    */   extends AbstractPacketReader
/*    */ {
/*    */   public int getPacketType() {
/* 38 */     return 17;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Packet createPacket() {
/* 46 */     return new ConsumerInfo();
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
/* 60 */     ConsumerInfo info = (ConsumerInfo)packet;
/* 61 */     info.setConsumerId(dataIn.readUTF());
/* 62 */     info.setClientId(dataIn.readUTF());
/* 63 */     info.setSessionId(dataIn.readUTF());
/* 64 */     info.setSelector(dataIn.readUTF());
/* 65 */     info.setConsumerName(dataIn.readUTF());
/* 66 */     info.setConsumerNo(dataIn.readInt());
/* 67 */     info.setPrefetchNumber(dataIn.readShort());
/* 68 */     info.setStartTime(dataIn.readLong());
/* 69 */     info.setStarted(dataIn.readBoolean());
/* 70 */     info.setReceiptRequired(dataIn.readBoolean());
/* 71 */     info.setNoLocal(dataIn.readBoolean());
/* 72 */     info.setBrowser(dataIn.readBoolean());
/* 73 */     info.setDestination(ActiveMQDestination.readFromStream(dataIn));
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ConsumerInfoReader.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */