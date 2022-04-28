/*    */ package org.codehaus.activemq.transport;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.EventObject;
/*    */ import java.util.Map;
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
/*    */ public class DiscoveryEvent
/*    */   extends EventObject
/*    */ {
/*    */   private String serviceName;
/*    */   private Map serviceDetails;
/*    */   
/*    */   public DiscoveryEvent(DiscoveryAgent source, String serviceName) {
/* 34 */     this(source, serviceName, Collections.EMPTY_MAP);
/*    */   }
/*    */   
/*    */   public DiscoveryEvent(DiscoveryAgent source, String serviceName, Map serviceDetails) {
/* 38 */     super(source);
/* 39 */     this.serviceName = serviceName;
/* 40 */     this.serviceDetails = serviceDetails;
/*    */   }
/*    */   
/*    */   public String getServiceName() {
/* 44 */     return this.serviceName;
/*    */   }
/*    */   
/*    */   public Map getServiceDetails() {
/* 48 */     return this.serviceDetails;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\DiscoveryEvent.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */