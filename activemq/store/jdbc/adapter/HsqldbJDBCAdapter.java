/*    */ package org.codehaus.activemq.store.jdbc.adapter;
/*    */ 
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
/*    */ public class HsqldbJDBCAdapter
/*    */   extends BytesJDBCAdapter
/*    */ {
/*    */   public static class HSQLStatementProvider
/*    */     extends DefaultStatementProvider
/*    */   {
/*    */     public HSQLStatementProvider() {
/* 30 */       setBinaryDataType("OTHER");
/*    */     }
/*    */   }
/*    */   
/*    */   public HsqldbJDBCAdapter() {
/* 35 */     super(new HSQLStatementProvider());
/*    */   }
/*    */   
/*    */   public HsqldbJDBCAdapter(StatementProvider provider) {
/* 39 */     super(provider);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\jdbc\adapter\HsqldbJDBCAdapter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */