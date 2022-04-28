/*     */ package org.codehaus.activemq.transport.jrms;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;
/*     */ import com.sun.multicast.reliable.RMException;
/*     */ import com.sun.multicast.reliable.transport.RMPacketSocket;
/*     */ import com.sun.multicast.reliable.transport.SessionDoneException;
/*     */ import com.sun.multicast.reliable.transport.lrmp.LRMPTransportProfile;
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.InetAddress;
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
/*     */ 
/*     */ public class JRMSTransportChannel
/*     */   extends TransportChannelSupport
/*     */   implements Runnable
/*     */ {
/*     */   private static final int SOCKET_BUFFER_SIZE = 32768;
/*  47 */   private static final Log log = LogFactory.getLog(JRMSTransportChannel.class);
/*     */   
/*     */   private WireFormat wireFormat;
/*     */   
/*     */   private SynchronizedBoolean closed;
/*     */   
/*     */   private SynchronizedBoolean started;
/*     */   
/*     */   private Thread thread;
/*     */   
/*     */   private RMPacketSocket socket;
/*     */   private IdGenerator idGenerator;
/*     */   private String channelId;
/*     */   private int port;
/*     */   private InetAddress inetAddress;
/*     */   private Object lock;
/*     */   
/*     */   protected JRMSTransportChannel(WireFormat wireFormat) {
/*  65 */     this.wireFormat = wireFormat;
/*  66 */     this.idGenerator = new IdGenerator();
/*  67 */     this.channelId = this.idGenerator.generateId();
/*  68 */     this.closed = new SynchronizedBoolean(false);
/*  69 */     this.started = new SynchronizedBoolean(false);
/*  70 */     this.lock = new Object();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JRMSTransportChannel(WireFormat wireFormat, URI remoteLocation) throws JMSException {
/*  80 */     this(wireFormat);
/*     */     try {
/*  82 */       this.port = remoteLocation.getPort();
/*  83 */       this.inetAddress = InetAddress.getByName(remoteLocation.getHost());
/*  84 */       LRMPTransportProfile profile = new LRMPTransportProfile(this.inetAddress, this.port);
/*  85 */       profile.setTTL((byte)1);
/*  86 */       profile.setOrdered(true);
/*  87 */       this.socket = profile.createRMPacketSocket(3);
/*     */     }
/*  89 */     catch (Exception ioe) {
/*  90 */       ioe.printStackTrace();
/*  91 */       JMSException jmsEx = new JMSException("Initialization of JRMSTransportChannel failed: " + ioe);
/*  92 */       jmsEx.setLinkedException(ioe);
/*  93 */       throw jmsEx;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 101 */     if (this.closed.commit(false, true)) {
/* 102 */       super.stop();
/*     */       try {
/* 104 */         this.socket.close();
/*     */       }
/* 106 */       catch (Exception e) {
/* 107 */         log.trace(toString() + " now closed");
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
/* 118 */     if (this.started.commit(false, true)) {
/* 119 */       this.thread = new Thread(this, toString());
/* 120 */       if (isServerSide()) {
/* 121 */         this.thread.setDaemon(true);
/*     */       }
/* 123 */       this.thread.start();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void asyncSend(Packet packet) throws JMSException {
/*     */     try {
/* 135 */       DatagramPacket dpacket = createDatagramPacket(packet);
/*     */ 
/*     */ 
/*     */       
/* 139 */       this.socket.send(dpacket);
/*     */     
/*     */     }
/* 142 */     catch (RMException rme) {
/* 143 */       JMSException jmsEx = new JMSException("syncSend failed " + rme.getMessage());
/* 144 */       jmsEx.setLinkedException((Exception)rme);
/* 145 */       throw jmsEx;
/*     */     }
/* 147 */     catch (IOException e) {
/* 148 */       JMSException jmsEx = new JMSException("asyncSend failed " + e.getMessage());
/* 149 */       jmsEx.setLinkedException(e);
/* 150 */       throw jmsEx;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMulticast() {
/* 156 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     try {
/* 164 */       while (!this.closed.get()) {
/* 165 */         DatagramPacket dpacket = this.socket.receive();
/* 166 */         Packet packet = this.wireFormat.readPacket(this.channelId, dpacket);
/* 167 */         if (packet != null) {
/* 168 */           doConsumePacket(packet);
/*     */         }
/*     */       } 
/* 171 */       log.trace("The socket peer is now closed");
/*     */       
/* 173 */       stop();
/*     */     }
/* 175 */     catch (SessionDoneException e) {
/*     */ 
/*     */       
/* 178 */       log.trace("Session completed", (Throwable)e);
/* 179 */       stop();
/*     */     }
/* 181 */     catch (RMException ste) {
/* 182 */       doClose((Exception)ste);
/*     */     }
/* 184 */     catch (IOException e) {
/* 185 */       doClose(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canProcessWireFormatVersion(int version) {
/* 195 */     return this.wireFormat.canProcessWireFormatVersion(version);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCurrentWireFormatVersion() {
/* 202 */     return this.wireFormat.getCurrentWireFormatVersion();
/*     */   }
/*     */   
/*     */   protected DatagramPacket createDatagramPacket() {
/* 206 */     DatagramPacket answer = new DatagramPacket(new byte[32768], 32768);
/* 207 */     answer.setPort(this.port);
/* 208 */     answer.setAddress(this.inetAddress);
/* 209 */     return answer;
/*     */   }
/*     */   
/*     */   protected DatagramPacket createDatagramPacket(Packet packet) throws IOException, JMSException {
/* 213 */     DatagramPacket answer = this.wireFormat.writePacket(this.channelId, packet);
/* 214 */     answer.setPort(this.port);
/* 215 */     answer.setAddress(this.inetAddress);
/* 216 */     return answer;
/*     */   }
/*     */   
/*     */   private void doClose(Exception ex) {
/* 220 */     if (!this.closed.get()) {
/* 221 */       JMSException jmsEx = new JMSException("Error reading socket: " + ex);
/* 222 */       jmsEx.setLinkedException(ex);
/* 223 */       onAsyncException(jmsEx);
/* 224 */       stop();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 234 */     return "JRMSTransportChannel: " + this.socket;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\jrms\JRMSTransportChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */