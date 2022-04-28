/*     */ package org.codehaus.activemq.store.jdbc.adapter;
/*     */ 
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
/*     */ public class DefaultStatementProvider
/*     */   implements StatementProvider
/*     */ {
/*  28 */   protected String tablePrefix = "";
/*  29 */   protected String messageTableName = "ACTIVEMQ_MSGS";
/*  30 */   protected String txTableName = "ACTIVEMQ_TXS";
/*  31 */   protected String durableSubAcksTableName = "ACTIVEMQ_ACKS";
/*     */   
/*  33 */   protected String binaryDataType = "BLOB";
/*  34 */   protected String containerNameDataType = "VARCHAR(250)";
/*  35 */   protected String xidDataType = "VARCHAR(250)";
/*  36 */   protected String msgIdDataType = "VARCHAR(250)";
/*  37 */   protected String subscriptionIdDataType = "VARCHAR(250)";
/*  38 */   protected String sequenceDataType = "INTEGER";
/*  39 */   protected String stringIdDataType = "VARCHAR(250)";
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getCreateSchemaStatments() {
/*  44 */     return new String[] { "CREATE TABLE " + this.tablePrefix + this.messageTableName + "(" + "ID " + this.sequenceDataType + ", CONTAINER " + this.containerNameDataType + ", MSGID " + this.msgIdDataType + ", MSG " + this.binaryDataType + ", PRIMARY KEY ( ID ) )", "CREATE INDEX " + this.tablePrefix + this.messageTableName + "_MIDX ON " + this.tablePrefix + this.messageTableName + " (MSGID)", "CREATE INDEX " + this.tablePrefix + this.messageTableName + "_CIDX ON " + this.tablePrefix + this.messageTableName + " (CONTAINER)", "CREATE TABLE " + this.tablePrefix + this.txTableName + "(" + "XID " + this.xidDataType + ", TX " + this.binaryDataType + ", PRIMARY KEY ( XID ))", "CREATE TABLE " + this.tablePrefix + this.durableSubAcksTableName + "(" + "SUB " + this.subscriptionIdDataType + ", CONTAINER " + this.containerNameDataType + ", LAST_ACKED_ID " + this.sequenceDataType + ", SE_ID INTEGER" + ", SE_CLIENT_ID " + this.stringIdDataType + ", SE_CONSUMER_NAME " + this.stringIdDataType + ", SE_SELECTOR " + this.stringIdDataType + ", PRIMARY KEY ( SUB ))", "CREATE INDEX " + this.tablePrefix + this.durableSubAcksTableName + "_CIDX ON " + this.tablePrefix + this.durableSubAcksTableName + " (CONTAINER)" };
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getDropSchemaStatments() {
/*  73 */     return new String[] { "DROP TABLE " + this.tablePrefix + this.durableSubAcksTableName + "", "DROP TABLE " + this.tablePrefix + this.messageTableName + "", "DROP TABLE " + this.tablePrefix + this.txTableName + "" };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAddMessageStatment() {
/*  81 */     return "INSERT INTO " + this.tablePrefix + this.messageTableName + "(ID, CONTAINER, MSGID, MSG) VALUES (?, ?, ?, ?)";
/*     */   }
/*     */   public String getUpdateMessageStatment() {
/*  84 */     return "UPDATE " + this.tablePrefix + this.messageTableName + " SET MSG=? WHERE ID=?";
/*     */   }
/*     */   public String getRemoveMessageStatment() {
/*  87 */     return "DELETE FROM " + this.tablePrefix + this.messageTableName + " WHERE ID=?";
/*     */   }
/*     */   public String getFindMessageSequenceIdStatment() {
/*  90 */     return "SELECT ID FROM " + this.tablePrefix + this.messageTableName + " WHERE MSGID=?";
/*     */   }
/*     */   public String getFindMessageStatment() {
/*  93 */     return "SELECT MSG FROM " + this.tablePrefix + this.messageTableName + " WHERE ID=?";
/*     */   }
/*     */   public String getFindAllMessagesStatment() {
/*  96 */     return "SELECT ID, MSGID FROM " + this.tablePrefix + this.messageTableName + " WHERE CONTAINER=? ORDER BY ID";
/*     */   }
/*     */   public String getFindLastSequenceId() {
/*  99 */     return "SELECT MAX(ID) FROM " + this.tablePrefix + this.messageTableName + "";
/*     */   }
/*     */   
/*     */   public String getAddXidStatment() {
/* 103 */     return "INSERT INTO " + this.tablePrefix + this.txTableName + "(XID, TX) VALUES (?, ?)";
/*     */   }
/*     */   public String getUpdateXidStatment() {
/* 106 */     return "UPDATE " + this.tablePrefix + this.txTableName + " SET TX=? WHERE XID=?";
/*     */   }
/*     */   public String getRemoveXidStatment() {
/* 109 */     return "DELETE FROM " + this.tablePrefix + this.txTableName + " WHERE XID=?";
/*     */   }
/*     */   public String getFindXidStatment() {
/* 112 */     return "SELECT TX FROM " + this.tablePrefix + this.txTableName + " WHERE XID=?";
/*     */   }
/*     */   public String getFindAllXidStatment() {
/* 115 */     return "SELECT XID FROM " + this.tablePrefix + this.txTableName + "";
/*     */   }
/*     */   public String getFindAllTxStatment() {
/* 118 */     return "SELECT XID, TX FROM " + this.tablePrefix + this.txTableName + "";
/*     */   }
/*     */   
/*     */   public String getCreateDurableSubStatment() {
/* 122 */     return "INSERT INTO " + this.tablePrefix + this.durableSubAcksTableName + "(SE_ID, SE_CLIENT_ID, SE_CONSUMER_NAME, SE_SELECTOR, SUB, CONTAINER, LAST_ACKED_ID) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUpdateDurableSubStatment() {
/* 128 */     return "UPDATE " + this.tablePrefix + this.durableSubAcksTableName + " SET SE_ID=?, SE_CLIENT_ID=?, SE_CONSUMER_NAME=?, SE_SELECTOR=? WHERE SUB=? AND CONTAINER=?";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFindDurableSubStatment() {
/* 133 */     return "SELECT SE_ID, SE_CLIENT_ID, SE_CONSUMER_NAME, SE_SELECTOR, CONTAINER=? " + this.tablePrefix + this.durableSubAcksTableName + " WHERE SUB=? AND CONTAINER=?";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUpdateLastAckOfDurableSub() {
/* 138 */     return "UPDATE " + this.tablePrefix + this.durableSubAcksTableName + " SET LAST_ACKED_ID=? WHERE SUB=? AND CONTAINER=?";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFindAllDurableSubMessagesStatment() {
/* 143 */     return "SELECT M.ID, M.MSGID FROM " + this.tablePrefix + this.messageTableName + " M, " + this.tablePrefix + this.durableSubAcksTableName + " D " + " WHERE D.CONTAINER=? AND D.SUB=? " + " AND M.CONTAINER=D.CONTAINER AND M.ID > D.LAST_ACKED_ID" + " ORDER BY M.ID";
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
/*     */   public String getContainerNameDataType() {
/* 155 */     return this.containerNameDataType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContainerNameDataType(String containerNameDataType) {
/* 161 */     this.containerNameDataType = containerNameDataType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBinaryDataType() {
/* 167 */     return this.binaryDataType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBinaryDataType(String messageDataType) {
/* 173 */     this.binaryDataType = messageDataType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessageTableName() {
/* 179 */     return this.messageTableName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageTableName(String messageTableName) {
/* 185 */     this.messageTableName = messageTableName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMsgIdDataType() {
/* 191 */     return this.msgIdDataType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMsgIdDataType(String msgIdDataType) {
/* 197 */     this.msgIdDataType = msgIdDataType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSequenceDataType() {
/* 203 */     return this.sequenceDataType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSequenceDataType(String sequenceDataType) {
/* 209 */     this.sequenceDataType = sequenceDataType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTablePrefix() {
/* 215 */     return this.tablePrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTablePrefix(String tablePrefix) {
/* 221 */     this.tablePrefix = tablePrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTxTableName() {
/* 227 */     return this.txTableName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTxTableName(String txTableName) {
/* 233 */     this.txTableName = txTableName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getXidDataType() {
/* 239 */     return this.xidDataType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setXidDataType(String xidDataType) {
/* 245 */     this.xidDataType = xidDataType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDurableSubAcksTableName() {
/* 251 */     return this.durableSubAcksTableName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDurableSubAcksTableName(String durableSubAcksTableName) {
/* 257 */     this.durableSubAcksTableName = durableSubAcksTableName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubscriptionIdDataType() {
/* 263 */     return this.subscriptionIdDataType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSubscriptionIdDataType(String subscriptionIdDataType) {
/* 269 */     this.subscriptionIdDataType = subscriptionIdDataType;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\jdbc\adapter\DefaultStatementProvider.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */