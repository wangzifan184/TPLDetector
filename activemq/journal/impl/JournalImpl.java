/*     */ package org.codehaus.activemq.journal.impl;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import org.codehaus.activemq.journal.InvalidRecordLocationException;
/*     */ import org.codehaus.activemq.journal.Journal;
/*     */ import org.codehaus.activemq.journal.JournalEventListener;
/*     */ import org.codehaus.activemq.journal.RecordLocation;
/*     */ import org.codehaus.activemq.util.LongSequenceGenerator;
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
/*     */ public class JournalImpl
/*     */   implements Journal
/*     */ {
/*     */   LogFileManager manager;
/*  57 */   LongSequenceGenerator sequenceGenerator = new LongSequenceGenerator();
/*     */   
/*     */   public JournalImpl() throws IOException {
/*  60 */     this(new File("logs"));
/*     */   }
/*     */   
/*     */   public JournalImpl(File logDirectory) throws IOException {
/*  64 */     this.manager = new LogFileManager((byte)0, this.sequenceGenerator, new LogFile(logDirectory));
/*  65 */     initSequenceId();
/*     */   }
/*     */   
/*     */   public JournalImpl(File logDirectory, int segments, int segmentSize) throws IOException {
/*  69 */     this.manager = new LogFileManager((byte)0, this.sequenceGenerator, new LogFile(logDirectory, segments, segmentSize));
/*  70 */     initSequenceId();
/*     */   }
/*     */ 
/*     */   
/*     */   private void initSequenceId() {
/*  75 */     long id = this.manager.getLastSequenceId();
/*  76 */     if (id == -1L) {
/*  77 */       id = 0L;
/*     */     }
/*  79 */     this.sequenceGenerator.setLastSequenceId(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RecordLocation write(byte[] data, boolean sync) throws IOException {
/*  87 */     return this.manager.write(data, sync);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMark(RecordLocation recordLocator, boolean force) throws InvalidRecordLocationException, IOException {
/*  96 */     RecordLocationImpl rl = (RecordLocationImpl)recordLocator;
/*     */     try {
/*  98 */       this.manager.setMark(rl, force);
/*     */     }
/* 100 */     catch (InterruptedException e) {
/* 101 */       throw new InterruptedIOException();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RecordLocation getMark() {
/* 109 */     return this.manager.getMark();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 116 */     this.manager.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJournalEventListener(JournalEventListener eventListener) {
/* 123 */     this.manager.setJournalEventListener(eventListener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RecordLocation getNextRecordLocation(RecordLocation lastLocation) throws IOException, InvalidRecordLocationException {
/* 134 */     RecordLocationImpl rl = (RecordLocationImpl)lastLocation;
/* 135 */     return this.manager.getNextRecordLocation(rl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] read(RecordLocation location) throws InvalidRecordLocationException, IOException {
/* 144 */     RecordLocationImpl rl = (RecordLocationImpl)location;
/* 145 */     return this.manager.read(rl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 152 */     return "JournalImpl at '" + this.manager.getLogDirectory() + "' using " + this.manager.getTotalSegements() + " x " + (this.manager.getInitialSegmentSize() / 1048576.0F) + " Meg log files.";
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\journal\impl\JournalImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */