package org.codehaus.activemq.broker;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.transaction.xa.XAException;
import org.codehaus.activemq.capacity.CapacityMonitor;
import org.codehaus.activemq.message.ActiveMQMessage;
import org.codehaus.activemq.message.ActiveMQXid;
import org.codehaus.activemq.message.ConnectionInfo;
import org.codehaus.activemq.message.ConsumerInfo;
import org.codehaus.activemq.message.MessageAck;
import org.codehaus.activemq.message.ProducerInfo;
import org.codehaus.activemq.security.SecurityAdapter;
import org.codehaus.activemq.service.MessageContainerManager;
import org.codehaus.activemq.service.RedeliveryPolicy;
import org.codehaus.activemq.service.Service;
import org.codehaus.activemq.store.PersistenceAdapter;

public interface Broker extends Service, CapacityMonitor {
  void addClient(BrokerClient paramBrokerClient, ConnectionInfo paramConnectionInfo) throws JMSException;
  
  void removeClient(BrokerClient paramBrokerClient, ConnectionInfo paramConnectionInfo) throws JMSException;
  
  void addMessageProducer(BrokerClient paramBrokerClient, ProducerInfo paramProducerInfo) throws JMSException;
  
  void removeMessageProducer(BrokerClient paramBrokerClient, ProducerInfo paramProducerInfo) throws JMSException;
  
  void addMessageConsumer(BrokerClient paramBrokerClient, ConsumerInfo paramConsumerInfo) throws JMSException;
  
  void removeMessageConsumer(BrokerClient paramBrokerClient, ConsumerInfo paramConsumerInfo) throws JMSException;
  
  void sendMessage(BrokerClient paramBrokerClient, ActiveMQMessage paramActiveMQMessage) throws JMSException;
  
  void sendTransactedMessage(BrokerClient paramBrokerClient, String paramString, ActiveMQMessage paramActiveMQMessage) throws JMSException;
  
  void acknowledgeMessage(BrokerClient paramBrokerClient, MessageAck paramMessageAck) throws JMSException;
  
  void acknowledgeTransactedMessage(BrokerClient paramBrokerClient, String paramString, MessageAck paramMessageAck) throws JMSException;
  
  ActiveMQXid[] getPreparedTransactions(BrokerClient paramBrokerClient) throws XAException;
  
  void redeliverMessage(BrokerClient paramBrokerClient, MessageAck paramMessageAck) throws JMSException;
  
  void deleteSubscription(String paramString1, String paramString2) throws JMSException;
  
  void startTransaction(BrokerClient paramBrokerClient, String paramString) throws JMSException;
  
  void commitTransaction(BrokerClient paramBrokerClient, String paramString) throws JMSException;
  
  void rollbackTransaction(BrokerClient paramBrokerClient, String paramString) throws JMSException;
  
  void startTransaction(BrokerClient paramBrokerClient, ActiveMQXid paramActiveMQXid) throws XAException;
  
  int prepareTransaction(BrokerClient paramBrokerClient, ActiveMQXid paramActiveMQXid) throws XAException;
  
  void rollbackTransaction(BrokerClient paramBrokerClient, ActiveMQXid paramActiveMQXid) throws XAException;
  
  void commitTransaction(BrokerClient paramBrokerClient, ActiveMQXid paramActiveMQXid, boolean paramBoolean) throws XAException;
  
  File getTempDir();
  
  String getBrokerName();
  
  String getBrokerClusterName();
  
  PersistenceAdapter getPersistenceAdapter();
  
  void setPersistenceAdapter(PersistenceAdapter paramPersistenceAdapter);
  
  Map getContainerManagerMap();
  
  Context getDestinationContext(Hashtable paramHashtable);
  
  void addConsumerInfoListener(ConsumerInfoListener paramConsumerInfoListener);
  
  void removeConsumerInfoListener(ConsumerInfoListener paramConsumerInfoListener);
  
  MessageContainerManager getPersistentTopicContainerManager();
  
  MessageContainerManager getTransientTopicContainerManager();
  
  MessageContainerManager getPersistentQueueContainerManager();
  
  MessageContainerManager getTransientQueueContainerManager();
  
  SecurityAdapter getSecurityAdapter();
  
  void setSecurityAdapter(SecurityAdapter paramSecurityAdapter);
  
  RedeliveryPolicy getRedeliveryPolicy();
  
  void setRedeliveryPolicy(RedeliveryPolicy paramRedeliveryPolicy);
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\broker\Broker.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */