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
/*    */ public class DurableUnsubscribe
/*    */   extends AbstractPacket
/*    */ {
/*    */   private String clientId;
/*    */   private String subscriberName;
/*    */   
/*    */   public int getPacketType() {
/* 40 */     return 24;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getClientId() {
/* 48 */     return this.clientId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setClientId(String clientId) {
/* 55 */     this.clientId = clientId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSubscriberName() {
/* 62 */     return this.subscriberName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSubscriberName(String subscriberName) {
/* 69 */     this.subscriberName = subscriberName;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\DurableUnsubscribe.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */