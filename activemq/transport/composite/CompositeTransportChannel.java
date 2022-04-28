/*     */ package org.codehaus.activemq.transport.composite;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.jms.ExceptionListener;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.TimeoutExpiredException;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.PacketListener;
/*     */ import org.codehaus.activemq.message.Receipt;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.transport.TransportChannel;
/*     */ import org.codehaus.activemq.transport.TransportChannelProvider;
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
/*     */ public class CompositeTransportChannel
/*     */   extends TransportChannelSupport
/*     */ {
/*  45 */   private static final Log log = LogFactory.getLog(CompositeTransportChannel.class);
/*     */   
/*     */   protected WireFormat wireFormat;
/*     */   protected List uris;
/*     */   protected TransportChannel channel;
/*     */   protected SynchronizedBoolean closed;
/*     */   protected SynchronizedBoolean started;
/*  52 */   protected int maximumRetries = 10;
/*  53 */   protected long failureSleepTime = 500L;
/*     */   protected URI currentURI;
/*  55 */   private long establishConnectionTimeout = 30000L;
/*     */ 
/*     */   
/*     */   public CompositeTransportChannel(WireFormat wireFormat) {
/*  59 */     this.wireFormat = wireFormat;
/*  60 */     this.uris = Collections.synchronizedList(new ArrayList());
/*  61 */     this.closed = new SynchronizedBoolean(false);
/*  62 */     this.started = new SynchronizedBoolean(false);
/*     */   }
/*     */   
/*     */   public CompositeTransportChannel(WireFormat wireFormat, List uris) {
/*  66 */     this(wireFormat);
/*  67 */     this.uris.addAll(uris);
/*     */   }
/*     */   
/*     */   public String toString() {
/*  71 */     return "CompositeTransportChannel: " + this.channel;
/*     */   }
/*     */   
/*     */   public void start() throws JMSException {
/*  75 */     if (this.started.commit(false, true)) {
/*  76 */       establishConnection(this.establishConnectionTimeout);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/*  84 */     if (this.closed.commit(false, true) && 
/*  85 */       this.channel != null) {
/*     */       try {
/*  87 */         this.channel.stop();
/*     */       }
/*  89 */       catch (Exception e) {
/*  90 */         log.warn("Caught while closing: " + e + ". Now Closed", e);
/*     */       } finally {
/*     */         
/*  93 */         this.channel = null;
/*  94 */         super.stop();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Receipt send(Packet packet) throws JMSException {
/* 101 */     return getChannel().send(packet);
/*     */   }
/*     */ 
/*     */   
/*     */   public Receipt send(Packet packet, int timeout) throws JMSException {
/* 106 */     return getChannel().send(packet, timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public void asyncSend(Packet packet) throws JMSException {
/* 111 */     getChannel().asyncSend(packet);
/*     */   }
/*     */   
/*     */   public void setPacketListener(PacketListener listener) {
/* 115 */     super.setPacketListener(listener);
/* 116 */     if (this.channel != null) {
/* 117 */       this.channel.setPacketListener(listener);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setExceptionListener(ExceptionListener listener) {
/* 123 */     super.setExceptionListener(listener);
/* 124 */     if (this.channel != null) {
/* 125 */       this.channel.setExceptionListener(listener);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMulticast() {
/* 131 */     return false;
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
/*     */   
/*     */   public long getEstablishConnectionTimeout() {
/* 145 */     return this.establishConnectionTimeout;
/*     */   }
/*     */   
/*     */   public void setEstablishConnectionTimeout(long establishConnectionTimeout) {
/* 149 */     this.establishConnectionTimeout = establishConnectionTimeout;
/*     */   }
/*     */   
/*     */   public int getMaximumRetries() {
/* 153 */     return this.maximumRetries;
/*     */   }
/*     */   
/*     */   public void setMaximumRetries(int maximumRetries) {
/* 157 */     this.maximumRetries = maximumRetries;
/*     */   }
/*     */   
/*     */   public long getFailureSleepTime() {
/* 161 */     return this.failureSleepTime;
/*     */   }
/*     */   
/*     */   public void setFailureSleepTime(long failureSleepTime) {
/* 165 */     this.failureSleepTime = failureSleepTime;
/*     */   }
/*     */   
/*     */   public List getUris() {
/* 169 */     return this.uris;
/*     */   }
/*     */   
/*     */   public void setUris(List list) {
/* 173 */     synchronized (this.uris) {
/* 174 */       this.uris.clear();
/* 175 */       this.uris.addAll(list);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canProcessWireFormatVersion(int version) {
/* 185 */     return (this.channel != null) ? this.channel.canProcessWireFormatVersion(version) : true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCurrentWireFormatVersion() {
/* 192 */     return (this.channel != null) ? this.channel.getCurrentWireFormatVersion() : 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void establishConnection(long timeout) throws JMSException {
/* 201 */     boolean connected = false;
/* 202 */     long time = this.failureSleepTime;
/* 203 */     long startTime = System.currentTimeMillis();
/*     */     
/* 205 */     for (int i = 0; !connected && (i < this.maximumRetries || this.maximumRetries <= 0) && !this.closed.get() && !isPendingStop(); i++) {
/* 206 */       List list = new ArrayList(getUris());
/* 207 */       if (i > 0) {
/* 208 */         if (this.maximumRetries > 0 || timeout > 0L) {
/* 209 */           long current = System.currentTimeMillis();
/* 210 */           if (timeout >= 0L && 
/* 211 */             current + time > startTime + timeout) {
/* 212 */             time = startTime + timeout - current;
/*     */           }
/*     */           
/* 215 */           if (current > startTime + timeout || time <= 0L) {
/* 216 */             throw new TimeoutExpiredException("Could not connect to any of the URIs: " + list);
/*     */           }
/*     */         } 
/* 219 */         log.info("Could not connect; sleeping for: " + time + " millis and trying again");
/*     */         try {
/* 221 */           Thread.sleep(time);
/*     */         }
/* 223 */         catch (InterruptedException e) {
/* 224 */           log.warn("Sleep interupted: " + e, e);
/*     */         } 
/* 226 */         if (this.maximumRetries > 0) {
/* 227 */           time *= 2L;
/*     */         }
/*     */       } 
/*     */       
/* 231 */       while (!connected && !list.isEmpty() && !this.closed.get() && !isPendingStop()) {
/* 232 */         URI uri = extractURI(list);
/*     */         try {
/* 234 */           attemptToConnect(uri);
/* 235 */           configureChannel();
/* 236 */           connected = true;
/* 237 */           this.currentURI = uri;
/*     */         }
/* 239 */         catch (JMSException e) {
/* 240 */           log.info("Could not connect to: " + uri + ". Reason: " + e);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 245 */     if (!connected && !this.closed.get()) {
/* 246 */       StringBuffer buffer = new StringBuffer("");
/* 247 */       Object[] uriArray = getUris().toArray();
/* 248 */       for (int j = 0; j < uriArray.length; j++) {
/* 249 */         buffer.append(uriArray[j]);
/* 250 */         if (j < uriArray.length - 1) {
/* 251 */           buffer.append(",");
/*     */         }
/*     */       } 
/* 254 */       JMSException jmsEx = new JMSException("Failed to connect to resource(s): " + buffer.toString());
/* 255 */       throw jmsEx;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected TransportChannel getChannel() throws JMSException {
/* 262 */     if (this.channel == null) {
/* 263 */       throw new JMSException("No TransportChannel connection available");
/*     */     }
/* 265 */     return this.channel;
/*     */   }
/*     */   
/*     */   protected void configureChannel() {
/* 269 */     ExceptionListener exceptionListener = getExceptionListener();
/* 270 */     if (exceptionListener != null) {
/* 271 */       this.channel.setExceptionListener(exceptionListener);
/*     */     }
/* 273 */     PacketListener packetListener = getPacketListener();
/* 274 */     if (packetListener != null) {
/* 275 */       this.channel.setPacketListener(packetListener);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected URI extractURI(List list) throws JMSException {
/* 281 */     int idx = 0;
/* 282 */     if (list.size() > 1) {
/*     */       do {
/* 284 */         idx = (int)(Math.random() * list.size());
/*     */       }
/* 286 */       while (idx < 0 || idx >= list.size());
/*     */     }
/* 288 */     return list.remove(idx);
/*     */   }
/*     */   
/*     */   protected void attemptToConnect(URI uri) throws JMSException {
/* 292 */     this.channel = TransportChannelProvider.create(this.wireFormat, uri);
/* 293 */     if (this.started.get())
/* 294 */       this.channel.start(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\composite\CompositeTransportChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */