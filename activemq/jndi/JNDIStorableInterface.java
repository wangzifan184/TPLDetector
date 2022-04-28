package org.codehaus.activemq.jndi;

import java.util.Properties;
import javax.naming.Referenceable;

public interface JNDIStorableInterface extends Referenceable {
  void setProperties(Properties paramProperties);
  
  Properties getProperties();
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\jndi\JNDIStorableInterface.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */