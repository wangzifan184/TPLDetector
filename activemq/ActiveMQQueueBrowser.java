/*     */ package org.codehaus.activemq;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Queue;
/*     */ import javax.jms.QueueBrowser;
/*     */ import org.codehaus.activemq.message.ActiveMQDestination;
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
/*     */ public class ActiveMQQueueBrowser
/*     */   extends ActiveMQMessageConsumer
/*     */   implements QueueBrowser, Enumeration
/*     */ {
/*     */   protected ActiveMQQueueBrowser(ActiveMQSession theSession, ActiveMQDestination dest, String selector, int cnum) throws JMSException {
/*  69 */     super(theSession, dest, "", selector, cnum, theSession.connection.getPrefetchPolicy().getQueueBrowserPrefetch(), false, true);
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
/*     */   public Queue getQueue() throws JMSException {
/*  81 */     return (Queue)getDestination();
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
/*     */   public Enumeration getEnumeration() throws JMSException {
/*  94 */     checkClosed();
/*     */     
/*  96 */     if (this.messageQueue.size() == 0) {
/*     */       try {
/*  98 */         Thread.sleep(1000L);
/*     */       }
/* 100 */       catch (InterruptedException e) {}
/*     */     }
/*     */     
/* 103 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasMoreElements() {
/* 111 */     return (this.messageQueue.size() > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object nextElement() {
/* 118 */     Object answer = null;
/*     */     try {
/* 120 */       answer = receiveNoWait();
/*     */     }
/* 122 */     catch (JMSException e) {
/* 123 */       e.printStackTrace();
/*     */     } 
/* 125 */     return answer;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ActiveMQQueueBrowser.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */