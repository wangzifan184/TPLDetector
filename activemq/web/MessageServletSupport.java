/*     */ package org.codehaus.activemq.web;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.jms.Destination;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.TextMessage;
/*     */ import javax.servlet.ServletConfig;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServlet;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.codehaus.activemq.message.ActiveMQQueue;
/*     */ import org.codehaus.activemq.message.ActiveMQTopic;
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
/*     */ public abstract class MessageServletSupport
/*     */   extends HttpServlet
/*     */ {
/*     */   private boolean defaultTopicFlag = true;
/*     */   private Destination defaultDestination;
/*  48 */   private String destinationParameter = "destination";
/*  49 */   private String topicParameter = "topic";
/*  50 */   private String bodyParameter = "body";
/*     */ 
/*     */   
/*     */   public void init(ServletConfig servletConfig) throws ServletException {
/*  54 */     super.init(servletConfig);
/*     */     
/*  56 */     String name = servletConfig.getInitParameter("topic");
/*  57 */     if (name != null) {
/*  58 */       this.defaultTopicFlag = asBoolean(name);
/*     */     }
/*     */     
/*  61 */     log("Defaulting to use topics: " + this.defaultTopicFlag);
/*     */     
/*  63 */     name = servletConfig.getInitParameter("destination");
/*  64 */     if (name != null) {
/*  65 */       if (this.defaultTopicFlag) {
/*  66 */         this.defaultDestination = (Destination)new ActiveMQTopic(name);
/*     */       } else {
/*     */         
/*  69 */         this.defaultDestination = (Destination)new ActiveMQQueue(name);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*  74 */     WebClient.initContext(getServletContext());
/*     */   }
/*     */   
/*     */   protected WebClient createWebClient(HttpServletRequest request) {
/*  78 */     return new WebClient(getServletContext());
/*     */   }
/*     */   
/*     */   public static boolean asBoolean(String param) {
/*  82 */     return (param != null && param.equalsIgnoreCase("true"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected WebClient getWebClient(HttpServletRequest request) {
/*  92 */     HttpSession session = request.getSession(true);
/*  93 */     WebClient client = WebClient.getWebClient(session);
/*  94 */     if (client == null) {
/*  95 */       client = createWebClient(request);
/*  96 */       session.setAttribute("org.codehaus.activemq.webclient", client);
/*     */     } 
/*  98 */     return client;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void appendParametersToMessage(HttpServletRequest request, TextMessage message) throws JMSException {
/* 103 */     for (Iterator iter = request.getParameterMap().entrySet().iterator(); iter.hasNext(); ) {
/* 104 */       Map.Entry entry = iter.next();
/* 105 */       String name = (String)entry.getKey();
/* 106 */       if (!this.destinationParameter.equals(name) && !this.topicParameter.equals(name) && !this.bodyParameter.equals(name)) {
/* 107 */         Object value = entry.getValue();
/* 108 */         if (value instanceof Object[]) {
/* 109 */           Object[] array = (Object[])value;
/* 110 */           if (array.length == 1) {
/* 111 */             value = array[0];
/*     */           } else {
/*     */             
/* 114 */             log("Can't use property: " + name + " which is of type: " + value.getClass().getName() + " value");
/* 115 */             value = null;
/* 116 */             for (int i = 0, size = array.length; i < size; i++) {
/* 117 */               log("value[" + i + "] = " + array[i]);
/*     */             }
/*     */           } 
/*     */         } 
/* 121 */         if (value != null) {
/* 122 */           message.setObjectProperty(name, value);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Destination getDestination(WebClient client, HttpServletRequest request) throws JMSException, NoDestinationSuppliedException {
/* 132 */     String destinationName = request.getParameter(this.destinationParameter);
/* 133 */     if (destinationName == null) {
/* 134 */       if (this.defaultDestination == null) {
/* 135 */         return getDestinationFromURI(client, request);
/*     */       }
/*     */       
/* 138 */       return this.defaultDestination;
/*     */     } 
/*     */ 
/*     */     
/* 142 */     return getDestination(client, request, destinationName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Destination getDestinationFromURI(WebClient client, HttpServletRequest request) throws NoDestinationSuppliedException, JMSException {
/* 150 */     String uri = request.getPathInfo();
/* 151 */     if (uri == null) {
/* 152 */       throw new NoDestinationSuppliedException();
/*     */     }
/*     */     
/* 155 */     if (uri.startsWith("/")) {
/* 156 */       uri = uri.substring(1);
/*     */     }
/* 158 */     uri = uri.replace('/', '.');
/* 159 */     return getDestination(client, request, uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Destination getDestination(WebClient client, HttpServletRequest request, String destinationName) throws JMSException {
/* 166 */     if (isTopic(request)) {
/* 167 */       return (Destination)client.getSession().createTopic(destinationName);
/*     */     }
/*     */     
/* 170 */     return (Destination)client.getSession().createQueue(destinationName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isTopic(HttpServletRequest request) {
/* 180 */     boolean aTopic = this.defaultTopicFlag;
/* 181 */     String aTopicText = request.getParameter(this.topicParameter);
/* 182 */     if (aTopicText != null) {
/* 183 */       aTopic = asBoolean(aTopicText);
/*     */     }
/* 185 */     return aTopic;
/*     */   }
/*     */   
/*     */   protected long asLong(String name) {
/* 189 */     return Long.parseLong(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getPostedMessageBody(HttpServletRequest request) throws IOException {
/* 197 */     String answer = request.getParameter(this.bodyParameter);
/* 198 */     if (answer == null) {
/*     */       
/* 200 */       BufferedReader reader = request.getReader();
/* 201 */       StringBuffer buffer = new StringBuffer();
/*     */       while (true) {
/* 203 */         String line = reader.readLine();
/* 204 */         if (line == null) {
/*     */           break;
/*     */         }
/* 207 */         buffer.append(line);
/* 208 */         buffer.append("\n");
/*     */       } 
/* 210 */       return buffer.toString();
/*     */     } 
/* 212 */     return answer;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\web\MessageServletSupport.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */