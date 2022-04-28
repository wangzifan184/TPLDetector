/*     */ package org.codehaus.activemq.util;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
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
/*     */ public class BitArray
/*     */ {
/*     */   static final int LONG_SIZE = 64;
/*     */   static final int INT_SIZE = 32;
/*     */   static final int SHORT_SIZE = 16;
/*     */   static final int BYTE_SIZE = 8;
/*  36 */   private static final long[] BIT_VALUES = new long[] { 1L, 2L, 4L, 8L, 16L, 32L, 64L, 128L, 256L, 512L, 1024L, 2048L, 4096L, 8192L, 16384L, 32768L, 65536L, 131072L, 262144L, 524288L, 1048576L, 2097152L, 4194304L, 8388608L, 16777216L, 33554432L, 67108864L, 134217728L, 268435456L, 536870912L, 1073741824L, 2147483648L, 4294967296L, 8589934592L, 17179869184L, 34359738368L, 68719476736L, 137438953472L, 274877906944L, 549755813888L, 1099511627776L, 2199023255552L, 4398046511104L, 8796093022208L, 17592186044416L, 35184372088832L, 70368744177664L, 140737488355328L, 281474976710656L, 562949953421312L, 1125899906842624L, 2251799813685248L, 4503599627370496L, 9007199254740992L, 18014398509481984L, 36028797018963968L, 72057594037927936L, 144115188075855872L, 288230376151711744L, 576460752303423488L, 1152921504606846976L, 2305843009213693952L, 4611686018427387904L, Long.MIN_VALUE };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long bits;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int length;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/*  57 */     return this.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getBits() {
/*  64 */     return this.bits;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean set(int index, boolean flag) {
/*  75 */     this.length = Math.max(this.length, index + 1);
/*  76 */     boolean oldValue = ((this.bits & BIT_VALUES[index]) != 0L);
/*  77 */     if (flag) {
/*  78 */       this.bits |= BIT_VALUES[index];
/*     */     }
/*  80 */     else if (oldValue) {
/*  81 */       this.bits &= BIT_VALUES[index] ^ 0xFFFFFFFFFFFFFFFFL;
/*     */     } 
/*  83 */     return oldValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean get(int index) {
/*  91 */     return ((this.bits & BIT_VALUES[index]) != 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/*  98 */     this.bits = 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeToStream(DataOutput dataOut) throws IOException {
/* 108 */     dataOut.writeByte(this.length);
/* 109 */     if (this.length <= 8) {
/* 110 */       dataOut.writeByte((int)this.bits);
/*     */     }
/* 112 */     else if (this.length <= 16) {
/* 113 */       dataOut.writeShort((short)(int)this.bits);
/*     */     }
/* 115 */     else if (this.length <= 32) {
/* 116 */       dataOut.writeInt((int)this.bits);
/*     */     } else {
/*     */       
/* 119 */       dataOut.writeLong(this.bits);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void readFromStream(DataInput dataIn) throws IOException {
/* 130 */     this.length = dataIn.readByte();
/* 131 */     if (this.length <= 8) {
/* 132 */       this.bits = dataIn.readByte();
/*     */     }
/* 134 */     else if (this.length <= 16) {
/* 135 */       this.bits = dataIn.readShort();
/*     */     }
/* 137 */     else if (this.length <= 32) {
/* 138 */       this.bits = dataIn.readInt();
/*     */     } else {
/*     */       
/* 141 */       this.bits = dataIn.readLong();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activem\\util\BitArray.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */