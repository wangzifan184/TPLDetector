/*     */ package org.codehaus.activemq.message;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.jms.Destination;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Queue;
/*     */ import javax.jms.Topic;
/*     */ import org.codehaus.activemq.filter.DestinationFilter;
/*     */ import org.codehaus.activemq.filter.DestinationPath;
/*     */ import org.codehaus.activemq.jndi.JNDIBaseStorable;
/*     */ import org.codehaus.activemq.management.JMSDestinationStats;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ActiveMQDestination
/*     */   extends JNDIBaseStorable
/*     */   implements Destination, Comparable, Serializable
/*     */ {
/*     */   public static final int ACTIVEMQ_TOPIC = 1;
/*     */   public static final int ACTIVEMQ_TEMPORARY_TOPIC = 2;
/*     */   public static final int ACTIVEMQ_QUEUE = 3;
/*     */   public static final int ACTIVEMQ_TEMPORARY_QUEUE = 4;
/*     */   private static final int NULL_DESTINATION = 10;
/*     */   private static final String TEMP_PREFIX = "{TD{";
/*     */   private static final String TEMP_POSTFIX = "}TD}";
/*     */   private static final String COMPOSITE_SEPARATOR = ",";
/*     */   private static final String QUEUE_PREFIX = "queue://";
/*     */   private static final String TOPIC_PREFIX = "topic://";
/*     */   private String physicalName;
/*     */   private transient DestinationFilter filter;
/*     */   private transient JMSDestinationStats stats;
/*     */   private transient String[] paths;
/*     */   
/*     */   public static String inspect(Destination destination) {
/* 145 */     if (destination instanceof Topic) {
/* 146 */       return "Topic(" + destination.toString() + ")";
/*     */     }
/*     */     
/* 149 */     return "Queue(" + destination.toString() + ")";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ActiveMQDestination transformDestination(Destination destination) throws JMSException {
/* 159 */     ActiveMQDestination result = null;
/* 160 */     if (destination != null) {
/* 161 */       if (destination instanceof ActiveMQDestination) {
/* 162 */         result = (ActiveMQDestination)destination;
/*     */       
/*     */       }
/* 165 */       else if (destination instanceof javax.jms.TemporaryQueue) {
/* 166 */         result = new ActiveMQTemporaryQueue(((Queue)destination).getQueueName());
/*     */       }
/* 168 */       else if (destination instanceof javax.jms.TemporaryTopic) {
/* 169 */         result = new ActiveMQTemporaryTopic(((Topic)destination).getTopicName());
/*     */       }
/* 171 */       else if (destination instanceof Queue) {
/* 172 */         result = new ActiveMQTemporaryQueue(((Queue)destination).getQueueName());
/*     */       }
/* 174 */       else if (destination instanceof Topic) {
/* 175 */         result = new ActiveMQTemporaryTopic(((Topic)destination).getTopicName());
/*     */       } 
/*     */     }
/*     */     
/* 179 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeToStream(ActiveMQDestination destination, DataOutput dataOut) throws IOException {
/* 191 */     if (destination != null) {
/* 192 */       dataOut.write(destination.getDestinationType());
/* 193 */       dataOut.writeUTF((destination == null) ? "" : destination.getPhysicalName());
/*     */     } else {
/*     */       
/* 196 */       dataOut.write(10);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ActiveMQDestination readFromStream(DataInput dataIn) throws IOException {
/* 210 */     int type = dataIn.readByte();
/* 211 */     if (type == 10) {
/* 212 */       return null;
/*     */     }
/* 214 */     ActiveMQDestination result = null;
/* 215 */     if (type == 1) {
/* 216 */       result = new ActiveMQTopic();
/*     */     }
/* 218 */     else if (type == 2) {
/* 219 */       result = new ActiveMQTemporaryTopic();
/*     */     }
/* 221 */     else if (type == 3) {
/* 222 */       result = new ActiveMQQueue();
/*     */     } else {
/*     */       
/* 225 */       result = new ActiveMQTemporaryQueue();
/*     */     } 
/* 227 */     result.setPhysicalName(dataIn.readUTF());
/* 228 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String createTemporaryName(String clientId) {
/* 239 */     return "{TD{" + clientId + "}TD}";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getClientId(ActiveMQDestination destination) {
/* 249 */     String answer = null;
/* 250 */     if (destination != null && destination.isTemporary()) {
/* 251 */       String name = destination.getPhysicalName();
/* 252 */       int start = name.indexOf("{TD{");
/* 253 */       if (start >= 0) {
/* 254 */         start += "{TD{".length();
/* 255 */         int stop = name.lastIndexOf("}TD}");
/* 256 */         if (stop > start && stop < name.length()) {
/* 257 */           answer = name.substring(start, stop);
/*     */         }
/*     */       } 
/*     */     } 
/* 261 */     return answer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ActiveMQDestination() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ActiveMQDestination(String name) {
/* 278 */     this.physicalName = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Object o) {
/* 286 */     if (o instanceof ActiveMQDestination) {
/* 287 */       return compareTo((ActiveMQDestination)o);
/*     */     }
/* 289 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(ActiveMQDestination that) {
/* 299 */     int answer = 0;
/* 300 */     if (this.physicalName != that.physicalName) {
/* 301 */       if (this.physicalName == null) {
/* 302 */         return -1;
/*     */       }
/* 304 */       if (that.physicalName == null) {
/* 305 */         return 1;
/*     */       }
/* 307 */       answer = this.physicalName.compareTo(that.physicalName);
/*     */     } 
/* 309 */     if (answer == 0) {
/* 310 */       if (isTopic()) {
/* 311 */         if (that.isQueue()) {
/* 312 */           return 1;
/*     */         
/*     */         }
/*     */       }
/* 316 */       else if (that.isTopic()) {
/* 317 */         return -1;
/*     */       } 
/*     */     }
/*     */     
/* 321 */     return answer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int getDestinationType();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPhysicalName() {
/* 336 */     return this.physicalName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPhysicalName(String newPhysicalName) {
/* 343 */     this.physicalName = newPhysicalName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTemporary() {
/* 353 */     return (getDestinationType() == 2 || getDestinationType() == 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTopic() {
/* 364 */     return (getDestinationType() == 1 || getDestinationType() == 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isQueue() {
/* 374 */     return !isTopic();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isComposite() {
/* 388 */     return (this.physicalName.indexOf(",") > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getChildDestinations() {
/* 398 */     List answer = new ArrayList();
/* 399 */     StringTokenizer iter = new StringTokenizer(this.physicalName, ",");
/* 400 */     while (iter.hasMoreTokens()) {
/* 401 */       String name = iter.nextToken();
/* 402 */       Destination child = null;
/* 403 */       if (name.startsWith("queue://")) {
/* 404 */         child = new ActiveMQQueue(name.substring("queue://".length()));
/*     */       }
/* 406 */       else if (name.startsWith("topic://")) {
/* 407 */         child = new ActiveMQTopic(name.substring("topic://".length()));
/*     */       } else {
/*     */         
/* 410 */         child = createDestination(name);
/*     */       } 
/* 412 */       answer.add(child);
/*     */     } 
/* 414 */     if (answer.size() == 1)
/*     */     {
/*     */       
/* 417 */       answer.set(0, this);
/*     */     }
/* 419 */     return answer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 427 */     return this.physicalName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 435 */     int answer = -889275714;
/*     */     
/* 437 */     if (this.physicalName != null) {
/* 438 */       answer = this.physicalName.hashCode();
/*     */     }
/* 440 */     if (isTopic()) {
/* 441 */       answer ^= 0xFABFAB;
/*     */     }
/* 443 */     return answer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 454 */     boolean result = (this == obj);
/* 455 */     if (!result && obj != null && obj instanceof ActiveMQDestination) {
/* 456 */       ActiveMQDestination other = (ActiveMQDestination)obj;
/* 457 */       result = (getDestinationType() == other.getDestinationType() && this.physicalName.equals(other.physicalName));
/*     */     } 
/*     */     
/* 460 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWildcard() {
/* 468 */     if (this.physicalName != null) {
/* 469 */       return (this.physicalName.indexOf("*") >= 0 || this.physicalName.indexOf(">") >= 0);
/*     */     }
/*     */     
/* 472 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(ActiveMQDestination destination) {
/* 479 */     if (isWildcard()) {
/* 480 */       return getDestinationFilter().matches(destination);
/*     */     }
/*     */     
/* 483 */     return equals(destination);
/*     */   }
/*     */ 
/*     */   
/*     */   public DestinationFilter getDestinationFilter() {
/* 488 */     if (this.filter == null) {
/* 489 */       this.filter = DestinationFilter.parseFilter(this);
/*     */     }
/* 491 */     return this.filter;
/*     */   }
/*     */   
/*     */   public String[] getDestinationPaths() {
/* 495 */     if (this.paths == null) {
/* 496 */       this.paths = DestinationPath.getDestinationPaths(this.physicalName);
/*     */     }
/* 498 */     return this.paths;
/*     */   }
/*     */   
/*     */   public JMSDestinationStats getStats() {
/* 502 */     if (this.stats == null) {
/* 503 */       this.stats = createDestinationStats();
/*     */     }
/* 505 */     return this.stats;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStats(JMSDestinationStats stats) {
/* 510 */     this.stats = stats;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Destination createDestination(String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract JMSDestinationStats createDestinationStats();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void buildFromProperties(Properties props) {
/* 535 */     this.physicalName = props.getProperty("physicalName", this.physicalName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void populateProperties(Properties props) {
/* 546 */     props.put("physicalName", this.physicalName);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ActiveMQDestination.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */