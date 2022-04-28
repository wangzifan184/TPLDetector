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
/*    */ public class JMSQueueStatsImpl
/*    */   extends JMSEndpointStatsImpl
/*    */   implements JMSDestinationStats
/*    */ {
/*    */   protected TimeStatisticImpl sendMessageRateTime;
/*    */   
/*    */   public JMSQueueStatsImpl() {
/* 31 */     this.sendMessageRateTime = new TimeStatisticImpl("sendMessageRateTime", "Time taken to send a message (publish thoughtput rate)");
/* 32 */     addStatistic("sendMessageRateTime", this.sendMessageRateTime);
/*    */   }
/*    */   
/*    */   public JMSQueueStatsImpl(CountStatisticImpl messageCount, CountStatisticImpl pendingMessageCount, CountStatisticImpl expiredMessageCount, TimeStatisticImpl messageWaitTime, TimeStatisticImpl messageRateTime, TimeStatisticImpl sendMessageRateTime) {
/* 36 */     super(messageCount, pendingMessageCount, expiredMessageCount, messageWaitTime, messageRateTime);
/* 37 */     this.sendMessageRateTime = sendMessageRateTime;
/* 38 */     addStatistic("sendMessageRateTime", sendMessageRateTime);
/*    */   }
/*    */   
/*    */   public void setPendingMessageCountOnStartup(long count) {
/* 42 */     CountStatisticImpl messageCount = (CountStatisticImpl)getPendingMessageCount();
/* 43 */     messageCount.setCount(count);
/*    */   }
/*    */   
/*    */   public void onMessageSend(Message message) {
/* 47 */     long start = this.pendingMessageCount.getLastSampleTime();
/* 48 */     this.pendingMessageCount.increment();
/* 49 */     long end = this.pendingMessageCount.getLastSampleTime();
/* 50 */     this.sendMessageRateTime.addTime(end - start);
/*    */   }
/*    */   
/*    */   public void onMessageAck() {
/* 54 */     long start = this.messageCount.getLastSampleTime();
/* 55 */     this.messageCount.increment();
/* 56 */     long end = this.messageCount.getLastSampleTime();
/* 57 */     this.messageRateTime.addTime(end - start);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\management\JMSQueueStatsImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */