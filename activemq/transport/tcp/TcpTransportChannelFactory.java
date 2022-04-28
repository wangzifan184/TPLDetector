/*    */ package org.codehaus.activemq.transport.tcp;
/*    */ 
/*    */ import java.net.URI;
/*    */ import javax.jms.JMSException;
/*    */ import org.codehaus.activemq.message.WireFormat;
/*    */ import org.codehaus.activemq.transport.TransportChannel;
/*    */ import org.codehaus.activemq.transport.TransportChannelFactorySupport;
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
/*    */ public class TcpTransportChannelFactory
/*    */   extends TransportChannelFactorySupport
/*    */ {
/*    */   public TransportChannel create(WireFormat wireFormat, URI remoteLocation) throws JMSException {
/* 43 */     return populateProperties((TransportChannel)new TcpTransportChannel(wireFormat, remoteLocation), remoteLocation);
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
/*    */ 
/*    */   
/*    */   public TransportChannel create(WireFormat wireFormat, URI remoteLocation, URI localLocation) throws JMSException {
/* 57 */     return populateProperties((TransportChannel)new TcpTransportChannel(wireFormat, localLocation, remoteLocation), remoteLocation);
/*    */   }
/*    */   
/*    */   public boolean requiresEmbeddedBroker() {
/* 61 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\tcp\TcpTransportChannelFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */