/*    */ package org.codehaus.activemq.transport.reliable;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import javax.jms.JMSException;
/*    */ import org.codehaus.activemq.message.WireFormat;
/*    */ import org.codehaus.activemq.transport.TransportChannel;
/*    */ import org.codehaus.activemq.transport.composite.CompositeTransportChannelFactory;
/*    */ import org.codehaus.activemq.util.JMSExceptionHelper;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ReliableTransportChannelFactory
/*    */   extends CompositeTransportChannelFactory
/*    */ {
/*    */   public TransportChannel create(WireFormat wireFormat, URI remoteLocation) throws JMSException {
/*    */     try {
/* 51 */       return (TransportChannel)new ReliableTransportChannel(wireFormat, parseURIs(remoteLocation));
/*    */     }
/* 53 */     catch (URISyntaxException e) {
/* 54 */       throw JMSExceptionHelper.newJMSException("Can't parse list of URIs for: " + remoteLocation + ". Reason: " + e, e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected List randomizeURIs(List uris) {
/* 64 */     if (!uris.isEmpty()) {
/* 65 */       int size = uris.size();
/* 66 */       Object[] result = new Object[size];
/* 67 */       SMLCGRandom random = new SMLCGRandom();
/* 68 */       int startIndex = (int)(random.nextDouble() * (size + 1));
/* 69 */       int count = 0; int i;
/* 70 */       for (i = startIndex; i < size; i++) {
/* 71 */         result[count++] = uris.get(i);
/*    */       }
/* 73 */       for (i = 0; i < startIndex; i++) {
/* 74 */         result[count++] = uris.get(i);
/*    */       }
/* 76 */       return new ArrayList(Arrays.asList(result));
/*    */     } 
/* 78 */     return uris;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\reliable\ReliableTransportChannelFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */