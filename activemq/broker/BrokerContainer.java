package org.codehaus.activemq.broker;

import java.util.List;
import javax.jms.JMSException;
import javax.transaction.xa.XAException;
import org.codehaus.activemq.message.ActiveMQMessage;
import org.codehaus.activemq.message.ActiveMQXid;
import org.codehaus.activemq.message.ConnectionInfo;
import org.codehaus.activemq.message.ConsumerInfo;
import org.codehaus.activemq.message.DurableUnsubscribe;
import org.codehaus.activemq.message.MessageAck;
import org.codehaus.activemq.message.ProducerInfo;
import org.codehaus.activemq.message.SessionInfo;
import org.codehaus.activemq.message.WireFormat;
import org.codehaus.activemq.security.SecurityAdapter;
import org.codehaus.activemq.service.Service;
import org.codehaus.activemq.store.PersistenceAdapter;
import org.codehaus.activemq.transport.DiscoveryAgent;
import org.codehaus.activemq.transport.NetworkConnector;
import org.codehaus.activemq.transport.TransportServerChannel;

public interface BrokerContainer extends Service {
  void registerConnection(BrokerClient paramBrokerClient, ConnectionInfo paramConnectionInfo) throws JMSException;
  
  void deregisterConnection(BrokerClient paramBrokerClient, ConnectionInfo paramConnectionInfo) throws JMSException;
  
  void registerMessageConsumer(BrokerClient paramBrokerClient, ConsumerInfo paramConsumerInfo) throws JMSException;
  
  void deregisterMessageConsumer(BrokerClient paramBrokerClient, ConsumerInfo paramConsumerInfo) throws JMSException;
  
  void registerMessageProducer(BrokerClient paramBrokerClient, ProducerInfo paramProducerInfo) throws JMSException;
  
  void deregisterMessageProducer(BrokerClient paramBrokerClient, ProducerInfo paramProducerInfo) throws JMSException;
  
  void registerSession(BrokerClient paramBrokerClient, SessionInfo paramSessionInfo) throws JMSException;
  
  void deregisterSession(BrokerClient paramBrokerClient, SessionInfo paramSessionInfo) throws JMSException;
  
  void startTransaction(BrokerClient paramBrokerClient, String paramString) throws JMSException;
  
  void rollbackTransaction(BrokerClient paramBrokerClient, String paramString) throws JMSException;
  
  void commitTransaction(BrokerClient paramBrokerClient, String paramString) throws JMSException;
  
  void sendTransactedMessage(BrokerClient paramBrokerClient, String paramString, ActiveMQMessage paramActiveMQMessage) throws JMSException;
  
  void acknowledgeTransactedMessage(BrokerClient paramBrokerClient, String paramString, MessageAck paramMessageAck) throws JMSException;
  
  void sendMessage(BrokerClient paramBrokerClient, ActiveMQMessage paramActiveMQMessage) throws JMSException;
  
  void acknowledgeMessage(BrokerClient paramBrokerClient, MessageAck paramMessageAck) throws JMSException;
  
  void durableUnsubscribe(BrokerClient paramBrokerClient, DurableUnsubscribe paramDurableUnsubscribe) throws JMSException;
  
  void startTransaction(BrokerClient paramBrokerClient, ActiveMQXid paramActiveMQXid) throws XAException;
  
  ActiveMQXid[] getPreparedTransactions(BrokerClient paramBrokerClient) throws XAException;
  
  int prepareTransaction(BrokerClient paramBrokerClient, ActiveMQXid paramActiveMQXid) throws XAException;
  
  void rollbackTransaction(BrokerClient paramBrokerClient, ActiveMQXid paramActiveMQXid) throws XAException;
  
  void commitTransaction(BrokerClient paramBrokerClient, ActiveMQXid paramActiveMQXid, boolean paramBoolean) throws XAException;
  
  void addConnector(BrokerConnector paramBrokerConnector);
  
  void removeConnector(BrokerConnector paramBrokerConnector);
  
  Broker getBroker();
  
  List getTransportConnectors();
  
  void setTransportConnectors(List paramList);
  
  List getNetworkConnectors();
  
  void setNetworkConnectors(List paramList);
  
  PersistenceAdapter getPersistenceAdapter();
  
  void setPersistenceAdapter(PersistenceAdapter paramPersistenceAdapter);
  
  DiscoveryAgent getDiscoveryAgent();
  
  void setDiscoveryAgent(DiscoveryAgent paramDiscoveryAgent);
  
  SecurityAdapter getSecurityAdapter();
  
  void setSecurityAdapter(SecurityAdapter paramSecurityAdapter);
  
  NetworkConnector addNetworkConnector(String paramString);
  
  NetworkConnector addNetworkConnector();
  
  void addNetworkConnector(NetworkConnector paramNetworkConnector);
  
  void removeNetworkConnector(NetworkConnector paramNetworkConnector);
  
  void addConnector(String paramString) throws JMSException;
  
  void addConnector(String paramString, WireFormat paramWireFormat) throws JMSException;
  
  void addConnector(TransportServerChannel paramTransportServerChannel);
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\broker\BrokerContainer.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */