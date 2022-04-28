package org.codehaus.activemq.message;

import java.io.DataOutput;
import java.io.IOException;

public interface PacketWriter {
  int getPacketType();
  
  boolean canWrite(Packet paramPacket);
  
  void writePacket(Packet paramPacket, DataOutput paramDataOutput) throws IOException;
  
  byte[] writePacketToByteArray(Packet paramPacket) throws IOException;
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\PacketWriter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */