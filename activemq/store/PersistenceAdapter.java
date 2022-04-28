package org.codehaus.activemq.store;

import java.util.Map;
import javax.jms.JMSException;
import org.codehaus.activemq.service.QueueMessageContainer;
import org.codehaus.activemq.service.Service;
import org.codehaus.activemq.service.TopicMessageContainer;

public interface PersistenceAdapter extends Service {
  Map getInitialDestinations();
  
  MessageStore createQueueMessageStore(String paramString) throws JMSException;
  
  TopicMessageStore createTopicMessageStore(String paramString) throws JMSException;
  
  PreparedTransactionStore createPreparedTransactionStore() throws JMSException;
  
  QueueMessageContainer createQueueMessageContainer(String paramString) throws JMSException;
  
  TopicMessageContainer createTopicMessageContainer(String paramString) throws JMSException;
  
  void beginTransaction() throws JMSException;
  
  void commitTransaction() throws JMSException;
  
  void rollbackTransaction();
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\PersistenceAdapter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */