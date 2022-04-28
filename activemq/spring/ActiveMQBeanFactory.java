/*     */ package org.codehaus.activemq.spring;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
/*     */ import org.springframework.core.io.InputStreamResource;
/*     */ import org.springframework.core.io.Resource;
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
/*     */ public class ActiveMQBeanFactory
/*     */   extends DefaultListableBeanFactory
/*     */ {
/*     */   private XmlBeanDefinitionReader reader;
/*     */   
/*     */   public static ActiveMQBeanFactory newInstance(String brokerName, Resource resource) {
/*  44 */     return new ActiveMQBeanFactory(brokerName, resource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQBeanFactory(String brokerName, Resource resource) throws BeansException {
/*  56 */     this(brokerName, resource, null);
/*     */   }
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
/*     */   public ActiveMQBeanFactory(String brokerName, InputStream is) throws BeansException {
/*  71 */     this(brokerName, (Resource)new InputStreamResource(is, "(no description)"), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQBeanFactory(String brokerName, Resource resource, BeanFactory parentBeanFactory) throws BeansException {
/*  83 */     super(parentBeanFactory);
/*  84 */     this.reader = createReader(brokerName);
/*  85 */     this.reader.loadBeanDefinitions(resource);
/*     */   }
/*     */   
/*     */   protected XmlBeanDefinitionReader getReader() {
/*  89 */     return this.reader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected XmlBeanDefinitionReader createReader(String brokerName) {
/* 100 */     return new ActiveMQBeanDefinitionReader((BeanDefinitionRegistry)this, brokerName);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\spring\ActiveMQBeanFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */