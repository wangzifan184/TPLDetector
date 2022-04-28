/*     */ package org.codehaus.activemq.journal.howl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import org.codehaus.activemq.journal.InvalidRecordLocationException;
/*     */ import org.codehaus.activemq.journal.Journal;
/*     */ import org.codehaus.activemq.journal.JournalEventListener;
/*     */ import org.codehaus.activemq.journal.RecordLocation;
/*     */ import org.objectweb.howl.log.Configuration;
/*     */ import org.objectweb.howl.log.InvalidFileSetException;
/*     */ import org.objectweb.howl.log.InvalidLogBufferException;
/*     */ import org.objectweb.howl.log.InvalidLogKeyException;
/*     */ import org.objectweb.howl.log.LogConfigurationException;
/*     */ import org.objectweb.howl.log.LogEventListener;
/*     */ import org.objectweb.howl.log.Logger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HowlJournal
/*     */   implements Journal
/*     */ {
/*     */   private final Logger logger;
/*     */   private RecordLocation lastMark;
/*     */   
/*     */   public HowlJournal(Configuration configuration) throws InvalidFileSetException, LogConfigurationException, InvalidLogBufferException, ClassNotFoundException, IOException, InterruptedException {
/*  55 */     this.logger = new Logger(configuration);
/*  56 */     this.logger.open();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RecordLocation write(byte[] data, boolean sync) throws IOException {
/*     */     try {
/*  64 */       return new LongRecordLocation(this.logger.put(data, sync));
/*  65 */     } catch (InterruptedException e) {
/*  66 */       throw (InterruptedIOException)(new InterruptedIOException()).initCause(e);
/*     */     }
/*  68 */     catch (IOException e) {
/*  69 */       throw e;
/*  70 */     } catch (Exception e) {
/*  71 */       throw (IOException)(new IOException("Journal write failed: " + e)).initCause(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMark(RecordLocation recordLocator, boolean force) throws InvalidRecordLocationException, IOException {
/*     */     try {
/*  82 */       if (recordLocator == null || recordLocator.getClass() != LongRecordLocation.class)
/*     */       {
/*  84 */         throw new InvalidRecordLocationException();
/*     */       }
/*  86 */       long location = ((LongRecordLocation)recordLocator).getLongLocation();
/*     */       
/*  88 */       this.logger.mark(location, force);
/*  89 */       this.lastMark = recordLocator;
/*     */     }
/*  91 */     catch (InterruptedException e) {
/*  92 */       throw (InterruptedIOException)(new InterruptedIOException()).initCause(e);
/*     */     }
/*  94 */     catch (IOException e) {
/*  95 */       throw e;
/*  96 */     } catch (InvalidLogKeyException e) {
/*  97 */       throw new InvalidRecordLocationException(e.getMessage(), e);
/*  98 */     } catch (Exception e) {
/*  99 */       throw (IOException)(new IOException("Journal write failed: " + e)).initCause(e);
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
/*     */   public RecordLocation getMark() {
/* 111 */     return this.lastMark;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*     */     try {
/* 119 */       this.logger.close();
/* 120 */     } catch (IOException e) {
/* 121 */       throw e;
/* 122 */     } catch (InterruptedException e) {
/* 123 */       throw (InterruptedIOException)(new InterruptedIOException()).initCause(e);
/*     */     }
/* 125 */     catch (Exception e) {
/* 126 */       throw (IOException)(new IOException("Journal close failed: " + e)).initCause(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJournalEventListener(final JournalEventListener eventListener) {
/* 135 */     this.logger.setLogEventListener(new LogEventListener() {
/*     */           public void logOverflowNotification(long key) {
/* 137 */             eventListener.overflowNotification(new LongRecordLocation(key));
/*     */           }
/*     */ 
/*     */           
/*     */           private final JournalEventListener val$eventListener;
/*     */           
/*     */           private final HowlJournal this$0;
/*     */         });
/*     */   }
/*     */   
/*     */   public RecordLocation getNextRecordLocation(RecordLocation lastLocation) throws InvalidRecordLocationException {
/* 148 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] read(RecordLocation location) throws InvalidRecordLocationException, IOException {
/* 157 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\journal\howl\HowlJournal.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */