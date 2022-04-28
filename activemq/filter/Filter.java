package org.codehaus.activemq.filter;

import javax.jms.JMSException;
import javax.jms.Message;

public interface Filter {
  boolean matches(Message paramMessage) throws JMSException;
  
  boolean isWildcard();
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\filter\Filter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */