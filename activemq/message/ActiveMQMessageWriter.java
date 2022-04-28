/*     */ package org.codehaus.activemq.message;
/*     */ 
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import org.codehaus.activemq.util.BitArray;
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
/*     */ public class ActiveMQMessageWriter
/*     */   extends AbstractPacketWriter
/*     */ {
/*     */   public int getPacketType() {
/*  36 */     return 6;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writePacket(Packet packet, DataOutput dataOut) throws IOException {
/*  47 */     ActiveMQMessage msg = (ActiveMQMessage)packet;
/*  48 */     byte[] payload = msg.getBodyAsBytes();
/*     */     
/*  50 */     BitArray ba = msg.getBitArray();
/*  51 */     ba.reset();
/*     */     
/*  53 */     ba.set(2, (msg.getJMSCorrelationID() != null));
/*  54 */     ba.set(3, (msg.getJMSType() != null));
/*  55 */     ba.set(4, (msg.getEntryBrokerName() != null));
/*  56 */     ba.set(5, (msg.getEntryClusterName() != null));
/*  57 */     ba.set(6, (msg.getTransactionId() != null));
/*  58 */     ba.set(7, (msg.getJMSReplyTo() != null));
/*  59 */     ba.set(8, (msg.getJMSTimestamp() > 0L));
/*  60 */     ba.set(9, (msg.getJMSExpiration() > 0L));
/*  61 */     ba.set(10, msg.getJMSRedelivered());
/*  62 */     ba.set(11, msg.isXaTransacted());
/*  63 */     ba.set(12, (msg.getConsumerNos() != null));
/*  64 */     ba.set(13, (msg.getProperties() != null && msg.getProperties().size() > 0));
/*  65 */     ba.set(15, (payload != null));
/*     */     
/*  67 */     super.writePacket(msg, dataOut);
/*     */     
/*  69 */     writeUTF(msg.getJMSClientID(), dataOut);
/*  70 */     writeUTF(msg.getProducerID(), dataOut);
/*  71 */     ActiveMQDestination.writeToStream((ActiveMQDestination)msg.getJMSDestination(), dataOut);
/*  72 */     dataOut.write(msg.getJMSDeliveryMode());
/*  73 */     dataOut.write(msg.getJMSPriority());
/*     */ 
/*     */ 
/*     */     
/*  77 */     if (ba.get(2)) {
/*  78 */       writeUTF(msg.getJMSCorrelationID(), dataOut);
/*     */     }
/*  80 */     if (ba.get(3)) {
/*  81 */       writeUTF(msg.getJMSType(), dataOut);
/*     */     }
/*  83 */     if (ba.get(4)) {
/*  84 */       writeUTF(msg.getEntryBrokerName(), dataOut);
/*     */     }
/*  86 */     if (ba.get(5)) {
/*  87 */       writeUTF(msg.getEntryClusterName(), dataOut);
/*     */     }
/*  89 */     if (ba.get(6)) {
/*  90 */       writeUTF(msg.getTransactionId(), dataOut);
/*     */     }
/*  92 */     if (ba.get(7)) {
/*  93 */       ActiveMQDestination.writeToStream((ActiveMQDestination)msg.getJMSReplyTo(), dataOut);
/*     */     }
/*  95 */     if (ba.get(8)) {
/*  96 */       dataOut.writeLong(msg.getJMSTimestamp());
/*     */     }
/*  98 */     if (ba.get(9)) {
/*  99 */       dataOut.writeLong(msg.getJMSExpiration());
/*     */     }
/* 101 */     if (ba.get(12)) {
/*     */       
/* 103 */       int[] cids = msg.getConsumerNos();
/* 104 */       dataOut.writeShort(cids.length);
/* 105 */       for (int i = 0; i < cids.length; i++) {
/* 106 */         dataOut.writeShort(cids[i]);
/*     */       }
/*     */     } 
/* 109 */     if (ba.get(13)) {
/* 110 */       msg.writeMapProperties(msg.getProperties(), dataOut);
/*     */     }
/* 112 */     if (ba.get(15)) {
/* 113 */       dataOut.writeInt(payload.length);
/* 114 */       dataOut.write(payload);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ActiveMQMessageWriter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */