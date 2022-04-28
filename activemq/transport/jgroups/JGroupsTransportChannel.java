/*     */ package org.codehaus.activemq.transport.jgroups;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.BoundedBuffer;
/*     */ import EDU.oswego.cs.dl.util.concurrent.Channel;
/*     */ import EDU.oswego.cs.dl.util.concurrent.Executor;
/*     */ import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
/*     */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;
/*     */ import java.io.IOException;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.transport.TransportChannelSupport;
/*     */ import org.codehaus.activemq.util.JMSExceptionHelper;
/*     */ import org.jgroups.Address;
/*     */ import org.jgroups.Channel;
/*     */ import org.jgroups.ChannelClosedException;
/*     */ import org.jgroups.ChannelException;
/*     */ import org.jgroups.ChannelNotConnectedException;
/*     */ import org.jgroups.Message;
/*     */ import org.jgroups.TimeoutException;
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
/*     */ public class JGroupsTransportChannel
/*     */   extends TransportChannelSupport
/*     */   implements Runnable
/*     */ {
/*  47 */   private static final Log log = LogFactory.getLog(JGroupsTransportChannel.class);
/*     */   
/*     */   private Channel channel;
/*  50 */   private Address localAddress = null;
/*     */   private WireFormat wireFormat;
/*     */   private SynchronizedBoolean closed;
/*     */   private SynchronizedBoolean started;
/*     */   private Object outboundLock;
/*     */   private Executor executor;
/*     */   private Thread thread;
/*     */   private boolean useAsyncSend = false;
/*     */   
/*     */   public JGroupsTransportChannel(WireFormat wireFormat, Channel channel, Executor executor) {
/*  60 */     this.wireFormat = wireFormat;
/*  61 */     this.channel = channel;
/*  62 */     this.executor = executor;
/*  63 */     this.localAddress = channel.getLocalAddress();
/*     */     
/*  65 */     this.closed = new SynchronizedBoolean(false);
/*  66 */     this.started = new SynchronizedBoolean(false);
/*  67 */     this.outboundLock = new Object();
/*  68 */     if (this.useAsyncSend) {
/*  69 */       PooledExecutor pooledExecutor = new PooledExecutor((Channel)new BoundedBuffer(1000), 1);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString() {
/*  74 */     return "JGroupsTransportChannel: " + this.channel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/*  81 */     if (this.closed.commit(false, true)) {
/*  82 */       super.stop();
/*     */       try {
/*  84 */         stopExecutor(this.executor);
/*  85 */         this.channel.disconnect();
/*  86 */         this.channel.close();
/*     */       }
/*  88 */       catch (Exception e) {
/*  89 */         log.warn("Caught while closing: " + e + ". Now Closed", e);
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
/* 100 */     if (this.started.commit(false, true)) {
/* 101 */       this.thread = new Thread(this, toString());
/* 102 */       if (isServerSide()) {
/* 103 */         this.thread.setDaemon(true);
/*     */       }
/* 105 */       this.thread.start();
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
/*     */   public void asyncSend(final Packet packet) throws JMSException {
/* 117 */     if (this.executor != null) {
/*     */       try {
/* 119 */         this.executor.execute(new Runnable() { private final Packet val$packet;
/*     */               public void run() {
/*     */                 try {
/* 122 */                   JGroupsTransportChannel.this.writePacket(packet);
/*     */                 }
/* 124 */                 catch (JMSException e) {
/* 125 */                   JGroupsTransportChannel.this.onAsyncException(e);
/*     */                 } 
/*     */               }
/*     */               private final JGroupsTransportChannel this$0; }
/*     */           );
/* 130 */       } catch (InterruptedException e) {
/* 131 */         log.info("Caught: " + e, e);
/*     */       } 
/*     */     } else {
/*     */       
/* 135 */       writePacket(packet);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMulticast() {
/* 141 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canProcessWireFormatVersion(int version) {
/* 150 */     return this.wireFormat.canProcessWireFormatVersion(version);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCurrentWireFormatVersion() {
/* 157 */     return this.wireFormat.getCurrentWireFormatVersion();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 164 */     log.trace("JGroups consumer thread starting");
/* 165 */     while (!this.closed.get()) {
/*     */       try {
/* 167 */         Object value = this.channel.receive(0L);
/* 168 */         if (value instanceof Message) {
/* 169 */           Message message = (Message)value;
/*     */ 
/*     */ 
/*     */           
/* 173 */           if (!this.localAddress.equals(message.getSrc())) {
/* 174 */             byte[] data = message.getBuffer();
/* 175 */             Packet packet = this.wireFormat.fromBytes(data);
/* 176 */             if (packet != null) {
/* 177 */               doConsumePacket(packet);
/*     */ 
/*     */             
/*     */             }
/*     */ 
/*     */           
/*     */           }
/*     */ 
/*     */ 
/*     */         
/*     */         }
/*     */ 
/*     */       
/*     */       }
/* 191 */       catch (IOException e) {
/* 192 */         doClose(e);
/*     */       }
/* 194 */       catch (ChannelClosedException e) {
/* 195 */         stop();
/*     */       }
/* 197 */       catch (ChannelNotConnectedException e) {
/* 198 */         doClose((Exception)e);
/*     */       }
/* 200 */       catch (TimeoutException e) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writePacket(Packet packet) throws JMSException {
/*     */     try {
/* 211 */       synchronized (this.outboundLock) {
/* 212 */         Address dest = null;
/* 213 */         Message message = new Message(dest, this.localAddress, this.wireFormat.toBytes(packet));
/* 214 */         this.channel.send(message);
/*     */       }
/*     */     
/* 217 */     } catch (ChannelException e) {
/* 218 */       throw JMSExceptionHelper.newJMSException("writePacket failed: " + e, e);
/*     */     }
/* 220 */     catch (IOException e) {
/* 221 */       throw JMSExceptionHelper.newJMSException("writePacket failed: " + e, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void doClose(Exception ex) {
/* 227 */     if (!this.closed.get()) {
/* 228 */       onAsyncException(JMSExceptionHelper.newJMSException("Error reading socket: " + ex, ex));
/* 229 */       stop();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\jgroups\JGroupsTransportChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */