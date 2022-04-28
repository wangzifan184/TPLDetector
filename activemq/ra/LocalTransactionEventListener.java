package org.codehaus.activemq.ra;

public interface LocalTransactionEventListener {
  void beginEvent();
  
  void commitEvent();
  
  void rollbackEvent();
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ra\LocalTransactionEventListener.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */