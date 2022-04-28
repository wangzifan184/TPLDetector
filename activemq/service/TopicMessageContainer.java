package org.codehaus.activemq.service;

import javax.jms.JMSException;
import org.codehaus.activemq.message.ConsumerInfo;

public interface TopicMessageContainer extends MessageContainer {
  void setLastAcknowledgedMessageID(Subscription paramSubscription, MessageIdentity paramMessageIdentity) throws JMSException;
  
  void recoverSubscription(Subscription paramSubscription) throws JMSException;
  
  void storeSubscription(ConsumerInfo paramConsumerInfo, Subscription paramSubscription) throws JMSException;
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\TopicMessageContainer.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */