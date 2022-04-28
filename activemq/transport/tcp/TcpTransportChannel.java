/*     */ package org.codehaus.activemq.transport.tcp;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.BoundedBuffer;
/*     */ import EDU.oswego.cs.dl.util.concurrent.BoundedChannel;
/*     */ import EDU.oswego.cs.dl.util.concurrent.BoundedLinkedQueue;
/*     */ import EDU.oswego.cs.dl.util.concurrent.Channel;
/*     */ import EDU.oswego.cs.dl.util.concurrent.Executor;
/*     */ import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
/*     */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.net.URI;
/*     */ import java.net.UnknownHostException;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.message.WireFormatInfo;
/*     */ import org.codehaus.activemq.transport.TransportChannelSupport;
/*     */ import org.codehaus.activemq.util.JMSExceptionHelper;
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
/*     */ public class TcpTransportChannel
/*     */   extends TransportChannelSupport
/*     */   implements Runnable
/*     */ {
/*     */   private static final int SOCKET_BUFFER_SIZE = 65536;
/*  52 */   private static final Log log = LogFactory.getLog(TcpTransportChannel.class);
/*     */   protected Socket socket;
/*     */   private WireFormat wireFormat;
/*     */   private DataOutputStream dataOut;
/*     */   private DataInputStream dataIn;
/*     */   private SynchronizedBoolean closed;
/*     */   private SynchronizedBoolean started;
/*     */   private Object outboundLock;
/*     */   private Executor executor;
/*     */   private Thread thread;
/*     */   private boolean useAsyncSend = false;
/*     */   private boolean changeTimeout = false;
/*  64 */   private int soTimeout = 5000;
/*     */ 
/*     */   
/*     */   private BoundedChannel exceptionsList;
/*     */ 
/*     */   
/*     */   protected TcpTransportChannel(WireFormat wireFormat) {
/*  71 */     this.wireFormat = wireFormat;
/*  72 */     this.closed = new SynchronizedBoolean(false);
/*  73 */     this.started = new SynchronizedBoolean(false);
/*     */     
/*  75 */     this.exceptionsList = (BoundedChannel)new BoundedLinkedQueue(10);
/*  76 */     this.outboundLock = new Object();
/*  77 */     if (this.useAsyncSend) {
/*  78 */       this.executor = (Executor)new PooledExecutor((Channel)new BoundedBuffer(1000), 1);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TcpTransportChannel(WireFormat wireFormat, URI remoteLocation) throws JMSException {
/*  89 */     this(wireFormat);
/*     */     try {
/*  91 */       this.socket = createSocket(remoteLocation);
/*  92 */       initialiseSocket();
/*     */     }
/*  94 */     catch (Exception ioe) {
/*  95 */       throw JMSExceptionHelper.newJMSException("Initialization of TcpTransportChannel failed: " + ioe, ioe);
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
/*     */   public TcpTransportChannel(WireFormat wireFormat, URI remoteLocation, URI localLocation) throws JMSException {
/* 107 */     this(wireFormat);
/*     */     try {
/* 109 */       this.socket = createSocket(remoteLocation, localLocation);
/* 110 */       initialiseSocket();
/*     */     }
/* 112 */     catch (Exception ioe) {
/* 113 */       throw JMSExceptionHelper.newJMSException("Initialization of TcpTransportChannel failed: " + ioe, ioe);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TcpTransportChannel(WireFormat wireFormat, Socket socket, Executor executor) throws JMSException {
/* 122 */     this(wireFormat);
/* 123 */     this.socket = socket;
/* 124 */     this.executor = executor;
/* 125 */     setServerSide(true);
/*     */     try {
/* 127 */       initialiseSocket();
/*     */     }
/* 129 */     catch (IOException ioe) {
/* 130 */       throw JMSExceptionHelper.newJMSException("Initialization of TcpTransportChannel failed: " + ioe, ioe);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() throws JMSException {
/* 140 */     if (this.started.commit(false, true)) {
/* 141 */       this.thread = new Thread(this, toString());
/* 142 */       if (isServerSide()) {
/* 143 */         this.thread.setDaemon(true);
/*     */       } else {
/*     */         
/* 146 */         this.thread.setPriority(7);
/*     */       } 
/* 148 */       this.thread.start();
/*     */       
/* 150 */       if (isServerSide()) {
/* 151 */         WireFormatInfo info = new WireFormatInfo();
/* 152 */         info.setVersion(getCurrentWireFormatVersion());
/* 153 */         asyncSend((Packet)info);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 162 */     if (this.closed.commit(false, true)) {
/* 163 */       super.stop();
/*     */       try {
/* 165 */         stopExecutor(this.executor);
/* 166 */         this.dataOut.close();
/* 167 */         this.dataIn.close();
/* 168 */         this.socket.close();
/*     */ 
/*     */       
/*     */       }
/* 172 */       catch (Exception e) {
/* 173 */         log.warn("Caught while closing: " + e + ". Now Closed", e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void asyncSend(final Packet packet) throws JMSException {
/* 185 */     if (this.executor != null) {
/*     */       try {
/* 187 */         this.executor.execute(new Runnable() { private final Packet val$packet;
/*     */               public void run() {
/*     */                 try {
/* 190 */                   if (!TcpTransportChannel.this.closed.get()) {
/* 191 */                     TcpTransportChannel.this.doAsyncSend(packet);
/*     */                   }
/*     */                 }
/* 194 */                 catch (JMSException e) {
/*     */                   try {
/* 196 */                     TcpTransportChannel.this.exceptionsList.put(e);
/*     */                   }
/* 198 */                   catch (InterruptedException e1) {
/* 199 */                     TcpTransportChannel.log.warn("Failed to add element to exception list: " + e1);
/*     */                   } 
/*     */                 } 
/*     */               }
/*     */               private final TcpTransportChannel this$0; }
/*     */           );
/* 205 */       } catch (InterruptedException e) {
/* 206 */         log.info("Caught: " + e, e);
/*     */       } 
/*     */       try {
/* 209 */         JMSException e = (JMSException)this.exceptionsList.poll(0L);
/* 210 */         if (e != null) {
/* 211 */           throw e;
/*     */         }
/*     */       }
/* 214 */       catch (InterruptedException e1) {
/* 215 */         log.warn("Failed to remove element to exception list: " + e1);
/*     */       } 
/*     */     } else {
/*     */       
/* 219 */       doAsyncSend(packet);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMulticast() {
/* 227 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 234 */     log.trace("TCP consumer thread starting");
/* 235 */     int count = 0;
/* 236 */     while (!this.closed.get()) {
/* 237 */       if (isServerSide() && ++count > 500) {
/* 238 */         count = 0;
/* 239 */         Thread.yield();
/*     */       } 
/* 241 */       int type = 0;
/*     */       try {
/* 243 */         if (this.changeTimeout) {
/* 244 */           this.socket.setSoTimeout(this.soTimeout);
/*     */         }
/* 246 */         while ((type = this.dataIn.read()) == 0);
/*     */         
/* 248 */         if (type == -1) {
/* 249 */           log.info("The socket peer is now closed");
/* 250 */           onAsyncException(new JMSException("Socket peer is now closed"));
/* 251 */           stop();
/*     */           continue;
/*     */         } 
/* 254 */         if (this.changeTimeout) {
/* 255 */           this.socket.setSoTimeout(0);
/*     */         }
/* 257 */         Packet packet = this.wireFormat.readPacket(type, this.dataIn);
/* 258 */         if (packet != null) {
/* 259 */           doConsumePacket(packet);
/*     */         
/*     */         }
/*     */       }
/* 263 */       catch (SocketTimeoutException e) {
/*     */ 
/*     */       
/* 266 */       } catch (InterruptedIOException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 274 */       catch (IOException e) {
/* 275 */         doClose(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 286 */     return "TcpTransportChannel: " + this.socket;
/*     */   }
/*     */   
/*     */   public Socket getSocket() {
/* 290 */     return this.socket;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canProcessWireFormatVersion(int version) {
/* 300 */     return this.wireFormat.canProcessWireFormatVersion(version);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCurrentWireFormatVersion() {
/* 307 */     return this.wireFormat.getCurrentWireFormatVersion();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isChangeTimeout() {
/* 313 */     return this.changeTimeout;
/*     */   }
/*     */   
/*     */   public void setChangeTimeout(boolean changeTimeout) {
/* 317 */     this.changeTimeout = changeTimeout;
/*     */   }
/*     */   
/*     */   public boolean isUseAsyncSend() {
/* 321 */     return this.useAsyncSend;
/*     */   }
/*     */   
/*     */   public void setUseAsyncSend(boolean useAsyncSend) {
/* 325 */     this.useAsyncSend = useAsyncSend;
/*     */   }
/*     */   
/*     */   public int getSoTimeout() {
/* 329 */     return this.soTimeout;
/*     */   }
/*     */   
/*     */   public void setSoTimeout(int soTimeout) {
/* 333 */     this.soTimeout = soTimeout;
/* 334 */     this.changeTimeout = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doAsyncSend(Packet packet) throws JMSException {
/*     */     try {
/* 347 */       synchronized (this.outboundLock) {
/* 348 */         this.wireFormat.writePacket(packet, this.dataOut);
/* 349 */         this.dataOut.flush();
/*     */       }
/*     */     
/* 352 */     } catch (IOException e) {
/* 353 */       if (this.closed.get()) {
/* 354 */         log.trace("Caught exception while closed: " + e, e);
/*     */       } else {
/*     */         
/* 357 */         throw JMSExceptionHelper.newJMSException("asyncSend failed: " + e, e);
/*     */       }
/*     */     
/* 360 */     } catch (JMSException e) {
/* 361 */       if (this.closed.get()) {
/* 362 */         log.trace("Caught exception while closed: " + e, (Throwable)e);
/*     */       } else {
/*     */         
/* 365 */         throw e;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doClose(Exception ex) {
/* 371 */     if (!this.closed.get()) {
/* 372 */       setPendingStop(true);
/* 373 */       onAsyncException(JMSExceptionHelper.newJMSException("Error reading socket: " + ex, ex));
/* 374 */       stop();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initialiseSocket() throws IOException {
/* 384 */     this.socket.setReceiveBufferSize(65536);
/* 385 */     this.socket.setSendBufferSize(65536);
/* 386 */     this.socket.setSoTimeout(this.soTimeout);
/* 387 */     BufferedInputStream buffIn = new BufferedInputStream(this.socket.getInputStream());
/* 388 */     this.dataIn = new DataInputStream(buffIn);
/* 389 */     TcpBufferedOutputStream buffOut = new TcpBufferedOutputStream(this.socket.getOutputStream());
/* 390 */     this.dataOut = new DataOutputStream(buffOut);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Socket createSocket(URI remoteLocation) throws UnknownHostException, IOException {
/* 402 */     return new Socket(remoteLocation.getHost(), remoteLocation.getPort());
/*     */   }
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
/*     */   protected Socket createSocket(URI remoteLocation, URI localLocation) throws IOException, UnknownHostException {
/* 415 */     return new Socket(remoteLocation.getHost(), remoteLocation.getPort(), InetAddress.getByName(localLocation.getHost()), localLocation.getPort());
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\tcp\TcpTransportChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */