/*    */ package org.codehaus.activemq.transport.jxta;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.InetAddress;
/*    */ import java.net.ServerSocket;
/*    */ import java.net.URI;
/*    */ import java.net.UnknownHostException;
/*    */ import javax.jms.JMSException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.codehaus.activemq.message.WireFormat;
/*    */ import org.codehaus.activemq.transport.tcp.TcpTransportServerChannel;
/*    */ import org.p2psockets.P2PInetAddress;
/*    */ import org.p2psockets.P2PServerSocket;
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
/*    */ public class JxtaTransportServerChannel
/*    */   extends TcpTransportServerChannel
/*    */ {
/* 41 */   private static final Log log = LogFactory.getLog(JxtaTransportServerChannel.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JxtaTransportServerChannel(WireFormat wireFormat, URI bindAddr) throws JMSException {
/* 50 */     super(wireFormat, bindAddr);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return "P2pTransportServerChannel@" + getUrl();
/*    */   }
/*    */   protected ServerSocket createServerSocket(URI bind) throws UnknownHostException, IOException {
/*    */     P2PServerSocket p2PServerSocket;
/* 61 */     ServerSocket answer = null;
/* 62 */     String host = bind.getHost();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 69 */     if (host == null || host.length() == 0 || host.equals("localhost")) {
/* 70 */       InetAddress addr = P2PInetAddress.getLocalHost();
/* 71 */       p2PServerSocket = new P2PServerSocket(bind.getPort(), getBacklog(), addr);
/*    */     } else {
/*    */       
/* 74 */       InetAddress addr = P2PInetAddress.getByName(host);
/* 75 */       p2PServerSocket = new P2PServerSocket(bind.getPort(), getBacklog(), addr);
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 86 */     return (ServerSocket)p2PServerSocket;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\jxta\JxtaTransportServerChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */