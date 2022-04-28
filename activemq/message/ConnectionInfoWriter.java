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
/*    */ public class ConnectionInfoWriter
/*    */   extends AbstractPacketWriter
/*    */ {
/*    */   public int getPacketType() {
/* 39 */     return 22;
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
/* 52 */     ConnectionInfo info = (ConnectionInfo)packet;
/* 53 */     writeUTF(info.getClientId(), dataOut);
/* 54 */     writeUTF(info.getUserName(), dataOut);
/* 55 */     writeUTF(info.getPassword(), dataOut);
/* 56 */     writeUTF(info.getHostName(), dataOut);
/* 57 */     writeUTF(info.getClientVersion(), dataOut);
/* 58 */     dataOut.writeInt(info.getWireFormatVersion());
/* 59 */     dataOut.writeLong(info.getStartTime());
/* 60 */     dataOut.writeBoolean(info.isStarted());
/* 61 */     dataOut.writeBoolean(info.isClosed());
/* 62 */     writeObject(info.getProperties(), dataOut);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ConnectionInfoWriter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */