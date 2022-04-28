/*     */ package org.codehaus.activemq.service.boundedvm;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Message;
/*     */ import org.codehaus.activemq.broker.BrokerClient;
/*     */ import org.codehaus.activemq.broker.BrokerConnector;
/*     */ import org.codehaus.activemq.filter.Filter;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.BrokerInfo;
/*     */ import org.codehaus.activemq.message.ConsumerInfo;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.util.MemoryBoundedQueue;
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
/*     */ public class TransientQueueSubscription
/*     */   extends TransientSubscription
/*     */ {
/*     */   private BrokerClient client;
/*     */   private String brokerName;
/*     */   private String clusterName;
/*     */   private MemoryBoundedQueue dispatchedQueue;
/*     */   
/*     */   public TransientQueueSubscription(BrokerClient client, MemoryBoundedQueue dispatchedQueue, Filter filter, ConsumerInfo info) {
/*  53 */     super(filter, info);
/*  54 */     this.client = client;
/*  55 */     this.dispatchedQueue = dispatchedQueue;
/*  56 */     if (client != null) {
/*  57 */       BrokerConnector connector = client.getBrokerConnector();
/*  58 */       if (connector != null) {
/*  59 */         BrokerInfo bi = connector.getBrokerInfo();
/*  60 */         if (bi != null) {
/*  61 */           this.brokerName = bi.getBrokerName();
/*  62 */           this.clusterName = bi.getClusterName();
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
/*     */ 
/*     */   
/*     */   public boolean isTarget(ActiveMQMessage message) throws JMSException {
/*  76 */     boolean result = false;
/*  77 */     if (message != null)
/*     */     {
/*  79 */       if (!this.client.isClusteredConnection() || !message.isEntryCluster(this.clusterName) || message.isEntryBroker(this.brokerName))
/*     */       {
/*  81 */         result = (this.filter.matches((Message)message) && (message.getJMSDeliveryMode() == 1 || this.consumerInfo.getDestination().isTemporary()));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*  86 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canAcceptMessages() {
/*  93 */     return (this.dispatchedQueue.size() <= this.consumerInfo.getPrefetchNumber());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doDispatch(ActiveMQMessage message) throws JMSException {
/* 103 */     addDispatchedMessage(message);
/* 104 */     message = message.shallowCopy();
/* 105 */     message.setConsumerNos(new int[] { this.consumerInfo.getConsumerNo() });
/* 106 */     this.client.dispatch(message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addDispatchedMessage(ActiveMQMessage message) {
/* 115 */     this.dispatchedQueue.enqueue((Packet)message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQMessage acknowledgeMessage(String id) {
/* 125 */     ActiveMQMessage msg = (ActiveMQMessage)this.dispatchedQueue.remove(id);
/* 126 */     return msg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getUndeliveredMessages() {
/* 133 */     return this.dispatchedQueue.getContents();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 140 */     super.close();
/* 141 */     this.dispatchedQueue.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\boundedvm\TransientQueueSubscription.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */