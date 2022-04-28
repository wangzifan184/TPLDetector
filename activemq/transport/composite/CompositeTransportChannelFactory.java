/*    */ package org.codehaus.activemq.transport.composite;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.StringTokenizer;
/*    */ import javax.jms.JMSException;
/*    */ import org.codehaus.activemq.message.WireFormat;
/*    */ import org.codehaus.activemq.transport.TransportChannel;
/*    */ import org.codehaus.activemq.transport.TransportChannelFactory;
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
/*    */ public class CompositeTransportChannelFactory
/*    */   implements TransportChannelFactory
/*    */ {
/* 38 */   private String separator = ",";
/*    */   
/*    */   public TransportChannel create(WireFormat wireFormat, URI remoteLocation) throws JMSException {
/*    */     try {
/* 42 */       return (TransportChannel)new CompositeTransportChannel(wireFormat, parseURIs(remoteLocation));
/*    */     }
/* 44 */     catch (URISyntaxException e) {
/* 45 */       throw JMSExceptionHelper.newJMSException("Can't parse list of URIs for: " + remoteLocation + ". Reason: " + e, e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public TransportChannel create(WireFormat wireFormat, URI remoteLocation, URI localLocation) throws JMSException {
/* 50 */     return create(wireFormat, remoteLocation);
/*    */   }
/*    */   
/*    */   public boolean requiresEmbeddedBroker() {
/* 54 */     return false;
/*    */   }
/*    */   
/*    */   protected List parseURIs(URI uri) throws URISyntaxException {
/* 58 */     String text = uri.getSchemeSpecificPart();
/* 59 */     List uris = new ArrayList();
/* 60 */     StringTokenizer iter = new StringTokenizer(text, this.separator);
/* 61 */     while (iter.hasMoreTokens()) {
/* 62 */       String child = stripLeadingSlashes(iter.nextToken().trim());
/* 63 */       uris.add(new URI(child));
/*    */     } 
/* 65 */     return randomizeURIs(uris);
/*    */   }
/*    */   
/*    */   protected String stripLeadingSlashes(String text) {
/* 69 */     int idx = 0;
/* 70 */     while (text.charAt(idx) == '/') {
/* 71 */       idx++;
/*    */     }
/* 73 */     if (idx > 0) {
/* 74 */       return text.substring(idx);
/*    */     }
/* 76 */     return text;
/*    */   }
/*    */ 
/*    */   
/*    */   protected List randomizeURIs(List uris) {
/* 81 */     return uris;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\composite\CompositeTransportChannelFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */