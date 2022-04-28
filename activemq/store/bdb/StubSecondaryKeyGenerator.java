/*    */ package org.codehaus.activemq.store.bdb;
/*    */ 
/*    */ import com.sleepycat.je.DatabaseEntry;
/*    */ import com.sleepycat.je.DatabaseException;
/*    */ import com.sleepycat.je.SecondaryDatabase;
/*    */ import com.sleepycat.je.SecondaryKeyCreator;
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
/*    */ public class StubSecondaryKeyGenerator
/*    */   implements SecondaryKeyCreator
/*    */ {
/* 29 */   long counter = 100L;
/*    */   
/*    */   public synchronized boolean createSecondaryKey(SecondaryDatabase secondaryDatabase, DatabaseEntry keyEntry, DatabaseEntry valueEntry, DatabaseEntry resultEntry) throws DatabaseException {
/* 32 */     long value = ++this.counter;
/* 33 */     System.out.println("Creating new counter key of value: " + value);
/* 34 */     resultEntry.setData(BDbHelper.asBytes(value));
/* 35 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\store\bdb\StubSecondaryKeyGenerator.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */