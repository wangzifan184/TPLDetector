/*     */ package org.codehaus.activemq;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import javax.jms.Message;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.util.BitArrayBin;
/*     */ import org.codehaus.activemq.util.IdGenerator;
/*     */ import org.codehaus.activemq.util.LRUCache;
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
/*     */ public class ActiveMQMessageAudit
/*     */ {
/*     */   private static final int DEFAULT_WINDOW_SIZE = 1024;
/*     */   private static final int MAXIMUM_PRODUCER_COUNT = 128;
/*     */   private int windowSize;
/*     */   private LinkedHashMap map;
/*     */   
/*     */   public ActiveMQMessageAudit() {
/*  44 */     this(1024, 128);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQMessageAudit(int windowSize, int maximumNumberOfProducersToTrack) {
/*  55 */     this.windowSize = windowSize;
/*  56 */     this.map = (LinkedHashMap)new LRUCache(maximumNumberOfProducersToTrack);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDuplicate(Message message) {
/*  66 */     if (message instanceof Packet) {
/*  67 */       return isDuplicate((Packet)message);
/*     */     }
/*  69 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDuplicate(Packet packet) {
/*  79 */     return isDuplicate(packet.getId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDuplicate(String id) {
/*  89 */     boolean answer = false;
/*  90 */     String seed = IdGenerator.getSeedFromId(id);
/*  91 */     if (seed != null) {
/*  92 */       BitArrayBin bab = (BitArrayBin)this.map.get(seed);
/*  93 */       if (bab == null) {
/*  94 */         bab = new BitArrayBin(this.windowSize);
/*  95 */         this.map.put(seed, bab);
/*     */       } 
/*  97 */       long index = IdGenerator.getCountFromId(id);
/*  98 */       if (index >= 0L) {
/*  99 */         answer = bab.setBit(index, true);
/*     */       }
/*     */     } 
/* 102 */     return answer;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ActiveMQMessageAudit.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */