/*    */ package org.codehaus.activemq.transport.multicast;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.DatagramSocket;
/*    */ import java.net.MulticastSocket;
/*    */ import java.net.URI;
/*    */ import javax.jms.JMSException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.codehaus.activemq.message.WireFormat;
/*    */ import org.codehaus.activemq.transport.udp.UdpTransportChannel;
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
/*    */ public class MulticastTransportChannel
/*    */   extends UdpTransportChannel
/*    */ {
/* 38 */   private static final Log log = LogFactory.getLog(MulticastTransportChannel.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean loopbackMode = false;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MulticastTransportChannel(WireFormat wireFormat, URI remoteLocation) throws JMSException {
/* 51 */     super(wireFormat, remoteLocation);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MulticastTransportChannel(WireFormat wireFormat, MulticastSocket socket) throws JMSException {
/* 59 */     super(wireFormat, socket);
/*    */   }
/*    */   
/*    */   public boolean isMulticast() {
/* 63 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 72 */     return "MulticastTransportChannel: " + this.socket;
/*    */   }
/*    */   
/*    */   protected void connect() throws IOException {
/* 76 */     MulticastSocket msocket = (MulticastSocket)this.socket;
/*    */ 
/*    */     
/* 79 */     msocket.setLoopbackMode(this.loopbackMode);
/*    */     
/* 81 */     msocket.joinGroup(this.inetAddress);
/*    */   }
/*    */   
/*    */   protected DatagramSocket createSocket(int port) throws IOException {
/* 85 */     return new MulticastSocket(port);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\multicast\MulticastTransportChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */