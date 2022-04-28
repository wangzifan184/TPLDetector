package org.codehaus.activemq;

import org.codehaus.activemq.message.ActiveMQMessage;

public interface ActiveMQMessageDispatcher {
  boolean isTarget(ActiveMQMessage paramActiveMQMessage);
  
  void dispatch(ActiveMQMessage paramActiveMQMessage);
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ActiveMQMessageDispatcher.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */