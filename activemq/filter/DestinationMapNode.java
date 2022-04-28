/*     */ package org.codehaus.activemq.filter;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public class DestinationMapNode
/*     */ {
/*  34 */   private List values = new ArrayList();
/*  35 */   private Map childNodes = new HashMap();
/*     */   
/*     */   private DestinationMapNode anyChild;
/*     */   
/*     */   protected static final String ANY_CHILD = "*";
/*     */   
/*     */   protected static final String ANY_DESCENDENT = ">";
/*     */ 
/*     */   
/*     */   public DestinationMapNode getChild(String path) {
/*  45 */     return (DestinationMapNode)this.childNodes.get(path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DestinationMapNode getChildOrCreate(String path) {
/*  53 */     DestinationMapNode answer = (DestinationMapNode)this.childNodes.get(path);
/*  54 */     if (answer == null) {
/*  55 */       answer = createChildNode();
/*  56 */       this.childNodes.put(path, answer);
/*     */     } 
/*  58 */     return answer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DestinationMapNode getAnyChildNode() {
/*  65 */     if (this.anyChild == null) {
/*  66 */       this.anyChild = createChildNode();
/*     */     }
/*  68 */     return this.anyChild;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getValues() {
/*  75 */     return this.values;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set getDesendentValues() {
/*  82 */     Set answer = new HashSet();
/*  83 */     appendDescendantValues(answer);
/*  84 */     return answer;
/*     */   }
/*     */   
/*     */   public void add(String[] paths, int idx, Object value) {
/*  88 */     if (idx >= paths.length) {
/*  89 */       this.values.add(value);
/*     */     } else {
/*     */       
/*  92 */       if (idx == paths.length - 1) {
/*  93 */         getAnyChildNode().getValues().add(value);
/*     */       } else {
/*     */         
/*  96 */         getAnyChildNode().add(paths, idx + 1, value);
/*     */       } 
/*  98 */       getChildOrCreate(paths[idx]).add(paths, idx + 1, value);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void remove(String[] paths, int idx, Object value) {
/* 103 */     if (idx >= paths.length) {
/* 104 */       this.values.remove(value);
/*     */     } else {
/*     */       
/* 107 */       if (idx == paths.length - 1) {
/* 108 */         getAnyChildNode().getValues().remove(value);
/*     */       } else {
/*     */         
/* 111 */         getAnyChildNode().remove(paths, idx + 1, value);
/*     */       } 
/* 113 */       getChildOrCreate(paths[idx]).remove(paths, ++idx, value);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void appendDescendantValues(Set answer) {
/* 118 */     answer.addAll(this.values);
/* 119 */     if (this.anyChild != null) {
/* 120 */       this.anyChild.appendDescendantValues(answer);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DestinationMapNode createChildNode() {
/* 128 */     return new DestinationMapNode();
/*     */   }
/*     */   
/*     */   public void appendMatchingWildcards(Set answer, String[] paths, int idx) {
/* 132 */     DestinationMapNode wildCardNode = getChild("*");
/* 133 */     if (wildCardNode != null) {
/* 134 */       wildCardNode.appendMatchingValues(answer, paths, idx + 1);
/*     */     }
/* 136 */     wildCardNode = getChild(">");
/* 137 */     if (wildCardNode != null) {
/* 138 */       answer.addAll(wildCardNode.getDesendentValues());
/*     */     }
/*     */   }
/*     */   
/*     */   public void appendMatchingValues(Set answer, String[] paths, int startIndex) {
/* 143 */     DestinationMapNode node = this;
/* 144 */     for (int i = startIndex, size = paths.length; i < size && node != null; i++) {
/* 145 */       String path = paths[i];
/* 146 */       if (path.equals(">")) {
/* 147 */         answer.addAll(node.getDesendentValues());
/*     */         
/*     */         break;
/*     */       } 
/* 151 */       node.appendMatchingWildcards(answer, paths, i);
/* 152 */       if (path.equals("*")) {
/* 153 */         node = node.getAnyChildNode();
/*     */       } else {
/*     */         
/* 156 */         node = node.getChild(path);
/*     */       } 
/*     */     } 
/* 159 */     if (node != null)
/* 160 */       answer.addAll(node.getValues()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\filter\DestinationMapNode.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */