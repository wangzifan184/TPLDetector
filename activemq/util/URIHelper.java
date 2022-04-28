/*    */ package org.codehaus.activemq.util;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.StringTokenizer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class URIHelper
/*    */ {
/*    */   public static Map parseQuery(URI uri) {
/* 36 */     return parseQuery(uri.getQuery());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Map parseQuery(String query) {
/* 43 */     Map answer = new HashMap();
/* 44 */     if (query != null) {
/* 45 */       StringTokenizer iter = new StringTokenizer(query, "&");
/* 46 */       while (iter.hasMoreTokens()) {
/* 47 */         String pair = iter.nextToken();
/* 48 */         addKeyValuePair(answer, pair);
/*    */       } 
/*    */     } 
/* 51 */     return answer;
/*    */   }
/*    */   
/*    */   protected static void addKeyValuePair(Map answer, String pair) {
/* 55 */     int idx = pair.indexOf('=');
/* 56 */     String name = null;
/* 57 */     String value = null;
/* 58 */     if (idx >= 0) {
/* 59 */       name = pair.substring(0, idx);
/* 60 */       if (++idx < pair.length()) {
/* 61 */         value = pair.substring(idx);
/*    */       }
/*    */     } else {
/*    */       
/* 65 */       name = pair;
/*    */     } 
/* 67 */     answer.put(name, value);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activem\\util\URIHelper.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */