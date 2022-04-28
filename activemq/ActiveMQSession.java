/*      */ package org.codehaus.activemq;
/*      */ 
/*      */ import EDU.oswego.cs.dl.util.concurrent.CopyOnWriteArrayList;
/*      */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;
/*      */ import java.io.Serializable;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import javax.jms.BytesMessage;
/*      */ import javax.jms.Destination;
/*      */ import javax.jms.IllegalStateException;
/*      */ import javax.jms.JMSException;
/*      */ import javax.jms.MapMessage;
/*      */ import javax.jms.Message;
/*      */ import javax.jms.MessageConsumer;
/*      */ import javax.jms.MessageListener;
/*      */ import javax.jms.MessageProducer;
/*      */ import javax.jms.ObjectMessage;
/*      */ import javax.jms.Queue;
/*      */ import javax.jms.QueueBrowser;
/*      */ import javax.jms.QueueReceiver;
/*      */ import javax.jms.QueueSender;
/*      */ import javax.jms.QueueSession;
/*      */ import javax.jms.Session;
/*      */ import javax.jms.StreamMessage;
/*      */ import javax.jms.TemporaryQueue;
/*      */ import javax.jms.TemporaryTopic;
/*      */ import javax.jms.TextMessage;
/*      */ import javax.jms.Topic;
/*      */ import javax.jms.TopicPublisher;
/*      */ import javax.jms.TopicSession;
/*      */ import javax.jms.TopicSubscriber;
/*      */ import javax.management.j2ee.statistics.Stats;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.codehaus.activemq.management.JMSSessionStatsImpl;
/*      */ import org.codehaus.activemq.management.StatsCapable;
/*      */ import org.codehaus.activemq.message.ActiveMQBytesMessage;
/*      */ import org.codehaus.activemq.message.ActiveMQDestination;
/*      */ import org.codehaus.activemq.message.ActiveMQMapMessage;
/*      */ import org.codehaus.activemq.message.ActiveMQMessage;
/*      */ import org.codehaus.activemq.message.ActiveMQObjectMessage;
/*      */ import org.codehaus.activemq.message.ActiveMQQueue;
/*      */ import org.codehaus.activemq.message.ActiveMQStreamMessage;
/*      */ import org.codehaus.activemq.message.ActiveMQTemporaryQueue;
/*      */ import org.codehaus.activemq.message.ActiveMQTemporaryTopic;
/*      */ import org.codehaus.activemq.message.ActiveMQTextMessage;
/*      */ import org.codehaus.activemq.message.ActiveMQTopic;
/*      */ import org.codehaus.activemq.message.ConsumerInfo;
/*      */ import org.codehaus.activemq.message.DurableUnsubscribe;
/*      */ import org.codehaus.activemq.message.MessageAck;
/*      */ import org.codehaus.activemq.message.MessageAcknowledge;
/*      */ import org.codehaus.activemq.message.Packet;
/*      */ import org.codehaus.activemq.message.ProducerInfo;
/*      */ import org.codehaus.activemq.message.TransactionInfo;
/*      */ import org.codehaus.activemq.ra.LocalTransactionEventListener;
/*      */ import org.codehaus.activemq.service.impl.DefaultQueueList;
/*      */ import org.codehaus.activemq.util.IdGenerator;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ActiveMQSession
/*      */   implements Session, QueueSession, TopicSession, ActiveMQMessageDispatcher, MessageAcknowledge, StatsCapable
/*      */ {
/*      */   protected static final int CONSUMER_DISPATCH_UNSET = 1;
/*      */   protected static final int CONSUMER_DISPATCH_ASYNC = 2;
/*      */   protected static final int CONSUMER_DISPATCH_SYNC = 3;
/*  121 */   private static final Log log = LogFactory.getLog(ActiveMQSession.class);
/*      */   
/*      */   protected ActiveMQConnection connection;
/*      */   
/*      */   private int acknowledgeMode;
/*      */   
/*      */   protected CopyOnWriteArrayList consumers;
/*      */   
/*      */   protected CopyOnWriteArrayList producers;
/*      */   
/*      */   private IdGenerator transactionIdGenerator;
/*      */   
/*      */   private IdGenerator temporaryDestinationGenerator;
/*      */   
/*      */   protected IdGenerator packetIdGenerator;
/*      */   private IdGenerator producerIdGenerator;
/*      */   private IdGenerator consumerIdGenerator;
/*      */   private MessageListener messageListener;
/*      */   protected SynchronizedBoolean closed;
/*      */   private SynchronizedBoolean startTransaction;
/*      */   private String sessionId;
/*      */   protected String currentTransactionId;
/*      */   private long startTime;
/*      */   private LocalTransactionEventListener localTransactionEventListener;
/*      */   private DefaultQueueList deliveredMessages;
/*      */   private ActiveMQSessionExecutor messageExecutor;
/*      */   private JMSSessionStatsImpl stats;
/*      */   private int consumerDispatchState;
/*      */   
/*      */   protected ActiveMQSession(ActiveMQConnection theConnection, int theAcknowledgeMode) throws JMSException {
/*  151 */     this.connection = theConnection;
/*  152 */     this.acknowledgeMode = theAcknowledgeMode;
/*  153 */     this.consumers = new CopyOnWriteArrayList();
/*  154 */     this.producers = new CopyOnWriteArrayList();
/*  155 */     this.producerIdGenerator = new IdGenerator();
/*  156 */     this.consumerIdGenerator = new IdGenerator();
/*  157 */     this.transactionIdGenerator = new IdGenerator();
/*  158 */     this.temporaryDestinationGenerator = new IdGenerator();
/*  159 */     this.packetIdGenerator = new IdGenerator();
/*  160 */     this.closed = new SynchronizedBoolean(false);
/*  161 */     this.startTransaction = new SynchronizedBoolean(false);
/*  162 */     this.sessionId = this.connection.generateSessionId();
/*  163 */     this.startTime = System.currentTimeMillis();
/*  164 */     this.deliveredMessages = new DefaultQueueList();
/*  165 */     this.messageExecutor = new ActiveMQSessionExecutor(this, this.connection.getMemoryBoundedQueue(this.sessionId));
/*  166 */     if (getTransacted()) {
/*  167 */       this.currentTransactionId = getNextTransactionId();
/*      */     }
/*  169 */     this.connection.addSession(this);
/*  170 */     this.stats = new JMSSessionStatsImpl((List)this.producers, (List)this.consumers);
/*  171 */     this.consumerDispatchState = 1;
/*      */   }
/*      */   
/*      */   public Stats getStats() {
/*  175 */     return (Stats)this.stats;
/*      */   }
/*      */   
/*      */   public JMSSessionStatsImpl getSessionStats() {
/*  179 */     return this.stats;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BytesMessage createBytesMessage() throws JMSException {
/*  190 */     checkClosed();
/*  191 */     return (BytesMessage)new ActiveMQBytesMessage();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MapMessage createMapMessage() throws JMSException {
/*  203 */     checkClosed();
/*  204 */     return (MapMessage)new ActiveMQMapMessage();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Message createMessage() throws JMSException {
/*  216 */     checkClosed();
/*  217 */     return (Message)new ActiveMQMessage();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMessage createObjectMessage() throws JMSException {
/*  228 */     checkClosed();
/*  229 */     return (ObjectMessage)new ActiveMQObjectMessage();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectMessage createObjectMessage(Serializable object) throws JMSException {
/*  241 */     checkClosed();
/*  242 */     ActiveMQObjectMessage msg = new ActiveMQObjectMessage();
/*  243 */     msg.setObject(object);
/*  244 */     return (ObjectMessage)msg;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StreamMessage createStreamMessage() throws JMSException {
/*  255 */     checkClosed();
/*  256 */     return (StreamMessage)new ActiveMQStreamMessage();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TextMessage createTextMessage() throws JMSException {
/*  267 */     checkClosed();
/*  268 */     return (TextMessage)new ActiveMQTextMessage();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TextMessage createTextMessage(String text) throws JMSException {
/*  280 */     checkClosed();
/*  281 */     ActiveMQTextMessage msg = new ActiveMQTextMessage();
/*  282 */     msg.setText(text);
/*  283 */     return (TextMessage)msg;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getTransacted() throws JMSException {
/*  293 */     checkClosed();
/*  294 */     return (this.acknowledgeMode == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getAcknowledgeMode() throws JMSException {
/*  308 */     checkClosed();
/*  309 */     return this.acknowledgeMode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void commit() throws JMSException {
/*  322 */     checkClosed();
/*  323 */     if (!getTransacted()) {
/*  324 */       throw new IllegalStateException("Not a transacted session");
/*      */     }
/*      */     
/*  327 */     if (this.startTransaction.commit(true, false)) {
/*  328 */       TransactionInfo info = new TransactionInfo();
/*  329 */       info.setId(this.packetIdGenerator.generateId());
/*  330 */       info.setTransactionId(this.currentTransactionId);
/*  331 */       info.setType(103);
/*      */       
/*  333 */       this.currentTransactionId = getNextTransactionId();
/*      */       
/*  335 */       this.connection.syncSendPacket((Packet)info);
/*  336 */       if (this.localTransactionEventListener != null) {
/*  337 */         this.localTransactionEventListener.commitEvent();
/*      */       }
/*      */     } 
/*  340 */     this.deliveredMessages.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void rollback() throws JMSException {
/*  351 */     checkClosed();
/*  352 */     if (!getTransacted()) {
/*  353 */       throw new IllegalStateException("Not a transacted session");
/*      */     }
/*      */     
/*  356 */     if (this.startTransaction.commit(true, false)) {
/*  357 */       TransactionInfo info = new TransactionInfo();
/*  358 */       info.setId(this.packetIdGenerator.generateId());
/*  359 */       info.setTransactionId(this.currentTransactionId);
/*  360 */       info.setType(105);
/*      */       
/*  362 */       this.currentTransactionId = getNextTransactionId();
/*  363 */       this.connection.asyncSendPacket((Packet)info);
/*      */       
/*  365 */       if (this.localTransactionEventListener != null) {
/*  366 */         this.localTransactionEventListener.rollbackEvent();
/*      */       }
/*      */     } 
/*  369 */     redeliverUnacknowledgedMessages(true);
/*  370 */     this.deliveredMessages.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void close() throws JMSException {
/*  395 */     if (!this.closed.get()) {
/*  396 */       if (getTransacted()) {
/*  397 */         rollback();
/*      */       }
/*  399 */       doClose();
/*  400 */       this.closed.set(true);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void doClose() throws JMSException {
/*  405 */     doAcknowledge(true);
/*  406 */     for (Iterator iterator = this.consumers.iterator(); iterator.hasNext(); ) {
/*  407 */       ActiveMQMessageConsumer consumer = iterator.next();
/*  408 */       consumer.close();
/*      */     } 
/*  410 */     for (Iterator i = this.producers.iterator(); i.hasNext(); ) {
/*  411 */       ActiveMQMessageProducer producer = i.next();
/*  412 */       producer.close();
/*      */     } 
/*  414 */     this.consumers.clear();
/*  415 */     this.producers.clear();
/*  416 */     this.connection.removeSession(this);
/*  417 */     this.messageExecutor.close();
/*  418 */     this.deliveredMessages.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void checkClosed() throws IllegalStateException {
/*  425 */     if (this.closed.get()) {
/*  426 */       throw new IllegalStateException("The Consumer is closed");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void recover() throws JMSException {
/*  448 */     checkClosed();
/*  449 */     if (getTransacted()) {
/*  450 */       throw new IllegalStateException("This session is transacted");
/*      */     }
/*  452 */     redeliverUnacknowledgedMessages();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MessageListener getMessageListener() throws JMSException {
/*  465 */     checkClosed();
/*  466 */     return this.messageListener;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMessageListener(MessageListener listener) throws JMSException {
/*  484 */     checkClosed();
/*  485 */     this.messageListener = listener;
/*  486 */     if (listener != null) {
/*  487 */       this.messageExecutor.setDoDispatch(false);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void run() {
/*  497 */     MessageListener listener = this.messageListener;
/*  498 */     boolean doRemove = (this.acknowledgeMode != 2);
/*      */     ActiveMQMessage message;
/*  500 */     while ((message = this.messageExecutor.dequeueNoWait()) != null) {
/*  501 */       if (listener != null) {
/*      */         try {
/*  503 */           listener.onMessage((Message)message);
/*  504 */           messageDelivered(true, message, true);
/*      */         }
/*  506 */         catch (Throwable t) {
/*  507 */           log.info("Caught :" + t, t);
/*  508 */           messageDelivered(true, message, false);
/*      */         } 
/*      */         continue;
/*      */       } 
/*  512 */       messageDelivered(true, message, false);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MessageProducer createProducer(Destination destination) throws JMSException {
/*  532 */     checkClosed();
/*  533 */     return new ActiveMQMessageProducer(this, ActiveMQMessageTransformation.transformDestination(destination));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MessageConsumer createConsumer(Destination destination) throws JMSException {
/*  548 */     checkClosed();
/*  549 */     int prefetch = (destination instanceof Topic) ? this.connection.getPrefetchPolicy().getTopicPrefetch() : this.connection.getPrefetchPolicy().getQueuePrefetch();
/*      */     
/*  551 */     return new ActiveMQMessageConsumer(this, ActiveMQMessageTransformation.transformDestination(destination), "", "", this.connection.getNextConsumerNumber(), prefetch, false, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MessageConsumer createConsumer(Destination destination, String messageSelector) throws JMSException {
/*  572 */     checkClosed();
/*  573 */     int prefetch = (destination instanceof Topic) ? this.connection.getPrefetchPolicy().getTopicPrefetch() : this.connection.getPrefetchPolicy().getQueuePrefetch();
/*      */     
/*  575 */     return new ActiveMQMessageConsumer(this, ActiveMQMessageTransformation.transformDestination(destination), "", messageSelector, this.connection.getNextConsumerNumber(), prefetch, false, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MessageConsumer createConsumer(Destination destination, String messageSelector, boolean NoLocal) throws JMSException {
/*  608 */     checkClosed();
/*  609 */     int prefetch = this.connection.getPrefetchPolicy().getTopicPrefetch();
/*  610 */     return new ActiveMQMessageConsumer(this, ActiveMQMessageTransformation.transformDestination(destination), "", messageSelector, this.connection.getNextConsumerNumber(), prefetch, NoLocal, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Queue createQueue(String queueName) throws JMSException {
/*  631 */     checkClosed();
/*  632 */     return (Queue)new ActiveMQQueue(queueName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Topic createTopic(String topicName) throws JMSException {
/*  652 */     checkClosed();
/*  653 */     return (Topic)new ActiveMQTopic(topicName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TopicSubscriber createDurableSubscriber(Topic topic, String name) throws JMSException {
/*  691 */     checkClosed();
/*  692 */     return new ActiveMQTopicSubscriber(this, ActiveMQMessageTransformation.transformDestination((Destination)topic), name, "", this.connection.getNextConsumerNumber(), this.connection.getPrefetchPolicy().getDurableTopicPrefetch(), false, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TopicSubscriber createDurableSubscriber(Topic topic, String name, String messageSelector, boolean noLocal) throws JMSException {
/*  728 */     checkClosed();
/*  729 */     return new ActiveMQTopicSubscriber(this, ActiveMQMessageTransformation.transformDestination((Destination)topic), name, messageSelector, this.connection.getNextConsumerNumber(), this.connection.getPrefetchPolicy().getDurableTopicPrefetch(), noLocal, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public QueueBrowser createBrowser(Queue queue) throws JMSException {
/*  744 */     checkClosed();
/*  745 */     return new ActiveMQQueueBrowser(this, ActiveMQMessageTransformation.transformDestination((Destination)queue), "", this.connection.getNextConsumerNumber());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public QueueBrowser createBrowser(Queue queue, String messageSelector) throws JMSException {
/*  763 */     checkClosed();
/*  764 */     return new ActiveMQQueueBrowser(this, ActiveMQMessageTransformation.transformDestination((Destination)queue), messageSelector, this.connection.getNextConsumerNumber());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TemporaryQueue createTemporaryQueue() throws JMSException {
/*  777 */     checkClosed();
/*  778 */     String tempQueueName = "TemporaryQueue-" + ActiveMQDestination.createTemporaryName(this.connection.getInitializedClientID());
/*      */     
/*  780 */     tempQueueName = tempQueueName + this.temporaryDestinationGenerator.generateId();
/*  781 */     return (TemporaryQueue)new ActiveMQTemporaryQueue(tempQueueName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TemporaryTopic createTemporaryTopic() throws JMSException {
/*  793 */     checkClosed();
/*  794 */     String tempTopicName = "TemporaryTopic-" + ActiveMQDestination.createTemporaryName(this.connection.getInitializedClientID());
/*      */     
/*  796 */     tempTopicName = tempTopicName + this.temporaryDestinationGenerator.generateId();
/*  797 */     return (TemporaryTopic)new ActiveMQTemporaryTopic(tempTopicName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public QueueReceiver createReceiver(Queue queue) throws JMSException {
/*  809 */     checkClosed();
/*  810 */     return new ActiveMQQueueReceiver(this, ActiveMQDestination.transformDestination((Destination)queue), "", this.connection.getNextConsumerNumber(), this.connection.getPrefetchPolicy().getQueuePrefetch());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public QueueReceiver createReceiver(Queue queue, String messageSelector) throws JMSException {
/*  827 */     checkClosed();
/*  828 */     return new ActiveMQQueueReceiver(this, ActiveMQMessageTransformation.transformDestination((Destination)queue), messageSelector, this.connection.getNextConsumerNumber(), this.connection.getPrefetchPolicy().getQueuePrefetch());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public QueueSender createSender(Queue queue) throws JMSException {
/*  842 */     checkClosed();
/*  843 */     return new ActiveMQQueueSender(this, ActiveMQMessageTransformation.transformDestination((Destination)queue));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TopicSubscriber createSubscriber(Topic topic) throws JMSException {
/*  865 */     checkClosed();
/*  866 */     return new ActiveMQTopicSubscriber(this, ActiveMQMessageTransformation.transformDestination((Destination)topic), null, null, this.connection.getNextConsumerNumber(), this.connection.getPrefetchPolicy().getTopicPrefetch(), false, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TopicSubscriber createSubscriber(Topic topic, String messageSelector, boolean noLocal) throws JMSException {
/*  898 */     checkClosed();
/*  899 */     return new ActiveMQTopicSubscriber(this, ActiveMQMessageTransformation.transformDestination((Destination)topic), null, messageSelector, this.connection.getNextConsumerNumber(), this.connection.getPrefetchPolicy().getTopicPrefetch(), noLocal, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TopicPublisher createPublisher(Topic topic) throws JMSException {
/*  917 */     checkClosed();
/*  918 */     return new ActiveMQTopicPublisher(this, ActiveMQMessageTransformation.transformDestination((Destination)topic));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void unsubscribe(String name) throws JMSException {
/*  936 */     checkClosed();
/*  937 */     DurableUnsubscribe ds = new DurableUnsubscribe();
/*  938 */     ds.setId(this.packetIdGenerator.generateId());
/*  939 */     ds.setClientId(this.connection.getClientID());
/*  940 */     ds.setSubscriberName(name);
/*  941 */     this.connection.syncSendPacket((Packet)ds);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTarget(ActiveMQMessage message) {
/*  951 */     for (Iterator i = this.consumers.iterator(); i.hasNext(); ) {
/*  952 */       ActiveMQMessageConsumer consumer = i.next();
/*  953 */       if (message.isConsumerTarget(consumer.getConsumerNumber())) {
/*  954 */         return true;
/*      */       }
/*      */     } 
/*  957 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void dispatch(ActiveMQMessage message) {
/*  966 */     message.setMessageAcknowledge(this);
/*  967 */     this.messageExecutor.execute(message);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void acknowledge() throws JMSException {
/*  992 */     doAcknowledge(false);
/*      */   }
/*      */   
/*      */   protected void doAcknowledge(boolean isClosing) throws JMSException {
/*  996 */     checkClosed();
/*  997 */     if (this.acknowledgeMode == 2) {
/*  998 */       ActiveMQMessage msg = null;
/*  999 */       while ((msg = (ActiveMQMessage)this.deliveredMessages.removeFirst()) != null) {
/* 1000 */         MessageAck ack = new MessageAck();
/* 1001 */         ack.setConsumerId(msg.getConsumerId());
/* 1002 */         ack.setMessageID(msg.getJMSMessageID());
/* 1003 */         if (!isClosing) {
/* 1004 */           ack.setMessageRead(msg.isMessageConsumed());
/*      */         }
/* 1006 */         ack.setId(this.packetIdGenerator.generateId());
/* 1007 */         ack.setDestination(msg.getJMSActiveMQDestination());
/* 1008 */         ack.setPersistent((msg.getJMSDeliveryMode() == 2));
/* 1009 */         this.connection.asyncSendPacket((Packet)ack, false);
/*      */       } 
/* 1011 */       this.deliveredMessages.clear();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void messageDelivered(boolean sendAcknowledge, ActiveMQMessage message, boolean messageConsumed) {
/* 1016 */     if (message != null && !this.closed.get()) {
/* 1017 */       if (isClientAcknowledge() || (isTransacted() && message.isTransientConsumed())) {
/* 1018 */         message.setMessageConsumed(messageConsumed);
/* 1019 */         this.deliveredMessages.add(message);
/*      */       } 
/* 1021 */       if (sendAcknowledge) {
/*      */         try {
/* 1023 */           doStartTransaction();
/* 1024 */           MessageAck ack = new MessageAck();
/* 1025 */           ack.setConsumerId(message.getConsumerId());
/* 1026 */           ack.setTransactionId(this.currentTransactionId);
/* 1027 */           ack.setMessageID(message.getJMSMessageID());
/* 1028 */           ack.setMessageRead(messageConsumed);
/* 1029 */           ack.setId(this.packetIdGenerator.generateId());
/* 1030 */           ack.setXaTransacted(isXaTransacted());
/* 1031 */           ack.setDestination(message.getJMSActiveMQDestination());
/* 1032 */           ack.setPersistent((message.getJMSDeliveryMode() == 2));
/* 1033 */           this.connection.asyncSendPacket((Packet)ack);
/*      */         }
/* 1035 */         catch (JMSException e) {
/* 1036 */           log.warn("failed to notify Broker that message is delivered", (Throwable)e);
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addConsumer(ActiveMQMessageConsumer consumer) throws JMSException {
/* 1048 */     if (consumer.isDurableSubscriber()) {
/* 1049 */       this.stats.onCreateDurableSubscriber();
/*      */     }
/* 1051 */     consumer.setConsumerId(this.consumerIdGenerator.generateId());
/* 1052 */     ConsumerInfo info = createConsumerInfo(consumer);
/* 1053 */     info.setStarted(true);
/* 1054 */     this.connection.syncSendPacket((Packet)info);
/* 1055 */     this.consumers.add(consumer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void removeConsumer(ActiveMQMessageConsumer consumer) throws JMSException {
/* 1063 */     this.consumers.remove(consumer);
/*      */     
/* 1065 */     if (consumer.isDurableSubscriber()) {
/* 1066 */       this.stats.onRemoveDurableSubscriber();
/*      */     }
/* 1068 */     if (!this.closed.get()) {
/* 1069 */       ConsumerInfo info = createConsumerInfo(consumer);
/* 1070 */       info.setStarted(false);
/* 1071 */       this.connection.asyncSendPacket((Packet)info, false);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected ConsumerInfo createConsumerInfo(ActiveMQMessageConsumer consumer) throws JMSException {
/* 1076 */     ConsumerInfo info = new ConsumerInfo();
/* 1077 */     info.setConsumerId(consumer.consumerId);
/* 1078 */     info.setClientId(this.connection.clientID);
/* 1079 */     info.setSessionId(this.sessionId);
/* 1080 */     info.setConsumerNo(consumer.consumerNumber);
/* 1081 */     info.setPrefetchNumber(consumer.prefetchNumber);
/* 1082 */     info.setDestination(consumer.destination);
/* 1083 */     info.setId(this.packetIdGenerator.generateId());
/* 1084 */     info.setNoLocal(consumer.noLocal);
/* 1085 */     info.setBrowser(consumer.browser);
/* 1086 */     info.setSelector(consumer.messageSelector);
/* 1087 */     info.setStartTime(consumer.startTime);
/* 1088 */     info.setConsumerName(consumer.consumerName);
/* 1089 */     return info;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addProducer(ActiveMQMessageProducer producer) throws JMSException {
/* 1097 */     producer.setProducerId(this.producerIdGenerator.generateId());
/* 1098 */     ProducerInfo info = createProducerInfo(producer);
/* 1099 */     info.setStarted(true);
/* 1100 */     this.connection.syncSendPacket((Packet)info);
/* 1101 */     this.producers.add(producer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void removeProducer(ActiveMQMessageProducer producer) throws JMSException {
/* 1109 */     this.producers.remove(producer);
/* 1110 */     if (!this.closed.get()) {
/* 1111 */       ProducerInfo info = createProducerInfo(producer);
/* 1112 */       info.setStarted(false);
/* 1113 */       this.connection.asyncSendPacket((Packet)info, false);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected ProducerInfo createProducerInfo(ActiveMQMessageProducer producer) throws JMSException {
/* 1118 */     ProducerInfo info = new ProducerInfo();
/* 1119 */     info.setProducerId(producer.getProducerId());
/* 1120 */     info.setClientId(this.connection.clientID);
/* 1121 */     info.setSessionId(this.sessionId);
/* 1122 */     info.setDestination(producer.defaultDestination);
/* 1123 */     info.setId(this.packetIdGenerator.generateId());
/* 1124 */     info.setStartTime(producer.getStartTime());
/* 1125 */     return info;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void start() {
/* 1132 */     this.messageExecutor.start();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void stop() {
/* 1139 */     this.messageExecutor.stop();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getSessionId() {
/* 1146 */     return this.sessionId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setSessionId(String sessionId) {
/* 1153 */     this.sessionId = sessionId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected long getStartTime() {
/* 1160 */     return this.startTime;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setStartTime(long startTime) {
/* 1167 */     this.startTime = startTime;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void send(ActiveMQMessageProducer producer, Destination destination, Message message, int deliveryMode, int priority, long timeToLive, boolean reuseMessageId) throws JMSException {
/* 1183 */     checkClosed();
/*      */     
/* 1185 */     this.connection.sendConnectionInfoToBroker();
/*      */     
/* 1187 */     doStartTransaction();
/* 1188 */     message.setJMSDestination(destination);
/* 1189 */     message.setJMSDeliveryMode(deliveryMode);
/* 1190 */     message.setJMSPriority(priority);
/* 1191 */     long expiration = 0L;
/* 1192 */     if (!producer.getDisableMessageTimestamp()) {
/* 1193 */       long timeStamp = System.currentTimeMillis();
/* 1194 */       message.setJMSTimestamp(timeStamp);
/* 1195 */       if (timeToLive > 0L) {
/* 1196 */         expiration = timeToLive + timeStamp;
/*      */       }
/*      */     } 
/* 1199 */     message.setJMSExpiration(expiration);
/* 1200 */     String id = message.getJMSMessageID();
/* 1201 */     if (id == null || id.length() == 0 || (!producer.getDisableMessageID() && !reuseMessageId)) {
/* 1202 */       message.setJMSMessageID(producer.getIdGenerator().generateId());
/*      */     }
/*      */     
/* 1205 */     ActiveMQMessage msg = ActiveMQMessageTransformation.transformMessage(message);
/* 1206 */     msg.prepareMessageBody();
/* 1207 */     msg.setProducerID(producer.getProducerId());
/* 1208 */     msg.setTransactionId(this.currentTransactionId);
/* 1209 */     msg.setXaTransacted(isXaTransacted());
/* 1210 */     msg.setJMSClientID(this.connection.clientID);
/* 1211 */     msg.setJMSRedelivered(false);
/* 1212 */     if (log.isDebugEnabled()) {
/* 1213 */       log.debug("Sending message: " + msg);
/*      */     }
/*      */     
/* 1216 */     if (this.connection.isUseAsyncSend() || this.acknowledgeMode == 0 || deliveryMode == 1) {
/*      */       
/* 1218 */       this.connection.asyncSendPacket((Packet)msg);
/*      */     } else {
/*      */       
/* 1221 */       this.connection.syncSendPacket((Packet)msg);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void doStartTransaction() throws JMSException {
/* 1231 */     if (getTransacted() && 
/* 1232 */       this.startTransaction.commit(false, true)) {
/* 1233 */       TransactionInfo info = new TransactionInfo();
/* 1234 */       info.setId(this.packetIdGenerator.generateId());
/* 1235 */       info.setTransactionId(this.currentTransactionId);
/* 1236 */       info.setType(101);
/* 1237 */       this.connection.asyncSendPacket((Packet)info);
/*      */       
/* 1239 */       if (this.localTransactionEventListener != null) {
/* 1240 */         this.localTransactionEventListener.beginEvent();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LocalTransactionEventListener getLocalTransactionEventListener() {
/* 1250 */     return this.localTransactionEventListener;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLocalTransactionEventListener(LocalTransactionEventListener localTransactionEventListener) {
/* 1259 */     this.localTransactionEventListener = localTransactionEventListener;
/*      */   }
/*      */   
/*      */   protected boolean isXaTransacted() {
/* 1263 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getNextTransactionId() {
/* 1269 */     return this.transactionIdGenerator.generateId();
/*      */   }
/*      */   
/*      */   protected void setSessionConsumerDispatchState(int value) throws JMSException {
/* 1273 */     if (this.consumerDispatchState != 1 && value != this.consumerDispatchState) {
/* 1274 */       String errorStr = "Cannot mix consumer dispatching on a session - already: ";
/* 1275 */       if (value == 3) {
/* 1276 */         errorStr = errorStr + "synchronous";
/*      */       } else {
/*      */         
/* 1279 */         errorStr = errorStr + "asynchronous";
/*      */       } 
/* 1281 */       throw new IllegalStateException(errorStr);
/*      */     } 
/* 1283 */     this.consumerDispatchState = value;
/*      */   }
/*      */   
/*      */   protected void redeliverUnacknowledgedMessages() {
/* 1287 */     redeliverUnacknowledgedMessages(false);
/*      */   }
/*      */   
/*      */   protected void redeliverUnacknowledgedMessages(boolean onlyDeliverTransientConsumed) {
/* 1291 */     this.messageExecutor.stop();
/* 1292 */     LinkedList replay = new LinkedList();
/* 1293 */     Object obj = null;
/* 1294 */     while ((obj = this.deliveredMessages.removeFirst()) != null) {
/* 1295 */       replay.add(obj);
/*      */     }
/* 1297 */     this.deliveredMessages.clear();
/* 1298 */     if (!replay.isEmpty()) {
/* 1299 */       for (ListIterator i = replay.listIterator(replay.size()); i.hasPrevious(); ) {
/* 1300 */         ActiveMQMessage msg = i.previous();
/* 1301 */         if (!onlyDeliverTransientConsumed || msg.isTransientConsumed()) {
/* 1302 */           msg.setJMSRedelivered(true);
/* 1303 */           this.messageExecutor.executeFirst(msg);
/*      */         } 
/*      */       } 
/*      */     }
/* 1307 */     replay.clear();
/* 1308 */     this.messageExecutor.start();
/*      */   }
/*      */   
/*      */   protected void clearMessagesInProgress() {
/* 1312 */     this.messageExecutor.clearMessagesInProgress();
/* 1313 */     for (Iterator i = this.consumers.iterator(); i.hasNext(); ) {
/* 1314 */       ActiveMQMessageConsumer consumer = i.next();
/* 1315 */       consumer.clearMessagesInProgress();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected boolean isTransacted() {
/* 1320 */     return (this.acknowledgeMode == 0);
/*      */   }
/*      */   
/*      */   protected boolean isClientAcknowledge() {
/* 1324 */     return (this.acknowledgeMode == 2);
/*      */   }
/*      */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ActiveMQSession.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */