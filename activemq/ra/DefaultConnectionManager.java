/*     */ package org.codehaus.activemq.ra;
/*     */ 
/*     */ import javax.resource.ResourceException;
/*     */ import javax.resource.spi.ConnectionEvent;
/*     */ import javax.resource.spi.ConnectionEventListener;
/*     */ import javax.resource.spi.ConnectionManager;
/*     */ import javax.resource.spi.ConnectionRequestInfo;
/*     */ import javax.resource.spi.ManagedConnection;
/*     */ import javax.resource.spi.ManagedConnectionFactory;
/*     */ import javax.security.auth.Subject;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
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
/*     */ public class DefaultConnectionManager
/*     */   implements ConnectionManager, ConnectionEventListener
/*     */ {
/*  41 */   private static final Log log = LogFactory.getLog(DefaultConnectionManager.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object allocateConnection(ManagedConnectionFactory connectionFactory, ConnectionRequestInfo info) throws ResourceException {
/*  47 */     Subject subject = null;
/*  48 */     ManagedConnection connection = connectionFactory.createManagedConnection(subject, info);
/*  49 */     connection.addConnectionEventListener(this);
/*  50 */     return connection.getConnection(subject, info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void connectionClosed(ConnectionEvent event) {
/*     */     try {
/*  58 */       ((ManagedConnection)event.getSource()).cleanup();
/*     */     }
/*  60 */     catch (ResourceException e) {
/*  61 */       log.warn("Error occured during the cleanup of a managed connection: ", (Throwable)e);
/*     */     } 
/*     */     try {
/*  64 */       ((ManagedConnection)event.getSource()).destroy();
/*     */     }
/*  66 */     catch (ResourceException e) {
/*  67 */       log.warn("Error occured during the destruction of a managed connection: ", (Throwable)e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void localTransactionStarted(ConnectionEvent event) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void localTransactionCommitted(ConnectionEvent event) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void localTransactionRolledback(ConnectionEvent event) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void connectionErrorOccurred(ConnectionEvent event) {
/*  93 */     log.warn("Managed connection experiened an error: ", event.getException());
/*     */     try {
/*  95 */       ((ManagedConnection)event.getSource()).cleanup();
/*     */     }
/*  97 */     catch (ResourceException e) {
/*  98 */       log.warn("Error occured during the cleanup of a managed connection: ", (Throwable)e);
/*     */     } 
/*     */     try {
/* 101 */       ((ManagedConnection)event.getSource()).destroy();
/*     */     }
/* 103 */     catch (ResourceException e) {
/* 104 */       log.warn("Error occured during the destruction of a managed connection: ", (Throwable)e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ra\DefaultConnectionManager.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */