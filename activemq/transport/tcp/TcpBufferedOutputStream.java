/*     */ package org.codehaus.activemq.transport.tcp;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
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
/*     */ public class TcpBufferedOutputStream
/*     */   extends FilterOutputStream
/*     */ {
/*     */   private static final int BUFFER_SIZE = 4096;
/*     */   private byte[] buf;
/*     */   private int count;
/*     */   
/*     */   public TcpBufferedOutputStream(OutputStream out) {
/*  43 */     this(out, 4096);
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
/*     */   public TcpBufferedOutputStream(OutputStream out, int size) {
/*  55 */     super(out);
/*  56 */     if (size <= 0) {
/*  57 */       throw new IllegalArgumentException("Buffer size <= 0");
/*     */     }
/*  59 */     this.buf = new byte[size];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/*  69 */     checkClosed();
/*  70 */     if (availableBufferToWrite() < 1) {
/*  71 */       flush();
/*     */     }
/*  73 */     this.buf[this.count++] = (byte)b;
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
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/*  86 */     checkClosed();
/*  87 */     if (availableBufferToWrite() < len) {
/*  88 */       flush();
/*     */     }
/*  90 */     if (this.buf.length >= len) {
/*  91 */       System.arraycopy(b, off, this.buf, this.count, len);
/*  92 */       this.count += len;
/*     */     } else {
/*     */       
/*  95 */       this.out.write(b, off, len);
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
/*     */   public void flush() throws IOException {
/* 107 */     if (this.count > 0 && this.out != null) {
/* 108 */       this.out.write(this.buf, 0, this.count);
/* 109 */       this.count = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 119 */     super.close();
/* 120 */     this.out = null;
/* 121 */     this.buf = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkClosed() throws IOException {
/* 131 */     if (this.buf == null || this.out == null) {
/* 132 */       throw new EOFException("Cannot write to the stream any more it has already been closed");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int availableBufferToWrite() {
/* 140 */     return this.buf.length - this.count;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\tcp\TcpBufferedOutputStream.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */