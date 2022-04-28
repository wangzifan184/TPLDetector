/*    */ package org.codehaus.activemq.util;
/*    */ 
/*    */ import javax.jms.Connection;
/*    */ import javax.jms.JMSException;
/*    */ import org.codehaus.activemq.ActiveMQConnection;
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
/*    */ public class JmsLogAppender
/*    */   extends JmsLogAppenderSupport
/*    */ {
/* 32 */   private String uri = "tcp://localhost:61616";
/*    */   
/*    */   private String userName;
/*    */   
/*    */   private String password;
/*    */ 
/*    */   
/*    */   public String getUri() {
/* 40 */     return this.uri;
/*    */   }
/*    */   
/*    */   public void setUri(String uri) {
/* 44 */     this.uri = uri;
/*    */   }
/*    */   
/*    */   public String getUserName() {
/* 48 */     return this.userName;
/*    */   }
/*    */   
/*    */   public void setUserName(String userName) {
/* 52 */     this.userName = userName;
/*    */   }
/*    */   
/*    */   public String getPassword() {
/* 56 */     return this.password;
/*    */   }
/*    */   
/*    */   public void setPassword(String password) {
/* 60 */     this.password = password;
/*    */   }
/*    */   
/*    */   protected Connection createConnection() throws JMSException {
/* 64 */     if (this.userName != null) {
/* 65 */       return (Connection)ActiveMQConnection.makeConnection(this.userName, this.password, this.uri);
/*    */     }
/*    */     
/* 68 */     return (Connection)ActiveMQConnection.makeConnection(this.uri);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activem\\util\JmsLogAppender.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */