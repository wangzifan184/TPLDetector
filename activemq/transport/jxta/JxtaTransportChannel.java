/*    */ package org.codehaus.activemq.transport.jxta;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.Socket;
/*    */ import java.net.URI;
/*    */ import java.net.UnknownHostException;
/*    */ import javax.jms.JMSException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.codehaus.activemq.message.WireFormat;
/*    */ import org.codehaus.activemq.transport.tcp.TcpTransportChannel;
/*    */ import org.p2psockets.P2PInetAddress;
/*    */ import org.p2psockets.P2PSocket;
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
/*    */ public class JxtaTransportChannel
/*    */   extends TcpTransportChannel
/*    */ {
/* 40 */   private static final Log log = LogFactory.getLog(JxtaTransportChannel.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JxtaTransportChannel(WireFormat wireFormat, URI remoteLocation) throws JMSException {
/* 49 */     super(wireFormat, remoteLocation);
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
/*    */   public JxtaTransportChannel(WireFormat wireFormat, URI remoteLocation, URI localLocation) throws JMSException {
/* 61 */     super(wireFormat, localLocation, remoteLocation);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 70 */     return "P2pTransportChannel: " + this.socket;
/*    */   }
/*    */   
/*    */   protected Socket createSocket(URI remoteLocation) throws UnknownHostException, IOException {
/* 74 */     return (Socket)new P2PSocket(remoteLocation.getHost(), remoteLocation.getPort());
/*    */   }
/*    */   
/*    */   protected Socket createSocket(URI remoteLocation, URI localLocation) throws IOException, UnknownHostException {
/* 78 */     return (Socket)new P2PSocket(remoteLocation.getHost(), remoteLocation.getPort(), P2PInetAddress.getByName(localLocation.getHost()), localLocation.getPort());
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\jxta\JxtaTransportChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */