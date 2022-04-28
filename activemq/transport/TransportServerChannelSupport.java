/*    */ package org.codehaus.activemq.transport;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import javax.jms.JMSException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
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
/*    */ public abstract class TransportServerChannelSupport
/*    */   implements TransportServerChannel
/*    */ {
/* 36 */   private static final Log log = LogFactory.getLog(TransportServerChannelSupport.class);
/*    */   
/*    */   private String url;
/*    */   private TransportChannelListener transportChannelListener;
/* 40 */   private List channels = new ArrayList();
/*    */   
/*    */   public TransportServerChannelSupport(URI url) {
/* 43 */     this(url.toString());
/*    */   }
/*    */   
/*    */   public TransportServerChannelSupport(String url) {
/* 47 */     this.url = url;
/*    */   }
/*    */   
/*    */   public void start() throws JMSException {
/* 51 */     if (this.transportChannelListener == null) {
/* 52 */       throw new JMSException("Must have a TransportChannelListener attached!");
/*    */     }
/*    */   }
/*    */   
/*    */   public synchronized void stop() throws JMSException {
/* 57 */     for (Iterator iter = this.channels.iterator(); iter.hasNext(); ) {
/* 58 */       TransportChannel channel = iter.next();
/* 59 */       channel.stop();
/*    */     } 
/*    */   }
/*    */   
/*    */   public TransportChannelListener getTransportChannelListener() {
/* 64 */     return this.transportChannelListener;
/*    */   }
/*    */   
/*    */   public void setTransportChannelListener(TransportChannelListener listener) {
/* 68 */     this.transportChannelListener = listener;
/*    */   }
/*    */   
/*    */   public String getUrl() {
/* 72 */     return this.url;
/*    */   }
/*    */   
/*    */   public void setUrl(String url) {
/* 76 */     this.url = url;
/*    */   }
/*    */   
/*    */   protected synchronized void addClient(TransportChannel channel) {
/* 80 */     if (this.transportChannelListener == null) {
/* 81 */       log.warn("No listener attached, cannot add channel: " + channel);
/*    */     } else {
/*    */       
/* 84 */       this.transportChannelListener.addClient(channel);
/* 85 */       channel.setTransportChannelListener(this.transportChannelListener);
/* 86 */       this.channels.add(channel);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\TransportServerChannelSupport.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */