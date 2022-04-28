/*    */ package org.codehaus.activemq.ra;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import javax.jms.ConnectionConsumer;
/*    */ import javax.jms.JMSException;
/*    */ import javax.jms.Message;
/*    */ import javax.jms.MessageListener;
/*    */ import javax.jms.Session;
/*    */ import javax.resource.ResourceException;
/*    */ import javax.resource.spi.endpoint.MessageEndpointFactory;
/*    */ import javax.resource.spi.work.WorkException;
/*    */ import javax.resource.spi.work.WorkManager;
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
/*    */ public abstract class ActiveMQBaseEndpointWorker
/*    */ {
/* 40 */   private static final Log log = LogFactory.getLog(ActiveMQBaseEndpointWorker.class);
/*    */   public static final Method ON_MESSAGE_METHOD;
/*    */   
/*    */   static {
/*    */     try {
/* 45 */       ON_MESSAGE_METHOD = MessageListener.class.getMethod("onMessage", new Class[] { Message.class });
/* 46 */     } catch (Exception e) {
/* 47 */       throw new ExceptionInInitializerError(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected ActiveMQResourceAdapter adapter;
/*    */   
/*    */   protected ActiveMQEndpointActivationKey endpointActivationKey;
/*    */   
/*    */   protected MessageEndpointFactory endpointFactory;
/*    */   protected WorkManager workManager;
/*    */   protected boolean transacted;
/*    */   
/*    */   public static void safeClose(Session s) {
/*    */     try {
/* 62 */       if (s != null) {
/* 63 */         s.close();
/*    */       }
/* 65 */     } catch (JMSException e) {}
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void safeClose(ConnectionConsumer cc) {
/*    */     try {
/* 74 */       if (cc != null) {
/* 75 */         cc.close();
/*    */       }
/* 77 */     } catch (JMSException e) {}
/*    */   }
/*    */ 
/*    */   
/*    */   public ActiveMQBaseEndpointWorker(ActiveMQResourceAdapter adapter, ActiveMQEndpointActivationKey key) throws ResourceException {
/* 82 */     this.endpointActivationKey = key;
/* 83 */     this.adapter = adapter;
/* 84 */     this.endpointFactory = this.endpointActivationKey.getMessageEndpointFactory();
/* 85 */     this.workManager = adapter.getBootstrapContext().getWorkManager();
/*    */     try {
/* 87 */       this.transacted = this.endpointFactory.isDeliveryTransacted(ON_MESSAGE_METHOD);
/* 88 */     } catch (NoSuchMethodException e) {
/* 89 */       throw new ResourceException("Endpoint does not implement the onMessage method.");
/*    */     } 
/*    */   }
/*    */   
/*    */   public abstract void start() throws WorkException, ResourceException;
/*    */   
/*    */   public abstract void stop() throws InterruptedException;
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ra\ActiveMQBaseEndpointWorker.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */