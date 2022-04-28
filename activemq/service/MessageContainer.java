package org.codehaus.activemq.service;

import javax.jms.JMSException;
import org.codehaus.activemq.message.ActiveMQMessage;
import org.codehaus.activemq.message.MessageAck;

public interface MessageContainer extends Service {
  String getDestinationName();
  
  MessageIdentity addMessage(ActiveMQMessage paramActiveMQMessage) throws JMSException;
  
  void delete(MessageIdentity paramMessageIdentity, MessageAck paramMessageAck) throws JMSException;
  
  ActiveMQMessage getMessage(MessageIdentity paramMessageIdentity) throws JMSException;
  
  void registerMessageInterest(MessageIdentity paramMessageIdentity) throws JMSException;
  
  void unregisterMessageInterest(MessageIdentity paramMessageIdentity, MessageAck paramMessageAck) throws JMSException;
  
  boolean containsMessage(MessageIdentity paramMessageIdentity) throws JMSException;
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\MessageContainer.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */