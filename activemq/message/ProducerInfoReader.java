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
/*    */ public class ProducerInfoReader
/*    */   extends AbstractPacketReader
/*    */ {
/*    */   public int getPacketType() {
/* 38 */     return 18;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Packet createPacket() {
/* 46 */     return new ProducerInfo();
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
/* 60 */     ProducerInfo info = (ProducerInfo)packet;
/* 61 */     info.setProducerId(dataIn.readUTF());
/* 62 */     info.setClientId(dataIn.readUTF());
/* 63 */     info.setSessionId(dataIn.readUTF());
/* 64 */     info.setStartTime(dataIn.readLong());
/* 65 */     info.setStarted(dataIn.readBoolean());
/* 66 */     info.setDestination(ActiveMQDestination.readFromStream(dataIn));
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ProducerInfoReader.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */