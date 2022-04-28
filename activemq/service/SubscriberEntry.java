/*     */ package org.codehaus.activemq.service;
/*     */ 
/*     */ import java.io.Externalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
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
/*     */ public class SubscriberEntry
/*     */   implements Externalizable
/*     */ {
/*     */   private static final long serialVersionUID = -5754338187296859149L;
/*     */   private int subscriberID;
/*     */   private String clientID;
/*     */   private String consumerName;
/*     */   private String destination;
/*     */   private String selector;
/*     */   
/*     */   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
/*  44 */     this.subscriberID = in.readInt();
/*  45 */     this.clientID = in.readUTF();
/*  46 */     this.consumerName = in.readUTF();
/*  47 */     this.destination = in.readUTF();
/*  48 */     this.selector = readNullableUTF(in);
/*     */   }
/*     */   
/*     */   public void writeExternal(ObjectOutput out) throws IOException {
/*  52 */     out.writeInt(this.subscriberID);
/*  53 */     out.writeUTF(this.clientID);
/*  54 */     out.writeUTF(this.consumerName);
/*  55 */     out.writeUTF(this.destination);
/*  56 */     writeNullableUTF(out, this.selector);
/*     */   }
/*     */   
/*     */   public static String readNullableUTF(ObjectInput in) throws IOException, ClassNotFoundException {
/*  60 */     if (in.readBoolean()) {
/*  61 */       return in.readUTF();
/*     */     }
/*  63 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void writeNullableUTF(ObjectOutput out, String value) throws IOException {
/*  68 */     if (value == null) {
/*  69 */       out.writeBoolean(false);
/*     */     } else {
/*  71 */       out.writeBoolean(true);
/*  72 */       out.writeUTF(value);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String toString() {
/*  77 */     return super.toString() + "[clientID: " + this.clientID + " consumerName: " + this.consumerName + " destination: " + this.destination + " selector: " + this.selector + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClientID() {
/*  84 */     return this.clientID;
/*     */   }
/*     */   
/*     */   public void setClientID(String clientID) {
/*  88 */     this.clientID = clientID;
/*     */   }
/*     */   
/*     */   public String getConsumerName() {
/*  92 */     return this.consumerName;
/*     */   }
/*     */   
/*     */   public void setConsumerName(String consumerName) {
/*  96 */     this.consumerName = consumerName;
/*     */   }
/*     */   
/*     */   public String getDestination() {
/* 100 */     return this.destination;
/*     */   }
/*     */   
/*     */   public void setDestination(String destination) {
/* 104 */     this.destination = destination;
/*     */   }
/*     */   
/*     */   public String getSelector() {
/* 108 */     return this.selector;
/*     */   }
/*     */   
/*     */   public void setSelector(String selector) {
/* 112 */     this.selector = selector;
/*     */   }
/*     */   
/*     */   public int getSubscriberID() {
/* 116 */     return this.subscriberID;
/*     */   }
/*     */   
/*     */   public void setSubscriberID(int subscriberID) {
/* 120 */     this.subscriberID = subscriberID;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\SubscriberEntry.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */