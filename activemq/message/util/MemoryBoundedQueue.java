/*     */ package org.codehaus.activemq.message.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.service.QueueListEntry;
/*     */ import org.codehaus.activemq.service.impl.DefaultQueueList;
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
/*     */ public class MemoryBoundedQueue
/*     */   implements BoundedPacketQueue
/*     */ {
/*     */   private MemoryBoundedQueueManager queueManager;
/*     */   private String name;
/*     */   private boolean stopped = false;
/*     */   private boolean closed = false;
/*     */   private long memoryUsedByThisQueue;
/*  40 */   private Object outLock = new Object();
/*  41 */   private Object inLock = new Object();
/*  42 */   private DefaultQueueList internalList = new DefaultQueueList();
/*     */   private static final int WAIT_TIMEOUT = 100;
/*  44 */   private static final Log log = LogFactory.getLog(MemoryBoundedQueueManager.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   MemoryBoundedQueue(String name, MemoryBoundedQueueManager manager) {
/*  53 */     this.name = name;
/*  54 */     this.queueManager = manager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  61 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  68 */     return "" + this.name + " , cardinality = " + size() + " memory usage = " + this.memoryUsedByThisQueue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/*  75 */     return this.internalList.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLocalMemoryUsedByThisQueue() {
/*  82 */     return this.memoryUsedByThisQueue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/*     */     try {
/*  90 */       clear();
/*  91 */       this.closed = true;
/*  92 */       synchronized (this.outLock) {
/*  93 */         this.outLock.notifyAll();
/*     */       } 
/*  95 */       synchronized (this.inLock) {
/*  96 */         this.inLock.notifyAll();
/*     */       }
/*     */     
/*  99 */     } catch (Throwable e) {
/* 100 */       e.printStackTrace();
/*     */     } finally {
/*     */       
/* 103 */       this.queueManager.removeMemoryBoundedQueue(getName());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enqueueNoBlock(Packet packet) {
/* 113 */     if (!this.closed) {
/* 114 */       this.internalList.add(packet);
/* 115 */       incrementMemoryUsed(packet);
/* 116 */       synchronized (this.outLock) {
/* 117 */         this.outLock.notify();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enqueue(Packet packet) {
/* 128 */     if (!this.queueManager.isFull()) {
/* 129 */       enqueueNoBlock(packet);
/*     */     } else {
/*     */       
/* 132 */       synchronized (this.inLock) { while (true) {
/*     */           try {
/* 134 */             if (this.queueManager.isFull() && !this.closed) {
/* 135 */               this.inLock.wait(100L);
/*     */               continue;
/*     */             } 
/* 138 */           } catch (InterruptedException ie) {} break;
/*     */         }  }
/*     */       
/* 141 */       enqueueNoBlock(packet);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void enqueueFirstNoBlock(Packet packet) {
/* 151 */     if (!this.closed) {
/* 152 */       this.internalList.addFirst(packet);
/* 153 */       incrementMemoryUsed(packet);
/* 154 */       synchronized (this.outLock) {
/* 155 */         this.outLock.notify();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enqueueFirst(Packet packet) throws InterruptedException {
/* 167 */     if (!this.queueManager.isFull()) {
/* 168 */       enqueueFirstNoBlock(packet);
/*     */     } else {
/*     */       
/* 171 */       synchronized (this.inLock) {
/* 172 */         while (this.queueManager.isFull() && !this.closed) {
/* 173 */           this.inLock.wait(100L);
/*     */         }
/*     */       } 
/* 176 */       enqueueFirstNoBlock(packet);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Packet dequeue() throws InterruptedException {
/* 185 */     Packet result = null;
/* 186 */     synchronized (this.outLock) {
/* 187 */       while (this.internalList.isEmpty() && !this.closed) {
/* 188 */         this.outLock.wait(100L);
/*     */       }
/* 190 */       result = dequeueNoWait();
/*     */     } 
/* 192 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Packet dequeue(long timeInMillis) throws InterruptedException {
/* 203 */     Packet result = null;
/* 204 */     if (timeInMillis == 0L) {
/* 205 */       result = dequeue();
/*     */     } else {
/*     */       
/* 208 */       synchronized (this.outLock) {
/*     */         
/* 210 */         long waitTime = timeInMillis;
/* 211 */         long start = (timeInMillis <= 0L) ? 0L : System.currentTimeMillis();
/* 212 */         while (!this.closed) {
/* 213 */           result = dequeueNoWait();
/* 214 */           if (result != null || waitTime <= 0L) {
/*     */             break;
/*     */           }
/*     */           
/* 218 */           this.outLock.wait(waitTime);
/* 219 */           waitTime = timeInMillis - System.currentTimeMillis() - start;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 224 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Packet dequeueNoWait() throws InterruptedException {
/* 234 */     Packet packet = null;
/* 235 */     if (this.stopped) {
/* 236 */       synchronized (this.outLock) {
/* 237 */         while (this.stopped && !this.closed) {
/* 238 */           this.outLock.wait(100L);
/*     */         }
/*     */       } 
/*     */     }
/* 242 */     packet = (Packet)this.internalList.removeFirst();
/* 243 */     decrementMemoryUsed(packet);
/* 244 */     if (packet != null) {
/* 245 */       synchronized (this.inLock) {
/* 246 */         this.inLock.notify();
/*     */       } 
/*     */     }
/* 249 */     return packet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStarted() {
/* 256 */     return !this.stopped;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 263 */     synchronized (this.outLock) {
/* 264 */       this.stopped = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/* 272 */     this.stopped = false;
/* 273 */     synchronized (this.outLock) {
/* 274 */       this.outLock.notifyAll();
/*     */     } 
/* 276 */     synchronized (this.inLock) {
/* 277 */       this.inLock.notifyAll();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean remove(Packet packet) {
/* 288 */     boolean result = false;
/* 289 */     if (!this.internalList.isEmpty()) {
/* 290 */       result = this.internalList.remove(packet);
/*     */     }
/* 292 */     if (result) {
/* 293 */       decrementMemoryUsed(packet);
/*     */     }
/* 295 */     synchronized (this.inLock) {
/* 296 */       this.inLock.notify();
/*     */     } 
/* 298 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Packet remove(String id) {
/* 308 */     Packet result = null;
/* 309 */     QueueListEntry entry = this.internalList.getFirstEntry();
/*     */     try {
/* 311 */       while (entry != null) {
/* 312 */         Packet p = (Packet)entry.getElement();
/* 313 */         if (p.getId().equals(id)) {
/* 314 */           result = p;
/* 315 */           this.internalList.remove(entry);
/*     */           break;
/*     */         } 
/* 318 */         entry = this.internalList.getNextEntry(entry);
/*     */       }
/*     */     
/* 321 */     } catch (JMSException jmsEx) {
/* 322 */       jmsEx.printStackTrace();
/*     */     } 
/* 324 */     synchronized (this.inLock) {
/* 325 */       this.inLock.notify();
/*     */     } 
/* 327 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 334 */     while (!this.internalList.isEmpty()) {
/* 335 */       Packet packet = (Packet)this.internalList.removeFirst();
/* 336 */       decrementMemoryUsed(packet);
/*     */     } 
/* 338 */     synchronized (this.inLock) {
/* 339 */       this.inLock.notifyAll();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 347 */     return this.internalList.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Packet get(int index) {
/* 357 */     return (Packet)this.internalList.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getContents() {
/* 366 */     Object[] array = this.internalList.toArray();
/* 367 */     List list = new ArrayList();
/* 368 */     for (int i = 0; i < array.length; i++) {
/* 369 */       list.add(array[i]);
/*     */     }
/* 371 */     return list;
/*     */   }
/*     */   
/*     */   private synchronized void incrementMemoryUsed(Packet packet) {
/* 375 */     if (packet != null) {
/* 376 */       this.memoryUsedByThisQueue += this.queueManager.incrementMemoryUsed(packet);
/*     */     }
/*     */   }
/*     */   
/*     */   private synchronized void decrementMemoryUsed(Packet packet) {
/* 381 */     if (packet != null)
/* 382 */       this.memoryUsedByThisQueue -= this.queueManager.decrementMemoryUsed(packet); 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\messag\\util\MemoryBoundedQueue.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */