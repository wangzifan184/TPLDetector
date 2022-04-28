/*    */ package org.codehaus.activemq.spring;
/*    */ 
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.codehaus.activemq.broker.BrokerContainer;
/*    */ import org.codehaus.activemq.broker.BrokerContainerFactory;
/*    */ import org.codehaus.activemq.broker.BrokerContext;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.core.io.ResourceEditor;
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
/*    */ public class SpringBrokerContainerFactory
/*    */   implements BrokerContainerFactory
/*    */ {
/* 35 */   private static final Log log = LogFactory.getLog(SpringBrokerContainerFactory.class);
/*    */ 
/*    */ 
/*    */   
/*    */   private Resource resource;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static BrokerContainer newInstance(Resource resource, String brokerName) {
/* 45 */     SpringBrokerContainerFactory factory = new SpringBrokerContainerFactory(resource);
/* 46 */     return factory.createBrokerContainer(brokerName, BrokerContext.getInstance());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static SpringBrokerContainerFactory newFactory(String resourceName) {
/* 57 */     ResourceEditor editor = new ResourceEditor();
/* 58 */     editor.setAsText(resourceName);
/* 59 */     Resource resource = (Resource)editor.getValue();
/* 60 */     if (resource == null) {
/* 61 */       throw new IllegalArgumentException("Could not convert '" + resourceName + "' into a Spring Resource");
/*    */     }
/* 63 */     return new SpringBrokerContainerFactory(resource);
/*    */   }
/*    */ 
/*    */   
/*    */   public SpringBrokerContainerFactory() {}
/*    */   
/*    */   public SpringBrokerContainerFactory(Resource resource) {
/* 70 */     this.resource = resource;
/*    */   }
/*    */   
/*    */   public BrokerContainer createBrokerContainer(String brokerName, BrokerContext context) {
/* 74 */     log.info("Loading ActiveMQ broker from configuration: " + this.resource);
/*    */     
/* 76 */     ActiveMQBeanFactory beanFactory = new ActiveMQBeanFactory(brokerName, this.resource);
/* 77 */     return (BrokerContainer)beanFactory.getBean("broker");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Resource getResource() {
/* 83 */     return this.resource;
/*    */   }
/*    */   
/*    */   public void setResource(Resource resource) {
/* 87 */     this.resource = resource;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\spring\SpringBrokerContainerFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */