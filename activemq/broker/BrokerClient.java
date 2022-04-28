package org.codehaus.activemq.broker;

import org.codehaus.activemq.message.ActiveMQMessage;
import org.codehaus.activemq.service.Service;
import org.codehaus.activemq.transport.TransportChannel;

public interface BrokerClient extends Service {
  void initialize(BrokerConnector paramBrokerConnector, TransportChannel paramTransportChannel);
  
  void dispatch(ActiveMQMessage paramActiveMQMessage);
  
  boolean isBrokerConnection();
  
  boolean isClusteredConnection();
  
  int getCapacity();
  
  boolean isSlowConsumer();
  
  void updateBrokerCapacity(int paramInt);
  
  String getClientID();
  
  void cleanUp();
  
  TransportChannel getChannel();
  
  BrokerConnector getBrokerConnector();
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\broker\BrokerClient.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */