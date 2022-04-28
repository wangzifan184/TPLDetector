/*     */ package org.codehaus.activemq;
/*     */ 
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Topic;
/*     */ import javax.jms.TopicSubscriber;
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
/*     */ public class ActiveMQTopicSubscriber
/*     */   extends ActiveMQMessageConsumer
/*     */   implements TopicSubscriber
/*     */ {
/*     */   protected ActiveMQTopicSubscriber(ActiveMQSession theSession, ActiveMQDestination dest, String name, String selector, int cnum, int prefetch, boolean noLocalValue, boolean browserValue) throws JMSException {
/* 116 */     super(theSession, dest, name, selector, cnum, prefetch, noLocalValue, browserValue);
/*     */     
/* 118 */     if (name != null)
/*     */     {
/* 120 */       theSession.connection.checkClientIDWasManuallySpecified();
/*     */     }
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
/*     */   public Topic getTopic() throws JMSException {
/* 133 */     checkClosed();
/* 134 */     return (Topic)getDestination();
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
/*     */   public boolean getNoLocal() throws JMSException {
/* 148 */     checkClosed();
/* 149 */     return isNoLocal();
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ActiveMQTopicSubscriber.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */