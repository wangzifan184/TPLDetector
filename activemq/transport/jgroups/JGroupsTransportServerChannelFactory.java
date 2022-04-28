/*    */ package org.codehaus.activemq.transport.jgroups;
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
/*    */ public class JGroupsTransportServerChannelFactory
/*    */   implements TransportServerChannelFactory
/*    */ {
/*    */   public TransportServerChannel create(WireFormat wireFormat, URI bindAddress) throws JMSException {
/* 35 */     return (TransportServerChannel)new JGroupsTransportServerChannel(bindAddress);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\jgroups\JGroupsTransportServerChannelFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */