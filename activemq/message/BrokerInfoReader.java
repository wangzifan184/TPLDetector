/*    */ package org.codehaus.activemq.message;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
/*    */ import java.util.Properties;
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
/*    */ public class BrokerInfoReader
/*    */   extends AbstractPacketReader
/*    */ {
/*    */   public int getPacketType() {
/* 39 */     return 21;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Packet createPacket() {
/* 47 */     return new BrokerInfo();
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
/* 61 */     BrokerInfo info = (BrokerInfo)packet;
/* 62 */     info.setBrokerName(dataIn.readUTF());
/* 63 */     info.setClusterName(dataIn.readUTF());
/* 64 */     info.setStartTime(dataIn.readLong());
/* 65 */     info.setProperties((Properties)readObject(dataIn));
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\BrokerInfoReader.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */