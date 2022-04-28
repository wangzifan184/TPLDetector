/*     */ package org.codehaus.activemq.message;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import javax.jms.BytesMessage;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.MessageEOFException;
/*     */ import javax.jms.MessageFormatException;
/*     */ import javax.jms.MessageNotReadableException;
/*     */ import javax.jms.MessageNotWriteableException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ActiveMQBytesMessage
/*     */   extends ActiveMQMessage
/*     */   implements BytesMessage
/*     */ {
/*     */   private DataOutputStream dataOut;
/*     */   private ByteArrayOutputStream bytesOut;
/*     */   private DataInputStream dataIn;
/*     */   
/*     */   public int getPacketType() {
/*  83 */     return 9;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQMessage shallowCopy() throws JMSException {
/*  91 */     ActiveMQBytesMessage other = new ActiveMQBytesMessage();
/*  92 */     initializeOther(other);
/*     */     try {
/*  94 */       other.setBodyAsBytes(getBodyAsBytes());
/*     */     }
/*  96 */     catch (IOException e) {
/*  97 */       JMSException jmsEx = new JMSException("setBodyAsBytes() failed");
/*  98 */       jmsEx.setLinkedException(e);
/*  99 */       throw jmsEx;
/*     */     } 
/* 101 */     return other;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQMessage deepCopy() throws JMSException {
/* 109 */     ActiveMQBytesMessage other = new ActiveMQBytesMessage();
/* 110 */     initializeOther(other);
/*     */     try {
/* 112 */       if (getBodyAsBytes() != null) {
/* 113 */         byte[] data = new byte[(getBodyAsBytes()).length];
/* 114 */         System.arraycopy(getBodyAsBytes(), 0, data, 0, data.length);
/* 115 */         other.setBodyAsBytes(data);
/*     */       }
/*     */     
/* 118 */     } catch (IOException e) {
/* 119 */       JMSException jmsEx = new JMSException("setBodyAsBytes() failed");
/* 120 */       jmsEx.setLinkedException(e);
/* 121 */       throw jmsEx;
/*     */     } 
/* 123 */     return other;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBodyAsBytes(byte[] bodyAsBytes) {
/* 130 */     super.setBodyAsBytes(bodyAsBytes);
/* 131 */     this.dataOut = null;
/* 132 */     this.dataIn = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getBodyAsBytes() throws IOException {
/* 140 */     if (this.dataOut != null) {
/* 141 */       this.dataOut.flush();
/* 142 */       super.setBodyAsBytes(this.bytesOut.toByteArray());
/* 143 */       this.dataOut.close();
/* 144 */       this.dataOut = null;
/*     */     } 
/* 146 */     return super.getBodyAsBytes();
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
/*     */   public void clearBody() throws JMSException {
/* 158 */     super.clearBody();
/* 159 */     this.dataOut = null;
/* 160 */     this.dataIn = null;
/* 161 */     this.bytesOut = null;
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
/*     */   public long getBodyLength() throws JMSException {
/* 175 */     if (!this.readOnlyMessage) {
/* 176 */       throw new MessageNotReadableException("This message is in write-only mode");
/*     */     }
/* 178 */     long length = 0L;
/*     */     try {
/* 180 */       if (super.getBodyAsBytes() != null) {
/* 181 */         length = (super.getBodyAsBytes()).length;
/*     */       }
/*     */     }
/* 184 */     catch (IOException e) {
/* 185 */       JMSException jmsEx = new JMSException("getBodyAsBytes() failed");
/* 186 */       jmsEx.setLinkedException(e);
/* 187 */       throw jmsEx;
/*     */     } 
/* 189 */     return length;
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
/*     */   public boolean readBoolean() throws JMSException {
/* 201 */     initializeReading();
/*     */     try {
/* 203 */       return this.dataIn.readBoolean();
/*     */     }
/* 205 */     catch (EOFException eof) {
/* 206 */       MessageEOFException messageEOFException = new MessageEOFException(eof.getMessage());
/* 207 */       messageEOFException.setLinkedException(eof);
/* 208 */       throw messageEOFException;
/*     */     }
/* 210 */     catch (IOException ioe) {
/* 211 */       JMSException jmsEx = new JMSException("Format error occured" + ioe.getMessage());
/* 212 */       jmsEx.setLinkedException(ioe);
/* 213 */       throw jmsEx;
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
/*     */   public byte readByte() throws JMSException {
/* 226 */     initializeReading();
/*     */     try {
/* 228 */       return this.dataIn.readByte();
/*     */     }
/* 230 */     catch (EOFException eof) {
/* 231 */       MessageEOFException messageEOFException = new MessageEOFException(eof.getMessage());
/* 232 */       messageEOFException.setLinkedException(eof);
/* 233 */       throw messageEOFException;
/*     */     }
/* 235 */     catch (IOException ioe) {
/* 236 */       JMSException jmsEx = new JMSException("Format error occured" + ioe.getMessage());
/* 237 */       jmsEx.setLinkedException(ioe);
/* 238 */       throw jmsEx;
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
/*     */   public int readUnsignedByte() throws JMSException {
/* 251 */     initializeReading();
/*     */     try {
/* 253 */       return this.dataIn.readUnsignedByte();
/*     */     }
/* 255 */     catch (EOFException eof) {
/* 256 */       MessageEOFException messageEOFException = new MessageEOFException(eof.getMessage());
/* 257 */       messageEOFException.setLinkedException(eof);
/* 258 */       throw messageEOFException;
/*     */     }
/* 260 */     catch (IOException ioe) {
/* 261 */       JMSException jmsEx = new JMSException("Format error occured" + ioe.getMessage());
/* 262 */       jmsEx.setLinkedException(ioe);
/* 263 */       throw jmsEx;
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
/*     */   public short readShort() throws JMSException {
/* 276 */     initializeReading();
/*     */     try {
/* 278 */       return this.dataIn.readShort();
/*     */     }
/* 280 */     catch (EOFException eof) {
/* 281 */       MessageEOFException messageEOFException = new MessageEOFException(eof.getMessage());
/* 282 */       messageEOFException.setLinkedException(eof);
/* 283 */       throw messageEOFException;
/*     */     }
/* 285 */     catch (IOException ioe) {
/* 286 */       JMSException jmsEx = new JMSException("Format error occured" + ioe.getMessage());
/* 287 */       jmsEx.setLinkedException(ioe);
/* 288 */       throw jmsEx;
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
/*     */   public int readUnsignedShort() throws JMSException {
/* 301 */     initializeReading();
/*     */     try {
/* 303 */       return this.dataIn.readUnsignedShort();
/*     */     }
/* 305 */     catch (EOFException eof) {
/* 306 */       MessageEOFException messageEOFException = new MessageEOFException(eof.getMessage());
/* 307 */       messageEOFException.setLinkedException(eof);
/* 308 */       throw messageEOFException;
/*     */     }
/* 310 */     catch (IOException ioe) {
/* 311 */       JMSException jmsEx = new JMSException("Format error occured" + ioe.getMessage());
/* 312 */       jmsEx.setLinkedException(ioe);
/* 313 */       throw jmsEx;
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
/*     */   public char readChar() throws JMSException {
/* 326 */     initializeReading();
/*     */     try {
/* 328 */       return this.dataIn.readChar();
/*     */     }
/* 330 */     catch (EOFException eof) {
/* 331 */       MessageEOFException messageEOFException = new MessageEOFException(eof.getMessage());
/* 332 */       messageEOFException.setLinkedException(eof);
/* 333 */       throw messageEOFException;
/*     */     }
/* 335 */     catch (IOException ioe) {
/* 336 */       JMSException jmsEx = new JMSException("Format error occured" + ioe.getMessage());
/* 337 */       jmsEx.setLinkedException(ioe);
/* 338 */       throw jmsEx;
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
/*     */   public int readInt() throws JMSException {
/* 351 */     initializeReading();
/*     */     try {
/* 353 */       return this.dataIn.readInt();
/*     */     }
/* 355 */     catch (EOFException eof) {
/* 356 */       MessageEOFException messageEOFException = new MessageEOFException(eof.getMessage());
/* 357 */       messageEOFException.setLinkedException(eof);
/* 358 */       throw messageEOFException;
/*     */     }
/* 360 */     catch (IOException ioe) {
/* 361 */       JMSException jmsEx = new JMSException("Format error occured" + ioe.getMessage());
/* 362 */       jmsEx.setLinkedException(ioe);
/* 363 */       throw jmsEx;
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
/*     */   public long readLong() throws JMSException {
/* 376 */     initializeReading();
/*     */     try {
/* 378 */       return this.dataIn.readLong();
/*     */     }
/* 380 */     catch (EOFException eof) {
/* 381 */       MessageEOFException messageEOFException = new MessageEOFException(eof.getMessage());
/* 382 */       messageEOFException.setLinkedException(eof);
/* 383 */       throw messageEOFException;
/*     */     }
/* 385 */     catch (IOException ioe) {
/* 386 */       JMSException jmsEx = new JMSException("Format error occured" + ioe.getMessage());
/* 387 */       jmsEx.setLinkedException(ioe);
/* 388 */       throw jmsEx;
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
/*     */   public float readFloat() throws JMSException {
/* 401 */     initializeReading();
/*     */     try {
/* 403 */       return this.dataIn.readFloat();
/*     */     }
/* 405 */     catch (EOFException eof) {
/* 406 */       MessageEOFException messageEOFException = new MessageEOFException(eof.getMessage());
/* 407 */       messageEOFException.setLinkedException(eof);
/* 408 */       throw messageEOFException;
/*     */     }
/* 410 */     catch (IOException ioe) {
/* 411 */       JMSException jmsEx = new JMSException("Format error occured" + ioe.getMessage());
/* 412 */       jmsEx.setLinkedException(ioe);
/* 413 */       throw jmsEx;
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
/*     */   public double readDouble() throws JMSException {
/* 426 */     initializeReading();
/*     */     try {
/* 428 */       return this.dataIn.readDouble();
/*     */     }
/* 430 */     catch (EOFException eof) {
/* 431 */       MessageEOFException messageEOFException = new MessageEOFException(eof.getMessage());
/* 432 */       messageEOFException.setLinkedException(eof);
/* 433 */       throw messageEOFException;
/*     */     }
/* 435 */     catch (IOException ioe) {
/* 436 */       JMSException jmsEx = new JMSException("Format error occured" + ioe.getMessage());
/* 437 */       jmsEx.setLinkedException(ioe);
/* 438 */       throw jmsEx;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String readUTF() throws JMSException {
/* 455 */     initializeReading();
/*     */     try {
/* 457 */       return this.dataIn.readUTF();
/*     */     }
/* 459 */     catch (EOFException eof) {
/* 460 */       MessageEOFException messageEOFException = new MessageEOFException(eof.getMessage());
/* 461 */       messageEOFException.setLinkedException(eof);
/* 462 */       throw messageEOFException;
/*     */     }
/* 464 */     catch (IOException ioe) {
/* 465 */       JMSException jmsEx = new JMSException("Format error occured" + ioe.getMessage());
/* 466 */       jmsEx.setLinkedException(ioe);
/* 467 */       throw jmsEx;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int readBytes(byte[] value) throws JMSException {
/* 489 */     return readBytes(value, value.length);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public int readBytes(byte[] value, int length) throws JMSException {
/* 515 */     initializeReading();
/*     */     try {
/* 517 */       int n = 0;
/* 518 */       while (n < length) {
/* 519 */         int count = this.dataIn.read(value, n, length - n);
/* 520 */         if (count < 0) {
/*     */           break;
/*     */         }
/* 523 */         n += count;
/*     */       } 
/* 525 */       if (n == 0 && length > 0) {
/* 526 */         n = -1;
/*     */       }
/* 528 */       return n;
/*     */     }
/* 530 */     catch (EOFException eof) {
/* 531 */       MessageEOFException messageEOFException = new MessageEOFException(eof.getMessage());
/* 532 */       messageEOFException.setLinkedException(eof);
/* 533 */       throw messageEOFException;
/*     */     }
/* 535 */     catch (IOException ioe) {
/* 536 */       JMSException jmsEx = new JMSException("Format error occured" + ioe.getMessage());
/* 537 */       jmsEx.setLinkedException(ioe);
/* 538 */       throw jmsEx;
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
/*     */   public void writeBoolean(boolean value) throws JMSException {
/* 551 */     initializeWriting();
/*     */     try {
/* 553 */       this.dataOut.writeBoolean(value);
/*     */     }
/* 555 */     catch (IOException ioe) {
/* 556 */       JMSException jmsEx = new JMSException("Could not write data:" + ioe.getMessage());
/* 557 */       jmsEx.setLinkedException(ioe);
/* 558 */       throw jmsEx;
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
/*     */   public void writeByte(byte value) throws JMSException {
/* 570 */     initializeWriting();
/*     */     try {
/* 572 */       this.dataOut.writeByte(value);
/*     */     }
/* 574 */     catch (IOException ioe) {
/* 575 */       JMSException jmsEx = new JMSException("Could not write data:" + ioe.getMessage());
/* 576 */       jmsEx.setLinkedException(ioe);
/* 577 */       throw jmsEx;
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
/*     */   public void writeShort(short value) throws JMSException {
/* 589 */     initializeWriting();
/*     */     try {
/* 591 */       this.dataOut.writeShort(value);
/*     */     }
/* 593 */     catch (IOException ioe) {
/* 594 */       JMSException jmsEx = new JMSException("Could not write data:" + ioe.getMessage());
/* 595 */       jmsEx.setLinkedException(ioe);
/* 596 */       throw jmsEx;
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
/*     */   public void writeChar(char value) throws JMSException {
/* 608 */     initializeWriting();
/*     */     try {
/* 610 */       this.dataOut.writeChar(value);
/*     */     }
/* 612 */     catch (IOException ioe) {
/* 613 */       JMSException jmsEx = new JMSException("Could not write data:" + ioe.getMessage());
/* 614 */       jmsEx.setLinkedException(ioe);
/* 615 */       throw jmsEx;
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
/*     */   public void writeInt(int value) throws JMSException {
/* 627 */     initializeWriting();
/*     */     try {
/* 629 */       this.dataOut.writeInt(value);
/*     */     }
/* 631 */     catch (IOException ioe) {
/* 632 */       JMSException jmsEx = new JMSException("Could not write data:" + ioe.getMessage());
/* 633 */       jmsEx.setLinkedException(ioe);
/* 634 */       throw jmsEx;
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
/*     */   public void writeLong(long value) throws JMSException {
/* 646 */     initializeWriting();
/*     */     try {
/* 648 */       this.dataOut.writeLong(value);
/*     */     }
/* 650 */     catch (IOException ioe) {
/* 651 */       JMSException jmsEx = new JMSException("Could not write data:" + ioe.getMessage());
/* 652 */       jmsEx.setLinkedException(ioe);
/* 653 */       throw jmsEx;
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
/*     */   
/*     */   public void writeFloat(float value) throws JMSException {
/* 667 */     initializeWriting();
/*     */     try {
/* 669 */       this.dataOut.writeFloat(value);
/*     */     }
/* 671 */     catch (IOException ioe) {
/* 672 */       JMSException jmsEx = new JMSException("Could not write data:" + ioe.getMessage());
/* 673 */       jmsEx.setLinkedException(ioe);
/* 674 */       throw jmsEx;
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
/*     */   
/*     */   public void writeDouble(double value) throws JMSException {
/* 688 */     initializeWriting();
/*     */     try {
/* 690 */       this.dataOut.writeDouble(value);
/*     */     }
/* 692 */     catch (IOException ioe) {
/* 693 */       JMSException jmsEx = new JMSException("Could not write data:" + ioe.getMessage());
/* 694 */       jmsEx.setLinkedException(ioe);
/* 695 */       throw jmsEx;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeUTF(String value) throws JMSException {
/* 711 */     initializeWriting();
/*     */     try {
/* 713 */       this.dataOut.writeUTF(value);
/*     */     }
/* 715 */     catch (IOException ioe) {
/* 716 */       JMSException jmsEx = new JMSException("Could not write data:" + ioe.getMessage());
/* 717 */       jmsEx.setLinkedException(ioe);
/* 718 */       throw jmsEx;
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
/*     */   public void writeBytes(byte[] value) throws JMSException {
/* 730 */     initializeWriting();
/*     */     try {
/* 732 */       this.dataOut.write(value);
/*     */     }
/* 734 */     catch (IOException ioe) {
/* 735 */       JMSException jmsEx = new JMSException("Could not write data:" + ioe.getMessage());
/* 736 */       jmsEx.setLinkedException(ioe);
/* 737 */       throw jmsEx;
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
/*     */   
/*     */   public void writeBytes(byte[] value, int offset, int length) throws JMSException {
/* 751 */     initializeWriting();
/*     */     try {
/* 753 */       this.dataOut.write(value, offset, length);
/*     */     }
/* 755 */     catch (IOException ioe) {
/* 756 */       JMSException jmsEx = new JMSException("Could not write data:" + ioe.getMessage());
/* 757 */       jmsEx.setLinkedException(ioe);
/* 758 */       throw jmsEx;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeObject(Object value) throws JMSException {
/* 775 */     if (value == null) {
/* 776 */       throw new NullPointerException();
/*     */     }
/* 778 */     initializeWriting();
/* 779 */     if (value instanceof Boolean) {
/* 780 */       writeBoolean(((Boolean)value).booleanValue());
/*     */     }
/* 782 */     else if (value instanceof Character) {
/* 783 */       writeChar(((Character)value).charValue());
/*     */     }
/* 785 */     else if (value instanceof Byte) {
/* 786 */       writeByte(((Byte)value).byteValue());
/*     */     }
/* 788 */     else if (value instanceof Short) {
/* 789 */       writeShort(((Short)value).shortValue());
/*     */     }
/* 791 */     else if (value instanceof Integer) {
/* 792 */       writeInt(((Integer)value).intValue());
/*     */     }
/* 794 */     else if (value instanceof Double) {
/* 795 */       writeDouble(((Double)value).doubleValue());
/*     */     }
/* 797 */     else if (value instanceof Long) {
/* 798 */       writeLong(((Long)value).longValue());
/*     */     }
/* 800 */     else if (value instanceof Float) {
/* 801 */       writeFloat(((Float)value).floatValue());
/*     */     }
/* 803 */     else if (value instanceof Double) {
/* 804 */       writeDouble(((Double)value).doubleValue());
/*     */     }
/* 806 */     else if (value instanceof String) {
/* 807 */       writeUTF(value.toString());
/*     */     }
/* 809 */     else if (value instanceof byte[]) {
/* 810 */       writeBytes((byte[])value);
/*     */     } else {
/*     */       
/* 813 */       throw new MessageFormatException("Cannot write non-primitive type:" + value.getClass());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() throws JMSException {
/* 823 */     this.readOnlyMessage = true;
/* 824 */     if (this.dataOut != null) {
/*     */       try {
/* 826 */         this.dataOut.flush();
/* 827 */         super.setBodyAsBytes(this.bytesOut.toByteArray());
/* 828 */         this.dataOut.close();
/*     */       }
/* 830 */       catch (IOException ioe) {
/* 831 */         JMSException jmsEx = new JMSException("reset failed: " + ioe.getMessage());
/* 832 */         jmsEx.setLinkedException(ioe);
/* 833 */         throw jmsEx;
/*     */       } 
/*     */     }
/* 836 */     this.bytesOut = null;
/* 837 */     this.dataIn = null;
/* 838 */     this.dataOut = null;
/*     */   }
/*     */   
/*     */   private void initializeWriting() throws MessageNotWriteableException {
/* 842 */     if (this.readOnlyMessage) {
/* 843 */       throw new MessageNotWriteableException("This message is in read-onlu mode");
/*     */     }
/* 845 */     if (this.dataOut == null) {
/* 846 */       this.bytesOut = new ByteArrayOutputStream();
/* 847 */       this.dataOut = new DataOutputStream(this.bytesOut);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void initializeReading() throws MessageNotReadableException {
/* 852 */     if (!this.readOnlyMessage) {
/* 853 */       throw new MessageNotReadableException("This message is in write-only mode");
/*     */     }
/*     */     try {
/* 856 */       byte[] data = super.getBodyAsBytes();
/* 857 */       if (this.dataIn == null && data != null) {
/* 858 */         ByteArrayInputStream bytesIn = new ByteArrayInputStream(data);
/* 859 */         this.dataIn = new DataInputStream(bytesIn);
/*     */       }
/*     */     
/* 862 */     } catch (IOException e) {
/* 863 */       MessageNotReadableException mnr = new MessageNotReadableException("getBodyAsBytes failed");
/* 864 */       mnr.setLinkedException(e);
/* 865 */       throw mnr;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ActiveMQBytesMessage.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */