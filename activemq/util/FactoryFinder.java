/*     */ package org.codehaus.activemq.util;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class FactoryFinder
/*     */ {
/*     */   private String path;
/*  35 */   private Map classes = new HashMap();
/*     */   
/*     */   public FactoryFinder(String path) {
/*  38 */     this.path = path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object newInstance(String key) throws IllegalAccessException, InstantiationException, IOException, ClassNotFoundException {
/*  48 */     Class type = findClass(key);
/*  49 */     return type.newInstance();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class findClass(String key) throws IOException, ClassNotFoundException {
/*  59 */     Class answer = (Class)this.classes.get(key);
/*  60 */     if (answer == null) {
/*  61 */       answer = doFindClass(key);
/*  62 */       this.classes.put(key, answer);
/*     */     } 
/*  64 */     return answer;
/*     */   }
/*     */ 
/*     */   
/*     */   private Class doFindClass(String key) throws IOException, ClassNotFoundException {
/*  69 */     String uri = this.path + key;
/*     */ 
/*     */     
/*  72 */     InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(uri);
/*  73 */     if (in == null) {
/*  74 */       in = getClass().getClassLoader().getResourceAsStream(uri);
/*  75 */       if (in == null) {
/*  76 */         throw new IOException("Could not find class for resource: " + uri);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  81 */     BufferedReader reader = null;
/*     */     try {
/*  83 */       reader = new BufferedReader(new InputStreamReader(in));
/*  84 */       String line = reader.readLine();
/*  85 */       if (line == null) {
/*  86 */         throw new IOException("Empty file found for: " + uri);
/*     */       }
/*  88 */       line = line.trim();
/*  89 */       Class answer = loadClass(line);
/*  90 */       if (answer == null) {
/*  91 */         throw new ClassNotFoundException("Could not find class: " + line);
/*     */       }
/*  93 */       return answer;
/*     */     } finally {
/*     */       
/*     */       try {
/*  97 */         reader.close();
/*     */       }
/*  99 */       catch (Exception e) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class loadClass(String name) throws ClassNotFoundException {
/*     */     try {
/* 107 */       return Thread.currentThread().getContextClassLoader().loadClass(name);
/*     */     }
/* 109 */     catch (ClassNotFoundException e) {
/* 110 */       return getClass().getClassLoader().loadClass(name);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activem\\util\FactoryFinder.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */