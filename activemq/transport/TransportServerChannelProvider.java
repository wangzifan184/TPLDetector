/*    */ package org.codehaus.activemq.transport;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
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
/*    */ 
/*    */ public class TransportServerChannelProvider
/*    */ {
/* 35 */   private static FactoryFinder finder = new FactoryFinder("META-INF/services/org/codehaus/activemq/transport/server/");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static TransportServerChannel create(WireFormat wireFormat, URI bindAddress) throws JMSException {
/* 45 */     return getFactory(bindAddress.getScheme()).create(wireFormat, bindAddress);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static TransportServerChannel newInstance(WireFormat wireFormat, String bindAddress) throws JMSException, URISyntaxException {
/* 56 */     return create(wireFormat, new URI(bindAddress));
/*    */   }
/*    */   
/*    */   protected static TransportServerChannelFactory getFactory(String protocol) throws JMSException {
/*    */     try {
/* 61 */       Object value = finder.newInstance(protocol);
/* 62 */       if (value instanceof TransportServerChannelFactory) {
/* 63 */         return (TransportServerChannelFactory)value;
/*    */       }
/*    */       
/* 66 */       throw new JMSException("Factory does not implement TransportServerChannelFactory: " + value);
/*    */     
/*    */     }
/* 69 */     catch (IllegalAccessException e) {
/* 70 */       throw createJMSexception(protocol, e);
/*    */     }
/* 72 */     catch (InstantiationException e) {
/* 73 */       throw createJMSexception(protocol, e);
/*    */     }
/* 75 */     catch (IOException e) {
/* 76 */       throw createJMSexception(protocol, e);
/*    */     }
/* 78 */     catch (ClassNotFoundException e) {
/* 79 */       throw createJMSexception(protocol, e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected static JMSException createJMSexception(String protocol, Exception e) {
/* 85 */     JMSException answer = new JMSException("Could not load protocol: " + protocol + ". Reason: " + e);
/* 86 */     answer.setLinkedException(e);
/* 87 */     return answer;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\TransportServerChannelProvider.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */