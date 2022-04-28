/*     */ package org.codehaus.activemq.store.howl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.MessageAck;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.service.MessageIdentity;
/*     */ import org.codehaus.activemq.service.QueueMessageContainer;
/*     */ import org.codehaus.activemq.store.MessageStore;
/*     */ import org.codehaus.activemq.store.PersistenceAdapter;
/*     */ import org.codehaus.activemq.util.Callback;
/*     */ import org.codehaus.activemq.util.JMSExceptionHelper;
/*     */ import org.codehaus.activemq.util.TransactionTemplate;
/*     */ import org.objectweb.howl.log.LogConfigurationException;
/*     */ import org.objectweb.howl.log.LogException;
/*     */ import org.objectweb.howl.log.LogRecord;
/*     */ import org.objectweb.howl.log.Logger;
/*     */ import org.objectweb.howl.log.ReplayListener;
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
/*     */ public class HowlMessageStore
/*     */   implements MessageStore
/*     */ {
/*     */   private static final int DEFAULT_RECORD_SIZE = 65536;
/*  54 */   private static final Log log = LogFactory.getLog(HowlMessageStore.class);
/*     */   
/*     */   private HowlPersistenceAdapter longTermPersistence;
/*     */   private MessageStore longTermStore;
/*     */   private Logger transactionLog;
/*     */   private WireFormat wireFormat;
/*     */   private TransactionTemplate transactionTemplate;
/*  61 */   private int maximumCacheSize = 100;
/*  62 */   private Map map = new LinkedHashMap();
/*     */   private boolean sync = true;
/*     */   private long lastLogMark;
/*     */   private Exception firstException;
/*     */   
/*     */   public HowlMessageStore(HowlPersistenceAdapter adapter, MessageStore checkpointStore, Logger transactionLog, WireFormat wireFormat) {
/*  68 */     this.longTermPersistence = adapter;
/*  69 */     this.longTermStore = checkpointStore;
/*  70 */     this.transactionLog = transactionLog;
/*  71 */     this.wireFormat = wireFormat;
/*  72 */     this.transactionTemplate = new TransactionTemplate((PersistenceAdapter)adapter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized MessageIdentity addMessage(ActiveMQMessage message) throws JMSException {
/*  83 */     writePacket((Packet)message);
/*     */ 
/*     */     
/*  86 */     if (!addMessageToCache(message)) {
/*  87 */       log.warn("Not enough RAM to store the active transaction log and so we're having to forcea checkpoint so that we can ensure that reads are efficient and do not have to replay the transaction log");
/*     */ 
/*     */       
/*  90 */       checkpoint(message);
/*     */ 
/*     */       
/*  93 */       this.longTermStore.addMessage(message);
/*     */     } 
/*  95 */     return message.getJMSMessageIdentity();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQMessage getMessage(MessageIdentity identity) throws JMSException {
/* 103 */     ActiveMQMessage answer = null;
/* 104 */     synchronized (this.map) {
/* 105 */       answer = (ActiveMQMessage)this.map.get(identity.getMessageID());
/*     */     } 
/* 107 */     if (answer == null) {
/* 108 */       answer = this.longTermStore.getMessage(identity);
/*     */     }
/* 110 */     return answer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeMessage(MessageIdentity identity, MessageAck ack) throws JMSException {
/* 119 */     writePacket((Packet)ack);
/*     */     
/* 121 */     synchronized (this.map) {
/* 122 */       this.map.remove(identity.getMessageID());
/*     */     } 
/* 124 */     this.longTermPersistence.onMessageRemove(this);
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
/*     */   public synchronized void recover(final QueueMessageContainer container) throws JMSException {
/* 136 */     this.longTermStore.recover(container);
/*     */ 
/*     */ 
/*     */     
/* 140 */     this.firstException = null;
/*     */     try {
/* 142 */       this.transactionLog.replay(new ReplayListener() {
/* 143 */             LogRecord record = new LogRecord(65536); private final QueueMessageContainer val$container;
/*     */             
/*     */             public void onRecord(LogRecord logRecord) {
/* 146 */               HowlMessageStore.this.readPacket(logRecord, container);
/*     */             }
/*     */             private final HowlMessageStore this$0;
/*     */             public void onError(LogException e) {
/* 150 */               HowlMessageStore.log.error("Error while recovering Howl transaction log: " + e, (Throwable)e);
/*     */             }
/*     */             
/*     */             public LogRecord getLogRecord() {
/* 154 */               return this.record;
/*     */             }
/*     */           });
/*     */     }
/* 158 */     catch (LogConfigurationException e) {
/* 159 */       throw createRecoveryFailedException(e);
/*     */     } 
/* 161 */     if (this.firstException != null) {
/* 162 */       if (this.firstException instanceof JMSException) {
/* 163 */         throw (JMSException)this.firstException;
/*     */       }
/*     */       
/* 166 */       throw createRecoveryFailedException(this.firstException);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void start() throws JMSException {
/* 172 */     this.longTermStore.start();
/*     */   }
/*     */   
/*     */   public synchronized void stop() throws JMSException {
/* 176 */     this.longTermStore.stop();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void checkpoint() throws JMSException {
/* 184 */     checkpoint(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaximumCacheSize() {
/* 191 */     return this.maximumCacheSize;
/*     */   }
/*     */   
/*     */   public void setMaximumCacheSize(int maximumCacheSize) {
/* 195 */     this.maximumCacheSize = maximumCacheSize;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkpoint(final ActiveMQMessage message) throws JMSException {
/* 211 */     ActiveMQMessage[] temp = null;
/* 212 */     synchronized (this.map) {
/* 213 */       temp = new ActiveMQMessage[this.map.size()];
/* 214 */       this.map.values().toArray((Object[])temp);
/*     */ 
/*     */ 
/*     */       
/* 218 */       this.map.clear();
/*     */     } 
/*     */     
/* 221 */     final ActiveMQMessage[] data = temp;
/* 222 */     this.transactionTemplate.run(new Callback() { private final ActiveMQMessage[] val$data;
/*     */           public void execute() throws Throwable {
/* 224 */             for (int i = 0, size = data.length; i < size; i++) {
/* 225 */               HowlMessageStore.this.longTermStore.addMessage(data[i]);
/*     */             }
/* 227 */             if (message != null)
/* 228 */               HowlMessageStore.this.longTermStore.addMessage(message); 
/*     */           }
/*     */           private final ActiveMQMessage val$message;
/*     */           private final HowlMessageStore this$0; }
/*     */       );
/*     */     try {
/* 234 */       this.transactionLog.mark(this.lastLogMark);
/*     */     }
/* 236 */     catch (Exception e) {
/* 237 */       throw JMSExceptionHelper.newJMSException("Failed to checkpoint the Howl transaction log: " + e, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean addMessageToCache(ActiveMQMessage message) {
/* 248 */     synchronized (this.map) {
/* 249 */       if (this.map.size() < this.maximumCacheSize && this.longTermPersistence.hasCacheCapacity(this)) {
/* 250 */         this.map.put(message.getJMSMessageID(), message);
/* 251 */         return true;
/*     */       } 
/*     */     } 
/* 254 */     return false;
/*     */   }
/*     */   
/*     */   protected void readPacket(LogRecord logRecord, QueueMessageContainer container) {
/* 258 */     if (!logRecord.isCTRL() && !logRecord.isEOB() && logRecord.length > 0) {
/*     */       
/*     */       try {
/* 261 */         Packet packet = this.wireFormat.fromBytes(logRecord.data, 2, logRecord.length - 2);
/* 262 */         if (packet instanceof ActiveMQMessage) {
/* 263 */           container.addMessage((ActiveMQMessage)packet);
/*     */         }
/* 265 */         else if (packet instanceof MessageAck) {
/* 266 */           MessageAck ack = (MessageAck)packet;
/* 267 */           container.delete(ack.getMessageIdentity(), ack);
/*     */         } else {
/*     */           
/* 270 */           log.error("Unknown type of packet in transaction log which will be discarded: " + packet);
/*     */         }
/*     */       
/* 273 */       } catch (Exception e) {
/* 274 */         if (this.firstException == null) {
/* 275 */           this.firstException = e;
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void writePacket(Packet packet) throws JMSException {
/*     */     try {
/* 286 */       byte[] data = this.wireFormat.toBytes(packet);
/* 287 */       this.lastLogMark = this.transactionLog.put(data, this.sync);
/*     */     }
/* 289 */     catch (IOException e) {
/* 290 */       throw createWriteException(packet, e);
/*     */     }
/* 292 */     catch (LogException e) {
/* 293 */       throw createWriteException(packet, e);
/*     */     }
/* 295 */     catch (InterruptedException e) {
/* 296 */       throw createWriteException(packet, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected JMSException createRecoveryFailedException(Exception e) {
/* 302 */     return JMSExceptionHelper.newJMSException("Failed to recover from Howl transaction log. Reason: " + e, e);
/*     */   }
/*     */   
/*     */   protected JMSException createWriteException(Packet packet, Exception e) {
/* 306 */     return JMSExceptionHelper.newJMSException("Failed to write to Howl transaction log for: " + packet + ". Reason: " + e, e);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\howl\HowlMessageStore.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */