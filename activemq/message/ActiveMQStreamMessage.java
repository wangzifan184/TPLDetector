/*      */ package org.codehaus.activemq.message;
/*      */ 
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.EOFException;
/*      */ import java.io.IOException;
/*      */ import javax.jms.JMSException;
/*      */ import javax.jms.MessageEOFException;
/*      */ import javax.jms.MessageFormatException;
/*      */ import javax.jms.MessageNotReadableException;
/*      */ import javax.jms.MessageNotWriteableException;
/*      */ import javax.jms.StreamMessage;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ActiveMQStreamMessage
/*      */   extends ActiveMQMessage
/*      */   implements StreamMessage
/*      */ {
/*      */   private DataOutputStream dataOut;
/*      */   private ByteArrayOutputStream bytesOut;
/*      */   private DataInputStream dataIn;
/*  113 */   private int bytesToRead = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getPacketType() {
/*  123 */     return 10;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ActiveMQMessage shallowCopy() throws JMSException {
/*  132 */     ActiveMQStreamMessage other = new ActiveMQStreamMessage();
/*  133 */     initializeOther(other);
/*      */     try {
/*  135 */       other.setBodyAsBytes(getBodyAsBytes());
/*      */     }
/*  137 */     catch (IOException e) {
/*  138 */       JMSException jmsEx = new JMSException("setBodyAsBytes() failed");
/*  139 */       jmsEx.setLinkedException(e);
/*  140 */       throw jmsEx;
/*      */     } 
/*  142 */     return other;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ActiveMQMessage deepCopy() throws JMSException {
/*  151 */     ActiveMQStreamMessage other = new ActiveMQStreamMessage();
/*  152 */     initializeOther(other);
/*      */     try {
/*  154 */       if (getBodyAsBytes() != null) {
/*  155 */         byte[] data = new byte[(getBodyAsBytes()).length];
/*  156 */         System.arraycopy(getBodyAsBytes(), 0, data, 0, data.length);
/*  157 */         other.setBodyAsBytes(data);
/*      */       }
/*      */     
/*  160 */     } catch (IOException e) {
/*  161 */       JMSException jmsEx = new JMSException("setBodyAsBytes() failed");
/*  162 */       jmsEx.setLinkedException(e);
/*  163 */       throw jmsEx;
/*      */     } 
/*  165 */     return other;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clearBody() throws JMSException {
/*  182 */     super.clearBody();
/*  183 */     this.dataOut = null;
/*  184 */     this.dataIn = null;
/*  185 */     this.bytesOut = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean readBoolean() throws JMSException {
/*  202 */     initializeReading();
/*      */     try {
/*  204 */       if (this.dataIn.available() == 0) {
/*  205 */         throw new MessageEOFException("reached end of data");
/*      */       }
/*  207 */       if (this.dataIn.available() < 2) {
/*  208 */         throw new MessageFormatException("Not enough data left to read value");
/*      */       }
/*      */       
/*  211 */       this.dataIn.mark(10);
/*  212 */       int type = this.dataIn.read();
/*  213 */       if (type == 5) {
/*  214 */         return this.dataIn.readBoolean();
/*      */       }
/*  216 */       if (type == 4) {
/*  217 */         return Boolean.valueOf(this.dataIn.readUTF()).booleanValue();
/*      */       }
/*      */       
/*  220 */       this.dataIn.reset();
/*  221 */       throw new MessageFormatException(" not a boolean type");
/*      */     
/*      */     }
/*  224 */     catch (EOFException e) {
/*  225 */       MessageEOFException messageEOFException = new MessageEOFException(e.getMessage());
/*  226 */       messageEOFException.setLinkedException(e);
/*  227 */       throw messageEOFException;
/*      */     }
/*  229 */     catch (IOException e) {
/*  230 */       MessageFormatException messageFormatException = new MessageFormatException(e.getMessage());
/*  231 */       messageFormatException.setLinkedException(e);
/*  232 */       throw messageFormatException;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte readByte() throws JMSException {
/*  252 */     initializeReading();
/*      */     try {
/*  254 */       if (this.dataIn.available() == 0) {
/*  255 */         throw new MessageEOFException("reached end of data");
/*      */       }
/*  257 */       if (this.dataIn.available() < 2) {
/*  258 */         throw new MessageFormatException("Not enough data left to read value");
/*      */       }
/*      */       
/*  261 */       this.dataIn.mark(10);
/*  262 */       int type = this.dataIn.read();
/*  263 */       if (type == 7) {
/*  264 */         return this.dataIn.readByte();
/*      */       }
/*  266 */       if (type == 4) {
/*  267 */         return Byte.valueOf(this.dataIn.readUTF()).byteValue();
/*      */       }
/*      */       
/*  270 */       this.dataIn.reset();
/*  271 */       throw new MessageFormatException(" not a byte type");
/*      */     
/*      */     }
/*  274 */     catch (NumberFormatException mfe) {
/*      */       try {
/*  276 */         this.dataIn.reset();
/*      */       }
/*  278 */       catch (IOException ioe) {
/*  279 */         JMSException jmsEx = new JMSException("reset failed");
/*  280 */         jmsEx.setLinkedException(ioe);
/*      */       } 
/*  282 */       throw mfe;
/*      */     
/*      */     }
/*  285 */     catch (EOFException e) {
/*  286 */       MessageEOFException messageEOFException = new MessageEOFException(e.getMessage());
/*  287 */       messageEOFException.setLinkedException(e);
/*  288 */       throw messageEOFException;
/*      */     }
/*  290 */     catch (IOException e) {
/*  291 */       MessageFormatException messageFormatException = new MessageFormatException(e.getMessage());
/*  292 */       messageFormatException.setLinkedException(e);
/*  293 */       throw messageFormatException;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short readShort() throws JMSException {
/*  312 */     initializeReading();
/*      */     try {
/*  314 */       if (this.dataIn.available() == 0) {
/*  315 */         throw new MessageEOFException("reached end of data");
/*      */       }
/*  317 */       if (this.dataIn.available() < 2) {
/*  318 */         throw new MessageFormatException("Not enough data left to read value");
/*      */       }
/*      */       
/*  321 */       this.dataIn.mark(17);
/*  322 */       int type = this.dataIn.read();
/*  323 */       if (type == 8) {
/*  324 */         return this.dataIn.readShort();
/*      */       }
/*  326 */       if (type == 7) {
/*  327 */         return (short)this.dataIn.readByte();
/*      */       }
/*  329 */       if (type == 4) {
/*  330 */         return Short.valueOf(this.dataIn.readUTF()).shortValue();
/*      */       }
/*      */       
/*  333 */       this.dataIn.reset();
/*  334 */       throw new MessageFormatException(" not a short type");
/*      */     
/*      */     }
/*  337 */     catch (NumberFormatException mfe) {
/*      */       try {
/*  339 */         this.dataIn.reset();
/*      */       }
/*  341 */       catch (IOException ioe) {
/*  342 */         JMSException jmsEx = new JMSException("reset failed");
/*  343 */         jmsEx.setLinkedException(ioe);
/*      */       } 
/*  345 */       throw mfe;
/*      */     
/*      */     }
/*  348 */     catch (EOFException e) {
/*  349 */       MessageEOFException messageEOFException = new MessageEOFException(e.getMessage());
/*  350 */       messageEOFException.setLinkedException(e);
/*  351 */       throw messageEOFException;
/*      */     }
/*  353 */     catch (IOException e) {
/*  354 */       MessageFormatException messageFormatException = new MessageFormatException(e.getMessage());
/*  355 */       messageFormatException.setLinkedException(e);
/*  356 */       throw messageFormatException;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public char readChar() throws JMSException {
/*  376 */     initializeReading();
/*      */     try {
/*  378 */       if (this.dataIn.available() == 0) {
/*  379 */         throw new MessageEOFException("reached end of data");
/*      */       }
/*  381 */       if (this.dataIn.available() < 2) {
/*  382 */         throw new MessageFormatException("Not enough data left to read value");
/*      */       }
/*      */       
/*  385 */       this.dataIn.mark(17);
/*  386 */       int type = this.dataIn.read();
/*  387 */       if (type == 6) {
/*  388 */         return this.dataIn.readChar();
/*      */       }
/*      */       
/*  391 */       this.dataIn.reset();
/*  392 */       throw new MessageFormatException(" not a char type");
/*      */     
/*      */     }
/*  395 */     catch (NumberFormatException mfe) {
/*      */       try {
/*  397 */         this.dataIn.reset();
/*      */       }
/*  399 */       catch (IOException ioe) {
/*  400 */         JMSException jmsEx = new JMSException("reset failed");
/*  401 */         jmsEx.setLinkedException(ioe);
/*      */       } 
/*  403 */       throw mfe;
/*      */     
/*      */     }
/*  406 */     catch (EOFException e) {
/*  407 */       MessageEOFException messageEOFException = new MessageEOFException(e.getMessage());
/*  408 */       messageEOFException.setLinkedException(e);
/*  409 */       throw messageEOFException;
/*      */     }
/*  411 */     catch (IOException e) {
/*  412 */       MessageFormatException messageFormatException = new MessageFormatException(e.getMessage());
/*  413 */       messageFormatException.setLinkedException(e);
/*  414 */       throw messageFormatException;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int readInt() throws JMSException {
/*  434 */     initializeReading();
/*      */     try {
/*  436 */       if (this.dataIn.available() == 0) {
/*  437 */         throw new MessageEOFException("reached end of data");
/*      */       }
/*  439 */       if (this.dataIn.available() < 2) {
/*  440 */         throw new MessageFormatException("Not enough data left to read value");
/*      */       }
/*      */       
/*  443 */       this.dataIn.mark(33);
/*  444 */       int type = this.dataIn.read();
/*  445 */       if (type == 9) {
/*  446 */         return this.dataIn.readInt();
/*      */       }
/*  448 */       if (type == 8) {
/*  449 */         return this.dataIn.readShort();
/*      */       }
/*  451 */       if (type == 7) {
/*  452 */         return this.dataIn.readByte();
/*      */       }
/*  454 */       if (type == 4) {
/*  455 */         return Integer.valueOf(this.dataIn.readUTF()).intValue();
/*      */       }
/*      */       
/*  458 */       this.dataIn.reset();
/*  459 */       throw new MessageFormatException(" not an int type");
/*      */     
/*      */     }
/*  462 */     catch (NumberFormatException mfe) {
/*      */       try {
/*  464 */         this.dataIn.reset();
/*      */       }
/*  466 */       catch (IOException ioe) {
/*  467 */         JMSException jmsEx = new JMSException("reset failed");
/*  468 */         jmsEx.setLinkedException(ioe);
/*      */       } 
/*  470 */       throw mfe;
/*      */     
/*      */     }
/*  473 */     catch (EOFException e) {
/*  474 */       MessageEOFException messageEOFException = new MessageEOFException(e.getMessage());
/*  475 */       messageEOFException.setLinkedException(e);
/*  476 */       throw messageEOFException;
/*      */     }
/*  478 */     catch (IOException e) {
/*  479 */       MessageFormatException messageFormatException = new MessageFormatException(e.getMessage());
/*  480 */       messageFormatException.setLinkedException(e);
/*  481 */       throw messageFormatException;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long readLong() throws JMSException {
/*  501 */     initializeReading();
/*      */     try {
/*  503 */       if (this.dataIn.available() == 0) {
/*  504 */         throw new MessageEOFException("reached end of data");
/*      */       }
/*  506 */       if (this.dataIn.available() < 2) {
/*  507 */         throw new MessageFormatException("Not enough data left to read value");
/*      */       }
/*      */       
/*  510 */       this.dataIn.mark(65);
/*  511 */       int type = this.dataIn.read();
/*  512 */       if (type == 10) {
/*  513 */         return this.dataIn.readLong();
/*      */       }
/*  515 */       if (type == 9) {
/*  516 */         return this.dataIn.readInt();
/*      */       }
/*  518 */       if (type == 8) {
/*  519 */         return this.dataIn.readShort();
/*      */       }
/*  521 */       if (type == 7) {
/*  522 */         return this.dataIn.readByte();
/*      */       }
/*  524 */       if (type == 4) {
/*  525 */         return Long.valueOf(this.dataIn.readUTF()).longValue();
/*      */       }
/*      */       
/*  528 */       this.dataIn.reset();
/*  529 */       throw new MessageFormatException(" not a long type");
/*      */     
/*      */     }
/*  532 */     catch (NumberFormatException mfe) {
/*      */       try {
/*  534 */         this.dataIn.reset();
/*      */       }
/*  536 */       catch (IOException ioe) {
/*  537 */         JMSException jmsEx = new JMSException("reset failed");
/*  538 */         jmsEx.setLinkedException(ioe);
/*      */       } 
/*  540 */       throw mfe;
/*      */     
/*      */     }
/*  543 */     catch (EOFException e) {
/*  544 */       MessageEOFException messageEOFException = new MessageEOFException(e.getMessage());
/*  545 */       messageEOFException.setLinkedException(e);
/*  546 */       throw messageEOFException;
/*      */     }
/*  548 */     catch (IOException e) {
/*  549 */       MessageFormatException messageFormatException = new MessageFormatException(e.getMessage());
/*  550 */       messageFormatException.setLinkedException(e);
/*  551 */       throw messageFormatException;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float readFloat() throws JMSException {
/*  570 */     initializeReading();
/*      */     try {
/*  572 */       if (this.dataIn.available() == 0) {
/*  573 */         throw new MessageEOFException("reached end of data");
/*      */       }
/*  575 */       if (this.dataIn.available() < 2) {
/*  576 */         throw new MessageFormatException("Not enough data left to read value");
/*      */       }
/*      */       
/*  579 */       this.dataIn.mark(33);
/*  580 */       int type = this.dataIn.read();
/*  581 */       if (type == 11) {
/*  582 */         return this.dataIn.readFloat();
/*      */       }
/*  584 */       if (type == 4) {
/*  585 */         return Float.valueOf(this.dataIn.readUTF()).floatValue();
/*      */       }
/*      */       
/*  588 */       this.dataIn.reset();
/*  589 */       throw new MessageFormatException(" not a float type");
/*      */     
/*      */     }
/*  592 */     catch (NumberFormatException mfe) {
/*      */       try {
/*  594 */         this.dataIn.reset();
/*      */       }
/*  596 */       catch (IOException ioe) {
/*  597 */         JMSException jmsEx = new JMSException("reset failed");
/*  598 */         jmsEx.setLinkedException(ioe);
/*      */       } 
/*  600 */       throw mfe;
/*      */     
/*      */     }
/*  603 */     catch (EOFException e) {
/*  604 */       MessageEOFException messageEOFException = new MessageEOFException(e.getMessage());
/*  605 */       messageEOFException.setLinkedException(e);
/*  606 */       throw messageEOFException;
/*      */     }
/*  608 */     catch (IOException e) {
/*  609 */       MessageFormatException messageFormatException = new MessageFormatException(e.getMessage());
/*  610 */       messageFormatException.setLinkedException(e);
/*  611 */       throw messageFormatException;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double readDouble() throws JMSException {
/*  630 */     initializeReading();
/*      */     try {
/*  632 */       if (this.dataIn.available() == 0) {
/*  633 */         throw new MessageEOFException("reached end of data");
/*      */       }
/*  635 */       if (this.dataIn.available() < 2) {
/*  636 */         throw new MessageFormatException("Not enough data left to read value");
/*      */       }
/*      */       
/*  639 */       this.dataIn.mark(65);
/*  640 */       int type = this.dataIn.read();
/*  641 */       if (type == 12) {
/*  642 */         return this.dataIn.readDouble();
/*      */       }
/*  644 */       if (type == 11) {
/*  645 */         return this.dataIn.readFloat();
/*      */       }
/*  647 */       if (type == 4) {
/*  648 */         return Double.valueOf(this.dataIn.readUTF()).doubleValue();
/*      */       }
/*      */       
/*  651 */       this.dataIn.reset();
/*  652 */       throw new MessageFormatException(" not a double type");
/*      */     
/*      */     }
/*  655 */     catch (NumberFormatException mfe) {
/*      */       try {
/*  657 */         this.dataIn.reset();
/*      */       }
/*  659 */       catch (IOException ioe) {
/*  660 */         JMSException jmsEx = new JMSException("reset failed");
/*  661 */         jmsEx.setLinkedException(ioe);
/*      */       } 
/*  663 */       throw mfe;
/*      */     
/*      */     }
/*  666 */     catch (EOFException e) {
/*  667 */       MessageEOFException messageEOFException = new MessageEOFException(e.getMessage());
/*  668 */       messageEOFException.setLinkedException(e);
/*  669 */       throw messageEOFException;
/*      */     }
/*  671 */     catch (IOException e) {
/*  672 */       MessageFormatException messageFormatException = new MessageFormatException(e.getMessage());
/*  673 */       messageFormatException.setLinkedException(e);
/*  674 */       throw messageFormatException;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String readString() throws JMSException {
/*  693 */     initializeReading();
/*      */     try {
/*  695 */       if (this.dataIn.available() == 0) {
/*  696 */         throw new MessageEOFException("reached end of data");
/*      */       }
/*  698 */       if (this.dataIn.available() < 2) {
/*  699 */         throw new MessageFormatException("Not enough data left to read value");
/*      */       }
/*      */       
/*  702 */       this.dataIn.mark(65);
/*  703 */       int type = this.dataIn.read();
/*  704 */       if (type == 13) {
/*  705 */         return null;
/*      */       }
/*  707 */       if (type == 4) {
/*  708 */         return this.dataIn.readUTF();
/*      */       }
/*  710 */       if (type == 10) {
/*  711 */         return (new Long(this.dataIn.readLong())).toString();
/*      */       }
/*  713 */       if (type == 9) {
/*  714 */         return (new Integer(this.dataIn.readInt())).toString();
/*      */       }
/*  716 */       if (type == 8) {
/*  717 */         return (new Short(this.dataIn.readShort())).toString();
/*      */       }
/*  719 */       if (type == 7) {
/*  720 */         return (new Byte(this.dataIn.readByte())).toString();
/*      */       }
/*  722 */       if (type == 11) {
/*  723 */         return (new Float(this.dataIn.readFloat())).toString();
/*      */       }
/*  725 */       if (type == 12) {
/*  726 */         return (new Double(this.dataIn.readDouble())).toString();
/*      */       }
/*  728 */       if (type == 5) {
/*  729 */         return (this.dataIn.readBoolean() ? Boolean.TRUE : Boolean.FALSE).toString();
/*      */       }
/*  731 */       if (type == 6) {
/*  732 */         return (new Character(this.dataIn.readChar())).toString();
/*      */       }
/*      */       
/*  735 */       this.dataIn.reset();
/*  736 */       throw new MessageFormatException(" not a String type");
/*      */     
/*      */     }
/*  739 */     catch (NumberFormatException mfe) {
/*      */       try {
/*  741 */         this.dataIn.reset();
/*      */       }
/*  743 */       catch (IOException ioe) {
/*  744 */         JMSException jmsEx = new JMSException("reset failed");
/*  745 */         jmsEx.setLinkedException(ioe);
/*      */       } 
/*  747 */       throw mfe;
/*      */     
/*      */     }
/*  750 */     catch (EOFException e) {
/*  751 */       MessageEOFException messageEOFException = new MessageEOFException(e.getMessage());
/*  752 */       messageEOFException.setLinkedException(e);
/*  753 */       throw messageEOFException;
/*      */     }
/*  755 */     catch (IOException e) {
/*  756 */       MessageFormatException messageFormatException = new MessageFormatException(e.getMessage());
/*  757 */       messageFormatException.setLinkedException(e);
/*  758 */       throw messageFormatException;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int readBytes(byte[] value) throws JMSException {
/*  807 */     initializeReading();
/*      */     try {
/*  809 */       if (value == null) {
/*  810 */         throw new NullPointerException();
/*      */       }
/*  812 */       if (this.bytesToRead == 0) {
/*  813 */         this.bytesToRead = -1;
/*  814 */         return -1;
/*      */       } 
/*  816 */       if (this.bytesToRead > 0) {
/*  817 */         if (value.length >= this.bytesToRead) {
/*  818 */           this.bytesToRead = 0;
/*  819 */           return this.dataIn.read(value, 0, this.bytesToRead);
/*      */         } 
/*      */         
/*  822 */         this.bytesToRead -= value.length;
/*  823 */         return this.dataIn.read(value);
/*      */       } 
/*      */ 
/*      */       
/*  827 */       if (this.dataIn.available() == 0) {
/*  828 */         throw new MessageEOFException("reached end of data");
/*      */       }
/*  830 */       if (this.dataIn.available() < 1) {
/*  831 */         throw new MessageFormatException("Not enough data left to read value");
/*      */       }
/*  833 */       this.dataIn.mark(value.length + 1);
/*  834 */       int type = this.dataIn.read();
/*  835 */       if (this.dataIn.available() < 1) {
/*  836 */         return -1;
/*      */       }
/*  838 */       if (type != 3) {
/*  839 */         throw new MessageFormatException("Not a byte array");
/*      */       }
/*  841 */       int len = this.dataIn.readInt();
/*      */       
/*  843 */       if (len >= value.length) {
/*  844 */         this.bytesToRead = len - value.length;
/*  845 */         return this.dataIn.read(value);
/*      */       } 
/*      */       
/*  848 */       this.bytesToRead = 0;
/*  849 */       return this.dataIn.read(value, 0, len);
/*      */ 
/*      */     
/*      */     }
/*  853 */     catch (EOFException e) {
/*  854 */       MessageEOFException messageEOFException = new MessageEOFException(e.getMessage());
/*  855 */       messageEOFException.setLinkedException(e);
/*  856 */       throw messageEOFException;
/*      */     }
/*  858 */     catch (IOException e) {
/*  859 */       MessageFormatException messageFormatException = new MessageFormatException(e.getMessage());
/*  860 */       messageFormatException.setLinkedException(e);
/*  861 */       throw messageFormatException;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object readObject() throws JMSException {
/*  897 */     initializeReading();
/*      */     try {
/*  899 */       if (this.dataIn.available() == 0) {
/*  900 */         throw new MessageEOFException("reached end of data");
/*      */       }
/*  902 */       if (this.dataIn.available() < 1) {
/*  903 */         throw new MessageFormatException("Not enough data left to read value - avaialable = " + this.dataIn.available());
/*      */       }
/*      */       
/*  906 */       this.dataIn.mark(65);
/*  907 */       int type = this.dataIn.read();
/*  908 */       if (type == 13) {
/*  909 */         return null;
/*      */       }
/*  911 */       if (type == 4) {
/*  912 */         return this.dataIn.readUTF();
/*      */       }
/*  914 */       if (type == 10) {
/*  915 */         return new Long(this.dataIn.readLong());
/*      */       }
/*  917 */       if (type == 9) {
/*  918 */         return new Integer(this.dataIn.readInt());
/*      */       }
/*  920 */       if (type == 8) {
/*  921 */         return new Short(this.dataIn.readShort());
/*      */       }
/*  923 */       if (type == 7) {
/*  924 */         return new Byte(this.dataIn.readByte());
/*      */       }
/*  926 */       if (type == 11) {
/*  927 */         return new Float(this.dataIn.readFloat());
/*      */       }
/*  929 */       if (type == 12) {
/*  930 */         return new Double(this.dataIn.readDouble());
/*      */       }
/*  932 */       if (type == 5) {
/*  933 */         return this.dataIn.readBoolean() ? Boolean.TRUE : Boolean.FALSE;
/*      */       }
/*  935 */       if (type == 6) {
/*  936 */         return new Character(this.dataIn.readChar());
/*      */       }
/*  938 */       if (type == 3) {
/*  939 */         int len = this.dataIn.readInt();
/*  940 */         byte[] value = new byte[len];
/*  941 */         this.dataIn.read(value);
/*  942 */         return value;
/*      */       } 
/*      */       
/*  945 */       this.dataIn.reset();
/*  946 */       throw new MessageFormatException("unknown type");
/*      */     
/*      */     }
/*  949 */     catch (NumberFormatException mfe) {
/*      */       try {
/*  951 */         this.dataIn.reset();
/*      */       }
/*  953 */       catch (IOException ioe) {
/*  954 */         JMSException jmsEx = new JMSException("reset failed");
/*  955 */         jmsEx.setLinkedException(ioe);
/*      */       } 
/*  957 */       throw mfe;
/*      */     
/*      */     }
/*  960 */     catch (EOFException e) {
/*  961 */       MessageEOFException messageEOFException = new MessageEOFException(e.getMessage());
/*  962 */       messageEOFException.setLinkedException(e);
/*  963 */       throw messageEOFException;
/*      */     }
/*  965 */     catch (IOException e) {
/*  966 */       MessageFormatException messageFormatException = new MessageFormatException(e.getMessage());
/*  967 */       messageFormatException.setLinkedException(e);
/*  968 */       throw messageFormatException;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeBoolean(boolean value) throws JMSException {
/*  987 */     initializeWriting();
/*      */     try {
/*  989 */       this.dataOut.write(5);
/*  990 */       this.dataOut.writeBoolean(value);
/*      */     }
/*  992 */     catch (IOException ioe) {
/*  993 */       JMSException jmsEx = new JMSException(ioe.getMessage());
/*  994 */       jmsEx.setLinkedException(ioe);
/*  995 */       throw jmsEx;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeByte(byte value) throws JMSException {
/* 1011 */     initializeWriting();
/*      */     try {
/* 1013 */       this.dataOut.write(7);
/* 1014 */       this.dataOut.writeByte(value);
/*      */     }
/* 1016 */     catch (IOException ioe) {
/* 1017 */       JMSException jmsEx = new JMSException(ioe.getMessage());
/* 1018 */       jmsEx.setLinkedException(ioe);
/* 1019 */       throw jmsEx;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeShort(short value) throws JMSException {
/* 1035 */     initializeWriting();
/*      */     try {
/* 1037 */       this.dataOut.write(8);
/* 1038 */       this.dataOut.writeShort(value);
/*      */     }
/* 1040 */     catch (IOException ioe) {
/* 1041 */       JMSException jmsEx = new JMSException(ioe.getMessage());
/* 1042 */       jmsEx.setLinkedException(ioe);
/* 1043 */       throw jmsEx;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeChar(char value) throws JMSException {
/* 1059 */     initializeWriting();
/*      */     try {
/* 1061 */       this.dataOut.write(6);
/* 1062 */       this.dataOut.writeChar(value);
/*      */     }
/* 1064 */     catch (IOException ioe) {
/* 1065 */       JMSException jmsEx = new JMSException(ioe.getMessage());
/* 1066 */       jmsEx.setLinkedException(ioe);
/* 1067 */       throw jmsEx;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeInt(int value) throws JMSException {
/* 1083 */     initializeWriting();
/*      */     try {
/* 1085 */       this.dataOut.write(9);
/* 1086 */       this.dataOut.writeInt(value);
/*      */     }
/* 1088 */     catch (IOException ioe) {
/* 1089 */       JMSException jmsEx = new JMSException(ioe.getMessage());
/* 1090 */       jmsEx.setLinkedException(ioe);
/* 1091 */       throw jmsEx;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeLong(long value) throws JMSException {
/* 1107 */     initializeWriting();
/*      */     try {
/* 1109 */       this.dataOut.write(10);
/* 1110 */       this.dataOut.writeLong(value);
/*      */     }
/* 1112 */     catch (IOException ioe) {
/* 1113 */       JMSException jmsEx = new JMSException(ioe.getMessage());
/* 1114 */       jmsEx.setLinkedException(ioe);
/* 1115 */       throw jmsEx;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeFloat(float value) throws JMSException {
/* 1131 */     initializeWriting();
/*      */     try {
/* 1133 */       this.dataOut.write(11);
/* 1134 */       this.dataOut.writeFloat(value);
/*      */     }
/* 1136 */     catch (IOException ioe) {
/* 1137 */       JMSException jmsEx = new JMSException(ioe.getMessage());
/* 1138 */       jmsEx.setLinkedException(ioe);
/* 1139 */       throw jmsEx;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeDouble(double value) throws JMSException {
/* 1155 */     initializeWriting();
/*      */     try {
/* 1157 */       this.dataOut.write(12);
/* 1158 */       this.dataOut.writeDouble(value);
/*      */     }
/* 1160 */     catch (IOException ioe) {
/* 1161 */       JMSException jmsEx = new JMSException(ioe.getMessage());
/* 1162 */       jmsEx.setLinkedException(ioe);
/* 1163 */       throw jmsEx;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeString(String value) throws JMSException {
/* 1179 */     initializeWriting();
/*      */     try {
/* 1181 */       if (value == null) {
/* 1182 */         this.dataOut.write(13);
/*      */       } else {
/*      */         
/* 1185 */         this.dataOut.write(4);
/* 1186 */         this.dataOut.writeUTF(value);
/*      */       }
/*      */     
/* 1189 */     } catch (IOException ioe) {
/* 1190 */       JMSException jmsEx = new JMSException(ioe.getMessage());
/* 1191 */       jmsEx.setLinkedException(ioe);
/* 1192 */       throw jmsEx;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeBytes(byte[] value) throws JMSException {
/* 1212 */     writeBytes(value, 0, value.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeBytes(byte[] value, int offset, int length) throws JMSException {
/* 1235 */     initializeWriting();
/*      */     try {
/* 1237 */       this.dataOut.write(3);
/* 1238 */       this.dataOut.writeInt(length);
/* 1239 */       this.dataOut.write(value, offset, length);
/*      */     }
/* 1241 */     catch (IOException ioe) {
/* 1242 */       JMSException jmsEx = new JMSException(ioe.getMessage());
/* 1243 */       jmsEx.setLinkedException(ioe);
/* 1244 */       throw jmsEx;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeObject(Object value) throws JMSException {
/* 1266 */     initializeWriting();
/* 1267 */     if (value == null) {
/*      */       try {
/* 1269 */         this.dataOut.write(13);
/*      */       }
/* 1271 */       catch (IOException ioe) {
/* 1272 */         JMSException jmsEx = new JMSException(ioe.getMessage());
/* 1273 */         jmsEx.setLinkedException(ioe);
/* 1274 */         throw jmsEx;
/*      */       }
/*      */     
/* 1277 */     } else if (value instanceof String) {
/* 1278 */       writeString(value.toString());
/*      */     }
/* 1280 */     else if (value instanceof Character) {
/* 1281 */       writeChar(((Character)value).charValue());
/*      */     }
/* 1283 */     else if (value instanceof Boolean) {
/* 1284 */       writeBoolean(((Boolean)value).booleanValue());
/*      */     }
/* 1286 */     else if (value instanceof Byte) {
/* 1287 */       writeByte(((Byte)value).byteValue());
/*      */     }
/* 1289 */     else if (value instanceof Short) {
/* 1290 */       writeShort(((Short)value).shortValue());
/*      */     }
/* 1292 */     else if (value instanceof Integer) {
/* 1293 */       writeInt(((Integer)value).intValue());
/*      */     }
/* 1295 */     else if (value instanceof Float) {
/* 1296 */       writeFloat(((Float)value).floatValue());
/*      */     }
/* 1298 */     else if (value instanceof Double) {
/* 1299 */       writeDouble(((Double)value).doubleValue());
/*      */     }
/* 1301 */     else if (value instanceof byte[]) {
/* 1302 */       writeBytes((byte[])value);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void reset() throws JMSException {
/* 1315 */     this.readOnlyMessage = true;
/* 1316 */     if (this.dataOut != null) {
/*      */       try {
/* 1318 */         this.dataOut.flush();
/* 1319 */         super.setBodyAsBytes(this.bytesOut.toByteArray());
/* 1320 */         this.dataOut.close();
/*      */       }
/* 1322 */       catch (IOException ioe) {
/* 1323 */         JMSException jmsEx = new JMSException("reset failed: " + ioe.getMessage());
/* 1324 */         jmsEx.setLinkedException(ioe);
/* 1325 */         throw jmsEx;
/*      */       } 
/*      */     }
/* 1328 */     this.bytesOut = null;
/* 1329 */     this.dataIn = null;
/* 1330 */     this.dataOut = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBodyAsBytes(byte[] bodyAsBytes) {
/* 1337 */     super.setBodyAsBytes(bodyAsBytes);
/* 1338 */     this.dataOut = null;
/* 1339 */     this.dataIn = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getBodyAsBytes() throws IOException {
/* 1347 */     if (this.dataOut != null) {
/* 1348 */       this.dataOut.flush();
/* 1349 */       super.setBodyAsBytes(this.bytesOut.toByteArray());
/* 1350 */       this.dataOut.close();
/* 1351 */       this.dataOut = null;
/*      */     } 
/* 1353 */     return super.getBodyAsBytes();
/*      */   }
/*      */ 
/*      */   
/*      */   private void initializeWriting() throws MessageNotWriteableException {
/* 1358 */     if (this.readOnlyMessage) {
/* 1359 */       throw new MessageNotWriteableException("This message is in read-only mode");
/*      */     }
/* 1361 */     if (this.dataOut == null) {
/* 1362 */       this.bytesOut = new ByteArrayOutputStream();
/* 1363 */       this.dataOut = new DataOutputStream(this.bytesOut);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void initializeReading() throws MessageNotReadableException {
/* 1369 */     if (!this.readOnlyMessage) {
/* 1370 */       throw new MessageNotReadableException("This message is in write-only mode");
/*      */     }
/*      */     try {
/* 1373 */       byte[] data = super.getBodyAsBytes();
/* 1374 */       if (this.dataIn == null && data != null) {
/* 1375 */         ByteArrayInputStream bytesIn = new ByteArrayInputStream(data);
/* 1376 */         this.dataIn = new DataInputStream(bytesIn);
/*      */       }
/*      */     
/* 1379 */     } catch (IOException e) {
/* 1380 */       MessageNotReadableException mnr = new MessageNotReadableException("getBodyAsBytes failed");
/* 1381 */       mnr.setLinkedException(e);
/* 1382 */       throw mnr;
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ActiveMQStreamMessage.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */