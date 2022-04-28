/*     */ package org.codehaus.activemq.journal.impl;
/*     */ 
/*     */ import org.codehaus.activemq.journal.RecordLocation;
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
/*     */ public class RecordLocationImpl
/*     */   implements RecordLocation
/*     */ {
/*     */   private final byte fileMangerId;
/*     */   private final byte segmentIndex;
/*     */   private final int segmentOffset;
/*     */   private final long sequenceId;
/*     */   
/*     */   public RecordLocationImpl(byte fileMangerId, byte segmentIndex, int fileOffset, long sequenceId) {
/*  37 */     this.fileMangerId = fileMangerId;
/*  38 */     this.segmentIndex = segmentIndex;
/*  39 */     this.segmentOffset = fileOffset;
/*  40 */     this.sequenceId = sequenceId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RecordLocationImpl(byte fileMangerId, byte segmentIndex, int fileOffset) {
/*  50 */     this.fileMangerId = fileMangerId;
/*  51 */     this.segmentIndex = segmentIndex;
/*  52 */     this.segmentOffset = fileOffset;
/*  53 */     this.sequenceId = -1L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Object o) {
/*  60 */     return (int)(this.sequenceId - ((RecordLocationImpl)o).sequenceId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  67 */     return this.segmentOffset ^ this.segmentIndex ^ this.fileMangerId << 8 ^ (int)this.sequenceId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  74 */     if (o == null || o.getClass() != RecordLocationImpl.class)
/*  75 */       return false; 
/*  76 */     RecordLocationImpl rl = (RecordLocationImpl)o;
/*  77 */     return (rl.sequenceId == this.sequenceId && rl.segmentOffset == this.segmentOffset && rl.segmentIndex == this.segmentIndex && rl.fileMangerId == this.fileMangerId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  85 */     return "" + this.fileMangerId + ":" + this.segmentIndex + ":" + this.segmentOffset + ":" + this.sequenceId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getFileMangerId() {
/*  92 */     return this.fileMangerId;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getSegmentIndex() {
/*  98 */     return this.segmentIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSegmentOffset() {
/* 104 */     return this.segmentOffset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getSequenceId() {
/* 111 */     return this.sequenceId;
/*     */   }
/*     */   
/*     */   public RecordLocationImpl setSegmentIndexAndOffset(byte segmentIndex, int offset) {
/* 115 */     return new RecordLocationImpl(this.fileMangerId, segmentIndex, offset);
/*     */   }
/*     */   
/*     */   public RecordLocationImpl setSequence(long seq) {
/* 119 */     return new RecordLocationImpl(this.fileMangerId, this.segmentIndex, this.segmentOffset, seq);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\journal\impl\RecordLocationImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */