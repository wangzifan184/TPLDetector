/*    */ package org.codehaus.activemq.broker.impl;
/*    */ 
/*    */ import org.codehaus.activemq.broker.BrokerContainer;
/*    */ import org.codehaus.activemq.broker.BrokerContainerFactory;
/*    */ import org.codehaus.activemq.broker.BrokerContext;
/*    */ import org.codehaus.activemq.store.PersistenceAdapter;
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
/*    */ public class BrokerContainerFactoryImpl
/*    */   implements BrokerContainerFactory
/*    */ {
/*    */   private PersistenceAdapter persistenceAdapter;
/*    */   
/*    */   public BrokerContainerFactoryImpl() {}
/*    */   
/*    */   public BrokerContainerFactoryImpl(PersistenceAdapter persistenceAdapter) {
/* 35 */     this.persistenceAdapter = persistenceAdapter;
/*    */   }
/*    */   
/*    */   public BrokerContainer createBrokerContainer(String brokerName, BrokerContext context) {
/* 39 */     if (this.persistenceAdapter != null) {
/* 40 */       return new BrokerContainerImpl(brokerName, this.persistenceAdapter, context);
/*    */     }
/*    */     
/* 43 */     return new BrokerContainerImpl(brokerName, context);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PersistenceAdapter getPersistenceAdapter() {
/* 50 */     return this.persistenceAdapter;
/*    */   }
/*    */   
/*    */   public void setPersistenceAdapter(PersistenceAdapter persistenceAdapter) {
/* 54 */     this.persistenceAdapter = persistenceAdapter;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\broker\impl\BrokerContainerFactoryImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */