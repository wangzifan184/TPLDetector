package org.codehaus.activemq.store.jdbc;

public interface StatementProvider {
  String[] getCreateSchemaStatments();
  
  String[] getDropSchemaStatments();
  
  String getAddMessageStatment();
  
  String getUpdateMessageStatment();
  
  String getRemoveMessageStatment();
  
  String getFindMessageSequenceIdStatment();
  
  String getFindMessageStatment();
  
  String getFindAllMessagesStatment();
  
  String getFindLastSequenceId();
  
  String getAddXidStatment();
  
  String getUpdateXidStatment();
  
  String getRemoveXidStatment();
  
  String getFindXidStatment();
  
  String getFindAllXidStatment();
  
  String getFindAllTxStatment();
  
  String getCreateDurableSubStatment();
  
  String getUpdateDurableSubStatment();
  
  String getFindDurableSubStatment();
  
  String getUpdateLastAckOfDurableSub();
  
  String getFindAllDurableSubMessagesStatment();
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\jdbc\StatementProvider.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */