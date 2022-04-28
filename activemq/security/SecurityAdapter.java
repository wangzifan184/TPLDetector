package org.codehaus.activemq.security;

import javax.jms.JMSException;
import org.codehaus.activemq.broker.BrokerClient;
import org.codehaus.activemq.message.ActiveMQMessage;
import org.codehaus.activemq.message.ConnectionInfo;
import org.codehaus.activemq.message.ConsumerInfo;
import org.codehaus.activemq.message.ProducerInfo;

public interface SecurityAdapter {
  void authorizeConnection(BrokerClient paramBrokerClient, ConnectionInfo paramConnectionInfo) throws JMSException;
  
  void authorizeConsumer(BrokerClient paramBrokerClient, ConsumerInfo paramConsumerInfo) throws JMSException;
  
  void authorizeProducer(BrokerClient paramBrokerClient, ProducerInfo paramProducerInfo) throws JMSException;
  
  void authorizeSendMessage(BrokerClient paramBrokerClient, ActiveMQMessage paramActiveMQMessage) throws JMSException;
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\security\SecurityAdapter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */