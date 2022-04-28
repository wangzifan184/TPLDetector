/*     */ package org.codehaus.activemq.util;
/*     */ 
/*     */ import java.util.LinkedList;
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
/*     */ public class BitArrayBin
/*     */ {
/*     */   private LinkedList list;
/*     */   private int maxNumberOfArrays;
/*     */   private int currentIndex;
/*  31 */   private int firstIndex = -1;
/*  32 */   private int firstBin = -1;
/*     */ 
/*     */ 
/*     */   
/*     */   private int windowSize;
/*     */ 
/*     */ 
/*     */   
/*     */   public BitArrayBin(int windowSize) {
/*  41 */     this.windowSize = windowSize;
/*  42 */     this.maxNumberOfArrays = (windowSize + 1) / 64 + 1;
/*  43 */     this.maxNumberOfArrays = Math.max(this.maxNumberOfArrays, 1);
/*  44 */     this.list = new LinkedList();
/*  45 */     for (int i = 0; i < this.maxNumberOfArrays; i++) {
/*  46 */       this.list.add(new BitArray());
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
/*     */   public boolean setBit(long index, boolean value) {
/*  58 */     boolean answer = true;
/*  59 */     BitArray ba = getBitArray(index);
/*  60 */     if (ba != null) {
/*  61 */       int offset = getOffset(index);
/*  62 */       if (offset >= 0) {
/*  63 */         answer = ba.set(offset, value);
/*     */       }
/*     */     } 
/*  66 */     return answer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getBit(long index) {
/*  76 */     boolean answer = (index >= this.firstIndex);
/*  77 */     BitArray ba = getBitArray(index);
/*  78 */     if (ba != null) {
/*  79 */       int offset = getOffset(index);
/*  80 */       if (offset >= 0) {
/*  81 */         answer = ba.get(offset);
/*  82 */         return answer;
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/*  87 */       answer = true;
/*     */     } 
/*  89 */     return answer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BitArray getBitArray(long index) {
/*  99 */     int bin = getBin(index);
/* 100 */     BitArray answer = null;
/* 101 */     if (bin >= 0) {
/* 102 */       if (this.firstIndex < 0) {
/* 103 */         this.firstIndex = 0;
/*     */       }
/* 105 */       if (bin >= this.list.size()) {
/* 106 */         this.list.removeFirst();
/* 107 */         this.firstIndex += 64;
/* 108 */         this.list.add(new BitArray());
/* 109 */         bin = this.list.size() - 1;
/*     */       } 
/* 111 */       answer = this.list.get(bin);
/*     */     } 
/* 113 */     return answer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getBin(long index) {
/* 123 */     int answer = 0;
/* 124 */     if (this.firstBin < 0) {
/* 125 */       this.firstBin = 0;
/*     */     }
/* 127 */     else if (this.firstIndex >= 0) {
/* 128 */       answer = (int)((index - this.firstIndex) / 64L);
/*     */     } 
/* 130 */     return answer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getOffset(long index) {
/* 140 */     int answer = 0;
/* 141 */     if (this.firstIndex >= 0)
/*     */     {
/* 143 */       answer = (int)(index - this.firstIndex - (64 * getBin(index)));
/*     */     }
/* 145 */     return answer;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activem\\util\BitArrayBin.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */