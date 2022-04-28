/*    */ package org.codehaus.activemq.store.jdbc.adapter;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
/*    */ import org.codehaus.activemq.store.jdbc.StatementProvider;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StreamJDBCAdapter
/*    */   extends DefaultJDBCAdapter
/*    */ {
/*    */   public StreamJDBCAdapter() {}
/*    */   
/*    */   public StreamJDBCAdapter(StatementProvider provider) {
/* 48 */     super(provider);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected byte[] getBinaryData(ResultSet rs, int index) throws SQLException {
/*    */     try {
/* 57 */       InputStream is = rs.getBinaryStream(index);
/* 58 */       ByteArrayOutputStream os = new ByteArrayOutputStream(4096);
/*    */       
/*    */       int ch;
/* 61 */       while ((ch = is.read()) >= 0) {
/* 62 */         os.write(ch);
/*    */       }
/* 64 */       is.close();
/* 65 */       os.close();
/*    */       
/* 67 */       return os.toByteArray();
/* 68 */     } catch (IOException e) {
/* 69 */       throw (SQLException)(new SQLException("Error reading binary parameter: " + index)).initCause(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void setBinaryData(PreparedStatement s, int index, byte[] data) throws SQLException {
/* 77 */     s.setBinaryStream(index, new ByteArrayInputStream(data), data.length);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\jdbc\adapter\StreamJDBCAdapter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */