/*     */ package org.codehaus.activemq.transport;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.CopyOnWriteArrayList;
/*     */ import EDU.oswego.cs.dl.util.concurrent.Executor;
/*     */ import java.net.URI;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.jms.ExceptionListener;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.UnsupportedWireFormatException;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.PacketListener;
/*     */ import org.codehaus.activemq.message.Receipt;
/*     */ import org.codehaus.activemq.message.ReceiptHolder;
/*     */ import org.codehaus.activemq.message.WireFormatInfo;
/*     */ import org.codehaus.activemq.util.ExecutorHelper;
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
/*     */ public abstract class TransportChannelSupport
/*     */   implements TransportChannel
/*     */ {
/*  44 */   private static final Log log = LogFactory.getLog(TransportChannelSupport.class);
/*  45 */   private CopyOnWriteArrayList listeners = new CopyOnWriteArrayList();
/*  46 */   private HashMap requestMap = new HashMap();
/*     */   
/*     */   private PacketListener packetListener;
/*     */   
/*     */   private ExceptionListener exceptionListener;
/*     */   
/*     */   private String clientID;
/*     */   
/*     */   private TransportChannelListener transportChannelListener;
/*     */   
/*     */   private boolean serverSide;
/*     */   protected boolean pendingStop = false;
/*     */   protected boolean transportConnected = true;
/*     */   
/*     */   public void setPendingStop(boolean pendingStop) {
/*  61 */     this.pendingStop = pendingStop;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPendingStop() {
/*  68 */     return this.pendingStop;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/*  75 */     this.transportConnected = false;
/*  76 */     Map map = (Map)this.requestMap.clone();
/*  77 */     for (Iterator i = map.values().iterator(); i.hasNext(); ) {
/*  78 */       ReceiptHolder rh = i.next();
/*  79 */       rh.close();
/*     */     } 
/*  81 */     map.clear();
/*  82 */     this.requestMap.clear();
/*  83 */     if (this.transportChannelListener != null) {
/*  84 */       this.transportChannelListener.removeClient(this);
/*     */     }
/*  86 */     this.exceptionListener = null;
/*  87 */     this.packetListener = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Receipt send(Packet packet) throws JMSException {
/*  98 */     return send(packet, 0);
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
/*     */   public Receipt send(Packet packet, int timeout) throws JMSException {
/* 110 */     ReceiptHolder rh = new ReceiptHolder();
/* 111 */     this.requestMap.put(packet.getId(), rh);
/* 112 */     doAsyncSend(packet);
/* 113 */     Receipt result = rh.getReceipt(timeout);
/* 114 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TransportChannelListener getTransportChannelListener() {
/* 123 */     return this.transportChannelListener;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTransportChannelListener(TransportChannelListener transportChannelListener) {
/* 130 */     this.transportChannelListener = transportChannelListener;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTransportStatusEventListener(TransportStatusEventListener listener) {
/* 139 */     this.listeners.add(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeTransportStatusEventListener(TransportStatusEventListener listener) {
/* 148 */     this.listeners.remove(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClientID() {
/* 155 */     return this.clientID;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClientID(String clientID) {
/* 162 */     this.clientID = clientID;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExceptionListener getExceptionListener() {
/* 169 */     return this.exceptionListener;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PacketListener getPacketListener() {
/* 176 */     return this.packetListener;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPacketListener(PacketListener l) {
/* 185 */     this.packetListener = l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExceptionListener(ExceptionListener listener) {
/* 194 */     this.exceptionListener = listener;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isServerSide() {
/* 201 */     return this.serverSide;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServerSide(boolean serverSide) {
/* 208 */     this.serverSide = serverSide;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTransportConnected() {
/* 216 */     return this.transportConnected;
/*     */   }
/*     */   
/*     */   protected void setTransportConnected(boolean value) {
/* 220 */     this.transportConnected = value;
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
/*     */   protected void doConsumePacket(Packet packet) {
/* 232 */     doConsumePacket(packet, this.packetListener);
/*     */   }
/*     */   
/*     */   protected void doConsumePacket(Packet packet, PacketListener listener) {
/* 236 */     if (!doHandleReceipt(packet) && !doHandleWireFormat(packet)) {
/* 237 */       if (listener != null) {
/* 238 */         listener.consume(packet);
/*     */       } else {
/*     */         
/* 241 */         log.warn("No packet listener set to receive packets");
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean doHandleReceipt(Packet packet) {
/* 247 */     boolean result = false;
/* 248 */     if (packet != null && 
/* 249 */       packet.isReceipt()) {
/* 250 */       result = true;
/* 251 */       Receipt receipt = (Receipt)packet;
/* 252 */       ReceiptHolder rh = (ReceiptHolder)this.requestMap.remove(receipt.getCorrelationId());
/* 253 */       if (rh != null) {
/* 254 */         rh.setReceipt(receipt);
/*     */       } else {
/*     */         
/* 257 */         log.warn("No Packet found to match Receipt correlationId: " + receipt.getCorrelationId());
/*     */       } 
/*     */     } 
/*     */     
/* 261 */     return result;
/*     */   }
/*     */   
/*     */   protected boolean doHandleWireFormat(Packet packet) {
/* 265 */     boolean handled = false;
/* 266 */     if (packet.getPacketType() == 29) {
/* 267 */       handled = true;
/* 268 */       WireFormatInfo info = (WireFormatInfo)packet;
/* 269 */       if (!canProcessWireFormatVersion(info.getVersion())) {
/* 270 */         setPendingStop(true);
/* 271 */         String errorStr = "Cannot process wire format of version: " + info.getVersion();
/* 272 */         TransportStatusEvent event = new TransportStatusEvent();
/* 273 */         event.setChannelStatus(4);
/* 274 */         fireStatusEvent(event);
/* 275 */         onAsyncException((JMSException)new UnsupportedWireFormatException(errorStr));
/* 276 */         stop();
/*     */       
/*     */       }
/* 279 */       else if (log.isDebugEnabled()) {
/* 280 */         log.debug(this + " using wire format version: " + info.getVersion());
/*     */       } 
/*     */     } 
/*     */     
/* 284 */     return handled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doAsyncSend(Packet packet) throws JMSException {
/* 295 */     asyncSend(packet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onAsyncException(JMSException e) {
/* 304 */     if (this.exceptionListener != null) {
/* 305 */       this.transportConnected = false;
/* 306 */       this.exceptionListener.onException(e);
/*     */     } else {
/*     */       
/* 309 */       log.warn("Caught exception dispatching message and no ExceptionListener registered: " + e, (Throwable)e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void fireStatusEvent(URI remoteURI, int status) {
/* 320 */     TransportStatusEvent event = new TransportStatusEvent();
/* 321 */     event.setChannelStatus(status);
/* 322 */     event.setRemoteURI(remoteURI);
/* 323 */     fireStatusEvent(event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void fireStatusEvent(TransportStatusEvent event) {
/* 332 */     if (event != null) {
/* 333 */       for (Iterator i = this.listeners.iterator(); i.hasNext(); ) {
/* 334 */         TransportStatusEventListener l = i.next();
/* 335 */         l.statusChanged(event);
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
/*     */   
/*     */   protected void stopExecutor(Executor executor) throws InterruptedException, JMSException {
/* 348 */     ExecutorHelper.stopExecutor(executor);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\TransportChannelSupport.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */