/*    */ package org.codehaus.activemq.web;
/*    */ 
/*    */ import javax.servlet.http.HttpSessionEvent;
/*    */ import javax.servlet.http.HttpSessionListener;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
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
/*    */ public class ConnectionManager
/*    */   implements HttpSessionListener
/*    */ {
/* 34 */   private static final Log log = LogFactory.getLog(ConnectionManager.class);
/*    */   
/*    */   public void sessionCreated(HttpSessionEvent event) {}
/*    */   
/*    */   public void sessionDestroyed(HttpSessionEvent event) {}
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\web\ConnectionManager.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */