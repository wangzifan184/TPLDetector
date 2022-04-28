/*     */ package org.codehaus.activemq.message;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.MapMessage;
/*     */ import javax.jms.MessageFormatException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ActiveMQMapMessage
/*     */   extends ActiveMQMessage
/*     */   implements MapMessage
/*     */ {
/*     */   private Hashtable theTable;
/*     */   
/*     */   public int getPacketType() {
/*  81 */     return 11;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQMessage shallowCopy() throws JMSException {
/*  89 */     ActiveMQMapMessage other = new ActiveMQMapMessage();
/*  90 */     initializeOther(other);
/*  91 */     other.theTable = this.theTable;
/*  92 */     return other;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQMessage deepCopy() throws JMSException {
/* 100 */     ActiveMQMapMessage other = new ActiveMQMapMessage();
/* 101 */     initializeOther(other);
/* 102 */     if (this.theTable != null) {
/* 103 */       other.theTable = (Hashtable)this.theTable.clone();
/*     */     }
/* 105 */     return other;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTable(Hashtable newTable) {
/* 114 */     this.theTable = newTable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Hashtable getTable() throws JMSException {
/* 122 */     if (this.theTable == null) {
/*     */       try {
/* 124 */         buildBodyFromBytes();
/*     */       }
/* 126 */       catch (IOException ioe) {
/* 127 */         JMSException jmsEx = new JMSException("building table from data failed");
/* 128 */         jmsEx.setLinkedException(ioe);
/* 129 */         throw jmsEx;
/*     */       } 
/*     */     }
/* 132 */     if (this.theTable == null) {
/* 133 */       this.theTable = new Hashtable();
/*     */     }
/* 135 */     return this.theTable;
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
/* 147 */     super.clearBody();
/* 148 */     getTable().clear();
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
/*     */   public boolean getBoolean(String name) throws JMSException {
/* 160 */     Object value = getTable().get(name);
/* 161 */     if (value == null) {
/* 162 */       return false;
/*     */     }
/* 164 */     if (value instanceof Boolean) {
/* 165 */       return ((Boolean)value).booleanValue();
/*     */     }
/* 167 */     if (value instanceof String) {
/* 168 */       return Boolean.valueOf(value.toString()).booleanValue();
/*     */     }
/*     */     
/* 171 */     throw new MessageFormatException(" cannot read a boolean from " + value.getClass().getName());
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
/*     */   public byte getByte(String name) throws JMSException {
/* 184 */     Object value = getTable().get(name);
/* 185 */     if (value == null) {
/* 186 */       return 0;
/*     */     }
/* 188 */     if (value instanceof Byte) {
/* 189 */       return ((Byte)value).byteValue();
/*     */     }
/* 191 */     if (value instanceof String) {
/* 192 */       return Byte.valueOf(value.toString()).byteValue();
/*     */     }
/*     */     
/* 195 */     throw new MessageFormatException(" cannot read a byte from " + value.getClass().getName());
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
/*     */   public short getShort(String name) throws JMSException {
/* 208 */     Object value = getTable().get(name);
/* 209 */     if (value == null) {
/* 210 */       return 0;
/*     */     }
/* 212 */     if (value instanceof Short) {
/* 213 */       return ((Short)value).shortValue();
/*     */     }
/* 215 */     if (value instanceof Byte) {
/* 216 */       return ((Byte)value).shortValue();
/*     */     }
/* 218 */     if (value instanceof String) {
/* 219 */       return Short.valueOf(value.toString()).shortValue();
/*     */     }
/*     */     
/* 222 */     throw new MessageFormatException(" cannot read a short from " + value.getClass().getName());
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
/*     */   public char getChar(String name) throws JMSException {
/* 235 */     Object value = getTable().get(name);
/* 236 */     if (value == null) {
/* 237 */       throw new NullPointerException();
/*     */     }
/* 239 */     if (value instanceof Character) {
/* 240 */       return ((Character)value).charValue();
/*     */     }
/*     */     
/* 243 */     throw new MessageFormatException(" cannot read a short from " + value.getClass().getName());
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
/*     */   public int getInt(String name) throws JMSException {
/* 256 */     Object value = getTable().get(name);
/* 257 */     if (value == null) {
/* 258 */       return 0;
/*     */     }
/* 260 */     if (value instanceof Integer) {
/* 261 */       return ((Integer)value).intValue();
/*     */     }
/* 263 */     if (value instanceof Short) {
/* 264 */       return ((Short)value).intValue();
/*     */     }
/* 266 */     if (value instanceof Byte) {
/* 267 */       return ((Byte)value).intValue();
/*     */     }
/* 269 */     if (value instanceof String) {
/* 270 */       return Integer.valueOf(value.toString()).intValue();
/*     */     }
/*     */     
/* 273 */     throw new MessageFormatException(" cannot read an int from " + value.getClass().getName());
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
/*     */   public long getLong(String name) throws JMSException {
/* 286 */     Object value = getTable().get(name);
/* 287 */     if (value == null) {
/* 288 */       return 0L;
/*     */     }
/* 290 */     if (value instanceof Long) {
/* 291 */       return ((Long)value).longValue();
/*     */     }
/* 293 */     if (value instanceof Integer) {
/* 294 */       return ((Integer)value).longValue();
/*     */     }
/* 296 */     if (value instanceof Short) {
/* 297 */       return ((Short)value).longValue();
/*     */     }
/* 299 */     if (value instanceof Byte) {
/* 300 */       return ((Byte)value).longValue();
/*     */     }
/* 302 */     if (value instanceof String) {
/* 303 */       return Long.valueOf(value.toString()).longValue();
/*     */     }
/*     */     
/* 306 */     throw new MessageFormatException(" cannot read a long from " + value.getClass().getName());
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
/*     */   public float getFloat(String name) throws JMSException {
/* 319 */     Object value = getTable().get(name);
/* 320 */     if (value == null) {
/* 321 */       return 0.0F;
/*     */     }
/* 323 */     if (value instanceof Float) {
/* 324 */       return ((Float)value).floatValue();
/*     */     }
/* 326 */     if (value instanceof String) {
/* 327 */       return Float.valueOf(value.toString()).floatValue();
/*     */     }
/*     */     
/* 330 */     throw new MessageFormatException(" cannot read a float from " + value.getClass().getName());
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
/*     */   public double getDouble(String name) throws JMSException {
/* 343 */     Object value = getTable().get(name);
/* 344 */     if (value == null) {
/* 345 */       return 0.0D;
/*     */     }
/* 347 */     if (value instanceof Double) {
/* 348 */       return ((Double)value).doubleValue();
/*     */     }
/* 350 */     if (value instanceof Float) {
/* 351 */       return ((Float)value).floatValue();
/*     */     }
/* 353 */     if (value instanceof String) {
/* 354 */       return Float.valueOf(value.toString()).floatValue();
/*     */     }
/*     */     
/* 357 */     throw new MessageFormatException(" cannot read a double from " + value.getClass().getName());
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
/*     */   public String getString(String name) throws JMSException {
/* 371 */     Object value = getTable().get(name);
/* 372 */     if (value == null) {
/* 373 */       return null;
/*     */     }
/* 375 */     if (value instanceof byte[]) {
/* 376 */       throw new MessageFormatException("Use getBytes to read a byte array");
/*     */     }
/*     */     
/* 379 */     return value.toString();
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
/*     */   public byte[] getBytes(String name) throws JMSException {
/* 393 */     Object value = getTable().get(name);
/* 394 */     if (value instanceof byte[]) {
/* 395 */       return (byte[])value;
/*     */     }
/*     */     
/* 398 */     throw new MessageFormatException(" cannot read a byte[] from " + value.getClass().getName());
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
/*     */   public Object getObject(String name) throws JMSException {
/* 418 */     return getTable().get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration getMapNames() throws JMSException {
/* 428 */     return getTable().keys();
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
/*     */   public void setBoolean(String name, boolean value) throws JMSException {
/* 441 */     initializeWriting();
/* 442 */     getTable().put(name, value ? Boolean.TRUE : Boolean.FALSE);
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
/*     */   public void setByte(String name, byte value) throws JMSException {
/* 455 */     initializeWriting();
/* 456 */     getTable().put(name, new Byte(value));
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
/*     */   public void setShort(String name, short value) throws JMSException {
/* 469 */     initializeWriting();
/* 470 */     getTable().put(name, new Short(value));
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
/*     */   public void setChar(String name, char value) throws JMSException {
/* 483 */     initializeWriting();
/* 484 */     getTable().put(name, new Character(value));
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
/*     */   public void setInt(String name, int value) throws JMSException {
/* 497 */     initializeWriting();
/* 498 */     getTable().put(name, new Integer(value));
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
/*     */   public void setLong(String name, long value) throws JMSException {
/* 511 */     initializeWriting();
/* 512 */     getTable().put(name, new Long(value));
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
/*     */   public void setFloat(String name, float value) throws JMSException {
/* 525 */     initializeWriting();
/* 526 */     getTable().put(name, new Float(value));
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
/*     */   public void setDouble(String name, double value) throws JMSException {
/* 539 */     initializeWriting();
/* 540 */     getTable().put(name, new Double(value));
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
/*     */   public void setString(String name, String value) throws JMSException {
/* 553 */     initializeWriting();
/* 554 */     getTable().put(name, value);
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
/*     */   public void setBytes(String name, byte[] value) throws JMSException {
/* 568 */     initializeWriting();
/* 569 */     getTable().put(name, value);
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
/*     */   public void setBytes(String name, byte[] value, int offset, int length) throws JMSException {
/* 584 */     initializeWriting();
/* 585 */     byte[] data = new byte[length];
/* 586 */     System.arraycopy(value, offset, data, 0, length);
/* 587 */     getTable().put(name, data);
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
/*     */   public void setObject(String name, Object value) throws JMSException {
/* 604 */     initializeWriting();
/* 605 */     if (value instanceof Number || value instanceof String || value instanceof Boolean || value instanceof Byte || value instanceof Character || value instanceof byte[]) {
/*     */       
/* 607 */       getTable().put(name, value);
/*     */     } else {
/*     */       
/* 610 */       throw new MessageFormatException(value.getClass() + " is not a primitive type");
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void writeBody(DataOutput dataOut) throws IOException {
/* 615 */     writeMapProperties(this.theTable, dataOut);
/* 616 */     this.theTable = null;
/*     */   }
/*     */   
/*     */   protected void readBody(DataInput dataIn) throws IOException {
/* 620 */     this.theTable = readMapProperties(dataIn);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean itemExists(String name) throws JMSException {
/* 631 */     return getTable().containsKey(name);
/*     */   }
/*     */   
/*     */   private void initializeWriting() throws MessageNotWriteableException {
/* 635 */     if (this.readOnlyMessage)
/* 636 */       throw new MessageNotWriteableException("This message is in read-only mode"); 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ActiveMQMapMessage.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */