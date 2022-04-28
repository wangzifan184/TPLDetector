/*    */ package org.codehaus.activemq.message;
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
/*    */ public class ActiveMQMapMessageReader
/*    */   extends ActiveMQMessageReader
/*    */ {
/*    */   public int getPacketType() {
/* 34 */     return 11;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Packet createPacket() {
/* 42 */     return new ActiveMQMapMessage();
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ActiveMQMapMessageReader.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */