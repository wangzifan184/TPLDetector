/*    */ package org.codehaus.activemq.journal.impl;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
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
/*    */ class Mark
/*    */ {
/*    */   public static final int MARK_RECORD_SIZE = 12;
/*    */   public long sequenceId;
/*    */   public int offsetId;
/*    */   
/*    */   public Mark() {}
/*    */   
/*    */   public Mark(RecordLocationImpl location) {
/* 41 */     this.sequenceId = location.getSequenceId();
/* 42 */     this.offsetId = location.getSegmentOffset();
/*    */   }
/*    */   
/*    */   public byte[] writeExternal() throws IOException {
/* 46 */     ByteArrayOutputStream stream = new ByteArrayOutputStream(16);
/* 47 */     DataOutputStream os = new DataOutputStream(stream);
/* 48 */     os.writeLong(this.sequenceId);
/* 49 */     os.writeInt(this.offsetId);
/* 50 */     os.close();
/* 51 */     return stream.toByteArray();
/*    */   }
/*    */   
/*    */   public void readExternal(byte[] data) throws IOException {
/* 55 */     DataInputStream is = new DataInputStream(new ByteArrayInputStream(data));
/* 56 */     this.sequenceId = is.readLong();
/* 57 */     this.offsetId = is.readInt();
/* 58 */     is.close();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void copy(Mark mark) {
/* 64 */     this.offsetId = mark.offsetId;
/* 65 */     this.sequenceId = mark.sequenceId;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\journal\impl\Mark.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */