package org.codehaus.activemq.store.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.jms.JMSException;
import javax.transaction.xa.XAException;
import org.codehaus.activemq.message.ActiveMQXid;
import org.codehaus.activemq.service.SubscriberEntry;
import org.codehaus.activemq.service.TransactionManager;
import org.codehaus.activemq.util.LongSequenceGenerator;

public interface JDBCAdapter {
  LongSequenceGenerator getSequenceGenerator();
  
  void doCreateTables(Connection paramConnection) throws SQLException;
  
  void initSequenceGenerator(Connection paramConnection);
  
  void doAddMessage(Connection paramConnection, long paramLong, String paramString1, String paramString2, byte[] paramArrayOfbyte) throws SQLException, JMSException;
  
  byte[] doGetMessage(Connection paramConnection, long paramLong) throws SQLException;
  
  void doRemoveMessage(Connection paramConnection, long paramLong) throws SQLException;
  
  void doRecover(Connection paramConnection, String paramString, MessageListResultHandler paramMessageListResultHandler) throws SQLException, JMSException;
  
  void doGetXids(Connection paramConnection, List paramList) throws SQLException;
  
  void doRemoveXid(Connection paramConnection, ActiveMQXid paramActiveMQXid) throws SQLException, XAException;
  
  void doAddXid(Connection paramConnection, ActiveMQXid paramActiveMQXid, byte[] paramArrayOfbyte) throws SQLException, XAException;
  
  void doLoadPreparedTransactions(Connection paramConnection, TransactionManager paramTransactionManager) throws SQLException;
  
  void doSetLastAck(Connection paramConnection, String paramString1, String paramString2, long paramLong) throws SQLException, JMSException;
  
  void doRecoverSubscription(Connection paramConnection, String paramString1, String paramString2, MessageListResultHandler paramMessageListResultHandler) throws SQLException, JMSException;
  
  void doSetSubscriberEntry(Connection paramConnection, String paramString1, String paramString2, SubscriberEntry paramSubscriberEntry) throws SQLException, JMSException;
  
  SubscriberEntry doGetSubscriberEntry(Connection paramConnection, String paramString1, String paramString2) throws SQLException, JMSException;
  
  Long getMessageSequenceId(Connection paramConnection, String paramString) throws SQLException, JMSException;
  
  public static interface MessageListResultHandler {
    void onMessage(long param1Long, String param1String) throws JMSException;
  }
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\jdbc\JDBCAdapter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */