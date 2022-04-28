package org.codehaus.activemq.service;

import javax.jms.JMSException;
import javax.transaction.xa.XAException;
import org.codehaus.activemq.broker.BrokerClient;
import org.codehaus.activemq.message.ActiveMQXid;

public interface TransactionManager extends Service {
  void setContexTransaction(Transaction paramTransaction);
  
  Transaction getContexTransaction();
  
  Transaction createLocalTransaction(BrokerClient paramBrokerClient, String paramString) throws JMSException;
  
  Transaction getLocalTransaction(String paramString) throws JMSException;
  
  Transaction createXATransaction(BrokerClient paramBrokerClient, ActiveMQXid paramActiveMQXid) throws XAException;
  
  Transaction getXATransaction(ActiveMQXid paramActiveMQXid) throws XAException;
  
  ActiveMQXid[] getPreparedXATransactions() throws XAException;
  
  void cleanUpClient(BrokerClient paramBrokerClient) throws JMSException;
  
  void loadTransaction(ActiveMQXid paramActiveMQXid, Transaction paramTransaction) throws XAException;
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\TransactionManager.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */