/*    */ package org.codehaus.activemq.service.impl;
/*    */ 
/*    */ import org.codehaus.activemq.broker.Broker;
/*    */ import org.codehaus.activemq.broker.BrokerClient;
/*    */ import org.codehaus.activemq.message.MessageAck;
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
/*    */ public class MessageAckTransactionTask
/*    */   extends PacketTransactionTask
/*    */ {
/*    */   public MessageAckTransactionTask(BrokerClient brokerClient, MessageAck ack) {
/* 32 */     super(brokerClient, (Packet)ack);
/*    */   }
/*    */   
/*    */   public void execute(Broker broker) throws Throwable {
/* 36 */     MessageAck ack = (MessageAck)getPacket();
/* 37 */     broker.acknowledgeMessage(getBrokerClient(ack.getConsumerId()), ack);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\impl\MessageAckTransactionTask.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */