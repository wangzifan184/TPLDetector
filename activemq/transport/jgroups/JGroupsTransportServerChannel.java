/*    */ package org.codehaus.activemq.transport.jgroups;
/*    */ 
/*    */ import java.net.URI;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
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
/*    */ public class JGroupsTransportServerChannel
/*    */   extends TransportServerChannelSupport
/*    */ {
/* 33 */   private static final Log log = LogFactory.getLog(JGroupsTransportServerChannel.class);
/*    */   
/*    */   public JGroupsTransportServerChannel(URI bindAddr) {
/* 36 */     super(bindAddr);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 40 */     return "JGroupsTransportServerChannel@" + getUrl();
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\jgroups\JGroupsTransportServerChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */