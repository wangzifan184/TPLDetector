package org.codehaus.activemq.filter;

import javax.jms.Destination;
import javax.jms.JMSException;

public interface FilterFactory {
  Filter createFilter(Destination paramDestination, String paramString) throws JMSException;
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\filter\FilterFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */