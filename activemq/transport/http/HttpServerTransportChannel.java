/*    */ package org.codehaus.activemq.transport.http;
/*    */ 
/*    */ import EDU.oswego.cs.dl.util.concurrent.Channel;
/*    */ import javax.jms.JMSException;
/*    */ import org.codehaus.activemq.message.Packet;
/*    */ import org.codehaus.activemq.transport.TransportChannelSupport;
/*    */ import org.codehaus.activemq.util.JMSExceptionHelper;
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
/*    */ public class HttpServerTransportChannel
/*    */   extends TransportChannelSupport
/*    */ {
/*    */   private Channel channel;
/*    */   
/*    */   public HttpServerTransportChannel(Channel channel) {
/* 38 */     this.channel = channel;
/*    */   }
/*    */   
/*    */   public Channel getChannel() {
/* 42 */     return this.channel;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void start() throws JMSException {}
/*    */ 
/*    */   
/*    */   public void asyncSend(Packet packet) throws JMSException {
/*    */     try {
/* 52 */       this.channel.put(packet);
/*    */     }
/* 54 */     catch (InterruptedException e) {
/* 55 */       throw JMSExceptionHelper.newJMSException("Failed to send: " + packet + ". Reason: " + e, e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean isMulticast() {
/* 60 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canProcessWireFormatVersion(int version) {
/* 69 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getCurrentWireFormatVersion() {
/* 76 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\http\HttpServerTransportChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */