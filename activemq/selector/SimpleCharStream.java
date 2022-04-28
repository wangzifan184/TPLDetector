/*     */ package org.codehaus.activemq.selector;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ 
/*     */ public final class SimpleCharStream
/*     */ {
/*     */   public static final boolean staticFlag = false;
/*     */   int bufsize;
/*     */   int available;
/*     */   int tokenBegin;
/*  14 */   public int bufpos = -1;
/*     */   
/*     */   private int[] bufline;
/*     */   private int[] bufcolumn;
/*  18 */   private int column = 0;
/*  19 */   private int line = 1;
/*     */   
/*     */   private boolean prevCharIsCR = false;
/*     */   
/*     */   private boolean prevCharIsLF = false;
/*     */   
/*     */   private Reader inputStream;
/*     */   private char[] buffer;
/*  27 */   private int maxNextCharInd = 0;
/*  28 */   private int inBuf = 0;
/*     */   
/*     */   private final void ExpandBuff(boolean wrapAround) {
/*  31 */     char[] newbuffer = new char[this.bufsize + 2048];
/*  32 */     int[] newbufline = new int[this.bufsize + 2048];
/*  33 */     int[] newbufcolumn = new int[this.bufsize + 2048];
/*     */     
/*     */     try {
/*  36 */       if (wrapAround) {
/*  37 */         System.arraycopy(this.buffer, this.tokenBegin, newbuffer, 0, this.bufsize - this.tokenBegin);
/*  38 */         System.arraycopy(this.buffer, 0, newbuffer, this.bufsize - this.tokenBegin, this.bufpos);
/*     */         
/*  40 */         this.buffer = newbuffer;
/*     */         
/*  42 */         System.arraycopy(this.bufline, this.tokenBegin, newbufline, 0, this.bufsize - this.tokenBegin);
/*  43 */         System.arraycopy(this.bufline, 0, newbufline, this.bufsize - this.tokenBegin, this.bufpos);
/*  44 */         this.bufline = newbufline;
/*     */         
/*  46 */         System.arraycopy(this.bufcolumn, this.tokenBegin, newbufcolumn, 0, this.bufsize - this.tokenBegin);
/*  47 */         System.arraycopy(this.bufcolumn, 0, newbufcolumn, this.bufsize - this.tokenBegin, this.bufpos);
/*  48 */         this.bufcolumn = newbufcolumn;
/*     */         
/*  50 */         this.maxNextCharInd = this.bufpos += this.bufsize - this.tokenBegin;
/*     */       } else {
/*     */         
/*  53 */         System.arraycopy(this.buffer, this.tokenBegin, newbuffer, 0, this.bufsize - this.tokenBegin);
/*  54 */         this.buffer = newbuffer;
/*     */         
/*  56 */         System.arraycopy(this.bufline, this.tokenBegin, newbufline, 0, this.bufsize - this.tokenBegin);
/*  57 */         this.bufline = newbufline;
/*     */         
/*  59 */         System.arraycopy(this.bufcolumn, this.tokenBegin, newbufcolumn, 0, this.bufsize - this.tokenBegin);
/*  60 */         this.bufcolumn = newbufcolumn;
/*     */         
/*  62 */         this.maxNextCharInd = this.bufpos -= this.tokenBegin;
/*     */       }
/*     */     
/*  65 */     } catch (Throwable t) {
/*  66 */       throw new Error(t.getMessage());
/*     */     } 
/*     */ 
/*     */     
/*  70 */     this.bufsize += 2048;
/*  71 */     this.available = this.bufsize;
/*  72 */     this.tokenBegin = 0;
/*     */   }
/*     */   
/*     */   private final void FillBuff() throws IOException {
/*  76 */     if (this.maxNextCharInd == this.available) {
/*  77 */       if (this.available == this.bufsize) {
/*  78 */         if (this.tokenBegin > 2048) {
/*  79 */           this.bufpos = this.maxNextCharInd = 0;
/*  80 */           this.available = this.tokenBegin;
/*     */         }
/*  82 */         else if (this.tokenBegin < 0) {
/*  83 */           this.bufpos = this.maxNextCharInd = 0;
/*     */         } else {
/*     */           
/*  86 */           ExpandBuff(false);
/*     */         }
/*     */       
/*  89 */       } else if (this.available > this.tokenBegin) {
/*  90 */         this.available = this.bufsize;
/*     */       }
/*  92 */       else if (this.tokenBegin - this.available < 2048) {
/*  93 */         ExpandBuff(true);
/*     */       } else {
/*     */         
/*  96 */         this.available = this.tokenBegin;
/*     */       } 
/*     */     }
/*     */     
/*     */     try {
/*     */       int i;
/* 102 */       if ((i = this.inputStream.read(this.buffer, this.maxNextCharInd, this.available - this.maxNextCharInd)) == -1) {
/*     */         
/* 104 */         this.inputStream.close();
/* 105 */         throw new IOException();
/*     */       } 
/*     */       
/* 108 */       this.maxNextCharInd += i;
/*     */ 
/*     */       
/*     */       return;
/* 112 */     } catch (IOException e) {
/* 113 */       this.bufpos--;
/* 114 */       backup(0);
/* 115 */       if (this.tokenBegin == -1) {
/* 116 */         this.tokenBegin = this.bufpos;
/*     */       }
/* 118 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   public final char BeginToken() throws IOException {
/* 123 */     this.tokenBegin = -1;
/* 124 */     char c = readChar();
/* 125 */     this.tokenBegin = this.bufpos;
/*     */     
/* 127 */     return c;
/*     */   }
/*     */   
/*     */   private final void UpdateLineColumn(char c) {
/* 131 */     this.column++;
/*     */     
/* 133 */     if (this.prevCharIsLF) {
/* 134 */       this.prevCharIsLF = false;
/* 135 */       this.line += this.column = 1;
/*     */     }
/* 137 */     else if (this.prevCharIsCR) {
/* 138 */       this.prevCharIsCR = false;
/* 139 */       if (c == '\n') {
/* 140 */         this.prevCharIsLF = true;
/*     */       } else {
/*     */         
/* 143 */         this.line += this.column = 1;
/*     */       } 
/*     */     } 
/*     */     
/* 147 */     switch (c) {
/*     */       case '\r':
/* 149 */         this.prevCharIsCR = true;
/*     */         break;
/*     */       case '\n':
/* 152 */         this.prevCharIsLF = true;
/*     */         break;
/*     */       case '\t':
/* 155 */         this.column--;
/* 156 */         this.column += 8 - (this.column & 0x7);
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 162 */     this.bufline[this.bufpos] = this.line;
/* 163 */     this.bufcolumn[this.bufpos] = this.column;
/*     */   }
/*     */   
/*     */   public final char readChar() throws IOException {
/* 167 */     if (this.inBuf > 0) {
/* 168 */       this.inBuf--;
/*     */       
/* 170 */       if (++this.bufpos == this.bufsize) {
/* 171 */         this.bufpos = 0;
/*     */       }
/*     */       
/* 174 */       return this.buffer[this.bufpos];
/*     */     } 
/*     */     
/* 177 */     if (++this.bufpos >= this.maxNextCharInd) {
/* 178 */       FillBuff();
/*     */     }
/*     */     
/* 181 */     char c = this.buffer[this.bufpos];
/*     */     
/* 183 */     UpdateLineColumn(c);
/* 184 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getColumn() {
/* 193 */     return this.bufcolumn[this.bufpos];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getLine() {
/* 202 */     return this.bufline[this.bufpos];
/*     */   }
/*     */   
/*     */   public final int getEndColumn() {
/* 206 */     return this.bufcolumn[this.bufpos];
/*     */   }
/*     */   
/*     */   public final int getEndLine() {
/* 210 */     return this.bufline[this.bufpos];
/*     */   }
/*     */   
/*     */   public final int getBeginColumn() {
/* 214 */     return this.bufcolumn[this.tokenBegin];
/*     */   }
/*     */   
/*     */   public final int getBeginLine() {
/* 218 */     return this.bufline[this.tokenBegin];
/*     */   }
/*     */ 
/*     */   
/*     */   public final void backup(int amount) {
/* 223 */     this.inBuf += amount;
/* 224 */     if ((this.bufpos -= amount) < 0) {
/* 225 */       this.bufpos += this.bufsize;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCharStream(Reader dstream, int startline, int startcolumn, int buffersize) {
/* 231 */     this.inputStream = dstream;
/* 232 */     this.line = startline;
/* 233 */     this.column = startcolumn - 1;
/*     */     
/* 235 */     this.available = this.bufsize = buffersize;
/* 236 */     this.buffer = new char[buffersize];
/* 237 */     this.bufline = new int[buffersize];
/* 238 */     this.bufcolumn = new int[buffersize];
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCharStream(Reader dstream, int startline, int startcolumn) {
/* 243 */     this(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */   
/*     */   public SimpleCharStream(Reader dstream) {
/* 247 */     this(dstream, 1, 1, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public void ReInit(Reader dstream, int startline, int startcolumn, int buffersize) {
/* 252 */     this.inputStream = dstream;
/* 253 */     this.line = startline;
/* 254 */     this.column = startcolumn - 1;
/*     */     
/* 256 */     if (this.buffer == null || buffersize != this.buffer.length) {
/* 257 */       this.available = this.bufsize = buffersize;
/* 258 */       this.buffer = new char[buffersize];
/* 259 */       this.bufline = new int[buffersize];
/* 260 */       this.bufcolumn = new int[buffersize];
/*     */     } 
/* 262 */     this.prevCharIsLF = this.prevCharIsCR = false;
/* 263 */     this.tokenBegin = this.inBuf = this.maxNextCharInd = 0;
/* 264 */     this.bufpos = -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void ReInit(Reader dstream, int startline, int startcolumn) {
/* 269 */     ReInit(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */   
/*     */   public void ReInit(Reader dstream) {
/* 273 */     ReInit(dstream, 1, 1, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCharStream(InputStream dstream, int startline, int startcolumn, int buffersize) {
/* 278 */     this(new InputStreamReader(dstream), startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCharStream(InputStream dstream, int startline, int startcolumn) {
/* 283 */     this(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */   
/*     */   public SimpleCharStream(InputStream dstream) {
/* 287 */     this(dstream, 1, 1, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public void ReInit(InputStream dstream, int startline, int startcolumn, int buffersize) {
/* 292 */     ReInit(new InputStreamReader(dstream), startline, startcolumn, 4096);
/*     */   }
/*     */   
/*     */   public void ReInit(InputStream dstream) {
/* 296 */     ReInit(dstream, 1, 1, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public void ReInit(InputStream dstream, int startline, int startcolumn) {
/* 301 */     ReInit(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */   
/*     */   public final String GetImage() {
/* 305 */     if (this.bufpos >= this.tokenBegin) {
/* 306 */       return new String(this.buffer, this.tokenBegin, this.bufpos - this.tokenBegin + 1);
/*     */     }
/*     */     
/* 309 */     return new String(this.buffer, this.tokenBegin, this.bufsize - this.tokenBegin) + new String(this.buffer, 0, this.bufpos + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final char[] GetSuffix(int len) {
/* 315 */     char[] ret = new char[len];
/*     */     
/* 317 */     if (this.bufpos + 1 >= len) {
/* 318 */       System.arraycopy(this.buffer, this.bufpos - len + 1, ret, 0, len);
/*     */     } else {
/*     */       
/* 321 */       System.arraycopy(this.buffer, this.bufsize - len - this.bufpos - 1, ret, 0, len - this.bufpos - 1);
/*     */       
/* 323 */       System.arraycopy(this.buffer, 0, ret, len - this.bufpos - 1, this.bufpos + 1);
/*     */     } 
/*     */     
/* 326 */     return ret;
/*     */   }
/*     */   
/*     */   public void Done() {
/* 330 */     this.buffer = null;
/* 331 */     this.bufline = null;
/* 332 */     this.bufcolumn = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void adjustBeginLineColumn(int newLine, int newCol) {
/* 339 */     int len, start = this.tokenBegin;
/*     */ 
/*     */     
/* 342 */     if (this.bufpos >= this.tokenBegin) {
/* 343 */       len = this.bufpos - this.tokenBegin + this.inBuf + 1;
/*     */     } else {
/*     */       
/* 346 */       len = this.bufsize - this.tokenBegin + this.bufpos + 1 + this.inBuf;
/*     */     } 
/*     */     
/* 349 */     int i = 0, j = 0, k = 0;
/* 350 */     int nextColDiff = 0, columnDiff = 0;
/*     */ 
/*     */     
/* 353 */     while (i < len && this.bufline[j = start % this.bufsize] == this.bufline[k = ++start % this.bufsize]) {
/* 354 */       this.bufline[j] = newLine;
/* 355 */       nextColDiff = columnDiff + this.bufcolumn[k] - this.bufcolumn[j];
/* 356 */       this.bufcolumn[j] = newCol + columnDiff;
/* 357 */       columnDiff = nextColDiff;
/* 358 */       i++;
/*     */     } 
/*     */     
/* 361 */     if (i < len) {
/* 362 */       this.bufline[j] = newLine++;
/* 363 */       this.bufcolumn[j] = newCol + columnDiff;
/*     */       
/* 365 */       while (i++ < len) {
/* 366 */         if (this.bufline[j = start % this.bufsize] != this.bufline[++start % this.bufsize]) {
/* 367 */           this.bufline[j] = newLine++;
/*     */           continue;
/*     */         } 
/* 370 */         this.bufline[j] = newLine;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 375 */     this.line = this.bufline[j];
/* 376 */     this.column = this.bufcolumn[j];
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\selector\SimpleCharStream.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */