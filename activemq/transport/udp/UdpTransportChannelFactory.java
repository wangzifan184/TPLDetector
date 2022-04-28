/*    */ package org.codehaus.activemq.transport.udp;
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
/*    */ public class UdpTransportChannelFactory
/*    */   extends TransportChannelFactorySupport
/*    */ {
/*    */   public TransportChannel create(WireFormat wireFormat, URI remoteLocation) throws JMSException {
/* 43 */     return populateProperties((TransportChannel)new UdpTransportChannel(wireFormat, remoteLocation), remoteLocation);
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
/* 57 */     return create(wireFormat, remoteLocation);
/*    */   }
/*    */   
/*    */   public boolean requiresEmbeddedBroker() {
/* 61 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transpor\\udp\UdpTransportChannelFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */