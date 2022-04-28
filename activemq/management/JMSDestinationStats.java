package org.codehaus.activemq.management;

import javax.jms.Message;

public interface JMSDestinationStats {
  void setPendingMessageCountOnStartup(long paramLong);
  
  void onMessageSend(Message paramMessage);
  
  void onMessageAck();
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\management\JMSDestinationStats.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */