/*    */ package org.codehaus.activemq.transport.jrms;
/*    */ 
/*    */ import java.net.URI;
/*    */ import javax.jms.JMSException;
/*    */ import org.codehaus.activemq.message.WireFormat;
/*    */ import org.codehaus.activemq.transport.TransportServerChannel;
/*    */ import org.codehaus.activemq.transport.TransportServerChannelFactory;
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
/*    */ public class JRMSTransportServerChannelFactory
/*    */   implements TransportServerChannelFactory
/*    */ {
/*    */   public TransportServerChannel create(WireFormat wireFormat, URI bindAddress) throws JMSException {
/* 43 */     return (TransportServerChannel)new JRMSTransportServerChannel(wireFormat, bindAddress);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\jrms\JRMSTransportServerChannelFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */