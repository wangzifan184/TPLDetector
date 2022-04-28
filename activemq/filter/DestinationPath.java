/*    */ package org.codehaus.activemq.filter;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.jms.Destination;
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
/*    */ public class DestinationPath
/*    */ {
/*    */   protected static final char SEPARATOR = '.';
/*    */   
/*    */   public static String[] getDestinationPaths(String subject) {
/* 36 */     List list = new ArrayList();
/* 37 */     int previous = 0;
/* 38 */     int lastIndex = subject.length() - 1;
/*    */     while (true) {
/* 40 */       int idx = subject.indexOf('.', previous);
/* 41 */       if (idx < 0) {
/* 42 */         list.add(subject.substring(previous, lastIndex + 1));
/*    */         break;
/*    */       } 
/* 45 */       list.add(subject.substring(previous, idx));
/* 46 */       previous = idx + 1;
/*    */     } 
/* 48 */     String[] answer = new String[list.size()];
/* 49 */     list.toArray(answer);
/* 50 */     return answer;
/*    */   }
/*    */   
/*    */   public static String[] getDestinationPaths(Message message) throws JMSException {
/* 54 */     return getDestinationPaths(message.getJMSDestination());
/*    */   }
/*    */   
/*    */   public static String[] getDestinationPaths(Destination destination) {
/* 58 */     return getDestinationPaths(destination.toString());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String toString(String[] paths) {
/* 68 */     StringBuffer buffer = new StringBuffer();
/* 69 */     for (int i = 0; i < paths.length; i++) {
/* 70 */       if (i > 0) {
/* 71 */         buffer.append('.');
/*    */       }
/* 73 */       String path = paths[i];
/* 74 */       if (path == null) {
/* 75 */         buffer.append("*");
/*    */       } else {
/*    */         
/* 78 */         buffer.append(path);
/*    */       } 
/*    */     } 
/* 81 */     return buffer.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\filter\DestinationPath.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */