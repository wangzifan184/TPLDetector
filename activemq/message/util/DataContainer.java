/*     */ package org.codehaus.activemq.message.util;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.IOException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
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
/*     */ class DataContainer
/*     */ {
/*  35 */   private CopyOnWriteArrayList dataBlocks = new CopyOnWriteArrayList();
/*     */   private FileDataBlock writeBlock;
/*     */   private FileDataBlock readBlock;
/*     */   private File dir;
/*     */   private long length;
/*     */   private int size;
/*     */   private String name;
/*     */   private int maxBlockSize;
/*     */   private int sequence;
/*     */   private static final String SUFFIX = ".fdb";
/*  45 */   private static final Log log = LogFactory.getLog(DataContainer.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DataContainer(File dir, String name, int maxBlockSize) throws IOException {
/*  56 */     this.dir = dir;
/*  57 */     this.name = name;
/*  58 */     this.maxBlockSize = maxBlockSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void deleteAll() {
/*  65 */     FileFilter filter = new FileFilter() {
/*     */         public boolean accept(File file) {
/*  67 */           return (file.getName().endsWith(".fdb") && file.getName().startsWith(DataContainer.this.name));
/*     */         } private final DataContainer this$0;
/*     */       };
/*  70 */     File[] files = this.dir.listFiles(filter);
/*  71 */     if (files != null) {
/*  72 */       for (int i = 0; i < files.length; i++) {
/*  73 */         files[i].delete();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isEmpty() {
/*  84 */     return (this.size == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long length() {
/*  91 */     return this.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/*  98 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void write(byte[] data) throws IOException {
/* 108 */     if (this.writeBlock == null) {
/* 109 */       this.writeBlock = createDataBlock(this.sequence++);
/* 110 */       this.dataBlocks.add(this.writeBlock);
/* 111 */       this.readBlock = this.writeBlock;
/*     */     }
/* 113 */     else if (!this.writeBlock.isEnoughSpace(data)) {
/* 114 */       this.writeBlock = createDataBlock(this.sequence++);
/* 115 */       this.dataBlocks.add(this.writeBlock);
/*     */     } 
/* 117 */     this.length += data.length;
/* 118 */     this.size++;
/* 119 */     this.writeBlock.write(data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] read() throws IOException {
/* 129 */     byte[] result = null;
/* 130 */     if (this.readBlock != null) {
/* 131 */       result = this.readBlock.read();
/* 132 */       if (result == null) {
/* 133 */         if (this.readBlock != this.writeBlock) {
/* 134 */           this.readBlock.close();
/* 135 */           this.dataBlocks.remove(this.readBlock);
/* 136 */           this.readBlock = (FileDataBlock)this.dataBlocks.get(0);
/*     */         } 
/*     */       } else {
/*     */         
/* 140 */         this.length -= result.length;
/* 141 */         this.size--;
/*     */       } 
/*     */     } 
/* 144 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 153 */     for (int i = 0; i < this.dataBlocks.size(); i++) {
/* 154 */       FileDataBlock db = (FileDataBlock)this.dataBlocks.get(i);
/* 155 */       db.close();
/*     */     } 
/* 157 */     this.dataBlocks.clear();
/* 158 */     this.readBlock = null;
/* 159 */     this.writeBlock = null;
/* 160 */     this.size = 0;
/* 161 */     this.length = 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private FileDataBlock createDataBlock(int sequence) throws IOException {
/* 172 */     String fileName = this.name + "_" + sequence + ".fdb";
/*     */     
/* 174 */     File file = File.createTempFile(this.name, ".fdb", this.dir);
/* 175 */     file.deleteOnExit();
/* 176 */     return new FileDataBlock(file, this.maxBlockSize);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\messag\\util\DataContainer.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */