/*    */ package org.codehaus.activemq.journal.howl;
/*    */ 
/*    */ import org.codehaus.activemq.journal.RecordLocation;
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
/*    */ public class LongRecordLocation
/*    */   implements RecordLocation
/*    */ {
/*    */   private final long location;
/*    */   
/*    */   public LongRecordLocation(long l) {
/* 33 */     this.location = l;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int compareTo(Object o) {
/* 40 */     return (int)(this.location - ((LongRecordLocation)o).location);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long getLongLocation() {
/* 47 */     return this.location;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 54 */     int lowPart = (int)(0xFFFFFFFFFFFFFFFFL & this.location);
/* 55 */     int highPart = (int)(0xFFFFFFFFFFFFFFFFL & this.location >> 4L);
/* 56 */     return lowPart ^ highPart;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 63 */     if (o == null || o.getClass() != LongRecordLocation.class)
/* 64 */       return false; 
/* 65 */     return (((LongRecordLocation)o).location == this.location);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 72 */     return "0x" + Long.toHexString(this.location);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\journal\howl\LongRecordLocation.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */