/*    */ package org.codehaus.activemq.store.jdbc.adapter;
/*    */ 
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
/*    */ 
/*    */ public class BytesJDBCAdapter
/*    */   extends DefaultJDBCAdapter
/*    */ {
/*    */   public BytesJDBCAdapter() {}
/*    */   
/*    */   public BytesJDBCAdapter(StatementProvider provider) {
/* 45 */     super(provider);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected byte[] getBinaryData(ResultSet rs, int index) throws SQLException {
/* 52 */     return rs.getBytes(index);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void setBinaryData(PreparedStatement s, int index, byte[] data) throws SQLException {
/* 59 */     s.setBytes(index, data);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\jdbc\adapter\BytesJDBCAdapter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */