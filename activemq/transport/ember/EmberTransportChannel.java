/*     */ package org.codehaus.activemq.transport.ember;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;
/*     */ import java.io.IOException;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.transport.TransportChannelSupport;
/*     */ import pyrasun.eio.EIOGlobalContext;
/*     */ import pyrasun.eio.services.EmberServiceController;
/*     */ import pyrasun.eio.services.EmberServiceException;
/*     */ import pyrasun.eio.services.bytearray.ByteArrayServerClient;
/*     */ import pyrasun.eio.services.bytearray.ByteArrayServerClientListener;
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
/*     */ public class EmberTransportChannel
/*     */   extends TransportChannelSupport
/*     */   implements ByteArrayServerClientListener
/*     */ {
/*  42 */   private static final Log log = LogFactory.getLog(EmberTransportChannel.class);
/*     */   
/*     */   private WireFormat wireFormat;
/*     */   
/*     */   private EIOGlobalContext context;
/*     */   
/*     */   private EmberServiceController controller;
/*     */   
/*     */   private ByteArrayServerClient client;
/*     */   
/*     */   private SynchronizedBoolean closed;
/*     */   
/*     */   private SynchronizedBoolean started;
/*     */   
/*     */   protected EmberTransportChannel(WireFormat wireFormat) {
/*  57 */     this.wireFormat = wireFormat;
/*     */     
/*  59 */     this.closed = new SynchronizedBoolean(false);
/*  60 */     this.started = new SynchronizedBoolean(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EmberTransportChannel(WireFormat wireFormat, EIOGlobalContext context, EmberServiceController controller, ByteArrayServerClient client) {
/*  67 */     this(wireFormat);
/*  68 */     this.context = context;
/*  69 */     this.client = client;
/*  70 */     this.controller = controller;
/*  71 */     client.setListener(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/*  78 */     super.stop();
/*  79 */     if (this.closed.commit(false, true)) {
/*     */       
/*     */       try {
/*  82 */         if (this.controller != null) {
/*  83 */           this.controller.stopAll();
/*     */         }
/*  85 */         if (this.context != null) {
/*  86 */           this.context.stop();
/*     */         }
/*     */       }
/*  89 */       catch (EmberServiceException e) {
/*  90 */         log.error("Caught while closing: " + e, (Throwable)e);
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
/* 101 */     if (this.started.commit(false, true)) {
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 106 */         if (this.context != null) {
/* 107 */           this.context.start();
/*     */         }
/* 109 */         if (this.controller != null) {
/* 110 */           this.controller.startAll();
/*     */         }
/*     */       }
/* 113 */       catch (EmberServiceException e) {
/* 114 */         JMSException jmsEx = new JMSException("Error starting NIO client: " + e.getMessage());
/* 115 */         jmsEx.setLinkedException((Exception)e);
/* 116 */         throw jmsEx;
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
/*     */   public void asyncSend(Packet packet) throws JMSException {
/*     */     try {
/* 130 */       byte[] bytes = this.wireFormat.toBytes(packet);
/*     */       
/* 132 */       synchronized (this.client) {
/* 133 */         this.client.write(bytes);
/*     */       }
/*     */     
/* 136 */     } catch (IOException e) {
/* 137 */       throw createJMSException("Failed to write packet: " + packet + ". ", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMulticast() {
/* 143 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JMSException createJMSException(String message, Exception ex) {
/* 150 */     JMSException jmsEx = new JMSException(message + ex.getMessage());
/* 151 */     jmsEx.setLinkedException(ex);
/* 152 */     return jmsEx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 161 */     return "EmberTransportChannel: " + this.client;
/*     */   }
/*     */   
/*     */   public void newMessage(ByteArrayServerClient client, Object msg) {
/* 165 */     byte[] bytes = (byte[])msg;
/* 166 */     Packet packet = null;
/*     */     try {
/* 168 */       packet = this.wireFormat.fromBytes(bytes);
/* 169 */       doConsumePacket(packet);
/*     */     }
/* 171 */     catch (IOException e) {
/* 172 */       log.error("Could not parse byte[] of size: " + bytes.length + ". Reason: " + e, e);
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
/* 183 */     return this.wireFormat.canProcessWireFormatVersion(version);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCurrentWireFormatVersion() {
/* 190 */     return this.wireFormat.getCurrentWireFormatVersion();
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\ember\EmberTransportChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */