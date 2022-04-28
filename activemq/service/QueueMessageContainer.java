package org.codehaus.activemq.service;

import javax.jms.JMSException;
import org.codehaus.activemq.message.ActiveMQMessage;

public interface QueueMessageContainer extends MessageContainer {
  ActiveMQMessage poll() throws JMSException;
  
  ActiveMQMessage peekNext(MessageIdentity paramMessageIdentity) throws JMSException;
  
  void returnMessage(MessageIdentity paramMessageIdentity) throws JMSException;
  
  void reset() throws JMSException;
  
  void start() throws JMSException;
  
  void recoverMessageToBeDelivered(MessageIdentity paramMessageIdentity) throws JMSException;
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\QueueMessageContainer.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */