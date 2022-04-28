/*    */ package org.codehaus.activemq.transport.tcp;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.InetAddress;
/*    */ import java.net.ServerSocket;
/*    */ import java.net.URI;
/*    */ import javax.jms.JMSException;
/*    */ import javax.net.ServerSocketFactory;
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
/*    */ public class SfTransportServerChannelFactory
/*    */   implements TransportServerChannelFactory
/*    */ {
/*    */   protected static final int BACKLOG = 500;
/*    */   private ServerSocketFactory serverSocketFactory;
/*    */   
/*    */   public SfTransportServerChannelFactory(ServerSocketFactory socketFactory) {
/* 44 */     this.serverSocketFactory = socketFactory;
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
/*    */   public TransportServerChannel create(WireFormat wireFormat, URI bindAddress) throws JMSException {
/* 56 */     ServerSocket serverSocket = null;
/*    */     try {
/* 58 */       serverSocket = createServerSocket(bindAddress);
/*    */     }
/* 60 */     catch (IOException e) {
/* 61 */       JMSException jmsEx = new JMSException("Creation of ServerSocket failed: " + e);
/* 62 */       jmsEx.setLinkedException(e);
/* 63 */       throw jmsEx;
/*    */     } 
/* 65 */     return (TransportServerChannel)new TcpTransportServerChannel(wireFormat, serverSocket);
/*    */   }
/*    */   
/*    */   protected ServerSocket createServerSocket(URI bind) throws IOException {
/* 69 */     String host = bind.getHost();
/* 70 */     host = (host == null || host.length() == 0) ? "localhost" : host;
/*    */     
/* 72 */     InetAddress addr = InetAddress.getByName(host);
/* 73 */     if (addr.equals(InetAddress.getLocalHost())) {
/* 74 */       return this.serverSocketFactory.createServerSocket(bind.getPort(), 500);
/*    */     }
/*    */     
/* 77 */     return this.serverSocketFactory.createServerSocket(bind.getPort(), 500, addr);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\tcp\SfTransportServerChannelFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */