/*    */ package org.codehaus.activemq.service.impl;
/*    */ 
/*    */ import org.codehaus.activemq.broker.Broker;
/*    */ import org.codehaus.activemq.broker.BrokerClient;
/*    */ import org.codehaus.activemq.message.ActiveMQMessage;
/*    */ import org.codehaus.activemq.message.Packet;
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
/*    */ public class SendMessageTransactionTask
/*    */   extends PacketTransactionTask
/*    */ {
/*    */   public SendMessageTransactionTask(BrokerClient brokerClient, ActiveMQMessage message) {
/* 32 */     super(brokerClient, (Packet)message);
/*    */   }
/*    */   
/*    */   public void execute(Broker broker) throws Throwable {
/* 36 */     ActiveMQMessage message = (ActiveMQMessage)getPacket();
/* 37 */     broker.sendMessage(getBrokerClient(message.getConsumerId()), message);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\SendMessageTransactionTask.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */