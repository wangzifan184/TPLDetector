package org.codehaus.activemq.transport;

import java.net.URI;
import javax.jms.JMSException;
import org.codehaus.activemq.message.WireFormat;

public interface TransportChannelFactory {
  TransportChannel create(WireFormat paramWireFormat, URI paramURI) throws JMSException;
  
  TransportChannel create(WireFormat paramWireFormat, URI paramURI1, URI paramURI2) throws JMSException;
  
  boolean requiresEmbeddedBroker();
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\TransportChannelFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */