package org.codehaus.activemq.message;

public interface TransactionType {
  public static final int NOT_SET = 0;
  
  public static final int START = 101;
  
  public static final int PRE_COMMIT = 102;
  
  public static final int COMMIT = 103;
  
  public static final int RECOVER = 104;
  
  public static final int ROLLBACK = 105;
  
  public static final int END = 106;
  
  public static final int FORGET = 107;
  
  public static final int JOIN = 108;
  
  public static final int COMMIT_ONE_PHASE = 109;
  
  public static final int XA_RECOVER = 110;
  
  public static final int GET_TX_TIMEOUT = 111;
  
  public static final int SET_TX_TIMEOUT = 112;
  
  public static final int GET_RM_ID = 113;
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\TransactionType.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */