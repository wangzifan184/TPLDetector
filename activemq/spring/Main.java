/*    */ package org.codehaus.activemq.spring;
/*    */ 
/*    */ import javax.jms.JMSException;
/*    */ import org.codehaus.activemq.broker.BrokerContainer;
/*    */ import org.codehaus.activemq.broker.BrokerContext;
/*    */ import org.springframework.core.io.ClassPathResource;
/*    */ import org.springframework.core.io.FileSystemResource;
/*    */ import org.springframework.core.io.Resource;
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
/* 42 */       String version = "";
/* 43 */       Package p = Package.getPackage("org.codehaus.activemq");
/* 44 */       if (p != null) {
/* 45 */         version = ": " + p.getImplementationVersion();
/*    */       }
/* 47 */       System.out.println("ActiveMQ Message Broker" + version);
/* 48 */       System.out.println();
/*    */       
/* 50 */       SpringBrokerContainerFactory factory = new SpringBrokerContainerFactory();
/* 51 */       String file = null;
/* 52 */       if (args.length <= 0) {
/* 53 */         System.out.println("Loading Mesaage Broker from activemq.xml on the CLASSPATH");
/* 54 */         factory.setResource((Resource)new ClassPathResource("activemq.xml"));
/*    */       } else {
/*    */         
/* 57 */         file = args[0];
/*    */         
/* 59 */         if (file.equals("-?") || file.equals("?") || file.equals("--help") || file.equals("-h")) {
/* 60 */           System.out.println("Usage: Main [xmlConfigFile]");
/* 61 */           System.out.println("If an XML config file is not specified then activemq.xml is used from the CLASSPAHT");
/*    */           
/*    */           return;
/*    */         } 
/* 65 */         System.out.println("Loading Message Broker from file: " + file);
/* 66 */         factory.setResource((Resource)new FileSystemResource(file));
/*    */       } 
/*    */ 
/*    */       
/* 70 */       BrokerContainer container = factory.createBrokerContainer("DefaultBroker", BrokerContext.getInstance());
/* 71 */       container.start();
/*    */ 
/*    */       
/* 74 */       Object lock = new Object();
/* 75 */       synchronized (lock) {
/* 76 */         lock.wait();
/*    */       }
/*    */     
/* 79 */     } catch (JMSException e) {
/* 80 */       System.out.println("Caught: " + e);
/* 81 */       e.printStackTrace();
/* 82 */       Exception le = e.getLinkedException();
/* 83 */       System.out.println("Reason: " + le);
/* 84 */       if (le != null) {
/* 85 */         le.printStackTrace();
/*    */       }
/*    */     }
/* 88 */     catch (Exception e) {
/* 89 */       System.out.println("Caught: " + e);
/* 90 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\spring\Main.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */