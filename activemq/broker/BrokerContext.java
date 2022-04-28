/*    */ package org.codehaus.activemq.broker;
/*    */ 
/*    */ import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.Map;
/*    */ import javax.jms.JMSException;
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
/*    */ public class BrokerContext
/*    */ {
/* 33 */   private static final BrokerContext singleton = new BrokerContext();
/*    */   
/* 35 */   private Map brokersByName = (Map)new ConcurrentHashMap();
/* 36 */   private Map connectorsByURL = (Map)new ConcurrentHashMap();
/*    */   
/*    */   public static BrokerContext getInstance() {
/* 39 */     return singleton;
/*    */   }
/*    */   static final boolean $assertionsDisabled;
/*    */   
/*    */   public synchronized BrokerContainer getBrokerContainerByName(String name, BrokerContainerFactory factory) throws JMSException {
/* 44 */     BrokerContainer container = (BrokerContainer)this.brokersByName.get(name);
/* 45 */     if (container == null) {
/*    */       
/* 47 */       container = factory.createBrokerContainer(name, this);
/*    */       
/* 49 */       assert this.brokersByName.get(name) == container : "Should have registered the container by now";
/*    */       
/* 51 */       container.start();
/*    */     } 
/* 53 */     return container;
/*    */   }
/*    */   
/*    */   public void registerContainer(String name, BrokerContainer container) {
/* 57 */     if (name == null) {
/* 58 */       throw new IllegalArgumentException("Name must not be null");
/*    */     }
/* 60 */     this.brokersByName.put(name, container);
/*    */   }
/*    */   
/*    */   public void deregisterContainer(String name, BrokerContainer container) {
/* 64 */     this.brokersByName.remove(name);
/*    */   }
/*    */   
/*    */   public void registerConnector(String url, BrokerConnector connector) {
/* 68 */     this.connectorsByURL.put(url, connector);
/*    */   }
/*    */   
/*    */   public void deregisterConnector(String urlString) {
/* 72 */     this.connectorsByURL.remove(urlString);
/*    */   }
/*    */   
/*    */   public BrokerConnector getConnectorByURL(String url) {
/* 76 */     BrokerConnector brokerConnector = (BrokerConnector)this.connectorsByURL.get(url);
/* 77 */     if (brokerConnector == null) {
/* 78 */       if (url.startsWith("reliable:")) {
/* 79 */         return getConnectorByURL(url.substring("reliable:".length()));
/*    */       }
/* 81 */       if (url.startsWith("list:")) {
/* 82 */         return getConnectorByURL(url.substring("list:".length()));
/*    */       }
/*    */     } 
/* 85 */     return brokerConnector;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\broker\BrokerContext.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */