package org.codehaus.activemq.message;

public interface Packet {
  public static final int ACTIVEMQ_MESSAGE = 6;
  
  public static final int ACTIVEMQ_TEXT_MESSAGE = 7;
  
  public static final int ACTIVEMQ_OBJECT_MESSAGE = 8;
  
  public static final int ACTIVEMQ_BYTES_MESSAGE = 9;
  
  public static final int ACTIVEMQ_STREAM_MESSAGE = 10;
  
  public static final int ACTIVEMQ_MAP_MESSAGE = 11;
  
  public static final int ACTIVEMQ_MSG_ACK = 15;
  
  public static final int RECEIPT_INFO = 16;
  
  public static final int CONSUMER_INFO = 17;
  
  public static final int PRODUCER_INFO = 18;
  
  public static final int TRANSACTION_INFO = 19;
  
  public static final int XA_TRANSACTION_INFO = 20;
  
  public static final int ACTIVEMQ_BROKER_INFO = 21;
  
  public static final int ACTIVEMQ_CONNECTION_INFO = 22;
  
  public static final int SESSION_INFO = 23;
  
  public static final int DURABLE_UNSUBSCRIBE = 24;
  
  public static final int RESPONSE_RECEIPT_INFO = 25;
  
  public static final int INT_RESPONSE_RECEIPT_INFO = 26;
  
  public static final int CAPACITY_INFO = 27;
  
  public static final int CAPACITY_INFO_REQUEST = 28;
  
  public static final int WIRE_FORMAT_INFO = 29;
  
  int getPacketType();
  
  String getId();
  
  void setId(String paramString);
  
  boolean isReceiptRequired();
  
  boolean isReceipt();
  
  void setReceiptRequired(boolean paramBoolean);
  
  boolean isJMSMessage();
  
  int getMemoryUsage();
  
  void setMemoryUsage(int paramInt);
  
  int incrementMemoryReferenceCount();
  
  int decrementMemoryReferenceCount();
  
  int getMemoryUsageReferenceCount();
  
  void addBrokerVisited(String paramString);
  
  boolean hasVisited(String paramString);
  
  String getBrokersVisitedAsString();
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\Packet.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */