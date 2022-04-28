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
/*    */ public class XATransactionInfoReader
/*    */   extends AbstractPacketReader
/*    */ {
/*    */   public int getPacketType() {
/* 38 */     return 20;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Packet createPacket() {
/* 46 */     return new XATransactionInfo();
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
/* 60 */     XATransactionInfo info = (XATransactionInfo)packet;
/* 61 */     info.setType(dataIn.readByte());
/* 62 */     switch (info.getType()) {
/*    */       case 101:
/*    */       case 102:
/*    */       case 103:
/*    */       case 104:
/*    */       case 105:
/*    */       case 106:
/*    */       case 107:
/*    */       case 108:
/*    */       case 109:
/* 72 */         info.setXid(ActiveMQXid.read(dataIn));
/*    */       
/*    */       case 112:
/* 75 */         info.setTransactionTimeout(dataIn.readInt());
/*    */ 
/*    */       
/*    */       case 110:
/*    */       case 111:
/*    */       case 113:
/*    */         return;
/*    */     } 
/*    */     
/* 84 */     throw new IllegalArgumentException("Invalid type code: " + info.getType());
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\XATransactionInfoReader.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */