package org.codehaus.activemq.service;

import java.util.Map;
import javax.jms.JMSException;
import org.codehaus.activemq.broker.BrokerClient;
import org.codehaus.activemq.message.ActiveMQMessage;
import org.codehaus.activemq.message.ConsumerInfo;
import org.codehaus.activemq.message.MessageAck;

public interface MessageContainerManager extends Service {
  Map getDestinations();
  
  void addMessageConsumer(BrokerClient paramBrokerClient, ConsumerInfo paramConsumerInfo) throws JMSException;
  
  void removeMessageConsumer(BrokerClient paramBrokerClient, ConsumerInfo paramConsumerInfo) throws JMSException;
  
  void deleteSubscription(String paramString1, String paramString2) throws JMSException;
  
  void sendMessage(BrokerClient paramBrokerClient, ActiveMQMessage paramActiveMQMessage) throws JMSException;
  
  void acknowledgeMessage(BrokerClient paramBrokerClient, MessageAck paramMessageAck) throws JMSException;
  
  void redeliverMessage(BrokerClient paramBrokerClient, MessageAck paramMessageAck) throws JMSException;
  
  void poll() throws JMSException;
  
  void commitTransaction(BrokerClient paramBrokerClient, String paramString) throws JMSException;
  
  void rollbackTransaction(BrokerClient paramBrokerClient, String paramString);
  
  MessageContainer getContainer(String paramString) throws JMSException;
  
  void acknowledgeTransactedMessage(BrokerClient paramBrokerClient, String paramString, MessageAck paramMessageAck) throws JMSException;
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\MessageContainerManager.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */