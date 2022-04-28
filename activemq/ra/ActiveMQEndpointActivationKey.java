/*    */ package org.codehaus.activemq.ra;
/*    */ 
/*    */ import javax.resource.spi.endpoint.MessageEndpointFactory;
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
/*    */ public class ActiveMQEndpointActivationKey
/*    */ {
/*    */   private final MessageEndpointFactory messageEndpointFactory;
/*    */   private final ActiveMQActivationSpec activationSpec;
/*    */   
/*    */   public ActiveMQActivationSpec getActivationSpec() {
/* 31 */     return this.activationSpec;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MessageEndpointFactory getMessageEndpointFactory() {
/* 38 */     return this.messageEndpointFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ActiveMQEndpointActivationKey(MessageEndpointFactory messageEndpointFactory, ActiveMQActivationSpec activationSpec) {
/* 46 */     this.messageEndpointFactory = messageEndpointFactory;
/* 47 */     this.activationSpec = activationSpec;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 54 */     return this.messageEndpointFactory.hashCode() ^ this.activationSpec.hashCode();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 61 */     if (this == obj) {
/* 62 */       return true;
/*    */     }
/* 64 */     if (obj == null) {
/* 65 */       return false;
/*    */     }
/* 67 */     ActiveMQEndpointActivationKey o = (ActiveMQEndpointActivationKey)obj;
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 72 */     return (o.activationSpec == this.activationSpec && o.messageEndpointFactory == this.messageEndpointFactory);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ra\ActiveMQEndpointActivationKey.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */