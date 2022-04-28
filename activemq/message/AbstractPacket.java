/*     */ package org.codehaus.activemq.message;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.CopyOnWriteArraySet;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import org.codehaus.activemq.util.BitArray;
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
/*     */ public abstract class AbstractPacket
/*     */   implements Packet
/*     */ {
/*     */   static final int RECEIPT_REQUIRED_INDEX = 0;
/*     */   static final int BROKERS_VISITED_INDEX = 1;
/*     */   private String id;
/*     */   protected BitArray bitArray;
/*     */   private boolean receiptRequired;
/*  40 */   private transient int memoryUsage = 2048;
/*     */   
/*     */   private transient int memoryUsageReferenceCount;
/*     */   private CopyOnWriteArraySet brokersVisited;
/*     */   
/*     */   protected AbstractPacket() {
/*  46 */     this.bitArray = new BitArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/*  53 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setId(String newId) {
/*  62 */     this.id = newId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReceiptRequired() {
/*  69 */     return this.receiptRequired;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReceipt() {
/*  76 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReceiptRequired(boolean value) {
/*  85 */     this.receiptRequired = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isJMSMessage() {
/*  94 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 104 */     boolean result = (this == obj);
/* 105 */     if (!result && obj != null && obj instanceof AbstractPacket) {
/* 106 */       AbstractPacket other = (AbstractPacket)obj;
/* 107 */       result = other.getId().equals(getId());
/*     */     } 
/* 109 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 116 */     return (this.id != null) ? this.id.hashCode() : super.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMemoryUsage() {
/* 125 */     return this.memoryUsage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMemoryUsage(int newMemoryUsage) {
/* 134 */     this.memoryUsage = newMemoryUsage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int incrementMemoryReferenceCount() {
/* 144 */     return ++this.memoryUsageReferenceCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int decrementMemoryReferenceCount() {
/* 154 */     return --this.memoryUsageReferenceCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int getMemoryUsageReferenceCount() {
/* 162 */     return this.memoryUsageReferenceCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addBrokerVisited(String brokerName) {
/* 171 */     initializeBrokersVisited();
/* 172 */     this.brokersVisited.add(brokerName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasVisited(String brokerName) {
/* 182 */     if (this.brokersVisited == null) {
/* 183 */       return false;
/*     */     }
/* 185 */     return this.brokersVisited.contains(brokerName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBrokersVisitedAsString() {
/* 192 */     String result = "";
/* 193 */     if (this.brokersVisited != null && !this.brokersVisited.isEmpty()) {
/* 194 */       for (Iterator i = this.brokersVisited.iterator(); i.hasNext();) {
/* 195 */         result = result + i.next().toString() + ",";
/*     */       }
/*     */     }
/* 198 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 206 */     return getPacketTypeAsString(getPacketType()) + ": " + getId();
/*     */   }
/*     */ 
/*     */   
/*     */   protected static String getPacketTypeAsString(int type) {
/* 211 */     String packetTypeStr = "";
/* 212 */     switch (type)
/*     */     { case 6:
/* 214 */         packetTypeStr = "ACTIVEMQ_MESSAGE";
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
/* 261 */         return packetTypeStr;case 7: packetTypeStr = "ACTIVEMQ_TEXT_MESSAGE"; return packetTypeStr;case 8: packetTypeStr = "ACTIVEMQ_OBJECT_MESSAGE"; return packetTypeStr;case 9: packetTypeStr = "ACTIVEMQ_BYTES_MESSAGE"; return packetTypeStr;case 10: packetTypeStr = "ACTIVEMQ_STREAM_MESSAGE"; return packetTypeStr;case 11: packetTypeStr = "ACTIVEMQ_MAP_MESSAGE"; return packetTypeStr;case 15: packetTypeStr = "ACTIVEMQ_MSG_ACK"; return packetTypeStr;case 16: packetTypeStr = "RECEIPT_INFO"; return packetTypeStr;case 17: packetTypeStr = "CONSUMER_INFO"; return packetTypeStr;case 18: packetTypeStr = "PRODUCER_INFO"; return packetTypeStr;case 19: packetTypeStr = "TRANSACTION_INFO"; return packetTypeStr;case 20: packetTypeStr = "XA_TRANSACTION_INFO"; return packetTypeStr;case 21: packetTypeStr = "ACTIVEMQ_BROKER_INFO"; return packetTypeStr;case 22: packetTypeStr = "ACTIVEMQ_CONNECTION_INFO"; return packetTypeStr;case 23: packetTypeStr = "SESSION_INFO"; return packetTypeStr; }  packetTypeStr = "UNKNOWN PACKET TYPE: " + type; return packetTypeStr;
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
/*     */   protected boolean equals(Object left, Object right) {
/* 273 */     return (left == right || (left != null && left.equals(right)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initializeOther(AbstractPacket other) {
/* 282 */     initializeBrokersVisited();
/* 283 */     other.id = this.id;
/* 284 */     other.receiptRequired = this.receiptRequired;
/* 285 */     CopyOnWriteArraySet set = this.brokersVisited;
/* 286 */     if (set != null && !set.isEmpty()) {
/* 287 */       other.brokersVisited = new CopyOnWriteArraySet((Collection)set);
/*     */     }
/*     */   }
/*     */   
/*     */   synchronized void initializeBrokersVisited() {
/* 292 */     if (this.brokersVisited == null) {
/* 293 */       this.brokersVisited = new CopyOnWriteArraySet();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object[] getBrokersVisited() {
/* 301 */     if (this.brokersVisited == null || this.brokersVisited.isEmpty()) {
/* 302 */       return null;
/*     */     }
/* 304 */     return this.brokersVisited.toArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BitArray getBitArray() {
/* 311 */     return this.bitArray;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBitArray(BitArray bitArray) {
/* 317 */     this.bitArray = bitArray;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\AbstractPacket.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */