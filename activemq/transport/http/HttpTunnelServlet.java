/*     */ package org.codehaus.activemq.transport.http;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.BoundedLinkedQueue;
/*     */ import EDU.oswego.cs.dl.util.concurrent.Channel;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.jms.JMSException;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServlet;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.TextWireFormat;
/*     */ import org.codehaus.activemq.transport.TransportChannel;
/*     */ import org.codehaus.activemq.transport.TransportChannelListener;
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
/*     */ public class HttpTunnelServlet
/*     */   extends HttpServlet
/*     */ {
/*  44 */   private static final Log log = LogFactory.getLog(HttpTunnelServlet.class);
/*     */   
/*     */   private TransportChannelListener listener;
/*     */   private TextWireFormat wireFormat;
/*  48 */   private Map clients = new HashMap();
/*  49 */   private long requestTimeout = 30000L;
/*     */   
/*     */   public void init() throws ServletException {
/*  52 */     super.init();
/*  53 */     this.listener = (TransportChannelListener)getServletContext().getAttribute("transportChannelListener");
/*  54 */     if (this.listener == null) {
/*  55 */       throw new ServletException("No such attribute 'transportChannelListener' available in the ServletContext");
/*     */     }
/*  57 */     this.wireFormat = (TextWireFormat)getServletContext().getAttribute("wireFormat");
/*  58 */     if (this.wireFormat == null) {
/*  59 */       throw new ServletException("No such attribute 'wireFormat' available in the ServletContext");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*  65 */     Packet packet = null;
/*     */     try {
/*  67 */       HttpServerTransportChannel transportChannel = getTransportChannel(request);
/*  68 */       if (transportChannel == null) {
/*     */         return;
/*     */       }
/*  71 */       packet = (Packet)transportChannel.getChannel().poll(this.requestTimeout);
/*     */     }
/*  73 */     catch (InterruptedException e) {}
/*     */ 
/*     */     
/*  76 */     if (packet == null) {
/*  77 */       response.setStatus(408);
/*     */     } else {
/*     */       
/*     */       try {
/*  81 */         this.wireFormat.writePacket(packet, new DataOutputStream((OutputStream)response.getOutputStream()));
/*     */       }
/*  83 */       catch (JMSException e) {
/*  84 */         throw JMSExceptionHelper.newIOException(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*  90 */     HttpServerTransportChannel transportChannel = getTransportChannel(request);
/*  91 */     if (transportChannel == null) {
/*  92 */       response.setStatus(404);
/*     */     } else {
/*     */       
/*     */       try {
/*  96 */         Packet packet = this.wireFormat.fromString(readRequestBody(request));
/*  97 */         transportChannel.getPacketListener().consume(packet);
/*     */       }
/*  99 */       catch (IOException e) {
/* 100 */         log.error("Caught: " + e, e);
/*     */       }
/* 102 */       catch (JMSException e) {
/* 103 */         throw JMSExceptionHelper.newIOException(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected String readRequestBody(HttpServletRequest request) throws IOException {
/* 109 */     StringBuffer buffer = new StringBuffer();
/* 110 */     BufferedReader reader = request.getReader();
/*     */     while (true) {
/* 112 */       String line = reader.readLine();
/* 113 */       if (line == null) {
/*     */         break;
/*     */       }
/*     */       
/* 117 */       buffer.append(line);
/* 118 */       buffer.append("\n");
/*     */     } 
/*     */     
/* 121 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   protected HttpServerTransportChannel getTransportChannel(HttpServletRequest request) {
/* 125 */     String clientID = request.getHeader("clientID");
/* 126 */     if (clientID == null) {
/* 127 */       clientID = request.getParameter("clientID");
/*     */     }
/* 129 */     if (clientID == null) {
/* 130 */       log.warn("No clientID header so ignoring request");
/* 131 */       return null;
/*     */     } 
/* 133 */     synchronized (this) {
/* 134 */       HttpServerTransportChannel answer = (HttpServerTransportChannel)this.clients.get(clientID);
/* 135 */       if (answer == null) {
/* 136 */         answer = createTransportChannel();
/* 137 */         this.clients.put(clientID, answer);
/* 138 */         this.listener.addClient((TransportChannel)answer);
/*     */       }
/*     */       else {
/*     */         
/* 142 */         keepAlivePing(answer);
/*     */       } 
/* 144 */       return answer;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void keepAlivePing(HttpServerTransportChannel channel) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpServerTransportChannel createTransportChannel() {
/* 156 */     return new HttpServerTransportChannel((Channel)new BoundedLinkedQueue(10));
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\http\HttpTunnelServlet.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */