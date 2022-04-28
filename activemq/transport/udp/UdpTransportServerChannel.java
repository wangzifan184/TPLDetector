/*    */ package org.codehaus.activemq.transport.udp;
/*    */ 
/*    */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;
/*    */ import java.net.URI;
/*    */ import javax.jms.JMSException;
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
/*    */ public class UdpTransportServerChannel
/*    */   extends TransportServerChannelSupport
/*    */ {
/* 35 */   private static final Log log = LogFactory.getLog(UdpTransportServerChannel.class);
/*    */   
/*    */   private SynchronizedBoolean started;
/*    */   
/*    */   public UdpTransportServerChannel(URI bindAddr) {
/* 40 */     super(bindAddr);
/* 41 */     this.started = new SynchronizedBoolean(false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void start() throws JMSException {
/* 50 */     if (this.started.commit(false, true)) {
/* 51 */       log.info("ServerChannel at: " + getUrl());
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 59 */     return "UdpTransportServerChannel@" + getUrl();
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transpor\\udp\UdpTransportServerChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */