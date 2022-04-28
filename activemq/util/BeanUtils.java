/*    */ package org.codehaus.activemq.util;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Map;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
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
/*    */ public class BeanUtils
/*    */ {
/* 32 */   private static final Log log = LogFactory.getLog(BeanUtils.class);
/*    */   
/*    */   protected static final String NAME = "org.apache.commons.beanutils.BeanUtils";
/* 35 */   protected static final Class[] PARAMETER_TYPES = new Class[] { Object.class, Map.class };
/*    */ 
/*    */   
/*    */   public static void populate(Object bean, Map properties) {
/* 39 */     Class beanUtils = null;
/*    */     try {
/* 41 */       beanUtils = Thread.currentThread().getContextClassLoader().loadClass("org.apache.commons.beanutils.BeanUtils");
/*    */     }
/* 43 */     catch (ClassNotFoundException e) {
/*    */       try {
/* 45 */         beanUtils = BeanUtils.class.getClassLoader().loadClass("org.apache.commons.beanutils.BeanUtils");
/*    */       }
/* 47 */       catch (ClassNotFoundException e1) {
/* 48 */         log.trace("Cannot find: org.apache.commons.beanutils.BeanUtils. Reason: " + e, e);
/*    */       } 
/*    */     } 
/* 51 */     if (beanUtils == null) {
/* 52 */       log.warn("Could not find class: org.apache.commons.beanutils.BeanUtils so cannot configure bean using introspection: " + bean);
/*    */     } else {
/*    */       
/* 55 */       Method method = null;
/*    */       try {
/* 57 */         method = beanUtils.getMethod("populate", PARAMETER_TYPES);
/*    */       }
/* 59 */       catch (NoSuchMethodException e) {
/* 60 */         log.trace("Could not find populate", e);
/*    */       } 
/* 62 */       if (method == null) {
/* 63 */         log.warn("Could not find populate() method so cannot configure bean using introspection: " + bean);
/*    */       } else {
/*    */         
/*    */         try {
/* 67 */           method.invoke(null, new Object[] { bean, properties });
/*    */         }
/* 69 */         catch (Exception e) {
/* 70 */           log.warn("Could not populate the bean via introspection: " + bean + ". Reason: " + e, e);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activem\\util\BeanUtils.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */