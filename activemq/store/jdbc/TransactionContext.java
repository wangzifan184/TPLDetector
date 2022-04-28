/*    */ package org.codehaus.activemq.store.jdbc;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import javax.sql.DataSource;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TransactionContext
/*    */ {
/* 34 */   private static final Log log = LogFactory.getLog(TransactionContext.class);
/* 35 */   private static ThreadLocal threadLocalTxn = new ThreadLocal();
/*    */   private final DataSource dataSource;
/*    */   
/*    */   TransactionContext(DataSource dataSource) {
/* 39 */     this.dataSource = dataSource;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Connection popConnection() {
/* 46 */     Connection[] tx = threadLocalTxn.get();
/* 47 */     if (tx == null || tx[0] == null) {
/* 48 */       log.warn("Attempt to pop connection when no transaction in progress");
/* 49 */       return null;
/*    */     } 
/*    */     
/* 52 */     Connection answer = tx[0];
/* 53 */     tx[0] = null;
/* 54 */     return answer;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void pushConnection(Connection connection) {
/* 62 */     Connection[] tx = threadLocalTxn.get();
/* 63 */     if (tx == null) {
/* 64 */       tx = new Connection[] { null };
/* 65 */       threadLocalTxn.set(tx);
/*    */     } 
/* 67 */     if (tx[0] != null) {
/* 68 */       throw new IllegalStateException("A transaction is allready in progress");
/*    */     }
/* 70 */     tx[0] = connection;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Connection peekConnection() {
/* 79 */     Connection[] tx = threadLocalTxn.get();
/* 80 */     if (tx != null && tx[0] != null) {
/* 81 */       return tx[0];
/*    */     }
/* 83 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\jdbc\TransactionContext.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */