/*    */ package org.codehaus.activemq.transport.ssl;
/*    */ 
/*    */ import javax.net.ssl.SSLServerSocketFactory;
/*    */ import org.codehaus.activemq.transport.tcp.SfTransportServerChannelFactory;
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
/*    */ public class SslTransportServerChannelFactory
/*    */   extends SfTransportServerChannelFactory
/*    */ {
/*    */   public SslTransportServerChannelFactory() {
/* 32 */     super(SSLServerSocketFactory.getDefault());
/*    */   }
/*    */   
/*    */   public SslTransportServerChannelFactory(SSLServerSocketFactory socketFactory) {
/* 36 */     super(socketFactory);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\ssl\SslTransportServerChannelFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */