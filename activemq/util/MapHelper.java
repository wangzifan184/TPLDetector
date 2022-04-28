/*    */ package org.codehaus.activemq.util;
/*    */ 
/*    */ import java.util.Map;
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
/*    */ public class MapHelper
/*    */ {
/*    */   public static String getString(Map map, String key) {
/* 32 */     Object answer = map.get(key);
/* 33 */     return (answer != null) ? answer.toString() : null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int getInt(Map map, String key, int defaultValue) {
/* 41 */     Object value = map.get(key);
/* 42 */     if (value instanceof Number) {
/* 43 */       return ((Number)value).intValue();
/*    */     }
/* 45 */     if (value instanceof String) {
/* 46 */       return Integer.parseInt((String)value);
/*    */     }
/* 48 */     return defaultValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activem\\util\MapHelper.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */