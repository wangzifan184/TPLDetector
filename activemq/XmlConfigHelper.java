/*    */ package org.codehaus.activemq;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import javax.jms.JMSException;
/*    */ import org.codehaus.activemq.broker.BrokerContainerFactory;
/*    */ import org.codehaus.activemq.util.JMSExceptionHelper;
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
/*    */ public class XmlConfigHelper
/*    */ {
/* 33 */   private static final Class[] ARGUMENT_TYPES = new Class[] { String.class };
/*    */ 
/*    */   
/*    */   public static final String SPRING_CLASS_NAME = "org.codehaus.activemq.spring.SpringBrokerContainerFactory";
/*    */ 
/*    */ 
/*    */   
/*    */   public static BrokerContainerFactory createBrokerContainerFactory(String xmlConfig) throws JMSException {
/*    */     try {
/* 42 */       Class factoryClass = getSpringFactoryClass();
/* 43 */       Method method = factoryClass.getMethod("newFactory", ARGUMENT_TYPES);
/* 44 */       if (method == null) {
/* 45 */         throw new JMSException("Could not find newFactory() method - classpath strangeness occurred");
/*    */       }
/* 47 */       return (BrokerContainerFactory)method.invoke(null, new Object[] { xmlConfig });
/*    */     }
/* 49 */     catch (JMSException e) {
/* 50 */       throw e;
/*    */     }
/* 52 */     catch (ClassNotFoundException e) {
/* 53 */       throw JMSExceptionHelper.newJMSException("Could not configure broker using XML configuration file as Spring factory class could not be loaded. Maybe you need the Spring.jar on your classpath? Reason: " + e, e);
/*    */     }
/* 55 */     catch (Exception e) {
/* 56 */       throw JMSExceptionHelper.newJMSException("Could not configure broker using XML configuration file as attempt to use Spring factory failed. Reason: " + e, e);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static Class getSpringFactoryClass() throws ClassNotFoundException {
/* 61 */     Class answer = null;
/*    */     try {
/* 63 */       answer = Thread.currentThread().getContextClassLoader().loadClass("org.codehaus.activemq.spring.SpringBrokerContainerFactory");
/*    */     }
/* 65 */     catch (ClassNotFoundException e) {
/* 66 */       answer = XmlConfigHelper.class.getClassLoader().loadClass("org.codehaus.activemq.spring.SpringBrokerContainerFactory");
/*    */     } 
/* 68 */     return answer;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\XmlConfigHelper.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */