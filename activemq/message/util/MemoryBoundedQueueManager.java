/*     */ package org.codehaus.activemq.message.util;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;
/*     */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedLong;
/*     */ import java.util.Iterator;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.capacity.BasicCapacityMonitor;
/*     */ import org.codehaus.activemq.message.Packet;
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
/*     */ public class MemoryBoundedQueueManager
/*     */   extends BasicCapacityMonitor
/*     */ {
/*     */   private static final int OBJECT_OVERHEAD = 50;
/*  36 */   SynchronizedLong totalMemoryUsedSize = new SynchronizedLong(0L);
/*  37 */   private ConcurrentHashMap activeQueues = new ConcurrentHashMap();
/*  38 */   private static final Log log = LogFactory.getLog(MemoryBoundedQueueManager.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemoryBoundedQueueManager(String name, long maxSize) {
/*  45 */     super(name, maxSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemoryBoundedQueue getMemoryBoundedQueue(String name) {
/*  55 */     MemoryBoundedQueue result = (MemoryBoundedQueue)this.activeQueues.get(name);
/*  56 */     if (result == null) {
/*  57 */       result = new MemoryBoundedQueue(name, this);
/*  58 */       this.activeQueues.put(name, result);
/*     */     } 
/*  60 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/*  67 */     for (Iterator i = this.activeQueues.values().iterator(); i.hasNext(); ) {
/*  68 */       MemoryBoundedQueue mbq = i.next();
/*  69 */       mbq.close();
/*     */     } 
/*  71 */     this.activeQueues.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTotalMemoryUsedSize() {
/*  78 */     return this.totalMemoryUsedSize.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFull() {
/*  85 */     boolean result = (this.totalMemoryUsedSize.get() >= getValueLimit());
/*  86 */     return result;
/*     */   }
/*     */   
/*     */   int incrementMemoryUsed(Packet obj) {
/*  90 */     int size = 50;
/*  91 */     if (obj != null) {
/*  92 */       if (obj.getMemoryUsageReferenceCount() == 0) {
/*  93 */         size += obj.getMemoryUsage();
/*     */       }
/*  95 */       obj.incrementMemoryReferenceCount();
/*     */     } 
/*  97 */     this.totalMemoryUsedSize.add(size);
/*  98 */     setCurrentValue(this.totalMemoryUsedSize.get());
/*  99 */     return size;
/*     */   }
/*     */   
/*     */   int decrementMemoryUsed(Packet obj) {
/* 103 */     int size = 50;
/* 104 */     if (obj != null) {
/* 105 */       obj.decrementMemoryReferenceCount();
/* 106 */       if (obj.getMemoryUsageReferenceCount() == 0) {
/* 107 */         size += obj.getMemoryUsage();
/*     */       }
/*     */     } 
/* 110 */     this.totalMemoryUsedSize.subtract(size);
/* 111 */     setCurrentValue(this.totalMemoryUsedSize.get());
/* 112 */     return size;
/*     */   }
/*     */   
/*     */   protected void finalize() {
/* 116 */     close();
/*     */   }
/*     */   
/*     */   void removeMemoryBoundedQueue(String name) {
/* 120 */     this.activeQueues.remove(name);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\messag\\util\MemoryBoundedQueueManager.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */