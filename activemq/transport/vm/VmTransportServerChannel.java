/*    */ package org.codehaus.activemq.transport.vm;
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
/*    */ public class VmTransportServerChannel
/*    */   extends TransportServerChannelSupport
/*    */ {
/* 35 */   private static final Log log = LogFactory.getLog(VmTransportServerChannel.class);
/*    */   
/*    */   private SynchronizedBoolean started;
/*    */   
/*    */   public VmTransportServerChannel(URI bindAddr) {
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
/* 51 */       log.info("Listening for connections at: " + getUrl());
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 59 */     return "VmTransportServerChannel@" + getUrl();
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\vm\VmTransportServerChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */