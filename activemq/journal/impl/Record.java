/*    */ package org.codehaus.activemq.journal.impl;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Record
/*    */ {
/*    */   public static final int RECORD_BASE_SIZE = 35;
/* 32 */   final RecordHeader header = new RecordHeader();
/* 33 */   final RecordFooter footer = new RecordFooter();
/*    */   final ByteBuffer headerBuffer;
/*    */   final ByteBuffer dataBuffer;
/*    */   final ByteBuffer footerBuffer;
/*    */   final Mark mark;
/*    */   
/*    */   public Record(RecordHeader header, byte[] data) {
/* 40 */     this(header.sequenceId, header.recordType, data, null);
/*    */   }
/*    */   
/*    */   public Record(long sequenceId, byte recordType, byte[] data, Mark visitData) {
/* 44 */     this.header.bulkSet(recordType, sequenceId, data);
/* 45 */     this.headerBuffer = this.header.toByteBuffer();
/* 46 */     this.dataBuffer = ByteBuffer.wrap(data);
/* 47 */     this.footer.bulkSet(this.header, data);
/* 48 */     this.footerBuffer = this.footer.toByteBuffer();
/* 49 */     this.mark = visitData;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecordHeader getHeader() {
/* 54 */     return this.header;
/*    */   }
/*    */   
/*    */   public Mark getMark() {
/* 58 */     return this.mark;
/*    */   }
/*    */   
/*    */   public int remaining() {
/* 62 */     return this.headerBuffer.remaining() + this.dataBuffer.remaining();
/*    */   }
/*    */   
/*    */   public void fill(ByteBuffer byteBuffer) {
/* 66 */     fill(byteBuffer, this.headerBuffer);
/* 67 */     fill(byteBuffer, this.dataBuffer);
/* 68 */     fill(byteBuffer, this.footerBuffer);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void fill(ByteBuffer dest, ByteBuffer src) {
/* 81 */     int r = dest.remaining();
/* 82 */     if (r == 0) {
/*    */       return;
/*    */     }
/* 85 */     if (r < src.remaining()) {
/*    */       
/* 87 */       int limit = src.limit();
/* 88 */       src.limit(src.position() + r);
/* 89 */       dest.put(src);
/*    */       
/* 91 */       src.limit(limit);
/*    */     } else {
/* 93 */       dest.put(src);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\journal\impl\Record.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */