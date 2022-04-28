/*     */ package org.codehaus.activemq.journal.impl;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.Latch;
/*     */ import java.nio.ByteBuffer;
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
/*     */ public class BatchedWrite
/*     */ {
/*  33 */   private final Latch writeDoneLatch = new Latch();
/*  34 */   private Record data = null;
/*     */   public Throwable error;
/*     */   private final ByteBuffer byteBuffer;
/*  37 */   private long firstSequenceId = -1L;
/*     */   
/*     */   private long lastSequenceId;
/*     */   
/*     */   private Mark mark;
/*     */   
/*     */   private boolean appendDisabled = false;
/*     */   
/*     */   private boolean appendInProgress = false;
/*     */   
/*     */   public BatchedWrite(ByteBuffer byteBuffer) {
/*  48 */     this.byteBuffer = byteBuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void disableAppend() throws InterruptedException {
/*  56 */     this.appendDisabled = true;
/*  57 */     while (this.appendInProgress) {
/*  58 */       wait();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean append(Record record) {
/*  67 */     synchronized (this) {
/*  68 */       if (this.appendDisabled)
/*  69 */         return false; 
/*  70 */       this.appendInProgress = true;
/*     */     } 
/*     */     
/*  73 */     record.fill(this.byteBuffer);
/*     */     
/*  75 */     if (record.remaining() == 0) {
/*  76 */       if (this.firstSequenceId == -1L)
/*  77 */         this.firstSequenceId = (record.getHeader()).sequenceId; 
/*  78 */       this.lastSequenceId = (record.getHeader()).sequenceId;
/*  79 */       if (record.getMark() != null) {
/*  80 */         this.mark = record.getMark();
/*     */       }
/*     */     } 
/*  83 */     synchronized (this) {
/*  84 */       this.appendInProgress = false;
/*  85 */       notify();
/*     */       
/*  87 */       if (this.appendDisabled) {
/*  88 */         return false;
/*     */       }
/*  90 */       return (this.byteBuffer.remaining() > 0);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void waitForForce() throws Throwable {
/*  95 */     this.writeDoneLatch.acquire();
/*  96 */     synchronized (this) {
/*  97 */       if (this.error != null)
/*  98 */         throw this.error; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void forced() {
/* 103 */     this.writeDoneLatch.release();
/*     */   }
/*     */   
/*     */   public void writeFailed(Throwable error) {
/* 107 */     synchronized (this) {
/* 108 */       this.error = error;
/*     */     } 
/* 110 */     this.writeDoneLatch.release();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Record getData() {
/* 117 */     return this.data;
/*     */   }
/*     */   
/*     */   public ByteBuffer getByteBuffer() {
/* 121 */     return this.byteBuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mark getMark() {
/* 128 */     return this.mark;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLastSequenceId() {
/* 135 */     return this.lastSequenceId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getFirstSequenceId() {
/* 142 */     return this.firstSequenceId;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\journal\impl\BatchedWrite.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */