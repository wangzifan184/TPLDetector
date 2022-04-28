/*     */ package org.codehaus.activemq.store.jdbc.adapter;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.sql.Blob;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import javax.jms.JMSException;
/*     */ import org.codehaus.activemq.store.jdbc.StatementProvider;
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
/*     */ public class BlobJDBCAdapter
/*     */   extends DefaultJDBCAdapter
/*     */ {
/*     */   public BlobJDBCAdapter() {}
/*     */   
/*     */   public BlobJDBCAdapter(StatementProvider provider) {
/*  58 */     super(provider);
/*     */   }
/*     */ 
/*     */   
/*     */   public void doAddMessage(Connection c, long seq, String messageID, String destinationName, byte[] data) throws SQLException, JMSException {
/*  63 */     PreparedStatement s = null;
/*  64 */     ResultSet rs = null;
/*     */ 
/*     */     
/*     */     try {
/*  68 */       s = c.prepareStatement(this.statementProvider.getAddMessageStatment());
/*  69 */       s.setLong(1, seq);
/*  70 */       s.setString(2, destinationName);
/*  71 */       s.setString(3, messageID);
/*  72 */       s.setString(4, " ");
/*     */       
/*  74 */       if (s.executeUpdate() != 1) {
/*  75 */         throw new JMSException("Failed to broker message: " + messageID + " in container.");
/*     */       }
/*  77 */       s.close();
/*     */ 
/*     */       
/*  80 */       s = c.prepareStatement(this.statementProvider.getFindMessageStatment());
/*  81 */       s.setLong(1, seq);
/*  82 */       rs = s.executeQuery();
/*  83 */       if (!rs.next()) {
/*  84 */         throw new JMSException("Failed to broker message: " + messageID + " in container.");
/*     */       }
/*     */ 
/*     */       
/*  88 */       Blob blob = rs.getBlob(1);
/*  89 */       OutputStream stream = blob.setBinaryStream(data.length);
/*  90 */       stream.write(data);
/*  91 */       stream.close();
/*  92 */       s.close();
/*     */ 
/*     */       
/*  95 */       s = c.prepareStatement(this.statementProvider.getUpdateMessageStatment());
/*  96 */       s.setBlob(1, blob);
/*  97 */       s.setLong(2, seq);
/*     */     }
/*  99 */     catch (IOException e) {
/* 100 */       throw (SQLException)(new SQLException("BLOB could not be updated: " + e)).initCause(e);
/*     */     } finally {
/*     */       
/*     */       try {
/* 104 */         rs.close();
/* 105 */       } catch (Throwable e) {}
/*     */       
/*     */       try {
/* 108 */         s.close();
/* 109 */       } catch (Throwable e) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] doGetMessage(Connection c, long seq) throws SQLException {
/* 115 */     PreparedStatement s = null; ResultSet rs = null;
/*     */     
/*     */     try {
/* 118 */       s = c.prepareStatement(this.statementProvider.getFindMessageStatment());
/* 119 */       s.setLong(1, seq);
/* 120 */       rs = s.executeQuery();
/*     */       
/* 122 */       if (!rs.next())
/* 123 */         return null; 
/* 124 */       Blob blob = rs.getBlob(1);
/* 125 */       InputStream is = blob.getBinaryStream();
/*     */       
/* 127 */       ByteArrayOutputStream os = new ByteArrayOutputStream((int)blob.length());
/*     */       int ch;
/* 129 */       while ((ch = is.read()) >= 0) {
/* 130 */         os.write(ch);
/*     */       }
/* 132 */       is.close();
/* 133 */       os.close();
/*     */       
/* 135 */       return os.toByteArray();
/*     */     }
/* 137 */     catch (IOException e) {
/* 138 */       throw (SQLException)(new SQLException("BLOB could not be updated: " + e)).initCause(e);
/*     */     } finally {
/*     */       
/* 141 */       try { rs.close(); } catch (Throwable e) {} 
/* 142 */       try { s.close(); } catch (Throwable e) {}
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\jdbc\adapter\BlobJDBCAdapter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */