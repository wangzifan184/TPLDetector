package org.codehaus.activemq;

import java.util.LinkedHashMap;
import javax.jms.Message;
import org.codehaus.activemq.message.Packet;
import org.codehaus.activemq.util.BitArrayBin;
import org.codehaus.activemq.util.IdGenerator;
import org.codehaus.activemq.util.LRUCache;

public class ActiveMQMessageAudit {
  private static final int DEFAULT_WINDOW_SIZE = 1024;

  private static final int MAXIMUM_PRODUCER_COUNT = 128;

  private int windowSize;

  private LinkedHashMap map;

  public ActiveMQMessageAudit() {
    this(1024, 128);
  }

  public ActiveMQMessageAudit(int windowSize, int maximumNumberOfProducersToTrack) {
    this.windowSize = windowSize;
    this.map = (LinkedHashMap)new LRUCache(maximumNumberOfProducersToTrack);
  }

  public boolean isDuplicate(Message message) {
    if (message instanceof Packet)
      return isDuplicate((Packet)message);
    return false;
  }

  public boolean isDuplicate(Packet packet) {
    return isDuplicate(packet.getId());
  }

  public boolean isDuplicate(String id) {
    boolean answer = false;
    String seed = IdGenerator.getSeedFromId(id);
    if (seed != null) {
      BitArrayBin bab = (BitArrayBin)this.map.get(seed);
      if (bab == null) {
        bab = new BitArrayBin(this.windowSize);
        this.map.put(seed, bab);
      }
      long index = IdGenerator.getCountFromId(id);
      if (index >= 0L)
        answer = bab.setBit(index, true);
    }
    return answer;
  }
}
