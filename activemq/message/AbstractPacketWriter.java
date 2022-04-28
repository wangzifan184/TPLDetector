/*     */ package org.codehaus.activemq.message;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutput;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
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
/*     */ public abstract class AbstractPacketWriter
/*     */   implements PacketWriter
/*     */ {
/*     */   protected void writeUTF(String str, DataOutput dataOut) throws IOException {
/*  41 */     if (str == null) {
/*  42 */       str = "";
/*     */     }
/*  44 */     dataOut.writeUTF(str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canWrite(Packet packet) {
/*  52 */     return (packet.getPacketType() == getPacketType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeObject(Object object, DataOutput dataOut) throws IOException {
/*  63 */     if (object != null) {
/*  64 */       ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
/*  65 */       ObjectOutputStream objOut = new ObjectOutputStream(bytesOut);
/*  66 */       objOut.writeObject(object);
/*  67 */       objOut.flush();
/*  68 */       byte[] data = bytesOut.toByteArray();
/*  69 */       dataOut.writeInt(data.length);
/*  70 */       dataOut.write(data);
/*     */     } else {
/*     */       
/*  73 */       dataOut.writeInt(0);
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
/*     */   public byte[] writePacketToByteArray(Packet packet) throws IOException {
/*  85 */     ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
/*  86 */     DataOutputStream dataOut = new DataOutputStream(bytesOut);
/*  87 */     writePacket(packet, dataOut);
/*  88 */     dataOut.flush();
/*  89 */     return bytesOut.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writePacket(Packet p, DataOutput dataOut) throws IOException {
/* 100 */     AbstractPacket packet = (AbstractPacket)p;
/* 101 */     writeUTF(packet.getId(), dataOut);
/* 102 */     BitArray ba = packet.getBitArray();
/* 103 */     ba.set(0, packet.isReceiptRequired());
/* 104 */     Object[] visited = packet.getBrokersVisited();
/* 105 */     boolean writeVisited = (visited != null && visited.length > 0);
/* 106 */     ba.set(1, writeVisited);
/* 107 */     ba.writeToStream(dataOut);
/* 108 */     if (writeVisited) {
/* 109 */       dataOut.writeShort(visited.length);
/* 110 */       for (int i = 0; i < visited.length; i++)
/* 111 */         dataOut.writeUTF(visited[i].toString()); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\AbstractPacketWriter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */