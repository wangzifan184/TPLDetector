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
/*    */ public class WireFormatInfoWriter
/*    */   extends AbstractPacketWriter
/*    */ {
/*    */   public int getPacketType() {
/* 33 */     return 29;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void writePacket(Packet packet, DataOutput dataOut) throws IOException {
/* 44 */     super.writePacket(packet, dataOut);
/* 45 */     WireFormatInfo info = (WireFormatInfo)packet;
/* 46 */     dataOut.writeInt(info.getVersion());
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\WireFormatInfoWriter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */