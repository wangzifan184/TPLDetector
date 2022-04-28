/*     */ package org.codehaus.activemq.web;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;
/*     */ import java.io.Externalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.jms.ConnectionFactory;
/*     */ import javax.jms.Destination;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Message;
/*     */ import javax.jms.MessageConsumer;
/*     */ import javax.jms.MessageProducer;
/*     */ import javax.jms.Session;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import javax.servlet.http.HttpSessionActivationListener;
/*     */ import javax.servlet.http.HttpSessionEvent;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.ActiveMQConnection;
/*     */ import org.codehaus.activemq.ActiveMQConnectionFactory;
/*     */ import org.codehaus.activemq.ActiveMQSession;
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
/*     */ public class WebClient
/*     */   implements HttpSessionActivationListener, Externalizable
/*     */ {
/*     */   public static final String webClientAttribute = "org.codehaus.activemq.webclient";
/*     */   public static final String connectionFactoryAttribute = "org.codehaus.activemq.connectionFactory";
/*     */   public static final String queueConsumersAttribute = "org.codehaus.activemq.queueConsumers";
/*     */   public static final String brokerUrlInitParam = "org.codehaus.activemq.brokerURL";
/*     */   public static final String embeddedBrokerInitParam = "org.codehaus.activemq.embeddedBroker";
/*  60 */   private static final Log log = LogFactory.getLog(WebClient.class);
/*     */   
/*     */   private static transient ConnectionFactory factory;
/*     */   
/*     */   private static transient Map queueConsumers;
/*     */   private transient ServletContext context;
/*     */   private transient ActiveMQConnection connection;
/*     */   private transient ActiveMQSession session;
/*     */   private transient MessageProducer producer;
/*  69 */   private transient Map topicConsumers = (Map)new ConcurrentHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static WebClient getWebClient(HttpSession session) {
/*  76 */     return (WebClient)session.getAttribute("org.codehaus.activemq.webclient");
/*     */   }
/*     */ 
/*     */   
/*     */   public static void initContext(ServletContext context) {
/*  81 */     factory = initConnectionFactory(context);
/*  82 */     if (factory == null) {
/*  83 */       log.warn("No ConnectionFactory available in the ServletContext for: org.codehaus.activemq.connectionFactory");
/*  84 */       factory = (ConnectionFactory)new ActiveMQConnectionFactory("vm://localhost");
/*  85 */       context.setAttribute("org.codehaus.activemq.connectionFactory", factory);
/*     */     } 
/*  87 */     queueConsumers = initQueueConsumers(context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebClient(ServletContext context) {
/*  97 */     this.context = context;
/*  98 */     initContext(context);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() throws JMSException {}
/*     */   
/*     */   public void stop() throws JMSException {
/*     */     try {
/* 106 */       this.connection.close();
/*     */     } finally {
/*     */       
/* 109 */       this.producer = null;
/* 110 */       this.session = null;
/* 111 */       this.connection = null;
/* 112 */       this.topicConsumers.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeExternal(ObjectOutput out) throws IOException {}
/*     */   
/*     */   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
/* 120 */     this.topicConsumers = new HashMap();
/*     */   }
/*     */   
/*     */   public void send(Destination destination, Message message) throws JMSException {
/* 124 */     if (this.producer == null) {
/* 125 */       this.producer = getSession().createProducer(null);
/*     */     }
/* 127 */     log.info("Sending to destination: " + destination);
/* 128 */     this.producer.send(destination, message);
/* 129 */     log.info("Sent! message: " + message);
/*     */   }
/*     */   
/*     */   public Session getSession() throws JMSException {
/* 133 */     if (this.session == null) {
/* 134 */       this.session = createSession();
/*     */     }
/* 136 */     return (Session)this.session;
/*     */   }
/*     */   
/*     */   public ActiveMQConnection getConnection() throws JMSException {
/* 140 */     if (this.connection == null) {
/* 141 */       this.connection = (ActiveMQConnection)factory.createConnection();
/* 142 */       this.connection.start();
/*     */     } 
/* 144 */     return this.connection;
/*     */   }
/*     */   
/*     */   public void sessionWillPassivate(HttpSessionEvent event) {
/*     */     try {
/* 149 */       stop();
/*     */     }
/* 151 */     catch (JMSException e) {
/* 152 */       log.warn("Could not close connection: " + e, (Throwable)e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sessionDidActivate(HttpSessionEvent event) {
/* 158 */     this.context = event.getSession().getServletContext();
/* 159 */     initContext(this.context);
/*     */   }
/*     */   
/*     */   public static Map initQueueConsumers(ServletContext context) {
/* 163 */     Map answer = (Map)context.getAttribute("org.codehaus.activemq.queueConsumers");
/* 164 */     if (answer == null) {
/* 165 */       answer = new HashMap();
/* 166 */       context.setAttribute("org.codehaus.activemq.queueConsumers", answer);
/*     */     } 
/* 168 */     return answer;
/*     */   }
/*     */   
/*     */   public static ConnectionFactory initConnectionFactory(ServletContext servletContext) {
/*     */     ActiveMQConnectionFactory activeMQConnectionFactory;
/* 173 */     ConnectionFactory connectionFactory = (ConnectionFactory)servletContext.getAttribute("org.codehaus.activemq.connectionFactory");
/* 174 */     if (connectionFactory == null) {
/* 175 */       String brokerURL = servletContext.getInitParameter("org.codehaus.activemq.brokerURL");
/*     */       
/* 177 */       servletContext.log("Value of: org.codehaus.activemq.brokerURL is: " + brokerURL);
/*     */       
/* 179 */       if (brokerURL == null) {
/* 180 */         brokerURL = "vm://localhost";
/*     */       }
/*     */       
/* 183 */       boolean embeddedBroker = MessageServletSupport.asBoolean(servletContext.getInitParameter("org.codehaus.activemq.embeddedBroker"));
/* 184 */       servletContext.log("Use embedded broker: " + embeddedBroker);
/*     */       
/* 186 */       ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);
/* 187 */       factory.setUseEmbeddedBroker(embeddedBroker);
/*     */       
/* 189 */       activeMQConnectionFactory = factory;
/* 190 */       servletContext.setAttribute("org.codehaus.activemq.connectionFactory", activeMQConnectionFactory);
/*     */     } 
/* 192 */     return (ConnectionFactory)activeMQConnectionFactory;
/*     */   }
/*     */   
/*     */   public synchronized MessageConsumer getConsumer(Destination destination) throws JMSException {
/* 196 */     if (destination instanceof javax.jms.Topic) {
/* 197 */       MessageConsumer consumer = (MessageConsumer)this.topicConsumers.get(destination);
/* 198 */       if (consumer == null) {
/* 199 */         consumer = this.session.createConsumer(destination);
/* 200 */         this.topicConsumers.put(destination, consumer);
/*     */       } 
/* 202 */       return consumer;
/*     */     } 
/*     */     
/* 205 */     synchronized (queueConsumers) {
/* 206 */       SessionConsumerPair pair = (SessionConsumerPair)queueConsumers.get(destination);
/* 207 */       if (pair == null) {
/* 208 */         pair = createSessionConsumerPair(destination);
/* 209 */         queueConsumers.put(destination, pair);
/*     */       } 
/* 211 */       return pair.consumer;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected ActiveMQSession createSession() throws JMSException {
/* 217 */     return (ActiveMQSession)getConnection().createSession(false, 1);
/*     */   }
/*     */   
/*     */   protected SessionConsumerPair createSessionConsumerPair(Destination destination) throws JMSException {
/* 221 */     SessionConsumerPair answer = new SessionConsumerPair();
/* 222 */     answer.session = (Session)createSession();
/* 223 */     answer.consumer = answer.session.createConsumer(destination);
/* 224 */     return answer;
/*     */   }
/*     */   
/*     */   public WebClient() {}
/*     */   
/*     */   protected static class SessionConsumerPair {
/*     */     public Session session;
/*     */     public MessageConsumer consumer;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\web\WebClient.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */