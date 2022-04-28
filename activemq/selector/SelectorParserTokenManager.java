/*     */ package org.codehaus.activemq.selector;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ 
/*     */ public class SelectorParserTokenManager
/*     */   implements SelectorParserConstants
/*     */ {
/*  10 */   public PrintStream debugStream = System.out; public void setDebugStream(PrintStream ds) {
/*  11 */     this.debugStream = ds;
/*     */   }
/*     */   private final int jjStopAtPos(int pos, int kind) {
/*  14 */     this.jjmatchedKind = kind;
/*  15 */     this.jjmatchedPos = pos;
/*  16 */     return pos + 1;
/*     */   }
/*     */   
/*     */   private final int jjMoveStringLiteralDfa0_0() {
/*  20 */     switch (this.curChar) {
/*     */       
/*     */       case '\t':
/*  23 */         this.jjmatchedKind = 2;
/*  24 */         return jjMoveNfa_0(5, 0);
/*     */       case '\n':
/*  26 */         this.jjmatchedKind = 3;
/*  27 */         return jjMoveNfa_0(5, 0);
/*     */       case '\f':
/*  29 */         this.jjmatchedKind = 5;
/*  30 */         return jjMoveNfa_0(5, 0);
/*     */       case '\r':
/*  32 */         this.jjmatchedKind = 4;
/*  33 */         return jjMoveNfa_0(5, 0);
/*     */       case ' ':
/*  35 */         this.jjmatchedKind = 1;
/*  36 */         return jjMoveNfa_0(5, 0);
/*     */       case '%':
/*  38 */         this.jjmatchedKind = 37;
/*  39 */         return jjMoveNfa_0(5, 0);
/*     */       case '(':
/*  41 */         this.jjmatchedKind = 30;
/*  42 */         return jjMoveNfa_0(5, 0);
/*     */       case ')':
/*  44 */         this.jjmatchedKind = 32;
/*  45 */         return jjMoveNfa_0(5, 0);
/*     */       case '*':
/*  47 */         this.jjmatchedKind = 35;
/*  48 */         return jjMoveNfa_0(5, 0);
/*     */       case '+':
/*  50 */         this.jjmatchedKind = 33;
/*  51 */         return jjMoveNfa_0(5, 0);
/*     */       case ',':
/*  53 */         this.jjmatchedKind = 31;
/*  54 */         return jjMoveNfa_0(5, 0);
/*     */       case '-':
/*  56 */         this.jjmatchedKind = 34;
/*  57 */         return jjMoveNfa_0(5, 0);
/*     */       case '/':
/*  59 */         this.jjmatchedKind = 36;
/*  60 */         return jjMoveNfa_0(5, 0);
/*     */       case '<':
/*  62 */         this.jjmatchedKind = 28;
/*  63 */         return jjMoveStringLiteralDfa1_0(570425344L);
/*     */       case '=':
/*  65 */         this.jjmatchedKind = 24;
/*  66 */         return jjMoveNfa_0(5, 0);
/*     */       case '>':
/*  68 */         this.jjmatchedKind = 26;
/*  69 */         return jjMoveStringLiteralDfa1_0(134217728L);
/*     */       case 'A':
/*  71 */         return jjMoveStringLiteralDfa1_0(512L);
/*     */       case 'B':
/*  73 */         return jjMoveStringLiteralDfa1_0(2048L);
/*     */       case 'E':
/*  75 */         return jjMoveStringLiteralDfa1_0(8192L);
/*     */       case 'F':
/*  77 */         return jjMoveStringLiteralDfa1_0(131072L);
/*     */       case 'I':
/*  79 */         return jjMoveStringLiteralDfa1_0(49152L);
/*     */       case 'L':
/*  81 */         return jjMoveStringLiteralDfa1_0(4096L);
/*     */       case 'N':
/*  83 */         return jjMoveStringLiteralDfa1_0(262400L);
/*     */       case 'O':
/*  85 */         return jjMoveStringLiteralDfa1_0(1024L);
/*     */       case 'T':
/*  87 */         return jjMoveStringLiteralDfa1_0(65536L);
/*     */       case 'a':
/*  89 */         return jjMoveStringLiteralDfa1_0(512L);
/*     */       case 'b':
/*  91 */         return jjMoveStringLiteralDfa1_0(2048L);
/*     */       case 'e':
/*  93 */         return jjMoveStringLiteralDfa1_0(8192L);
/*     */       case 'f':
/*  95 */         return jjMoveStringLiteralDfa1_0(131072L);
/*     */       case 'i':
/*  97 */         return jjMoveStringLiteralDfa1_0(49152L);
/*     */       case 'l':
/*  99 */         return jjMoveStringLiteralDfa1_0(4096L);
/*     */       case 'n':
/* 101 */         return jjMoveStringLiteralDfa1_0(262400L);
/*     */       case 'o':
/* 103 */         return jjMoveStringLiteralDfa1_0(1024L);
/*     */       case 't':
/* 105 */         return jjMoveStringLiteralDfa1_0(65536L);
/*     */     } 
/* 107 */     return jjMoveNfa_0(5, 0);
/*     */   }
/*     */   
/*     */   private final int jjMoveStringLiteralDfa1_0(long active0) {
/*     */     try {
/* 112 */       this.curChar = this.input_stream.readChar();
/* 113 */     } catch (IOException e) {
/* 114 */       return jjMoveNfa_0(5, 0);
/*     */     } 
/* 116 */     switch (this.curChar) {
/*     */       
/*     */       case '=':
/* 119 */         if ((active0 & 0x8000000L) != 0L) {
/*     */           
/* 121 */           this.jjmatchedKind = 27;
/* 122 */           this.jjmatchedPos = 1; break;
/*     */         } 
/* 124 */         if ((active0 & 0x20000000L) != 0L) {
/*     */           
/* 126 */           this.jjmatchedKind = 29;
/* 127 */           this.jjmatchedPos = 1;
/*     */         } 
/*     */         break;
/*     */       case '>':
/* 131 */         if ((active0 & 0x2000000L) != 0L) {
/*     */           
/* 133 */           this.jjmatchedKind = 25;
/* 134 */           this.jjmatchedPos = 1;
/*     */         } 
/*     */         break;
/*     */       case 'A':
/* 138 */         return jjMoveStringLiteralDfa2_0(active0, 131072L);
/*     */       case 'E':
/* 140 */         return jjMoveStringLiteralDfa2_0(active0, 2048L);
/*     */       case 'I':
/* 142 */         return jjMoveStringLiteralDfa2_0(active0, 4096L);
/*     */       case 'N':
/* 144 */         if ((active0 & 0x4000L) != 0L) {
/*     */           
/* 146 */           this.jjmatchedKind = 14;
/* 147 */           this.jjmatchedPos = 1;
/*     */         } 
/* 149 */         return jjMoveStringLiteralDfa2_0(active0, 512L);
/*     */       case 'O':
/* 151 */         return jjMoveStringLiteralDfa2_0(active0, 256L);
/*     */       case 'R':
/* 153 */         if ((active0 & 0x400L) != 0L) {
/*     */           
/* 155 */           this.jjmatchedKind = 10;
/* 156 */           this.jjmatchedPos = 1;
/*     */         } 
/* 158 */         return jjMoveStringLiteralDfa2_0(active0, 65536L);
/*     */       case 'S':
/* 160 */         if ((active0 & 0x8000L) != 0L) {
/*     */           
/* 162 */           this.jjmatchedKind = 15;
/* 163 */           this.jjmatchedPos = 1;
/*     */         } 
/* 165 */         return jjMoveStringLiteralDfa2_0(active0, 8192L);
/*     */       case 'U':
/* 167 */         return jjMoveStringLiteralDfa2_0(active0, 262144L);
/*     */       case 'a':
/* 169 */         return jjMoveStringLiteralDfa2_0(active0, 131072L);
/*     */       case 'e':
/* 171 */         return jjMoveStringLiteralDfa2_0(active0, 2048L);
/*     */       case 'i':
/* 173 */         return jjMoveStringLiteralDfa2_0(active0, 4096L);
/*     */       case 'n':
/* 175 */         if ((active0 & 0x4000L) != 0L) {
/*     */           
/* 177 */           this.jjmatchedKind = 14;
/* 178 */           this.jjmatchedPos = 1;
/*     */         } 
/* 180 */         return jjMoveStringLiteralDfa2_0(active0, 512L);
/*     */       case 'o':
/* 182 */         return jjMoveStringLiteralDfa2_0(active0, 256L);
/*     */       case 'r':
/* 184 */         if ((active0 & 0x400L) != 0L) {
/*     */           
/* 186 */           this.jjmatchedKind = 10;
/* 187 */           this.jjmatchedPos = 1;
/*     */         } 
/* 189 */         return jjMoveStringLiteralDfa2_0(active0, 65536L);
/*     */       case 's':
/* 191 */         if ((active0 & 0x8000L) != 0L) {
/*     */           
/* 193 */           this.jjmatchedKind = 15;
/* 194 */           this.jjmatchedPos = 1;
/*     */         } 
/* 196 */         return jjMoveStringLiteralDfa2_0(active0, 8192L);
/*     */       case 'u':
/* 198 */         return jjMoveStringLiteralDfa2_0(active0, 262144L);
/*     */     } 
/*     */ 
/*     */     
/* 202 */     return jjMoveNfa_0(5, 1);
/*     */   }
/*     */   
/*     */   private final int jjMoveStringLiteralDfa2_0(long old0, long active0) {
/* 206 */     if ((active0 &= old0) == 0L)
/* 207 */       return jjMoveNfa_0(5, 1);  try {
/* 208 */       this.curChar = this.input_stream.readChar();
/* 209 */     } catch (IOException e) {
/* 210 */       return jjMoveNfa_0(5, 1);
/*     */     } 
/* 212 */     switch (this.curChar) {
/*     */       
/*     */       case 'C':
/* 215 */         return jjMoveStringLiteralDfa3_0(active0, 8192L);
/*     */       case 'D':
/* 217 */         if ((active0 & 0x200L) != 0L) {
/*     */           
/* 219 */           this.jjmatchedKind = 9;
/* 220 */           this.jjmatchedPos = 2;
/*     */         } 
/*     */         break;
/*     */       case 'K':
/* 224 */         return jjMoveStringLiteralDfa3_0(active0, 4096L);
/*     */       case 'L':
/* 226 */         return jjMoveStringLiteralDfa3_0(active0, 393216L);
/*     */       case 'T':
/* 228 */         if ((active0 & 0x100L) != 0L) {
/*     */           
/* 230 */           this.jjmatchedKind = 8;
/* 231 */           this.jjmatchedPos = 2;
/*     */         } 
/* 233 */         return jjMoveStringLiteralDfa3_0(active0, 2048L);
/*     */       case 'U':
/* 235 */         return jjMoveStringLiteralDfa3_0(active0, 65536L);
/*     */       case 'c':
/* 237 */         return jjMoveStringLiteralDfa3_0(active0, 8192L);
/*     */       case 'd':
/* 239 */         if ((active0 & 0x200L) != 0L) {
/*     */           
/* 241 */           this.jjmatchedKind = 9;
/* 242 */           this.jjmatchedPos = 2;
/*     */         } 
/*     */         break;
/*     */       case 'k':
/* 246 */         return jjMoveStringLiteralDfa3_0(active0, 4096L);
/*     */       case 'l':
/* 248 */         return jjMoveStringLiteralDfa3_0(active0, 393216L);
/*     */       case 't':
/* 250 */         if ((active0 & 0x100L) != 0L) {
/*     */           
/* 252 */           this.jjmatchedKind = 8;
/* 253 */           this.jjmatchedPos = 2;
/*     */         } 
/* 255 */         return jjMoveStringLiteralDfa3_0(active0, 2048L);
/*     */       case 'u':
/* 257 */         return jjMoveStringLiteralDfa3_0(active0, 65536L);
/*     */     } 
/*     */ 
/*     */     
/* 261 */     return jjMoveNfa_0(5, 2);
/*     */   }
/*     */   
/*     */   private final int jjMoveStringLiteralDfa3_0(long old0, long active0) {
/* 265 */     if ((active0 &= old0) == 0L)
/* 266 */       return jjMoveNfa_0(5, 2);  try {
/* 267 */       this.curChar = this.input_stream.readChar();
/* 268 */     } catch (IOException e) {
/* 269 */       return jjMoveNfa_0(5, 2);
/*     */     } 
/* 271 */     switch (this.curChar) {
/*     */       
/*     */       case 'A':
/* 274 */         return jjMoveStringLiteralDfa4_0(active0, 8192L);
/*     */       case 'E':
/* 276 */         if ((active0 & 0x1000L) != 0L) {
/*     */           
/* 278 */           this.jjmatchedKind = 12;
/* 279 */           this.jjmatchedPos = 3; break;
/*     */         } 
/* 281 */         if ((active0 & 0x10000L) != 0L) {
/*     */           
/* 283 */           this.jjmatchedKind = 16;
/* 284 */           this.jjmatchedPos = 3;
/*     */         } 
/*     */         break;
/*     */       case 'L':
/* 288 */         if ((active0 & 0x40000L) != 0L) {
/*     */           
/* 290 */           this.jjmatchedKind = 18;
/* 291 */           this.jjmatchedPos = 3;
/*     */         } 
/*     */         break;
/*     */       case 'S':
/* 295 */         return jjMoveStringLiteralDfa4_0(active0, 131072L);
/*     */       case 'W':
/* 297 */         return jjMoveStringLiteralDfa4_0(active0, 2048L);
/*     */       case 'a':
/* 299 */         return jjMoveStringLiteralDfa4_0(active0, 8192L);
/*     */       case 'e':
/* 301 */         if ((active0 & 0x1000L) != 0L) {
/*     */           
/* 303 */           this.jjmatchedKind = 12;
/* 304 */           this.jjmatchedPos = 3; break;
/*     */         } 
/* 306 */         if ((active0 & 0x10000L) != 0L) {
/*     */           
/* 308 */           this.jjmatchedKind = 16;
/* 309 */           this.jjmatchedPos = 3;
/*     */         } 
/*     */         break;
/*     */       case 'l':
/* 313 */         if ((active0 & 0x40000L) != 0L) {
/*     */           
/* 315 */           this.jjmatchedKind = 18;
/* 316 */           this.jjmatchedPos = 3;
/*     */         } 
/*     */         break;
/*     */       case 's':
/* 320 */         return jjMoveStringLiteralDfa4_0(active0, 131072L);
/*     */       case 'w':
/* 322 */         return jjMoveStringLiteralDfa4_0(active0, 2048L);
/*     */     } 
/*     */ 
/*     */     
/* 326 */     return jjMoveNfa_0(5, 3);
/*     */   }
/*     */   
/*     */   private final int jjMoveStringLiteralDfa4_0(long old0, long active0) {
/* 330 */     if ((active0 &= old0) == 0L)
/* 331 */       return jjMoveNfa_0(5, 3);  try {
/* 332 */       this.curChar = this.input_stream.readChar();
/* 333 */     } catch (IOException e) {
/* 334 */       return jjMoveNfa_0(5, 3);
/*     */     } 
/* 336 */     switch (this.curChar) {
/*     */       
/*     */       case 'E':
/* 339 */         if ((active0 & 0x20000L) != 0L) {
/*     */           
/* 341 */           this.jjmatchedKind = 17;
/* 342 */           this.jjmatchedPos = 4;
/*     */         } 
/* 344 */         return jjMoveStringLiteralDfa5_0(active0, 2048L);
/*     */       case 'P':
/* 346 */         return jjMoveStringLiteralDfa5_0(active0, 8192L);
/*     */       case 'e':
/* 348 */         if ((active0 & 0x20000L) != 0L) {
/*     */           
/* 350 */           this.jjmatchedKind = 17;
/* 351 */           this.jjmatchedPos = 4;
/*     */         } 
/* 353 */         return jjMoveStringLiteralDfa5_0(active0, 2048L);
/*     */       case 'p':
/* 355 */         return jjMoveStringLiteralDfa5_0(active0, 8192L);
/*     */     } 
/*     */ 
/*     */     
/* 359 */     return jjMoveNfa_0(5, 4);
/*     */   }
/*     */   
/*     */   private final int jjMoveStringLiteralDfa5_0(long old0, long active0) {
/* 363 */     if ((active0 &= old0) == 0L)
/* 364 */       return jjMoveNfa_0(5, 4);  try {
/* 365 */       this.curChar = this.input_stream.readChar();
/* 366 */     } catch (IOException e) {
/* 367 */       return jjMoveNfa_0(5, 4);
/*     */     } 
/* 369 */     switch (this.curChar) {
/*     */       
/*     */       case 'E':
/* 372 */         if ((active0 & 0x2000L) != 0L) {
/*     */           
/* 374 */           this.jjmatchedKind = 13;
/* 375 */           this.jjmatchedPos = 5;
/*     */         } 
/* 377 */         return jjMoveStringLiteralDfa6_0(active0, 2048L);
/*     */       case 'e':
/* 379 */         if ((active0 & 0x2000L) != 0L) {
/*     */           
/* 381 */           this.jjmatchedKind = 13;
/* 382 */           this.jjmatchedPos = 5;
/*     */         } 
/* 384 */         return jjMoveStringLiteralDfa6_0(active0, 2048L);
/*     */     } 
/*     */ 
/*     */     
/* 388 */     return jjMoveNfa_0(5, 5);
/*     */   }
/*     */   
/*     */   private final int jjMoveStringLiteralDfa6_0(long old0, long active0) {
/* 392 */     if ((active0 &= old0) == 0L)
/* 393 */       return jjMoveNfa_0(5, 5);  try {
/* 394 */       this.curChar = this.input_stream.readChar();
/* 395 */     } catch (IOException e) {
/* 396 */       return jjMoveNfa_0(5, 5);
/*     */     } 
/* 398 */     switch (this.curChar) {
/*     */       
/*     */       case 'N':
/* 401 */         if ((active0 & 0x800L) != 0L) {
/*     */           
/* 403 */           this.jjmatchedKind = 11;
/* 404 */           this.jjmatchedPos = 6;
/*     */         } 
/*     */         break;
/*     */       case 'n':
/* 408 */         if ((active0 & 0x800L) != 0L) {
/*     */           
/* 410 */           this.jjmatchedKind = 11;
/* 411 */           this.jjmatchedPos = 6;
/*     */         } 
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 417 */     return jjMoveNfa_0(5, 6);
/*     */   }
/*     */   
/*     */   private final void jjCheckNAdd(int state) {
/* 421 */     if (this.jjrounds[state] != this.jjround) {
/*     */       
/* 423 */       this.jjstateSet[this.jjnewStateCnt++] = state;
/* 424 */       this.jjrounds[state] = this.jjround;
/*     */     } 
/*     */   }
/*     */   
/*     */   private final void jjAddStates(int start, int end) {
/*     */     do {
/* 430 */       this.jjstateSet[this.jjnewStateCnt++] = jjnextStates[start];
/* 431 */     } while (start++ != end);
/*     */   }
/*     */   
/*     */   private final void jjCheckNAddTwoStates(int state1, int state2) {
/* 435 */     jjCheckNAdd(state1);
/* 436 */     jjCheckNAdd(state2);
/*     */   }
/*     */   
/*     */   private final void jjCheckNAddStates(int start, int end) {
/*     */     do {
/* 441 */       jjCheckNAdd(jjnextStates[start]);
/* 442 */     } while (start++ != end);
/*     */   }
/*     */   
/*     */   private final void jjCheckNAddStates(int start) {
/* 446 */     jjCheckNAdd(jjnextStates[start]);
/* 447 */     jjCheckNAdd(jjnextStates[start + 1]);
/*     */   }
/* 449 */   static final long[] jjbitVec0 = new long[] { -2L, -1L, -1L, -1L };
/*     */ 
/*     */   
/* 452 */   static final long[] jjbitVec2 = new long[] { 0L, 0L, -1L, -1L };
/*     */ 
/*     */ 
/*     */   
/*     */   private final int jjMoveNfa_0(int startState, int curPos) {
/* 457 */     int strKind = this.jjmatchedKind;
/* 458 */     int strPos = this.jjmatchedPos;
/*     */     int seenUpto;
/* 460 */     this.input_stream.backup(seenUpto = curPos + 1); 
/* 461 */     try { this.curChar = this.input_stream.readChar(); }
/* 462 */     catch (IOException e) { throw new Error("Internal Error"); }
/* 463 */      curPos = 0;
/*     */     
/* 465 */     int startsAt = 0;
/* 466 */     this.jjnewStateCnt = 37;
/* 467 */     int i = 1;
/* 468 */     this.jjstateSet[0] = startState;
/* 469 */     int kind = Integer.MAX_VALUE;
/*     */     
/*     */     while (true) {
/* 472 */       if (++this.jjround == Integer.MAX_VALUE)
/* 473 */         ReInitRounds(); 
/* 474 */       if (this.curChar < '@') {
/*     */         
/* 476 */         long l = 1L << this.curChar;
/*     */         
/*     */         do {
/* 479 */           switch (this.jjstateSet[--i]) {
/*     */             
/*     */             case 5:
/* 482 */               if ((0x3FF000000000000L & l) != 0L) {
/*     */                 
/* 484 */                 if (kind > 19)
/* 485 */                   kind = 19; 
/* 486 */                 jjCheckNAddStates(0, 4); break;
/*     */               } 
/* 488 */               if (this.curChar == '$') {
/*     */                 
/* 490 */                 if (kind > 23)
/* 491 */                   kind = 23; 
/* 492 */                 jjCheckNAdd(24); break;
/*     */               } 
/* 494 */               if (this.curChar == '\'') {
/* 495 */                 jjCheckNAddStates(5, 7); break;
/* 496 */               }  if (this.curChar == '.') {
/* 497 */                 jjCheckNAdd(14); break;
/* 498 */               }  if (this.curChar == '/') {
/* 499 */                 this.jjstateSet[this.jjnewStateCnt++] = 6; break;
/* 500 */               }  if (this.curChar == '-')
/* 501 */                 this.jjstateSet[this.jjnewStateCnt++] = 0; 
/*     */               break;
/*     */             case 0:
/* 504 */               if (this.curChar == '-')
/* 505 */                 jjCheckNAddStates(8, 10); 
/*     */               break;
/*     */             case 1:
/* 508 */               if ((0xFFFFFFFFFFFFDBFFL & l) != 0L)
/* 509 */                 jjCheckNAddStates(8, 10); 
/*     */               break;
/*     */             case 2:
/* 512 */               if ((0x2400L & l) != 0L && kind > 6)
/* 513 */                 kind = 6; 
/*     */               break;
/*     */             case 3:
/* 516 */               if (this.curChar == '\n' && kind > 6)
/* 517 */                 kind = 6; 
/*     */               break;
/*     */             case 4:
/* 520 */               if (this.curChar == '\r')
/* 521 */                 this.jjstateSet[this.jjnewStateCnt++] = 3; 
/*     */               break;
/*     */             case 6:
/* 524 */               if (this.curChar == '*')
/* 525 */                 jjCheckNAddTwoStates(7, 8); 
/*     */               break;
/*     */             case 7:
/* 528 */               if ((0xFFFFFBFFFFFFFFFFL & l) != 0L)
/* 529 */                 jjCheckNAddTwoStates(7, 8); 
/*     */               break;
/*     */             case 8:
/* 532 */               if (this.curChar == '*')
/* 533 */                 jjCheckNAddStates(11, 13); 
/*     */               break;
/*     */             case 9:
/* 536 */               if ((0xFFFF7BFFFFFFFFFFL & l) != 0L)
/* 537 */                 jjCheckNAddTwoStates(10, 8); 
/*     */               break;
/*     */             case 10:
/* 540 */               if ((0xFFFFFBFFFFFFFFFFL & l) != 0L)
/* 541 */                 jjCheckNAddTwoStates(10, 8); 
/*     */               break;
/*     */             case 11:
/* 544 */               if (this.curChar == '/' && kind > 7)
/* 545 */                 kind = 7; 
/*     */               break;
/*     */             case 12:
/* 548 */               if (this.curChar == '/')
/* 549 */                 this.jjstateSet[this.jjnewStateCnt++] = 6; 
/*     */               break;
/*     */             case 13:
/* 552 */               if (this.curChar == '.')
/* 553 */                 jjCheckNAdd(14); 
/*     */               break;
/*     */             case 14:
/* 556 */               if ((0x3FF000000000000L & l) == 0L)
/*     */                 break; 
/* 558 */               if (kind > 20)
/* 559 */                 kind = 20; 
/* 560 */               jjCheckNAddTwoStates(14, 15);
/*     */               break;
/*     */             case 16:
/* 563 */               if ((0x280000000000L & l) != 0L)
/* 564 */                 jjCheckNAdd(17); 
/*     */               break;
/*     */             case 17:
/* 567 */               if ((0x3FF000000000000L & l) == 0L)
/*     */                 break; 
/* 569 */               if (kind > 20)
/* 570 */                 kind = 20; 
/* 571 */               jjCheckNAdd(17);
/*     */               break;
/*     */             case 18:
/*     */             case 19:
/* 575 */               if (this.curChar == '\'')
/* 576 */                 jjCheckNAddStates(5, 7); 
/*     */               break;
/*     */             case 20:
/* 579 */               if (this.curChar == '\'')
/* 580 */                 this.jjstateSet[this.jjnewStateCnt++] = 19; 
/*     */               break;
/*     */             case 21:
/* 583 */               if ((0xFFFFFF7FFFFFFFFFL & l) != 0L)
/* 584 */                 jjCheckNAddStates(5, 7); 
/*     */               break;
/*     */             case 22:
/* 587 */               if (this.curChar == '\'' && kind > 22)
/* 588 */                 kind = 22; 
/*     */               break;
/*     */             case 23:
/* 591 */               if (this.curChar != '$')
/*     */                 break; 
/* 593 */               if (kind > 23)
/* 594 */                 kind = 23; 
/* 595 */               jjCheckNAdd(24);
/*     */               break;
/*     */             case 24:
/* 598 */               if ((0x3FF001000000000L & l) == 0L)
/*     */                 break; 
/* 600 */               if (kind > 23)
/* 601 */                 kind = 23; 
/* 602 */               jjCheckNAdd(24);
/*     */               break;
/*     */             case 25:
/* 605 */               if ((0x3FF000000000000L & l) == 0L)
/*     */                 break; 
/* 607 */               if (kind > 19)
/* 608 */                 kind = 19; 
/* 609 */               jjCheckNAddStates(0, 4);
/*     */               break;
/*     */             case 26:
/* 612 */               if ((0x3FF000000000000L & l) == 0L)
/*     */                 break; 
/* 614 */               if (kind > 19)
/* 615 */                 kind = 19; 
/* 616 */               jjCheckNAdd(26);
/*     */               break;
/*     */             case 27:
/* 619 */               if ((0x3FF000000000000L & l) != 0L)
/* 620 */                 jjCheckNAddTwoStates(27, 28); 
/*     */               break;
/*     */             case 28:
/* 623 */               if (this.curChar != '.')
/*     */                 break; 
/* 625 */               if (kind > 20)
/* 626 */                 kind = 20; 
/* 627 */               jjCheckNAddTwoStates(29, 30);
/*     */               break;
/*     */             case 29:
/* 630 */               if ((0x3FF000000000000L & l) == 0L)
/*     */                 break; 
/* 632 */               if (kind > 20)
/* 633 */                 kind = 20; 
/* 634 */               jjCheckNAddTwoStates(29, 30);
/*     */               break;
/*     */             case 31:
/* 637 */               if ((0x280000000000L & l) != 0L)
/* 638 */                 jjCheckNAdd(32); 
/*     */               break;
/*     */             case 32:
/* 641 */               if ((0x3FF000000000000L & l) == 0L)
/*     */                 break; 
/* 643 */               if (kind > 20)
/* 644 */                 kind = 20; 
/* 645 */               jjCheckNAdd(32);
/*     */               break;
/*     */             case 33:
/* 648 */               if ((0x3FF000000000000L & l) != 0L)
/* 649 */                 jjCheckNAddTwoStates(33, 34); 
/*     */               break;
/*     */             case 35:
/* 652 */               if ((0x280000000000L & l) != 0L)
/* 653 */                 jjCheckNAdd(36); 
/*     */               break;
/*     */             case 36:
/* 656 */               if ((0x3FF000000000000L & l) == 0L)
/*     */                 break; 
/* 658 */               if (kind > 20)
/* 659 */                 kind = 20; 
/* 660 */               jjCheckNAdd(36);
/*     */               break;
/*     */           } 
/*     */         
/* 664 */         } while (i != startsAt);
/*     */       }
/* 666 */       else if (this.curChar < '') {
/*     */         
/* 668 */         long l = 1L << (this.curChar & 0x3F);
/*     */         
/*     */         do {
/* 671 */           switch (this.jjstateSet[--i]) {
/*     */             
/*     */             case 5:
/*     */             case 24:
/* 675 */               if ((0x7FFFFFE87FFFFFEL & l) == 0L)
/*     */                 break; 
/* 677 */               if (kind > 23)
/* 678 */                 kind = 23; 
/* 679 */               jjCheckNAdd(24);
/*     */               break;
/*     */             case 1:
/* 682 */               jjAddStates(8, 10);
/*     */               break;
/*     */             case 7:
/* 685 */               jjCheckNAddTwoStates(7, 8);
/*     */               break;
/*     */             case 9:
/*     */             case 10:
/* 689 */               jjCheckNAddTwoStates(10, 8);
/*     */               break;
/*     */             case 15:
/* 692 */               if ((0x2000000020L & l) != 0L)
/* 693 */                 jjAddStates(14, 15); 
/*     */               break;
/*     */             case 21:
/* 696 */               jjAddStates(5, 7);
/*     */               break;
/*     */             case 30:
/* 699 */               if ((0x2000000020L & l) != 0L)
/* 700 */                 jjAddStates(16, 17); 
/*     */               break;
/*     */             case 34:
/* 703 */               if ((0x2000000020L & l) != 0L) {
/* 704 */                 jjAddStates(18, 19);
/*     */               }
/*     */               break;
/*     */           } 
/* 708 */         } while (i != startsAt);
/*     */       }
/*     */       else {
/*     */         
/* 712 */         int hiByte = this.curChar >> 8;
/* 713 */         int i1 = hiByte >> 6;
/* 714 */         long l1 = 1L << (hiByte & 0x3F);
/* 715 */         int i2 = (this.curChar & 0xFF) >> 6;
/* 716 */         long l2 = 1L << (this.curChar & 0x3F);
/*     */         
/*     */         do {
/* 719 */           switch (this.jjstateSet[--i]) {
/*     */             
/*     */             case 1:
/* 722 */               if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 723 */                 jjAddStates(8, 10); 
/*     */               break;
/*     */             case 7:
/* 726 */               if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 727 */                 jjCheckNAddTwoStates(7, 8); 
/*     */               break;
/*     */             case 9:
/*     */             case 10:
/* 731 */               if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 732 */                 jjCheckNAddTwoStates(10, 8); 
/*     */               break;
/*     */             case 21:
/* 735 */               if (jjCanMove_0(hiByte, i1, i2, l1, l2)) {
/* 736 */                 jjAddStates(5, 7);
/*     */               }
/*     */               break;
/*     */           } 
/* 740 */         } while (i != startsAt);
/*     */       } 
/* 742 */       if (kind != Integer.MAX_VALUE) {
/*     */         
/* 744 */         this.jjmatchedKind = kind;
/* 745 */         this.jjmatchedPos = curPos;
/* 746 */         kind = Integer.MAX_VALUE;
/*     */       } 
/* 748 */       curPos++;
/* 749 */       if ((i = this.jjnewStateCnt) == (startsAt = 37 - (this.jjnewStateCnt = startsAt)))
/*     */         break;  
/* 751 */       try { this.curChar = this.input_stream.readChar(); }
/* 752 */       catch (IOException e) { break; }
/*     */     
/* 754 */     }  if (this.jjmatchedPos > strPos) {
/* 755 */       return curPos;
/*     */     }
/* 757 */     int toRet = Math.max(curPos, seenUpto);
/*     */     
/* 759 */     if (curPos < toRet)
/* 760 */       for (i = toRet - Math.min(curPos, seenUpto); i-- > 0;) { 
/* 761 */         try { this.curChar = this.input_stream.readChar(); }
/* 762 */         catch (IOException e) { throw new Error("Internal Error : Please send a bug report."); }
/*     */          }
/* 764 */         if (this.jjmatchedPos < strPos) {
/*     */       
/* 766 */       this.jjmatchedKind = strKind;
/* 767 */       this.jjmatchedPos = strPos;
/*     */     }
/* 769 */     else if (this.jjmatchedPos == strPos && this.jjmatchedKind > strKind) {
/* 770 */       this.jjmatchedKind = strKind;
/*     */     } 
/* 772 */     return toRet;
/*     */   }
/* 774 */   static final int[] jjnextStates = new int[] { 26, 27, 28, 33, 34, 20, 21, 22, 1, 2, 4, 8, 9, 11, 16, 17, 31, 32, 35, 36 };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2) {
/* 780 */     switch (hiByte) {
/*     */       
/*     */       case 0:
/* 783 */         return ((jjbitVec2[i2] & l2) != 0L);
/*     */     } 
/* 785 */     if ((jjbitVec0[i1] & l1) != 0L)
/* 786 */       return true; 
/* 787 */     return false;
/*     */   }
/*     */   
/* 790 */   public static final String[] jjstrLiteralImages = new String[] { "", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "=", "<>", ">", ">=", "<", "<=", "(", ",", ")", "+", "-", "*", "/", "%" };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 795 */   public static final String[] lexStateNames = new String[] { "DEFAULT" };
/*     */ 
/*     */   
/* 798 */   static final long[] jjtoToken = new long[] { 274875809537L };
/*     */ 
/*     */   
/* 801 */   static final long[] jjtoSkip = new long[] { 254L };
/*     */ 
/*     */   
/* 804 */   static final long[] jjtoSpecial = new long[] { 62L };
/*     */   
/*     */   private SimpleCharStream input_stream;
/*     */   
/* 808 */   private final int[] jjrounds = new int[37];
/* 809 */   private final int[] jjstateSet = new int[74];
/*     */   protected char curChar;
/*     */   int curLexState;
/*     */   int defaultLexState;
/*     */   int jjnewStateCnt;
/*     */   int jjround;
/*     */   int jjmatchedPos;
/*     */   int jjmatchedKind;
/*     */   
/*     */   public SelectorParserTokenManager(SimpleCharStream stream, int lexState) {
/* 819 */     this(stream);
/* 820 */     SwitchTo(lexState);
/*     */   }
/*     */   
/*     */   public void ReInit(SimpleCharStream stream) {
/* 824 */     this.jjmatchedPos = this.jjnewStateCnt = 0;
/* 825 */     this.curLexState = this.defaultLexState;
/* 826 */     this.input_stream = stream;
/* 827 */     ReInitRounds();
/*     */   }
/*     */ 
/*     */   
/*     */   private final void ReInitRounds() {
/* 832 */     this.jjround = -2147483647;
/* 833 */     for (int i = 37; i-- > 0;)
/* 834 */       this.jjrounds[i] = Integer.MIN_VALUE; 
/*     */   }
/*     */   
/*     */   public void ReInit(SimpleCharStream stream, int lexState) {
/* 838 */     ReInit(stream);
/* 839 */     SwitchTo(lexState);
/*     */   }
/*     */   
/*     */   public void SwitchTo(int lexState) {
/* 843 */     if (lexState >= 1 || lexState < 0) {
/* 844 */       throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
/*     */     }
/* 846 */     this.curLexState = lexState;
/*     */   }
/*     */ 
/*     */   
/*     */   private final Token jjFillToken() {
/* 851 */     Token t = Token.newToken(this.jjmatchedKind);
/* 852 */     t.kind = this.jjmatchedKind;
/* 853 */     String im = jjstrLiteralImages[this.jjmatchedKind];
/* 854 */     t.image = (im == null) ? this.input_stream.GetImage() : im;
/* 855 */     t.beginLine = this.input_stream.getBeginLine();
/* 856 */     t.beginColumn = this.input_stream.getBeginColumn();
/* 857 */     t.endLine = this.input_stream.getEndLine();
/* 858 */     t.endColumn = this.input_stream.getEndColumn();
/* 859 */     return t;
/*     */   }
/*     */   public SelectorParserTokenManager(SimpleCharStream stream) {
/* 862 */     this.curLexState = 0;
/* 863 */     this.defaultLexState = 0;
/*     */     this.input_stream = stream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Token getNextToken() {
/* 872 */     Token specialToken = null;
/*     */     
/* 874 */     int curPos = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/*     */       try {
/* 881 */         this.curChar = this.input_stream.BeginToken();
/*     */       }
/* 883 */       catch (IOException e) {
/*     */         
/* 885 */         this.jjmatchedKind = 0;
/* 886 */         Token matchedToken = jjFillToken();
/* 887 */         matchedToken.specialToken = specialToken;
/* 888 */         return matchedToken;
/*     */       } 
/*     */       
/* 891 */       this.jjmatchedKind = Integer.MAX_VALUE;
/* 892 */       this.jjmatchedPos = 0;
/* 893 */       curPos = jjMoveStringLiteralDfa0_0();
/* 894 */       if (this.jjmatchedKind != Integer.MAX_VALUE) {
/*     */         
/* 896 */         if (this.jjmatchedPos + 1 < curPos)
/* 897 */           this.input_stream.backup(curPos - this.jjmatchedPos - 1); 
/* 898 */         if ((jjtoToken[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 0x3F)) != 0L) {
/*     */           
/* 900 */           Token matchedToken = jjFillToken();
/* 901 */           matchedToken.specialToken = specialToken;
/* 902 */           return matchedToken;
/*     */         } 
/*     */ 
/*     */         
/* 906 */         if ((jjtoSpecial[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 0x3F)) != 0L) {
/*     */           
/* 908 */           Token matchedToken = jjFillToken();
/* 909 */           if (specialToken == null) {
/* 910 */             specialToken = matchedToken;
/*     */             continue;
/*     */           } 
/* 913 */           matchedToken.specialToken = specialToken;
/* 914 */           specialToken = specialToken.next = matchedToken;
/*     */         } 
/*     */         continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 920 */     int error_line = this.input_stream.getEndLine();
/* 921 */     int error_column = this.input_stream.getEndColumn();
/* 922 */     String error_after = null;
/* 923 */     boolean EOFSeen = false; try {
/* 924 */       this.input_stream.readChar(); this.input_stream.backup(1);
/* 925 */     } catch (IOException e1) {
/* 926 */       EOFSeen = true;
/* 927 */       error_after = (curPos <= 1) ? "" : this.input_stream.GetImage();
/* 928 */       if (this.curChar == '\n' || this.curChar == '\r') {
/* 929 */         error_line++;
/* 930 */         error_column = 0;
/*     */       } else {
/*     */         
/* 933 */         error_column++;
/*     */       } 
/* 935 */     }  if (!EOFSeen) {
/* 936 */       this.input_stream.backup(1);
/* 937 */       error_after = (curPos <= 1) ? "" : this.input_stream.GetImage();
/*     */     } 
/* 939 */     throw new TokenMgrError(EOFSeen, this.curLexState, error_line, error_column, error_after, this.curChar, 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\selector\SelectorParserTokenManager.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */