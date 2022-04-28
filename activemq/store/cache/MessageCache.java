package org.codehaus.activemq.store.cache;

import org.codehaus.activemq.message.ActiveMQMessage;

public interface MessageCache {
  ActiveMQMessage get(String paramString);
  
  void put(String paramString, ActiveMQMessage paramActiveMQMessage);
  
  void remove(String paramString);
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\cache\MessageCache.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */