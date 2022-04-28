/*     */ package org.codehaus.activemq.transport.http;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.TextWireFormat;
/*     */ import org.codehaus.activemq.transport.TransportChannelSupport;
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
/*     */ public abstract class HttpTransportChannelSupport
/*     */   extends TransportChannelSupport
/*     */   implements Runnable
/*     */ {
/*  32 */   private static final Log log = LogFactory.getLog(HttpTransportChannelSupport.class);
/*     */   
/*     */   private TextWireFormat wireFormat;
/*     */   private String remoteUrl;
/*     */   private Thread thread;
/*  37 */   private SynchronizedBoolean closed = new SynchronizedBoolean(false);
/*  38 */   private SynchronizedBoolean started = new SynchronizedBoolean(false);
/*     */   
/*     */   public HttpTransportChannelSupport(TextWireFormat wireFormat, String remoteUrl) {
/*  41 */     this.wireFormat = wireFormat;
/*  42 */     this.remoteUrl = remoteUrl;
/*     */   }
/*     */   
/*     */   public boolean isMulticast() {
/*  46 */     return false;
/*     */   }
/*     */   
/*     */   public void start() throws JMSException {
/*  50 */     if (this.started.commit(false, true) && 
/*  51 */       getClientID() != null) {
/*  52 */       startThread();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void startThread() {
/*  58 */     this.thread = new Thread(this, toString());
/*  59 */     this.thread.start();
/*     */   }
/*     */   
/*     */   public void stop() {
/*  63 */     if (this.closed.commit(false, true)) {
/*  64 */       super.stop();
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void setClientID(String clientID) {
/*  69 */     super.setClientID(clientID);
/*  70 */     if (clientID != null && this.thread == null && this.started.get()) {
/*  71 */       startThread();
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString() {
/*  76 */     return "HTTP Reader " + getRemoteUrl();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canProcessWireFormatVersion(int version) {
/*  85 */     return this.wireFormat.canProcessWireFormatVersion(version);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCurrentWireFormatVersion() {
/*  92 */     return this.wireFormat.getCurrentWireFormatVersion();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRemoteUrl() {
/*  98 */     return this.remoteUrl;
/*     */   }
/*     */   
/*     */   public TextWireFormat getWireFormat() {
/* 102 */     return this.wireFormat;
/*     */   }
/*     */   
/*     */   public void setWireFormat(TextWireFormat wireFormat) {
/* 106 */     this.wireFormat = wireFormat;
/*     */   }
/*     */   
/*     */   public SynchronizedBoolean getClosed() {
/* 110 */     return this.closed;
/*     */   }
/*     */   
/*     */   public SynchronizedBoolean getStarted() {
/* 114 */     return this.started;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\http\HttpTransportChannelSupport.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */