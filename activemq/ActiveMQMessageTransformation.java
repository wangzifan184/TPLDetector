/*     */ package org.codehaus.activemq;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import javax.jms.BytesMessage;
/*     */ import javax.jms.Destination;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.MapMessage;
/*     */ import javax.jms.Message;
/*     */ import javax.jms.ObjectMessage;
/*     */ import javax.jms.Queue;
/*     */ import javax.jms.StreamMessage;
/*     */ import javax.jms.TextMessage;
/*     */ import javax.jms.Topic;
/*     */ import org.codehaus.activemq.message.ActiveMQBytesMessage;
/*     */ import org.codehaus.activemq.message.ActiveMQDestination;
/*     */ import org.codehaus.activemq.message.ActiveMQMapMessage;
/*     */ import org.codehaus.activemq.message.ActiveMQMessage;
/*     */ import org.codehaus.activemq.message.ActiveMQObjectMessage;
/*     */ import org.codehaus.activemq.message.ActiveMQQueue;
/*     */ import org.codehaus.activemq.message.ActiveMQStreamMessage;
/*     */ import org.codehaus.activemq.message.ActiveMQTemporaryQueue;
/*     */ import org.codehaus.activemq.message.ActiveMQTemporaryTopic;
/*     */ import org.codehaus.activemq.message.ActiveMQTextMessage;
/*     */ import org.codehaus.activemq.message.ActiveMQTopic;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ActiveMQMessageTransformation
/*     */ {
/*     */   static ActiveMQDestination transformDestination(Destination destination) throws JMSException {
/*     */     ActiveMQTopic activeMQTopic;
/*  38 */     ActiveMQDestination result = null;
/*  39 */     if (destination != null) {
/*  40 */       if (destination instanceof ActiveMQDestination) {
/*  41 */         result = (ActiveMQDestination)destination;
/*     */       
/*     */       }
/*  44 */       else if (destination instanceof javax.jms.TemporaryQueue) {
/*  45 */         ActiveMQTemporaryQueue activeMQTemporaryQueue = new ActiveMQTemporaryQueue(((Queue)destination).getQueueName());
/*     */       }
/*  47 */       else if (destination instanceof javax.jms.TemporaryTopic) {
/*  48 */         ActiveMQTemporaryTopic activeMQTemporaryTopic = new ActiveMQTemporaryTopic(((Topic)destination).getTopicName());
/*     */       }
/*  50 */       else if (destination instanceof Queue) {
/*  51 */         ActiveMQQueue activeMQQueue = new ActiveMQQueue(((Queue)destination).getQueueName());
/*     */       }
/*  53 */       else if (destination instanceof Topic) {
/*  54 */         activeMQTopic = new ActiveMQTopic(((Topic)destination).getTopicName());
/*     */       } 
/*     */     }
/*     */     
/*  58 */     return (ActiveMQDestination)activeMQTopic;
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
/*     */   public static final ActiveMQMessage transformMessage(Message message) throws JMSException {
/*  70 */     if (message instanceof ActiveMQMessage) {
/*  71 */       ActiveMQMessage answer = ((ActiveMQMessage)message).shallowCopy();
/*     */       
/*  73 */       answer.setJMSMessageIdentity(null);
/*  74 */       return answer;
/*     */     } 
/*     */     
/*  77 */     ActiveMQMessage activeMessage = null;
/*  78 */     if (message instanceof ObjectMessage) {
/*  79 */       ObjectMessage objMsg = (ObjectMessage)message;
/*  80 */       ActiveMQObjectMessage msg = new ActiveMQObjectMessage();
/*  81 */       msg.setObject(objMsg.getObject());
/*  82 */       ActiveMQObjectMessage activeMQObjectMessage1 = msg;
/*     */     }
/*  84 */     else if (message instanceof TextMessage) {
/*  85 */       TextMessage textMsg = (TextMessage)message;
/*  86 */       ActiveMQTextMessage msg = new ActiveMQTextMessage();
/*  87 */       msg.setText(textMsg.getText());
/*  88 */       ActiveMQTextMessage activeMQTextMessage1 = msg;
/*     */     }
/*  90 */     else if (message instanceof MapMessage) {
/*  91 */       MapMessage mapMsg = (MapMessage)message;
/*  92 */       ActiveMQMapMessage msg = new ActiveMQMapMessage();
/*  93 */       for (Enumeration iter = mapMsg.getMapNames(); iter.hasMoreElements(); ) {
/*  94 */         String name = iter.nextElement().toString();
/*  95 */         msg.setObject(name, mapMsg.getObject(name));
/*     */       } 
/*  97 */       ActiveMQMapMessage activeMQMapMessage1 = msg;
/*     */     }
/*  99 */     else if (message instanceof BytesMessage) {
/* 100 */       BytesMessage bytesMsg = (BytesMessage)message;
/* 101 */       bytesMsg.reset();
/* 102 */       ActiveMQBytesMessage msg = new ActiveMQBytesMessage();
/*     */       try {
/*     */         while (true) {
/* 105 */           msg.writeByte(bytesMsg.readByte());
/*     */         }
/*     */       }
/* 108 */       catch (JMSException e) {
/*     */         
/* 110 */         ActiveMQBytesMessage activeMQBytesMessage = msg;
/*     */       } 
/* 112 */     } else if (message instanceof StreamMessage) {
/* 113 */       StreamMessage streamMessage = (StreamMessage)message;
/* 114 */       streamMessage.reset();
/* 115 */       ActiveMQStreamMessage msg = new ActiveMQStreamMessage();
/* 116 */       Object obj = null;
/*     */       try {
/* 118 */         while ((obj = streamMessage.readObject()) != null) {
/* 119 */           msg.writeObject(obj);
/*     */         }
/*     */       }
/* 122 */       catch (JMSException e) {}
/*     */       
/* 124 */       ActiveMQStreamMessage activeMQStreamMessage1 = msg;
/*     */     } else {
/*     */       
/* 127 */       activeMessage = new ActiveMQMessage();
/*     */     } 
/* 129 */     activeMessage.setJMSMessageID(message.getJMSMessageID());
/* 130 */     activeMessage.setJMSCorrelationID(message.getJMSCorrelationID());
/* 131 */     activeMessage.setJMSReplyTo((Destination)transformDestination(message.getJMSReplyTo()));
/* 132 */     activeMessage.setJMSDestination((Destination)transformDestination(message.getJMSDestination()));
/* 133 */     activeMessage.setJMSDeliveryMode(message.getJMSDeliveryMode());
/* 134 */     activeMessage.setJMSRedelivered(message.getJMSRedelivered());
/* 135 */     activeMessage.setJMSType(message.getJMSType());
/* 136 */     activeMessage.setJMSExpiration(message.getJMSExpiration());
/* 137 */     activeMessage.setJMSPriority(message.getJMSPriority());
/* 138 */     activeMessage.setJMSTimestamp(message.getJMSTimestamp());
/* 139 */     for (Enumeration propertyNames = message.getPropertyNames(); propertyNames.hasMoreElements(); ) {
/* 140 */       String name = propertyNames.nextElement().toString();
/* 141 */       Object obj = message.getObjectProperty(name);
/* 142 */       activeMessage.setObjectProperty(name, obj);
/*     */     } 
/* 144 */     return activeMessage;
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\ActiveMQMessageTransformation.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */