/*     */ package org.codehaus.activemq.transport.vm;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.BoundedLinkedQueue;
/*     */ import EDU.oswego.cs.dl.util.concurrent.Channel;
/*     */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.broker.BrokerConnector;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.PacketListener;
/*     */ import org.codehaus.activemq.transport.TransportChannel;
/*     */ import org.codehaus.activemq.transport.TransportChannelListener;
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
/*     */ public class VmTransportChannel
/*     */   extends TransportChannelSupport
/*     */   implements Runnable
/*     */ {
/*  40 */   private static final Log log = LogFactory.getLog(VmTransportChannel.class);
/*  41 */   private static final Object TERMINATE = new Object();
/*  42 */   private static int lastThreadId = 0;
/*     */   
/*     */   private Channel sendChannel;
/*     */   
/*     */   private Channel receiveChannel;
/*  47 */   private int sendCapacity = 10;
/*  48 */   private int receiveCapacity = 10;
/*     */   
/*     */   private boolean asyncSend = false;
/*     */   
/*     */   private SynchronizedBoolean closed;
/*     */   private SynchronizedBoolean started;
/*     */   private Thread thread;
/*     */   private PacketListener sendListener;
/*     */   private VmTransportChannel clientSide;
/*     */   
/*     */   public VmTransportChannel() {
/*  59 */     this.closed = new SynchronizedBoolean(false);
/*  60 */     this.started = new SynchronizedBoolean(false);
/*     */   }
/*     */   
/*     */   public VmTransportChannel(Channel sendChannel, Channel receiveChannel) {
/*  64 */     this();
/*  65 */     this.sendChannel = sendChannel;
/*  66 */     this.receiveChannel = receiveChannel;
/*     */   }
/*     */   
/*     */   public VmTransportChannel(int capacity) {
/*  70 */     this((Channel)new BoundedLinkedQueue(capacity), (Channel)new BoundedLinkedQueue(capacity));
/*     */   }
/*     */   
/*     */   public void start() throws JMSException {
/*  74 */     if (this.started.commit(false, true) && 
/*  75 */       isAsyncSend()) {
/*     */ 
/*     */ 
/*     */       
/*  79 */       getSendChannel();
/*  80 */       getReceiveChannel();
/*     */       
/*  82 */       this.thread = new Thread(this, "VM Transport: " + getNextThreadId());
/*  83 */       if (isServerSide()) {
/*  84 */         this.thread.setDaemon(true);
/*     */       }
/*  86 */       this.thread.start();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  92 */     if (this.closed.commit(false, true)) {
/*  93 */       super.stop();
/*     */       
/*     */       try {
/*  96 */         if (this.sendChannel != null) {
/*  97 */           this.sendChannel.put(TERMINATE);
/*     */         }
/*  99 */         if (this.receiveChannel != null) {
/* 100 */           this.receiveChannel.put(TERMINATE);
/*     */         }
/*     */         
/* 103 */         if (this.thread != null)
/*     */         {
/* 105 */           this.thread.join();
/*     */         }
/*     */       }
/* 108 */       catch (Exception e) {
/* 109 */         log.trace(toString() + " now closed with exception: " + e);
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
/*     */   public void asyncSend(Packet packet) throws JMSException {
/* 121 */     if (this.sendChannel != null) {
/*     */       while (true) {
/*     */         try {
/* 124 */           this.sendChannel.put(packet);
/*     */           
/*     */           break;
/* 127 */         } catch (InterruptedException e) {}
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 133 */       if (this.sendListener == null && 
/* 134 */         this.clientSide != null) {
/* 135 */         this.sendListener = this.clientSide.createPacketListenerSender();
/*     */       }
/*     */       
/* 138 */       if (this.sendListener != null) {
/* 139 */         this.sendListener.consume(packet);
/*     */       } else {
/*     */         
/* 142 */         throw new JMSException("No sendListener available");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMulticast() {
/* 149 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 156 */     while (!this.closed.get()) {
/*     */       try {
/* 158 */         Object answer = this.receiveChannel.take();
/* 159 */         if (answer == TERMINATE) {
/* 160 */           log.trace("The socket peer is now closed");
/* 161 */           stop();
/*     */           return;
/*     */         } 
/* 164 */         if (answer != null) {
/* 165 */           Packet packet = (Packet)answer;
/*     */           
/* 167 */           if (this.closed.get()) {
/*     */             break;
/*     */           }
/* 170 */           doConsumePacket(packet);
/*     */         }
/*     */       
/* 173 */       } catch (InterruptedException e) {}
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
/*     */   public String toString() {
/* 185 */     return "VmTransportChannel: " + this.sendChannel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void connect(BrokerConnector brokerConnector) throws JMSException {
/* 192 */     TransportChannelListener listener = (TransportChannelListener)brokerConnector;
/* 193 */     VmTransportChannel serverSide = createServerSide();
/* 194 */     listener.addClient((TransportChannel)serverSide);
/* 195 */     serverSide.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VmTransportChannel createServerSide() throws JMSException {
/* 205 */     VmTransportChannel channel = new VmTransportChannel(getReceiveChannel(), getSendChannel());
/* 206 */     channel.clientSide = this;
/* 207 */     return channel;
/*     */   }
/*     */   
/*     */   public void setPacketListener(PacketListener listener) {
/* 211 */     super.setPacketListener(listener);
/* 212 */     if (this.clientSide != null) {
/* 213 */       this.clientSide.sendListener = listener;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canProcessWireFormatVersion(int version) {
/* 224 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCurrentWireFormatVersion() {
/* 231 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getReceiveCapacity() {
/* 237 */     return this.receiveCapacity;
/*     */   }
/*     */   
/*     */   public void setReceiveCapacity(int receiveCapacity) {
/* 241 */     this.receiveCapacity = receiveCapacity;
/*     */   }
/*     */   
/*     */   public int getSendCapacity() {
/* 245 */     return this.sendCapacity;
/*     */   }
/*     */   
/*     */   public void setSendCapacity(int sendCapacity) {
/* 249 */     this.sendCapacity = sendCapacity;
/*     */   }
/*     */   
/*     */   public boolean isAsyncSend() {
/* 253 */     return this.asyncSend;
/*     */   }
/*     */   
/*     */   public void setAsyncSend(boolean asyncSend) {
/* 257 */     this.asyncSend = asyncSend;
/*     */   }
/*     */   
/*     */   public Channel getSendChannel() {
/* 261 */     if (isAsyncSend() && 
/* 262 */       this.sendChannel == null) {
/* 263 */       this.sendChannel = createChannel(getSendCapacity());
/*     */     }
/*     */     
/* 266 */     return this.sendChannel;
/*     */   }
/*     */   
/*     */   public void setSendChannel(Channel sendChannel) {
/* 270 */     this.sendChannel = sendChannel;
/*     */   }
/*     */   
/*     */   public Channel getReceiveChannel() {
/* 274 */     if (isAsyncSend() && 
/* 275 */       this.receiveChannel == null) {
/* 276 */       this.receiveChannel = createChannel(getReceiveCapacity());
/*     */     }
/*     */     
/* 279 */     return this.receiveChannel;
/*     */   }
/*     */   
/*     */   public void setReceiveChannel(Channel receiveChannel) {
/* 283 */     this.receiveChannel = receiveChannel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static synchronized int getNextThreadId() {
/* 289 */     return lastThreadId++;
/*     */   }
/*     */   
/*     */   protected Channel createChannel(int capacity) {
/* 293 */     return (Channel)new BoundedLinkedQueue(capacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PacketListener createPacketListenerSender() {
/* 303 */     return new PacketListener() {
/*     */         public void consume(Packet packet) {
/* 305 */           VmTransportChannel.this.doConsumePacket(packet, VmTransportChannel.this.getPacketListener());
/*     */         }
/*     */         private final VmTransportChannel this$0;
/*     */       };
/*     */   }
/*     */   protected void doClose(Exception ex) {
/* 311 */     if (!this.closed.get()) {
/* 312 */       JMSException jmsEx = new JMSException("Error reading socket: " + ex.getMessage());
/* 313 */       jmsEx.setLinkedException(ex);
/* 314 */       onAsyncException(jmsEx);
/* 315 */       stop();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\vm\VmTransportChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */