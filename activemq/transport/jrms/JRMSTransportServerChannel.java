/*    */ package org.codehaus.activemq.transport.jrms;
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
/*    */ public class JRMSTransportServerChannel
/*    */   extends TransportServerChannelSupport
/*    */ {
/* 36 */   private static final Log log = LogFactory.getLog(JRMSTransportServerChannel.class);
/*    */   
/*    */   private SynchronizedBoolean started;
/*    */ 
/*    */   
/*    */   public JRMSTransportServerChannel(WireFormat wireFormat, URI bindAddr) {
/* 42 */     super(bindAddr);
/* 43 */     this.started = new SynchronizedBoolean(false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void start() throws JMSException {
/* 53 */     if (this.started.commit(false, true)) {
/* 54 */       log.info("JRMS ServerChannel at: " + getUrl());
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 63 */     return "JRMSTransportServerChannel@" + getUrl();
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\jrms\JRMSTransportServerChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */