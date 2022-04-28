/*     */ package org.codehaus.activemq.jndi;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Properties;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.RefAddr;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.StringRefAddr;
/*     */ import javax.naming.spi.ObjectFactory;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
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
/*     */ public class JNDIReferenceFactory
/*     */   implements ObjectFactory
/*     */ {
/*  40 */   static Log log = LogFactory.getLog(JNDIReferenceFactory.class);
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
/*     */   public Object getObjectInstance(Object object, Name name, Context nameCtx, Hashtable environment) throws Exception {
/*  55 */     Object result = null;
/*  56 */     if (object instanceof Reference) {
/*  57 */       Reference reference = (Reference)object;
/*     */       
/*  59 */       if (log.isTraceEnabled()) {
/*  60 */         log.trace("Getting instance of " + reference.getClassName());
/*     */       }
/*     */       
/*  63 */       Class theClass = loadClass(this, reference.getClassName());
/*  64 */       if (JNDIStorableInterface.class.isAssignableFrom(theClass)) {
/*     */         
/*  66 */         JNDIStorableInterface store = (JNDIStorableInterface)theClass.newInstance();
/*  67 */         Properties properties = new Properties();
/*  68 */         for (Enumeration iter = reference.getAll(); iter.hasMoreElements(); ) {
/*     */           
/*  70 */           StringRefAddr addr = (StringRefAddr)iter.nextElement();
/*  71 */           properties.put(addr.getType(), (addr.getContent() == null) ? "" : addr.getContent());
/*     */         } 
/*     */         
/*  74 */         store.setProperties(properties);
/*  75 */         result = store;
/*     */       } 
/*     */     } else {
/*     */       
/*  79 */       log.error("Object " + object + " is not a reference - cannot load");
/*  80 */       throw new RuntimeException("Object " + object + " is not a reference");
/*     */     } 
/*  82 */     return result;
/*     */   }
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
/*     */   public static Reference createReference(String instanceClassName, JNDIStorableInterface po) throws NamingException {
/*  95 */     if (log.isTraceEnabled()) {
/*  96 */       log.trace("Creating reference: " + instanceClassName + "," + po);
/*     */     }
/*  98 */     Reference result = new Reference(instanceClassName, JNDIReferenceFactory.class.getName(), null);
/*     */     try {
/* 100 */       Properties props = po.getProperties();
/* 101 */       for (Enumeration iter = props.propertyNames(); iter.hasMoreElements(); ) {
/* 102 */         String key = (String)iter.nextElement();
/* 103 */         String value = props.getProperty(key);
/* 104 */         StringRefAddr addr = new StringRefAddr(key, value);
/* 105 */         result.add(addr);
/*     */       }
/*     */     
/* 108 */     } catch (Exception e) {
/* 109 */       log.error(e.getMessage(), e);
/* 110 */       throw new NamingException(e.getMessage());
/*     */     } 
/* 112 */     return result;
/*     */   }
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
/*     */   public static Class loadClass(Object thisObj, String className) throws ClassNotFoundException {
/*     */     Class theClass;
/* 126 */     ClassLoader loader = thisObj.getClass().getClassLoader();
/*     */     
/* 128 */     if (loader != null) {
/* 129 */       theClass = loader.loadClass(className);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 134 */       theClass = Class.forName(className);
/*     */     } 
/* 136 */     return theClass;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\jndi\JNDIReferenceFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */