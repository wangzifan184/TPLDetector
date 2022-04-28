/*     */ package org.codehaus.activemq.ra;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import javax.resource.ResourceException;
/*     */ import javax.resource.spi.ConnectionManager;
/*     */ import javax.resource.spi.ConnectionRequestInfo;
/*     */ import javax.resource.spi.ManagedConnection;
/*     */ import javax.resource.spi.ManagedConnectionFactory;
/*     */ import javax.resource.spi.ResourceAdapter;
/*     */ import javax.resource.spi.ResourceAdapterAssociation;
/*     */ import javax.security.auth.Subject;
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
/*     */ public class ActiveMQManagedConnectionFactory
/*     */   implements ManagedConnectionFactory, ResourceAdapterAssociation
/*     */ {
/*     */   private ActiveMQResourceAdapter adapter;
/*     */   private PrintWriter logWriter;
/*     */   
/*     */   public void setResourceAdapter(ResourceAdapter adapter) throws ResourceException {
/*  48 */     this.adapter = (ActiveMQResourceAdapter)adapter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceAdapter getResourceAdapter() {
/*  55 */     return this.adapter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object createConnectionFactory(ConnectionManager manager) throws ResourceException {
/*  62 */     return new ActiveMQConnectionFactory(this, manager, this.adapter.getInfo());
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
/*     */   public Object createConnectionFactory() throws ResourceException {
/*  74 */     return new ActiveMQConnectionFactory(this, new SimpleConnectionManager(), this.adapter.getInfo());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo info) throws ResourceException {
/*  82 */     return new ActiveMQManagedConnection(subject, this.adapter, (ActiveMQConnectionRequestInfo)info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ManagedConnection matchManagedConnections(Set connections, Subject subject, ConnectionRequestInfo info) throws ResourceException {
/*  91 */     Iterator iterator = connections.iterator();
/*  92 */     while (iterator.hasNext()) {
/*  93 */       ActiveMQManagedConnection c = iterator.next();
/*  94 */       if (c.matches(subject, info)) {
/*  95 */         return c;
/*     */       }
/*     */     } 
/*  98 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLogWriter(PrintWriter logWriter) throws ResourceException {
/* 105 */     this.logWriter = logWriter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrintWriter getLogWriter() throws ResourceException {
/* 112 */     return this.logWriter;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ra\ActiveMQManagedConnectionFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */