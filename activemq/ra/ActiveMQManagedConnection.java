/*     */ package org.codehaus.activemq.ra;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import javax.jms.Connection;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Session;
/*     */ import javax.jms.XASession;
/*     */ import javax.resource.ResourceException;
/*     */ import javax.resource.spi.ConnectionEvent;
/*     */ import javax.resource.spi.ConnectionEventListener;
/*     */ import javax.resource.spi.ConnectionRequestInfo;
/*     */ import javax.resource.spi.LocalTransaction;
/*     */ import javax.resource.spi.ManagedConnection;
/*     */ import javax.resource.spi.ManagedConnectionMetaData;
/*     */ import javax.security.auth.Subject;
/*     */ import javax.transaction.xa.XAException;
/*     */ import javax.transaction.xa.XAResource;
/*     */ import javax.transaction.xa.Xid;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.ActiveMQSession;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ActiveMQManagedConnection
/*     */   implements ManagedConnection
/*     */ {
/*  62 */   private static final Log log = LogFactory.getLog(ActiveMQManagedConnection.class);
/*     */   
/*     */   private PrintWriter logWriter;
/*     */   
/*     */   private Subject subject;
/*     */   private ActiveMQConnectionRequestInfo info;
/*  68 */   private ArrayList listeners = new ArrayList();
/*     */   private Connection physicalConnection;
/*     */   private Session physicalSession;
/*  71 */   private ArrayList proxyConnections = new ArrayList();
/*  72 */   private XAResource xaresource = null;
/*     */   
/*     */   public Connection getPhysicalConnection() {
/*  75 */     return this.physicalConnection;
/*     */   }
/*     */   
/*     */   public Session getPhysicalSession() {
/*  79 */     return this.physicalSession;
/*     */   }
/*     */ 
/*     */   
/*     */   public ActiveMQManagedConnection(Subject subject, ActiveMQResourceAdapter adapter, ActiveMQConnectionRequestInfo info) throws ResourceException {
/*  84 */     this.subject = subject;
/*  85 */     this.info = info;
/*  86 */     this.physicalConnection = adapter.getPhysicalConnection();
/*  87 */     createSession();
/*     */   }
/*     */   
/*     */   private void createSession() throws ResourceException {
/*     */     try {
/*  92 */       this.physicalSession = this.physicalConnection.createSession(true, 0);
/*     */       
/*  94 */       if (this.physicalSession instanceof ActiveMQSession) {
/*  95 */         ActiveMQSession session = (ActiveMQSession)this.physicalSession;
/*  96 */         LocalTransactionEventListener l = createLocalTransactionEventListener();
/*  97 */         session.setLocalTransactionEventListener(l);
/*     */       } else {
/*     */         
/* 100 */         log.trace("Cannot register LocalTransactionEventLister on non-ActiveMQ session");
/*     */       } 
/*     */       
/* 103 */       if (this.physicalSession instanceof XASession) {
/* 104 */         this.xaresource = ((XASession)this.physicalSession).getXAResource();
/*     */       } else {
/* 106 */         this.xaresource = null;
/*     */       }
/*     */     
/*     */     }
/* 110 */     catch (JMSException e) {
/* 111 */       throw new ResourceException("Could not create a new session.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private LocalTransactionEventListener createLocalTransactionEventListener() {
/* 119 */     return new LocalTransactionEventListener() { private final ActiveMQManagedConnection this$0;
/*     */         
/*     */         public void beginEvent() {
/* 122 */           ConnectionEvent event = new ConnectionEvent(ActiveMQManagedConnection.this, 2);
/* 123 */           Iterator iterator = ActiveMQManagedConnection.this.listeners.iterator();
/* 124 */           while (iterator.hasNext()) {
/* 125 */             ConnectionEventListener l = iterator.next();
/*     */             
/* 127 */             l.localTransactionStarted(event);
/*     */           } 
/*     */         }
/*     */         
/*     */         public void commitEvent() {
/* 132 */           ConnectionEvent event = new ConnectionEvent(ActiveMQManagedConnection.this, 3);
/* 133 */           Iterator iterator = ActiveMQManagedConnection.this.listeners.iterator();
/* 134 */           while (iterator.hasNext()) {
/* 135 */             ConnectionEventListener l = iterator.next();
/*     */             
/* 137 */             l.localTransactionCommitted(event);
/*     */           } 
/*     */         }
/*     */         
/*     */         public void rollbackEvent() {
/* 142 */           ConnectionEvent event = new ConnectionEvent(ActiveMQManagedConnection.this, 4);
/* 143 */           Iterator iterator = ActiveMQManagedConnection.this.listeners.iterator();
/* 144 */           while (iterator.hasNext()) {
/* 145 */             ConnectionEventListener l = iterator.next();
/*     */             
/* 147 */             l.localTransactionRolledback(event);
/*     */           } 
/*     */         } }
/*     */       ;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getConnection(Subject subject, ConnectionRequestInfo info) throws ResourceException {
/* 159 */     JMSConnectionProxy proxy = new JMSConnectionProxy(this);
/* 160 */     this.proxyConnections.add(proxy);
/* 161 */     return proxy;
/*     */   }
/*     */   
/*     */   private boolean isDestroyed() {
/* 165 */     return (this.physicalConnection == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() throws ResourceException {
/* 176 */     if (isDestroyed()) {
/*     */       return;
/*     */     }
/*     */     
/* 180 */     cleanup();
/*     */     
/*     */     try {
/* 183 */       this.physicalSession.close();
/* 184 */       this.physicalConnection = null;
/*     */     }
/* 186 */     catch (JMSException e) {
/* 187 */       log.info("Error occured during close of a JMS connection.", (Throwable)e);
/*     */     } 
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
/*     */   public void cleanup() throws ResourceException {
/* 200 */     if (isDestroyed()) {
/*     */       return;
/*     */     }
/*     */     
/* 204 */     Iterator iterator = this.proxyConnections.iterator();
/* 205 */     while (iterator.hasNext()) {
/* 206 */       JMSConnectionProxy proxy = iterator.next();
/* 207 */       proxy.cleanup();
/* 208 */       iterator.remove();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 214 */       this.physicalSession.close();
/* 215 */       this.physicalSession = null;
/* 216 */     } catch (JMSException e) {
/* 217 */       throw new ResourceException("Could close the JMS session.", e);
/*     */     } 
/*     */ 
/*     */     
/* 221 */     createSession();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void associateConnection(Object connection) throws ResourceException {
/* 228 */     throw new ResourceException("Not supported.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConnectionEventListener(ConnectionEventListener listener) {
/* 235 */     this.listeners.add(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeConnectionEventListener(ConnectionEventListener listener) {
/* 242 */     this.listeners.remove(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XAResource getXAResource() throws ResourceException {
/* 249 */     if (this.xaresource == null) {
/* 250 */       throw new ResourceException("This is not an XA connection.");
/*     */     }
/*     */ 
/*     */     
/* 254 */     return new XAResource() {
/*     */         public void commit(Xid arg0, boolean arg1) throws XAException {
/* 256 */           ActiveMQManagedConnection.this.xaresource.commit(arg0, arg1);
/*     */         } private final ActiveMQManagedConnection this$0;
/*     */         public void end(Xid arg0, int arg1) throws XAException {
/* 259 */           ActiveMQManagedConnection.this.xaresource.end(arg0, arg1);
/*     */         }
/*     */         public void forget(Xid arg0) throws XAException {
/* 262 */           ActiveMQManagedConnection.this.xaresource.forget(arg0);
/*     */         }
/*     */         public int getTransactionTimeout() throws XAException {
/* 265 */           return ActiveMQManagedConnection.this.xaresource.getTransactionTimeout();
/*     */         }
/*     */         public boolean isSameRM(XAResource arg0) throws XAException {
/* 268 */           return ActiveMQManagedConnection.this.xaresource.isSameRM(arg0);
/*     */         }
/*     */         public int prepare(Xid arg0) throws XAException {
/* 271 */           return ActiveMQManagedConnection.this.xaresource.prepare(arg0);
/*     */         }
/*     */         public Xid[] recover(int arg0) throws XAException {
/* 274 */           return ActiveMQManagedConnection.this.xaresource.recover(arg0);
/*     */         }
/*     */         public void rollback(Xid arg0) throws XAException {
/* 277 */           ActiveMQManagedConnection.this.xaresource.rollback(arg0);
/*     */         }
/*     */         public boolean setTransactionTimeout(int arg0) throws XAException {
/* 280 */           return ActiveMQManagedConnection.this.xaresource.setTransactionTimeout(arg0);
/*     */         }
/*     */         public void start(Xid arg0, int arg1) throws XAException {
/* 283 */           ActiveMQManagedConnection.this.xaresource.start(arg0, arg1);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocalTransaction getLocalTransaction() throws ResourceException {
/* 292 */     return new LocalTransaction()
/*     */       {
/*     */         private final ActiveMQManagedConnection this$0;
/*     */ 
/*     */         
/*     */         public void begin() {}
/*     */         
/*     */         public void commit() throws ResourceException {
/*     */           try {
/* 301 */             ActiveMQManagedConnection.this.physicalSession.commit();
/*     */           }
/* 303 */           catch (JMSException e) {
/* 304 */             throw new ResourceException("commit failed.", e);
/*     */           } 
/*     */         }
/*     */         
/*     */         public void rollback() throws ResourceException {
/*     */           try {
/* 310 */             ActiveMQManagedConnection.this.physicalSession.rollback();
/*     */           }
/* 312 */           catch (JMSException e) {
/* 313 */             throw new ResourceException("rollback failed.", e);
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ManagedConnectionMetaData getMetaData() throws ResourceException {
/* 323 */     return new ManagedConnectionMetaData() { private final ActiveMQManagedConnection this$0;
/*     */         
/*     */         public String getEISProductName() throws ResourceException {
/* 326 */           if (ActiveMQManagedConnection.this.physicalConnection == null) {
/* 327 */             throw new ResourceException("Not connected.");
/*     */           }
/*     */           try {
/* 330 */             return ActiveMQManagedConnection.this.physicalConnection.getMetaData().getJMSProviderName();
/*     */           
/*     */           }
/* 333 */           catch (JMSException e) {
/* 334 */             throw new ResourceException("Error accessing provider.", e);
/*     */           } 
/*     */         }
/*     */         
/*     */         public String getEISProductVersion() throws ResourceException {
/* 339 */           if (ActiveMQManagedConnection.this.physicalConnection == null) {
/* 340 */             throw new ResourceException("Not connected.");
/*     */           }
/*     */           try {
/* 343 */             return ActiveMQManagedConnection.this.physicalConnection.getMetaData().getProviderVersion();
/*     */           
/*     */           }
/* 346 */           catch (JMSException e) {
/* 347 */             throw new ResourceException("Error accessing provider.", e);
/*     */           } 
/*     */         }
/*     */         
/*     */         public int getMaxConnections() throws ResourceException {
/* 352 */           if (ActiveMQManagedConnection.this.physicalConnection == null) {
/* 353 */             throw new ResourceException("Not connected.");
/*     */           }
/* 355 */           return Integer.MAX_VALUE;
/*     */         }
/*     */         
/*     */         public String getUserName() throws ResourceException {
/* 359 */           if (ActiveMQManagedConnection.this.physicalConnection == null) {
/* 360 */             throw new ResourceException("Not connected.");
/*     */           }
/*     */           try {
/* 363 */             return ActiveMQManagedConnection.this.physicalConnection.getClientID();
/*     */           }
/* 365 */           catch (JMSException e) {
/* 366 */             throw new ResourceException("Error accessing provider.", e);
/*     */           } 
/*     */         } }
/*     */       ;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLogWriter(PrintWriter logWriter) throws ResourceException {
/* 376 */     this.logWriter = logWriter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrintWriter getLogWriter() throws ResourceException {
/* 383 */     return this.logWriter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(Subject subject, ConnectionRequestInfo info) {
/* 394 */     if (info == null) {
/* 395 */       return false;
/*     */     }
/* 397 */     if (info.getClass() != ActiveMQConnectionRequestInfo.class) {
/* 398 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 402 */     if ((((subject == null) ? 1 : 0) ^ ((this.subject == null) ? 1 : 0)) != 0) {
/* 403 */       return false;
/*     */     }
/* 405 */     if (subject != null && !subject.equals(this.subject)) {
/* 406 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 410 */     return info.equals(this.info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void proxyClosedEvent(JMSConnectionProxy proxy) {
/* 420 */     this.proxyConnections.remove(proxy);
/* 421 */     proxy.cleanup();
/*     */     
/* 423 */     ConnectionEvent event = new ConnectionEvent(this, 1);
/*     */     
/* 425 */     event.setConnectionHandle(proxy);
/* 426 */     Iterator iterator = this.listeners.iterator();
/* 427 */     while (iterator.hasNext()) {
/* 428 */       ConnectionEventListener l = iterator.next();
/*     */       
/* 430 */       l.connectionClosed(event);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ra\ActiveMQManagedConnection.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */