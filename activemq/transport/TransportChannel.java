package org.codehaus.activemq.transport;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import org.codehaus.activemq.message.Packet;
import org.codehaus.activemq.message.PacketListener;
import org.codehaus.activemq.message.Receipt;
import org.codehaus.activemq.service.Service;

public interface TransportChannel extends Service {
  void setPendingStop(boolean paramBoolean);
  
  boolean isPendingStop();
  
  void stop();
  
  void start() throws JMSException;
  
  Receipt send(Packet paramPacket) throws JMSException;
  
  Receipt send(Packet paramPacket, int paramInt) throws JMSException;
  
  void asyncSend(Packet paramPacket) throws JMSException;
  
  void setPacketListener(PacketListener paramPacketListener);
  
  void setExceptionListener(ExceptionListener paramExceptionListener);
  
  boolean isMulticast();
  
  void addTransportStatusEventListener(TransportStatusEventListener paramTransportStatusEventListener);
  
  void removeTransportStatusEventListener(TransportStatusEventListener paramTransportStatusEventListener);
  
  void setClientID(String paramString);
  
  String getClientID();
  
  void setTransportChannelListener(TransportChannelListener paramTransportChannelListener);
  
  boolean isServerSide();
  
  void setServerSide(boolean paramBoolean);
  
  boolean canProcessWireFormatVersion(int paramInt);
  
  int getCurrentWireFormatVersion();
  
  boolean isTransportConnected();
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\TransportChannel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */