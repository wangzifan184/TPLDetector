package org.codehaus.activemq.message;

import java.io.DataInput;
import java.io.IOException;

public interface PacketReader {
  int getPacketType();
  
  boolean canRead(int paramInt);
  
  Packet createPacket();
  
  void buildPacket(Packet paramPacket, DataInput paramDataInput) throws IOException;
  
  Packet readPacketFromByteArray(byte[] paramArrayOfbyte) throws IOException;
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\PacketReader.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */