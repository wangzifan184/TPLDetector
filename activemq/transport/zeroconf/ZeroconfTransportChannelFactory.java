/*    */ package org.codehaus.activemq.transport.zeroconf;
/*    */ 
/*    */ import java.net.URI;
/*    */ import org.codehaus.activemq.transport.DiscoveryAgent;
/*    */ import org.codehaus.activemq.transport.DiscoveryTransportChannelFactorySupport;
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
/*    */ public class ZeroconfTransportChannelFactory
/*    */   extends DiscoveryTransportChannelFactorySupport
/*    */ {
/*    */   protected DiscoveryAgent createDiscoveryAgent(URI remoteLocation) {
/* 30 */     ZeroconfDiscoveryAgent answer = new ZeroconfDiscoveryAgent();
/* 31 */     answer.setType(remoteLocation.getSchemeSpecificPart());
/* 32 */     return (DiscoveryAgent)answer;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\zeroconf\ZeroconfTransportChannelFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */