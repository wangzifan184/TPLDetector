package org.codehaus.activemq.service;

import java.io.Serializable;
import javax.transaction.xa.XAException;
import org.codehaus.activemq.broker.Broker;

public interface Transaction extends Serializable {
  void addPrePrepareTask(TransactionTask paramTransactionTask);
  
  void addPostCommitTask(TransactionTask paramTransactionTask);
  
  void addPostRollbackTask(TransactionTask paramTransactionTask);
  
  void commit(boolean paramBoolean) throws XAException;
  
  void rollback() throws XAException;
  
  int prepare() throws XAException;
  
  void setBroker(Broker paramBroker);
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\Transaction.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */