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
/*    */ 
/*    */ public class DurableUnsubscribeReader
/*    */   extends AbstractPacketReader
/*    */ {
/*    */   public int getPacketType() {
/* 40 */     return 24;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Packet createPacket() {
/* 48 */     return new DurableUnsubscribe();
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
/* 61 */     super.buildPacket(packet, dataIn);
/* 62 */     DurableUnsubscribe ds = (DurableUnsubscribe)packet;
/* 63 */     ds.setClientId(dataIn.readUTF());
/* 64 */     ds.setSubscriberName(dataIn.readUTF());
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\DurableUnsubscribeReader.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */