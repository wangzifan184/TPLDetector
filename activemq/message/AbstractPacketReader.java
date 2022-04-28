/*     */ package org.codehaus.activemq.message;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
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
/*     */ 
/*     */ 
/*     */ public abstract class AbstractPacketReader
/*     */   implements PacketReader
/*     */ {
/*     */   public boolean canRead(int packetType) {
/*  41 */     return (getPacketType() == packetType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String readUTF(DataInput dataIn) throws IOException {
/*  52 */     return dataIn.readUTF();
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
/*     */   protected Object readObject(DataInput dataIn) throws IOException {
/*  64 */     int dataLength = dataIn.readInt();
/*  65 */     if (dataLength > 0) {
/*  66 */       byte[] data = new byte[dataLength];
/*  67 */       dataIn.readFully(data);
/*  68 */       ByteArrayInputStream bytesIn = new ByteArrayInputStream(data);
/*  69 */       ObjectInputStream objIn = new ObjectInputStream(bytesIn);
/*     */       try {
/*  71 */         return objIn.readObject();
/*     */       }
/*  73 */       catch (ClassNotFoundException ex) {
/*  74 */         throw new IOException(ex.getMessage());
/*     */       } 
/*     */     } 
/*  77 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildPacket(Packet p, DataInput dataIn) throws IOException {
/*  88 */     AbstractPacket packet = (AbstractPacket)p;
/*  89 */     packet.setId(readUTF(dataIn));
/*  90 */     BitArray ba = packet.getBitArray();
/*  91 */     ba.readFromStream(dataIn);
/*  92 */     packet.setReceiptRequired(ba.get(0));
/*  93 */     if (ba.get(1)) {
/*  94 */       int visitedLen = dataIn.readShort();
/*  95 */       for (int i = 0; i < visitedLen; i++) {
/*  96 */         packet.addBrokerVisited(dataIn.readUTF());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Packet readPacketFromByteArray(byte[] data) throws IOException {
/* 109 */     ByteArrayInputStream bytesIn = new ByteArrayInputStream(data);
/* 110 */     DataInputStream dataIn = new DataInputStream(bytesIn);
/* 111 */     Packet packet = createPacket();
/* 112 */     buildPacket(packet, dataIn);
/* 113 */     return packet;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\AbstractPacketReader.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */