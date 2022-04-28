/*    */ package org.codehaus.activemq.journal.impl;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
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
/*    */ public class RecordHeader
/*    */ {
/*    */   public static final int RECORD_HEADER_SIZE = 16;
/* 32 */   public static final byte[] START_OF_RECORD = new byte[] { 83, 111, 82 };
/*    */   
/*    */   public int length;
/*    */   
/*    */   public long sequenceId;
/*    */   
/*    */   public byte recordType;
/*    */ 
/*    */   
/*    */   public ByteBuffer toByteBuffer() {
/* 42 */     ByteBuffer buff = ByteBuffer.allocate(16);
/* 43 */     buff.put(START_OF_RECORD).putInt(this.length).putLong(this.sequenceId).put(this.recordType).flip();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 48 */     return buff;
/*    */   }
/*    */   
/*    */   void writeRecordHeader(DataOutput out) throws IOException {
/* 52 */     out.write(START_OF_RECORD);
/* 53 */     out.writeInt(this.length);
/* 54 */     out.writeLong(this.sequenceId);
/* 55 */     out.writeByte(this.recordType);
/*    */   }
/*    */   
/*    */   void readRecordHeader(DataInput in) throws IOException {
/* 59 */     for (int i = 0; i < START_OF_RECORD.length; i++) {
/* 60 */       byte checkByte = START_OF_RECORD[i];
/* 61 */       if (in.readByte() != checkByte) {
/* 62 */         throw new IOException("Not a valid record header.");
/*    */       }
/*    */     } 
/* 65 */     this.length = in.readInt();
/* 66 */     this.sequenceId = in.readLong();
/* 67 */     this.recordType = in.readByte();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void invalidate() {
/* 74 */     this.length = -1;
/* 75 */     this.sequenceId = -1L;
/*    */   }
/*    */   
/*    */   public boolean isValid() {
/* 79 */     return (this.length >= 0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void bulkSet(byte recordType, long sequenceId, byte[] data) {
/* 88 */     this.length = data.length;
/* 89 */     this.sequenceId = sequenceId;
/* 90 */     this.recordType = recordType;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\journal\impl\RecordHeader.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */