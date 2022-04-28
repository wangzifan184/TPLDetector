/*     */ package org.codehaus.activemq.transport.http;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.httpclient.HttpClient;
/*     */ import org.apache.commons.httpclient.HttpMethod;
/*     */ import org.apache.commons.httpclient.methods.GetMethod;
/*     */ import org.apache.commons.httpclient.methods.PostMethod;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.TextWireFormat;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpClientTransportChannel
/*     */   extends HttpTransportChannelSupport
/*     */ {
/*  42 */   private static final Log log = LogFactory.getLog(HttpClientTransportChannel.class);
/*     */   
/*     */   private HttpClient sendHttpClient;
/*     */   private HttpClient receiveHttpClient;
/*     */   
/*     */   public HttpClientTransportChannel(TextWireFormat wireFormat, String remoteUrl) {
/*  48 */     super(wireFormat, remoteUrl);
/*     */   }
/*     */   
/*     */   public void asyncSend(Packet packet) throws JMSException {
/*  52 */     PostMethod httpMethod = new PostMethod(getRemoteUrl());
/*  53 */     configureMethod((HttpMethod)httpMethod);
/*  54 */     httpMethod.setRequestBody(getWireFormat().toString(packet));
/*     */     try {
/*  56 */       int answer = getSendHttpClient().executeMethod((HttpMethod)httpMethod);
/*  57 */       if (answer != 200) {
/*  58 */         throw new JMSException("Failed to post packet: " + packet + " as response was: " + answer);
/*     */       }
/*     */     }
/*  61 */     catch (IOException e) {
/*  62 */       throw JMSExceptionHelper.newJMSException("Could not post packet: " + packet + " due to: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isMulticast() {
/*  67 */     return false;
/*     */   }
/*     */   
/*     */   public void run() {
/*  71 */     log.trace("HTTP GET consumer thread starting for clientID: " + getClientID());
/*  72 */     HttpClient httpClient = getReceiveHttpClient();
/*  73 */     String remoteUrl = getRemoteUrl();
/*  74 */     while (!getClosed().get()) {
/*  75 */       GetMethod httpMethod = new GetMethod(remoteUrl);
/*  76 */       configureMethod((HttpMethod)httpMethod);
/*     */       try {
/*  78 */         int answer = httpClient.executeMethod((HttpMethod)httpMethod);
/*  79 */         if (answer != 200) {
/*  80 */           if (answer == 408) {
/*  81 */             log.info("GET timed out");
/*     */             continue;
/*     */           } 
/*  84 */           log.warn("Failed to perform GET on: " + remoteUrl + " as response was: " + answer);
/*     */           
/*     */           continue;
/*     */         } 
/*  88 */         Packet packet = getWireFormat().readPacket(new DataInputStream(httpMethod.getResponseBodyAsStream()));
/*  89 */         if (packet == null) {
/*  90 */           log.warn("Received null packet from url: " + remoteUrl);
/*     */           continue;
/*     */         } 
/*  93 */         doConsumePacket(packet);
/*     */ 
/*     */       
/*     */       }
/*  97 */       catch (IOException e) {
/*  98 */         log.warn("Failed to perform GET on: " + remoteUrl + " due to: " + e, e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpClient getSendHttpClient() {
/* 106 */     if (this.sendHttpClient == null) {
/* 107 */       this.sendHttpClient = createHttpClient();
/*     */     }
/* 109 */     return this.sendHttpClient;
/*     */   }
/*     */   
/*     */   public void setSendHttpClient(HttpClient sendHttpClient) {
/* 113 */     this.sendHttpClient = sendHttpClient;
/*     */   }
/*     */   
/*     */   public HttpClient getReceiveHttpClient() {
/* 117 */     if (this.receiveHttpClient == null) {
/* 118 */       this.receiveHttpClient = createHttpClient();
/*     */     }
/* 120 */     return this.receiveHttpClient;
/*     */   }
/*     */   
/*     */   public void setReceiveHttpClient(HttpClient receiveHttpClient) {
/* 124 */     this.receiveHttpClient = receiveHttpClient;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpClient createHttpClient() {
/* 130 */     return new HttpClient();
/*     */   }
/*     */   
/*     */   protected void configureMethod(HttpMethod method) {
/* 134 */     String clientID = getClientID();
/* 135 */     if (clientID != null)
/* 136 */       method.setRequestHeader("clientID", clientID); 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\http\HttpClientTransportChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */