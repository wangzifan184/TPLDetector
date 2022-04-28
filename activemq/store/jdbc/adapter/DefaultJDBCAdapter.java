/*     */ package org.codehaus.activemq.store.jdbc.adapter;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.List;
/*     */ import javax.jms.JMSException;
/*     */ import javax.transaction.xa.XAException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.ActiveMQXid;
/*     */ import org.codehaus.activemq.service.SubscriberEntry;
/*     */ import org.codehaus.activemq.service.Transaction;
/*     */ import org.codehaus.activemq.service.TransactionManager;
/*     */ import org.codehaus.activemq.service.impl.XATransactionCommand;
/*     */ import org.codehaus.activemq.store.jdbc.JDBCAdapter;
/*     */ import org.codehaus.activemq.store.jdbc.StatementProvider;
/*     */ import org.codehaus.activemq.util.LongSequenceGenerator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultJDBCAdapter
/*     */   implements JDBCAdapter
/*     */ {
/*  60 */   private static final Log log = LogFactory.getLog(DefaultJDBCAdapter.class);
/*     */   
/*     */   protected final CachingStatementProvider statementProvider;
/*  63 */   protected LongSequenceGenerator sequenceGenerator = new LongSequenceGenerator();
/*     */   
/*     */   protected void setBinaryData(PreparedStatement s, int index, byte[] data) throws SQLException {
/*  66 */     s.setBytes(index, data);
/*     */   }
/*     */   
/*     */   protected byte[] getBinaryData(ResultSet rs, int index) throws SQLException {
/*  70 */     return rs.getBytes(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultJDBCAdapter(StatementProvider provider) {
/*  77 */     this.statementProvider = new CachingStatementProvider(provider);
/*     */   }
/*     */   
/*     */   public DefaultJDBCAdapter() {
/*  81 */     this(new DefaultStatementProvider());
/*     */   }
/*     */   
/*     */   public LongSequenceGenerator getSequenceGenerator() {
/*  85 */     return this.sequenceGenerator;
/*     */   }
/*     */   
/*     */   public void doCreateTables(Connection c) throws SQLException {
/*  89 */     Statement s = null;
/*     */     try {
/*  91 */       s = c.createStatement();
/*  92 */       String[] createStatments = this.statementProvider.getCreateSchemaStatments();
/*  93 */       for (int i = 0; i < createStatments.length; i++) {
/*     */ 
/*     */         
/*     */         try {
/*  97 */           boolean rc = s.execute(createStatments[i]);
/*     */         }
/*  99 */         catch (SQLException e) {
/* 100 */           log.debug("Statment failed: " + createStatments[i], e);
/*     */         } 
/*     */       } 
/* 103 */       c.commit();
/*     */     } finally {
/*     */       
/*     */       try {
/* 107 */         s.close();
/*     */       }
/* 109 */       catch (Throwable e) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void initSequenceGenerator(Connection c) {
/* 115 */     PreparedStatement s = null;
/* 116 */     ResultSet rs = null;
/*     */     try {
/* 118 */       s = c.prepareStatement(this.statementProvider.getFindLastSequenceId());
/* 119 */       rs = s.executeQuery();
/* 120 */       if (rs.next()) {
/* 121 */         this.sequenceGenerator.setLastSequenceId(rs.getLong(1));
/*     */       }
/*     */     }
/* 124 */     catch (SQLException e) {
/* 125 */       log.warn("Failed to find last sequence number: " + e, e);
/*     */     } finally {
/*     */       
/*     */       try {
/* 129 */         rs.close();
/*     */       }
/* 131 */       catch (Throwable e) {}
/*     */       
/*     */       try {
/* 134 */         s.close();
/*     */       }
/* 136 */       catch (Throwable e) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void doAddMessage(Connection c, long seq, String messageID, String destinationName, byte[] data) throws SQLException, JMSException {
/* 142 */     PreparedStatement s = null;
/*     */     try {
/* 144 */       s = c.prepareStatement(this.statementProvider.getAddMessageStatment());
/* 145 */       s.setLong(1, seq);
/* 146 */       s.setString(2, destinationName);
/* 147 */       s.setString(3, messageID);
/* 148 */       setBinaryData(s, 4, data);
/* 149 */       if (s.executeUpdate() != 1) {
/* 150 */         throw new JMSException("Failed to broker message: " + messageID + " in container.  ");
/*     */       }
/*     */     } finally {
/*     */       
/*     */       try {
/* 155 */         s.close();
/*     */       }
/* 157 */       catch (Throwable e) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Long getMessageSequenceId(Connection c, String messageID) throws SQLException, JMSException {
/* 163 */     PreparedStatement s = null;
/* 164 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 167 */       s = c.prepareStatement(this.statementProvider.getFindMessageSequenceIdStatment());
/* 168 */       s.setString(1, messageID);
/* 169 */       rs = s.executeQuery();
/*     */       
/* 171 */       if (!rs.next()) {
/* 172 */         return null;
/*     */       }
/* 174 */       return new Long(rs.getLong(1));
/*     */     } finally {
/*     */ 
/*     */       
/*     */       try {
/* 179 */         rs.close();
/*     */       }
/* 181 */       catch (Throwable e) {}
/*     */       
/*     */       try {
/* 184 */         s.close();
/*     */       }
/* 186 */       catch (Throwable e) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] doGetMessage(Connection c, long seq) throws SQLException {
/* 192 */     PreparedStatement s = null;
/* 193 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 196 */       s = c.prepareStatement(this.statementProvider.getFindMessageStatment());
/* 197 */       s.setLong(1, seq);
/* 198 */       rs = s.executeQuery();
/*     */       
/* 200 */       if (!rs.next()) {
/* 201 */         return null;
/*     */       }
/* 203 */       return getBinaryData(rs, 1);
/*     */     } finally {
/*     */ 
/*     */       
/*     */       try {
/* 208 */         rs.close();
/*     */       }
/* 210 */       catch (Throwable e) {}
/*     */       
/*     */       try {
/* 213 */         s.close();
/*     */       }
/* 215 */       catch (Throwable e) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void doRemoveMessage(Connection c, long seq) throws SQLException {
/* 221 */     PreparedStatement s = null;
/*     */     try {
/* 223 */       s = c.prepareStatement(this.statementProvider.getRemoveMessageStatment());
/* 224 */       s.setLong(1, seq);
/* 225 */       if (s.executeUpdate() != 1) {
/* 226 */         log.error("Could not delete sequenece number for: " + seq);
/*     */       }
/*     */     } finally {
/*     */       
/*     */       try {
/* 231 */         s.close();
/*     */       }
/* 233 */       catch (Throwable e) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void doRecover(Connection c, String destinationName, JDBCAdapter.MessageListResultHandler listener) throws SQLException, JMSException {
/* 239 */     PreparedStatement s = null;
/* 240 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 243 */       s = c.prepareStatement(this.statementProvider.getFindAllMessagesStatment());
/* 244 */       s.setString(1, destinationName);
/* 245 */       rs = s.executeQuery();
/*     */       
/* 247 */       while (rs.next()) {
/* 248 */         long seq = rs.getLong(1);
/* 249 */         String msgid = rs.getString(2);
/* 250 */         listener.onMessage(seq, msgid);
/*     */       } 
/*     */     } finally {
/*     */ 
/*     */       
/*     */       try {
/* 256 */         rs.close();
/*     */       }
/* 258 */       catch (Throwable e) {}
/*     */       
/*     */       try {
/* 261 */         s.close();
/*     */       }
/* 263 */       catch (Throwable e) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void doGetXids(Connection c, List list) throws SQLException {
/* 269 */     PreparedStatement s = null;
/* 270 */     ResultSet rs = null;
/*     */     try {
/* 272 */       s = c.prepareStatement(this.statementProvider.getFindAllXidStatment());
/* 273 */       rs = s.executeQuery();
/*     */       
/* 275 */       while (rs.next()) {
/* 276 */         String xid = rs.getString(1);
/*     */         try {
/* 278 */           list.add(new ActiveMQXid(xid));
/*     */         }
/* 280 */         catch (JMSException e) {
/* 281 */           log.error("Failed to recover prepared transaction due to invalid xid: " + xid, (Throwable)e);
/*     */         } 
/*     */       } 
/*     */     } finally {
/*     */ 
/*     */       
/*     */       try {
/* 288 */         rs.close();
/*     */       }
/* 290 */       catch (Throwable e) {}
/*     */       
/*     */       try {
/* 293 */         s.close();
/*     */       }
/* 295 */       catch (Throwable e) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void doRemoveXid(Connection c, ActiveMQXid xid) throws SQLException, XAException {
/* 301 */     PreparedStatement s = null;
/*     */     try {
/* 303 */       s = c.prepareStatement(this.statementProvider.getRemoveMessageStatment());
/* 304 */       s.setString(1, xid.toLocalTransactionId());
/* 305 */       if (s.executeUpdate() != 1) {
/* 306 */         throw new XAException("Failed to remove prepared transaction: " + xid + ".");
/*     */       }
/*     */     } finally {
/*     */       
/*     */       try {
/* 311 */         s.close();
/*     */       }
/* 313 */       catch (Throwable e) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void doAddXid(Connection c, ActiveMQXid xid, byte[] data) throws SQLException, XAException {
/* 320 */     PreparedStatement s = null;
/*     */     
/*     */     try {
/* 323 */       s = c.prepareStatement(this.statementProvider.getAddMessageStatment());
/* 324 */       s.setString(1, xid.toLocalTransactionId());
/* 325 */       setBinaryData(s, 2, data);
/* 326 */       if (s.executeUpdate() != 1) {
/* 327 */         throw new XAException("Failed to store prepared transaction: " + xid);
/*     */       }
/*     */     } finally {
/*     */ 
/*     */       
/*     */       try {
/* 333 */         s.close();
/*     */       }
/* 335 */       catch (Throwable e) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void doLoadPreparedTransactions(Connection c, TransactionManager transactionManager) throws SQLException {
/* 341 */     PreparedStatement s = null;
/* 342 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 345 */       s = c.prepareStatement(this.statementProvider.getFindAllTxStatment());
/* 346 */       rs = s.executeQuery();
/*     */       
/* 348 */       while (rs.next()) {
/* 349 */         String id = rs.getString(1);
/* 350 */         byte[] data = getBinaryData(rs, 2);
/*     */         try {
/* 352 */           ActiveMQXid xid = new ActiveMQXid(id);
/* 353 */           Transaction transaction = XATransactionCommand.fromBytes(data);
/* 354 */           transactionManager.loadTransaction(xid, transaction);
/*     */         }
/* 356 */         catch (Exception e) {
/* 357 */           log.error("Failed to recover prepared transaction due to invalid xid: " + id, e);
/*     */         } 
/*     */       } 
/*     */     } finally {
/*     */       
/*     */       try {
/* 363 */         rs.close();
/*     */       }
/* 365 */       catch (Throwable e) {}
/*     */       
/*     */       try {
/* 368 */         s.close();
/*     */       }
/* 370 */       catch (Throwable e) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doSetLastAck(Connection c, String destinationName, String subscriptionID, long seq) throws SQLException, JMSException {
/* 380 */     PreparedStatement s = null;
/*     */     try {
/* 382 */       s = c.prepareStatement(this.statementProvider.getUpdateLastAckOfDurableSub());
/* 383 */       s.setLong(1, seq);
/* 384 */       s.setString(2, subscriptionID);
/* 385 */       s.setString(3, destinationName);
/*     */       
/* 387 */       if (s.executeUpdate() != 1) {
/* 388 */         throw new JMSException("Failed to acknowlege message with sequence id: " + seq + " for client: " + subscriptionID);
/*     */       }
/*     */     } finally {
/*     */       
/*     */       try {
/* 393 */         s.close();
/*     */       }
/* 395 */       catch (Throwable e) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doRecoverSubscription(Connection c, String destinationName, String subscriptionID, JDBCAdapter.MessageListResultHandler listener) throws SQLException, JMSException {
/* 405 */     PreparedStatement s = null;
/* 406 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 409 */       s = c.prepareStatement(this.statementProvider.getFindAllDurableSubMessagesStatment());
/* 410 */       s.setString(1, destinationName);
/* 411 */       s.setString(2, subscriptionID);
/* 412 */       rs = s.executeQuery();
/*     */       
/* 414 */       while (rs.next()) {
/* 415 */         long seq = rs.getLong(1);
/* 416 */         String msgid = rs.getString(2);
/* 417 */         listener.onMessage(seq, msgid);
/*     */       } 
/*     */     } finally {
/*     */ 
/*     */       
/*     */       try {
/* 423 */         rs.close();
/*     */       }
/* 425 */       catch (Throwable e) {}
/*     */       
/*     */       try {
/* 428 */         s.close();
/*     */       }
/* 430 */       catch (Throwable e) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doSetSubscriberEntry(Connection c, String destinationName, String sub, SubscriberEntry subscriberEntry) throws SQLException {
/* 439 */     PreparedStatement s = null;
/*     */     try {
/* 441 */       s = c.prepareStatement(this.statementProvider.getUpdateDurableSubStatment());
/* 442 */       s.setInt(1, subscriberEntry.getSubscriberID());
/* 443 */       s.setString(2, subscriberEntry.getClientID());
/* 444 */       s.setString(3, subscriberEntry.getConsumerName());
/* 445 */       s.setString(4, subscriberEntry.getSelector());
/* 446 */       s.setString(5, sub);
/* 447 */       s.setString(6, destinationName);
/*     */ 
/*     */       
/* 450 */       if (s.executeUpdate() != 1) {
/* 451 */         s.close();
/* 452 */         s = c.prepareStatement(this.statementProvider.getCreateDurableSubStatment());
/* 453 */         s.setInt(1, subscriberEntry.getSubscriberID());
/* 454 */         s.setString(2, subscriberEntry.getClientID());
/* 455 */         s.setString(3, subscriberEntry.getConsumerName());
/* 456 */         s.setString(4, subscriberEntry.getSelector());
/* 457 */         s.setString(5, sub);
/* 458 */         s.setString(6, destinationName);
/* 459 */         s.setLong(7, -1L);
/*     */         
/* 461 */         if (s.executeUpdate() != 1) {
/* 462 */           log.error("Failed to store durable subscription for: " + sub);
/*     */         }
/*     */       } 
/*     */     } finally {
/*     */       
/*     */       try {
/* 468 */         s.close();
/*     */       }
/* 470 */       catch (Throwable e) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SubscriberEntry doGetSubscriberEntry(Connection c, String destinationName, String sub) throws SQLException {
/* 479 */     PreparedStatement s = null;
/* 480 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 483 */       s = c.prepareStatement(this.statementProvider.getFindDurableSubStatment());
/* 484 */       s.setString(1, sub);
/* 485 */       s.setString(2, destinationName);
/* 486 */       rs = s.executeQuery();
/*     */       
/* 488 */       if (!rs.next()) {
/* 489 */         return null;
/*     */       }
/*     */       
/* 492 */       SubscriberEntry answer = new SubscriberEntry();
/* 493 */       answer.setSubscriberID(rs.getInt(1));
/* 494 */       answer.setClientID(rs.getString(2));
/* 495 */       answer.setConsumerName(rs.getString(3));
/* 496 */       answer.setDestination(rs.getString(4));
/*     */       
/* 498 */       return answer;
/*     */     } finally {
/*     */ 
/*     */       
/*     */       try {
/* 503 */         rs.close();
/*     */       }
/* 505 */       catch (Throwable e) {}
/*     */       
/*     */       try {
/* 508 */         s.close();
/*     */       }
/* 510 */       catch (Throwable e) {}
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\jdbc\adapter\DefaultJDBCAdapter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */