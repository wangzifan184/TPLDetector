package org.codehaus.activemq.service;

import java.util.Iterator;
import java.util.Set;
import org.codehaus.activemq.broker.BrokerClient;
import org.codehaus.activemq.filter.Filter;
import org.codehaus.activemq.message.ActiveMQDestination;
import org.codehaus.activemq.message.ConsumerInfo;

public interface SubscriptionContainer {
  Subscription getSubscription(String paramString);
  
  Subscription removeSubscription(String paramString);
  
  Iterator subscriptionIterator();
  
  Subscription makeSubscription(Dispatcher paramDispatcher, BrokerClient paramBrokerClient, ConsumerInfo paramConsumerInfo, Filter paramFilter);
  
  Set getSubscriptions(ActiveMQDestination paramActiveMQDestination);
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\SubscriptionContainer.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */