/*     */ package org.codehaus.activemq.message.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
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
/*     */ class FileDataBlock
/*     */ {
/*     */   private RandomAccessFile dataBlock;
/*     */   private File file;
/*     */   private long maxSize;
/*     */   private long currentOffset;
/*     */   
/*     */   FileDataBlock(File file, long maxSize) throws IOException {
/*  43 */     this.file = file;
/*  44 */     this.maxSize = maxSize;
/*  45 */     this.dataBlock = new RandomAccessFile(file, "rw");
/*  46 */     if (this.dataBlock.length() > 0L) {
/*  47 */       this.currentOffset = this.dataBlock.readLong();
/*     */     } else {
/*     */       
/*  50 */       this.dataBlock.writeLong(0L);
/*  51 */       this.currentOffset = this.dataBlock.length();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   synchronized void close() throws IOException {
/*  61 */     this.dataBlock.close();
/*  62 */     this.file.delete();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   synchronized boolean isEnoughSpace(byte[] data) throws IOException {
/*  73 */     return (this.dataBlock.length() + data.length < this.maxSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   synchronized void write(byte[] data) throws IOException {
/*  83 */     this.dataBlock.seek(this.dataBlock.length());
/*  84 */     this.dataBlock.writeInt(data.length);
/*  85 */     this.dataBlock.write(data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   synchronized byte[] read() throws IOException {
/*  95 */     byte[] result = null;
/*  96 */     if (this.currentOffset > 0L && this.currentOffset < this.dataBlock.length()) {
/*  97 */       this.dataBlock.seek(this.currentOffset);
/*  98 */       int length = this.dataBlock.readInt();
/*  99 */       result = new byte[length];
/* 100 */       this.dataBlock.readFully(result);
/* 101 */       this.currentOffset = this.dataBlock.getFilePointer();
/* 102 */       updateHeader(this.currentOffset);
/*     */     } 
/* 104 */     return result;
/*     */   }
/*     */   
/*     */   private void updateHeader(long pos) throws IOException {
/* 108 */     this.dataBlock.seek(0L);
/* 109 */     this.dataBlock.writeLong(pos);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\messag\\util\FileDataBlock.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */