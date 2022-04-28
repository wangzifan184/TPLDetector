/*    */ package org.codehaus.activemq.transport.tcp;
/*    */ 
/*    */ import EDU.oswego.cs.dl.util.concurrent.Executor;
/*    */ import java.io.IOException;
/*    */ import java.net.InetAddress;
/*    */ import java.net.Socket;
/*    */ import java.net.URI;
/*    */ import javax.jms.JMSException;
/*    */ import javax.net.SocketFactory;
/*    */ import org.codehaus.activemq.message.WireFormat;
/*    */ import org.codehaus.activemq.transport.TransportChannel;
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
/*    */ public class SfTransportChannelFactory
/*    */   extends TcpTransportChannelFactory
/*    */ {
/*    */   private SocketFactory socketFactory;
/*    */   private Executor executor;
/*    */   
/*    */   public SfTransportChannelFactory(SocketFactory socketFactory) {
/* 42 */     this.socketFactory = socketFactory;
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
/*    */   public TransportChannel create(WireFormat wireFormat, URI remoteLocation) throws JMSException {
/* 54 */     Socket socket = null;
/*    */     try {
/* 56 */       socket = createSocket(remoteLocation);
/*    */     }
/* 58 */     catch (IOException e) {
/* 59 */       JMSException jmsEx = new JMSException("Creation of Socket failed: " + e);
/* 60 */       jmsEx.setLinkedException(e);
/* 61 */       throw jmsEx;
/*    */     } 
/* 63 */     return populateProperties((TransportChannel)new TcpTransportChannel(wireFormat, socket, this.executor), remoteLocation);
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
/* 77 */     Socket socket = null;
/*    */     try {
/* 79 */       socket = createSocket(remoteLocation, localLocation);
/*    */     }
/* 81 */     catch (IOException e) {
/* 82 */       JMSException jmsEx = new JMSException("Creation of Socket failed: " + e);
/* 83 */       jmsEx.setLinkedException(e);
/* 84 */       throw jmsEx;
/*    */     } 
/* 86 */     return populateProperties((TransportChannel)new TcpTransportChannel(wireFormat, socket, this.executor), remoteLocation);
/*    */   }
/*    */   
/*    */   protected Socket createSocket(URI remoteLocation) throws IOException {
/* 90 */     return this.socketFactory.createSocket(remoteLocation.getHost(), remoteLocation.getPort());
/*    */   }
/*    */   
/*    */   protected Socket createSocket(URI remoteLocation, URI localLocation) throws IOException {
/* 94 */     return this.socketFactory.createSocket(remoteLocation.getHost(), remoteLocation.getPort(), InetAddress.getByName(localLocation.getHost()), localLocation.getPort());
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\tcp\SfTransportChannelFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */