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
/*     */ public class CachingStatementProvider
/*     */   implements StatementProvider
/*     */ {
/*     */   private final StatementProvider statementProvider;
/*     */   private String addMessageStatment;
/*     */   private String addXidStatment;
/*     */   private String[] createSchemaStatments;
/*     */   private String[] dropSchemaStatments;
/*     */   private String findAllMessagesStatment;
/*     */   private String findAllTxStatment;
/*     */   private String findAllXidStatment;
/*     */   private String findLastSequenceId;
/*     */   private String findMessageStatment;
/*     */   private String findXidStatment;
/*     */   private String removeMessageStatment;
/*     */   private String removeXidStatment;
/*     */   private String updateMessageStatment;
/*     */   private String updateXidStatment;
/*     */   private String createDurableSubStatment;
/*     */   private String updateDurableSubStatment;
/*     */   private String findDurableSubStatment;
/*     */   private String findAllDurableSubMessagesStatment;
/*     */   private String updateLastAckOfDurableSub;
/*     */   private String findMessageSequenceIdStatment;
/*     */   
/*     */   public CachingStatementProvider(StatementProvider statementProvider) {
/*  63 */     this.statementProvider = statementProvider;
/*     */   }
/*     */   
/*     */   public StatementProvider getNext() {
/*  67 */     return this.statementProvider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAddMessageStatment() {
/*  74 */     if (this.addMessageStatment == null) {
/*  75 */       this.addMessageStatment = this.statementProvider.getAddMessageStatment();
/*     */     }
/*  77 */     return this.addMessageStatment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAddXidStatment() {
/*  84 */     if (this.addXidStatment == null) {
/*  85 */       this.addXidStatment = this.statementProvider.getAddXidStatment();
/*     */     }
/*  87 */     return this.addXidStatment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getCreateSchemaStatments() {
/*  94 */     if (this.createSchemaStatments == null) {
/*  95 */       this.createSchemaStatments = this.statementProvider.getCreateSchemaStatments();
/*     */     }
/*  97 */     return this.createSchemaStatments;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getDropSchemaStatments() {
/* 104 */     if (this.dropSchemaStatments == null) {
/* 105 */       this.dropSchemaStatments = this.statementProvider.getDropSchemaStatments();
/*     */     }
/* 107 */     return this.dropSchemaStatments;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFindAllMessagesStatment() {
/* 114 */     if (this.findAllMessagesStatment == null) {
/* 115 */       this.findAllMessagesStatment = this.statementProvider.getFindAllMessagesStatment();
/*     */     }
/* 117 */     return this.findAllMessagesStatment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFindAllTxStatment() {
/* 124 */     if (this.findAllTxStatment == null) {
/* 125 */       this.findAllTxStatment = this.statementProvider.getFindAllTxStatment();
/*     */     }
/* 127 */     return this.findAllTxStatment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFindAllXidStatment() {
/* 134 */     if (this.findAllXidStatment == null) {
/* 135 */       this.findAllXidStatment = this.statementProvider.getFindAllXidStatment();
/*     */     }
/* 137 */     return this.findAllXidStatment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFindLastSequenceId() {
/* 144 */     if (this.findLastSequenceId == null) {
/* 145 */       this.findLastSequenceId = this.statementProvider.getFindLastSequenceId();
/*     */     }
/* 147 */     return this.findLastSequenceId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFindMessageStatment() {
/* 154 */     if (this.findMessageStatment == null) {
/* 155 */       this.findMessageStatment = this.statementProvider.getFindMessageStatment();
/*     */     }
/* 157 */     return this.findMessageStatment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFindXidStatment() {
/* 164 */     if (this.findXidStatment == null) {
/* 165 */       this.findXidStatment = this.statementProvider.getFindXidStatment();
/*     */     }
/* 167 */     return this.findXidStatment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRemoveMessageStatment() {
/* 174 */     if (this.removeMessageStatment == null) {
/* 175 */       this.removeMessageStatment = this.statementProvider.getRemoveMessageStatment();
/*     */     }
/* 177 */     return this.removeMessageStatment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRemoveXidStatment() {
/* 184 */     if (this.removeXidStatment == null) {
/* 185 */       this.removeXidStatment = this.statementProvider.getRemoveXidStatment();
/*     */     }
/* 187 */     return this.removeXidStatment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUpdateMessageStatment() {
/* 194 */     if (this.updateMessageStatment == null) {
/* 195 */       this.updateMessageStatment = this.statementProvider.getUpdateMessageStatment();
/*     */     }
/* 197 */     return this.updateMessageStatment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUpdateXidStatment() {
/* 204 */     if (this.updateXidStatment == null) {
/* 205 */       this.updateXidStatment = this.statementProvider.getUpdateXidStatment();
/*     */     }
/* 207 */     return this.updateXidStatment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCreateDurableSubStatment() {
/* 214 */     if (this.createDurableSubStatment == null) {
/* 215 */       this.createDurableSubStatment = this.statementProvider.getCreateDurableSubStatment();
/*     */     }
/* 217 */     return this.createDurableSubStatment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUpdateDurableSubStatment() {
/* 224 */     if (this.updateDurableSubStatment == null) {
/* 225 */       this.updateDurableSubStatment = this.statementProvider.getUpdateDurableSubStatment();
/*     */     }
/* 227 */     return this.updateDurableSubStatment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFindDurableSubStatment() {
/* 234 */     if (this.findDurableSubStatment == null) {
/* 235 */       this.findDurableSubStatment = this.statementProvider.getFindDurableSubStatment();
/*     */     }
/* 237 */     return this.findDurableSubStatment;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFindAllDurableSubMessagesStatment() {
/* 243 */     if (this.findAllDurableSubMessagesStatment == null) {
/* 244 */       this.findAllDurableSubMessagesStatment = this.statementProvider.getFindAllDurableSubMessagesStatment();
/*     */     }
/* 246 */     return this.findAllDurableSubMessagesStatment;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUpdateLastAckOfDurableSub() {
/* 252 */     if (this.updateLastAckOfDurableSub == null) {
/* 253 */       this.updateLastAckOfDurableSub = this.statementProvider.getUpdateLastAckOfDurableSub();
/*     */     }
/* 255 */     return this.updateLastAckOfDurableSub;
/*     */   }
/*     */   
/*     */   public String getFindMessageSequenceIdStatment() {
/* 259 */     if (this.findMessageSequenceIdStatment == null) {
/* 260 */       this.findMessageSequenceIdStatment = this.statementProvider.getFindMessageSequenceIdStatment();
/*     */     }
/* 262 */     return this.findMessageSequenceIdStatment;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\jdbc\adapter\CachingStatementProvider.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */