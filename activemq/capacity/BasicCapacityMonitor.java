/*     */ package org.codehaus.activemq.capacity;
/*     */ 
/*     */ import EDU.oswego.cs.dl.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.Iterator;
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
/*     */ public class BasicCapacityMonitor
/*     */   implements CapacityMonitor
/*     */ {
/*     */   private String name;
/*     */   private long valueLimit;
/*  30 */   private long currentValue = 0L;
/*  31 */   private int currentCapacity = 100;
/*  32 */   private int roundedCapacity = 100;
/*  33 */   private int roundingFactor = 10;
/*  34 */   private CopyOnWriteArrayList listeners = new CopyOnWriteArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicCapacityMonitor(String name, long valueLimit) {
/*  43 */     this.name = name;
/*  44 */     this.valueLimit = valueLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  53 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String newName) {
/*  60 */     this.name = newName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRoundingFactor() {
/*  68 */     return this.roundingFactor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRoundingFactor(int newRoundingFactor) {
/*  76 */     if (newRoundingFactor < 1 || newRoundingFactor > 100) {
/*  77 */       throw new IllegalArgumentException("invalid roundingFactor: " + newRoundingFactor);
/*     */     }
/*  79 */     this.roundingFactor = newRoundingFactor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCapacityEventListener(CapacityMonitorEventListener l) {
/*  88 */     this.listeners.add(l);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeCapacityEventListener(CapacityMonitorEventListener l) {
/*  97 */     this.listeners.remove(l);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCurrentCapacity() {
/* 106 */     return this.currentCapacity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRoundedCapacity() {
/* 114 */     return this.roundedCapacity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getCurrentValue() {
/* 123 */     return this.currentValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCurrentValue(long newCurrentValue) {
/* 132 */     this.currentValue = newCurrentValue;
/* 133 */     int newCapacity = calculateCapacity();
/* 134 */     int newRoundedCapacity = (newCapacity > 0) ? (newCapacity / this.roundingFactor * this.roundingFactor) : 0;
/* 135 */     updateCapacityChanged(newRoundedCapacity);
/* 136 */     this.currentCapacity = newCapacity;
/* 137 */     this.roundedCapacity = newRoundedCapacity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getValueLimit() {
/* 144 */     return this.valueLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValueLimit(long newValueLimit) {
/* 153 */     this.valueLimit = newValueLimit;
/*     */     
/* 155 */     setCurrentValue(this.currentValue);
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateCapacityChanged(int newRoundedCapacity) {
/* 160 */     if (this.listeners.size() > 0 && newRoundedCapacity != this.roundedCapacity) {
/* 161 */       CapacityMonitorEvent event = new CapacityMonitorEvent(this.name, newRoundedCapacity);
/* 162 */       for (Iterator i = this.listeners.iterator(); i.hasNext(); ) {
/* 163 */         CapacityMonitorEventListener listener = i.next();
/* 164 */         listener.capacityChanged(event);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int calculateCapacity() {
/* 171 */     int result = 100;
/* 172 */     if (this.currentValue != 0L) {
/* 173 */       result = (int)(100L - this.currentValue * 100L / this.valueLimit);
/*     */     }
/* 175 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\capacity\BasicCapacityMonitor.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */