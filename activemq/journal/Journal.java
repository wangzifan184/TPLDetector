package org.codehaus.activemq.journal;

import java.io.IOException;

public interface Journal {
  RecordLocation write(byte[] paramArrayOfbyte, boolean paramBoolean) throws IOException, IllegalStateException;
  
  byte[] read(RecordLocation paramRecordLocation) throws InvalidRecordLocationException, IOException, IllegalStateException;
  
  void setMark(RecordLocation paramRecordLocation, boolean paramBoolean) throws InvalidRecordLocationException, IOException, IllegalStateException;
  
  RecordLocation getMark() throws IllegalStateException;
  
  void close() throws IOException;
  
  RecordLocation getNextRecordLocation(RecordLocation paramRecordLocation) throws InvalidRecordLocationException, IOException, IllegalStateException;
  
  void setJournalEventListener(JournalEventListener paramJournalEventListener) throws IllegalStateException;
}


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\journal\Journal.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */