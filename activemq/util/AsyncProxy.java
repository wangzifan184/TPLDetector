/*    */ package org.codehaus.activemq.util;
/*    */ 
/*    */ import EDU.oswego.cs.dl.util.concurrent.Executor;
/*    */ import java.lang.reflect.InvocationHandler;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Proxy;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AsyncProxy
/*    */   implements InvocationHandler
/*    */ {
/*    */   private Object realObject;
/*    */   private Executor executor;
/*    */   private Log log;
/*    */   
/*    */   public static Object createProxy(Class interfaceType, Object realObject, Executor executor) {
/* 43 */     return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] { interfaceType }, new AsyncProxy(realObject, executor));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public AsyncProxy(Object realObject, Executor executor) {
/* 49 */     this(realObject, executor, LogFactory.getLog(AsyncProxy.class));
/*    */   }
/*    */   
/*    */   public AsyncProxy(Object realObject, Executor executor, Log log) {
/* 53 */     this.realObject = realObject;
/* 54 */     this.executor = executor;
/* 55 */     this.log = log;
/*    */   }
/*    */   
/*    */   public AsyncProxy(Executor executor, Log log) {
/* 59 */     this.executor = executor;
/* 60 */     this.log = log;
/*    */   }
/*    */   
/*    */   public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
/* 64 */     if (method.getReturnType() == void.class) {
/* 65 */       this.executor.execute(new Runnable() { private final Method val$method;
/*    */             public void run() {
/* 67 */               AsyncProxy.this.doAsyncMethodInvoke(method, args);
/*    */             } private final Object[] val$args; private final AsyncProxy this$0; }
/*    */         );
/* 70 */       return null;
/*    */     } 
/*    */     
/* 73 */     return method.invoke(this.realObject, args);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doAsyncMethodInvoke(Method method, Object[] args) {
/*    */     try {
/* 79 */       method.invoke(this.realObject, args);
/*    */     }
/* 81 */     catch (Throwable e) {
/* 82 */       this.log.warn("Caught exception: " + e, e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activem\\util\AsyncProxy.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */