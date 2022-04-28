/*     */ package org.codehaus.activemq.message;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutput;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.util.WireByteArrayInputStream;
/*     */ import org.codehaus.activemq.message.util.WireByteArrayOutputStream;
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
/*     */ public class DefaultWireFormat
/*     */   extends WireFormat
/*     */   implements Serializable
/*     */ {
/*     */   public static final int WIRE_FORMAT_VERSION = 1;
/*  43 */   private static final Log log = LogFactory.getLog(DefaultWireFormat.class);
/*     */   
/*  45 */   private final transient PacketReader messageReader = new ActiveMQMessageReader();
/*  46 */   private final transient PacketReader textMessageReader = new ActiveMQTextMessageReader();
/*  47 */   private final transient PacketReader objectMessageReader = new ActiveMQObjectMessageReader();
/*  48 */   private final transient PacketReader bytesMessageReader = new ActiveMQBytesMessageReader();
/*  49 */   private final transient PacketReader streamMessageReader = new ActiveMQStreamMessageReader();
/*  50 */   private final transient PacketReader mapMessageReader = new ActiveMQMapMessageReader();
/*  51 */   private final transient PacketReader messageAckReader = new MessageAckReader();
/*  52 */   private final transient PacketReader receiptReader = new ReceiptReader();
/*  53 */   private final transient PacketReader consumerInfoReader = new ConsumerInfoReader();
/*  54 */   private final transient PacketReader producerInfoReader = new ProducerInfoReader();
/*  55 */   private final transient PacketReader transactionInfoReader = new TransactionInfoReader();
/*  56 */   private final transient PacketReader xaTransactionInfoReader = new XATransactionInfoReader();
/*  57 */   private final transient PacketReader brokerInfoReader = new BrokerInfoReader();
/*  58 */   private final transient PacketReader connectionInfoReader = new ConnectionInfoReader();
/*  59 */   private final transient PacketReader sessionInfoReader = new SessionInfoReader();
/*  60 */   private final transient PacketReader durableUnsubscribeReader = new DurableUnsubscribeReader();
/*  61 */   private final transient PacketReader reponseReceiptReader = new ResponseReceiptReader();
/*  62 */   private final transient PacketReader intReponseReceiptReader = new IntResponseReceiptReader();
/*  63 */   private final transient PacketReader capacityInfoReader = new CapacityInfoReader();
/*  64 */   private final transient PacketReader capacityInfoRequestReader = new CapacityInfoRequestReader();
/*  65 */   private final transient PacketReader wireFormatInfoReader = new WireFormatInfoReader();
/*  66 */   private final transient PacketWriter messageWriter = new ActiveMQMessageWriter();
/*  67 */   private final transient PacketWriter textMessageWriter = new ActiveMQTextMessageWriter();
/*  68 */   private final transient PacketWriter objectMessageWriter = new ActiveMQObjectMessageWriter();
/*  69 */   private final transient PacketWriter bytesMessageWriter = new ActiveMQBytesMessageWriter();
/*  70 */   private final transient PacketWriter streamMessageWriter = new ActiveMQStreamMessageWriter();
/*  71 */   private final transient PacketWriter mapMessageWriter = new ActiveMQMapMessageWriter();
/*  72 */   private final transient PacketWriter messageAckWriter = new MessageAckWriter();
/*  73 */   private final transient PacketWriter receiptWriter = new ReceiptWriter();
/*  74 */   private final transient PacketWriter consumerInfoWriter = new ConsumerInfoWriter();
/*  75 */   private final transient PacketWriter producerInfoWriter = new ProducerInfoWriter();
/*  76 */   private final transient PacketWriter transactionInfoWriter = new TransactionInfoWriter();
/*  77 */   private final transient PacketWriter xaTransactionInfoWriter = new XATransactionInfoWriter();
/*  78 */   private final transient PacketWriter brokerInfoWriter = new BrokerInfoWriter();
/*  79 */   private final transient PacketWriter connectionInfoWriter = new ConnectionInfoWriter();
/*  80 */   private final transient PacketWriter sessionInfoWriter = new SessionInfoWriter();
/*  81 */   private final transient PacketWriter durableUnsubscribeWriter = new DurableUnsubscribeWriter();
/*  82 */   private final transient PacketWriter reponseReceiptWriter = new ResponseReceiptWriter();
/*  83 */   private final transient PacketWriter intReponseReceiptWriter = new IntResponseReceiptWriter();
/*  84 */   private final transient PacketWriter capacityInfoWriter = new CapacityInfoWriter();
/*  85 */   private final transient PacketWriter capacityInfoRequestWriter = new CapacityInfoRequestWriter();
/*  86 */   private final transient PacketWriter wireFormatInfoWriter = new WireFormatInfoWriter();
/*     */   
/*     */   private transient WireByteArrayOutputStream internalBytesOut;
/*     */   
/*     */   private transient DataOutputStream internalDataOut;
/*     */   
/*     */   private transient WireByteArrayInputStream internalBytesIn;
/*     */   private transient DataInputStream internalDataIn;
/*     */   
/*     */   public DefaultWireFormat() {
/*  96 */     this.internalBytesOut = new WireByteArrayOutputStream();
/*  97 */     this.internalDataOut = new DataOutputStream((OutputStream)this.internalBytesOut);
/*  98 */     this.internalBytesIn = new WireByteArrayInputStream();
/*  99 */     this.internalDataIn = new DataInputStream((InputStream)this.internalBytesIn);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WireFormat copy() {
/* 107 */     return new DefaultWireFormat();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Packet readPacket(DataInput in) throws IOException {
/* 117 */     int type = in.readByte();
/* 118 */     return readPacket(type, in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Packet readPacket(int firstByte, DataInput dataIn) throws IOException {
/* 129 */     switch (firstByte) {
/*     */       case 6:
/* 131 */         return readPacket(dataIn, this.messageReader);
/*     */       case 7:
/* 133 */         return readPacket(dataIn, this.textMessageReader);
/*     */       case 8:
/* 135 */         return readPacket(dataIn, this.objectMessageReader);
/*     */       case 9:
/* 137 */         return readPacket(dataIn, this.bytesMessageReader);
/*     */       case 10:
/* 139 */         return readPacket(dataIn, this.streamMessageReader);
/*     */       case 11:
/* 141 */         return readPacket(dataIn, this.mapMessageReader);
/*     */       case 15:
/* 143 */         return readPacket(dataIn, this.messageAckReader);
/*     */       case 16:
/* 145 */         return readPacket(dataIn, this.receiptReader);
/*     */       case 17:
/* 147 */         return readPacket(dataIn, this.consumerInfoReader);
/*     */       case 18:
/* 149 */         return readPacket(dataIn, this.producerInfoReader);
/*     */       case 19:
/* 151 */         return readPacket(dataIn, this.transactionInfoReader);
/*     */       case 20:
/* 153 */         return readPacket(dataIn, this.xaTransactionInfoReader);
/*     */       case 21:
/* 155 */         return readPacket(dataIn, this.brokerInfoReader);
/*     */       case 22:
/* 157 */         return readPacket(dataIn, this.connectionInfoReader);
/*     */       case 23:
/* 159 */         return readPacket(dataIn, this.sessionInfoReader);
/*     */       case 24:
/* 161 */         return readPacket(dataIn, this.durableUnsubscribeReader);
/*     */       case 25:
/* 163 */         return readPacket(dataIn, this.reponseReceiptReader);
/*     */       case 26:
/* 165 */         return readPacket(dataIn, this.intReponseReceiptReader);
/*     */       case 27:
/* 167 */         return readPacket(dataIn, this.capacityInfoReader);
/*     */       case 28:
/* 169 */         return readPacket(dataIn, this.capacityInfoRequestReader);
/*     */       case 29:
/* 171 */         return readPacket(dataIn, this.wireFormatInfoReader);
/*     */     } 
/* 173 */     log.error("Could not find PacketReader for packet type: " + AbstractPacket.getPacketTypeAsString(firstByte));
/*     */     
/* 175 */     return null;
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
/*     */   public void writePacket(Packet packet, DataOutput dataOut) throws IOException {
/* 187 */     PacketWriter writer = getWriter(packet);
/* 188 */     if (writer != null) {
/* 189 */       writePacket(packet, dataOut, writer);
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
/*     */   
/*     */   public byte[] toBytes(Packet packet) throws IOException {
/* 202 */     byte[] data = null;
/* 203 */     PacketWriter writer = getWriter(packet);
/* 204 */     if (writer != null) {
/* 205 */       this.internalBytesOut.reset();
/* 206 */       this.internalDataOut.writeByte(packet.getPacketType());
/* 207 */       this.internalDataOut.writeInt(-1);
/* 208 */       writer.writePacket(packet, this.internalDataOut);
/* 209 */       this.internalDataOut.flush();
/* 210 */       data = this.internalBytesOut.toByteArray();
/*     */       
/* 212 */       int length = data.length - 5;
/* 213 */       packet.setMemoryUsage(length);
/*     */       
/* 215 */       data[1] = (byte)(length >>> 24 & 0xFF);
/* 216 */       data[2] = (byte)(length >>> 16 & 0xFF);
/* 217 */       data[3] = (byte)(length >>> 8 & 0xFF);
/* 218 */       data[4] = (byte)(length >>> 0 & 0xFF);
/*     */     } 
/* 220 */     return data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canProcessWireFormatVersion(int version) {
/* 229 */     return (version == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCurrentWireFormatVersion() {
/* 236 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final synchronized void writePacket(Packet packet, DataOutput dataOut, PacketWriter writer) throws IOException {
/* 241 */     dataOut.writeByte(packet.getPacketType());
/* 242 */     this.internalBytesOut.reset();
/* 243 */     writer.writePacket(packet, this.internalDataOut);
/* 244 */     this.internalDataOut.flush();
/*     */     
/* 246 */     byte[] data = this.internalBytesOut.getData();
/* 247 */     int count = this.internalBytesOut.size();
/* 248 */     dataOut.writeInt(count);
/*     */ 
/*     */ 
/*     */     
/* 252 */     packet.setMemoryUsage(count);
/* 253 */     dataOut.write(data, 0, count);
/*     */   }
/*     */   
/*     */   protected final synchronized Packet readPacket(DataInput dataIn, PacketReader reader) throws IOException {
/* 257 */     Packet packet = reader.createPacket();
/* 258 */     int length = dataIn.readInt();
/* 259 */     packet.setMemoryUsage(length);
/*     */ 
/*     */     
/* 262 */     byte[] data = new byte[length];
/* 263 */     dataIn.readFully(data);
/*     */     
/* 265 */     this.internalBytesIn.restart(data);
/* 266 */     reader.buildPacket(packet, this.internalDataIn);
/* 267 */     return packet;
/*     */   }
/*     */   
/*     */   private Object readResolve() throws ObjectStreamException {
/* 271 */     return new DefaultWireFormat();
/*     */   }
/*     */   
/*     */   private PacketWriter getWriter(Packet packet) throws IOException {
/* 275 */     PacketWriter answer = null;
/* 276 */     switch (packet.getPacketType())
/*     */     { case 6:
/* 278 */         answer = this.messageWriter;
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
/* 343 */         return answer;case 7: answer = this.textMessageWriter; return answer;case 8: answer = this.objectMessageWriter; return answer;case 9: answer = this.bytesMessageWriter; return answer;case 10: answer = this.streamMessageWriter; return answer;case 11: answer = this.mapMessageWriter; return answer;case 15: answer = this.messageAckWriter; return answer;case 16: answer = this.receiptWriter; return answer;case 17: answer = this.consumerInfoWriter; return answer;case 18: answer = this.producerInfoWriter; return answer;case 19: answer = this.transactionInfoWriter; return answer;case 20: answer = this.xaTransactionInfoWriter; return answer;case 21: answer = this.brokerInfoWriter; return answer;case 22: answer = this.connectionInfoWriter; return answer;case 23: answer = this.sessionInfoWriter; return answer;case 24: answer = this.durableUnsubscribeWriter; return answer;case 25: answer = this.reponseReceiptWriter; return answer;case 26: answer = this.intReponseReceiptWriter; return answer;case 27: answer = this.capacityInfoWriter; return answer;case 28: answer = this.capacityInfoRequestWriter; return answer;case 29: answer = this.wireFormatInfoWriter; return answer; }  log.error("no PacketWriter for packet: " + packet); return answer;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\DefaultWireFormat.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */