/*     */ package org.codehaus.activemq.message;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutput;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.Externalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import javax.jms.JMSException;
/*     */ import javax.transaction.xa.Xid;
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
/*     */ public class ActiveMQXid
/*     */   implements Xid, Externalizable, Comparable
/*     */ {
/*     */   private static final long serialVersionUID = -5754338187296859149L;
/*  46 */   private static final Log log = LogFactory.getLog(ActiveMQXid.class);
/*     */   
/*     */   private int formatId;
/*     */   
/*     */   private byte[] branchQualifier;
/*     */   private byte[] globalTransactionId;
/*     */   private transient int hash;
/*  53 */   private static final String[] HEX_TABLE = new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0a", "0b", "0c", "0d", "0e", "0f", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "1a", "1b", "1c", "1d", "1e", "1f", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "2a", "2b", "2c", "2d", "2e", "2f", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "3a", "3b", "3c", "3d", "3e", "3f", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "4a", "4b", "4c", "4d", "4e", "4f", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "5a", "5b", "5c", "5d", "5e", "5f", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "6a", "6b", "6c", "6d", "6e", "6f", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "7a", "7b", "7c", "7d", "7e", "7f", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "8a", "8b", "8c", "8d", "8e", "8f", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "9a", "9b", "9c", "9d", "9e", "9f", "a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9", "aa", "ab", "ac", "ad", "ae", "af", "b0", "b1", "b2", "b3", "b4", "b5", "b6", "b7", "b8", "b9", "ba", "bb", "bc", "bd", "be", "bf", "c0", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "ca", "cb", "cc", "cd", "ce", "cf", "d0", "d1", "d2", "d3", "d4", "d5", "d6", "d7", "d8", "d9", "da", "db", "dc", "dd", "de", "df", "e0", "e1", "e2", "e3", "e4", "e5", "e6", "e7", "e8", "e9", "ea", "eb", "ec", "ed", "ee", "ef", "f0", "f1", "f2", "f3", "f4", "f5", "f6", "f7", "f8", "f9", "fa", "fb", "fc", "fd", "fe", "ff" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ActiveMQXid fromBytes(byte[] data) throws IOException {
/*  80 */     return read(new DataInputStream(new ByteArrayInputStream(data)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQXid() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQXid(Xid xid) {
/*  94 */     this.formatId = xid.getFormatId();
/*  95 */     this.branchQualifier = xid.getBranchQualifier();
/*  96 */     this.globalTransactionId = xid.getGlobalTransactionId();
/*     */   }
/*     */   
/*     */   public ActiveMQXid(int formatId, byte[] branchQualifier, byte[] globalTransactionId) {
/* 100 */     this.formatId = formatId;
/* 101 */     this.branchQualifier = branchQualifier;
/* 102 */     this.globalTransactionId = globalTransactionId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActiveMQXid(String txid) throws JMSException {
/* 109 */     String[] parts = txid.split(":", 3);
/* 110 */     if (parts.length != 3) {
/* 111 */       throw new JMSException("Invalid XID: " + txid);
/*     */     }
/* 113 */     this.formatId = Integer.parseInt(parts[0]);
/*     */     
/* 115 */     if (log.isDebugEnabled()) {
/* 116 */       log.debug("parts:" + parts[0]);
/* 117 */       log.debug("parts:" + parts[1]);
/* 118 */       log.debug("parts:" + parts[2]);
/*     */     } 
/* 120 */     this.globalTransactionId = toBytesFromHex(parts[1]);
/* 121 */     this.branchQualifier = toBytesFromHex(parts[2]);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 125 */     if (this.hash == 0) {
/* 126 */       this.hash = this.formatId;
/* 127 */       this.hash = hash(this.branchQualifier, this.hash);
/* 128 */       this.hash = hash(this.globalTransactionId, this.hash);
/*     */     } 
/* 130 */     if (this.hash == 0) {
/* 131 */       this.hash = 11332302;
/*     */     }
/* 133 */     return this.hash;
/*     */   }
/*     */   
/*     */   public boolean equals(Object that) {
/* 137 */     if (this == that) {
/* 138 */       return true;
/*     */     }
/* 140 */     if (hashCode() == that.hashCode() && that instanceof Xid) {
/* 141 */       return equals(this, (Xid)that);
/*     */     }
/* 143 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equals(Xid tis, Xid that) {
/* 153 */     if (tis == that)
/* 154 */       return true; 
/* 155 */     if (tis == null || that == null) {
/* 156 */       return false;
/*     */     }
/* 158 */     return (tis.getFormatId() == that.getFormatId() && equals(tis.getBranchQualifier(), that.getBranchQualifier()) && equals(tis.getGlobalTransactionId(), that.getGlobalTransactionId()));
/*     */   }
/*     */   
/*     */   public int compareTo(Object object) {
/* 162 */     if (this == object) {
/* 163 */       return 0;
/*     */     }
/*     */     
/* 166 */     if (object instanceof ActiveMQXid) {
/* 167 */       ActiveMQXid that = (ActiveMQXid)object;
/* 168 */       int diff = this.formatId - that.formatId;
/* 169 */       if (diff == 0) {
/* 170 */         diff = compareTo(this.branchQualifier, that.branchQualifier);
/* 171 */         if (diff == 0) {
/* 172 */           diff = compareTo(this.globalTransactionId, that.globalTransactionId);
/*     */         }
/*     */       } 
/* 175 */       return diff;
/*     */     } 
/*     */     
/* 178 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toLocalTransactionId() {
/* 184 */     StringBuffer rc = new StringBuffer(13 + this.globalTransactionId.length * 2 + this.branchQualifier.length * 2);
/* 185 */     rc.append(this.formatId);
/* 186 */     rc.append(":");
/* 187 */     rc.append(toHexFromBytes(this.globalTransactionId));
/* 188 */     rc.append(":");
/* 189 */     rc.append(toHexFromBytes(this.branchQualifier));
/* 190 */     return rc.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getBranchQualifier() {
/* 197 */     return this.branchQualifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFormatId() {
/* 204 */     return this.formatId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getGlobalTransactionId() {
/* 211 */     return this.globalTransactionId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 218 */     return "XID:" + toLocalTransactionId();
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeExternal(ObjectOutput out) throws IOException {
/* 223 */     write(out);
/*     */   }
/*     */   
/*     */   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
/* 227 */     readState(in);
/*     */   }
/*     */   
/*     */   public void readState(DataInput dataIn) throws IOException {
/* 231 */     this.formatId = dataIn.readInt();
/* 232 */     this.branchQualifier = readBytes(dataIn);
/* 233 */     this.globalTransactionId = readBytes(dataIn);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ActiveMQXid read(DataInput dataIn) throws IOException {
/* 243 */     ActiveMQXid answer = new ActiveMQXid();
/* 244 */     answer.readState(dataIn);
/* 245 */     return answer;
/*     */   }
/*     */   
/*     */   public byte[] toBytes() throws IOException {
/* 249 */     ByteArrayOutputStream buffer = new ByteArrayOutputStream();
/* 250 */     write(new DataOutputStream(buffer));
/* 251 */     return buffer.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(DataOutput dataOut) throws IOException {
/* 260 */     dataOut.writeInt(this.formatId);
/* 261 */     writeBytes(dataOut, this.branchQualifier);
/* 262 */     writeBytes(dataOut, this.globalTransactionId);
/*     */   }
/*     */   
/*     */   protected void writeBytes(DataOutput dataOut, byte[] data) throws IOException {
/* 266 */     dataOut.writeInt(data.length);
/* 267 */     dataOut.write(data);
/*     */   }
/*     */   
/*     */   protected static byte[] readBytes(DataInput dataIn) throws IOException {
/* 271 */     int size = dataIn.readInt();
/* 272 */     byte[] data = new byte[size];
/* 273 */     dataIn.readFully(data);
/* 274 */     return data;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean equals(byte[] left, byte[] right) {
/* 279 */     if (left == right) {
/* 280 */       return true;
/*     */     }
/* 282 */     int size = left.length;
/* 283 */     if (size != right.length) {
/* 284 */       return false;
/*     */     }
/* 286 */     for (int i = 0; i < size; i++) {
/* 287 */       if (left[i] != right[i]) {
/* 288 */         return false;
/*     */       }
/*     */     } 
/* 291 */     return true;
/*     */   }
/*     */   
/*     */   protected int compareTo(byte[] left, byte[] right) {
/* 295 */     if (left == right) {
/* 296 */       return 0;
/*     */     }
/* 298 */     int size = left.length;
/* 299 */     int answer = size - right.length;
/* 300 */     if (answer == 0) {
/* 301 */       for (int i = 0; i < size; i++) {
/* 302 */         answer = left[i] - right[i];
/* 303 */         if (answer != 0) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     }
/* 308 */     return answer;
/*     */   }
/*     */   
/*     */   protected int hash(byte[] bytes, int hash) {
/* 312 */     for (int i = 0, size = bytes.length; i < size; i++) {
/* 313 */       hash ^= bytes[i] << i % 4 * 8;
/*     */     }
/* 315 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private byte[] toBytesFromHex(String hex) {
/* 323 */     byte[] rc = new byte[hex.length() / 2];
/* 324 */     for (int i = 0; i < rc.length; i++) {
/* 325 */       String h = hex.substring(i * 2, i * 2 + 2);
/* 326 */       int x = Integer.parseInt(h, 16);
/* 327 */       rc[i] = (byte)x;
/*     */     } 
/* 329 */     return rc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String toHexFromBytes(byte[] bytes) {
/* 337 */     StringBuffer rc = new StringBuffer(bytes.length * 2);
/* 338 */     for (int i = 0; i < bytes.length; i++) {
/* 339 */       rc.append(HEX_TABLE[0xFF & bytes[i]]);
/*     */     }
/* 341 */     return rc.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ActiveMQXid.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */