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
/*    */ public class XATransactionInfoWriter
/*    */   extends AbstractPacketWriter
/*    */ {
/*    */   static final boolean $assertionsDisabled;
/*    */   
/*    */   public int getPacketType() {
/* 37 */     return 20;
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
/* 49 */     super.writePacket(packet, dataOut);
/* 50 */     XATransactionInfo info = (XATransactionInfo)packet;
/* 51 */     dataOut.writeByte(info.getType());
/* 52 */     switch (info.getType()) {
/*    */       case 101:
/*    */       case 102:
/*    */       case 103:
/*    */       case 104:
/*    */       case 105:
/*    */       case 106:
/*    */       case 107:
/*    */       case 108:
/*    */       case 109:
/* 62 */         assert info.getXid() != null;
/* 63 */         info.getXid().write(dataOut);
/*    */       
/*    */       case 112:
/* 66 */         dataOut.writeInt(info.getTransactionTimeout());
/*    */ 
/*    */       
/*    */       case 110:
/*    */       case 111:
/*    */       case 113:
/*    */         return;
/*    */     } 
/*    */     
/* 75 */     throw new IllegalArgumentException("Invalid type code: " + info.getType());
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\XATransactionInfoWriter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */