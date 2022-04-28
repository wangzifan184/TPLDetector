/*    */ package org.codehaus.activemq.jndi;
/*    */ 
/*    */ import javax.naming.CompositeName;
/*    */ import javax.naming.Name;
/*    */ import javax.naming.NameParser;
/*    */ import javax.naming.NamingException;
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
/*    */ public class NameParserImpl
/*    */   implements NameParser
/*    */ {
/*    */   public Name parse(String name) throws NamingException {
/* 32 */     return new CompositeName(name);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\jndi\NameParserImpl.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */