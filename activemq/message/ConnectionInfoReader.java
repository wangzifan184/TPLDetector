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
/*    */ 
/*    */ 
/*    */ public class ConnectionInfoReader
/*    */   extends AbstractPacketReader
/*    */ {
/*    */   public int getPacketType() {
/* 41 */     return 22;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Packet createPacket() {
/* 49 */     return new ConnectionInfo();
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
/* 62 */     super.buildPacket(packet, dataIn);
/* 63 */     ConnectionInfo info = (ConnectionInfo)packet;
/* 64 */     info.setClientId(dataIn.readUTF());
/* 65 */     info.setUserName(dataIn.readUTF());
/* 66 */     info.setPassword(dataIn.readUTF());
/* 67 */     info.setHostName(dataIn.readUTF());
/* 68 */     info.setClientVersion(dataIn.readUTF());
/* 69 */     info.setWireFormatVersion(dataIn.readInt());
/* 70 */     info.setStartTime(dataIn.readLong());
/* 71 */     info.setStarted(dataIn.readBoolean());
/* 72 */     info.setClosed(dataIn.readBoolean());
/* 73 */     info.setProperties((Properties)readObject(dataIn));
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ConnectionInfoReader.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */