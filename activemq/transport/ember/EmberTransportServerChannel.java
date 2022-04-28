/*     */ package org.codehaus.activemq.transport.ember;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;
/*     */ import java.net.URI;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.transport.TransportChannel;
/*     */ import org.codehaus.activemq.transport.TransportServerChannelSupport;
/*     */ import org.codehaus.activemq.util.JMSExceptionHelper;
/*     */ import pyrasun.eio.EIOGlobalContext;
/*     */ import pyrasun.eio.services.EmberServiceController;
/*     */ import pyrasun.eio.services.EmberServiceException;
/*     */ import pyrasun.eio.services.bytearray.ByteArrayServerClient;
/*     */ import pyrasun.eio.services.bytearray.ByteArrayServerClientListener;
/*     */ import pyrasun.eio.services.bytearray.ByteArrayServerListener;
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
/*     */ public class EmberTransportServerChannel
/*     */   extends TransportServerChannelSupport
/*     */   implements ByteArrayServerListener, ByteArrayServerClientListener
/*     */ {
/*  43 */   private static final Log log = LogFactory.getLog(EmberTransportServerChannel.class);
/*     */   
/*     */   private WireFormat wireFormat;
/*     */   private EIOGlobalContext context;
/*     */   private EmberServiceController controller;
/*     */   private SynchronizedBoolean closed;
/*     */   private SynchronizedBoolean started;
/*     */   
/*     */   public EmberTransportServerChannel(WireFormat wireFormat, URI bindAddr, EIOGlobalContext context, EmberServiceController controller) {
/*  52 */     super(bindAddr);
/*  53 */     this.wireFormat = wireFormat;
/*  54 */     this.context = context;
/*  55 */     this.controller = controller;
/*  56 */     this.closed = new SynchronizedBoolean(false);
/*  57 */     this.started = new SynchronizedBoolean(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() throws JMSException {
/*  66 */     super.start();
/*  67 */     if (this.started.commit(false, true)) {
/*  68 */       log.info("EmberTransportServerChannel at: " + getUrl());
/*     */       try {
/*  70 */         this.context.start();
/*  71 */         this.controller.startAll();
/*     */       }
/*  73 */       catch (EmberServiceException e) {
/*  74 */         JMSException jmsEx = new JMSException("Could not start EmberIOController: " + e);
/*  75 */         jmsEx.setLinkedException((Exception)e);
/*  76 */         throw jmsEx;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() throws JMSException {
/*  85 */     if (this.closed.commit(false, true)) {
/*     */       try {
/*  87 */         this.controller.stopAll();
/*  88 */         this.context.stop();
/*     */       }
/*  90 */       catch (EmberServiceException e) {
/*  91 */         throw JMSExceptionHelper.newJMSException("Failed to stop: " + e, e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 100 */     return "EmberTransportServerChannel@" + getUrl();
/*     */   }
/*     */   
/*     */   protected void handleException(ByteArrayServerClient client, JMSException e) {
/* 104 */     log.error("Could not create new TransportChannel for client: " + client, (Throwable)e);
/*     */   }
/*     */   
/*     */   public void newClient(ByteArrayServerClient client) {
/* 108 */     log.trace("New client received!");
/*     */     
/* 110 */     addClient((TransportChannel)new EmberTransportChannel(this.wireFormat, null, null, client));
/*     */   }
/*     */   
/*     */   public void clientClosed(ByteArrayServerClient client) {
/* 114 */     log.info("Client has disconnected: " + client);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void newMessage(ByteArrayServerClient byteArrayServerClient, Object msg) {
/* 120 */     log.warn("New message received!: " + msg);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\ember\EmberTransportServerChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */