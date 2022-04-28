/*    */ package org.codehaus.activemq.ra;
/*    */ 
/*    */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CircularQueue
/*    */ {
/*    */   private final int size;
/*    */   private final SynchronizedBoolean stopping;
/*    */   private final Object[] contents;
/* 16 */   private final Object mutex = new Object();
/*    */   
/* 18 */   private int start = 0;
/*    */   
/* 20 */   private int end = 0;
/*    */   
/*    */   public CircularQueue(int size, SynchronizedBoolean stopping) {
/* 23 */     this.size = size;
/* 24 */     this.contents = new Object[size];
/* 25 */     this.stopping = stopping;
/*    */   }
/*    */   
/*    */   public Object get() {
/* 29 */     synchronized (this.mutex) {
/*    */       while (true) {
/* 31 */         Object ew = this.contents[this.start];
/* 32 */         if (ew != null) {
/* 33 */           this.start++;
/* 34 */           if (this.start == this.contents.length) {
/* 35 */             this.start = 0;
/*    */           }
/* 37 */           return ew;
/*    */         } 
/*    */         try {
/* 40 */           this.mutex.wait();
/* 41 */           if (this.stopping.get()) {
/* 42 */             return null;
/*    */           }
/* 44 */         } catch (InterruptedException e) {
/* 45 */           return null;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void returnObject(Object worker) {
/* 53 */     synchronized (this.mutex) {
/* 54 */       this.contents[this.end++] = worker;
/* 55 */       if (this.end == this.contents.length) {
/* 56 */         this.end = 0;
/*    */       }
/* 58 */       this.mutex.notify();
/*    */     } 
/*    */   }
/*    */   
/*    */   public int size() {
/* 63 */     return this.contents.length;
/*    */   }
/*    */   
/*    */   public void drain() {
/* 67 */     int i = 0;
/* 68 */     while (i < this.size) {
/* 69 */       if (get() != null) {
/* 70 */         i++;
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void notifyWaiting() {
/* 77 */     synchronized (this.mutex) {
/* 78 */       this.mutex.notifyAll();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ra\CircularQueue.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */