/*     */ package org.codehaus.activemq.message.util;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
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
/*     */ public class WireByteArrayOutputStream
/*     */   extends ByteArrayOutputStream
/*     */ {
/*     */   public WireByteArrayOutputStream() {
/*  33 */     super(32);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WireByteArrayOutputStream(int size) {
/*  43 */     super(size);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void restart(int size) {
/*  52 */     this.buf = new byte[size];
/*  53 */     this.count = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void restart() {
/*  60 */     restart(32);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int b) {
/*  69 */     int newcount = this.count + 1;
/*  70 */     if (newcount > this.buf.length) {
/*  71 */       byte[] newbuf = new byte[Math.max(this.buf.length << 1, newcount)];
/*  72 */       System.arraycopy(this.buf, 0, newbuf, 0, this.count);
/*  73 */       this.buf = newbuf;
/*     */     } 
/*  75 */     this.buf[this.count] = (byte)b;
/*  76 */     this.count = newcount;
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
/*     */   public void write(byte[] b, int off, int len) {
/*  88 */     if (off < 0 || off > b.length || len < 0 || off + len > b.length || off + len < 0) {
/*  89 */       throw new IndexOutOfBoundsException();
/*     */     }
/*  91 */     if (len == 0) {
/*     */       return;
/*     */     }
/*  94 */     int newcount = this.count + len;
/*  95 */     if (newcount > this.buf.length) {
/*  96 */       byte[] newbuf = new byte[Math.max(this.buf.length << 1, newcount)];
/*  97 */       System.arraycopy(this.buf, 0, newbuf, 0, this.count);
/*  98 */       this.buf = newbuf;
/*     */     } 
/* 100 */     System.arraycopy(b, off, this.buf, this.count, len);
/* 101 */     this.count = newcount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getData() {
/* 108 */     return this.buf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 115 */     this.count = 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\messag\\util\WireByteArrayOutputStream.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */