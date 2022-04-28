/*     */ package org.codehaus.activemq.transport.gnet;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.Latch;
/*     */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;
/*     */ import java.net.URI;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.geronimo.network.SelectorManager;
/*     */ import org.apache.geronimo.network.protocol.AcceptableProtocol;
/*     */ import org.apache.geronimo.network.protocol.Protocol;
/*     */ import org.apache.geronimo.network.protocol.ProtocolFactory;
/*     */ import org.apache.geronimo.network.protocol.ServerSocketAcceptor;
/*     */ import org.apache.geronimo.network.protocol.ServerSocketAcceptorListener;
/*     */ import org.apache.geronimo.network.protocol.SocketProtocol;
/*     */ import org.apache.geronimo.pool.ClockPool;
/*     */ import org.apache.geronimo.pool.ThreadPool;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.transport.TransportChannel;
/*     */ import org.codehaus.activemq.transport.TransportServerChannel;
/*     */ import org.codehaus.activemq.transport.TransportServerChannelSupport;
/*     */ import org.codehaus.activemq.util.JMSExceptionHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GTransportServerChannel
/*     */   extends TransportServerChannelSupport
/*     */   implements TransportServerChannel
/*     */ {
/*     */   protected static final int BACKLOG = 500;
/*  49 */   private static final Log log = LogFactory.getLog(GTransportServerChannel.class);
/*     */   
/*     */   private WireFormat wireFormat;
/*     */   
/*     */   private SynchronizedBoolean closed;
/*     */   
/*     */   private ThreadPool tp;
/*     */   
/*     */   private ClockPool cp;
/*     */   
/*     */   private SelectorManager sm;
/*     */   
/*     */   private ServerSocketAcceptor ssa;
/*     */   
/*     */   private ProtocolFactory pf;
/*     */   private Latch startLatch;
/*     */   
/*     */   public GTransportServerChannel(WireFormat wireFormat, URI bindAddr, SelectorManager selectorManager, ThreadPool threadPool, ClockPool clockPool) throws Exception {
/*  67 */     super(bindAddr);
/*  68 */     this.wireFormat = wireFormat;
/*  69 */     this.sm = selectorManager;
/*  70 */     this.tp = threadPool;
/*  71 */     this.cp = clockPool;
/*     */     
/*  73 */     this.closed = new SynchronizedBoolean(false);
/*  74 */     this.startLatch = new Latch();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     SocketProtocol spt = new SocketProtocol();
/*  80 */     spt.setTimeout(30000L);
/*  81 */     spt.setSelectorManager(this.sm);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     this.pf = new ProtocolFactory();
/* 103 */     this.pf.setClockPool(this.cp);
/* 104 */     this.pf.setMaxAge(Long.MAX_VALUE);
/* 105 */     this.pf.setMaxInactivity(Long.MAX_VALUE);
/*     */     
/* 107 */     this.pf.setReclaimPeriod(10000L);
/* 108 */     this.pf.setTemplate((AcceptableProtocol)spt);
/*     */     
/* 110 */     this.pf.setAcceptedCallBack(createAcceptedCallBack());
/*     */     
/* 112 */     this.ssa = new ServerSocketAcceptor();
/* 113 */     this.ssa.setSelectorManager(this.sm);
/* 114 */     this.ssa.setTimeOut(5000);
/* 115 */     this.ssa.setUri(bindAddr);
/* 116 */     this.ssa.setAcceptorListener((ServerSocketAcceptorListener)this.pf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ProtocolFactory.AcceptedCallBack createAcceptedCallBack() {
/* 123 */     return new ProtocolFactory.AcceptedCallBack()
/*     */       {
/*     */         public void accepted(AcceptableProtocol p) {
/*     */           try {
/* 127 */             GTransportServerChannel.this.startLatch.acquire();
/*     */             
/* 129 */             if (p != null) {
/* 130 */               GTransportChannel channel = new GTransportChannel(GTransportServerChannel.this.wireFormat, (Protocol)p, GTransportServerChannel.this.tp);
/* 131 */               GTransportServerChannel.this.addClient((TransportChannel)channel);
/*     */             }
/*     */           
/*     */           }
/* 135 */           catch (Exception e) {
/* 136 */             GTransportServerChannel.log.error("Caught while attempting to add new protocol: " + e, e);
/*     */           } 
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         private final GTransportServerChannel this$0;
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() throws JMSException {
/* 148 */     super.start();
/*     */     try {
/* 150 */       this.ssa.startup();
/*     */     }
/* 152 */     catch (Exception e) {
/* 153 */       JMSException jmsEx = new JMSException("Could not start ServerSocketAcceptor: " + e);
/* 154 */       jmsEx.setLinkedException(e);
/* 155 */       throw jmsEx;
/*     */     } 
/* 157 */     this.startLatch.release();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() throws JMSException {
/* 164 */     if (this.closed.commit(false, true)) {
/* 165 */       super.stop();
/*     */       try {
/* 167 */         this.ssa.drain();
/* 168 */         this.pf.drain();
/*     */       }
/* 170 */       catch (Throwable e) {
/* 171 */         throw JMSExceptionHelper.newJMSException("Failed to stop: " + e, e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 181 */     return "GTransportServerChannel@" + getUrl();
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\gnet\GTransportServerChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */