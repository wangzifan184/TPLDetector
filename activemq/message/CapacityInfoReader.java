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
/*    */ 
/*    */ public class CapacityInfoReader
/*    */   extends AbstractPacketReader
/*    */ {
/*    */   public int getPacketType() {
/* 39 */     return 27;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Packet createPacket() {
/* 47 */     return new CapacityInfo();
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
/* 61 */     CapacityInfo info = (CapacityInfo)packet;
/* 62 */     info.setResourceName(dataIn.readUTF());
/* 63 */     info.setCorrelationId(dataIn.readUTF());
/* 64 */     info.setCapacity(dataIn.readByte());
/* 65 */     info.setFlowControlTimeout(dataIn.readInt());
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\CapacityInfoReader.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */