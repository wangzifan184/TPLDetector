/*    */ package org.codehaus.activemq.util;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
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
/*    */ public class SerializationHelper
/*    */ {
/*    */   public static byte[] serialize(Object object) throws IOException {
/* 31 */     ByteArrayOutputStream buffer = new ByteArrayOutputStream();
/* 32 */     ObjectOutputStream out = new ObjectOutputStream(buffer);
/* 33 */     out.writeObject(object);
/* 34 */     out.close();
/* 35 */     return buffer.toByteArray();
/*    */   }
/*    */   
/*    */   public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
/* 39 */     ByteArrayInputStream buffer = new ByteArrayInputStream(data);
/* 40 */     ObjectInputStream in = new ObjectInputStream(buffer);
/* 41 */     Object answer = in.readObject();
/* 42 */     in.close();
/* 43 */     return answer;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activem\\util\SerializationHelper.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */