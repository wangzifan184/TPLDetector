/*     */ package org.codehaus.activemq.transport.ember;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.transport.TransportChannel;
/*     */ import org.codehaus.activemq.transport.TransportChannelFactory;
/*     */ import org.codehaus.activemq.util.IdGenerator;
/*     */ import pyrasun.eio.EIOGlobalContext;
/*     */ import pyrasun.eio.EIOPoolingStrategy;
/*     */ import pyrasun.eio.services.EmberService;
/*     */ import pyrasun.eio.services.EmberServiceController;
/*     */ import pyrasun.eio.services.EmberServiceException;
/*     */ import pyrasun.eio.services.bytearray.ByteArrayClientService;
/*     */ import pyrasun.eio.services.bytearray.ByteArrayServerClient;
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
/*     */ public class EmberTransportChannelFactory
/*     */   extends EmberSupport
/*     */   implements TransportChannelFactory
/*     */ {
/*  44 */   protected static final Log log = LogFactory.getLog(EmberTransportChannelFactory.class);
/*     */   
/*  46 */   private IdGenerator idGenerator = new IdGenerator();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EmberTransportChannelFactory(EIOGlobalContext context, EIOPoolingStrategy ioPoolingStrategy) {
/*  52 */     super(context, ioPoolingStrategy);
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
/*     */   public TransportChannel create(WireFormat wireFormat, URI remoteLocation) throws JMSException {
/*     */     try {
/*  65 */       String id = this.idGenerator.generateId();
/*  66 */       EmberServiceController controller = getController();
/*     */       
/*  68 */       ByteArrayServerClient client = createClient(controller, remoteLocation, id);
/*  69 */       return (TransportChannel)new EmberTransportChannel(wireFormat, getContext(), controller, client);
/*     */     }
/*  71 */     catch (IOException ioe) {
/*  72 */       JMSException jmsEx = new JMSException("Initialization of TransportChannel failed: " + ioe);
/*  73 */       jmsEx.setLinkedException(ioe);
/*  74 */       throw jmsEx;
/*     */     }
/*  76 */     catch (EmberServiceException e) {
/*  77 */       JMSException jmsEx = new JMSException("Initialization of TransportChannel failed: " + e);
/*  78 */       jmsEx.setLinkedException((Exception)e);
/*  79 */       throw jmsEx;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public TransportChannel create(WireFormat wireFormat, URI remoteLocation, URI localLocation) throws JMSException {
/*  94 */     return create(wireFormat, remoteLocation);
/*     */   }
/*     */   
/*     */   public boolean requiresEmbeddedBroker() {
/*  98 */     return false;
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
/*     */   protected ByteArrayServerClient createClient(EmberServiceController controller, URI remoteLocation, String id) throws JMSException, EmberServiceException, IOException {
/* 111 */     ByteArrayClientService service = createNioService(controller);
/* 112 */     ByteArrayServerClient client = service.createClient(remoteLocation.getHost(), remoteLocation.getPort(), id, null);
/* 113 */     return client;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ByteArrayClientService createNioService(EmberServiceController controller) throws JMSException {
/*     */     ByteArrayClientService service;
/*     */     try {
/* 124 */       service = new ByteArrayClientService(getContext(), getIoPoolingStrategy());
/* 125 */       controller.addService((EmberService)service);
/*     */     }
/* 127 */     catch (IOException e) {
/* 128 */       throw createJMSException("Creation of NIO service failed: ", e);
/*     */     } 
/* 130 */     return service;
/*     */   }
/*     */   
/*     */   public EmberTransportChannelFactory() {}
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\ember\EmberTransportChannelFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */