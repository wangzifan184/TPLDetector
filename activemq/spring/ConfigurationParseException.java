/*    */ package org.codehaus.activemq.spring;
/*    */ 
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.core.io.Resource;
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
/*    */ public class ConfigurationParseException
/*    */   extends BeansException
/*    */ {
/*    */   private Resource resource;
/*    */   
/*    */   public ConfigurationParseException(Resource resource, Throwable e) {
/* 33 */     super("Could not parse resource: " + resource + ". Reason: " + e, e);
/* 34 */     this.resource = resource;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Resource getResource() {
/* 41 */     return this.resource;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\spring\ConfigurationParseException.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */