/*     */ package org.codehaus.activemq.web;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.jms.Destination;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Message;
/*     */ import javax.jms.MessageConsumer;
/*     */ import javax.jms.ObjectMessage;
/*     */ import javax.jms.TextMessage;
/*     */ import javax.servlet.ServletConfig;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.ActiveMQDestination;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MessageServlet
/*     */   extends MessageServletSupport
/*     */ {
/*  50 */   private static final Log log = LogFactory.getLog(MessageServlet.class);
/*     */   
/*  52 */   private String readTimeoutParameter = "readTimeout";
/*  53 */   private long defaultReadTimeout = -1L;
/*  54 */   private long maximumReadTimeout = 30000L;
/*     */   
/*     */   public void init() throws ServletException {
/*  57 */     ServletConfig servletConfig = getServletConfig();
/*  58 */     String name = servletConfig.getInitParameter("defaultReadTimeout");
/*  59 */     if (name != null) {
/*  60 */       this.defaultReadTimeout = asLong(name);
/*     */     }
/*  62 */     name = servletConfig.getInitParameter("maximumReadTimeout");
/*  63 */     if (name != null) {
/*  64 */       this.maximumReadTimeout = asLong(name);
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
/*     */ 
/*     */   
/*     */   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*     */     try {
/*  79 */       WebClient client = getWebClient(request);
/*     */       
/*  81 */       String text = getPostedMessageBody(request);
/*     */ 
/*     */       
/*  84 */       Destination destination = getDestination(client, request);
/*     */       
/*  86 */       log.info("Sending message to: " + ActiveMQDestination.inspect(destination) + " with text: " + text);
/*     */       
/*  88 */       TextMessage message = client.getSession().createTextMessage(text);
/*  89 */       appendParametersToMessage(request, message);
/*  90 */       client.send(destination, (Message)message);
/*     */ 
/*     */       
/*  93 */       response.setHeader("messageID", message.getJMSMessageID());
/*  94 */       response.setStatus(200);
/*     */     }
/*  96 */     catch (JMSException e) {
/*  97 */       throw new ServletException("Could not post JMS message: " + e, e);
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
/*     */   
/*     */   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*     */     try {
/* 111 */       WebClient client = getWebClient(request);
/*     */ 
/*     */       
/* 114 */       Destination destination = getDestination(client, request);
/*     */       
/* 116 */       long timeout = getReadTimeout(request);
/*     */       
/* 118 */       log.info("Receiving message from: " + ActiveMQDestination.inspect(destination) + " with timeout: " + timeout);
/*     */       
/* 120 */       MessageConsumer consumer = client.getConsumer(destination);
/*     */       
/* 122 */       Message message = null;
/*     */       
/* 124 */       synchronized (consumer) {
/* 125 */         if (timeout == 0L) {
/* 126 */           message = consumer.receiveNoWait();
/*     */         } else {
/*     */           
/* 129 */           message = consumer.receive(timeout);
/*     */         } 
/*     */       } 
/*     */       
/* 133 */       log.info("HTTP GET servlet done! message: " + message);
/*     */       
/* 135 */       sendMessageResponse(request, response, message);
/*     */     }
/* 137 */     catch (JMSException e) {
/* 138 */       throw new ServletException("Could not post JMS message: " + e, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*     */     try {
/* 148 */       WebClient client = getWebClient(request);
/*     */ 
/*     */       
/* 151 */       Destination destination = getDestination(client, request);
/*     */       
/* 153 */       MessageConsumer consumer = client.getConsumer(destination);
/*     */       
/* 155 */       Message message = null;
/*     */       
/* 157 */       synchronized (consumer) {
/* 158 */         message = consumer.receiveNoWait();
/*     */       } 
/*     */       
/* 161 */       sendMessageResponse(request, response, message);
/*     */     }
/* 163 */     catch (JMSException e) {
/* 164 */       throw new ServletException("Could not post JMS message: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void sendMessageResponse(HttpServletRequest request, HttpServletResponse response, Message message) throws JMSException, IOException {
/* 169 */     if (message == null) {
/* 170 */       response.setStatus(204);
/*     */     } else {
/*     */       
/* 173 */       String type = getContentType(request);
/* 174 */       if (type != null) {
/* 175 */         response.setContentType(type);
/*     */       }
/* 177 */       setResponseHeaders(response, message);
/* 178 */       if (message instanceof TextMessage) {
/* 179 */         TextMessage textMsg = (TextMessage)message;
/* 180 */         response.getWriter().print(textMsg.getText());
/*     */       }
/* 182 */       else if (message instanceof ObjectMessage) {
/* 183 */         ObjectMessage objectMsg = (ObjectMessage)message;
/* 184 */         Object object = objectMsg.getObject();
/* 185 */         response.getWriter().print(object.toString());
/*     */       } 
/* 187 */       response.setStatus(200);
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
/*     */   
/*     */   protected String getContentType(HttpServletRequest request) {
/* 200 */     String value = request.getParameter("xml");
/* 201 */     if (value != null && "true".equalsIgnoreCase(value)) {
/* 202 */       return "text/xml";
/*     */     }
/* 204 */     return null;
/*     */   }
/*     */   
/*     */   protected void setResponseHeaders(HttpServletResponse response, Message message) throws JMSException {
/* 208 */     response.setHeader("destination", message.getJMSDestination().toString());
/* 209 */     response.setHeader("id", message.getJMSMessageID());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long getReadTimeout(HttpServletRequest request) {
/* 217 */     long answer = this.defaultReadTimeout;
/*     */     
/* 219 */     String name = request.getParameter(this.readTimeoutParameter);
/* 220 */     if (name != null) {
/* 221 */       answer = asLong(name);
/*     */     }
/* 223 */     if (answer < 0L || answer > this.maximumReadTimeout) {
/* 224 */       answer = this.maximumReadTimeout;
/*     */     }
/* 226 */     return answer;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\web\MessageServlet.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */