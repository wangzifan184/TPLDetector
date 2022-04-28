package org.codehaus.activemq.store;

import javax.transaction.xa.XAException;
import org.codehaus.activemq.message.ActiveMQXid;
import org.codehaus.activemq.service.Service;
import org.codehaus.activemq.service.Transaction;
import org.codehaus.activemq.service.TransactionManager;

public interface PreparedTransactionStore extends Service {
  ActiveMQXid[] getXids() throws XAException;
  
  void remove(ActiveMQXid paramActiveMQXid) throws XAException;
  
  void put(ActiveMQXid paramActiveMQXid, Transaction paramTransaction) throws XAException;
  
  void loadPreparedTransactions(TransactionManager paramTransactionManager) throws XAException;
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\PreparedTransactionStore.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */