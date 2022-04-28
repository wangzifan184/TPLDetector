/*     */ package org.codehaus.activemq.message.util;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedInt;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.DefaultWireFormat;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.WireFormat;
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
/*     */ public class SpooledBoundedPacketQueue
/*     */   implements BoundedPacketQueue
/*     */ {
/*     */   private String name;
/*     */   private DataContainer container;
/*     */   private WireFormat wireFormat;
/*     */   private long maxDataLength;
/*     */   private boolean closed;
/*     */   private boolean stopped;
/*  41 */   private SynchronizedInt size = new SynchronizedInt(0);
/*  42 */   private Object inLock = new Object();
/*  43 */   private Object outLock = new Object();
/*  44 */   private static int WAIT_TIMEOUT = 250;
/*  45 */   private static final Log log = LogFactory.getLog(SpooledBoundedPacketQueue.class);
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
/*     */   public SpooledBoundedPacketQueue(File dir, String name, long maxDataLength, int maxBlockSize) throws IOException {
/*  58 */     char[] chars = name.toCharArray();
/*  59 */     for (int i = 0; i < chars.length; i++) {
/*  60 */       if (!Character.isLetterOrDigit(chars[i])) {
/*  61 */         chars[i] = '_';
/*     */       }
/*     */     } 
/*  64 */     this.name = new String(chars);
/*  65 */     this.maxDataLength = maxDataLength;
/*  66 */     this.wireFormat = (WireFormat)new DefaultWireFormat();
/*  67 */     this.container = new DataContainer(dir, this.name, maxBlockSize);
/*     */     
/*  69 */     this.container.deleteAll();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpooledBoundedPacketQueue(File dir, String name) throws IOException {
/*  79 */     this(dir, name, 67108864L, 8192);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enqueue(Packet packet) throws JMSException {
/*  89 */     if (!isFull()) {
/*  90 */       enqueueNoBlock(packet);
/*     */     } else {
/*     */       
/*  93 */       synchronized (this.inLock) { while (true) {
/*     */           try {
/*  95 */             if (isFull()) {
/*  96 */               this.inLock.wait(WAIT_TIMEOUT);
/*     */               continue;
/*     */             } 
/*  99 */           } catch (InterruptedException ie) {} break;
/*     */         }  }
/*     */       
/* 102 */       enqueueNoBlock(packet);
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
/*     */   public void enqueueNoBlock(Packet packet) throws JMSException {
/*     */     try {
/* 115 */       byte[] data = this.wireFormat.toBytes(packet);
/* 116 */       this.size.increment();
/* 117 */       this.container.write(data);
/*     */     }
/* 119 */     catch (IOException e) {
/* 120 */       JMSException jmsEx = new JMSException("toBytes failed");
/* 121 */       jmsEx.setLinkedException(e);
/* 122 */       throw jmsEx;
/*     */     } 
/* 124 */     synchronized (this.outLock) {
/* 125 */       this.outLock.notify();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Packet dequeue() throws JMSException, InterruptedException {
/* 135 */     Packet result = null;
/* 136 */     synchronized (this.outLock) {
/* 137 */       while ((result = dequeueNoWait()) == null) {
/* 138 */         this.outLock.wait(WAIT_TIMEOUT);
/*     */       }
/*     */     } 
/* 141 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Packet dequeue(long timeInMillis) throws JMSException, InterruptedException {
/* 151 */     Packet result = dequeueNoWait();
/* 152 */     if (result == null) {
/* 153 */       synchronized (this.outLock) {
/* 154 */         this.outLock.wait(timeInMillis);
/* 155 */         result = dequeueNoWait();
/*     */       } 
/*     */     }
/* 158 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Packet dequeueNoWait() throws JMSException, InterruptedException {
/* 167 */     Packet result = null;
/* 168 */     if (this.stopped) {
/* 169 */       synchronized (this.outLock) {
/* 170 */         while (this.stopped && !this.closed) {
/* 171 */           this.outLock.wait(WAIT_TIMEOUT);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     try {
/* 177 */       byte[] data = this.container.read();
/* 178 */       if (data != null) {
/* 179 */         result = this.wireFormat.fromBytes(data);
/* 180 */         this.size.decrement();
/*     */       }
/*     */     
/* 183 */     } catch (IOException e) {
/* 184 */       JMSException jmsEx = new JMSException("fromBytes failed");
/* 185 */       jmsEx.setLinkedException(e);
/* 186 */       throw jmsEx;
/*     */     } 
/* 188 */     if (result != null && !isFull()) {
/* 189 */       synchronized (this.inLock) {
/* 190 */         this.inLock.notify();
/*     */       } 
/*     */     }
/* 193 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFull() {
/* 200 */     return (this.container.length() >= this.maxDataLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/*     */     try {
/* 208 */       this.closed = true;
/* 209 */       this.container.close();
/*     */     }
/* 211 */     catch (IOException ioe) {
/* 212 */       log.warn("Couldn't close queue", ioe);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 220 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 227 */     return this.size.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStarted() {
/* 234 */     return !this.stopped;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 241 */     synchronized (this.outLock) {
/* 242 */       this.stopped = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/* 250 */     this.stopped = false;
/* 251 */     synchronized (this.outLock) {
/* 252 */       this.outLock.notifyAll();
/*     */     } 
/* 254 */     synchronized (this.inLock) {
/* 255 */       this.inLock.notifyAll();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 263 */     return (this.size.get() == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getContents() {
/* 278 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\messag\\util\SpooledBoundedPacketQueue.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */