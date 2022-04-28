/*     */ package org.codehaus.activemq.management;
/*     */ 
/*     */ import javax.jms.Message;
/*     */ import javax.management.j2ee.statistics.CountStatistic;
/*     */ import javax.management.j2ee.statistics.JMSEndpointStats;
/*     */ import javax.management.j2ee.statistics.TimeStatistic;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.util.IndentPrinter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JMSEndpointStatsImpl
/*     */   extends StatsImpl
/*     */   implements JMSEndpointStats
/*     */ {
/*  40 */   private static final Log log = LogFactory.getLog(JMSEndpointStatsImpl.class);
/*     */ 
/*     */   
/*     */   protected CountStatisticImpl messageCount;
/*     */ 
/*     */   
/*     */   protected CountStatisticImpl pendingMessageCount;
/*     */   
/*     */   protected CountStatisticImpl expiredMessageCount;
/*     */   
/*     */   protected TimeStatistic messageWaitTime;
/*     */   
/*     */   protected TimeStatisticImpl messageRateTime;
/*     */ 
/*     */   
/*     */   public JMSEndpointStatsImpl(JMSSessionStatsImpl sessionStats) {
/*  56 */     this();
/*  57 */     setParent(this.messageCount, sessionStats.getMessageCount());
/*  58 */     setParent(this.pendingMessageCount, sessionStats.getPendingMessageCount());
/*  59 */     setParent(this.expiredMessageCount, sessionStats.getExpiredMessageCount());
/*  60 */     setParent(this.messageWaitTime, sessionStats.getMessageWaitTime());
/*  61 */     setParent(this.messageRateTime, sessionStats.getMessageRateTime());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JMSEndpointStatsImpl() {
/*  69 */     this(new CountStatisticImpl("messageCount", "Number of messages processed"), new CountStatisticImpl("pendingMessageCount", "Number of pending messages"), new CountStatisticImpl("expiredMessageCount", "Number of expired messages"), new TimeStatisticImpl("messageWaitTime", "Time spent by a message before being delivered"), new TimeStatisticImpl("messageRateTime", "Time taken to process a message (thoughtput rate)"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JMSEndpointStatsImpl(CountStatisticImpl messageCount, CountStatisticImpl pendingMessageCount, CountStatisticImpl expiredMessageCount, TimeStatisticImpl messageWaitTime, TimeStatisticImpl messageRateTime) {
/*  77 */     this.messageCount = messageCount;
/*  78 */     this.pendingMessageCount = pendingMessageCount;
/*  79 */     this.expiredMessageCount = expiredMessageCount;
/*  80 */     this.messageWaitTime = messageWaitTime;
/*  81 */     this.messageRateTime = messageRateTime;
/*     */ 
/*     */     
/*  84 */     addStatistic("messageCount", messageCount);
/*  85 */     addStatistic("pendingMessageCount", pendingMessageCount);
/*  86 */     addStatistic("expiredMessageCount", expiredMessageCount);
/*  87 */     addStatistic("messageWaitTime", messageWaitTime);
/*  88 */     addStatistic("messageRateTime", messageRateTime);
/*     */   }
/*     */   
/*     */   public CountStatistic getMessageCount() {
/*  92 */     return this.messageCount;
/*     */   }
/*     */   
/*     */   public CountStatistic getPendingMessageCount() {
/*  96 */     return this.pendingMessageCount;
/*     */   }
/*     */   
/*     */   public CountStatistic getExpiredMessageCount() {
/* 100 */     return this.expiredMessageCount;
/*     */   }
/*     */   
/*     */   public TimeStatistic getMessageWaitTime() {
/* 104 */     return this.messageWaitTime;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 108 */     StringBuffer buffer = new StringBuffer();
/* 109 */     buffer.append(this.messageCount);
/* 110 */     buffer.append(" ");
/* 111 */     buffer.append(this.messageRateTime);
/* 112 */     buffer.append(" ");
/* 113 */     buffer.append(this.pendingMessageCount);
/* 114 */     buffer.append(" ");
/* 115 */     buffer.append(this.expiredMessageCount);
/* 116 */     buffer.append(" ");
/* 117 */     buffer.append(this.messageWaitTime);
/* 118 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   public void onMessage(Message message) {
/* 122 */     long start = this.messageCount.getLastSampleTime();
/* 123 */     this.messageCount.increment();
/* 124 */     long end = this.messageCount.getLastSampleTime();
/* 125 */     this.messageRateTime.addTime(end - start);
/*     */   }
/*     */   
/*     */   public void dump(IndentPrinter out) {
/* 129 */     out.printIndent();
/* 130 */     out.println(this.messageCount);
/* 131 */     out.printIndent();
/* 132 */     out.println(this.messageRateTime);
/* 133 */     out.printIndent();
/* 134 */     out.println(this.pendingMessageCount);
/* 135 */     out.printIndent();
/* 136 */     out.println(this.messageRateTime);
/* 137 */     out.printIndent();
/* 138 */     out.println(this.expiredMessageCount);
/* 139 */     out.printIndent();
/* 140 */     out.println(this.messageWaitTime);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setParent(CountStatistic child, CountStatistic parent) {
/* 146 */     if (child instanceof CountStatisticImpl && parent instanceof CountStatisticImpl) {
/* 147 */       CountStatisticImpl c = (CountStatisticImpl)child;
/* 148 */       c.setParent((CountStatisticImpl)parent);
/*     */     } else {
/*     */       
/* 151 */       log.warn("Cannot associate endpoint counters with session level counters as they are not both CountStatisticImpl clases. Endpoint: " + child + " session: " + parent);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void setParent(TimeStatistic child, TimeStatistic parent) {
/* 156 */     if (child instanceof TimeStatisticImpl && parent instanceof TimeStatisticImpl) {
/* 157 */       TimeStatisticImpl c = (TimeStatisticImpl)child;
/* 158 */       c.setParent((TimeStatisticImpl)parent);
/*     */     } else {
/*     */       
/* 161 */       log.warn("Cannot associate endpoint counters with session level counters as they are not both TimeStatisticImpl clases. Endpoint: " + child + " session: " + parent);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\management\JMSEndpointStatsImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */