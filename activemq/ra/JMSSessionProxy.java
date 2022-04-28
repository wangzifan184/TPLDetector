/*     */ package org.codehaus.activemq.ra;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import javax.jms.BytesMessage;
/*     */ import javax.jms.Destination;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.MapMessage;
/*     */ import javax.jms.Message;
/*     */ import javax.jms.MessageConsumer;
/*     */ import javax.jms.MessageListener;
/*     */ import javax.jms.MessageProducer;
/*     */ import javax.jms.ObjectMessage;
/*     */ import javax.jms.Queue;
/*     */ import javax.jms.QueueBrowser;
/*     */ import javax.jms.QueueReceiver;
/*     */ import javax.jms.QueueSender;
/*     */ import javax.jms.QueueSession;
/*     */ import javax.jms.Session;
/*     */ import javax.jms.StreamMessage;
/*     */ import javax.jms.TemporaryQueue;
/*     */ import javax.jms.TemporaryTopic;
/*     */ import javax.jms.TextMessage;
/*     */ import javax.jms.Topic;
/*     */ import javax.jms.TopicPublisher;
/*     */ import javax.jms.TopicSession;
/*     */ import javax.jms.TopicSubscriber;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JMSSessionProxy
/*     */   implements Session, QueueSession, TopicSession
/*     */ {
/*     */   private final ActiveMQManagedConnection managedConnection;
/*     */   boolean closed = false;
/*     */   
/*     */   public JMSSessionProxy(ActiveMQManagedConnection managedConnection) {
/*  37 */     this.managedConnection = managedConnection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws JMSException {
/*  44 */     cleanup();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cleanup() {
/*  51 */     this.closed = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Session getSession() throws JMSException {
/*  58 */     if (this.closed) {
/*  59 */       throw new JMSException("Session closed.");
/*     */     }
/*  61 */     return this.managedConnection.getPhysicalSession();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void commit() throws JMSException {
/*  68 */     getSession().commit();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QueueBrowser createBrowser(Queue queue) throws JMSException {
/*  77 */     return getSession().createBrowser(queue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QueueBrowser createBrowser(Queue queue, String messageSelector) throws JMSException {
/*  88 */     return getSession().createBrowser(queue, messageSelector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BytesMessage createBytesMessage() throws JMSException {
/*  96 */     return getSession().createBytesMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageConsumer createConsumer(Destination destination) throws JMSException {
/* 106 */     return getSession().createConsumer(destination);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageConsumer createConsumer(Destination destination, String messageSelector) throws JMSException {
/* 117 */     return getSession().createConsumer(destination, messageSelector);
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
/*     */   public MessageConsumer createConsumer(Destination destination, String messageSelector, boolean NoLocal) throws JMSException {
/* 129 */     return getSession().createConsumer(destination, messageSelector, NoLocal);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TopicSubscriber createDurableSubscriber(Topic topic, String name) throws JMSException {
/* 140 */     return getSession().createDurableSubscriber(topic, name);
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
/*     */   public TopicSubscriber createDurableSubscriber(Topic topic, String name, String messageSelector, boolean noLocal) throws JMSException {
/* 153 */     return getSession().createDurableSubscriber(topic, name, messageSelector, noLocal);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapMessage createMapMessage() throws JMSException {
/* 162 */     return getSession().createMapMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message createMessage() throws JMSException {
/* 170 */     return getSession().createMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectMessage createObjectMessage() throws JMSException {
/* 178 */     return getSession().createObjectMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectMessage createObjectMessage(Serializable object) throws JMSException {
/* 188 */     return getSession().createObjectMessage(object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageProducer createProducer(Destination destination) throws JMSException {
/* 198 */     return getSession().createProducer(destination);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Queue createQueue(String queueName) throws JMSException {
/* 207 */     return getSession().createQueue(queueName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StreamMessage createStreamMessage() throws JMSException {
/* 215 */     return getSession().createStreamMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TemporaryQueue createTemporaryQueue() throws JMSException {
/* 223 */     return getSession().createTemporaryQueue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TemporaryTopic createTemporaryTopic() throws JMSException {
/* 231 */     return getSession().createTemporaryTopic();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextMessage createTextMessage() throws JMSException {
/* 239 */     return getSession().createTextMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextMessage createTextMessage(String text) throws JMSException {
/* 248 */     return getSession().createTextMessage(text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Topic createTopic(String topicName) throws JMSException {
/* 257 */     return getSession().createTopic(topicName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAcknowledgeMode() throws JMSException {
/* 265 */     return getSession().getAcknowledgeMode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageListener getMessageListener() throws JMSException {
/* 273 */     return getSession().getMessageListener();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getTransacted() throws JMSException {
/* 281 */     return getSession().getTransacted();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void recover() throws JMSException {
/* 288 */     getSession().recover();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rollback() throws JMSException {
/* 295 */     getSession().rollback();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageListener(MessageListener listener) throws JMSException {
/* 304 */     getSession().setMessageListener(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unsubscribe(String name) throws JMSException {
/* 313 */     getSession().unsubscribe(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QueueReceiver createReceiver(Queue queue) throws JMSException {
/* 322 */     return ((QueueSession)getSession()).createReceiver(queue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QueueReceiver createReceiver(Queue queue, String messageSelector) throws JMSException {
/* 333 */     return ((QueueSession)getSession()).createReceiver(queue, messageSelector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QueueSender createSender(Queue queue) throws JMSException {
/* 342 */     return ((QueueSession)getSession()).createSender(queue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TopicPublisher createPublisher(Topic topic) throws JMSException {
/* 351 */     return ((TopicSession)getSession()).createPublisher(topic);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TopicSubscriber createSubscriber(Topic topic) throws JMSException {
/* 360 */     return ((TopicSession)getSession()).createSubscriber(topic);
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
/*     */   public TopicSubscriber createSubscriber(Topic topic, String messageSelector, boolean noLocal) throws JMSException {
/* 372 */     return ((TopicSession)getSession()).createSubscriber(topic, messageSelector, noLocal);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 379 */     throw new RuntimeException("Operation not supported.");
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ra\JMSSessionProxy.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */