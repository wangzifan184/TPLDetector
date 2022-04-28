/*    */ package org.codehaus.activemq.filter;
/*    */ 
/*    */ import javax.jms.JMSException;
/*    */ import javax.jms.Message;
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
/*    */ public class PropertyExpression
/*    */   implements Expression
/*    */ {
/*    */   private String name;
/*    */   
/*    */   public PropertyExpression(String name) {
/* 34 */     this.name = name;
/*    */   }
/*    */   
/*    */   public Object evaluate(Message message) throws JMSException {
/* 38 */     Object result = null;
/* 39 */     if (this.name != null) {
/* 40 */       result = message.getObjectProperty(this.name);
/*    */     }
/* 42 */     if (result == null)
/*    */     {
/* 44 */       if (this.name.equals("JMSType")) {
/* 45 */         result = message.getJMSType();
/*    */       }
/* 47 */       else if (this.name.equals("JMSMessageID")) {
/* 48 */         result = message.getJMSMessageID();
/*    */       }
/* 50 */       else if (this.name.equals("JMSCorrelationID")) {
/* 51 */         result = message.getJMSCorrelationID();
/*    */       }
/* 53 */       else if (this.name.equals("JMSPriority")) {
/* 54 */         result = new Integer(message.getJMSPriority());
/*    */       }
/* 56 */       else if (this.name.equals("JMSTimestamp")) {
/* 57 */         result = new Long(message.getJMSTimestamp());
/*    */       } 
/*    */     }
/* 60 */     return result;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 64 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 72 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 79 */     return this.name.hashCode();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 87 */     if (o == null || !getClass().equals(o.getClass())) {
/* 88 */       return false;
/*    */     }
/* 90 */     return this.name.equals(((PropertyExpression)o).name);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\filter\PropertyExpression.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */