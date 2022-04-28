package org.codehaus.activemq.service;

import java.io.Serializable;
import org.codehaus.activemq.broker.Broker;

public interface TransactionTask extends Serializable {
  void execute(Broker paramBroker) throws Throwable;
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\TransactionTask.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */