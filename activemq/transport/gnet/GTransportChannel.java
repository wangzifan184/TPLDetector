/*     */ package org.codehaus.activemq.transport.gnet;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.Latch;
/*     */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.URI;
/*     */ import java.net.UnknownHostException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.geronimo.network.SelectorManager;
/*     */ import org.apache.geronimo.network.protocol.AbstractProtocol;
/*     */ import org.apache.geronimo.network.protocol.DownPacket;
/*     */ import org.apache.geronimo.network.protocol.PlainDownPacket;
/*     */ import org.apache.geronimo.network.protocol.Protocol;
/*     */ import org.apache.geronimo.network.protocol.ProtocolException;
/*     */ import org.apache.geronimo.network.protocol.SocketProtocol;
/*     */ import org.apache.geronimo.network.protocol.UpPacket;
/*     */ import org.apache.geronimo.pool.ClockPool;
/*     */ import org.apache.geronimo.pool.ThreadPool;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.WireFormat;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GTransportChannel
/*     */   extends TransportChannelSupport
/*     */ {
/*  59 */   private static final Log log = LogFactory.getLog(GTransportChannel.class);
/*     */   
/*     */   private SynchronizedBoolean closed;
/*     */   
/*     */   private SynchronizedBoolean started;
/*     */   
/*     */   private Protocol protocol;
/*     */   
/*     */   private Latch dispatchLatch;
/*     */   private ThreadPool threadPool;
/*     */   private WireFormat wireFormat;
/*     */   
/*     */   protected GTransportChannel(WireFormat wireFormat, ThreadPool tp) {
/*  72 */     this.wireFormat = wireFormat;
/*  73 */     this.closed = new SynchronizedBoolean(false);
/*  74 */     this.started = new SynchronizedBoolean(false);
/*  75 */     this.dispatchLatch = new Latch();
/*  76 */     this.threadPool = tp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GTransportChannel(WireFormat wireFormat, Protocol protocol, ThreadPool tp) {
/*  83 */     this(wireFormat, tp);
/*  84 */     init(protocol);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GTransportChannel(WireFormat wireFormat, URI remoteLocation, URI localLocation, SelectorManager sm, ThreadPool tp, ClockPool cp) throws UnknownHostException, ProtocolException {
/*  94 */     this(wireFormat, tp);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 104 */     SocketProtocol sp = new SocketProtocol();
/* 105 */     sp.setTimeout(30000L);
/* 106 */     if (localLocation != null) {
/* 107 */       sp.setInterface(new InetSocketAddress(InetAddress.getByName(localLocation.getHost()), localLocation.getPort()));
/*     */     }
/*     */ 
/*     */     
/* 111 */     sp.setAddress(new InetSocketAddress(InetAddress.getByName(remoteLocation.getHost()), remoteLocation.getPort()));
/*     */ 
/*     */     
/* 114 */     sp.setSelectorManager(sm);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     init((Protocol)sp);
/* 124 */     sp.setup();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(Protocol protocol) {
/* 131 */     this.protocol = protocol;
/*     */     
/* 133 */     protocol.setUpProtocol((Protocol)new AbstractProtocol()
/*     */         {
/*     */           private final GTransportChannel this$0;
/*     */           
/*     */           public void setup() {}
/*     */           
/*     */           public void drain() {}
/*     */           
/*     */           public void teardown() {}
/*     */           
/*     */           public void sendUp(UpPacket p) {
/*     */             try {
/* 145 */               GTransportChannel.log.trace("AQUIRING: " + GTransportChannel.this.dispatchLatch);
/* 146 */               GTransportChannel.this.dispatchLatch.acquire();
/* 147 */               GTransportChannel.log.trace("AQUIRED: " + GTransportChannel.this.dispatchLatch);
/*     */               
/* 149 */               GTransportChannel.this.dispatch(p);
/*     */             }
/* 151 */             catch (InterruptedException e) {
/* 152 */               GTransportChannel.log.warn("Caught exception dispatching packet: " + p + ". Reason: " + e, e);
/*     */             } 
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public void sendDown(DownPacket p) throws ProtocolException {
/* 159 */             getDownProtocol().sendDown(p);
/*     */           }
/*     */           
/*     */           public void flush() throws ProtocolException {
/* 163 */             getDownProtocol().flush();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private void dispatch(UpPacket p) {
/*     */     try {
/* 171 */       Packet packet = toPacket(p);
/* 172 */       log.trace("<<<< SENDING UP <<<< " + packet);
/* 173 */       if (packet != null) {
/* 174 */         doConsumePacket(packet);
/*     */       }
/*     */     }
/* 177 */     catch (IOException e) {
/* 178 */       log.warn("Caught exception dispatching packet: " + p + ". Reason: " + e, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 188 */     super.stop();
/* 189 */     if (this.closed.commit(false, true)) {
/*     */       try {
/* 191 */         this.protocol.drain();
/*     */       }
/* 193 */       catch (Exception e) {
/* 194 */         log.trace(toString() + " now closed");
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
/* 205 */     if (this.started.commit(false, true))
/*     */     {
/* 207 */       this.dispatchLatch.release();
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
/* 220 */       if (log.isTraceEnabled()) {
/* 221 */         log.trace(">>>> ASYNC SENDING DOWN >>>> " + packet);
/*     */       }
/*     */ 
/*     */       
/* 225 */       synchronized (this.protocol) {
/* 226 */         this.protocol.sendDown((DownPacket)toPlainDownPacket(packet));
/*     */       }
/*     */     
/* 229 */     } catch (IOException e) {
/* 230 */       System.out.println("Caught: " + e);
/* 231 */       e.printStackTrace();
/* 232 */       JMSException jmsEx = new JMSException("asyncSend failed " + e.getMessage());
/*     */       
/* 234 */       jmsEx.setLinkedException(e);
/* 235 */       throw jmsEx;
/*     */     }
/* 237 */     catch (ProtocolException e) {
/* 238 */       System.out.println("Caught: " + e);
/* 239 */       e.printStackTrace();
/* 240 */       JMSException jmsEx = new JMSException("asyncSend failed " + e.getMessage());
/*     */       
/* 242 */       jmsEx.setLinkedException((Exception)e);
/* 243 */       throw jmsEx;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isMulticast() {
/* 248 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected PlainDownPacket toPlainDownPacket(Packet mqpacket) throws IOException, JMSException {
/* 254 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 255 */     DataOutputStream dos = new DataOutputStream(baos);
/* 256 */     this.wireFormat.writePacket(mqpacket, dos);
/* 257 */     dos.close();
/* 258 */     ArrayList list = new ArrayList(1);
/* 259 */     ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());
/* 260 */     buffer.limit(buffer.capacity());
/* 261 */     list.add(buffer);
/* 262 */     PlainDownPacket packet = new PlainDownPacket();
/* 263 */     packet.setBuffers(list);
/* 264 */     return packet;
/*     */   }
/*     */   
/*     */   protected Packet toPacket(UpPacket packet) throws IOException {
/* 268 */     final ByteBuffer buffer = packet.getBuffer();
/* 269 */     InputStream is = new InputStream() { private final ByteBuffer val$buffer;
/*     */         public int read() {
/* 271 */           if (!buffer.hasRemaining()) {
/* 272 */             return -1;
/*     */           }
/* 274 */           int rc = 0xFF & buffer.get();
/* 275 */           return rc;
/*     */         }
/*     */         private final GTransportChannel this$0;
/*     */         public synchronized int read(byte[] bytes, int off, int len) {
/* 279 */           len = Math.min(len, buffer.remaining());
/* 280 */           buffer.get(bytes, off, len);
/* 281 */           return len;
/*     */         } }
/*     */       ;
/* 284 */     DataInputStream dis = new DataInputStream(is);
/* 285 */     return this.wireFormat.readPacket(dis);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 294 */     return "GTransportChannel: " + this.protocol;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canProcessWireFormatVersion(int version) {
/* 303 */     return this.wireFormat.canProcessWireFormatVersion(version);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCurrentWireFormatVersion() {
/* 310 */     return this.wireFormat.getCurrentWireFormatVersion();
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\gnet\GTransportChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */