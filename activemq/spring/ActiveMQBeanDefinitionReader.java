/*     */ package org.codehaus.activemq.spring;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.URIResolver;
/*     */ import javax.xml.transform.dom.DOMResult;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
/*     */ import org.springframework.core.io.ClassPathResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.w3c.dom.Document;
/*     */ import org.xml.sax.EntityResolver;
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
/*     */ public class ActiveMQBeanDefinitionReader
/*     */   extends XmlBeanDefinitionReader
/*     */ {
/*     */   private String brokerName;
/*     */   
/*     */   public ActiveMQBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry, String brokerName) {
/*  46 */     super(beanDefinitionRegistry);
/*  47 */     this.brokerName = brokerName;
/*  48 */     setEntityResolver(createEntityResolver());
/*     */   }
/*     */   
/*     */   public int registerBeanDefinitions(Document document, Resource resource) throws BeansException {
/*     */     try {
/*  53 */       Document newDocument = transformDocument(document);
/*  54 */       return super.registerBeanDefinitions(newDocument, resource);
/*     */     }
/*  56 */     catch (Exception e) {
/*  57 */       throw new ConfigurationParseException(resource, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Transformer createTransformer(Source source) throws TransformerConfigurationException {
/*  62 */     TransformerFactory factory = TransformerFactory.newInstance();
/*  63 */     Transformer transformer = factory.newTransformer(source);
/*  64 */     transformer.setURIResolver(new URIResolver() {
/*     */           public Source resolve(String href, String base) {
/*  66 */             System.out.println("Called with href:  " + href + " base: " + base);
/*  67 */             return null;
/*     */           }
/*     */         });
/*  70 */     return transformer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBrokerName() {
/*  77 */     return this.brokerName;
/*     */   }
/*     */   
/*     */   public void setBrokerName(String brokerName) {
/*  81 */     this.brokerName = brokerName;
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
/*     */   protected Document transformDocument(Document document) throws IOException, TransformerException {
/*  94 */     Transformer transformer = createTransformer(createXslSource());
/*  95 */     transformer.setParameter("brokerName", getBrokerName());
/*  96 */     DOMResult result = new DOMResult();
/*  97 */     transformer.transform(new DOMSource(document), result);
/*  98 */     return (Document)result.getNode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Source createXslSource() throws IOException {
/* 107 */     return new StreamSource(getXslResource().getInputStream(), getXslResource().getURL().toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassPathResource getXslResource() {
/* 114 */     return new ClassPathResource("org/codehaus/activemq/activemq-to-spring.xsl");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected EntityResolver createEntityResolver() {
/* 121 */     return new ActiveMQDtdResolver();
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\spring\ActiveMQBeanDefinitionReader.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */