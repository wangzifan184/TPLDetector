/*    */ package org.codehaus.activemq;
/*    */ 
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
/*    */ public class ConfigurationException
/*    */   extends JMSException
/*    */ {
/*    */   public ConfigurationException(String description) {
/* 29 */     super(description, "AMQ-1002");
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ConfigurationException.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */