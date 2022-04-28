package org.codehaus.activemq.capacity;

public interface CapacityMonitor {
  String getName();
  
  void setName(String paramString);
  
  int getRoundingFactor();
  
  void setRoundingFactor(int paramInt);
  
  void addCapacityEventListener(CapacityMonitorEventListener paramCapacityMonitorEventListener);
  
  void removeCapacityEventListener(CapacityMonitorEventListener paramCapacityMonitorEventListener);
  
  int getCurrentCapacity();
  
  int getRoundedCapacity();
  
  long getCurrentValue();
  
  void setCurrentValue(long paramLong);
  
  long getValueLimit();
  
  void setValueLimit(long paramLong);
  
  public static class BasicCapacityMonitor {}
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\capacity\CapacityMonitor.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */