/*     */ package org.codehaus.activemq.message.util;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
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
/*     */ public class WireByteArrayInputStream
/*     */   extends ByteArrayInputStream
/*     */ {
/*     */   public WireByteArrayInputStream(byte[] buf) {
/*  34 */     super(buf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WireByteArrayInputStream(byte[] buf, int offset, int length) {
/*  45 */     super(buf, offset, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WireByteArrayInputStream() {
/*  52 */     super(new byte[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void restart(byte[] newBuff, int offset, int length) {
/*  63 */     this.buf = newBuff;
/*  64 */     this.pos = offset;
/*  65 */     this.count = Math.min(offset + length, newBuff.length);
/*  66 */     this.mark = offset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void restart(byte[] newBuff) {
/*  75 */     restart(newBuff, 0, newBuff.length);
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
/*     */   public int read() {
/*  88 */     return (this.pos < this.count) ? (this.buf[this.pos++] & 0xFF) : -1;
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
/*     */   public int read(byte[] b, int off, int len) {
/* 101 */     if (b == null) {
/* 102 */       throw new NullPointerException();
/*     */     }
/* 104 */     if (off < 0 || off > b.length || len < 0 || off + len > b.length || off + len < 0) {
/* 105 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 107 */     if (this.pos >= this.count) {
/* 108 */       return -1;
/*     */     }
/* 110 */     if (this.pos + len > this.count) {
/* 111 */       len = this.count - this.pos;
/*     */     }
/* 113 */     if (len <= 0) {
/* 114 */       return 0;
/*     */     }
/* 116 */     System.arraycopy(this.buf, this.pos, b, off, len);
/* 117 */     this.pos += len;
/* 118 */     return len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int available() {
/* 125 */     return this.count - this.pos;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\messag\\util\WireByteArrayInputStream.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */