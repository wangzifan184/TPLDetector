/*    */ package org.codehaus.activemq.transport;
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
/*    */ public abstract class DiscoveryAgentSupport
/*    */   implements DiscoveryAgent
/*    */ {
/*    */   private DiscoveryListener discoveryListener;
/*    */   
/*    */   public DiscoveryListener getDiscoveryListener() {
/* 33 */     return this.discoveryListener;
/*    */   }
/*    */   
/*    */   public void setDiscoveryListener(DiscoveryListener listener) {
/* 37 */     this.discoveryListener = listener;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\DiscoveryAgentSupport.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */