/*     */ package org.codehaus.activemq.transport.reliable;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import javax.jms.ExceptionListener;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.TimeoutExpiredException;
/*     */ import org.codehaus.activemq.UnsupportedWireFormatException;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.PacketListener;
/*     */ import org.codehaus.activemq.message.Receipt;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.transport.TransportChannel;
/*     */ import org.codehaus.activemq.transport.composite.CompositeTransportChannel;
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
/*     */ public class ReliableTransportChannel
/*     */   extends CompositeTransportChannel
/*     */   implements PacketListener, ExceptionListener
/*     */ {
/*  43 */   private static final Log log = LogFactory.getLog(ReliableTransportChannel.class);
/*  44 */   private Object lock = new Object();
/*  45 */   private LinkedList packetList = new LinkedList();
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean cacheMessagesForFailover;
/*     */ 
/*     */ 
/*     */   
/*     */   public ReliableTransportChannel(WireFormat wireFormat) {
/*  54 */     super(wireFormat);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReliableTransportChannel(WireFormat wireFormat, List uris) {
/*  64 */     super(wireFormat, uris);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  71 */     return "ReliableTransportChannel: " + this.channel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() throws JMSException {
/*  80 */     if (this.started.commit(false, true) && 
/*  81 */       this.channel != null) {
/*  82 */       this.channel.start();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Receipt send(Packet packet, int timeout) throws JMSException {
/*     */     do {
/*  95 */       TransportChannel tc = getEstablishedChannel(timeout);
/*  96 */       if (tc == null)
/*     */         continue;  try {
/*  98 */         return tc.send(packet, timeout);
/*     */       }
/* 100 */       catch (TimeoutExpiredException e) {
/* 101 */         throw e;
/*     */       }
/* 103 */       catch (UnsupportedWireFormatException uwf) {
/* 104 */         throw uwf;
/*     */       }
/* 106 */       catch (JMSException jmsEx) {
/* 107 */         if (isPendingStop()) {
/*     */           break;
/*     */         }
/* 110 */         doReconnect(tc, timeout);
/*     */       }
/*     */     
/*     */     }
/* 114 */     while (!this.closed.get() && !isPendingStop());
/* 115 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void asyncSend(Packet packet) throws JMSException {
/* 123 */     long timeout = getEstablishConnectionTimeout();
/*     */     do {
/* 125 */       TransportChannel tc = getEstablishedChannel(timeout);
/* 126 */       if (tc == null)
/*     */         continue;  try {
/* 128 */         tc.asyncSend(packet);
/*     */         
/*     */         break;
/* 131 */       } catch (TimeoutExpiredException e) {
/* 132 */         throw e;
/*     */       }
/* 134 */       catch (UnsupportedWireFormatException uwf) {
/* 135 */         throw uwf;
/*     */       }
/* 137 */       catch (JMSException jmsEx) {
/* 138 */         if (isPendingStop()) {
/*     */           break;
/*     */         }
/* 141 */         doReconnect(tc, timeout);
/*     */       }
/*     */     
/*     */     }
/* 145 */     while (!this.closed.get() && !isPendingStop());
/*     */   }
/*     */   
/*     */   protected void configureChannel() {
/* 149 */     this.channel.setPacketListener(this);
/* 150 */     this.channel.setExceptionListener(this);
/*     */   }
/*     */   
/*     */   protected URI extractURI(List list) throws JMSException {
/* 154 */     int idx = 0;
/* 155 */     if (list.size() > 1) {
/* 156 */       SMLCGRandom rand = new SMLCGRandom();
/*     */       do {
/* 158 */         idx = (int)(rand.nextDouble() * list.size());
/*     */       }
/* 160 */       while (idx < 0 || idx >= list.size());
/*     */     } 
/* 162 */     Object answer = list.remove(idx);
/* 163 */     if (answer instanceof URI) {
/* 164 */       return (URI)answer;
/*     */     }
/*     */     
/* 167 */     log.error("#### got: " + answer + " of type: " + answer.getClass());
/* 168 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void consume(Packet packet) {
/* 180 */     PacketListener listener = getPacketListener();
/* 181 */     if (listener != null) {
/* 182 */       listener.consume(packet);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onException(JMSException jmsEx) {
/* 192 */     TransportChannel tc = this.channel;
/* 193 */     if (jmsEx instanceof UnsupportedWireFormatException) {
/* 194 */       fireException(jmsEx);
/*     */     } else {
/*     */       
/*     */       try {
/* 198 */         doReconnect(tc, getEstablishConnectionTimeout());
/*     */       }
/* 200 */       catch (JMSException ex) {
/* 201 */         ex.setLinkedException((Exception)jmsEx);
/* 202 */         fireException(ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 211 */     super.stop();
/* 212 */     fireStatusEvent(this.currentURI, 5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void fireException(JMSException jmsEx) {
/* 221 */     ExceptionListener listener = getExceptionListener();
/* 222 */     if (listener != null) {
/* 223 */       listener.onException(jmsEx);
/*     */     }
/*     */   }
/*     */   
/*     */   protected TransportChannel getEstablishedChannel(long timeout) throws JMSException {
/* 228 */     if (!this.closed.get() && this.channel == null && !isPendingStop()) {
/* 229 */       establishConnection(timeout);
/*     */     }
/* 231 */     return this.channel;
/*     */   }
/*     */   
/*     */   protected void doReconnect(TransportChannel currentChannel, long timeout) throws JMSException {
/* 235 */     setTransportConnected(false);
/* 236 */     if (!this.closed.get() && !isPendingStop())
/* 237 */       synchronized (this.lock) {
/*     */ 
/*     */         
/* 240 */         if (this.channel == currentChannel) {
/* 241 */           fireStatusEvent(this.currentURI, 2);
/*     */           try {
/* 243 */             establishConnection(timeout);
/*     */           }
/* 245 */           catch (JMSException jmsEx) {
/* 246 */             fireStatusEvent(this.currentURI, 4);
/* 247 */             throw jmsEx;
/*     */           } 
/* 249 */           setTransportConnected(true);
/* 250 */           fireStatusEvent(this.currentURI, 3);
/*     */         } 
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\reliable\ReliableTransportChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */