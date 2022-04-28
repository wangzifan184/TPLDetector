/*     */ package org.codehaus.activemq.transport.jabber;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.ActiveMQBytesMessage;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.ActiveMQObjectMessage;
/*     */ import org.codehaus.activemq.message.ActiveMQTextMessage;
/*     */ import org.codehaus.activemq.message.Packet;
/*     */ import org.codehaus.activemq.message.WireFormat;
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
/*     */ public class JabberWireFormat
/*     */   extends WireFormat
/*     */ {
/*  44 */   private static final Log log = LogFactory.getLog(JabberWireFormat.class);
/*     */   
/*     */   public WireFormat copy() {
/*  47 */     return new JabberWireFormat();
/*     */   }
/*     */   
/*     */   public Packet readPacket(DataInput in) throws IOException {
/*  51 */     return null;
/*     */   }
/*     */   
/*     */   public Packet readPacket(int firstByte, DataInput in) throws IOException {
/*  55 */     return null;
/*     */   }
/*     */   
/*     */   public void writePacket(Packet packet, DataOutput out) throws IOException, JMSException {
/*  59 */     switch (packet.getPacketType()) {
/*     */       case 6:
/*  61 */         writeMessage((ActiveMQMessage)packet, "", out);
/*     */         return;
/*     */       
/*     */       case 7:
/*  65 */         writeTextMessage((ActiveMQTextMessage)packet, out);
/*     */         return;
/*     */       
/*     */       case 9:
/*  69 */         writeBytesMessage((ActiveMQBytesMessage)packet, out);
/*     */         return;
/*     */       
/*     */       case 8:
/*  73 */         writeObjectMessage((ActiveMQObjectMessage)packet, out);
/*     */         return;
/*     */     } 
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
/*  93 */     log.warn("Ignoring message type: " + packet.getPacketType() + " packet: " + packet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canProcessWireFormatVersion(int version) {
/* 103 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCurrentWireFormatVersion() {
/* 110 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeObjectMessage(ActiveMQObjectMessage message, DataOutput out) throws JMSException, IOException {
/* 116 */     Serializable object = message.getObject();
/* 117 */     String text = (object != null) ? object.toString() : "";
/* 118 */     writeMessage((ActiveMQMessage)message, text, out);
/*     */   }
/*     */   
/*     */   protected void writeTextMessage(ActiveMQTextMessage message, DataOutput out) throws JMSException, IOException {
/* 122 */     writeMessage((ActiveMQMessage)message, message.getText(), out);
/*     */   }
/*     */   
/*     */   protected void writeBytesMessage(ActiveMQBytesMessage message, DataOutput out) throws IOException {
/* 126 */     byte[] data = message.getBodyAsBytes();
/* 127 */     String text = encodeBinary(data);
/* 128 */     writeMessage((ActiveMQMessage)message, text, out);
/*     */   }
/*     */   
/*     */   protected void writeMessage(ActiveMQMessage message, String body, DataOutput out) throws IOException {
/* 132 */     String type = getXmppType(message);
/*     */     
/* 134 */     StringBuffer buffer = new StringBuffer("<");
/* 135 */     buffer.append(type);
/* 136 */     buffer.append(" to='");
/* 137 */     buffer.append(message.getJMSDestination().toString());
/* 138 */     buffer.append("' from='");
/* 139 */     buffer.append(message.getJMSReplyTo().toString());
/* 140 */     String messageID = message.getJMSMessageID();
/* 141 */     if (messageID != null) {
/* 142 */       buffer.append("' id='");
/* 143 */       buffer.append(messageID);
/*     */     } 
/*     */     
/* 146 */     Hashtable properties = message.getProperties();
/* 147 */     if (properties != null) {
/* 148 */       for (Iterator iter = properties.entrySet().iterator(); iter.hasNext(); ) {
/* 149 */         Map.Entry entry = iter.next();
/* 150 */         Object key = entry.getKey();
/* 151 */         Object value = entry.getValue();
/* 152 */         if (value != null) {
/* 153 */           buffer.append("' ");
/* 154 */           buffer.append(key.toString());
/* 155 */           buffer.append("='");
/* 156 */           buffer.append(value.toString());
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 161 */     buffer.append("'>");
/*     */     
/* 163 */     String id = message.getJMSCorrelationID();
/* 164 */     if (id != null) {
/* 165 */       buffer.append("<thread>");
/* 166 */       buffer.append(id);
/* 167 */       buffer.append("</thread>");
/*     */     } 
/* 169 */     buffer.append(body);
/* 170 */     buffer.append("</");
/* 171 */     buffer.append(type);
/* 172 */     buffer.append(">");
/*     */     
/* 174 */     out.write(buffer.toString().getBytes());
/*     */   }
/*     */ 
/*     */   
/*     */   protected String encodeBinary(byte[] data) {
/* 179 */     throw new RuntimeException("Not implemented yet!");
/*     */   }
/*     */   
/*     */   protected String getXmppType(ActiveMQMessage message) {
/* 183 */     String type = message.getJMSType();
/* 184 */     if (type == null) {
/* 185 */       type = "message";
/*     */     }
/* 187 */     return type;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\jabber\JabberWireFormat.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */