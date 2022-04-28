/*     */ package org.codehaus.activemq.journal.impl;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.FutureResult;
/*     */ import EDU.oswego.cs.dl.util.concurrent.QueuedExecutor;
/*     */ import EDU.oswego.cs.dl.util.concurrent.ThreadFactory;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.journal.InvalidRecordLocationException;
/*     */ import org.codehaus.activemq.journal.JournalEventListener;
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
/*     */ public class LogFileManager
/*     */ {
/*  42 */   private static final Log log = LogFactory.getLog(LogFileManager.class);
/*     */   
/*     */   private static final int LOG_HEADER_SIZE = 512;
/*     */   
/*     */   private static final int OVERFLOW_RENOTIFICATION_DELAY = 500;
/*     */   
/*     */   private final LongSequenceGenerator sequenceGenerator;
/*     */   
/*     */   private final byte fileManagerId;
/*     */   private boolean closed = false;
/*  52 */   private byte markedSegmentIndex = 0;
/*     */   
/*  54 */   private byte appendSegmentIndex = 0;
/*     */   
/*  56 */   private int appendSegmentOffset = 0;
/*     */   
/*     */   private BatchedWrite pendingBatchWrite;
/*     */   
/*     */   private RecordLocationImpl lastMarkedLocation;
/*     */   
/*     */   private LogFile file;
/*     */   
/*     */   private QueuedExecutor executor;
/*     */   
/*     */   private int rolloverFence;
/*     */   private JournalEventListener eventListener;
/*     */   private ByteBufferPool byteBufferPool;
/*  69 */   private long overflowNotificationTime = System.currentTimeMillis();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogFileManager(byte fileManagerId, LongSequenceGenerator sequenceGenerator, File logDirectory) throws IOException {
/*  75 */     this(fileManagerId, sequenceGenerator, new LogFile(logDirectory));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LogFileManager(byte fileManagerId, LongSequenceGenerator sequenceGenerator, LogFile logFile) {
/*  81 */     this.fileManagerId = fileManagerId;
/*  82 */     this.sequenceGenerator = sequenceGenerator;
/*  83 */     this.file = logFile;
/*  84 */     this.byteBufferPool = new ByteBufferPool();
/*  85 */     this.executor = new QueuedExecutor();
/*  86 */     this.executor.setThreadFactory(new ThreadFactory() { private final LogFileManager this$0;
/*     */           public Thread newThread(Runnable runnable) {
/*  88 */             Thread answer = new Thread(runnable, "Journal Writter");
/*  89 */             answer.setPriority(10);
/*  90 */             answer.setDaemon(true);
/*  91 */             return answer;
/*     */           } }
/*     */       );
/*     */ 
/*     */     
/*  96 */     this.lastMarkedLocation = this.file.getLastMarkedRecordLocation(fileManagerId);
/*  97 */     this.appendSegmentIndex = this.file.getAppendSegmentIndex();
/*  98 */     this.appendSegmentOffset = this.file.getAppendSegmentOffset();
/*  99 */     this.rolloverFence = this.file.getInitialSegmentSize() / 10 * 9;
/*     */   }
/*     */   
/*     */   public RecordLocationImpl write(byte[] data, boolean sync) throws IOException {
/* 103 */     return write((byte)1, data, sync, null);
/*     */   }
/*     */   
/*     */   private RecordLocationImpl write(byte recordType, byte[] data, boolean sync, Mark mark) throws IOException {
/*     */     try {
/*     */       RecordLocationImpl location;
/*     */       BatchedWrite writeCommand;
/* 110 */       synchronized (this) {
/* 111 */         if (this.closed) {
/* 112 */           throw new IOException("Journal has been closed.");
/*     */         }
/*     */ 
/*     */         
/* 116 */         long sequenceId = this.sequenceGenerator.getNextSequenceId();
/* 117 */         location = new RecordLocationImpl(this.fileManagerId, this.appendSegmentIndex, this.appendSegmentOffset, sequenceId);
/* 118 */         Record record = new Record(sequenceId, recordType, data, mark);
/*     */         
/* 120 */         writeCommand = addToPendingWriteBatch(record);
/*     */ 
/*     */         
/* 123 */         this.appendSegmentOffset += data.length + 35;
/* 124 */         rolloverCheck();
/*     */       } 
/*     */       
/* 127 */       if (sync) {
/* 128 */         writeCommand.waitForForce();
/*     */       }
/*     */       
/* 131 */       return location;
/* 132 */     } catch (IOException e) {
/* 133 */       RecordLocationImpl location; throw location;
/* 134 */     } catch (InterruptedException e) {
/* 135 */       throw (IOException)(new InterruptedIOException()).initCause(e);
/* 136 */     } catch (Throwable e) {
/* 137 */       throw (IOException)(new IOException("Write failed: " + e)).initCause(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BatchedWrite addToPendingWriteBatch(Record record) throws InterruptedException {
/* 150 */     BatchedWrite answer = null;
/* 151 */     while (record.remaining() > 0) {
/*     */       
/* 153 */       if (this.pendingBatchWrite == null) {
/* 154 */         final BatchedWrite write = new BatchedWrite(this.byteBufferPool.getByteBuffer());
/* 155 */         this.pendingBatchWrite = write;
/* 156 */         this.executor.execute(new Runnable() { private final BatchedWrite val$write; private final LogFileManager this$0;
/*     */               public void run() {
/*     */                 try {
/* 159 */                   LogFileManager.this.queuedWrite(write);
/* 160 */                 } catch (InterruptedException e) {}
/*     */               } }
/*     */           );
/*     */       } 
/*     */       
/* 165 */       answer = this.pendingBatchWrite;
/*     */       
/* 167 */       if (!this.pendingBatchWrite.append(record)) {
/* 168 */         this.pendingBatchWrite = null;
/*     */       }
/*     */     } 
/* 171 */     return answer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void queuedWrite(BatchedWrite write) throws InterruptedException {
/* 183 */     write.disableAppend();
/*     */ 
/*     */     
/*     */     try {
/* 187 */       this.file.appendAndForce(write);
/* 188 */       write.forced();
/* 189 */     } catch (Throwable e) {
/* 190 */       write.writeFailed(e);
/*     */     } finally {
/* 192 */       this.byteBufferPool.returnByteBuffer(write.getByteBuffer());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void rolloverCheck() throws IOException {
/* 202 */     if (this.eventListener != null && !this.file.canActivateNextSegment() && this.overflowNotificationTime + 500L < System.currentTimeMillis()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 208 */       RecordLocationImpl safeSpot = this.file.getFirstRecordLocationOfSecondActiveSegment(this.fileManagerId);
/* 209 */       this.eventListener.overflowNotification(safeSpot);
/* 210 */       this.overflowNotificationTime = System.currentTimeMillis();
/*     */     } 
/*     */ 
/*     */     
/* 214 */     if (this.appendSegmentOffset > this.rolloverFence && this.file.canActivateNextSegment()) {
/*     */ 
/*     */       
/* 217 */       this.overflowNotificationTime -= 500L;
/*     */       
/* 219 */       final FutureResult result = new FutureResult();
/*     */       try {
/* 221 */         this.executor.execute(new Runnable() { private final FutureResult val$result; private final LogFileManager this$0;
/*     */               public void run() {
/*     */                 try {
/* 224 */                   result.set(LogFileManager.this.queuedActivateNextSegment());
/* 225 */                 } catch (Throwable e) {
/* 226 */                   result.setException(e);
/*     */                 } 
/*     */               } }
/*     */           );
/* 230 */         this.appendSegmentIndex = ((Byte)result.get()).byteValue();
/* 231 */         this.appendSegmentOffset = 256;
/*     */       }
/* 233 */       catch (InterruptedException e) {
/* 234 */         throw (IOException)(new IOException("Interrupted.")).initCause(e);
/* 235 */       } catch (InvocationTargetException e) {
/* 236 */         if (e.getTargetException() instanceof IOException)
/* 237 */           throw (IOException)(new IOException(e.getTargetException().getMessage())).initCause(e.getTargetException()); 
/* 238 */         throw (IOException)(new IOException("Unexpected Exception: ")).initCause(e.getTargetException());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Byte queuedActivateNextSegment() throws IOException {
/* 247 */     this.file.activateNextSegment();
/* 248 */     return new Byte(this.file.getAppendSegmentIndex());
/*     */   }
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
/*     */   public synchronized void setMark(RecordLocationImpl recordLocator, boolean force) throws InvalidRecordLocationException, InterruptedException, IOException {
/* 261 */     if (recordLocator == null)
/* 262 */       throw new InvalidRecordLocationException("The location cannot be null."); 
/* 263 */     if (this.lastMarkedLocation != null && recordLocator.compareTo(this.lastMarkedLocation) < 0)
/* 264 */       throw new InvalidRecordLocationException("The location is less than the last mark."); 
/* 265 */     this.lastMarkedLocation = recordLocator;
/* 266 */     Mark mark = new Mark(recordLocator);
/* 267 */     byte[] data = mark.writeExternal();
/* 268 */     write((byte)2, data, force, mark);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RecordLocationImpl getMark() {
/* 275 */     return this.lastMarkedLocation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RecordLocationImpl getNextRecordLocation(final RecordLocationImpl lastLocation) throws IOException, InvalidRecordLocationException {
/* 285 */     if (lastLocation == null) {
/* 286 */       if (this.lastMarkedLocation != null) {
/* 287 */         return this.lastMarkedLocation;
/*     */       }
/* 289 */       byte safeSeg = this.file.getFirstActiveSegmentIndex();
/*     */       try {
/* 291 */         return this.file.readRecordLocation(new RecordLocationImpl(this.fileManagerId, safeSeg, 256));
/* 292 */       } catch (InvalidRecordLocationException e1) {
/* 293 */         return null;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 299 */     final FutureResult result = new FutureResult();
/*     */     try {
/* 301 */       this.executor.execute(new Runnable() { private final FutureResult val$result;
/*     */             public void run() {
/*     */               try {
/* 304 */                 result.set(LogFileManager.this.queuedGetNextRecordLocation(lastLocation));
/* 305 */               } catch (Throwable e) {
/* 306 */                 result.setException(e);
/*     */               } 
/*     */             } private final RecordLocationImpl val$lastLocation; private final LogFileManager this$0; }
/*     */         );
/* 310 */       return (RecordLocationImpl)result.get();
/* 311 */     } catch (InterruptedException e) {
/* 312 */       throw (IOException)(new IOException("Interrupted.")).initCause(e);
/* 313 */     } catch (InvocationTargetException e) {
/* 314 */       if (e.getTargetException() instanceof InvalidRecordLocationException)
/* 315 */         throw new InvalidRecordLocationException(e.getTargetException().getMessage(), e.getTargetException()); 
/* 316 */       if (e.getTargetException() instanceof IOException)
/* 317 */         throw (IOException)(new IOException(e.getTargetException().getMessage())).initCause(e.getTargetException()); 
/* 318 */       throw (IOException)(new IOException("Unexpected Exception: ")).initCause(e.getTargetException());
/*     */     } 
/*     */   }
/*     */   
/*     */   private RecordLocationImpl queuedGetNextRecordLocation(RecordLocationImpl location) throws IOException, InvalidRecordLocationException {
/* 323 */     return this.file.getNextDataRecordLocation(location);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] read(final RecordLocationImpl location) throws IOException, InvalidRecordLocationException {
/* 334 */     final FutureResult result = new FutureResult();
/*     */     try {
/* 336 */       this.executor.execute(new Runnable() { private final FutureResult val$result; private final RecordLocationImpl val$location; private final LogFileManager this$0;
/*     */             public void run() {
/*     */               try {
/* 339 */                 result.set(LogFileManager.this.queuedRead(location));
/* 340 */               } catch (Throwable e) {
/* 341 */                 result.setException(e);
/*     */               } 
/*     */             } }
/*     */         );
/* 345 */       return (byte[])result.get();
/* 346 */     } catch (InterruptedException e) {
/* 347 */       throw (IOException)(new IOException("Interrupted.")).initCause(e);
/* 348 */     } catch (InvocationTargetException e) {
/* 349 */       if (e.getTargetException() instanceof InvalidRecordLocationException)
/* 350 */         throw new InvalidRecordLocationException(e.getTargetException().getMessage(), e.getTargetException()); 
/* 351 */       if (e.getTargetException() instanceof IOException)
/* 352 */         throw (IOException)(new IOException(e.getTargetException().getMessage())).initCause(e.getTargetException()); 
/* 353 */       throw (IOException)(new IOException("Unexpected Exception: ")).initCause(e.getTargetException());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private byte[] queuedRead(RecordLocationImpl newLocation) throws IOException, InvalidRecordLocationException {
/*     */     int segmentIndex;
/*     */     int segmentOffset;
/* 361 */     if (newLocation == null) {
/* 362 */       segmentIndex = this.markedSegmentIndex;
/* 363 */       segmentOffset = 256;
/*     */     } else {
/* 365 */       segmentIndex = newLocation.getSegmentIndex();
/* 366 */       segmentOffset = newLocation.getSegmentOffset();
/*     */     } 
/*     */     
/* 369 */     return this.file.readData(segmentIndex, segmentOffset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJournalEventListener(JournalEventListener eventListener) {
/* 376 */     this.eventListener = eventListener;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 384 */     if (this.closed)
/*     */       return; 
/* 386 */     this.executor.shutdownAfterProcessingCurrentlyQueuedTasks(); 
/* 387 */     try { this.file.close(); } catch (Throwable e) {}
/* 388 */     this.closed = true;
/*     */   }
/*     */   
/*     */   public long getLastSequenceId() {
/* 392 */     return this.file.getLastSequenceId();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getLogDirectory() {
/* 399 */     return this.file.getLogDirectory();
/*     */   }
/*     */   
/*     */   public int getTotalSegements() {
/* 403 */     return this.file.getTotalSegements();
/*     */   }
/*     */   
/*     */   public int getInitialSegmentSize() {
/* 407 */     return this.file.getInitialSegmentSize();
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\journal\impl\LogFileManager.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */