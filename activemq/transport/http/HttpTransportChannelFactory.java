/*    */ package org.codehaus.activemq.transport.http;
/*    */ 
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import javax.jms.JMSException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.codehaus.activemq.message.TextWireFormat;
/*    */ import org.codehaus.activemq.message.WireFormat;
/*    */ import org.codehaus.activemq.transport.TransportChannel;
/*    */ import org.codehaus.activemq.transport.TransportChannelFactorySupport;
/*    */ import org.codehaus.activemq.transport.xstream.XStreamWireFormat;
/*    */ import org.codehaus.activemq.util.JMSExceptionHelper;
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
/*    */ public class HttpTransportChannelFactory
/*    */   extends TransportChannelFactorySupport
/*    */ {
/* 38 */   private static final Log log = LogFactory.getLog(HttpTransportChannelFactory.class);
/*    */   
/*    */   public TransportChannel create(WireFormat wireFormat, URI remoteLocation) throws JMSException {
/*    */     try {
/* 42 */       return create(wireFormat, remoteLocation, new URI("http://localhost:0"));
/*    */     }
/* 44 */     catch (URISyntaxException e) {
/* 45 */       throw JMSExceptionHelper.newJMSException(e.getMessage(), e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public TransportChannel create(WireFormat wireFormat, URI remoteLocation, URI localLocation) throws JMSException {
/*    */     try {
/* 51 */       HttpTransportChannel channel = new HttpTransportChannel(asTextWireFormat(wireFormat), remoteLocation.toString());
/* 52 */       return populateProperties((TransportChannel)channel, remoteLocation);
/*    */     }
/* 54 */     catch (MalformedURLException e) {
/* 55 */       throw JMSExceptionHelper.newJMSException(e.getMessage(), e);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected TextWireFormat asTextWireFormat(WireFormat wireFormat) {
/* 60 */     if (wireFormat instanceof TextWireFormat) {
/* 61 */       return (TextWireFormat)wireFormat;
/*    */     }
/* 63 */     log.trace("Not created with a TextWireFromat: " + wireFormat);
/* 64 */     return (TextWireFormat)new XStreamWireFormat();
/*    */   }
/*    */   
/*    */   public boolean requiresEmbeddedBroker() {
/* 68 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\http\HttpTransportChannelFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */