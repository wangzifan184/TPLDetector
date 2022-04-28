/*    */ package org.codehaus.activemq.transport.zeroconf;
/*    */ 
/*    */ import java.net.URI;
/*    */ import org.codehaus.activemq.message.WireFormat;
/*    */ import org.codehaus.activemq.transport.TransportServerChannelSupport;
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
/*    */ public class ZeroconfTransportConnector
/*    */   extends TransportServerChannelSupport
/*    */ {
/*    */   public ZeroconfTransportConnector(WireFormat wireFormat, URI bindAddr) {
/* 33 */     super(bindAddr);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 37 */     return "ZeroconfTransportConnector@" + getUrl();
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\zeroconf\ZeroconfTransportConnector.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */