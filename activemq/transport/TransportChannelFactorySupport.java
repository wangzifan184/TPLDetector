/*    */ package org.codehaus.activemq.transport;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.util.Map;
/*    */ import org.codehaus.activemq.util.BeanUtils;
/*    */ import org.codehaus.activemq.util.URIHelper;
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
/*    */ public abstract class TransportChannelFactorySupport
/*    */   implements TransportChannelFactory
/*    */ {
/*    */   protected TransportChannel populateProperties(TransportChannel channel, URI uri) {
/* 36 */     Map properties = URIHelper.parseQuery(uri);
/* 37 */     if (!properties.isEmpty()) {
/* 38 */       BeanUtils.populate(channel, properties);
/*    */     }
/* 40 */     return channel;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\TransportChannelFactorySupport.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */