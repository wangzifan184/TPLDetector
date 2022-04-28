
/*     */ public class ActiveMQXASession
/*     */   extends ActiveMQSession
/*     */   implements XASession, XAQueueSession, XATopicSession, XAResource
/*     */ {
/*  67 */   private static final Log log = LogFactory.getLog(ActiveMQXASession.class);
/*     */   private Xid associatedXid;
/*     */   private ActiveMQXid activeXid;
/*     */   
/*     */   public ActiveMQXASession(ActiveMQXAConnection theConnection) throws JMSException {
/*  72 */     super(theConnection, 0);
/*     */   }
/*     */   
/*     */   public boolean getTransacted() throws JMSException {
/*  76 */     return true;
/*     */   }
/*     */   
/*     */   public void rollback() throws JMSException {
/*  80 */     throw new TransactionInProgressException("Cannot rollback() inside an XASession");
/*     */   }
/*     */   
/*     */   public void commit() throws JMSException {
/*  84 */     throw new TransactionInProgressException("Cannot commit() inside an XASession");
/*     */   }
/*     */   
/*     */   public Session getSession() throws JMSException {
/*  88 */     return this;
/*     */   }
/*     */   
/*     */   public XAResource getXAResource() {
/*  92 */     return this;
/*     */   }
/*     */   
/*     */   public QueueSession getQueueSession() throws JMSException {
/*  96 */     return this;
/*     */   }
/*     */   
/*     */   public TopicSession getTopicSession() throws JMSException {
/* 100 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start(Xid xid, int flags) throws XAException {
/* 107 */     checkClosedXA();
/*     */ 
/*     */     
/* 110 */     if (this.associatedXid != null) {
/* 111 */       throw new XAException(-6);
/*     */     }
/*     */     
/* 114 */     if ((flags & 0x200000) == 2097152);
/*     */ 
/*     */     
/* 117 */     if ((flags & 0x200000) == 134217728);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     setXid(xid);
/*     */     
/* 124 */     XATransactionInfo info = new XATransactionInfo();
/* 125 */     info.setId(this.packetIdGenerator.generateId());
/* 126 */     info.setXid(this.activeXid);
/* 127 */     info.setType(101);
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 132 */       this.connection.syncSendPacket((Packet)info);
/* 133 */     } catch (JMSException e) {
/* 134 */       throw toXAException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void end(Xid xid, int flags) throws XAException {
/* 139 */     checkClosedXA();
/*     */     
/* 141 */     if ((flags & 0x2000000) == 33554432) {
/*     */       
/* 143 */       if (this.associatedXid == null || !ActiveMQXid.equals(this.associatedXid, xid)) {
/* 144 */         throw new XAException(-6);
/*     */       }
/*     */ 
/*     */       
/* 148 */       setXid((Xid)null);
/* 149 */     } else if ((flags & 0x20000000) == 536870912) {
/*     */       
/* 151 */       setXid((Xid)null);
/* 152 */     } else if ((flags & 0x4000000) == 67108864) {
/*     */ 
/*     */       
/* 155 */       if (ActiveMQXid.equals(this.associatedXid, xid)) {
/* 156 */         setXid((Xid)null);
/*     */       }
/*     */     } else {
/* 159 */       throw new XAException(-5);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int prepare(Xid xid) throws XAException {
/* 170 */     if (ActiveMQXid.equals(this.associatedXid, xid))
/*     */     {
/* 172 */       throw new XAException(-6);
/*     */     }
/*     */     
/* 175 */     ActiveMQXid x = new ActiveMQXid(xid);
/*     */ 
/*     */     
/* 178 */     XATransactionInfo info = new XATransactionInfo();
/* 179 */     info.setId(this.packetIdGenerator.generateId());
/* 180 */     info.setXid(x);
/* 181 */     info.setType(102);
/*     */ 
/*     */     
/*     */     try {
/* 185 */       IntResponseReceipt receipt = (IntResponseReceipt)this.connection.syncSendRequest((Packet)info);
/* 186 */       return receipt.getResult();
/* 187 */     } catch (JMSException e) {
/* 188 */       throw toXAException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rollback(Xid xid) throws XAException {
/*     */     ActiveMQXid x;
/* 197 */     if (ActiveMQXid.equals(this.associatedXid, xid)) {
/*     */       
/* 199 */       x = this.activeXid;
/*     */     } else {
/* 201 */       x = new ActiveMQXid(xid);
/*     */     } 
/*     */     
/* 204 */     XATransactionInfo info = new XATransactionInfo();
/* 205 */     info.setId(this.packetIdGenerator.generateId());
/* 206 */     info.setXid(x);
/* 207 */     info.setType(105);
/*     */ 
/*     */     
/*     */     try {
/* 211 */       this.connection.syncSendPacket((Packet)info);
/* 212 */     } catch (JMSException e) {
/* 213 */       throw toXAException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void commit(Xid xid, boolean onePhase) throws XAException {
/* 219 */     checkClosedXA();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 224 */     if (ActiveMQXid.equals(this.associatedXid, xid))
/*     */     {
/*     */       
/* 227 */       throw new XAException(-6);
/*     */     }
/* 229 */     ActiveMQXid x = new ActiveMQXid(xid);
/*     */ 
/*     */     
/* 232 */     XATransactionInfo info = new XATransactionInfo();
/* 233 */     info.setId(this.packetIdGenerator.generateId());
/* 234 */     info.setXid(x);
/* 235 */     info.setType(onePhase ? 109 : 103);
/*     */ 
/*     */     
/*     */     try {
/* 239 */       this.connection.syncSendPacket((Packet)info);
/* 240 */     } catch (JMSException e) {
/* 241 */       throw toXAException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void forget(Xid xid) throws XAException {
/*     */     ActiveMQXid x;
/* 247 */     checkClosedXA();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 252 */     if (ActiveMQXid.equals(this.associatedXid, xid)) {
/*     */       
/* 254 */       x = this.activeXid;
/*     */     } else {
/* 256 */       x = new ActiveMQXid(xid);
/*     */     } 
/*     */     
/* 259 */     XATransactionInfo info = new XATransactionInfo();
/* 260 */     info.setId(this.packetIdGenerator.generateId());
/* 261 */     info.setXid(x);
/* 262 */     info.setType(107);
/*     */ 
/*     */     
/*     */     try {
/* 266 */       this.connection.syncSendPacket((Packet)info);
/* 267 */     } catch (JMSException e) {
/* 268 */       throw toXAException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private String getResourceManagerId() {
/* 273 */     return ((ActiveMQXAConnection)this.connection).getResourceManagerId();
/*     */   }
/*     */   
/*     */   public boolean isSameRM(XAResource xaResource) throws XAException {
/* 277 */     if (xaResource == null) {
/* 278 */       return false;
/*     */     }
/* 280 */     if (!(xaResource instanceof ActiveMQXASession)) {
/* 281 */       return false;
/*     */     }
/* 283 */     ActiveMQXASession xar = (ActiveMQXASession)xaResource;
/* 284 */     return getResourceManagerId().equals(xar.getResourceManagerId());
/*     */   }
/*     */ 
/*     */   
/*     */   public Xid[] recover(int flag) throws XAException {
/* 289 */     checkClosedXA();
/*     */     
/* 291 */     XATransactionInfo info = new XATransactionInfo();
/* 292 */     info.setId(this.packetIdGenerator.generateId());
/* 293 */     info.setType(110);
/*     */     
/*     */     try {
/* 296 */       ResponseReceipt receipt = (ResponseReceipt)this.connection.syncSendRequest((Packet)info);
/* 297 */       return (Xid[])receipt.getResult();
/* 298 */     } catch (JMSException e) {
/* 299 */       throw toXAException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTransactionTimeout() throws XAException {
/* 305 */     checkClosedXA();
/*     */     
/* 307 */     XATransactionInfo info = new XATransactionInfo();
/* 308 */     info.setId(this.packetIdGenerator.generateId());
/* 309 */     info.setType(111);
/*     */ 
/*     */     
/*     */     try {
/* 313 */       IntResponseReceipt receipt = (IntResponseReceipt)this.connection.syncSendRequest((Packet)info);
/* 314 */       return receipt.getResult();
/* 315 */     } catch (JMSException e) {
/* 316 */       throw toXAException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setTransactionTimeout(int seconds) throws XAException {
/* 322 */     checkClosedXA();
/*     */     
/* 324 */     XATransactionInfo info = new XATransactionInfo();
/* 325 */     info.setId(this.packetIdGenerator.generateId());
/* 326 */     info.setType(112);
/* 327 */     info.setTransactionTimeout(seconds);
/*     */ 
/*     */     
/*     */     try {
/* 331 */       this.connection.asyncSendPacket((Packet)info);
/* 332 */       return true;
/* 333 */     } catch (JMSException e) {
/* 334 */       throw toXAException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws JMSException {
/* 342 */     if (!this.closed.get()) {
/* 343 */       doClose();
/* 344 */       this.closed.set(true);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkClosedXA() throws XAException {
/* 353 */     if (this.closed.get()) {
/* 354 */       throw new XAException(-7);
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean isXaTransacted() {
/* 359 */     return true;
/*     */   }
/*     */   
/*     */   protected String getNextTransactionId() {
/* 363 */     return this.currentTransactionId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doStartTransaction() throws JMSException {
/* 374 */     if (this.associatedXid == null) {
/* 375 */       throw new JMSException("Session's XAResource has not been enlisted in a distributed transaction.");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void setXid(Xid xid) {
/* 381 */     if (xid != null) {
/*     */       
/* 383 */       this.associatedXid = xid;
/* 384 */       this.activeXid = new ActiveMQXid(xid);
/* 385 */       this.currentTransactionId = this.activeXid.toLocalTransactionId();
/*     */     } else {
/*     */       
/* 388 */       this.associatedXid = null;
/* 389 */       this.activeXid = null;
/* 390 */       this.currentTransactionId = null;
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
/*     */   private XAException toXAException(JMSException e) {
/* 403 */     if (e.getCause() != null && e.getCause() instanceof XAException) {
/* 404 */       XAException original = (XAException)e.getCause();
/* 405 */       XAException xAException1 = new XAException(original.getMessage());
/* 406 */       xAException1.errorCode = original.errorCode;
/* 407 */       xAException1.initCause(original);
/* 408 */       return xAException1;
/*     */     } 
/*     */     
/* 411 */     XAException xae = new XAException(e.getMessage());
/* 412 */     xae.errorCode = -7;
/* 413 */     xae.initCause((Throwable)e);
/* 414 */     return xae;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ActiveMQXASession.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */