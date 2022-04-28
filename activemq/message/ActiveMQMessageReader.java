/*     */ package org.codehaus.activemq.message;
/*     */ 
/*     */ import java.io.DataInput;
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
/*     */ public class ActiveMQMessageReader
/*     */   extends AbstractPacketReader
/*     */ {
/*     */   public int getPacketType() {
/*  36 */     return 6;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Packet createPacket() {
/*  43 */     return new ActiveMQMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildPacket(Packet packet, DataInput dataIn) throws IOException {
/*  54 */     super.buildPacket(packet, dataIn);
/*  55 */     ActiveMQMessage msg = (ActiveMQMessage)packet;
/*  56 */     msg.setJMSClientID(readUTF(dataIn));
/*  57 */     msg.setProducerID(readUTF(dataIn));
/*  58 */     msg.setJMSDestination(ActiveMQDestination.readFromStream(dataIn));
/*  59 */     msg.setJMSDeliveryMode(dataIn.readByte());
/*  60 */     msg.setJMSPriority(dataIn.readByte());
/*  61 */     BitArray ba = msg.getBitArray();
/*     */     
/*  63 */     msg.setJMSRedelivered(ba.get(10));
/*  64 */     msg.setXaTransacted(ba.get(11));
/*     */     
/*  66 */     if (ba.get(2)) {
/*  67 */       msg.setJMSCorrelationID(readUTF(dataIn));
/*     */     }
/*  69 */     if (ba.get(3)) {
/*  70 */       msg.setJMSType(readUTF(dataIn));
/*     */     }
/*  72 */     if (ba.get(4)) {
/*  73 */       msg.setEntryBrokerName(readUTF(dataIn));
/*     */     }
/*  75 */     if (ba.get(5)) {
/*  76 */       msg.setEntryClusterName(readUTF(dataIn));
/*     */     }
/*  78 */     if (ba.get(6)) {
/*  79 */       msg.setTransactionId(readUTF(dataIn));
/*     */     }
/*  81 */     if (ba.get(7)) {
/*  82 */       msg.setJMSReplyTo(ActiveMQDestination.readFromStream(dataIn));
/*     */     }
/*  84 */     if (ba.get(8)) {
/*  85 */       msg.setJMSTimestamp(dataIn.readLong());
/*     */     }
/*  87 */     if (ba.get(9)) {
/*  88 */       msg.setJMSExpiration(dataIn.readLong());
/*     */     }
/*  90 */     if (ba.get(12)) {
/*  91 */       int cidlength = dataIn.readShort();
/*  92 */       if (cidlength > 0) {
/*  93 */         int[] cids = new int[cidlength];
/*  94 */         for (int i = 0; i < cids.length; i++) {
/*  95 */           cids[i] = dataIn.readShort();
/*     */         }
/*  97 */         msg.setConsumerNos(cids);
/*     */       } 
/*     */     } 
/* 100 */     if (ba.get(13)) {
/* 101 */       msg.setProperties(msg.readMapProperties(dataIn));
/*     */     }
/* 103 */     if (ba.get(15)) {
/* 104 */       int payloadLength = dataIn.readInt();
/* 105 */       if (payloadLength >= 0) {
/* 106 */         byte[] payload = new byte[payloadLength];
/* 107 */         dataIn.readFully(payload);
/* 108 */         msg.setBodyAsBytes(payload);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ActiveMQMessageReader.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */