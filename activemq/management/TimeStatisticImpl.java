/*     */ package org.codehaus.activemq.management;
/*     */ 
/*     */ import javax.management.j2ee.statistics.TimeStatistic;
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
/*     */ public class TimeStatisticImpl
/*     */   extends StatisticImpl
/*     */   implements TimeStatistic
/*     */ {
/*     */   private long count;
/*     */   private long maxTime;
/*     */   private long minTime;
/*     */   private long totalTime;
/*     */   private TimeStatisticImpl parent;
/*     */   
/*     */   public TimeStatisticImpl(String name, String description) {
/*  35 */     this(name, "millis", description);
/*     */   }
/*     */   
/*     */   public TimeStatisticImpl(TimeStatisticImpl parent, String name, String description) {
/*  39 */     this(name, description);
/*  40 */     this.parent = parent;
/*     */   }
/*     */   
/*     */   public TimeStatisticImpl(String name, String unit, String description) {
/*  44 */     super(name, unit, description);
/*     */   }
/*     */   
/*     */   public synchronized void reset() {
/*  48 */     super.reset();
/*  49 */     this.count = 0L;
/*  50 */     this.maxTime = 0L;
/*  51 */     this.minTime = 0L;
/*  52 */     this.totalTime = 0L;
/*     */   }
/*     */   
/*     */   public synchronized long getCount() {
/*  56 */     return this.count;
/*     */   }
/*     */   
/*     */   public synchronized void addTime(long time) {
/*  60 */     this.count++;
/*  61 */     this.totalTime += time;
/*  62 */     if (time > this.maxTime) {
/*  63 */       this.maxTime = time;
/*     */     }
/*  65 */     if (time < this.minTime || this.minTime == 0L) {
/*  66 */       this.minTime = time;
/*     */     }
/*  68 */     updateSampleTime();
/*  69 */     if (this.parent != null) {
/*  70 */       this.parent.addTime(time);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMaxTime() {
/*  78 */     return this.maxTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized long getMinTime() {
/*  85 */     return this.minTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized long getTotalTime() {
/*  92 */     return this.totalTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized double getAverageTime() {
/* 100 */     if (this.count == 0L) {
/* 101 */       return 0.0D;
/*     */     }
/* 103 */     double d = this.totalTime;
/* 104 */     return d / this.count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized double getAverageTimeExcludingMinMax() {
/* 114 */     if (this.count <= 2L) {
/* 115 */       return 0.0D;
/*     */     }
/* 117 */     double d = (this.totalTime - this.minTime - this.maxTime);
/* 118 */     return d / (this.count - 2L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getAveragePerSecond() {
/* 126 */     double d = 1000.0D;
/* 127 */     double averageTime = getAverageTime();
/* 128 */     if (averageTime == 0.0D) {
/* 129 */       return 0.0D;
/*     */     }
/* 131 */     return d / averageTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getAveragePerSecondExcludingMinMax() {
/* 138 */     double d = 1000.0D;
/* 139 */     double average = getAverageTimeExcludingMinMax();
/* 140 */     if (average == 0.0D) {
/* 141 */       return 0.0D;
/*     */     }
/* 143 */     return d / average;
/*     */   }
/*     */   
/*     */   public TimeStatisticImpl getParent() {
/* 147 */     return this.parent;
/*     */   }
/*     */   
/*     */   public void setParent(TimeStatisticImpl parent) {
/* 151 */     this.parent = parent;
/*     */   }
/*     */   
/*     */   protected synchronized void appendFieldDescription(StringBuffer buffer) {
/* 155 */     buffer.append(" count: ");
/* 156 */     buffer.append(Long.toString(this.count));
/* 157 */     buffer.append(" maxTime: ");
/* 158 */     buffer.append(Long.toString(this.maxTime));
/* 159 */     buffer.append(" minTime: ");
/* 160 */     buffer.append(Long.toString(this.minTime));
/* 161 */     buffer.append(" totalTime: ");
/* 162 */     buffer.append(Long.toString(this.totalTime));
/* 163 */     buffer.append(" averageTime: ");
/* 164 */     buffer.append(Double.toString(getAverageTime()));
/* 165 */     buffer.append(" averageTimeExMinMax: ");
/* 166 */     buffer.append(Double.toString(getAverageTimeExcludingMinMax()));
/* 167 */     buffer.append(" averagePerSecond: ");
/* 168 */     buffer.append(Double.toString(getAveragePerSecond()));
/* 169 */     buffer.append(" averagePerSecondExMinMax: ");
/* 170 */     buffer.append(Double.toString(getAveragePerSecondExcludingMinMax()));
/* 171 */     super.appendFieldDescription(buffer);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\management\TimeStatisticImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */