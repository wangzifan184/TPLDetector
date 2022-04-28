/*    */ package org.codehaus.activemq.transport;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import javax.jms.JMSException;
/*    */ import org.codehaus.activemq.message.WireFormat;
/*    */ import org.codehaus.activemq.util.FactoryFinder;
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
/*    */ public class TransportChannelProvider
/*    */ {
/* 33 */   private static FactoryFinder finder = new FactoryFinder("META-INF/services/org/codehaus/activemq/transport/");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static TransportChannel create(WireFormat wireFormat, URI remoteLocation) throws JMSException {
/* 43 */     return getFactory(remoteLocation).create(wireFormat, remoteLocation);
/*    */   }
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
/*    */   public static TransportChannel create(WireFormat wireFormat, URI remoteLocation, URI localLocation) throws JMSException {
/* 56 */     return getFactory(remoteLocation).create(wireFormat, remoteLocation, localLocation);
/*    */   }
/*    */   
/*    */   public static TransportChannelFactory getFactory(URI remoteLocation) throws JMSException {
/* 60 */     String protocol = remoteLocation.getScheme();
/*    */     try {
/* 62 */       Object value = finder.newInstance(protocol);
/* 63 */       if (value instanceof TransportChannelFactory) {
/* 64 */         return (TransportChannelFactory)value;
/*    */       }
/*    */       
/* 67 */       throw new JMSException("Factory does not implement TransportChannelFactory: " + value);
/*    */     
/*    */     }
/* 70 */     catch (IllegalAccessException e) {
/* 71 */       throw createJMSexception(protocol, e);
/*    */     }
/* 73 */     catch (InstantiationException e) {
/* 74 */       throw createJMSexception(protocol, e);
/*    */     }
/* 76 */     catch (IOException e) {
/* 77 */       throw createJMSexception(protocol, e);
/*    */     }
/* 79 */     catch (ClassNotFoundException e) {
/* 80 */       throw createJMSexception(protocol, e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected static JMSException createJMSexception(String protocol, Exception e) {
/* 86 */     JMSException answer = new JMSException("Could not load protocol: " + protocol + ". Reason: " + e);
/* 87 */     answer.setLinkedException(e);
/* 88 */     return answer;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\TransportChannelProvider.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */