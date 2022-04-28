/*     */ package org.codehaus.activemq.filter;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.codehaus.activemq.message.ActiveMQDestination;
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
/*     */ public class DestinationMap
/*     */ {
/*  42 */   private DestinationMapNode rootNode = new DestinationMapNode();
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final String ANY_DESCENDENT = ">";
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final String ANY_CHILD = "*";
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Set get(ActiveMQDestination key) {
/*  55 */     if (key.isComposite()) {
/*  56 */       List childDestinations = key.getChildDestinations();
/*  57 */       Set answer = new HashSet(childDestinations.size());
/*  58 */       Iterator iter = childDestinations.iterator(); if (iter.hasNext()) {
/*  59 */         ActiveMQDestination childDestination = iter.next();
/*  60 */         Object value = get(childDestination);
/*  61 */         if (value instanceof Set) {
/*  62 */           answer.addAll((Set)value);
/*     */         }
/*  64 */         else if (value != null) {
/*  65 */           answer.add(value);
/*     */         } 
/*  67 */         return answer;
/*     */       } 
/*     */     } 
/*  70 */     return findWildcardMatches(key);
/*     */   }
/*     */   
/*     */   public synchronized void put(ActiveMQDestination key, Object value) {
/*  74 */     if (key.isComposite()) {
/*  75 */       List childDestinations = key.getChildDestinations();
/*  76 */       for (Iterator iter = childDestinations.iterator(); iter.hasNext(); ) {
/*  77 */         ActiveMQDestination childDestination = iter.next();
/*  78 */         put(childDestination, value);
/*     */       } 
/*     */       return;
/*     */     } 
/*  82 */     String[] paths = key.getDestinationPaths();
/*  83 */     this.rootNode.add(paths, 0, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void remove(ActiveMQDestination key, Object value) {
/*  90 */     if (key.isComposite()) {
/*  91 */       List childDestinations = key.getChildDestinations();
/*  92 */       for (Iterator iter = childDestinations.iterator(); iter.hasNext(); ) {
/*  93 */         ActiveMQDestination childDestination = iter.next();
/*  94 */         remove(childDestination, value);
/*     */       } 
/*     */       return;
/*     */     } 
/*  98 */     String[] paths = key.getDestinationPaths();
/*  99 */     this.rootNode.remove(paths, 0, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Set findWildcardMatches(ActiveMQDestination key) {
/* 106 */     String[] paths = key.getDestinationPaths();
/* 107 */     Set answer = new HashSet();
/* 108 */     this.rootNode.appendMatchingValues(answer, paths, 0);
/* 109 */     return answer;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\filter\DestinationMap.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */