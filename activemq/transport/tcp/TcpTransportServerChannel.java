/*     */ package org.codehaus.activemq.transport.tcp;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.BoundedBuffer;
/*     */ import EDU.oswego.cs.dl.util.concurrent.Channel;
/*     */ import EDU.oswego.cs.dl.util.concurrent.Executor;
/*     */ import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
/*     */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.UnknownHostException;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.transport.TransportChannel;
/*     */ import org.codehaus.activemq.transport.TransportServerChannelSupport;
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
/*     */ public class TcpTransportServerChannel
/*     */   extends TransportServerChannelSupport
/*     */   implements Runnable
/*     */ {
/*  44 */   private static final Log log = LogFactory.getLog(TcpTransportServerChannel.class);
/*     */   protected static final int DEFAULT_BACKLOG = 500;
/*     */   private WireFormat wireFormat;
/*     */   private Thread serverSocketThread;
/*     */   private ServerSocket serverSocket;
/*     */   private SynchronizedBoolean closed;
/*     */   private SynchronizedBoolean started;
/*     */   private boolean useAsyncSend = false;
/*  52 */   private int maxOutstandingMessages = 10;
/*  53 */   private int backlog = 500;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TcpTransportServerChannel(WireFormat wireFormat, URI bindAddr) throws JMSException {
/*  62 */     super(bindAddr);
/*  63 */     this.wireFormat = wireFormat;
/*  64 */     this.closed = new SynchronizedBoolean(false);
/*  65 */     this.started = new SynchronizedBoolean(false);
/*     */     try {
/*  67 */       this.serverSocket = createServerSocket(bindAddr);
/*  68 */       this.serverSocket.setSoTimeout(2000);
/*  69 */       updatePhysicalUri(bindAddr);
/*     */     }
/*  71 */     catch (Exception se) {
/*  72 */       System.out.println(se);
/*  73 */       se.printStackTrace();
/*  74 */       throw JMSExceptionHelper.newJMSException("Bind to " + bindAddr + " failed: " + se.getMessage(), se);
/*     */     } 
/*     */   }
/*     */   
/*     */   public TcpTransportServerChannel(WireFormat wireFormat, ServerSocket serverSocket) throws JMSException {
/*  79 */     super(serverSocket.getInetAddress().toString());
/*  80 */     this.wireFormat = wireFormat;
/*  81 */     this.serverSocket = serverSocket;
/*  82 */     this.closed = new SynchronizedBoolean(false);
/*  83 */     this.started = new SynchronizedBoolean(false);
/*  84 */     InetAddress address = serverSocket.getInetAddress();
/*     */     try {
/*  86 */       updatePhysicalUri(new URI("tcp", "", address.getHostName(), 0, "", "", ""));
/*     */     }
/*  88 */     catch (URISyntaxException e) {
/*  89 */       throw JMSExceptionHelper.newJMSException("Failed to extract URI: : " + e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void start() throws JMSException {
/*  94 */     super.start();
/*  95 */     if (this.started.commit(false, true)) {
/*  96 */       log.info("Listening for connections at: " + getUrl());
/*  97 */       this.serverSocketThread = new Thread(this, toString());
/*  98 */       this.serverSocketThread.setDaemon(true);
/*  99 */       this.serverSocketThread.start();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stop() throws JMSException {
/* 104 */     if (this.closed.commit(false, true)) {
/* 105 */       super.stop();
/*     */       try {
/* 107 */         if (this.serverSocket != null) {
/* 108 */           this.serverSocket.close();
/* 109 */           this.serverSocketThread.join();
/* 110 */           this.serverSocketThread = null;
/*     */         }
/*     */       
/* 113 */       } catch (Throwable e) {
/* 114 */         throw JMSExceptionHelper.newJMSException("Failed to stop: " + e, e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 123 */     return "TcpTransportServerChannel@" + getUrl();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 130 */     while (!this.closed.get()) {
/* 131 */       Socket socket = null;
/*     */       try {
/* 133 */         socket = this.serverSocket.accept();
/* 134 */         if (socket != null) {
/* 135 */           if (this.closed.get()) {
/* 136 */             socket.close();
/*     */             
/*     */             continue;
/*     */           } 
/* 140 */           PooledExecutor executor = null;
/* 141 */           if (this.useAsyncSend) {
/* 142 */             executor = new PooledExecutor((Channel)new BoundedBuffer(this.maxOutstandingMessages), 1);
/*     */           }
/* 144 */           TcpTransportChannel channel = new TcpTransportChannel(this.wireFormat, socket, (Executor)executor);
/* 145 */           addClient((TransportChannel)channel);
/*     */         }
/*     */       
/*     */       }
/* 149 */       catch (SocketTimeoutException ste) {
/*     */ 
/*     */       
/* 152 */       } catch (Throwable e) {
/* 153 */         if (!this.closed.get()) {
/* 154 */           log.warn("run()", e);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUseAsyncSend() {
/* 163 */     return this.useAsyncSend;
/*     */   }
/*     */   
/*     */   public void setUseAsyncSend(boolean useAsyncSend) {
/* 167 */     this.useAsyncSend = useAsyncSend;
/*     */   }
/*     */   
/*     */   public int getMaxOutstandingMessages() {
/* 171 */     return this.maxOutstandingMessages;
/*     */   }
/*     */   
/*     */   public void setMaxOutstandingMessages(int maxOutstandingMessages) {
/* 175 */     this.maxOutstandingMessages = maxOutstandingMessages;
/*     */   }
/*     */   
/*     */   public int getBacklog() {
/* 179 */     return this.backlog;
/*     */   }
/*     */   
/*     */   public void setBacklog(int backlog) {
/* 183 */     this.backlog = backlog;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updatePhysicalUri(URI bindAddr) throws URISyntaxException {
/* 193 */     URI newURI = new URI(bindAddr.getScheme(), bindAddr.getUserInfo(), bindAddr.getHost(), this.serverSocket.getLocalPort(), bindAddr.getPath(), bindAddr.getQuery(), bindAddr.getFragment());
/*     */     
/* 195 */     setUrl(newURI.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ServerSocket createServerSocket(URI bind) throws UnknownHostException, IOException {
/* 205 */     ServerSocket answer = null;
/* 206 */     String host = bind.getHost();
/* 207 */     host = (host == null || host.length() == 0) ? "localhost" : host;
/* 208 */     InetAddress addr = InetAddress.getByName(host);
/* 209 */     if (addr.equals(InetAddress.getLocalHost())) {
/* 210 */       answer = new ServerSocket(bind.getPort(), this.backlog);
/*     */     } else {
/*     */       
/* 213 */       answer = new ServerSocket(bind.getPort(), this.backlog, addr);
/*     */     } 
/* 215 */     return answer;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\tcp\TcpTransportServerChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */