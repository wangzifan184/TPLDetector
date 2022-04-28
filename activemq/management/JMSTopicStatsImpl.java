/*    */ package org.codehaus.activemq.management;
/*    */ 
/*    */ import javax.jms.Message;
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
/*    */ public class JMSTopicStatsImpl
/*    */   extends JMSEndpointStatsImpl
/*    */   implements JMSDestinationStats
/*    */ {
/*    */   public void setPendingMessageCountOnStartup(long count) {}
/*    */   
/*    */   public void onMessageSend(Message message) {
/* 36 */     onMessage(message);
/*    */   }
/*    */   
/*    */   public void onMessageAck() {}
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\management\JMSTopicStatsImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */