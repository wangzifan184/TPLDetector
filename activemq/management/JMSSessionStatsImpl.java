/*     */ package org.codehaus.activemq.management;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.management.j2ee.statistics.CountStatistic;
/*     */ import javax.management.j2ee.statistics.JMSConsumerStats;
/*     */ import javax.management.j2ee.statistics.JMSProducerStats;
/*     */ import javax.management.j2ee.statistics.JMSSessionStats;
/*     */ import javax.management.j2ee.statistics.Statistic;
/*     */ import javax.management.j2ee.statistics.TimeStatistic;
/*     */ import org.codehaus.activemq.ActiveMQMessageConsumer;
/*     */ import org.codehaus.activemq.ActiveMQMessageProducer;
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
/*     */ public class JMSSessionStatsImpl
/*     */   extends StatsImpl
/*     */   implements JMSSessionStats
/*     */ {
/*     */   private List producers;
/*     */   private List consumers;
/*     */   private CountStatistic messageCount;
/*     */   private CountStatistic pendingMessageCount;
/*     */   private CountStatistic expiredMessageCount;
/*     */   private TimeStatistic messageWaitTime;
/*     */   private CountStatisticImpl durableSubscriptionCount;
/*     */   private TimeStatisticImpl messageRateTime;
/*     */   
/*     */   public JMSSessionStatsImpl(List producers, List consumers) {
/*  48 */     this.producers = producers;
/*  49 */     this.consumers = consumers;
/*  50 */     this.messageCount = new CountStatisticImpl("messageCount", "Number of messages exchanged");
/*  51 */     this.pendingMessageCount = new CountStatisticImpl("pendingMessageCount", "Number of pending messages");
/*  52 */     this.expiredMessageCount = new CountStatisticImpl("expiredMessageCount", "Number of expired messages");
/*  53 */     this.messageWaitTime = new TimeStatisticImpl("messageWaitTime", "Time spent by a message before being delivered");
/*  54 */     this.durableSubscriptionCount = new CountStatisticImpl("durableSubscriptionCount", "The number of durable subscriptions");
/*  55 */     this.messageWaitTime = new TimeStatisticImpl("messageWaitTime", "Time spent by a message before being delivered");
/*  56 */     this.messageRateTime = new TimeStatisticImpl("messageRateTime", "Time taken to process a message (thoughtput rate)");
/*     */ 
/*     */     
/*  59 */     addStatistic("messageCount", (Statistic)this.messageCount);
/*  60 */     addStatistic("pendingMessageCount", (Statistic)this.pendingMessageCount);
/*  61 */     addStatistic("expiredMessageCount", (Statistic)this.expiredMessageCount);
/*  62 */     addStatistic("messageWaitTime", (Statistic)this.messageWaitTime);
/*  63 */     addStatistic("durableSubscriptionCount", this.durableSubscriptionCount);
/*  64 */     addStatistic("messageRateTime", this.messageRateTime);
/*     */   }
/*     */ 
/*     */   
/*     */   public JMSProducerStats[] getProducers() {
/*  69 */     Object[] producerArray = this.producers.toArray();
/*  70 */     int size = producerArray.length;
/*  71 */     JMSProducerStats[] answer = new JMSProducerStats[size];
/*  72 */     for (int i = 0; i < size; i++) {
/*  73 */       ActiveMQMessageProducer producer = (ActiveMQMessageProducer)producerArray[i];
/*  74 */       answer[i] = producer.getProducerStats();
/*     */     } 
/*  76 */     return answer;
/*     */   }
/*     */ 
/*     */   
/*     */   public JMSConsumerStats[] getConsumers() {
/*  81 */     Object[] consumerArray = this.consumers.toArray();
/*  82 */     int size = consumerArray.length;
/*  83 */     JMSConsumerStats[] answer = new JMSConsumerStats[size];
/*  84 */     for (int i = 0; i < size; i++) {
/*  85 */       ActiveMQMessageConsumer consumer = (ActiveMQMessageConsumer)consumerArray[i];
/*  86 */       answer[i] = consumer.getConsumerStats();
/*     */     } 
/*  88 */     return answer;
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
/*     */   public CountStatistic getDurableSubscriptionCount() {
/* 108 */     return this.durableSubscriptionCount;
/*     */   }
/*     */   
/*     */   public TimeStatisticImpl getMessageRateTime() {
/* 112 */     return this.messageRateTime;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 116 */     StringBuffer buffer = new StringBuffer(" ");
/* 117 */     buffer.append(this.messageCount);
/* 118 */     buffer.append(" ");
/* 119 */     buffer.append(this.messageRateTime);
/* 120 */     buffer.append(" ");
/* 121 */     buffer.append(this.pendingMessageCount);
/* 122 */     buffer.append(" ");
/* 123 */     buffer.append(this.expiredMessageCount);
/* 124 */     buffer.append(" ");
/* 125 */     buffer.append(this.messageWaitTime);
/* 126 */     buffer.append(" ");
/* 127 */     buffer.append(this.durableSubscriptionCount);
/*     */     
/* 129 */     buffer.append(" producers{ ");
/* 130 */     JMSProducerStats[] producerArray = getProducers();
/* 131 */     for (int i = 0; i < producerArray.length; i++) {
/* 132 */       if (i > 0) {
/* 133 */         buffer.append(", ");
/*     */       }
/* 135 */       buffer.append(Integer.toString(i));
/* 136 */       buffer.append(" = ");
/* 137 */       buffer.append(producerArray[i]);
/*     */     } 
/* 139 */     buffer.append(" } consumers{ ");
/* 140 */     JMSConsumerStats[] consumerArray = getConsumers();
/* 141 */     for (int j = 0; j < consumerArray.length; j++) {
/* 142 */       if (j > 0) {
/* 143 */         buffer.append(", ");
/*     */       }
/* 145 */       buffer.append(Integer.toString(j));
/* 146 */       buffer.append(" = ");
/* 147 */       buffer.append(consumerArray[j]);
/*     */     } 
/* 149 */     buffer.append(" }");
/* 150 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   public void dump(IndentPrinter out) {
/* 154 */     out.printIndent();
/* 155 */     out.println(this.messageCount);
/* 156 */     out.printIndent();
/* 157 */     out.println(this.messageRateTime);
/* 158 */     out.printIndent();
/* 159 */     out.println(this.pendingMessageCount);
/* 160 */     out.printIndent();
/* 161 */     out.println(this.expiredMessageCount);
/* 162 */     out.printIndent();
/* 163 */     out.println(this.messageWaitTime);
/* 164 */     out.printIndent();
/* 165 */     out.println(this.durableSubscriptionCount);
/* 166 */     out.println();
/*     */     
/* 168 */     out.printIndent();
/* 169 */     out.println("producers {");
/* 170 */     out.incrementIndent();
/* 171 */     JMSProducerStats[] producerArray = getProducers();
/* 172 */     for (int i = 0; i < producerArray.length; i++) {
/* 173 */       JMSProducerStatsImpl producer = (JMSProducerStatsImpl)producerArray[i];
/* 174 */       producer.dump(out);
/*     */     } 
/* 176 */     out.decrementIndent();
/* 177 */     out.printIndent();
/* 178 */     out.println("}");
/*     */     
/* 180 */     out.printIndent();
/* 181 */     out.println("consumers {");
/* 182 */     out.incrementIndent();
/* 183 */     JMSConsumerStats[] consumerArray = getConsumers();
/* 184 */     for (int j = 0; j < consumerArray.length; j++) {
/* 185 */       JMSConsumerStatsImpl consumer = (JMSConsumerStatsImpl)consumerArray[j];
/* 186 */       consumer.dump(out);
/*     */     } 
/* 188 */     out.decrementIndent();
/* 189 */     out.printIndent();
/* 190 */     out.println("}");
/*     */   }
/*     */   
/*     */   public void onCreateDurableSubscriber() {
/* 194 */     this.durableSubscriptionCount.increment();
/*     */   }
/*     */   
/*     */   public void onRemoveDurableSubscriber() {
/* 198 */     this.durableSubscriptionCount.decrement();
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\management\JMSSessionStatsImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */