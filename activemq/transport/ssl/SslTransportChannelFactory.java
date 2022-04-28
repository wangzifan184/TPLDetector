/*    */ package org.codehaus.activemq.transport.ssl;
/*    */ 
/*    */ import javax.net.ssl.SSLSocketFactory;
/*    */ import org.codehaus.activemq.transport.tcp.SfTransportChannelFactory;
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
/*    */ public class SslTransportChannelFactory
/*    */   extends SfTransportChannelFactory
/*    */ {
/*    */   public SslTransportChannelFactory() {
/* 32 */     super(SSLSocketFactory.getDefault());
/*    */   }
/*    */   
/*    */   public SslTransportChannelFactory(SSLSocketFactory socketFactory) {
/* 36 */     super(socketFactory);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\ssl\SslTransportChannelFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */