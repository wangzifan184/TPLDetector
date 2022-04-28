/*    */ package org.codehaus.activemq.transport;
/*    */ 
/*    */ import java.net.URI;
/*    */ import javax.jms.JMSException;
/*    */ import org.codehaus.activemq.message.WireFormat;
/*    */ import org.codehaus.activemq.transport.composite.CompositeTransportChannelFactory;
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
/*    */ public abstract class DiscoveryTransportChannelFactorySupport
/*    */   extends CompositeTransportChannelFactory
/*    */ {
/*    */   private DiscoveryAgent discoveryAgent;
/*    */   
/*    */   public TransportChannel create(WireFormat wireFormat, URI remoteLocation) throws JMSException {
/* 33 */     if (this.discoveryAgent == null) {
/* 34 */       this.discoveryAgent = createDiscoveryAgent(remoteLocation);
/*    */     }
/* 36 */     return (TransportChannel)new DiscoveryTransportChannel(wireFormat, this.discoveryAgent);
/*    */   }
/*    */   
/*    */   public DiscoveryAgent getDiscoveryAgent() {
/* 40 */     return this.discoveryAgent;
/*    */   }
/*    */   
/*    */   public void setDiscoveryAgent(DiscoveryAgent discoveryAgent) {
/* 44 */     this.discoveryAgent = discoveryAgent;
/*    */   }
/*    */   
/*    */   protected abstract DiscoveryAgent createDiscoveryAgent(URI paramURI);
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\DiscoveryTransportChannelFactorySupport.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */