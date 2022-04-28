package org.codehaus.activemq.transport;

import javax.jms.JMSException;
import org.codehaus.activemq.service.Service;

public interface TransportServerChannel extends Service {
  void stop() throws JMSException;
  
  void start() throws JMSException;
  
  void setTransportChannelListener(TransportChannelListener paramTransportChannelListener);
  
  String getUrl();
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\TransportServerChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */