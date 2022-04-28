package org.codehaus.activemq.message;

import javax.jms.JMSException;

public abstract class TextWireFormat extends WireFormat {
  public abstract String toString(Packet paramPacket) throws JMSException;
  
  public abstract Packet fromString(String paramString) throws JMSException;
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\TextWireFormat.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */