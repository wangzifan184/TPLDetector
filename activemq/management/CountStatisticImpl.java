/*     */ package org.codehaus.activemq.management;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedLong;
/*     */ import javax.management.j2ee.statistics.CountStatistic;
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
/*     */ public class CountStatisticImpl
/*     */   extends StatisticImpl
/*     */   implements CountStatistic
/*     */ {
/*  31 */   private final SynchronizedLong counter = new SynchronizedLong(0L);
/*     */   private CountStatisticImpl parent;
/*     */   
/*     */   public CountStatisticImpl(CountStatisticImpl parent, String name, String description) {
/*  35 */     this(name, description);
/*  36 */     this.parent = parent;
/*     */   }
/*     */   
/*     */   public CountStatisticImpl(String name, String description) {
/*  40 */     this(name, "count", description);
/*     */   }
/*     */   
/*     */   public CountStatisticImpl(String name, String unit, String description) {
/*  44 */     super(name, unit, description);
/*     */   }
/*     */   
/*     */   public void reset() {
/*  48 */     super.reset();
/*  49 */     this.counter.set(0L);
/*     */   }
/*     */   
/*     */   public long getCount() {
/*  53 */     return this.counter.get();
/*     */   }
/*     */   
/*     */   public void setCount(long count) {
/*  57 */     this.counter.set(count);
/*     */   }
/*     */   
/*     */   public void add(long amount) {
/*  61 */     this.counter.add(amount);
/*  62 */     updateSampleTime();
/*  63 */     if (this.parent != null) {
/*  64 */       this.parent.add(amount);
/*     */     }
/*     */   }
/*     */   
/*     */   public void increment() {
/*  69 */     this.counter.increment();
/*  70 */     updateSampleTime();
/*  71 */     if (this.parent != null) {
/*  72 */       this.parent.increment();
/*     */     }
/*     */   }
/*     */   
/*     */   public void subtract(long amount) {
/*  77 */     this.counter.subtract(amount);
/*  78 */     updateSampleTime();
/*  79 */     if (this.parent != null) {
/*  80 */       this.parent.subtract(amount);
/*     */     }
/*     */   }
/*     */   
/*     */   public void decrement() {
/*  85 */     this.counter.decrement();
/*  86 */     updateSampleTime();
/*  87 */     if (this.parent != null) {
/*  88 */       this.parent.decrement();
/*     */     }
/*     */   }
/*     */   
/*     */   public CountStatisticImpl getParent() {
/*  93 */     return this.parent;
/*     */   }
/*     */   
/*     */   public void setParent(CountStatisticImpl parent) {
/*  97 */     this.parent = parent;
/*     */   }
/*     */   
/*     */   protected void appendFieldDescription(StringBuffer buffer) {
/* 101 */     buffer.append(" count: ");
/* 102 */     buffer.append(Long.toString(this.counter.get()));
/* 103 */     super.appendFieldDescription(buffer);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\management\CountStatisticImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */