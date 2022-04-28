/*     */ package org.codehaus.activemq.ra;
/*     */ 
/*     */ import javax.resource.ResourceException;
/*     */ import javax.resource.spi.ActivationSpec;
/*     */ import javax.resource.spi.InvalidPropertyException;
/*     */ import javax.resource.spi.ResourceAdapter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ActiveMQActivationSpec
/*     */   implements ActivationSpec
/*     */ {
/*     */   private ActiveMQResourceAdapter resourceAdapter;
/*     */   private String destinationType;
/*     */   private String destinationName;
/*     */   private String durableSubscriptionName;
/*     */   private String messageSelector;
/*     */   boolean noLocal = false;
/*     */   
/*     */   public void validate() throws InvalidPropertyException {}
/*     */   
/*     */   public ResourceAdapter getResourceAdapter() {
/*  48 */     return this.resourceAdapter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResourceAdapter(ResourceAdapter resourceAdapter) throws ResourceException {
/*  56 */     if (this.resourceAdapter != null) {
/*  57 */       throw new ResourceException("ResourceAdapter already set");
/*     */     }
/*  59 */     if (!(resourceAdapter instanceof ActiveMQResourceAdapter)) {
/*  60 */       throw new ResourceException("ResourceAdapter is not of type: " + ActiveMQResourceAdapter.class.getName());
/*     */     }
/*  62 */     this.resourceAdapter = (ActiveMQResourceAdapter)resourceAdapter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDestinationName() {
/*  76 */     return this.destinationName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestinationName(String destinationName) {
/*  83 */     this.destinationName = destinationName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDestinationType() {
/*  90 */     return this.destinationType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestinationType(String destinationType) {
/*  97 */     this.destinationType = destinationType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDurableSubscriptionName() {
/* 104 */     return this.durableSubscriptionName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDurableSubscriptionName(String durableSubscriptionName) {
/* 111 */     this.durableSubscriptionName = durableSubscriptionName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessageSelector() {
/* 118 */     return this.messageSelector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageSelector(String messageSelector) {
/* 125 */     this.messageSelector = messageSelector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getNoLocal() {
/* 132 */     return this.noLocal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNoLocal(boolean noLocal) {
/* 139 */     this.noLocal = noLocal;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ra\ActiveMQActivationSpec.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */