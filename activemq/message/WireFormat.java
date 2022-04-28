/*     */ package org.codehaus.activemq.message;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutput;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class WireFormat
/*     */ {
/*  49 */   private static final Log log = LogFactory.getLog(WireFormat.class);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Packet readPacket(String channelID, DatagramPacket dpacket) throws IOException {
/*  88 */     DataInput in = new DataInputStream(new ByteArrayInputStream(dpacket.getData(), dpacket.getOffset(), dpacket.getLength()));
/*  89 */     String id = in.readUTF();
/*     */     
/*  91 */     if (channelID == null) {
/*  92 */       log.trace("We do not have a channelID which is probably caused by a synchronization issue, we're receiving messages before we're fully initialised");
/*     */     }
/*  94 */     else if (channelID.equals(id)) {
/*  95 */       if (log.isTraceEnabled()) {
/*  96 */         log.trace("Discarding packet from id: " + id);
/*     */       }
/*  98 */       return null;
/*     */     } 
/* 100 */     int type = in.readByte();
/* 101 */     Packet packet = readPacket(type, in);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 107 */     return packet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DatagramPacket writePacket(String channelID, Packet packet) throws IOException, JMSException {
/* 130 */     ByteArrayOutputStream out = new ByteArrayOutputStream();
/* 131 */     DataOutputStream dataOut = new DataOutputStream(out);
/* 132 */     channelID = (channelID != null) ? channelID : "";
/* 133 */     dataOut.writeUTF(channelID);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     writePacket(packet, dataOut);
/* 140 */     dataOut.close();
/* 141 */     byte[] data = out.toByteArray();
/* 142 */     return new DatagramPacket(data, data.length);
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
/*     */   public Packet fromBytes(byte[] bytes, int offset, int length) throws IOException {
/* 154 */     DataInput in = new DataInputStream(new ByteArrayInputStream(bytes, offset, length));
/* 155 */     return readPacket(in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Packet fromBytes(byte[] bytes) throws IOException {
/* 165 */     DataInput in = new DataInputStream(new ByteArrayInputStream(bytes));
/* 166 */     return readPacket(in);
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
/*     */   public byte[] toBytes(Packet packet) throws IOException, JMSException {
/* 178 */     ByteArrayOutputStream out = new ByteArrayOutputStream();
/* 179 */     DataOutputStream dataOut = new DataOutputStream(out);
/* 180 */     writePacket(packet, dataOut);
/* 181 */     dataOut.close();
/* 182 */     return out.toByteArray();
/*     */   }
/*     */   
/*     */   public abstract Packet readPacket(DataInput paramDataInput) throws IOException;
/*     */   
/*     */   public abstract Packet readPacket(int paramInt, DataInput paramDataInput) throws IOException;
/*     */   
/*     */   public abstract void writePacket(Packet paramPacket, DataOutput paramDataOutput) throws IOException, JMSException;
/*     */   
/*     */   public abstract WireFormat copy();
/*     */   
/*     */   public abstract boolean canProcessWireFormatVersion(int paramInt);
/*     */   
/*     */   public abstract int getCurrentWireFormatVersion();
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\WireFormat.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */