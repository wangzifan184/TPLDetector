/*     */ package org.codehaus.activemq.transport.udp;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.net.URI;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.transport.TransportChannelSupport;
/*     */ import org.codehaus.activemq.util.IdGenerator;
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
/*     */ public class UdpTransportChannel
/*     */   extends TransportChannelSupport
/*     */   implements Runnable
/*     */ {
/*     */   private static final int SOCKET_BUFFER_SIZE = 32768;
/*     */   private static final int SO_TIMEOUT = 5000;
/*  45 */   private static final Log log = LogFactory.getLog(UdpTransportChannel.class);
/*     */   
/*     */   protected DatagramSocket socket;
/*     */   
/*     */   protected int port;
/*     */   protected InetAddress inetAddress;
/*     */   private WireFormat wireFormat;
/*     */   private SynchronizedBoolean closed;
/*     */   private SynchronizedBoolean started;
/*     */   private Thread thread;
/*  55 */   private IdGenerator idGenerator = new IdGenerator();
/*     */ 
/*     */   
/*     */   private Object lock;
/*     */ 
/*     */ 
/*     */   
/*     */   protected UdpTransportChannel(WireFormat wireFormat) {
/*  63 */     this.wireFormat = wireFormat;
/*  64 */     this.closed = new SynchronizedBoolean(false);
/*  65 */     this.started = new SynchronizedBoolean(false);
/*  66 */     this.lock = new Object();
/*     */   }
/*     */   
/*     */   public UdpTransportChannel(WireFormat wireFormat, URI remoteLocation) throws JMSException {
/*  70 */     this(wireFormat, remoteLocation, remoteLocation.getPort());
/*     */   }
/*     */   
/*     */   public UdpTransportChannel(WireFormat wireFormat, URI remoteLocation, int port) throws JMSException {
/*  74 */     this(wireFormat);
/*     */     try {
/*  76 */       this.port = port;
/*  77 */       this.inetAddress = InetAddress.getByName(remoteLocation.getHost());
/*  78 */       this.socket = createSocket(remoteLocation.getPort());
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  83 */       this.socket.setReceiveBufferSize(32768);
/*  84 */       this.socket.setSendBufferSize(32768);
/*     */       
/*  86 */       connect();
/*     */ 
/*     */     
/*     */     }
/*  90 */     catch (Exception ioe) {
/*  91 */       JMSException jmsEx = new JMSException("Initialization of TransportChannel failed: " + ioe);
/*  92 */       jmsEx.setLinkedException(ioe);
/*  93 */       throw jmsEx;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UdpTransportChannel(WireFormat wireFormat, DatagramSocket socket) throws JMSException {
/* 102 */     this(wireFormat);
/* 103 */     this.socket = socket;
/* 104 */     this.port = socket.getPort();
/* 105 */     this.inetAddress = socket.getInetAddress();
/*     */     try {
/* 107 */       socket.setReceiveBufferSize(32768);
/* 108 */       socket.setSendBufferSize(32768);
/*     */     }
/* 110 */     catch (IOException ioe) {
/* 111 */       JMSException jmsEx = new JMSException("Initialization of TransportChannel failed");
/* 112 */       jmsEx.setLinkedException(ioe);
/* 113 */       throw jmsEx;
/*     */     } 
/*     */   }
/*     */   
/*     */   public UdpTransportChannel(WireFormat wireFormat, DatagramSocket socket, int port) throws JMSException {
/* 118 */     this(wireFormat, socket);
/* 119 */     this.port = port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 126 */     if (this.closed.commit(false, true)) {
/* 127 */       super.stop();
/*     */       try {
/* 129 */         this.socket.close();
/*     */       }
/* 131 */       catch (Exception e) {
/* 132 */         log.trace(toString() + " now closed");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() throws JMSException {
/* 143 */     if (this.started.commit(false, true)) {
/* 144 */       this.thread = new Thread(this, toString());
/* 145 */       if (isServerSide()) {
/* 146 */         this.thread.setDaemon(true);
/*     */       }
/* 148 */       this.thread.start();
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
/*     */   public void asyncSend(Packet packet) throws JMSException {
/*     */     try {
/* 161 */       if (log.isDebugEnabled()) {
/* 162 */         log.debug("Sending packet: " + packet);
/*     */       }
/* 164 */       DatagramPacket dpacket = createDatagramPacket(packet);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 169 */       this.socket.send(dpacket);
/*     */     
/*     */     }
/* 172 */     catch (IOException e) {
/* 173 */       JMSException jmsEx = new JMSException("asyncSend failed " + e);
/* 174 */       jmsEx.setLinkedException(e);
/* 175 */       throw jmsEx;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isMulticast() {
/* 180 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 187 */     while (!this.closed.get()) {
/*     */       try {
/* 189 */         this.socket.setSoTimeout(5000);
/*     */         
/* 191 */         DatagramPacket dpacket = createDatagramPacket();
/* 192 */         while (!this.socket.isClosed()) {
/* 193 */           this.socket.setSoTimeout(0);
/* 194 */           this.socket.receive(dpacket);
/* 195 */           Packet packet = this.wireFormat.readPacket(getClientID(), dpacket);
/* 196 */           if (packet != null) {
/* 197 */             doConsumePacket(packet);
/*     */           }
/*     */         } 
/*     */         
/* 201 */         log.trace("The socket peer is now closed");
/* 202 */         doClose(new IOException("Socket peer is now closed"));
/*     */       }
/* 204 */       catch (SocketTimeoutException ste) {
/*     */ 
/*     */       
/* 207 */       } catch (IOException e) {
/* 208 */         doClose(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canProcessWireFormatVersion(int version) {
/* 219 */     return this.wireFormat.canProcessWireFormatVersion(version);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCurrentWireFormatVersion() {
/* 226 */     return this.wireFormat.getCurrentWireFormatVersion();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DatagramPacket createDatagramPacket() {
/* 233 */     DatagramPacket answer = new DatagramPacket(new byte[32768], 32768);
/* 234 */     if (this.port >= 0) {
/* 235 */       answer.setPort(this.port);
/*     */     }
/* 237 */     answer.setAddress(this.inetAddress);
/* 238 */     return answer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DatagramPacket createDatagramPacket(Packet packet) throws IOException, JMSException {
/* 246 */     DatagramPacket answer = this.wireFormat.writePacket(getClientID(), packet);
/* 247 */     if (this.port >= 0) {
/* 248 */       answer.setPort(this.port);
/*     */     }
/* 250 */     answer.setAddress(this.inetAddress);
/* 251 */     return answer;
/*     */   }
/*     */   
/*     */   private void doClose(Exception ex) {
/* 255 */     if (!this.closed.get()) {
/* 256 */       JMSException jmsEx = new JMSException("Error reading socket: " + ex.getMessage());
/* 257 */       jmsEx.setLinkedException(ex);
/* 258 */       onAsyncException(jmsEx);
/* 259 */       stop();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void connect() throws IOException {}
/*     */ 
/*     */   
/*     */   protected DatagramSocket createSocket(int port) throws IOException {
/* 268 */     return new DatagramSocket(port, this.inetAddress);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 277 */     return "UdpTransportChannel: " + this.socket;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transpor\\udp\UdpTransportChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */