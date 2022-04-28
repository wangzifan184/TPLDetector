package org.codehaus.activemq.transport;

import java.util.Map;
import javax.jms.JMSException;
import org.codehaus.activemq.service.Service;

public interface DiscoveryAgent extends Service {
  void setDiscoveryListener(DiscoveryListener paramDiscoveryListener);
  
  void registerService(String paramString, Map paramMap) throws JMSException;
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\DiscoveryAgent.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */