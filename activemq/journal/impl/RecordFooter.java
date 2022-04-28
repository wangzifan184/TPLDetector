/*     */ package org.codehaus.activemq.journal.impl;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.zip.CRC32;
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
/*     */ public class RecordFooter
/*     */ {
/*     */   public static final int SELECTED_CHECKSUM_ALGORITHIM;
/*     */   public static final int NO_CHECKSUM_ALGORITHIM = 0;
/*     */   public static final int HASH_CHECKSUM_ALGORITHIM = 1;
/*     */   public static final int CRC32_CHECKSUM_ALGORITHIM = 2;
/*     */   public static final int RECORD_FOOTER_SIZE = 19;
/*     */   
/*     */   static {
/*  39 */     String type = System.getProperty("org.codehaus.activemq.journal.impl.SELECTED_CHECKSUM_ALGORITHIM", "hash");
/*  40 */     if ("none".equals(type)) {
/*  41 */       SELECTED_CHECKSUM_ALGORITHIM = 0;
/*  42 */     } else if ("crc32".equals(type)) {
/*  43 */       SELECTED_CHECKSUM_ALGORITHIM = 2;
/*  44 */     } else if ("hash".equals(type)) {
/*  45 */       SELECTED_CHECKSUM_ALGORITHIM = 1;
/*     */     } else {
/*  47 */       System.err.println("System property 'org.codehaus.activemq.journal.impl.SELECTED_CHECKSUM_ALGORITHIM' not set properly.  Valid values are: 'none', 'hash', or 'crc32'");
/*  48 */       SELECTED_CHECKSUM_ALGORITHIM = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  53 */   public static final byte[] END_OF_RECORD = new byte[] { 69, 111, 82 };
/*     */ 
/*     */   
/*     */   public long checksum;
/*     */   
/*     */   public long sequenceId;
/*     */ 
/*     */   
/*     */   public ByteBuffer toByteBuffer() {
/*  62 */     ByteBuffer buff = ByteBuffer.allocate(19);
/*  63 */     buff.putLong(this.checksum).putLong(this.sequenceId).put(END_OF_RECORD).flip();
/*     */ 
/*     */ 
/*     */     
/*  67 */     return buff;
/*     */   }
/*     */   
/*     */   void writeRecordFooter(DataOutput out) throws IOException {
/*  71 */     out.writeLong(this.checksum);
/*  72 */     out.writeLong(this.sequenceId);
/*  73 */     out.write(END_OF_RECORD);
/*     */   }
/*     */   
/*     */   void readRecordFooter(DataInput in) throws IOException {
/*  77 */     this.checksum = in.readLong();
/*  78 */     this.sequenceId = in.readLong();
/*  79 */     for (int i = 0; i < END_OF_RECORD.length; i++) {
/*  80 */       byte checkByte = END_OF_RECORD[i];
/*  81 */       if (in.readByte() != checkByte) {
/*  82 */         throw new IOException("Not a valid record header.");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void invalidate() {
/*  91 */     this.checksum = -1L;
/*  92 */     this.sequenceId = -1L;
/*     */   }
/*     */   
/*     */   public boolean matches(RecordHeader header) {
/*  96 */     return (header.sequenceId == this.sequenceId);
/*     */   }
/*     */   
/*     */   public static boolean isChecksumingEnabled() {
/* 100 */     return (SELECTED_CHECKSUM_ALGORITHIM != 0);
/*     */   }
/*     */   
/*     */   public boolean matches(byte[] data) {
/* 104 */     return (buildChecksum(data) == this.checksum);
/*     */   }
/*     */   
/*     */   public static long buildChecksum(byte[] data) {
/* 108 */     if (SELECTED_CHECKSUM_ALGORITHIM == 1) {
/* 109 */       byte[] rc = new byte[8];
/* 110 */       for (int i = 0; i < data.length; i++) {
/* 111 */         rc[i % 8] = (byte)(rc[i % 8] ^ data[i]);
/*     */       }
/* 113 */       return (rc[0] | rc[1] << 1 | rc[2] << 2 | rc[3] << 3 | rc[4] << 4 | rc[5] << 5 | rc[6] << 6 | rc[7] << 7);
/* 114 */     }  if (SELECTED_CHECKSUM_ALGORITHIM == 2) {
/* 115 */       CRC32 crc32 = new CRC32();
/* 116 */       crc32.update(data);
/* 117 */       return crc32.getValue();
/*     */     } 
/* 119 */     return 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bulkSet(RecordHeader header, byte[] data) {
/* 128 */     this.sequenceId = header.sequenceId;
/* 129 */     this.checksum = buildChecksum(data);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\journal\impl\RecordFooter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */