/*     */ package org.codehaus.activemq.service.impl;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.codehaus.activemq.service.QueueList;
/*     */ import org.codehaus.activemq.service.QueueListEntry;
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
/*     */ public final class DefaultQueueList
/*     */   implements QueueList, Cloneable, Serializable
/*     */ {
/*  34 */   private transient DefaultQueueListEntry header = new DefaultQueueListEntry(null, null, null);
/*  35 */   private transient int size = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultQueueList() {
/*  41 */     this.header.next = this.header.previous = this.header;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Object getFirst() {
/*  49 */     if (this.size == 0) {
/*  50 */       return null;
/*     */     }
/*  52 */     return this.header.next.element;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Object getLast() {
/*  60 */     if (this.size == 0) {
/*  61 */       return null;
/*     */     }
/*  63 */     return this.header.previous.element;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Object removeFirst() {
/*  71 */     if (this.size == 0) {
/*  72 */       return null;
/*     */     }
/*  74 */     Object first = this.header.next.element;
/*  75 */     remove(this.header.next);
/*  76 */     return first;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void rotate() {
/*  84 */     if (this.size > 1) {
/*  85 */       Object obj = removeFirst();
/*  86 */       if (obj != null) {
/*  87 */         addLast(obj);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Object removeLast() {
/*  96 */     if (this.size == 0) {
/*  97 */       return null;
/*     */     }
/*  99 */     Object last = this.header.previous.element;
/* 100 */     remove(this.header.previous);
/* 101 */     return last;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized QueueListEntry addFirst(Object o) {
/* 106 */     return addBefore(o, this.header.next);
/*     */   }
/*     */   
/*     */   public synchronized QueueListEntry addLast(Object o) {
/* 110 */     return addBefore(o, this.header);
/*     */   }
/*     */   
/*     */   public synchronized boolean contains(Object o) {
/* 114 */     return (indexOf(o) != -1);
/*     */   }
/*     */   
/*     */   public synchronized int size() {
/* 118 */     return this.size;
/*     */   }
/*     */   
/*     */   public synchronized boolean isEmpty() {
/* 122 */     return (this.size == 0);
/*     */   }
/*     */   
/*     */   public synchronized QueueListEntry add(Object o) {
/* 126 */     return addBefore(o, this.header);
/*     */   }
/*     */   
/*     */   public synchronized boolean remove(Object o) {
/* 130 */     if (o == null) {
/* 131 */       for (DefaultQueueListEntry e = this.header.next; e != this.header; e = e.next) {
/* 132 */         if (e.element == null) {
/* 133 */           remove(e);
/* 134 */           return true;
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 139 */       for (DefaultQueueListEntry e = this.header.next; e != this.header; e = e.next) {
/* 140 */         if (o.equals(e.element)) {
/* 141 */           remove(e);
/* 142 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 146 */     return false;
/*     */   }
/*     */   
/*     */   public synchronized void clear() {
/* 150 */     this.header.next = this.header.previous = this.header;
/* 151 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Object get(int index) {
/* 156 */     return (entry(index)).element;
/*     */   }
/*     */   
/*     */   public synchronized Object set(int index, Object element) {
/* 160 */     DefaultQueueListEntry e = entry(index);
/* 161 */     Object oldVal = e.element;
/* 162 */     e.element = element;
/* 163 */     return oldVal;
/*     */   }
/*     */   
/*     */   public synchronized void add(int index, Object element) {
/* 167 */     addBefore(element, (index == this.size) ? this.header : entry(index));
/*     */   }
/*     */   
/*     */   public synchronized Object remove(int index) {
/* 171 */     DefaultQueueListEntry e = entry(index);
/* 172 */     remove(e);
/* 173 */     return e.element;
/*     */   }
/*     */   
/*     */   public synchronized DefaultQueueListEntry entry(int index) {
/* 177 */     if (index < 0 || index >= this.size) {
/* 178 */       throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
/*     */     }
/* 180 */     DefaultQueueListEntry e = this.header;
/* 181 */     if (index < this.size / 2) {
/* 182 */       for (int i = 0; i <= index; i++) {
/* 183 */         e = e.next;
/*     */       }
/*     */     } else {
/*     */       
/* 187 */       for (int i = this.size; i > index; i--) {
/* 188 */         e = e.previous;
/*     */       }
/*     */     } 
/* 191 */     return e;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int indexOf(Object o) {
/* 196 */     int index = 0;
/* 197 */     if (o == null) {
/* 198 */       for (DefaultQueueListEntry e = this.header.next; e != this.header; e = e.next) {
/* 199 */         if (e.element == null) {
/* 200 */           return index;
/*     */         }
/* 202 */         index++;
/*     */       } 
/*     */     } else {
/*     */       
/* 206 */       for (DefaultQueueListEntry e = this.header.next; e != this.header; e = e.next) {
/* 207 */         if (o.equals(e.element)) {
/* 208 */           return index;
/*     */         }
/* 210 */         index++;
/*     */       } 
/*     */     } 
/* 213 */     return -1;
/*     */   }
/*     */   
/*     */   public synchronized int lastIndexOf(Object o) {
/* 217 */     int index = this.size;
/* 218 */     if (o == null) {
/* 219 */       for (DefaultQueueListEntry e = this.header.previous; e != this.header; e = e.previous) {
/* 220 */         index--;
/* 221 */         if (e.element == null) {
/* 222 */           return index;
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       
/* 227 */       for (DefaultQueueListEntry e = this.header.previous; e != this.header; e = e.previous) {
/* 228 */         index--;
/* 229 */         if (o.equals(e.element)) {
/* 230 */           return index;
/*     */         }
/*     */       } 
/*     */     } 
/* 234 */     return -1;
/*     */   }
/*     */   
/*     */   public synchronized QueueListEntry getFirstEntry() {
/* 238 */     DefaultQueueListEntry result = this.header.next;
/* 239 */     return (result != this.header) ? result : null;
/*     */   }
/*     */   
/*     */   public synchronized QueueListEntry getLastEntry() {
/* 243 */     DefaultQueueListEntry result = this.header.previous;
/* 244 */     return (result != this.header) ? result : null;
/*     */   }
/*     */   
/*     */   public QueueListEntry getNextEntry(QueueListEntry node) {
/* 248 */     DefaultQueueListEntry entry = (DefaultQueueListEntry)node;
/* 249 */     if (entry == null) {
/* 250 */       return null;
/*     */     }
/* 252 */     DefaultQueueListEntry result = entry.next;
/* 253 */     return (result != entry && result != this.header) ? result : null;
/*     */   }
/*     */   
/*     */   public QueueListEntry getPrevEntry(QueueListEntry node) {
/* 257 */     DefaultQueueListEntry entry = (DefaultQueueListEntry)node;
/* 258 */     if (entry == null) {
/* 259 */       return null;
/*     */     }
/* 261 */     DefaultQueueListEntry result = entry.previous;
/* 262 */     return (result != entry && result != this.header) ? result : null;
/*     */   }
/*     */   
/*     */   public synchronized QueueListEntry addBefore(Object o, QueueListEntry node) {
/* 266 */     DefaultQueueListEntry e = (DefaultQueueListEntry)node;
/* 267 */     DefaultQueueListEntry newLinkedListEntry = new DefaultQueueListEntry(o, e, e.previous);
/* 268 */     newLinkedListEntry.previous.next = newLinkedListEntry;
/* 269 */     newLinkedListEntry.next.previous = newLinkedListEntry;
/* 270 */     this.size++;
/* 271 */     return newLinkedListEntry;
/*     */   }
/*     */   
/*     */   public synchronized void remove(QueueListEntry node) {
/* 275 */     DefaultQueueListEntry e = (DefaultQueueListEntry)node;
/* 276 */     if (e == this.header) {
/*     */       return;
/*     */     }
/* 279 */     e.previous.next = e.next;
/* 280 */     e.next.previous = e.previous;
/* 281 */     this.size--;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Object clone() {
/* 290 */     DefaultQueueList clone = new DefaultQueueList();
/*     */     
/* 292 */     clone.header = new DefaultQueueListEntry(null, null, null);
/* 293 */     clone.header.next = clone.header.previous = clone.header;
/* 294 */     clone.size = 0;
/*     */     
/* 296 */     for (DefaultQueueListEntry e = this.header.next; e != this.header; e = e.next) {
/* 297 */       clone.add(e.element);
/*     */     }
/* 299 */     return clone;
/*     */   }
/*     */   
/*     */   public synchronized Object[] toArray() {
/* 303 */     Object[] result = new Object[this.size];
/* 304 */     int i = 0;
/* 305 */     for (DefaultQueueListEntry e = this.header.next; e != this.header; e = e.next) {
/* 306 */       result[i++] = e.element;
/*     */     }
/* 308 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\DefaultQueueList.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */