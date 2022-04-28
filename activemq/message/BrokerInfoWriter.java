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
/*    */ public class BrokerInfoWriter
/*    */   extends AbstractPacketWriter
/*    */ {
/*    */   public int getPacketType() {
/* 39 */     return 21;
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
/* 52 */     BrokerInfo info = (BrokerInfo)packet;
/* 53 */     writeUTF(info.getBrokerName(), dataOut);
/* 54 */     writeUTF(info.getClusterName(), dataOut);
/* 55 */     dataOut.writeLong(info.getStartTime());
/* 56 */     writeObject(info.getProperties(), dataOut);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\BrokerInfoWriter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */