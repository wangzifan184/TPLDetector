/*     */ package org.codehaus.activemq.store.jdbc;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Map;
/*     */ import javax.jms.JMSException;
/*     */ import javax.sql.DataSource;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.DefaultWireFormat;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.service.impl.PersistenceAdapterSupport;
/*     */ import org.codehaus.activemq.store.MessageStore;
/*     */ import org.codehaus.activemq.store.PreparedTransactionStore;
/*     */ import org.codehaus.activemq.store.TopicMessageStore;
/*     */ import org.codehaus.activemq.store.jdbc.adapter.DefaultJDBCAdapter;
/*     */ import org.codehaus.activemq.util.FactoryFinder;
/*     */ import org.codehaus.activemq.util.JMSExceptionHelper;
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
/*     */ public class JDBCPersistenceAdapter
/*     */   extends PersistenceAdapterSupport
/*     */ {
/*  47 */   private static final Log log = LogFactory.getLog(JDBCPersistenceAdapter.class);
/*  48 */   private static FactoryFinder factoryFinder = new FactoryFinder("META-INF/services/org/codehaus/activemq/store/jdbc/");
/*     */   
/*  50 */   private WireFormat wireFormat = (WireFormat)new DefaultWireFormat();
/*     */ 
/*     */   
/*     */   private DataSource dataSource;
/*     */   
/*     */   private JDBCAdapter adapter;
/*     */ 
/*     */   
/*     */   public JDBCPersistenceAdapter(DataSource ds, WireFormat wireFormat) {
/*  59 */     this.dataSource = ds;
/*  60 */     this.wireFormat = wireFormat;
/*     */   }
/*     */   
/*     */   public Map getInitialDestinations() {
/*  64 */     return null;
/*     */   }
/*     */   
/*     */   public MessageStore createQueueMessageStore(String destinationName) throws JMSException {
/*  68 */     if (this.adapter == null) {
/*  69 */       throw new IllegalStateException("Not started");
/*     */     }
/*  71 */     return new JDBCMessageStore(this, this.adapter, this.wireFormat, destinationName);
/*     */   }
/*     */   
/*     */   public TopicMessageStore createTopicMessageStore(String destinationName) throws JMSException {
/*  75 */     if (this.adapter == null) {
/*  76 */       throw new IllegalStateException("Not started");
/*     */     }
/*  78 */     return new JDBCTopicMessageStore(this, this.adapter, this.wireFormat, destinationName);
/*     */   }
/*     */   
/*     */   public PreparedTransactionStore createPreparedTransactionStore() throws JMSException {
/*  82 */     if (this.adapter == null) {
/*  83 */       throw new IllegalStateException("Not started");
/*     */     }
/*  85 */     return new JDBCPreparedTransactionStore(this, this.adapter, this.wireFormat);
/*     */   }
/*     */   
/*     */   public void beginTransaction() throws JMSException {
/*     */     try {
/*  90 */       Connection c = this.dataSource.getConnection();
/*  91 */       c.setAutoCommit(false);
/*  92 */       TransactionContext.pushConnection(c);
/*     */     }
/*  94 */     catch (SQLException e) {
/*  95 */       throw JMSExceptionHelper.newJMSException("Failed to create transaction: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void commitTransaction() throws JMSException {
/* 100 */     Connection c = TransactionContext.popConnection();
/* 101 */     if (c == null) {
/* 102 */       log.warn("Commit while no transaction in progress");
/*     */     } else {
/*     */       
/*     */       try {
/* 106 */         c.commit();
/*     */       }
/* 108 */       catch (SQLException e) {
/* 109 */         throw JMSExceptionHelper.newJMSException("Failed to commit transaction: " + c + ": " + e, e);
/*     */       } finally {
/*     */         
/*     */         try {
/* 113 */           c.close();
/*     */         }
/* 115 */         catch (Throwable e) {}
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void rollbackTransaction() {
/* 122 */     Connection c = TransactionContext.popConnection();
/*     */     try {
/* 124 */       c.rollback();
/*     */     }
/* 126 */     catch (SQLException e) {
/* 127 */       log.warn("Cannot rollback transaction due to: " + e, e);
/*     */     } finally {
/*     */       
/*     */       try {
/* 131 */         c.close();
/*     */       }
/* 133 */       catch (Throwable e) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() throws JMSException {
/* 140 */     beginTransaction();
/*     */     try {
/* 142 */       Connection c = null;
/*     */       try {
/* 144 */         c = getConnection();
/*     */ 
/*     */ 
/*     */         
/* 148 */         this.adapter = null;
/* 149 */         String database = null;
/*     */         
/* 151 */         database = c.getMetaData().getDriverName();
/* 152 */         database = database.replaceAll(" ", "_");
/*     */         
/* 154 */         log.debug("Database type: [" + database + "]");
/*     */         try {
/* 156 */           this.adapter = (JDBCAdapter)factoryFinder.newInstance(database);
/*     */         }
/* 158 */         catch (Throwable e) {
/* 159 */           log.warn("Unrecognized database type (" + database + ").  Will use default JDBC implementation.");
/* 160 */           log.debug("Reason: " + e, e);
/*     */         }
/*     */       
/* 163 */       } catch (SQLException e1) {
/* 164 */         returnConnection(c);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 169 */       if (this.adapter == null) {
/* 170 */         this.adapter = (JDBCAdapter)new DefaultJDBCAdapter();
/*     */       }
/*     */       
/*     */       try {
/* 174 */         this.adapter.doCreateTables(c);
/*     */       }
/* 176 */       catch (SQLException e) {
/* 177 */         log.warn("Cannot create tables due to: " + e, e);
/*     */       } 
/* 179 */       this.adapter.initSequenceGenerator(c);
/*     */     }
/*     */     finally {
/*     */       
/* 183 */       commitTransaction();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void stop() throws JMSException {}
/*     */ 
/*     */   
/*     */   public DataSource getDataSource() {
/* 192 */     return this.dataSource;
/*     */   }
/*     */   public void setDataSource(DataSource dataSource) {
/* 195 */     this.dataSource = dataSource;
/*     */   }
/*     */   public WireFormat getWireFormat() {
/* 198 */     return this.wireFormat;
/*     */   }
/*     */   public void setWireFormat(WireFormat wireFormat) {
/* 201 */     this.wireFormat = wireFormat;
/*     */   }
/*     */   
/*     */   public Connection getConnection() throws SQLException {
/* 205 */     Connection answer = TransactionContext.peekConnection();
/* 206 */     if (answer == null) {
/* 207 */       answer = this.dataSource.getConnection();
/* 208 */       answer.setAutoCommit(true);
/*     */     } 
/* 210 */     return answer;
/*     */   }
/*     */   
/*     */   public void returnConnection(Connection connection) {
/* 214 */     if (connection == null)
/*     */       return; 
/* 216 */     Connection peek = TransactionContext.peekConnection();
/* 217 */     if (peek != connection)
/* 218 */       try { connection.close(); } catch (SQLException e) {} 
/*     */   }
/*     */   
/*     */   public JDBCPersistenceAdapter() {}
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\jdbc\JDBCPersistenceAdapter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */