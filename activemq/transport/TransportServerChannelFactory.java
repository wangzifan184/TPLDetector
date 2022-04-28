package org.codehaus.activemq.transport;

import java.net.URI;
import javax.jms.JMSException;
import org.codehaus.activemq.message.WireFormat;

public interface TransportServerChannelFactory {
  TransportServerChannel create(WireFormat paramWireFormat, URI paramURI) throws JMSException;
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\TransportServerChannelFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */