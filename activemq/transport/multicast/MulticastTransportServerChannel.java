/*    */ package org.codehaus.activemq.transport.multicast;
/*    */ 
/*    */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;
/*    */ import java.net.URI;
/*    */ import javax.jms.JMSException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.codehaus.activemq.message.WireFormat;
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
/*    */ public class MulticastTransportServerChannel
/*    */   extends TransportServerChannelSupport
/*    */ {
/* 36 */   private static final Log log = LogFactory.getLog(MulticastTransportServerChannel.class);
/*    */   
/*    */   protected URI bindAddress;
/*    */   private SynchronizedBoolean started;
/*    */   
/*    */   public MulticastTransportServerChannel(WireFormat wireFormat, URI bindAddr) {
/* 42 */     super(bindAddr);
/* 43 */     this.bindAddress = bindAddr;
/* 44 */     this.started = new SynchronizedBoolean(false);
/* 45 */     log.info("ServerChannel at: " + this.bindAddress);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void start() throws JMSException {
/* 55 */     if (this.started.commit(false, true));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 63 */     return "MulticastTransportServerChannel@" + this.bindAddress;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\multicast\MulticastTransportServerChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */