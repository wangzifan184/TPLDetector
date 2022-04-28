/*     */ package org.codehaus.activemq.broker.impl;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.CopyOnWriteArrayList;
/*     */ import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;
/*     */ import EDU.oswego.cs.dl.util.concurrent.ThreadedExecutor;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import javax.jms.ExceptionListener;
/*     */ import javax.jms.JMSException;
/*     */ import javax.transaction.xa.XAException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.broker.BrokerClient;
/*     */ import org.codehaus.activemq.broker.BrokerConnector;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.ActiveMQXid;
/*     */ import org.codehaus.activemq.message.BrokerInfo;
/*     */ import org.codehaus.activemq.message.CapacityInfo;
/*     */ import org.codehaus.activemq.message.ConnectionInfo;
/*     */ import org.codehaus.activemq.message.ConsumerInfo;
/*     */ import org.codehaus.activemq.message.DurableUnsubscribe;
/*     */ import org.codehaus.activemq.message.IntResponseReceipt;
/*     */ import org.codehaus.activemq.message.MessageAck;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.PacketListener;
/*     */ import org.codehaus.activemq.message.ProducerInfo;
/*     */ import org.codehaus.activemq.message.Receipt;
/*     */ import org.codehaus.activemq.message.ResponseReceipt;
/*     */ import org.codehaus.activemq.message.SessionInfo;
/*     */ import org.codehaus.activemq.message.TransactionInfo;
/*     */ import org.codehaus.activemq.message.XATransactionInfo;
/*     */ import org.codehaus.activemq.message.util.BoundedPacketQueue;
/*     */ import org.codehaus.activemq.message.util.SpooledBoundedPacketQueue;
/*     */ import org.codehaus.activemq.transport.TransportChannel;
/*     */ import org.codehaus.activemq.util.IdGenerator;
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
/*     */ public class BrokerClientImpl
/*     */   implements BrokerClient, ExceptionListener, PacketListener
/*     */ {
/*  62 */   private static final Log log = LogFactory.getLog(BrokerClientImpl.class);
/*     */   private BrokerConnector brokerConnector;
/*     */   private TransportChannel channel;
/*     */   private ConnectionInfo connectionInfo;
/*     */   private IdGenerator packetIdGenerator;
/*     */   private SynchronizedBoolean closed;
/*     */   private Set activeConsumers;
/*     */   private CopyOnWriteArrayList consumers;
/*     */   private CopyOnWriteArrayList producers;
/*     */   private CopyOnWriteArrayList transactions;
/*     */   private CopyOnWriteArrayList xatransactions;
/*     */   private CopyOnWriteArrayList sessions;
/*     */   private boolean started;
/*     */   private boolean brokerConnection;
/*     */   private boolean clusteredConnection;
/*     */   private String remoteBrokerName;
/*  78 */   private int capacity = 100;
/*     */   
/*     */   private SpooledBoundedPacketQueue spoolQueue;
/*     */   
/*     */   private boolean cleanedUp;
/*     */ 
/*     */   
/*     */   public BrokerClientImpl() {
/*  86 */     this.packetIdGenerator = new IdGenerator();
/*  87 */     this.closed = new SynchronizedBoolean(false);
/*  88 */     this.activeConsumers = new HashSet();
/*  89 */     this.consumers = new CopyOnWriteArrayList();
/*  90 */     this.producers = new CopyOnWriteArrayList();
/*  91 */     this.transactions = new CopyOnWriteArrayList();
/*  92 */     this.xatransactions = new CopyOnWriteArrayList();
/*  93 */     this.sessions = new CopyOnWriteArrayList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initialize(BrokerConnector brokerConnector, TransportChannel channel) {
/* 103 */     this.brokerConnector = brokerConnector;
/* 104 */     this.channel = channel;
/* 105 */     this.channel.setPacketListener(this);
/* 106 */     this.channel.setExceptionListener(this);
/* 107 */     log.trace("brokerConnectorConnector client initialized");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BrokerConnector getBrokerConnector() {
/* 114 */     return this.brokerConnector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onException(JMSException jmsEx) {
/* 121 */     log.warn(this + " caught exception ", (Throwable)jmsEx);
/* 122 */     close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 129 */     String str = "brokerConnector-client:(" + hashCode() + ") ";
/* 130 */     str = str + ((this.connectionInfo == null) ? "" : this.connectionInfo.getClientId());
/* 131 */     str = str + ": " + this.channel;
/* 132 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispatch(ActiveMQMessage message) {
/* 141 */     if (isSlowConsumer()) {
/* 142 */       if (this.spoolQueue == null) {
/* 143 */         log.warn("Connection: " + this.connectionInfo.getClientId() + " is a slow consumer");
/* 144 */         String spoolName = this.brokerConnector.getBrokerInfo().getBrokerName() + "_" + this.connectionInfo.getClientId();
/*     */         try {
/* 146 */           this.spoolQueue = new SpooledBoundedPacketQueue(this.brokerConnector.getBrokerContainer().getBroker().getTempDir(), spoolName);
/*     */           
/* 148 */           final SpooledBoundedPacketQueue bpq = this.spoolQueue;
/* 149 */           ThreadedExecutor exec = new ThreadedExecutor();
/* 150 */           exec.execute(new Runnable() { private final BoundedPacketQueue val$bpq; private final BrokerClientImpl this$0;
/*     */                 public void run() {
/* 152 */                   while (!BrokerClientImpl.this.closed.get()) {
/*     */                     try {
/* 154 */                       Packet packet = bpq.dequeue();
/*     */                     }
/* 156 */                     catch (InterruptedException e) {
/* 157 */                       BrokerClientImpl.log.warn("async dispatch got an interupt", e);
/*     */                     }
/* 159 */                     catch (JMSException e) {
/* 160 */                       BrokerClientImpl.log.error("async dispatch got an problem", (Throwable)e);
/*     */                     }
/*     */                   
/*     */                   } 
/*     */                 } }
/*     */             );
/* 166 */         } catch (IOException e) {
/* 167 */           log.error("Could not create SpooledBoundedQueue for this slow consumer", e);
/* 168 */           close();
/*     */         }
/* 170 */         catch (InterruptedException e) {
/* 171 */           log.error("Could not create SpooledBoundedQueue for this slow consumer", e);
/* 172 */           close();
/*     */         } 
/*     */       } 
/* 175 */       if (this.spoolQueue != null) {
/*     */         try {
/* 177 */           this.spoolQueue.enqueue((Packet)message);
/*     */         }
/* 179 */         catch (JMSException e) {
/* 180 */           log.error("Could not enqueue message " + message + " to SpooledBoundedQueue for this slow consumer", (Throwable)e);
/*     */ 
/*     */           
/* 183 */           close();
/*     */         } 
/*     */       }
/*     */     } else {
/*     */       
/* 188 */       send((Packet)message);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBrokerConnection() {
/* 196 */     return this.brokerConnection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClusteredConnection() {
/* 203 */     return this.clusteredConnection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCapacity() {
/* 214 */     return this.capacity;
/*     */   }
/*     */   
/*     */   public String getClientID() {
/* 218 */     if (this.connectionInfo != null) {
/* 219 */       return this.connectionInfo.getClientId();
/*     */     }
/* 221 */     return null;
/*     */   }
/*     */   
/*     */   public TransportChannel getChannel() {
/* 225 */     return this.channel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSlowConsumer() {
/* 234 */     return (this.capacity <= 20);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void consume(Packet packet) {
/* 243 */     if (!this.closed.get() && packet != null) {
/* 244 */       Throwable requestEx = null;
/* 245 */       boolean failed = false;
/* 246 */       String brokerName = this.brokerConnector.getBrokerInfo().getBrokerName();
/* 247 */       String clusterName = this.brokerConnector.getBrokerInfo().getClusterName();
/*     */       try {
/* 249 */         if (this.brokerConnection) {
/* 250 */           packet.addBrokerVisited(this.remoteBrokerName);
/* 251 */           packet.addBrokerVisited(brokerName);
/*     */         } 
/*     */         
/* 254 */         if (packet.isJMSMessage()) {
/* 255 */           ActiveMQMessage message = (ActiveMQMessage)packet;
/*     */ 
/*     */           
/* 258 */           if (this.connectionInfo != null) {
/* 259 */             message.setProducerID(this.connectionInfo.getClientId());
/*     */           } else {
/*     */             
/* 262 */             log.warn("No connection info available! Maybe the client forgot to start() the Connection?");
/*     */           } 
/* 264 */           if (!this.brokerConnection) {
/* 265 */             message.setEntryBrokerName(brokerName);
/* 266 */             message.setEntryClusterName(clusterName);
/*     */           } 
/* 268 */           consumeActiveMQMessage(message);
/*     */         } else {
/*     */           MessageAck ack; XATransactionInfo xATransactionInfo; TransactionInfo transactionInfo; ConsumerInfo consumerInfo; ProducerInfo producerInfo; SessionInfo sessionInfo; ConnectionInfo connectionInfo; DurableUnsubscribe ds; CapacityInfo info;
/* 271 */           switch (packet.getPacketType()) {
/*     */             case 15:
/* 273 */               ack = (MessageAck)packet;
/* 274 */               consumeMessageAck(ack);
/*     */               break;
/*     */             
/*     */             case 20:
/* 278 */               xATransactionInfo = (XATransactionInfo)packet;
/* 279 */               consumeXATransactionInfo(xATransactionInfo);
/*     */               break;
/*     */             
/*     */             case 19:
/* 283 */               transactionInfo = (TransactionInfo)packet;
/* 284 */               consumeTransactionInfo(transactionInfo);
/*     */               break;
/*     */             
/*     */             case 17:
/* 288 */               consumerInfo = (ConsumerInfo)packet;
/* 289 */               consumeConsumerInfo(consumerInfo);
/*     */               break;
/*     */             
/*     */             case 18:
/* 293 */               producerInfo = (ProducerInfo)packet;
/* 294 */               consumeProducerInfo(producerInfo);
/*     */               break;
/*     */             
/*     */             case 23:
/* 298 */               sessionInfo = (SessionInfo)packet;
/* 299 */               consumeSessionInfo(sessionInfo);
/*     */               break;
/*     */             
/*     */             case 22:
/* 303 */               connectionInfo = (ConnectionInfo)packet;
/* 304 */               consumeConnectionInfo(connectionInfo);
/*     */               break;
/*     */             
/*     */             case 24:
/* 308 */               ds = (DurableUnsubscribe)packet;
/* 309 */               this.brokerConnector.durableUnsubscribe(this, ds);
/*     */               break;
/*     */             
/*     */             case 27:
/* 313 */               info = (CapacityInfo)packet;
/* 314 */               consumeCapacityInfo(info);
/*     */               break;
/*     */             
/*     */             case 28:
/* 318 */               updateCapacityInfo(packet.getId());
/*     */               break;
/*     */             
/*     */             case 21:
/* 322 */               consumeBrokerInfo((BrokerInfo)packet);
/*     */               break;
/*     */             
/*     */             default:
/* 326 */               log.warn("Unknown Packet received: " + packet);
/*     */               break;
/*     */           } 
/*     */ 
/*     */         
/*     */         } 
/* 332 */       } catch (Throwable e) {
/* 333 */         requestEx = e;
/* 334 */         log.warn("caught exception consuming packet: " + packet, e);
/* 335 */         failed = true;
/*     */       } 
/* 337 */       sendReceipt(packet, requestEx, failed);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void consumeConsumerInfo(ConsumerInfo info) throws JMSException {
/* 348 */     if (info.isStarted()) {
/* 349 */       this.consumers.add(info);
/* 350 */       if (this.connectionInfo != null && this.connectionInfo.isStarted() && 
/* 351 */         this.activeConsumers.add(info)) {
/* 352 */         this.brokerConnector.registerMessageConsumer(this, info);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 357 */       this.consumers.remove(info);
/* 358 */       if (this.activeConsumers.remove(info)) {
/* 359 */         this.brokerConnector.deregisterMessageConsumer(this, info);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateBrokerCapacity(int capacity) {
/* 370 */     CapacityInfo info = new CapacityInfo();
/* 371 */     info.setResourceName(this.brokerConnector.getBrokerInfo().getBrokerName());
/* 372 */     info.setCapacity(capacity);
/* 373 */     info.setFlowControlTimeout(getFlowControlTimeout(capacity));
/* 374 */     send((Packet)info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void consumeConnectionInfo(ConnectionInfo info) throws JMSException {
/* 384 */     this.connectionInfo = info;
/* 385 */     if (info.isClosed()) {
/* 386 */       cleanUp();
/*     */       try {
/* 388 */         sendReceipt((Packet)info);
/* 389 */         info.setReceiptRequired(false);
/*     */         try {
/* 391 */           Thread.sleep(500L);
/*     */         }
/* 393 */         catch (Throwable e) {}
/*     */       }
/*     */       finally {
/*     */         
/* 397 */         close();
/*     */       } 
/*     */     } else {
/*     */       
/* 401 */       if (!this.started && info.isStarted()) {
/* 402 */         this.started = true;
/* 403 */         log.debug(this + " has started running client version " + info.getClientVersion() + " , wire format = " + info.getWireFormatVersion());
/*     */         
/* 405 */         this.brokerConnector.registerClient(this, info);
/*     */         
/* 407 */         for (Iterator iterator = this.consumers.iterator(); iterator.hasNext(); ) {
/* 408 */           ConsumerInfo ci = iterator.next();
/* 409 */           ci.setClientId(info.getClientId());
/*     */         } 
/* 411 */         for (Iterator i = this.producers.iterator(); i.hasNext(); ) {
/* 412 */           ProducerInfo pi = i.next();
/* 413 */           pi.setClientId(info.getClientId());
/*     */         } 
/*     */       } 
/* 416 */       if (info.isStarted()) {
/*     */         
/* 418 */         for (Iterator i = this.consumers.iterator(); i.hasNext(); ) {
/* 419 */           ConsumerInfo ci = i.next();
/* 420 */           if (this.activeConsumers.add(ci)) {
/* 421 */             this.brokerConnector.registerMessageConsumer(this, ci);
/*     */           }
/*     */         } 
/*     */       } else {
/*     */         
/* 426 */         log.debug(this + " has stopped");
/*     */         
/* 428 */         for (Iterator i = this.consumers.iterator(); i.hasNext(); ) {
/* 429 */           ConsumerInfo ci = i.next();
/* 430 */           if (this.activeConsumers.remove(ci)) {
/* 431 */             this.brokerConnector.deregisterMessageConsumer(this, ci);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() throws JMSException {
/* 444 */     this.channel.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() throws JMSException {
/* 453 */     log.trace("Stopping channel: " + this.channel);
/* 454 */     this.channel.stop();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void cleanUp() {
/* 461 */     if (!this.cleanedUp) {
/* 462 */       this.cleanedUp = true;
/*     */       try {
/*     */         try {
/* 465 */           for (Iterator iterator3 = this.consumers.iterator(); iterator3.hasNext(); ) {
/* 466 */             ConsumerInfo info = iterator3.next();
/* 467 */             info.setStarted(false);
/* 468 */             this.brokerConnector.deregisterMessageConsumer(this, info);
/*     */           } 
/* 470 */           for (Iterator iterator2 = this.producers.iterator(); iterator2.hasNext(); ) {
/* 471 */             ProducerInfo info = iterator2.next();
/* 472 */             info.setStarted(false);
/* 473 */             this.brokerConnector.deregisterMessageProducer(this, info);
/*     */           } 
/* 475 */           for (Iterator iterator1 = this.sessions.iterator(); iterator1.hasNext(); ) {
/* 476 */             SessionInfo info = iterator1.next();
/* 477 */             info.setStarted(false);
/* 478 */             this.brokerConnector.deregisterSession(this, info);
/*     */           } 
/* 480 */           for (Iterator iterator = this.transactions.iterator(); iterator.hasNext();) {
/* 481 */             this.brokerConnector.rollbackTransaction(this, iterator.next().toString());
/*     */           }
/* 483 */           for (Iterator i = this.xatransactions.iterator(); i.hasNext();) {
/*     */             try {
/* 485 */               this.brokerConnector.rollbackTransaction(this, i.next());
/*     */             }
/* 487 */             catch (XAException e) {
/* 488 */               log.warn("Transaction rollback failed:", e);
/*     */             }
/*     */           
/*     */           } 
/*     */         } finally {
/*     */           
/* 494 */           if (log.isDebugEnabled()) {
/* 495 */             log.debug(this + " has stopped");
/*     */           }
/* 497 */           this.consumers.clear();
/* 498 */           this.producers.clear();
/* 499 */           this.transactions.clear();
/* 500 */           this.xatransactions.clear();
/* 501 */           this.sessions.clear();
/* 502 */           this.brokerConnector.deregisterClient(this, this.connectionInfo);
/*     */         }
/*     */       
/* 505 */       } catch (JMSException e) {
/* 506 */         log.warn("failed to de-register Broker client: " + e, (Throwable)e);
/*     */       } 
/*     */     } else {
/*     */       
/* 510 */       log.debug("We are ignoring a duplicate cleanup() method called for: " + this);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void send(Packet packet) {
/* 517 */     if (!this.closed.get()) {
/*     */       try {
/* 519 */         if (this.brokerConnection) {
/* 520 */           String brokerName = this.brokerConnector.getBrokerContainer().getBroker().getBrokerName();
/* 521 */           packet.addBrokerVisited(brokerName);
/* 522 */           if (packet.hasVisited(this.remoteBrokerName)) {
/* 523 */             if (log.isDebugEnabled()) {
/* 524 */               log.debug("Not Forwarding: " + this.remoteBrokerName + " has already been visited by packet: " + packet);
/*     */             }
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 530 */         this.channel.asyncSend(packet);
/*     */       }
/* 532 */       catch (JMSException e) {
/* 533 */         log.warn(this + " caught exception ", (Throwable)e);
/* 534 */         close();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected void close() {
/* 540 */     if (this.closed.commit(false, true)) {
/* 541 */       this.channel.stop();
/* 542 */       log.debug(this + " has closed");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void consumeActiveMQMessage(ActiveMQMessage message) throws JMSException {
/* 553 */     message = message.shallowCopy();
/* 554 */     if (message.isPartOfTransaction()) {
/* 555 */       this.brokerConnector.sendTransactedMessage(this, message.getTransactionId(), message);
/*     */     } else {
/*     */       
/* 558 */       this.brokerConnector.sendMessage(this, message);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void consumeMessageAck(MessageAck ack) throws JMSException {
/* 569 */     if (ack.isPartOfTransaction()) {
/* 570 */       this.brokerConnector.acknowledgeTransactedMessage(this, ack.getTransactionId(), ack);
/*     */     } else {
/*     */       
/* 573 */       this.brokerConnector.acknowledgeMessage(this, ack);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void consumeTransactionInfo(TransactionInfo info) throws JMSException {
/* 584 */     if (info.getType() == 101) {
/* 585 */       this.transactions.add(info.getTransactionId());
/* 586 */       this.brokerConnector.startTransaction(this, info.getTransactionId());
/*     */     } else {
/*     */       
/* 589 */       if (info.getType() == 105) {
/* 590 */         this.brokerConnector.rollbackTransaction(this, info.getTransactionId());
/*     */       }
/* 592 */       else if (info.getType() == 103) {
/* 593 */         this.brokerConnector.commitTransaction(this, info.getTransactionId());
/*     */       } 
/* 595 */       this.transactions.remove(info.getTransactionId());
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
/*     */   private void consumeXATransactionInfo(XATransactionInfo info) throws JMSException, XAException {
/* 607 */     if (info.getType() == 101) {
/* 608 */       this.transactions.add(info.getXid());
/* 609 */       this.brokerConnector.startTransaction(this, info.getXid());
/*     */     }
/* 611 */     else if (info.getType() == 110) {
/* 612 */       ActiveMQXid[] rc = this.brokerConnector.getPreparedTransactions(this);
/*     */       
/* 614 */       info.setReceiptRequired(false);
/*     */       
/* 616 */       ResponseReceipt receipt = new ResponseReceipt();
/* 617 */       receipt.setId(this.packetIdGenerator.generateId());
/* 618 */       receipt.setCorrelationId(info.getId());
/* 619 */       receipt.setResult((Serializable)rc);
/* 620 */       send((Packet)receipt);
/*     */     }
/* 622 */     else if (info.getType() == 113) {
/* 623 */       String rc = this.brokerConnector.getResourceManagerId(this);
/*     */       
/* 625 */       info.setReceiptRequired(false);
/*     */       
/* 627 */       ResponseReceipt receipt = new ResponseReceipt();
/* 628 */       receipt.setId(this.packetIdGenerator.generateId());
/* 629 */       receipt.setCorrelationId(info.getId());
/* 630 */       receipt.setResult(rc);
/* 631 */       send((Packet)receipt);
/*     */     }
/* 633 */     else if (info.getType() != 106) {
/*     */ 
/*     */ 
/*     */       
/* 637 */       if (info.getType() == 102) {
/* 638 */         int rc = this.brokerConnector.prepareTransaction(this, info.getXid());
/*     */         
/* 640 */         info.setReceiptRequired(false);
/*     */         
/* 642 */         IntResponseReceipt receipt = new IntResponseReceipt();
/* 643 */         receipt.setId(this.packetIdGenerator.generateId());
/* 644 */         receipt.setCorrelationId(info.getId());
/* 645 */         receipt.setResult(rc);
/* 646 */         send((Packet)receipt);
/*     */       }
/* 648 */       else if (info.getType() == 105) {
/* 649 */         this.brokerConnector.rollbackTransaction(this, info.getXid());
/*     */       }
/* 651 */       else if (info.getType() == 109) {
/* 652 */         this.brokerConnector.commitTransaction(this, info.getXid(), true);
/*     */       }
/* 654 */       else if (info.getType() == 103) {
/* 655 */         this.brokerConnector.commitTransaction(this, info.getXid(), false);
/*     */       } else {
/*     */         
/* 658 */         throw new JMSException("Packet type: " + info.getType() + " not recognized.");
/*     */       } 
/* 660 */       this.transactions.remove(info.getXid());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void consumeProducerInfo(ProducerInfo info) throws JMSException {
/* 671 */     if (info.isStarted()) {
/* 672 */       this.producers.add(info);
/* 673 */       this.brokerConnector.registerMessageProducer(this, info);
/*     */     } else {
/*     */       
/* 676 */       this.producers.remove(info);
/* 677 */       this.brokerConnector.deregisterMessageProducer(this, info);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void consumeSessionInfo(SessionInfo info) throws JMSException {
/* 688 */     if (info.isStarted()) {
/* 689 */       this.sessions.add(info);
/* 690 */       this.brokerConnector.registerSession(this, info);
/*     */     } else {
/*     */       
/* 693 */       this.sessions.remove(info);
/* 694 */       this.brokerConnector.deregisterSession(this, info);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void consumeCapacityInfo(CapacityInfo info) {
/* 704 */     this.capacity = info.getCapacity();
/*     */   }
/*     */   
/*     */   private void updateCapacityInfo(String correlationId) {
/* 708 */     CapacityInfo info = new CapacityInfo();
/* 709 */     info.setResourceName(this.brokerConnector.getBrokerInfo().getBrokerName());
/* 710 */     info.setCorrelationId(correlationId);
/* 711 */     info.setCapacity(this.brokerConnector.getBrokerCapacity());
/* 712 */     info.setFlowControlTimeout(getFlowControlTimeout(info.getCapacity()));
/* 713 */     send((Packet)info);
/*     */   }
/*     */   
/*     */   private long getFlowControlTimeout(int capacity) {
/* 717 */     long result = -1L;
/* 718 */     if (capacity <= 0) {
/* 719 */       result = 10000L;
/*     */     }
/* 721 */     else if (capacity <= 10) {
/* 722 */       result = 1000L;
/*     */     }
/* 724 */     else if (capacity <= 20) {
/* 725 */       result = 10L;
/*     */     } 
/* 727 */     return result;
/*     */   }
/*     */   
/*     */   private void consumeBrokerInfo(BrokerInfo info) {
/* 731 */     this.brokerConnection = true;
/* 732 */     this.remoteBrokerName = info.getBrokerName();
/* 733 */     String clusterName = getBrokerConnector().getBrokerContainer().getBroker().getBrokerClusterName();
/* 734 */     if (clusterName.equals(info.getClusterName())) {
/* 735 */       this.clusteredConnection = true;
/*     */     }
/*     */   }
/*     */   
/*     */   private void sendReceipt(Packet packet) {
/* 740 */     sendReceipt(packet, null, false);
/*     */   }
/*     */   
/*     */   private void sendReceipt(Packet packet, Throwable requestEx, boolean failed) {
/* 744 */     if (packet != null && packet.isReceiptRequired()) {
/* 745 */       Receipt receipt = new Receipt();
/* 746 */       receipt.setId(this.packetIdGenerator.generateId());
/* 747 */       receipt.setCorrelationId(packet.getId());
/* 748 */       receipt.setBrokerName(this.brokerConnector.getBrokerInfo().getBrokerName());
/* 749 */       receipt.setClusterName(this.brokerConnector.getBrokerInfo().getClusterName());
/* 750 */       receipt.setException(requestEx);
/* 751 */       receipt.setFailed(failed);
/* 752 */       send((Packet)receipt);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\broker\impl\BrokerClientImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */