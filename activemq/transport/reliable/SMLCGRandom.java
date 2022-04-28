/*     */ package org.codehaus.activemq.transport.reliable;
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
/*     */ public class SMLCGRandom
/*     */ {
/*     */   private static final long MULTIPLIER_1 = 40014L;
/*     */   private static final long MOD_1 = 2147483563L;
/*     */   private static final long MULTIPLIER_2 = 40692L;
/*     */   private static final long MOD_2 = 2147483399L;
/*     */   private static final int SHUFFLE_LEN = 32;
/*     */   private static final int WARMUP_LENGTH = 19;
/*     */   private int generated_1;
/*     */   private int generated_2;
/*     */   private int state;
/*     */   private int[] shuffle;
/*     */   
/*     */   public SMLCGRandom() {
/*  42 */     this(System.currentTimeMillis());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SMLCGRandom(long seed) {
/*  51 */     this.shuffle = new int[32];
/*  52 */     setSeed(seed);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSeed(long seed) throws IllegalArgumentException {
/*  62 */     this.generated_1 = this.generated_2 = (int)(seed & 0x7FFFFFFFFL); int i;
/*  63 */     for (i = 0; i < 19; i++) {
/*  64 */       this.generated_1 = (int)(this.generated_1 * 40014L % 2147483563L);
/*     */     }
/*  66 */     for (i = 0; i < 32; i++) {
/*  67 */       this.generated_1 = (int)(this.generated_1 * 40014L % 2147483563L);
/*  68 */       this.shuffle[31 - i] = this.generated_1;
/*     */     } 
/*  70 */     this.state = this.shuffle[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short nextShort() {
/*  77 */     return (short)((short)nextByte() << 8 | (short)(nextByte() & 0xFF));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int nextInt() {
/*  84 */     return nextShort() << 16 | nextShort() & 0xFFFF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long nextLong() {
/*  91 */     return nextInt() << 32L | nextInt() & 0xFFFFFFFFL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float nextFloat() {
/*  99 */     return (float)((nextInt() & Integer.MAX_VALUE) / 2.147483647E9D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double nextDouble() {
/* 107 */     return (nextLong() & Long.MAX_VALUE) / 9.223372036854776E18D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte nextByte() {
/* 114 */     int i = 0;
/* 115 */     this.generated_1 = (int)(this.generated_1 * 40014L % 2147483563L);
/* 116 */     this.generated_2 = (int)(this.generated_2 * 40692L % 2147483399L);
/* 117 */     i = this.state / 67108862;
/* 118 */     i = Math.abs(i);
/* 119 */     this.state = (int)((this.shuffle[i] + this.generated_2) % 2147483563L);
/* 120 */     this.shuffle[i] = this.generated_1;
/* 121 */     return (byte)(this.state / 8388608);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\reliable\SMLCGRandom.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */