/*     */ package org.codehaus.activemq.transport.http;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.TextWireFormat;
/*     */ import org.codehaus.activemq.util.Callback;
/*     */ import org.codehaus.activemq.util.ExceptionTemplate;
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
/*     */ public class HttpTransportChannel
/*     */   extends HttpTransportChannelSupport
/*     */ {
/*  41 */   private static final Log log = LogFactory.getLog(HttpTransportChannel.class);
/*     */   
/*     */   private URL url;
/*     */   private HttpURLConnection sendConnection;
/*     */   private HttpURLConnection receiveConnection;
/*     */   
/*     */   public HttpTransportChannel(TextWireFormat wireFormat, String remoteUrl) throws MalformedURLException {
/*  48 */     super(wireFormat, remoteUrl);
/*  49 */     this.url = new URL(remoteUrl);
/*     */   }
/*     */   
/*     */   public void asyncSend(Packet packet) throws JMSException {
/*     */     try {
/*  54 */       HttpURLConnection connection = getSendConnection();
/*  55 */       String text = getWireFormat().toString(packet);
/*  56 */       Writer writer = new OutputStreamWriter(connection.getOutputStream());
/*  57 */       writer.write(text);
/*  58 */       writer.flush();
/*  59 */       int answer = connection.getResponseCode();
/*  60 */       if (answer != 200) {
/*  61 */         throw new JMSException("Failed to post packet: " + packet + " as response was: " + answer);
/*     */       }
/*     */     }
/*  64 */     catch (IOException e) {
/*  65 */       throw JMSExceptionHelper.newJMSException("Could not post packet: " + packet + " due to: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stop() {
/*  70 */     ExceptionTemplate template = new ExceptionTemplate();
/*  71 */     if (this.sendConnection != null)
/*  72 */       template.run(new Callback() {
/*     */             public void execute() throws Throwable {
/*  74 */               HttpTransportChannel.this.sendConnection.disconnect();
/*     */             }
/*     */             private final HttpTransportChannel this$0;
/*     */           }); 
/*  78 */     if (this.receiveConnection != null)
/*  79 */       template.run(new Callback() {
/*     */             public void execute() throws Throwable {
/*  81 */               HttpTransportChannel.this.receiveConnection.disconnect();
/*     */             }
/*     */             private final HttpTransportChannel this$0;
/*     */           }); 
/*  85 */     super.stop();
/*  86 */     Throwable firstException = template.getFirstException();
/*  87 */     if (firstException != null) {
/*  88 */       log.warn("Failed to shut down cleanly: " + firstException, firstException);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isMulticast() {
/*  93 */     return false;
/*     */   }
/*     */   
/*     */   public void run() {
/*  97 */     log.trace("HTTP GET consumer thread starting for clientID: " + getClientID());
/*  98 */     String remoteUrl = getRemoteUrl();
/*  99 */     while (!getClosed().get()) {
/*     */       try {
/* 101 */         HttpURLConnection connection = getReceiveConnection();
/* 102 */         int answer = connection.getResponseCode();
/* 103 */         if (answer != 200) {
/* 104 */           if (answer == 408) {
/* 105 */             log.trace("GET timed out");
/*     */             continue;
/*     */           } 
/* 108 */           log.warn("Failed to perform GET on: " + remoteUrl + " as response was: " + answer);
/*     */           
/*     */           continue;
/*     */         } 
/* 112 */         Packet packet = getWireFormat().readPacket(new DataInputStream(connection.getInputStream()));
/*     */         
/* 114 */         if (packet == null) {
/* 115 */           log.warn("Received null packet from url: " + remoteUrl);
/*     */           continue;
/*     */         } 
/* 118 */         doConsumePacket(packet);
/*     */ 
/*     */       
/*     */       }
/* 122 */       catch (Exception e) {
/* 123 */         if (!getClosed().get()) {
/* 124 */           log.warn("Failed to perform GET on: " + remoteUrl + " due to: " + e, e);
/*     */           continue;
/*     */         } 
/* 127 */         log.trace("Caught error after closed: " + e, e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized HttpURLConnection getSendConnection() throws IOException {
/* 137 */     this.sendConnection = (HttpURLConnection)this.url.openConnection();
/* 138 */     this.sendConnection.setDoOutput(true);
/* 139 */     this.sendConnection.setRequestMethod("POST");
/* 140 */     configureConnection(this.sendConnection);
/* 141 */     this.sendConnection.connect();
/* 142 */     return this.sendConnection;
/*     */   }
/*     */   
/*     */   protected synchronized HttpURLConnection getReceiveConnection() throws IOException {
/* 146 */     this.receiveConnection = (HttpURLConnection)this.url.openConnection();
/* 147 */     this.receiveConnection.setDoOutput(false);
/* 148 */     this.receiveConnection.setDoInput(true);
/* 149 */     this.receiveConnection.setRequestMethod("GET");
/* 150 */     configureConnection(this.receiveConnection);
/* 151 */     this.receiveConnection.connect();
/* 152 */     return this.receiveConnection;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void configureConnection(HttpURLConnection connection) {
/* 157 */     String clientID = getClientID();
/* 158 */     if (clientID != null)
/* 159 */       connection.setRequestProperty("clientID", clientID); 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\http\HttpTransportChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */