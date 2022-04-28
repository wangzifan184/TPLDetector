package org.codehaus.activemq.store;

import javax.jms.JMSException;
import org.codehaus.activemq.message.ActiveMQMessage;
import org.codehaus.activemq.message.MessageAck;
import org.codehaus.activemq.service.MessageIdentity;
import org.codehaus.activemq.service.QueueMessageContainer;
import org.codehaus.activemq.service.Service;

public interface MessageStore extends Service {
  MessageIdentity addMessage(ActiveMQMessage paramActiveMQMessage) throws JMSException;
  
  ActiveMQMessage getMessage(MessageIdentity paramMessageIdentity) throws JMSException;
  
  void removeMessage(MessageIdentity paramMessageIdentity, MessageAck paramMessageAck) throws JMSException;
  
  void recover(QueueMessageContainer paramQueueMessageContainer) throws JMSException;
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\MessageStore.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */