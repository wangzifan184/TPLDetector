/*     */ package org.codehaus.activemq.journal.impl;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
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
/*     */ public class Segment
/*     */ {
/*     */   public static final int SEGMENT_HEADER_SIZE = 256;
/*     */   private File file;
/*     */   private final int initialSize;
/*     */   private final byte index;
/*     */   private int id;
/*     */   private long firstSequenceId;
/*     */   private long lastSequenceId;
/*     */   private boolean active = false;
/*  51 */   private final Mark lastMark = new Mark();
/*     */   
/*  53 */   private int appendOffset = 0;
/*     */ 
/*     */   
/*     */   private RandomAccessFile randomAccessFile;
/*     */   
/*     */   private boolean readOnly;
/*     */   
/*     */   private FileChannel fileChannel;
/*     */ 
/*     */   
/*     */   public Segment(File file, int initialSize, byte index) throws IOException {
/*  64 */     this.file = file;
/*  65 */     this.initialSize = initialSize;
/*  66 */     this.index = index;
/*  67 */     boolean initializationNeeeded = !file.exists();
/*  68 */     this.randomAccessFile = new RandomAccessFile(file, "rw");
/*  69 */     this.randomAccessFile.seek(0L);
/*  70 */     this.fileChannel = this.randomAccessFile.getChannel();
/*  71 */     if (initializationNeeeded) {
/*  72 */       reinitialize();
/*     */     }
/*  74 */     loadState();
/*     */   }
/*     */   
/*     */   public void seek(int newPos) throws IOException {
/*  78 */     this.randomAccessFile.seek(newPos);
/*     */   }
/*     */   
/*     */   private void loadState() throws IOException {
/*  82 */     seek(0);
/*     */ 
/*     */     
/*     */     try {
/*  86 */       read(this.randomAccessFile);
/*  87 */       int firstCode = hashCode();
/*  88 */       read(this.randomAccessFile);
/*     */       
/*  90 */       if (firstCode == hashCode() && (this.readOnly || !this.active))
/*     */       {
/*     */         return;
/*     */       
/*     */       }
/*     */     }
/*  96 */     catch (IOException e) {}
/*     */ 
/*     */ 
/*     */     
/* 100 */     this.firstSequenceId = -1L;
/* 101 */     this.lastSequenceId = -1L;
/* 102 */     this.readOnly = false;
/* 103 */     this.active = true;
/*     */     
/* 105 */     int offset = 256;
/* 106 */     long fileSize = this.randomAccessFile.length();
/* 107 */     RecordHeader header = new RecordHeader();
/* 108 */     RecordFooter footer = new RecordFooter();
/* 109 */     while ((offset + 35) < fileSize) {
/*     */ 
/*     */       
/* 112 */       seek(offset);
/*     */       try {
/* 114 */         header.readRecordHeader(this.randomAccessFile);
/* 115 */       } catch (IOException e) {
/*     */         break;
/*     */       } 
/*     */ 
/*     */       
/* 120 */       if (!header.isValid())
/*     */         break; 
/* 122 */       if (this.firstSequenceId == -1L) {
/* 123 */         this.firstSequenceId = header.sequenceId;
/*     */       
/*     */       }
/* 126 */       else if (this.lastSequenceId >= header.sequenceId) {
/*     */         break;
/*     */       } 
/*     */ 
/*     */       
/* 131 */       if ((offset + header.length + 35) > fileSize) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/* 136 */       byte[] data = null;
/* 137 */       if (RecordFooter.isChecksumingEnabled() || header.recordType == 2) {
/* 138 */         data = new byte[header.length];
/* 139 */         this.randomAccessFile.readFully(data);
/*     */       } 
/*     */ 
/*     */       
/* 143 */       seek(offset + header.length + 16);
/*     */       try {
/* 145 */         footer.readRecordFooter(this.randomAccessFile);
/* 146 */       } catch (IOException e) {
/*     */         break;
/*     */       } 
/*     */ 
/*     */       
/* 151 */       if (!footer.matches(header))
/*     */         break; 
/* 153 */       if (RecordFooter.isChecksumingEnabled() && 
/* 154 */         !footer.matches(data)) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/* 159 */       if (header.recordType == 2) {
/* 160 */         this.lastMark.readExternal(data);
/*     */       }
/*     */       
/* 163 */       this.lastSequenceId = header.sequenceId;
/* 164 */       offset += header.length + 35;
/* 165 */       this.appendOffset = offset;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 170 */     storeState();
/*     */   }
/*     */   
/*     */   private void resize() throws IOException {
/* 174 */     this.randomAccessFile.setLength(this.initialSize);
/*     */   }
/*     */   
/*     */   private void storeState() throws IOException {
/* 178 */     int restorePos = getSeekPos();
/*     */ 
/*     */ 
/*     */     
/* 182 */     seek(0);
/* 183 */     writeHeader(this.randomAccessFile);
/* 184 */     writeHeader(this.randomAccessFile);
/* 185 */     force();
/* 186 */     seek(restorePos);
/*     */   }
/*     */   
/*     */   private int getSeekPos() throws IOException {
/* 190 */     return (int)this.randomAccessFile.getFilePointer();
/*     */   }
/*     */   
/*     */   public void force() throws IOException {
/* 194 */     this.fileChannel.force(false);
/*     */   }
/*     */   
/*     */   public void reinitialize() throws IOException {
/* 198 */     this.readOnly = true;
/* 199 */     this.active = false;
/* 200 */     this.firstSequenceId = -1L;
/* 201 */     this.appendOffset = -1;
/* 202 */     this.id = -1;
/* 203 */     this.lastSequenceId = -1L;
/* 204 */     this.lastMark.offsetId = -1;
/* 205 */     this.lastMark.sequenceId = -1L;
/* 206 */     this.appendOffset = 256;
/* 207 */     resize();
/* 208 */     storeState();
/*     */   }
/*     */   
/*     */   public void setReadOnly(boolean enable) throws IOException {
/* 212 */     if (!this.active) {
/* 213 */       throw new IllegalStateException("Not Active.");
/*     */     }
/* 215 */     this.readOnly = enable;
/* 216 */     storeState();
/*     */   }
/*     */   
/*     */   public void activate(int id) throws IOException {
/* 220 */     if (this.active) {
/* 221 */       throw new IllegalStateException("Allready Active.");
/*     */     }
/* 223 */     this.id = id;
/* 224 */     this.readOnly = false;
/* 225 */     this.active = true;
/* 226 */     this.appendOffset = 256;
/* 227 */     this.lastSequenceId = this.firstSequenceId = -1L;
/* 228 */     storeState();
/* 229 */     seek(this.appendOffset);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 233 */     int rc = this.id;
/* 234 */     rc ^= this.appendOffset;
/* 235 */     rc ^= (int)(0xFFFFFFFFFFFFFFFFL & this.firstSequenceId);
/* 236 */     rc ^= (int)(0xFFFFFFFFFFFFFFFFL & this.firstSequenceId >> 4L);
/* 237 */     rc ^= (int)(0xFFFFFFFFFFFFFFFFL & this.lastSequenceId);
/* 238 */     rc ^= (int)(0xFFFFFFFFFFFFFFFFL & this.lastSequenceId >> 4L);
/* 239 */     if (this.active)
/* 240 */       rc ^= 0xABD01212; 
/* 241 */     if (this.readOnly)
/* 242 */       rc ^= 0x1F8ADC21; 
/* 243 */     return rc;
/*     */   }
/*     */   
/*     */   private void writeHeader(DataOutput f) throws IOException {
/* 247 */     f.writeInt(this.id);
/* 248 */     f.writeLong(this.firstSequenceId);
/* 249 */     f.writeLong(this.lastSequenceId);
/* 250 */     f.writeBoolean(this.active);
/* 251 */     f.writeBoolean(this.readOnly);
/* 252 */     f.writeLong(this.lastMark.sequenceId);
/* 253 */     f.writeInt(this.lastMark.offsetId);
/* 254 */     f.writeInt(this.appendOffset);
/* 255 */     f.writeInt(hashCode());
/*     */   }
/*     */   
/*     */   private void read(DataInput f) throws IOException {
/* 259 */     this.id = f.readInt();
/* 260 */     this.firstSequenceId = f.readLong();
/* 261 */     this.lastSequenceId = f.readLong();
/* 262 */     this.active = f.readBoolean();
/* 263 */     this.readOnly = f.readBoolean();
/* 264 */     this.lastMark.sequenceId = f.readLong();
/* 265 */     this.lastMark.offsetId = f.readInt();
/* 266 */     this.appendOffset = f.readInt();
/*     */ 
/*     */     
/* 269 */     if (f.readInt() != hashCode())
/* 270 */       throw new IOException(); 
/*     */   }
/*     */   
/*     */   public int getId() {
/* 274 */     return this.id;
/*     */   }
/*     */   public boolean isActive() {
/* 277 */     return this.active;
/*     */   }
/*     */   public long getFirstSequenceId() {
/* 280 */     return this.firstSequenceId;
/*     */   }
/*     */   public Mark getLastMark() {
/* 283 */     return this.lastMark;
/*     */   }
/*     */   public long getLastSequenceId() {
/* 286 */     return this.lastSequenceId;
/*     */   }
/*     */   public int getAppendOffset() {
/* 289 */     return this.appendOffset;
/*     */   }
/*     */   
/*     */   public boolean isAtAppendOffset() throws IOException {
/* 293 */     return (this.appendOffset == getSeekPos());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(BatchedWrite write) throws IOException {
/* 302 */     ByteBuffer byteBuffer = write.getByteBuffer();
/* 303 */     byteBuffer.flip();
/*     */     
/* 305 */     write(byteBuffer);
/*     */     
/* 307 */     if (this.firstSequenceId == -1L)
/* 308 */       this.firstSequenceId = write.getFirstSequenceId(); 
/* 309 */     this.lastSequenceId = write.getLastSequenceId();
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(ByteBuffer byteBuffer) throws IOException {
/* 314 */     if (!this.active)
/* 315 */       throw new IllegalStateException("Segment is not active.  Writes are not allowed"); 
/* 316 */     if (this.readOnly)
/* 317 */       throw new IllegalStateException("Segment has been marked Read Only.  Writes are not allowed"); 
/* 318 */     if (!isAtAppendOffset()) {
/* 319 */       throw new IllegalStateException("Segment not at append location.");
/*     */     }
/* 321 */     int writeLength = byteBuffer.remaining();
/* 322 */     while (byteBuffer.remaining() > 0) {
/* 323 */       this.fileChannel.write(byteBuffer);
/*     */     }
/*     */ 
/*     */     
/* 327 */     this.appendOffset += writeLength;
/*     */   }
/*     */   
/*     */   public void readRecordHeader(RecordHeader seekedRecordHeader) throws IOException {
/* 331 */     seekedRecordHeader.readRecordHeader(this.randomAccessFile);
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 335 */     this.randomAccessFile.close();
/*     */   }
/*     */   
/*     */   public void read(byte[] answer) throws IOException {
/* 339 */     this.randomAccessFile.readFully(answer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReadOnly() {
/* 346 */     return this.readOnly;
/*     */   }
/*     */   
/*     */   public byte getIndex() {
/* 350 */     return this.index;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 354 */     return "Segment:" + this.index + ":" + this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RecordLocationImpl getFirstRecordLocation(byte fm) {
/* 361 */     return new RecordLocationImpl(fm, this.index, 256, this.firstSequenceId);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\journal\impl\Segment.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */