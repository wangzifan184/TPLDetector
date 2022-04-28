/*    */ package org.codehaus.activemq.jndi;
/*    */ 
/*    */ import java.util.Properties;
/*    */ import javax.naming.NamingException;
/*    */ import javax.naming.Reference;
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
/*    */ public abstract class JNDIBaseStorable
/*    */   implements JNDIStorableInterface
/*    */ {
/* 30 */   private Properties properties = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract void buildFromProperties(Properties paramProperties);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract void populateProperties(Properties paramProperties);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void setProperties(Properties props) {
/* 55 */     this.properties = props;
/* 56 */     buildFromProperties(props);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized Properties getProperties() {
/* 66 */     if (this.properties == null) {
/* 67 */       this.properties = new Properties();
/*    */     }
/* 69 */     populateProperties(this.properties);
/* 70 */     return this.properties;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Reference getReference() throws NamingException {
/* 81 */     return JNDIReferenceFactory.createReference(getClass().getName(), this);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\jndi\JNDIBaseStorable.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */