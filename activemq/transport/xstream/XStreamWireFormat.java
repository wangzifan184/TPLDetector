/*    */ package org.codehaus.activemq.transport.xstream;
/*    */ 
/*    */ import com.thoughtworks.xstream.XStream;
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ import javax.jms.JMSException;
/*    */ import org.codehaus.activemq.message.Packet;
/*    */ import org.codehaus.activemq.message.TextWireFormat;
/*    */ import org.codehaus.activemq.message.WireFormat;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XStreamWireFormat
/*    */   extends TextWireFormat
/*    */ {
/*    */   private XStream xStream;
/*    */   
/*    */   public Packet readPacket(DataInput in) throws IOException {
/* 41 */     String text = in.readUTF();
/* 42 */     return (Packet)getXStream().fromXML(text);
/*    */   }
/*    */   
/*    */   public Packet readPacket(int firstByte, DataInput in) throws IOException {
/* 46 */     String text = in.readUTF();
/* 47 */     return (Packet)getXStream().fromXML(text);
/*    */   }
/*    */   
/*    */   public void writePacket(Packet packet, DataOutput out) throws IOException, JMSException {
/* 51 */     String text = getXStream().toXML(packet);
/* 52 */     out.writeUTF(text);
/*    */   }
/*    */   
/*    */   public WireFormat copy() {
/* 56 */     return (WireFormat)new XStreamWireFormat();
/*    */   }
/*    */   
/*    */   public String toString(Packet packet) {
/* 60 */     return getXStream().toXML(packet);
/*    */   }
/*    */   
/*    */   public Packet fromString(String xml) {
/* 64 */     return (Packet)getXStream().fromXML(xml);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canProcessWireFormatVersion(int version) {
/* 73 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getCurrentWireFormatVersion() {
/* 80 */     return 1;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public XStream getXStream() {
/* 86 */     if (this.xStream == null) {
/* 87 */       this.xStream = createXStream();
/*    */     }
/* 89 */     return this.xStream;
/*    */   }
/*    */   
/*    */   public void setXStream(XStream xStream) {
/* 93 */     this.xStream = xStream;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected XStream createXStream() {
/* 99 */     return new XStream();
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\xstream\XStreamWireFormat.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */