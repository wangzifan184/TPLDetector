/*    */ package org.codehaus.activemq.store.vm;
/*    */ 
/*    */ import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Map;
/*    */ import javax.jms.JMSException;
/*    */ import org.codehaus.activemq.message.ActiveMQXid;
/*    */ import org.codehaus.activemq.service.Transaction;
/*    */ import org.codehaus.activemq.service.TransactionManager;
/*    */ import org.codehaus.activemq.store.PreparedTransactionStore;
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
/*    */ public class VMPreparedTransactionStoreImpl
/*    */   implements PreparedTransactionStore
/*    */ {
/* 34 */   private Map prepared = (Map)new ConcurrentHashMap();
/*    */   
/*    */   public ActiveMQXid[] getXids() {
/* 37 */     ArrayList list = new ArrayList(this.prepared.keySet());
/* 38 */     ActiveMQXid[] answer = new ActiveMQXid[list.size()];
/* 39 */     list.toArray((Object[])answer);
/* 40 */     return answer;
/*    */   }
/*    */   
/*    */   public void remove(ActiveMQXid xid) {
/* 44 */     this.prepared.remove(xid);
/*    */   }
/*    */   
/*    */   public void put(ActiveMQXid xid, Transaction transaction) {
/* 48 */     this.prepared.put(xid, transaction);
/*    */   }
/*    */   
/*    */   public void loadPreparedTransactions(TransactionManager transactionManager) {}
/*    */   
/*    */   public void start() throws JMSException {}
/*    */   
/*    */   public void stop() throws JMSException {}
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\vm\VMPreparedTransactionStoreImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */