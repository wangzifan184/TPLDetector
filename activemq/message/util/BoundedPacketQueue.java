package org.codehaus.activemq.message.util;

import java.util.List;
import javax.jms.JMSException;
import org.codehaus.activemq.message.Packet;

public interface BoundedPacketQueue {
  String getName();
  
  int size();
  
  void close();
  
  void enqueueNoBlock(Packet paramPacket) throws JMSException;
  
  void enqueue(Packet paramPacket) throws InterruptedException, JMSException;
  
  Packet dequeue() throws InterruptedException, JMSException;
  
  Packet dequeue(long paramLong) throws InterruptedException, JMSException;
  
  Packet dequeueNoWait() throws InterruptedException, JMSException;
  
  boolean isStarted();
  
  void stop();
  
  void start();
  
  boolean isEmpty();
  
  void clear();
  
  List getContents();
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\messag\\util\BoundedPacketQueue.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */