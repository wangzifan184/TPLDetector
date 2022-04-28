/*     */ package org.codehaus.activemq.service.impl;
/*     */ 
/*     */ import com.sleepycat.util.FastInputStream;
/*     */ import com.sleepycat.util.FastOutputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import javax.jms.JMSException;
/*     */ import org.codehaus.activemq.service.QueueList;
/*     */ import org.codehaus.activemq.service.QueueListEntry;
/*     */ import org.codehaus.activemq.util.JMSExceptionHelper;
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
/*     */ public abstract class QueueListSupport
/*     */   implements QueueList
/*     */ {
/*  41 */   protected static final Long HEAD_KEY = new Long(0L);
/*     */   
/*     */   public static class Header
/*     */     implements Serializable {
/*     */     private static final long serialVersionUID = 64734383295040L;
/*     */     public Long headKey;
/*     */     public Long tailKey;
/*     */     public long lastKeyValue;
/*     */     public int size;
/*     */     
/*     */     public byte[] asBytes() throws IOException {
/*  52 */       ByteArrayOutputStream buffer = new ByteArrayOutputStream();
/*  53 */       DataOutputStream out = new DataOutputStream(buffer);
/*  54 */       out.writeLong(QueueListSupport.unwrapLong(this.headKey));
/*  55 */       out.writeLong(QueueListSupport.unwrapLong(this.tailKey));
/*  56 */       out.writeLong(this.lastKeyValue);
/*  57 */       out.writeInt(this.size);
/*  58 */       return buffer.toByteArray();
/*     */     }
/*     */     
/*     */     public void fromBytes(byte[] data) throws IOException {
/*  62 */       DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
/*  63 */       this.headKey = QueueListSupport.wrapLong(in.readLong());
/*  64 */       this.tailKey = QueueListSupport.wrapLong(in.readLong());
/*  65 */       this.lastKeyValue = in.readLong();
/*  66 */       this.size = in.readInt();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Node
/*     */     implements Serializable, QueueListEntry
/*     */   {
/*     */     private static final long serialVersionUID = 4609474001468609536L;
/*     */     public Long previousKey;
/*     */     public Long nextKey;
/*     */     public Object value;
/*     */     public transient Long key;
/*     */     
/*     */     public Object getElement() {
/*  81 */       return this.value;
/*     */     }
/*     */     
/*     */     public byte[] asBytes() throws IOException {
/*  85 */       FastOutputStream buffer = new FastOutputStream();
/*  86 */       DataOutputStream out = new DataOutputStream((OutputStream)buffer);
/*  87 */       out.writeLong(QueueListSupport.unwrapLong(this.previousKey));
/*  88 */       out.writeLong(QueueListSupport.unwrapLong(this.nextKey));
/*  89 */       out.writeUTF((String)this.value);
/*  90 */       return buffer.toByteArray();
/*     */     }
/*     */     
/*     */     public void fromBytes(byte[] data) throws IOException {
/*  94 */       DataInputStream in = new DataInputStream((InputStream)new FastInputStream(data));
/*  95 */       this.previousKey = QueueListSupport.wrapLong(in.readLong());
/*  96 */       this.nextKey = QueueListSupport.wrapLong(in.readLong());
/*  97 */       this.value = in.readUTF();
/*     */     }
/*     */   }
/*     */   
/*     */   public Object getFirst() throws JMSException {
/*     */     try {
/* 103 */       Long key = (getHeader()).headKey;
/* 104 */       if (key != null) {
/* 105 */         Node node = getNode(key);
/* 106 */         if (node != null) {
/* 107 */           return node.getElement();
/*     */         }
/*     */       } 
/* 110 */       return null;
/*     */     }
/* 112 */     catch (IOException e) {
/* 113 */       throw JMSExceptionHelper.newJMSException("Failed to read from table: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object getLast() throws JMSException {
/*     */     try {
/* 119 */       Long key = (getHeader()).tailKey;
/* 120 */       if (key != null) {
/* 121 */         Node node = getNode(key);
/* 122 */         if (node != null) {
/* 123 */           return node.getElement();
/*     */         }
/*     */       } 
/* 126 */       return null;
/*     */     }
/* 128 */     catch (IOException e) {
/* 129 */       throw JMSExceptionHelper.newJMSException("Failed to read from table: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object removeFirst() throws JMSException {
/*     */     try {
/* 135 */       Header header = getHeader();
/* 136 */       Long key = header.headKey;
/* 137 */       if (key != null) {
/* 138 */         Node node = getNode(key);
/* 139 */         if (node != null) {
/* 140 */           doRemoveNode(node);
/* 141 */           header.headKey = node.nextKey;
/* 142 */           header.size--;
/* 143 */           updateHeader(header);
/* 144 */           return node.getElement();
/*     */         } 
/*     */       } 
/* 147 */       return null;
/*     */     }
/* 149 */     catch (IOException e) {
/* 150 */       throw JMSExceptionHelper.newJMSException("Failed to write to table: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object removeLast() throws JMSException {
/*     */     try {
/* 156 */       Header header = getHeader();
/* 157 */       Long key = header.tailKey;
/* 158 */       if (key != null) {
/* 159 */         Node node = getNode(key);
/* 160 */         if (node != null) {
/* 161 */           doRemoveNode(node);
/* 162 */           header.tailKey = node.previousKey;
/* 163 */           header.size--;
/* 164 */           updateHeader(header);
/* 165 */           return node.getElement();
/*     */         } 
/*     */       } 
/* 168 */       return null;
/*     */     }
/* 170 */     catch (IOException e) {
/* 171 */       throw JMSExceptionHelper.newJMSException("Failed to write to table: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public QueueListEntry addFirst(Object value) throws JMSException {
/*     */     try {
/* 177 */       Header header = getHeader();
/* 178 */       Node node = createNode();
/* 179 */       node.value = value;
/* 180 */       Long nextKey = header.headKey;
/* 181 */       node.nextKey = nextKey;
/* 182 */       Long key = createKey(header);
/* 183 */       node.key = key;
/* 184 */       updateNode(node);
/* 185 */       updateNextNode(nextKey, key);
/* 186 */       header.headKey = key;
/* 187 */       if (header.tailKey == null) {
/* 188 */         header.tailKey = key;
/*     */       }
/* 190 */       header.size++;
/* 191 */       updateHeader(header);
/* 192 */       return node;
/*     */     }
/* 194 */     catch (IOException e) {
/* 195 */       throw JMSExceptionHelper.newJMSException("Failed to write to table: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public QueueListEntry addLast(Object value) throws JMSException {
/*     */     try {
/* 201 */       Header header = getHeader();
/* 202 */       return doAddLast(value, header);
/*     */     }
/* 204 */     catch (IOException e) {
/* 205 */       throw JMSExceptionHelper.newJMSException("Failed to write to table: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean contains(Object value) throws JMSException {
/* 210 */     return (indexOf(value) != -1);
/*     */   }
/*     */   
/*     */   public int size() throws JMSException {
/*     */     try {
/* 215 */       return (getHeader()).size;
/*     */     }
/* 217 */     catch (IOException e) {
/* 218 */       throw JMSExceptionHelper.newJMSException("Failed to read from table: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isEmpty() throws JMSException {
/* 223 */     int size = size();
/* 224 */     return (size == 0);
/*     */   }
/*     */   
/*     */   public QueueListEntry add(Object value) throws JMSException {
/* 228 */     return addLast(value);
/*     */   }
/*     */   
/*     */   public Object get(int index) throws JMSException {
/*     */     try {
/* 233 */       Node node = getNode(index);
/* 234 */       if (node != null) {
/* 235 */         return node.value;
/*     */       }
/* 237 */       return null;
/*     */     }
/* 239 */     catch (IOException e) {
/* 240 */       throw JMSExceptionHelper.newJMSException("Failed to read from table: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object set(int index, Object element) throws JMSException {
/*     */     try {
/* 246 */       Node node = getNode(index);
/* 247 */       if (node != null) {
/* 248 */         Object previous = node.value;
/* 249 */         node.value = element;
/* 250 */         updateNode(node);
/* 251 */         return previous;
/*     */       } 
/* 253 */       return null;
/*     */     }
/* 255 */     catch (IOException e) {
/* 256 */       throw JMSExceptionHelper.newJMSException("Failed to write to table: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void add(int index, Object element) throws JMSException {
/* 261 */     if (index == 0) {
/* 262 */       addFirst(element);
/*     */     } else {
/*     */       
/*     */       try {
/* 266 */         Header header = getHeader();
/* 267 */         Node nextNode = getNode(header, index);
/* 268 */         doAddBefore(header, nextNode, element);
/*     */       }
/* 270 */       catch (IOException e) {
/* 271 */         throw JMSExceptionHelper.newJMSException("Failed to write to table: " + e, e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object remove(int index) throws JMSException {
/*     */     try {
/* 278 */       Node node = getNode(index);
/* 279 */       if (node != null) {
/* 280 */         removeNode(node);
/*     */       }
/* 282 */       return null;
/*     */     }
/* 284 */     catch (IOException e) {
/* 285 */       throw JMSExceptionHelper.newJMSException("Failed to write to table: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int indexOf(Object value) throws JMSException {
/*     */     try {
/* 291 */       Header header = getHeader();
/* 292 */       Long key = header.headKey;
/* 293 */       for (int i = 0; key != null; i++) {
/* 294 */         Node node = getNode(key);
/* 295 */         if (node == null) {
/*     */           break;
/*     */         }
/* 298 */         if (value == node.value || (value != null && value.equals(node.value))) {
/* 299 */           return i;
/*     */         }
/* 301 */         key = node.nextKey;
/*     */       } 
/* 303 */       return -1;
/*     */     }
/* 305 */     catch (IOException e) {
/* 306 */       throw JMSExceptionHelper.newJMSException("Failed to read from table: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int lastIndexOf(Object value) throws JMSException {
/*     */     try {
/* 312 */       Header header = getHeader();
/* 313 */       Long key = header.tailKey;
/* 314 */       for (int i = header.size - 1; key != null; i--) {
/* 315 */         Node node = getNode(key);
/* 316 */         if (node == null) {
/*     */           break;
/*     */         }
/* 319 */         if (value == node.value || (value != null && value.equals(node.value))) {
/* 320 */           return i;
/*     */         }
/* 322 */         key = node.previousKey;
/*     */       } 
/* 324 */       return -1;
/*     */     }
/* 326 */     catch (IOException e) {
/* 327 */       throw JMSExceptionHelper.newJMSException("Failed to read from table: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public QueueListEntry getFirstEntry() throws JMSException {
/*     */     try {
/* 333 */       return getNode((getHeader()).headKey);
/*     */     }
/* 335 */     catch (IOException e) {
/* 336 */       throw JMSExceptionHelper.newJMSException("Failed to read from table: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public QueueListEntry getLastEntry() throws JMSException {
/*     */     try {
/* 342 */       return getNode((getHeader()).tailKey);
/*     */     }
/* 344 */     catch (IOException e) {
/* 345 */       throw JMSExceptionHelper.newJMSException("Failed to read from table: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public QueueListEntry getNextEntry(QueueListEntry entry) throws JMSException {
/* 350 */     Node node = (Node)entry;
/*     */     try {
/* 352 */       return getNode(node.nextKey);
/*     */     }
/* 354 */     catch (IOException e) {
/* 355 */       throw JMSExceptionHelper.newJMSException("Failed to read from table: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public QueueListEntry getPrevEntry(QueueListEntry entry) throws JMSException {
/* 360 */     Node node = (Node)entry;
/*     */     try {
/* 362 */       return getNode(node.previousKey);
/*     */     }
/* 364 */     catch (IOException e) {
/* 365 */       throw JMSExceptionHelper.newJMSException("Failed to read from table: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public QueueListEntry addBefore(Object value, QueueListEntry entry) throws JMSException {
/*     */     try {
/* 371 */       return doAddBefore(getHeader(), (Node)entry, value);
/*     */     }
/* 373 */     catch (IOException e) {
/* 374 */       throw JMSExceptionHelper.newJMSException("Failed to write to table: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void remove(QueueListEntry entry) throws JMSException {
/*     */     try {
/* 380 */       removeNode((Node)entry);
/*     */     }
/* 382 */     catch (IOException e) {
/* 383 */       throw JMSExceptionHelper.newJMSException("Failed to write to table: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object[] toArray() throws JMSException {
/*     */     try {
/* 389 */       Header header = getHeader();
/* 390 */       int size = header.size;
/* 391 */       if (size == 0) {
/* 392 */         return EMPTY_ARRAY;
/*     */       }
/* 394 */       Long key = header.headKey;
/* 395 */       Object[] answer = new Object[size];
/* 396 */       for (int i = 0; i < size && key != null; i++) {
/* 397 */         Node node = getNode(key);
/* 398 */         if (node != null) {
/* 399 */           answer[i] = node.value;
/* 400 */           key = node.nextKey;
/*     */         } 
/*     */       } 
/* 403 */       return answer;
/*     */     }
/* 405 */     catch (IOException e) {
/* 406 */       throw JMSExceptionHelper.newJMSException("Failed to write to table: " + e, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void rotate() throws JMSException {
/* 412 */     Object obj = removeFirst();
/* 413 */     if (obj != null) {
/* 414 */       addLast(obj);
/*     */     }
/*     */   }
/*     */   
/*     */   protected Long createKey(Header header) throws IOException, JMSException {
/* 419 */     long value = header.lastKeyValue;
/*     */     while (true) {
/* 421 */       if (value == Long.MAX_VALUE) {
/* 422 */         value = 1L;
/*     */       } else {
/*     */         
/* 425 */         value++;
/*     */       } 
/* 427 */       Long key = new Long(value);
/* 428 */       if (getNode(key) == null) {
/* 429 */         header.lastKeyValue = value;
/* 430 */         return key;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean removeNode(Node node) throws IOException, JMSException {
/* 436 */     Header header = null;
/* 437 */     boolean updatedPrevious = false;
/* 438 */     if (node.previousKey != null) {
/*     */       
/* 440 */       Node previousNode = getNode(node.previousKey);
/* 441 */       if (previousNode != null) {
/* 442 */         previousNode.nextKey = node.nextKey;
/* 443 */         updateNode(previousNode);
/* 444 */         updatedPrevious = true;
/*     */       } 
/*     */     } 
/* 447 */     if (!updatedPrevious) {
/*     */       
/* 449 */       header = getHeader();
/* 450 */       header.headKey = node.nextKey;
/*     */     } 
/*     */     
/* 453 */     boolean updatedNext = false;
/* 454 */     if (node.nextKey != null) {
/*     */       
/* 456 */       Node nextNode = getNode(node.nextKey);
/* 457 */       if (nextNode != null) {
/* 458 */         nextNode.previousKey = node.previousKey;
/* 459 */         updateNode(nextNode);
/* 460 */         updatedNext = true;
/*     */       } 
/*     */     } 
/* 463 */     if (!updatedNext) {
/*     */       
/* 465 */       header = getHeader();
/* 466 */       header.tailKey = node.previousKey;
/*     */     } 
/* 468 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Header getHeader() throws IOException, JMSException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void updateHeader(Header paramHeader) throws IOException, JMSException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void updateNode(Node paramNode) throws IOException, JMSException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Node getNode(Long paramLong) throws IOException, JMSException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Node getNode(int index) throws IOException, JMSException {
/* 498 */     Header header = getHeader();
/* 499 */     return getNode(header, index);
/*     */   }
/*     */   
/*     */   protected Node getNode(Header header, int index) throws IOException, JMSException {
/* 503 */     Node node = null;
/* 504 */     int middle = header.size / 2;
/* 505 */     if (index > middle) {
/*     */       
/* 507 */       Long key = header.tailKey;
/* 508 */       for (int i = header.size; i > index && key != null; i--) {
/* 509 */         node = getNode(key);
/* 510 */         if (node != null) {
/* 511 */           key = node.previousKey;
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       
/* 516 */       Long key = header.headKey;
/* 517 */       for (int i = 0; i <= index && key != null; i++) {
/* 518 */         node = getNode(key);
/* 519 */         if (node != null) {
/* 520 */           key = node.nextKey;
/*     */         }
/*     */       } 
/*     */     } 
/* 524 */     return node;
/*     */   }
/*     */   
/*     */   protected Node doAddLast(Object value, Header header) throws IOException, JMSException {
/* 528 */     Node node = createNode();
/* 529 */     Long key = createKey(header);
/* 530 */     node.key = key;
/* 531 */     node.value = value;
/* 532 */     Long previousKey = header.tailKey;
/* 533 */     node.previousKey = previousKey;
/* 534 */     updateNode(node);
/* 535 */     updatePreviousNode(previousKey, key);
/* 536 */     header.tailKey = key;
/* 537 */     if (header.headKey == null) {
/* 538 */       header.headKey = key;
/*     */     }
/* 540 */     header.size++;
/* 541 */     updateHeader(header);
/* 542 */     return node;
/*     */   }
/*     */   
/*     */   protected void updateNextNode(Long nextKey, Long key) throws IOException, JMSException {
/* 546 */     if (nextKey != null) {
/* 547 */       Node nextNode = getNode(nextKey);
/* 548 */       if (nextNode == null) {
/* 549 */         throw new IOException("Missing node for key: " + nextKey);
/*     */       }
/* 551 */       nextNode.previousKey = key;
/* 552 */       updateNode(nextNode);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void updatePreviousNode(Long previousKey, Long key) throws IOException, JMSException {
/* 557 */     if (previousKey != null) {
/* 558 */       Node previousNode = getNode(previousKey);
/* 559 */       if (previousNode == null) {
/* 560 */         throw new IOException("Missing previous node for key: " + previousKey);
/*     */       }
/* 562 */       previousNode.nextKey = key;
/* 563 */       updateNode(previousNode);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Node doAddBefore(Header header, Node nextNode, Object element) throws JMSException, IOException {
/* 568 */     if (nextNode == null) {
/* 569 */       return doAddLast(element, header);
/*     */     }
/*     */ 
/*     */     
/* 573 */     Long key = createKey(header);
/* 574 */     Node node = createNode();
/* 575 */     node.value = element;
/* 576 */     node.key = key;
/* 577 */     Long previousKey = nextNode.previousKey;
/* 578 */     node.previousKey = previousKey;
/* 579 */     node.nextKey = nextNode.key;
/* 580 */     nextNode.previousKey = key;
/* 581 */     header.size++;
/*     */     
/* 583 */     updateNode(node);
/* 584 */     updateNode(nextNode);
/* 585 */     updatePreviousNode(previousKey, key);
/* 586 */     updateHeader(header);
/* 587 */     return node;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void doRemoveNode(Node paramNode) throws IOException, JMSException;
/*     */ 
/*     */   
/*     */   protected static Long wrapLong(long value) {
/* 595 */     if (value == 0L) {
/* 596 */       return null;
/*     */     }
/*     */     
/* 599 */     return new Long(value);
/*     */   }
/*     */   
/*     */   protected static long unwrapLong(Long key) {
/* 603 */     if (key != null) {
/* 604 */       return key.longValue();
/*     */     }
/*     */     
/* 607 */     return 0L;
/*     */   }
/*     */   
/*     */   protected Node createNode() {
/* 611 */     return new Node();
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\QueueListSupport.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */