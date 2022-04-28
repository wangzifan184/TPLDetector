/*    */ package org.codehaus.activemq.store.bdbn;
/*    */ 
/*    */ import com.sleepycat.bdb.bind.DataBinding;
/*    */ import com.sleepycat.bdb.bind.DataBuffer;
/*    */ import com.sleepycat.bdb.bind.DataFormat;
/*    */ import java.io.IOException;
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
/*    */ public class StringDataBinding
/*    */   implements DataBinding
/*    */ {
/* 32 */   private static StringDataFormat format = new StringDataFormat();
/*    */   
/*    */   public Object dataToObject(DataBuffer buffer) throws IOException {
/* 35 */     return new String(buffer.getDataBytes(), buffer.getDataOffset(), buffer.getDataLength());
/*    */   }
/*    */   
/*    */   public void objectToData(Object object, DataBuffer buffer) throws IOException {
/* 39 */     String text = (String)object;
/* 40 */     byte[] data = text.getBytes();
/* 41 */     buffer.setData(data, 0, data.length);
/*    */   }
/*    */   
/*    */   public DataFormat getDataFormat() {
/* 45 */     return format;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\bdbn\StringDataBinding.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */