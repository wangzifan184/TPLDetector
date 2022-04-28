/*     */ package org.codehaus.activemq.message;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.io.UTFDataFormatException;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.MessageNotWriteableException;
/*     */ import javax.jms.TextMessage;
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
/*     */ public class ActiveMQTextMessage
/*     */   extends ActiveMQMessage
/*     */   implements TextMessage
/*     */ {
/*     */   private String text;
/*     */   
/*     */   public String toString() {
/*  58 */     String payload = null;
/*     */     try {
/*  60 */       payload = getText();
/*     */     }
/*  62 */     catch (JMSException e) {
/*  63 */       payload = "could not read payload: " + e.toString();
/*     */     } 
/*  65 */     return super.toString() + ", text = " + payload;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPacketType() {
/*  75 */     return 7;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQMessage shallowCopy() throws JMSException {
/*  84 */     ActiveMQTextMessage other = new ActiveMQTextMessage();
/*  85 */     initializeOther(other);
/*  86 */     other.text = this.text;
/*  87 */     return other;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQMessage deepCopy() throws JMSException {
/*  96 */     return shallowCopy();
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
/*     */   public void clearBody() throws JMSException {
/* 112 */     super.clearBody();
/* 113 */     this.text = null;
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
/*     */   public void setText(String string) throws JMSException {
/* 127 */     if (this.readOnlyMessage) {
/* 128 */       throw new MessageNotWriteableException("The message is read only");
/*     */     }
/*     */     
/* 131 */     clearBody();
/* 132 */     this.text = string;
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
/*     */   public String getText() throws JMSException {
/* 145 */     if (this.text == null) {
/*     */       try {
/* 147 */         buildBodyFromBytes();
/*     */       }
/* 149 */       catch (IOException ioe) {
/* 150 */         JMSException jmsEx = new JMSException("failed to build body from bytes");
/* 151 */         jmsEx.setLinkedException(ioe);
/* 152 */         throw jmsEx;
/*     */       } 
/*     */     }
/* 155 */     return this.text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeBody(DataOutput dataOut) throws IOException {
/* 166 */     if (this.text != null) {
/* 167 */       int strlen = this.text.length();
/* 168 */       int utflen = 0;
/* 169 */       char[] charr = new char[strlen];
/* 170 */       int count = 0;
/*     */       
/* 172 */       this.text.getChars(0, strlen, charr, 0);
/*     */       
/* 174 */       for (int i = 0; i < strlen; i++) {
/* 175 */         int c = charr[i];
/* 176 */         if (c >= 1 && c <= 127) {
/* 177 */           utflen++;
/*     */         }
/* 179 */         else if (c > 2047) {
/* 180 */           utflen += 3;
/*     */         } else {
/*     */           
/* 183 */           utflen += 2;
/*     */         } 
/*     */       } 
/*     */       
/* 187 */       byte[] bytearr = new byte[utflen + 4];
/* 188 */       bytearr[count++] = (byte)(utflen >>> 24 & 0xFF);
/* 189 */       bytearr[count++] = (byte)(utflen >>> 16 & 0xFF);
/* 190 */       bytearr[count++] = (byte)(utflen >>> 8 & 0xFF);
/* 191 */       bytearr[count++] = (byte)(utflen >>> 0 & 0xFF);
/* 192 */       for (int j = 0; j < strlen; j++) {
/* 193 */         int c = charr[j];
/* 194 */         if (c >= 1 && c <= 127) {
/* 195 */           bytearr[count++] = (byte)c;
/*     */         }
/* 197 */         else if (c > 2047) {
/* 198 */           bytearr[count++] = (byte)(0xE0 | c >> 12 & 0xF);
/* 199 */           bytearr[count++] = (byte)(0x80 | c >> 6 & 0x3F);
/* 200 */           bytearr[count++] = (byte)(0x80 | c >> 0 & 0x3F);
/*     */         } else {
/*     */           
/* 203 */           bytearr[count++] = (byte)(0xC0 | c >> 6 & 0x1F);
/* 204 */           bytearr[count++] = (byte)(0x80 | c >> 0 & 0x3F);
/*     */         } 
/*     */       } 
/* 207 */       dataOut.write(bytearr);
/*     */     }
/*     */     else {
/*     */       
/* 211 */       dataOut.writeInt(-1);
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
/*     */   protected void readBody(DataInput dataIn) throws IOException {
/* 223 */     int utflen = dataIn.readInt();
/* 224 */     if (utflen > -1) {
/* 225 */       StringBuffer str = new StringBuffer(utflen);
/* 226 */       byte[] bytearr = new byte[utflen];
/*     */       
/* 228 */       int count = 0;
/*     */       
/* 230 */       dataIn.readFully(bytearr, 0, utflen);
/*     */       
/* 232 */       while (count < utflen) {
/* 233 */         int char2, char3, c = bytearr[count] & 0xFF;
/* 234 */         switch (c >> 4) {
/*     */           
/*     */           case 0:
/*     */           case 1:
/*     */           case 2:
/*     */           case 3:
/*     */           case 4:
/*     */           case 5:
/*     */           case 6:
/*     */           case 7:
/* 244 */             count++;
/* 245 */             str.append((char)c);
/*     */             continue;
/*     */           
/*     */           case 12:
/*     */           case 13:
/* 250 */             count += 2;
/* 251 */             if (count > utflen) {
/* 252 */               throw new UTFDataFormatException();
/*     */             }
/* 254 */             char2 = bytearr[count - 1];
/* 255 */             if ((char2 & 0xC0) != 128) {
/* 256 */               throw new UTFDataFormatException();
/*     */             }
/* 258 */             str.append((char)((c & 0x1F) << 6 | char2 & 0x3F));
/*     */             continue;
/*     */           
/*     */           case 14:
/* 262 */             count += 3;
/* 263 */             if (count > utflen) {
/* 264 */               throw new UTFDataFormatException();
/*     */             }
/* 266 */             char2 = bytearr[count - 2];
/* 267 */             char3 = bytearr[count - 1];
/* 268 */             if ((char2 & 0xC0) != 128 || (char3 & 0xC0) != 128) {
/* 269 */               throw new UTFDataFormatException();
/*     */             }
/* 271 */             str.append((char)((c & 0xF) << 12 | (char2 & 0x3F) << 6 | (char3 & 0x3F) << 0));
/*     */             continue;
/*     */         } 
/*     */         
/* 275 */         throw new UTFDataFormatException();
/*     */       } 
/*     */ 
/*     */       
/* 279 */       this.text = new String(str);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ActiveMQTextMessage.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */