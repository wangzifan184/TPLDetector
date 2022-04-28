package org.codehaus.activemq.service;

import javax.jms.JMSException;

public interface Service {
  void start() throws JMSException;
  
  void stop() throws JMSException;
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\service\Service.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */