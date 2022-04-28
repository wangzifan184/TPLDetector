/*    */ package org.codehaus.activemq.journal.impl;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.ArrayList;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
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
/*    */ public class ByteBufferPool
/*    */ {
/* 32 */   private static final Log log = LogFactory.getLog(ByteBufferPool.class);
/*    */   
/* 34 */   private final ArrayList pool = new ArrayList();
/*    */ 
/*    */   
/*    */   private final int bufferSize;
/*    */   
/*    */   private final int maxBuffers;
/*    */ 
/*    */   
/*    */   public ByteBufferPool() {
/* 43 */     this(4, 4194304);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ByteBufferPool(int bufferCount, int bufferSize) {
/* 54 */     this.maxBuffers = bufferCount;
/* 55 */     this.bufferSize = bufferSize;
/*    */     
/* 57 */     for (int i = 0; i < bufferCount; i++) {
/* 58 */       ByteBuffer bb = ByteBuffer.allocateDirect(bufferSize);
/* 59 */       this.pool.add(bb);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ByteBuffer getByteBuffer() throws InterruptedException {
/* 70 */     ByteBuffer answer = null;
/* 71 */     synchronized (this) {
/* 72 */       while (answer == null) {
/* 73 */         if (this.pool.size() > 0) {
/* 74 */           answer = this.pool.remove(this.pool.size() - 1);
/*    */         }
/* 76 */         if (answer == null) {
/* 77 */           log.warn("Performance Warning: Buffer pool ran out of buffers.  Waiting for buffer to be returned.  System may be uner heavy load or you may need to configure more buffers in the pool.");
/* 78 */           wait();
/*    */         } 
/*    */       } 
/*    */     } 
/* 82 */     return answer;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void returnByteBuffer(ByteBuffer buffer) {
/* 91 */     buffer.clear();
/* 92 */     synchronized (this) {
/* 93 */       this.pool.add(buffer);
/* 94 */       notify();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\journal\impl\ByteBufferPool.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */