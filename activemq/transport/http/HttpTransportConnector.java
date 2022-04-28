/*    */ package org.codehaus.activemq.transport.http;
/*    */ 
/*    */ import java.net.URI;
/*    */ import javax.jms.JMSException;
/*    */ import org.codehaus.activemq.message.TextWireFormat;
/*    */ import org.codehaus.activemq.transport.TransportServerChannelSupport;
/*    */ import org.codehaus.activemq.transport.xstream.XStreamWireFormat;
/*    */ import org.codehaus.activemq.util.JMSExceptionHelper;
/*    */ import org.mortbay.http.HttpContext;
/*    */ import org.mortbay.http.HttpHandler;
/*    */ import org.mortbay.http.HttpListener;
/*    */ import org.mortbay.http.SocketListener;
/*    */ import org.mortbay.jetty.Server;
/*    */ import org.mortbay.jetty.servlet.ServletHandler;
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
/*    */ public class HttpTransportConnector
/*    */   extends TransportServerChannelSupport
/*    */ {
/*    */   private URI bindAddress;
/*    */   private TextWireFormat wireFormat;
/* 38 */   private Server server = new Server();
/* 39 */   private SocketListener listener = new SocketListener();
/*    */   
/*    */   public HttpTransportConnector(URI uri) {
/* 42 */     super(uri);
/* 43 */     this.bindAddress = uri;
/*    */   }
/*    */   
/*    */   public void start() throws JMSException {
/*    */     try {
/* 48 */       this.listener.setPort(this.bindAddress.getPort());
/* 49 */       this.server.addListener((HttpListener)this.listener);
/*    */       
/* 51 */       HttpContext context = this.server.addContext("/");
/* 52 */       ServletHandler handler = new ServletHandler();
/* 53 */       handler.addServlet("httpTunnel", "/*", HttpTunnelServlet.class.getName());
/*    */       
/* 55 */       context.addHandler((HttpHandler)handler);
/* 56 */       context.setAttribute("transportChannelListener", getTransportChannelListener());
/* 57 */       context.setAttribute("wireFormat", getWireFormat());
/* 58 */       this.server.start();
/*    */     }
/* 60 */     catch (Exception e) {
/* 61 */       throw JMSExceptionHelper.newJMSException("Could not start HTTP server: " + e, e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public synchronized void stop() throws JMSException {
/* 66 */     super.stop();
/*    */     try {
/* 68 */       this.server.stop();
/*    */     }
/* 70 */     catch (InterruptedException e) {
/* 71 */       throw JMSExceptionHelper.newJMSException("Could not stop HTTP server: " + e, e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TextWireFormat getWireFormat() {
/* 78 */     if (this.wireFormat == null) {
/* 79 */       this.wireFormat = createWireFormat();
/*    */     }
/* 81 */     return this.wireFormat;
/*    */   }
/*    */   
/*    */   public void setWireFormat(TextWireFormat wireFormat) {
/* 85 */     this.wireFormat = wireFormat;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected TextWireFormat createWireFormat() {
/* 92 */     return (TextWireFormat)new XStreamWireFormat();
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\http\HttpTransportConnector.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */