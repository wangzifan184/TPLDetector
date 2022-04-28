package org.codehaus.activemq.service;

import javax.jms.JMSException;
import org.codehaus.activemq.broker.BrokerClient;
import org.codehaus.activemq.message.ActiveMQDestination;
import org.codehaus.activemq.message.ActiveMQMessage;
import org.codehaus.activemq.message.ConsumerInfo;
import org.codehaus.activemq.message.MessageAck;

public interface Subscription {
  void setActiveConsumer(BrokerClient paramBrokerClient, ConsumerInfo paramConsumerInfo);
  
  void clear() throws JMSException;
  
  void reset() throws JMSException;
  
  String getClientId();
  
  String getSubscriberName();
  
  ActiveMQDestination getDestination();
  
  String getSelector();
  
  boolean isActive();
  
  void setActive(boolean paramBoolean) throws JMSException;
  
  int getConsumerNumber();
  
  String getConsumerId();
  
  boolean isTarget(ActiveMQMessage paramActiveMQMessage) throws JMSException;
  
  void addMessage(MessageContainer paramMessageContainer, ActiveMQMessage paramActiveMQMessage) throws JMSException;
  
  void messageConsumed(MessageAck paramMessageAck) throws JMSException;
  
  void redeliverMessage(MessageContainer paramMessageContainer, MessageAck paramMessageAck) throws JMSException;
  
  ActiveMQMessage[] getMessagesToDispatch() throws JMSException;
  
  boolean isReadyToDispatch() throws JMSException;
  
  boolean isAtPrefetchLimit() throws JMSException;
  
  boolean isDurableTopic() throws JMSException;
  
  boolean isBrowser() throws JMSException;
  
  MessageIdentity getLastMessageIdentity() throws JMSException;
  
  void setLastMessageIdentifier(MessageIdentity paramMessageIdentity) throws JMSException;
  
  boolean isWildcard();
  
  String getPersistentKey();
  
  boolean isSameDurableSubscription(ConsumerInfo paramConsumerInfo) throws JMSException;
  
  void onAcknowledgeTransactedMessageBeforeCommit(MessageAck paramMessageAck) throws JMSException;
  
  SubscriberEntry getSubscriptionEntry();
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\Subscription.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */