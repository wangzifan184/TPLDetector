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
/*     */ public class DelegateCapacityMonitor
/*     */   implements CapacityMonitor
/*     */ {
/*     */   String name;
/*     */   CapacityMonitor monitor;
/*  30 */   CopyOnWriteArrayList listeners = new CopyOnWriteArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DelegateCapacityMonitor() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DelegateCapacityMonitor(String name, CapacityMonitor cm) {
/*  45 */     this.name = name;
/*  46 */     this.monitor = cm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDelegate(CapacityMonitor cm) {
/*  54 */     this.monitor = cm;
/*  55 */     if (cm != null) {
/*  56 */       for (Iterator i = this.listeners.iterator(); i.hasNext(); ) {
/*  57 */         CapacityMonitorEventListener listener = i.next();
/*  58 */         cm.addCapacityEventListener(listener);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  69 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String newName) {
/*  76 */     this.name = newName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRoundingFactor() {
/*  84 */     return (this.monitor == null) ? 0 : this.monitor.getRoundingFactor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRoundingFactor(int newRoundingFactor) {
/*  92 */     if (this.monitor != null) {
/*  93 */       this.monitor.setRoundingFactor(newRoundingFactor);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCapacityEventListener(CapacityMonitorEventListener l) {
/* 103 */     this.listeners.add(l);
/* 104 */     if (this.monitor != null) {
/* 105 */       this.monitor.addCapacityEventListener(l);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeCapacityEventListener(CapacityMonitorEventListener l) {
/* 115 */     this.listeners.remove(l);
/* 116 */     if (this.monitor != null) {
/* 117 */       this.monitor.removeCapacityEventListener(l);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCurrentCapacity() {
/* 127 */     return (this.monitor == null) ? 100 : this.monitor.getCurrentCapacity();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRoundedCapacity() {
/* 135 */     return (this.monitor == null) ? 100 : this.monitor.getRoundedCapacity();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getCurrentValue() {
/* 144 */     return (this.monitor == null) ? 100L : this.monitor.getCurrentValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCurrentValue(long newCurrentValue) {
/* 153 */     if (this.monitor != null) {
/* 154 */       this.monitor.setCurrentValue(newCurrentValue);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getValueLimit() {
/* 162 */     return (this.monitor == null) ? 100L : this.monitor.getValueLimit();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValueLimit(long newValueLimit) {
/* 171 */     if (this.monitor != null)
/* 172 */       this.monitor.setValueLimit(newValueLimit); 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\capacity\DelegateCapacityMonitor.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */