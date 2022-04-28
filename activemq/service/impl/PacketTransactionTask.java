/*     */ package org.codehaus.activemq.service.impl;
/*     */ 
/*     */ import java.io.Externalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import javax.jms.JMSException;
/*     */ import org.codehaus.activemq.broker.BrokerClient;
/*     */ import org.codehaus.activemq.broker.impl.BrokerClientImpl;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.DefaultWireFormat;
/*     */ import org.codehaus.activemq.message.MessageAck;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.service.TransactionTask;
/*     */ import org.codehaus.activemq.util.JMSExceptionHelper;
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
/*     */ public abstract class PacketTransactionTask
/*     */   implements TransactionTask, Externalizable
/*     */ {
/*     */   private static final long serialVersionUID = -5754338187296859149L;
/*  41 */   private static final transient WireFormat wireFormat = (WireFormat)new DefaultWireFormat();
/*     */   
/*     */   private BrokerClient brokerClient;
/*     */   
/*     */   private Packet packet;
/*     */   
/*     */   public static TransactionTask fromBytes(byte[] data) throws IOException {
/*  48 */     Packet packet = wireFormat.fromBytes(data);
/*  49 */     return createTask(packet);
/*     */   }
/*     */   
/*     */   public byte[] toBytes() throws JMSException, IOException {
/*  53 */     return wireFormat.toBytes(this.packet);
/*     */   }
/*     */   
/*     */   public static TransactionTask readTask(ObjectInput in) throws IOException {
/*  57 */     Packet packet = readPacket(in);
/*  58 */     return createTask(packet);
/*     */   }
/*     */   
/*     */   public static TransactionTask createTask(Packet packet) throws IOException {
/*  62 */     if (packet instanceof MessageAck) {
/*  63 */       return new MessageAckTransactionTask(null, (MessageAck)packet);
/*     */     }
/*  65 */     if (packet instanceof ActiveMQMessage) {
/*  66 */       return new SendMessageTransactionTask(null, (ActiveMQMessage)packet);
/*     */     }
/*     */     
/*  69 */     throw new IOException("Unexpected packet type: " + packet);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void writeTask(TransactionTask task, ObjectOutput out) throws IOException {
/*  74 */     if (task instanceof PacketTransactionTask) {
/*  75 */       PacketTransactionTask packetTask = (PacketTransactionTask)task;
/*  76 */       writePacket(packetTask.getPacket(), out);
/*     */     } else {
/*     */       
/*  79 */       out.writeObject(task);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected PacketTransactionTask(BrokerClient brokerClient, Packet packet) {
/*  84 */     this.brokerClient = brokerClient;
/*  85 */     this.packet = packet;
/*     */   }
/*     */   
/*     */   public Packet getPacket() {
/*  89 */     return this.packet;
/*     */   }
/*     */   
/*     */   public void writeExternal(ObjectOutput out) throws IOException {
/*  93 */     writePacket(this.packet, out);
/*     */   }
/*     */   
/*     */   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
/*  97 */     this.packet = readPacket(in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BrokerClient createBrokerClient(String consumerId) throws JMSException {
/* 104 */     BrokerClientImpl answer = new BrokerClientImpl();
/* 105 */     return (BrokerClient)answer;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BrokerClient getBrokerClient(String consumerId) throws JMSException {
/* 110 */     this.brokerClient = createBrokerClient(consumerId);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     return this.brokerClient;
/*     */   }
/*     */   
/*     */   protected static void writePacket(Packet packet, ObjectOutput out) throws IOException {
/*     */     try {
/* 121 */       wireFormat.writePacket(packet, out);
/*     */     }
/* 123 */     catch (JMSException e) {
/* 124 */       throw JMSExceptionHelper.newIOException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static Packet readPacket(ObjectInput in) throws IOException {
/* 129 */     return wireFormat.readPacket(in);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\PacketTransactionTask.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */