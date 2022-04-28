package org.codehaus.activemq.service;

import org.codehaus.activemq.broker.BrokerClient;

public interface Dispatcher extends Service {
  void register(MessageContainerManager paramMessageContainerManager);
  
  void wakeup(Subscription paramSubscription);
  
  void wakeup();
  
  void addActiveSubscription(BrokerClient paramBrokerClient, Subscription paramSubscription);
  
  void removeActiveSubscription(BrokerClient paramBrokerClient, Subscription paramSubscription);
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\Dispatcher.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */