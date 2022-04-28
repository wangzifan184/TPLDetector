package org.codehaus.activemq.store;

import javax.jms.JMSException;
import org.codehaus.activemq.message.ConsumerInfo;
import org.codehaus.activemq.message.MessageAck;
import org.codehaus.activemq.service.MessageContainer;
import org.codehaus.activemq.service.MessageIdentity;
import org.codehaus.activemq.service.SubscriberEntry;
import org.codehaus.activemq.service.Subscription;

public interface TopicMessageStore extends MessageStore {
  void setMessageContainer(MessageContainer paramMessageContainer);
  
  void incrementMessageCount(MessageIdentity paramMessageIdentity) throws JMSException;
  
  void decrementMessageCountAndMaybeDelete(MessageIdentity paramMessageIdentity, MessageAck paramMessageAck) throws JMSException;
  
  void setLastAcknowledgedMessageIdentity(Subscription paramSubscription, MessageIdentity paramMessageIdentity) throws JMSException;
  
  void recoverSubscription(Subscription paramSubscription, MessageIdentity paramMessageIdentity) throws JMSException;
  
  MessageIdentity getLastestMessageIdentity() throws JMSException;
  
  SubscriberEntry getSubscriberEntry(ConsumerInfo paramConsumerInfo) throws JMSException;
  
  void setSubscriberEntry(ConsumerInfo paramConsumerInfo, SubscriberEntry paramSubscriberEntry) throws JMSException;
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\TopicMessageStore.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */