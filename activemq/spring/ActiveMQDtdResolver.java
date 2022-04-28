/*    */ package org.codehaus.activemq.spring;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.core.io.ClassPathResource;
/*    */ import org.xml.sax.EntityResolver;
/*    */ import org.xml.sax.InputSource;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ActiveMQDtdResolver
/*    */   implements EntityResolver
/*    */ {
/*    */   private static final String DTD_NAME = "activemq.dtd";
/*    */   private static final String SEARCH_PACKAGE = "/org/codehaus/activemq/";
/* 46 */   protected final Log logger = LogFactory.getLog(getClass());
/*    */   
/*    */   public InputSource resolveEntity(String publicId, String systemId) throws IOException {
/* 49 */     this.logger.debug("Trying to resolve XML entity with public ID [" + publicId + "] and system ID [" + systemId + "]");
/*    */     
/* 51 */     if (systemId != null && systemId.indexOf("activemq.dtd") > systemId.lastIndexOf("/")) {
/*    */       
/* 53 */       String dtdFile = systemId.substring(systemId.indexOf("activemq.dtd"));
/* 54 */       this.logger.debug("Trying to locate [" + dtdFile + "] under [" + "/org/codehaus/activemq/" + "]");
/*    */       try {
/* 56 */         String name = "/org/codehaus/activemq/" + dtdFile;
/* 57 */         ClassPathResource classPathResource = new ClassPathResource(name, getClass());
/* 58 */         InputSource source = new InputSource(classPathResource.getInputStream());
/* 59 */         source.setPublicId(publicId);
/* 60 */         source.setSystemId(systemId);
/* 61 */         this.logger.debug("Found beans DTD [" + systemId + "] in classpath");
/* 62 */         return source;
/*    */       }
/* 64 */       catch (IOException ex) {
/* 65 */         this.logger.debug("Could not resolve beans DTD [" + systemId + "]: not found in classpath", ex);
/*    */       } 
/*    */     } 
/*    */     
/* 69 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\spring\ActiveMQDtdResolver.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */