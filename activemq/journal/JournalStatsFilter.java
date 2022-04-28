/*     */ package org.codehaus.activemq.journal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import org.codehaus.activemq.management.CountStatisticImpl;
/*     */ import org.codehaus.activemq.management.TimeStatisticImpl;
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
/*     */ public class JournalStatsFilter
/*     */   implements Journal
/*     */ {
/*  35 */   private final TimeStatisticImpl writeLatency = new TimeStatisticImpl("writeLatency", "The amount of time that is spent waiting for a record to be written to the Journal");
/*  36 */   private final CountStatisticImpl writeRecordsCounter = new CountStatisticImpl("writeRecordsCounter", "The number of records that have been written by the Journal");
/*  37 */   private final CountStatisticImpl writeBytesCounter = new CountStatisticImpl("writeBytesCounter", "The number of bytes that have been written by the Journal");
/*  38 */   private final TimeStatisticImpl synchedWriteLatency = new TimeStatisticImpl(this.writeLatency, "synchedWriteLatency", "The amount of time that is spent waiting for a synch record to be written to the Journal");
/*  39 */   private final TimeStatisticImpl unsynchedWriteLatency = new TimeStatisticImpl(this.writeLatency, "unsynchedWriteLatency", "The amount of time that is spent waiting for a non synch record to be written to the Journal");
/*  40 */   private final TimeStatisticImpl readLatency = new TimeStatisticImpl("readLatency", "The amount of time that is spent waiting for a record to be read from the Journal");
/*  41 */   private final CountStatisticImpl readBytesCounter = new CountStatisticImpl("readBytesCounter", "The number of bytes that have been read by the Journal");
/*     */ 
/*     */   
/*     */   private final Journal next;
/*     */ 
/*     */   
/*     */   private boolean detailedStats;
/*     */ 
/*     */ 
/*     */   
/*     */   public JournalStatsFilter(Journal next) {
/*  52 */     this.next = next;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RecordLocation write(byte[] data, boolean sync) throws IOException {
/*  60 */     long start = System.currentTimeMillis();
/*  61 */     RecordLocation answer = this.next.write(data, sync);
/*  62 */     long end = System.currentTimeMillis();
/*     */     
/*  64 */     this.writeRecordsCounter.increment();
/*  65 */     this.writeBytesCounter.add(data.length);
/*  66 */     if (sync) {
/*  67 */       this.synchedWriteLatency.addTime(end - start);
/*     */     } else {
/*  69 */       this.unsynchedWriteLatency.addTime(end - start);
/*  70 */     }  return answer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] read(RecordLocation location) throws InvalidRecordLocationException, IOException {
/*  79 */     long start = System.currentTimeMillis();
/*  80 */     byte[] answer = this.next.read(location);
/*  81 */     long end = System.currentTimeMillis();
/*     */     
/*  83 */     this.readBytesCounter.add(answer.length);
/*  84 */     this.readLatency.addTime(end - start);
/*  85 */     return answer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMark(RecordLocation recordLocator, boolean force) throws InvalidRecordLocationException, IOException {
/*  93 */     this.next.setMark(recordLocator, force);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RecordLocation getMark() {
/* 100 */     return this.next.getMark();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 107 */     this.next.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJournalEventListener(JournalEventListener eventListener) {
/* 114 */     this.next.setJournalEventListener(eventListener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RecordLocation getNextRecordLocation(RecordLocation lastLocation) throws IOException, InvalidRecordLocationException {
/* 122 */     return this.next.getNextRecordLocation(lastLocation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dump(IndentPrinter out) {
/* 131 */     out.printIndent();
/* 132 */     out.println("Journal Stats {");
/* 133 */     out.incrementIndent();
/* 134 */     out.printIndent();
/* 135 */     out.println("Throughput           : " + getThroughputKps() + " k/s and " + getThroughputRps() + " records/s");
/* 136 */     out.printIndent();
/* 137 */     out.println("Latency with force   : " + getAvgSyncedLatencyMs() + " ms");
/* 138 */     out.printIndent();
/* 139 */     out.println("Latency without force: " + getAvgUnSyncedLatencyMs() + " ms");
/*     */     
/* 141 */     out.printIndent();
/* 142 */     out.println("Raw Stats {");
/* 143 */     out.incrementIndent();
/*     */     
/* 145 */     out.printIndent();
/* 146 */     out.println(this.writeRecordsCounter);
/* 147 */     out.printIndent();
/* 148 */     out.println(this.writeBytesCounter);
/* 149 */     out.printIndent();
/* 150 */     out.println(this.writeLatency);
/* 151 */     out.incrementIndent();
/* 152 */     out.printIndent();
/* 153 */     out.println(this.synchedWriteLatency);
/* 154 */     out.printIndent();
/* 155 */     out.println(this.unsynchedWriteLatency);
/* 156 */     out.decrementIndent();
/*     */     
/* 158 */     out.printIndent();
/* 159 */     out.println(this.readBytesCounter);
/*     */     
/* 161 */     out.printIndent();
/* 162 */     out.println(this.readLatency);
/* 163 */     out.decrementIndent();
/* 164 */     out.printIndent();
/* 165 */     out.println("}");
/*     */     
/* 167 */     out.decrementIndent();
/* 168 */     out.printIndent();
/* 169 */     out.println("}");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 179 */     if (this.detailedStats) {
/* 180 */       StringWriter stringWriter = new StringWriter();
/* 181 */       PrintWriter printWriter = new PrintWriter(stringWriter);
/* 182 */       dump(new IndentPrinter(printWriter, "  "));
/* 183 */       return stringWriter.getBuffer().toString();
/*     */     } 
/* 185 */     StringWriter w = new StringWriter();
/* 186 */     PrintWriter pw = new PrintWriter(w);
/* 187 */     IndentPrinter out = new IndentPrinter(pw, "  ");
/* 188 */     out.println("Throughput           : " + getThroughputKps() + " k/s and " + getThroughputRps() + " records/s");
/* 189 */     out.printIndent();
/* 190 */     out.println("Latency with force   : " + getAvgSyncedLatencyMs() + " ms");
/* 191 */     out.printIndent();
/* 192 */     out.println("Latency without force: " + getAvgUnSyncedLatencyMs() + " ms");
/* 193 */     return w.getBuffer().toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JournalStatsFilter enableDetailedStats(boolean detailedStats) {
/* 202 */     this.detailedStats = detailedStats;
/* 203 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getThroughputKps() {
/* 212 */     long totalTime = this.writeBytesCounter.getLastSampleTime() - this.writeBytesCounter.getStartTime();
/* 213 */     return this.writeBytesCounter.getCount() / totalTime / 1024.0D * 1000.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getThroughputRps() {
/* 222 */     long totalTime = this.writeRecordsCounter.getLastSampleTime() - this.writeRecordsCounter.getStartTime();
/* 223 */     return this.writeRecordsCounter.getCount() / totalTime * 1000.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getWritesPerSecond() {
/* 232 */     return this.writeLatency.getAveragePerSecond();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getAvgSyncedLatencyMs() {
/* 241 */     return this.synchedWriteLatency.getAverageTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getAvgUnSyncedLatencyMs() {
/* 250 */     return this.unsynchedWriteLatency.getAverageTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 257 */     this.writeLatency.reset();
/* 258 */     this.writeBytesCounter.reset();
/* 259 */     this.writeRecordsCounter.reset();
/* 260 */     this.synchedWriteLatency.reset();
/* 261 */     this.unsynchedWriteLatency.reset();
/* 262 */     this.readLatency.reset();
/* 263 */     this.readBytesCounter.reset();
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\journal\JournalStatsFilter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */