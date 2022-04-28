/*    */ package org.codehaus.activemq.broker.impl;
/*    */ 
/*    */ import javax.jms.JMSException;
/*    */ import org.codehaus.activemq.broker.BrokerContainer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Main
/*    */ {
/*    */   public static void main(String[] args) {
/*    */     try {
/* 39 */       String url = "tcp://localhost:61616";
/* 40 */       if (args.length > 0) {
/* 41 */         url = args[0];
/*    */       }
/*    */       
/* 44 */       BrokerContainer container = new BrokerContainerImpl();
/* 45 */       container.addConnector(url);
/*    */       
/* 47 */       if (args.length > 1) {
/* 48 */         container.addNetworkConnector(args[1]);
/*    */       }
/*    */       
/* 51 */       container.start();
/*    */ 
/*    */       
/* 54 */       Object lock = new Object();
/* 55 */       synchronized (lock) {
/* 56 */         lock.wait();
/*    */       }
/*    */     
/* 59 */     } catch (JMSException e) {
/* 60 */       System.out.println("Caught: " + e);
/* 61 */       e.printStackTrace();
/* 62 */       Exception le = e.getLinkedException();
/* 63 */       System.out.println("Reason: " + le);
/* 64 */       if (le != null) {
/* 65 */         le.printStackTrace();
/*    */       }
/*    */     }
/* 68 */     catch (Exception e) {
/* 69 */       System.out.println("Caught: " + e);
/* 70 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\broker\impl\Main.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */