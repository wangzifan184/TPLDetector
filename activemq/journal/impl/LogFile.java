/*     */ package org.codehaus.activemq.journal.impl;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.text.NumberFormat;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import org.codehaus.activemq.journal.InvalidRecordLocationException;
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
/*     */ public final class LogFile
/*     */ {
/*     */   public static final byte DATA_RECORD_TYPE = 1;
/*     */   public static final byte MARK_RECORD_TYPE = 2;
/*  41 */   private static final NumberFormat onlineLogNameFormat = NumberFormat.getNumberInstance();
/*     */   
/*     */   static {
/*  44 */     onlineLogNameFormat.setMinimumIntegerDigits(3);
/*  45 */     onlineLogNameFormat.setMaximumIntegerDigits(3);
/*  46 */     onlineLogNameFormat.setGroupingUsed(false);
/*  47 */     onlineLogNameFormat.setParseIntegerOnly(true);
/*  48 */     onlineLogNameFormat.setMaximumFractionDigits(0);
/*     */   }
/*     */ 
/*     */   
/*     */   private final File logDirectory;
/*     */   
/*     */   private final int initialSegmentSize;
/*     */   
/*     */   private boolean closed;
/*     */   private final Segment[] segments;
/*  58 */   private final LinkedList activeSegments = new LinkedList();
/*  59 */   private final LinkedList inactiveSegments = new LinkedList();
/*     */   
/*  61 */   private byte markSegment = -1;
/*  62 */   private final Mark lastMark = new Mark();
/*  63 */   private byte appendSegment = -1;
/*  64 */   private int lastSegmentId = -1;
/*     */   
/*     */   public LogFile(File logDirectory) throws IOException {
/*  67 */     this(logDirectory, 4, 5242880);
/*     */   }
/*     */   
/*     */   public LogFile(File logDirectory, int onlineSegmentCount, int initialSegmentSize) throws IOException {
/*  71 */     this.logDirectory = logDirectory;
/*  72 */     this.segments = new Segment[onlineSegmentCount];
/*  73 */     this.initialSegmentSize = initialSegmentSize;
/*  74 */     initialize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initialize() throws IOException {
/*  85 */     if (!this.logDirectory.exists() && 
/*  86 */       !this.logDirectory.mkdirs()) {
/*  87 */       throw new IOException("Could not create directory: " + this.logDirectory);
/*     */     }
/*     */ 
/*     */     
/*  91 */     byte lastIndex = (byte)(this.segments.length - 1);
/*  92 */     int lastSegmentId = 0;
/*  93 */     Mark mark = null; byte i;
/*  94 */     for (i = 0; i < this.segments.length; i = (byte)(i + 1)) {
/*     */       
/*  96 */       this.segments[i] = new Segment(new File(this.logDirectory, "log-" + onlineLogNameFormat.format(i) + ".dat"), this.initialSegmentSize, i);
/*     */       
/*  98 */       if (this.segments[i].isActive()) {
/*  99 */         if ((this.segments[i].getLastMark()).sequenceId >= 0L) {
/* 100 */           this.markSegment = i;
/* 101 */           mark = this.segments[i].getLastMark();
/*     */         } 
/* 103 */         if (this.segments[i].getId() > lastSegmentId) {
/* 104 */           lastSegmentId = this.segments[i].getId();
/* 105 */           lastIndex = i;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 112 */     i = nextSegmentIndex(lastIndex);
/* 113 */     while (this.inactiveSegments.size() + this.activeSegments.size() < this.segments.length) {
/* 114 */       Segment segment = this.segments[i];
/* 115 */       if (segment.isActive()) {
/* 116 */         this.activeSegments.add(segment);
/*     */       } else {
/*     */         
/* 119 */         this.inactiveSegments.add(segment);
/*     */       } 
/* 121 */       i = nextSegmentIndex(i);
/*     */     } 
/* 123 */     if (mark != null) {
/* 124 */       setMark(mark);
/*     */     }
/*     */     
/* 127 */     if (this.activeSegments.size() == 0) {
/*     */       
/* 129 */       activateNextSegment();
/*     */     }
/*     */     else {
/*     */       
/* 133 */       Segment lastSegment = this.activeSegments.getLast();
/* 134 */       lastSegment.setReadOnly(false);
/* 135 */       this.appendSegment = lastSegment.getIndex();
/* 136 */       for (Iterator iter = this.activeSegments.iterator(); iter.hasNext(); ) {
/* 137 */         Segment s = iter.next();
/* 138 */         if (s != lastSegment) {
/* 139 */           s.setReadOnly(true);
/*     */         }
/*     */       } 
/*     */     } 
/* 143 */     if (mark == null) {
/*     */       
/* 145 */       Segment segment = this.activeSegments.getFirst();
/* 146 */       mark = new Mark();
/* 147 */       mark.sequenceId = segment.getIndex();
/* 148 */       mark.offsetId = 256;
/*     */     } 
/* 150 */     this.lastMark.copy(mark);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getNextSegmentId() {
/* 158 */     return ++this.lastSegmentId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 163 */     if (this.closed) {
/*     */       return;
/*     */     }
/* 166 */     this.closed = true;
/* 167 */     for (int i = 0; i < this.segments.length; i++) {
/* 168 */       this.segments[i].close();
/*     */     }
/*     */   }
/*     */   
/*     */   private void setMark(Mark mark) throws IOException {
/* 173 */     this.lastMark.copy(mark);
/*     */ 
/*     */     
/* 176 */     for (Iterator i = this.activeSegments.iterator(); i.hasNext(); ) {
/* 177 */       Segment segment = i.next();
/* 178 */       if (segment.getLastSequenceId() < this.lastMark.sequenceId) {
/* 179 */         segment.reinitialize();
/* 180 */         i.remove();
/* 181 */         this.inactiveSegments.add(segment); continue;
/*     */       } 
/* 183 */       this.markSegment = segment.getIndex();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendAndForce(BatchedWrite write) throws IOException {
/* 194 */     Segment segment = this.segments[this.appendSegment];
/* 195 */     segment.seek(segment.getAppendOffset());
/* 196 */     segment.write(write);
/* 197 */     if (write.getMark() != null) {
/* 198 */       setMark(write.getMark());
/*     */     }
/* 200 */     segment.force();
/*     */   }
/*     */   
/*     */   public static class RecordInfo
/*     */   {
/*     */     private final RecordLocationImpl location;
/*     */     private final RecordHeader header;
/*     */     
/*     */     public RecordInfo(RecordLocationImpl location, RecordHeader header) {
/* 209 */       this.location = location;
/* 210 */       this.header = header;
/*     */     }
/*     */     
/*     */     int getNextLocation() {
/* 214 */       return this.location.getSegmentOffset() + this.header.length + 35;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private RecordInfo readRecordInfo(RecordLocationImpl location) throws IOException, InvalidRecordLocationException {
/* 220 */     if (0 > location.getSegmentIndex() || location.getSegmentIndex() > this.segments.length) {
/* 221 */       throw new InvalidRecordLocationException("Invalid segment id.");
/*     */     }
/*     */     
/* 224 */     Segment segment = this.segments[location.getSegmentIndex()];
/* 225 */     segment.seek(location.getSegmentOffset());
/*     */ 
/*     */     
/* 228 */     if (segment.isAtAppendOffset()) {
/* 229 */       throw new InvalidRecordLocationException("No record at end of log.");
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 234 */       RecordHeader header = new RecordHeader();
/* 235 */       segment.readRecordHeader(header);
/* 236 */       return new RecordInfo(location, header);
/*     */     }
/* 238 */     catch (IOException e) {
/* 239 */       throw new InvalidRecordLocationException("No record at found.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RecordLocationImpl readRecordLocation(RecordLocationImpl location) throws IOException, InvalidRecordLocationException {
/* 250 */     RecordInfo info = readRecordInfo(location);
/* 251 */     return info.location.setSequence(info.header.sequenceId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RecordLocationImpl getNextDataRecordLocation(RecordLocationImpl lastLocation) throws IOException, InvalidRecordLocationException {
/* 259 */     RecordInfo ri = readRecordInfo(lastLocation);
/*     */     while (true) {
/* 261 */       byte segmentIndex = ri.location.getSegmentIndex();
/* 262 */       int offset = ri.getNextLocation();
/*     */ 
/*     */       
/* 265 */       if (offset >= this.segments[segmentIndex].getAppendOffset()) {
/* 266 */         segmentIndex = nextActiveSegmentIndex(segmentIndex);
/* 267 */         if (segmentIndex < 0) {
/* 268 */           return null;
/*     */         }
/* 270 */         offset = 256;
/*     */       } 
/*     */       
/*     */       try {
/* 274 */         ri = readRecordInfo(ri.location.setSegmentIndexAndOffset(segmentIndex, offset));
/*     */       }
/* 276 */       catch (InvalidRecordLocationException e) {
/* 277 */         return null;
/*     */       } 
/*     */ 
/*     */       
/* 281 */       if (ri.header.recordType == 1) {
/* 282 */         return ri.location.setSequence(ri.header.sequenceId);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private byte nextActiveSegmentIndex(byte i) {
/* 293 */     byte rc = nextSegmentIndex(i);
/* 294 */     return this.segments[rc].isActive() ? rc : -1;
/*     */   }
/*     */   
/*     */   private byte nextSegmentIndex(byte i) {
/* 298 */     i = (byte)(i + 1);
/* 299 */     if (i < this.segments.length) {
/* 300 */       return i;
/*     */     }
/* 302 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] readData(int segmentIndex, int segmentOffset) throws IOException {
/* 312 */     if (0 > segmentIndex || segmentIndex > this.segments.length) {
/* 313 */       return null;
/*     */     }
/*     */     
/* 316 */     Segment segment = this.segments[segmentIndex];
/* 317 */     segment.seek(segmentOffset);
/*     */ 
/*     */     
/* 320 */     if (segment.isAtAppendOffset()) {
/* 321 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 325 */     RecordHeader header = new RecordHeader();
/* 326 */     segment.readRecordHeader(header);
/* 327 */     byte[] data = new byte[header.length];
/* 328 */     segment.read(data);
/*     */     
/* 330 */     return data;
/*     */   }
/*     */   
/*     */   public int getInitialSegmentSize() {
/* 334 */     return this.initialSegmentSize;
/*     */   }
/*     */   
/*     */   public boolean isSegmentIndexActive(byte i) {
/* 338 */     synchronized (this.segments[i]) {
/* 339 */       return this.segments[i].isActive();
/*     */     } 
/*     */   }
/*     */   
/*     */   public long getFirstSequenceIdOfSegementIndex(byte i) {
/* 344 */     synchronized (this.segments[i]) {
/* 345 */       return this.segments[i].getFirstSequenceId();
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized boolean canActivateNextSegment() {
/* 350 */     return (this.inactiveSegments.size() > 0);
/*     */   }
/*     */   
/*     */   public byte getFirstActiveSegmentIndex() {
/* 354 */     return ((Segment)this.activeSegments.getFirst()).getIndex();
/*     */   }
/*     */ 
/*     */   
/*     */   void activateNextSegment() throws IOException {
/* 359 */     if (this.appendSegment >= 0) {
/* 360 */       this.segments[this.appendSegment].setReadOnly(true);
/*     */     }
/* 362 */     Segment next = this.inactiveSegments.removeFirst();
/* 363 */     this.activeSegments.addLast(next);
/* 364 */     next.activate(getNextSegmentId());
/* 365 */     this.appendSegment = next.getIndex();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getAppendSegmentIndex() {
/* 372 */     return this.appendSegment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAppendSegmentOffset() {
/* 379 */     return this.segments[this.appendSegment].getAppendOffset();
/*     */   }
/*     */   
/*     */   int getTotalSegements() {
/* 383 */     return this.segments.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLastSequenceId() {
/* 390 */     return this.segments[this.appendSegment].getLastSequenceId();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized RecordLocationImpl getFirstRecordLocationOfSecondActiveSegment(byte fm) {
/* 397 */     return ((Segment)this.activeSegments.get(1)).getFirstRecordLocation(fm);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getLogDirectory() {
/* 404 */     return this.logDirectory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public RecordLocationImpl getLastMarkedRecordLocation(byte fm) {
/* 410 */     if (this.markSegment == -1)
/* 411 */       return null; 
/* 412 */     return new RecordLocationImpl(fm, this.markSegment, this.lastMark.offsetId, this.lastMark.sequenceId);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\journal\impl\LogFile.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */