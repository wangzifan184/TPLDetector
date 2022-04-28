/*    */ package org.codehaus.activemq.transport.ember;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import javax.jms.JMSException;
/*    */ import org.codehaus.activemq.message.WireFormat;
/*    */ import org.codehaus.activemq.transport.TransportServerChannel;
/*    */ import org.codehaus.activemq.transport.TransportServerChannelFactory;
/*    */ import pyrasun.eio.EIOGlobalContext;
/*    */ import pyrasun.eio.EIOPoolingStrategy;
/*    */ import pyrasun.eio.services.EmberService;
/*    */ import pyrasun.eio.services.EmberServiceController;
/*    */ import pyrasun.eio.services.bytearray.ByteArrayServerService;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EmberTransportServerChannelFactory
/*    */   extends EmberSupport
/*    */   implements TransportServerChannelFactory
/*    */ {
/*    */   private EIOPoolingStrategy acceptPoolingStrategy;
/* 40 */   private String acceptPoolingStrategyName = "BLOCKING_ACCEPTOR";
/*    */ 
/*    */   
/*    */   public EmberTransportServerChannelFactory() {}
/*    */   
/*    */   public EmberTransportServerChannelFactory(EIOGlobalContext context, EIOPoolingStrategy ioPoolingStrategy, EIOPoolingStrategy acceptPoolingStrategy) {
/* 46 */     super(context, ioPoolingStrategy);
/* 47 */     this.acceptPoolingStrategy = acceptPoolingStrategy;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TransportServerChannel create(WireFormat wireFormat, URI bindAddress) throws JMSException {
/*    */     try {
/* 60 */       EmberServiceController controller = getController();
/* 61 */       ByteArrayServerService service = new ByteArrayServerService(getContext(), getAcceptPoolingStrategy(), getIoPoolingStrategy(), bindAddress.getHost(), bindAddress.getPort());
/* 62 */       controller.addService((EmberService)service);
/*    */       
/* 64 */       EmberTransportServerChannel answer = new EmberTransportServerChannel(wireFormat, bindAddress, getContext(), controller);
/* 65 */       service.setListener(answer);
/* 66 */       return (TransportServerChannel)answer;
/*    */     }
/* 68 */     catch (IOException e) {
/* 69 */       throw createJMSException("Initialization of TransportServerChannel failed: ", e);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected EIOPoolingStrategy getAcceptPoolingStrategy() {
/* 74 */     if (this.acceptPoolingStrategy == null) {
/* 75 */       this.acceptPoolingStrategy = getPoolingStrategyByName(this.acceptPoolingStrategyName);
/*    */     }
/* 77 */     return this.acceptPoolingStrategy;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\ember\EmberTransportServerChannelFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */