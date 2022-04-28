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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WireFormatInfo
/*    */   extends AbstractPacket
/*    */ {
/*    */   int version;
/*    */   
/*    */   public int getPacketType() {
/* 39 */     return 29;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 46 */     return "WireFormat: " + this.version;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getVersion() {
/* 55 */     return this.version;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setVersion(int version) {
/* 61 */     this.version = version;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\WireFormatInfo.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */