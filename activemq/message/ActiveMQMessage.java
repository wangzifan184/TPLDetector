/*      */ package org.codehaus.activemq.message;
/*      */ 
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.DataInput;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.DataOutput;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import javax.jms.Destination;
/*      */ import javax.jms.JMSException;
/*      */ import javax.jms.Message;
/*      */ import javax.jms.MessageFormatException;
/*      */ import javax.jms.MessageNotWriteableException;
/*      */ import org.codehaus.activemq.service.MessageIdentity;
/*      */ import org.codehaus.activemq.util.IdGenerator;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ActiveMQMessage
/*      */   extends AbstractPacket
/*      */   implements Message, Comparable
/*      */ {
/*      */   static final int DEFAULT_DELIVERY_MODE = 2;
/*      */   static final int DEFAULT_PRIORITY = 4;
/*      */   static final long DEFAULT_TIME_TO_LIVE = 0L;
/*      */   static final byte EOF = 2;
/*      */   static final byte BYTES = 3;
/*      */   static final byte STRING = 4;
/*      */   static final byte BOOLEAN = 5;
/*      */   static final byte CHAR = 6;
/*      */   static final byte BYTE = 7;
/*      */   static final byte SHORT = 8;
/*      */   static final byte INT = 9;
/*      */   static final byte LONG = 10;
/*      */   static final byte FLOAT = 11;
/*      */   static final byte DOUBLE = 12;
/*      */   static final byte NULL = 13;
/*      */   static final int CORRELATION_INDEX = 2;
/*      */   static final int TYPE_INDEX = 3;
/*      */   static final int BROKER_NAME_INDEX = 4;
/*      */   static final int CLUSTER_NAME_INDEX = 5;
/*      */   static final int TRANSACTION_ID_INDEX = 6;
/*      */   static final int REPLY_TO_INDEX = 7;
/*      */   static final int TIMESTAMP_INDEX = 8;
/*      */   static final int EXPIRATION_INDEX = 9;
/*      */   static final int REDELIVERED_INDEX = 10;
/*      */   static final int XA_TRANS_INDEX = 11;
/*      */   static final int CID_INDEX = 12;
/*      */   static final int PROPERTIES_INDEX = 13;
/*      */   static final int PAYLOAD_INDEX = 15;
/*      */   protected boolean readOnlyMessage;
/*      */   private String jmsClientID;
/*      */   private String jmsCorrelationID;
/*      */   private ActiveMQDestination jmsDestination;
/*      */   private ActiveMQDestination jmsReplyTo;
/*  618 */   private int jmsDeliveryMode = 2;
/*      */   private boolean jmsRedelivered;
/*      */   private String jmsType;
/*      */   private long jmsExpiration;
/*  622 */   private int jmsPriority = 4;
/*      */   
/*      */   private long jmsTimestamp;
/*      */   
/*      */   private Hashtable properties;
/*      */   
/*      */   private boolean readOnlyProperties;
/*      */   
/*      */   private String entryBrokerName;
/*      */   
/*      */   private String entryClusterName;
/*      */   
/*      */   private int[] consumerNos;
/*      */   
/*      */   private String producerID;
/*      */   private String transactionId;
/*      */   private boolean xaTransacted;
/*      */   private String consumerId;
/*      */   private boolean messageConsumed;
/*      */   private boolean transientConsumed;
/*      */   private MessageAcknowledge messageAcknowledge;
/*      */   private byte[] bodyAsBytes;
/*      */   private MessageIdentity jmsMessageIdentity;
/*      */   
/*      */   public boolean isJMSMessage() {
/*  647 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  655 */     return getPacketTypeAsString(getPacketType()) + ": dest = " + this.jmsDestination + ", id = " + getId();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MessageAcknowledge getMessageAcknowledge() {
/*  663 */     return this.messageAcknowledge;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMessageAcknowledge(MessageAcknowledge messageAcknowledge) {
/*  670 */     this.messageAcknowledge = messageAcknowledge;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getPacketType() {
/*  680 */     return 6;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setReadOnly(boolean value) {
/*  690 */     this.readOnlyProperties = value;
/*  691 */     this.readOnlyMessage = value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isConsumerTarget(int consumerNumber) {
/*  703 */     if (this.consumerNos != null) {
/*  704 */       for (int i = 0; i < this.consumerNos.length; i++) {
/*  705 */         if (this.consumerNos[i] == consumerNumber) {
/*  706 */           return true;
/*      */         }
/*      */       } 
/*      */     }
/*  710 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getConsumerNosAsString() {
/*  717 */     String result = "";
/*  718 */     if (this.consumerNos != null) {
/*  719 */       for (int i = 0; i < this.consumerNos.length; i++) {
/*  720 */         result = result + this.consumerNos[i] + ",";
/*      */       }
/*      */     }
/*  723 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTemporary() {
/*  730 */     return (this.jmsDeliveryMode == 1 || (this.jmsDestination != null && this.jmsDestination.isTemporary()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  739 */     return (getId() != null) ? getId().hashCode() : super.hashCode();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object obj) {
/*  750 */     boolean result = (obj == this);
/*  751 */     if (!result && obj != null && obj instanceof ActiveMQMessage) {
/*  752 */       ActiveMQMessage other = (ActiveMQMessage)obj;
/*  753 */       result = (getId() == other.getId() || (getId() != null && other.getId() != null && getId().equals(other.getId())));
/*      */     } 
/*      */ 
/*      */     
/*  757 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int compareTo(Object o) {
/*  765 */     if (o instanceof ActiveMQMessage) {
/*  766 */       return compareTo((ActiveMQMessage)o);
/*      */     }
/*  768 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int compareTo(ActiveMQMessage that) {
/*  778 */     int answer = 1;
/*      */     
/*  780 */     if (that != null && this.jmsDestination != null && that.jmsDestination != null) {
/*  781 */       answer = this.jmsDestination.compareTo(that.jmsDestination);
/*  782 */       if (answer == 0) {
/*  783 */         if (getId() != null && that.getId() != null) {
/*  784 */           answer = IdGenerator.compare(getId(), that.getId());
/*      */         } else {
/*      */           
/*  787 */           answer = 1;
/*      */         } 
/*      */       }
/*      */     } 
/*  791 */     return answer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ActiveMQMessage shallowCopy() throws JMSException {
/*  801 */     ActiveMQMessage other = new ActiveMQMessage();
/*  802 */     initializeOther(other);
/*  803 */     return other;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ActiveMQMessage deepCopy() throws JMSException {
/*  812 */     return shallowCopy();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isExpired(long currentTime) {
/*  824 */     boolean result = false;
/*  825 */     long expiration = this.jmsExpiration;
/*  826 */     if (expiration > 0L && expiration < currentTime) {
/*  827 */       result = true;
/*      */     }
/*  829 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isExpired() {
/*  836 */     return isExpired(System.currentTimeMillis());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initializeOther(ActiveMQMessage other) {
/*  845 */     initializeOther(other);
/*  846 */     other.jmsClientID = this.jmsClientID;
/*  847 */     other.jmsCorrelationID = this.jmsCorrelationID;
/*  848 */     other.jmsDestination = this.jmsDestination;
/*  849 */     other.jmsReplyTo = this.jmsReplyTo;
/*  850 */     other.jmsDeliveryMode = this.jmsDeliveryMode;
/*  851 */     other.jmsRedelivered = this.jmsRedelivered;
/*  852 */     other.jmsType = this.jmsType;
/*  853 */     other.jmsExpiration = this.jmsExpiration;
/*  854 */     other.jmsPriority = this.jmsPriority;
/*  855 */     other.jmsTimestamp = this.jmsTimestamp;
/*  856 */     other.properties = this.properties;
/*  857 */     other.readOnlyProperties = this.readOnlyProperties;
/*  858 */     other.readOnlyMessage = this.readOnlyMessage;
/*  859 */     other.entryBrokerName = this.entryBrokerName;
/*  860 */     other.entryClusterName = this.entryClusterName;
/*  861 */     other.consumerNos = this.consumerNos;
/*  862 */     other.producerID = this.producerID;
/*  863 */     other.transactionId = this.transactionId;
/*  864 */     other.xaTransacted = this.xaTransacted;
/*  865 */     other.bodyAsBytes = this.bodyAsBytes;
/*  866 */     other.messageAcknowledge = this.messageAcknowledge;
/*  867 */     other.jmsMessageIdentity = this.jmsMessageIdentity;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getJMSMessageID() {
/*  911 */     return getId();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setJMSMessageID(String id) {
/*  926 */     setId(id);
/*  927 */     this.jmsMessageIdentity = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getJMSTimestamp() {
/*  963 */     return this.jmsTimestamp;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setJMSTimestamp(long timestamp) {
/*  978 */     this.jmsTimestamp = timestamp;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getJMSCorrelationIDAsBytes() {
/*  995 */     return (this.jmsCorrelationID != null) ? this.jmsCorrelationID.getBytes() : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setJMSCorrelationIDAsBytes(byte[] correlationID) {
/* 1023 */     if (correlationID == null) {
/* 1024 */       this.jmsCorrelationID = null;
/*      */     } else {
/*      */       
/* 1027 */       this.jmsCorrelationID = new String(correlationID);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setJMSCorrelationID(String correlationID) {
/* 1073 */     this.jmsCorrelationID = correlationID;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getJMSCorrelationID() {
/* 1091 */     return this.jmsCorrelationID;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Destination getJMSReplyTo() {
/* 1105 */     return this.jmsReplyTo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setJMSReplyTo(Destination replyTo) {
/* 1139 */     this.jmsReplyTo = (ActiveMQDestination)replyTo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Destination getJMSDestination() {
/* 1161 */     return this.jmsDestination;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setJMSDestination(Destination destination) {
/* 1176 */     this.jmsDestination = (ActiveMQDestination)destination;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getJMSDeliveryMode() {
/* 1189 */     return this.jmsDeliveryMode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setJMSDeliveryMode(int deliveryMode) {
/* 1205 */     this.jmsDeliveryMode = deliveryMode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getJMSRedelivered() {
/* 1223 */     return this.jmsRedelivered;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setJMSRedelivered(boolean redelivered) {
/* 1240 */     this.jmsRedelivered = redelivered;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getJMSType() {
/* 1253 */     return this.jmsType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setJMSType(String type) {
/* 1285 */     this.jmsType = type;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getJMSExpiration() {
/* 1316 */     return this.jmsExpiration;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setJMSExpiration(long expiration) {
/* 1331 */     this.jmsExpiration = expiration;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getJMSPriority() {
/* 1353 */     return this.jmsPriority;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setJMSPriority(int priority) {
/* 1368 */     this.jmsPriority = priority;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void clearProperties() {
/* 1378 */     if (this.properties != null) {
/* 1379 */       this.properties.clear();
/*      */     }
/* 1381 */     this.readOnlyProperties = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean propertyExists(String name) {
/* 1393 */     return (this.properties != null) ? this.properties.containsKey(name) : false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getBooleanProperty(String name) throws JMSException {
/* 1409 */     return vanillaToBoolean(this.properties, name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getByteProperty(String name) throws JMSException {
/* 1425 */     return vanillaToByte(this.properties, name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short getShortProperty(String name) throws JMSException {
/* 1441 */     return vanillaToShort(this.properties, name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getIntProperty(String name) throws JMSException {
/* 1457 */     return vanillaToInt(this.properties, name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLongProperty(String name) throws JMSException {
/* 1473 */     return vanillaToLong(this.properties, name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getFloatProperty(String name) throws JMSException {
/* 1489 */     return vanillaToFloat(this.properties, name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getDoubleProperty(String name) throws JMSException {
/* 1505 */     return vanillaToDouble(this.properties, name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getStringProperty(String name) throws JMSException {
/* 1522 */     return vanillaToString(this.properties, name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getObjectProperty(String name) {
/* 1543 */     return (this.properties != null) ? this.properties.get(name) : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Enumeration getPropertyNames() {
/* 1557 */     if (this.properties == null) {
/* 1558 */       this.properties = new Hashtable();
/*      */     }
/* 1560 */     return this.properties.keys();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Hashtable getProperties() {
/* 1570 */     return this.properties;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setProperties(Hashtable newProperties) {
/* 1581 */     this.properties = newProperties;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBooleanProperty(String name, boolean value) throws JMSException {
/* 1599 */     prepareProperty(name);
/* 1600 */     this.properties.put(name, value ? Boolean.TRUE : Boolean.FALSE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setByteProperty(String name, byte value) throws JMSException {
/* 1618 */     prepareProperty(name);
/* 1619 */     this.properties.put(name, new Byte(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setShortProperty(String name, short value) throws JMSException {
/* 1637 */     prepareProperty(name);
/* 1638 */     this.properties.put(name, new Short(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIntProperty(String name, int value) throws JMSException {
/* 1656 */     prepareProperty(name);
/* 1657 */     this.properties.put(name, new Integer(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLongProperty(String name, long value) throws JMSException {
/* 1675 */     prepareProperty(name);
/* 1676 */     this.properties.put(name, new Long(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFloatProperty(String name, float value) throws JMSException {
/* 1694 */     prepareProperty(name);
/* 1695 */     this.properties.put(name, new Float(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDoubleProperty(String name, double value) throws JMSException {
/* 1714 */     prepareProperty(name);
/* 1715 */     this.properties.put(name, new Double(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setStringProperty(String name, String value) throws JMSException {
/* 1733 */     prepareProperty(name);
/* 1734 */     if (value == null) {
/* 1735 */       this.properties.remove(name);
/*      */     } else {
/*      */       
/* 1738 */       this.properties.put(name, value);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setObjectProperty(String name, Object value) throws JMSException {
/* 1762 */     prepareProperty(name);
/* 1763 */     if (value == null) {
/* 1764 */       this.properties.remove(name);
/*      */     
/*      */     }
/* 1767 */     else if (value instanceof Number || value instanceof Character || value instanceof Boolean || value instanceof String) {
/*      */ 
/*      */ 
/*      */       
/* 1771 */       this.properties.put(name, value);
/*      */     } else {
/*      */       
/* 1774 */       throw new MessageFormatException("Cannot set property to type: " + value.getClass().getName());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void acknowledge() throws JMSException {
/* 1810 */     if (this.messageAcknowledge != null) {
/* 1811 */       this.messageAcknowledge.acknowledge();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clearBody() throws JMSException {
/* 1829 */     this.readOnlyMessage = false;
/* 1830 */     this.bodyAsBytes = null;
/*      */   }
/*      */   
/*      */   boolean vanillaToBoolean(Hashtable table, String name) throws JMSException {
/* 1834 */     boolean result = false;
/* 1835 */     if (table != null) {
/* 1836 */       Object value = table.get(name);
/* 1837 */       if (value != null) {
/* 1838 */         if (value instanceof Boolean) {
/* 1839 */           result = ((Boolean)value).booleanValue();
/*      */         }
/* 1841 */         else if (value instanceof String) {
/*      */           
/* 1843 */           result = Boolean.valueOf((String)value).booleanValue();
/*      */         } else {
/*      */           
/* 1846 */           throw new MessageFormatException(name + " not a Boolean type");
/*      */         } 
/*      */       }
/*      */     } 
/* 1850 */     return result;
/*      */   }
/*      */   
/*      */   byte vanillaToByte(Hashtable table, String name) throws JMSException {
/* 1854 */     byte result = 0;
/* 1855 */     if (table != null) {
/* 1856 */       Object value = table.get(name);
/* 1857 */       if (value != null) {
/* 1858 */         if (value instanceof Byte) {
/* 1859 */           result = ((Byte)value).byteValue();
/*      */         }
/* 1861 */         else if (value instanceof String) {
/* 1862 */           result = Byte.valueOf((String)value).byteValue();
/*      */         } else {
/*      */           
/* 1865 */           throw new MessageFormatException(name + " not a Byte type");
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 1870 */         throw new NumberFormatException("Cannot interpret null as a Byte");
/*      */       } 
/*      */     } 
/* 1873 */     return result;
/*      */   }
/*      */   
/*      */   short vanillaToShort(Hashtable table, String name) throws JMSException {
/* 1877 */     short result = 0;
/* 1878 */     if (table != null) {
/* 1879 */       Object value = table.get(name);
/* 1880 */       if (value != null) {
/* 1881 */         if (value instanceof Short) {
/* 1882 */           result = ((Short)value).shortValue();
/*      */         } else {
/* 1884 */           if (value instanceof String) {
/* 1885 */             return Short.valueOf((String)value).shortValue();
/*      */           }
/* 1887 */           if (value instanceof Byte) {
/* 1888 */             result = (short)((Byte)value).byteValue();
/*      */           } else {
/*      */             
/* 1891 */             throw new MessageFormatException(name + " not a Short type");
/*      */           } 
/*      */         } 
/*      */       } else {
/* 1895 */         throw new NumberFormatException(name + " is null");
/*      */       } 
/*      */     } 
/* 1898 */     return result;
/*      */   }
/*      */   
/*      */   int vanillaToInt(Hashtable table, String name) throws JMSException {
/* 1902 */     int result = 0;
/* 1903 */     if (table != null) {
/* 1904 */       Object value = table.get(name);
/* 1905 */       if (value != null) {
/* 1906 */         if (value instanceof Integer) {
/* 1907 */           result = ((Integer)value).intValue();
/*      */         }
/* 1909 */         else if (value instanceof String) {
/* 1910 */           result = Integer.valueOf((String)value).intValue();
/*      */         }
/* 1912 */         else if (value instanceof Byte) {
/* 1913 */           result = ((Byte)value).intValue();
/*      */         }
/* 1915 */         else if (value instanceof Short) {
/* 1916 */           result = ((Short)value).intValue();
/*      */         } else {
/*      */           
/* 1919 */           throw new MessageFormatException(name + " not an Integer type");
/*      */         } 
/*      */       } else {
/*      */         
/* 1923 */         throw new NumberFormatException(name + " is null");
/*      */       } 
/*      */     } 
/* 1926 */     return result;
/*      */   }
/*      */   
/*      */   long vanillaToLong(Hashtable table, String name) throws JMSException {
/* 1930 */     long result = 0L;
/* 1931 */     if (table != null) {
/* 1932 */       Object value = table.get(name);
/* 1933 */       if (value != null) {
/* 1934 */         if (value instanceof Long) {
/* 1935 */           result = ((Long)value).longValue();
/*      */         }
/* 1937 */         else if (value instanceof String) {
/*      */           
/* 1939 */           result = Long.valueOf((String)value).longValue();
/*      */         }
/* 1941 */         else if (value instanceof Byte) {
/* 1942 */           result = ((Byte)value).byteValue();
/*      */         }
/* 1944 */         else if (value instanceof Short) {
/* 1945 */           result = ((Short)value).shortValue();
/*      */         }
/* 1947 */         else if (value instanceof Integer) {
/* 1948 */           result = ((Integer)value).intValue();
/*      */         } else {
/*      */           
/* 1951 */           throw new MessageFormatException(name + " not a Long type");
/*      */         } 
/*      */       } else {
/*      */         
/* 1955 */         throw new NumberFormatException(name + " is null");
/*      */       } 
/*      */     } 
/* 1958 */     return result;
/*      */   }
/*      */   
/*      */   float vanillaToFloat(Hashtable table, String name) throws JMSException {
/* 1962 */     float result = 0.0F;
/* 1963 */     if (table != null) {
/* 1964 */       Object value = table.get(name);
/* 1965 */       if (value != null) {
/* 1966 */         if (value instanceof Float) {
/* 1967 */           result = ((Float)value).floatValue();
/*      */         }
/* 1969 */         else if (value instanceof String) {
/* 1970 */           result = Float.valueOf((String)value).floatValue();
/*      */         } else {
/*      */           
/* 1973 */           throw new MessageFormatException(name + " not a Float type: " + value.getClass());
/*      */         } 
/*      */       } else {
/*      */         
/* 1977 */         throw new NullPointerException(name + " is null");
/*      */       } 
/*      */     } 
/* 1980 */     return result;
/*      */   }
/*      */   
/*      */   double vanillaToDouble(Hashtable table, String name) throws JMSException {
/* 1984 */     double result = 0.0D;
/* 1985 */     if (table != null) {
/* 1986 */       Object value = table.get(name);
/* 1987 */       if (value != null) {
/* 1988 */         if (value instanceof Double) {
/* 1989 */           result = ((Double)value).doubleValue();
/*      */         }
/* 1991 */         else if (value instanceof String) {
/* 1992 */           result = Double.valueOf((String)value).doubleValue();
/*      */         }
/* 1994 */         else if (value instanceof Float) {
/* 1995 */           result = ((Float)value).floatValue();
/*      */         } else {
/*      */           
/* 1998 */           throw new MessageFormatException(name + " not a Double type");
/*      */         } 
/*      */       } else {
/*      */         
/* 2002 */         throw new NullPointerException(name + " is null");
/*      */       } 
/*      */     } 
/* 2005 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   String vanillaToString(Hashtable table, String name) throws JMSException {
/* 2010 */     String result = null;
/* 2011 */     if (table != null) {
/* 2012 */       Object value = table.get(name);
/* 2013 */       if (value != null) {
/* 2014 */         if (value instanceof String || value instanceof Number || value instanceof Boolean) {
/* 2015 */           result = value.toString();
/*      */         } else {
/*      */           
/* 2018 */           throw new MessageFormatException(name + " not a String type");
/*      */         } 
/*      */       }
/*      */     } 
/* 2022 */     return result;
/*      */   }
/*      */   
/*      */   private void prepareProperty(String name) throws JMSException {
/* 2026 */     if (name == null) {
/* 2027 */       throw new IllegalArgumentException("Invalid property name: cannot be null");
/*      */     }
/* 2029 */     if (name.length() == 0) {
/* 2030 */       throw new IllegalArgumentException("Invalid property name: cannot be empty");
/*      */     }
/* 2032 */     if (this.readOnlyProperties) {
/* 2033 */       throw new MessageNotWriteableException("Properties are read-only");
/*      */     }
/* 2035 */     if (this.properties == null) {
/* 2036 */       this.properties = new Hashtable();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getEntryBrokerName() {
/* 2044 */     return this.entryBrokerName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEntryBrokerName(String newEntryBrokerName) {
/* 2051 */     this.entryBrokerName = newEntryBrokerName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getEntryClusterName() {
/* 2058 */     return this.entryClusterName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEntryClusterName(String newEntryClusterName) {
/* 2065 */     this.entryClusterName = newEntryClusterName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int[] getConsumerNos() {
/* 2072 */     return this.consumerNos;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConsumerNos(int[] newConsumerNos) {
/* 2079 */     this.consumerNos = newConsumerNos;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getJMSClientID() {
/* 2086 */     return this.jmsClientID;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setJMSClientID(String newJmsClientID) {
/* 2093 */     this.jmsClientID = newJmsClientID;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getProducerID() {
/* 2100 */     return this.producerID;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setProducerID(String newProducerID) {
/* 2107 */     this.producerID = newProducerID;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPartOfTransaction() {
/* 2116 */     return (this.transactionId != null && this.transactionId.length() > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTransactionId() {
/* 2123 */     return this.transactionId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTransactionId(String newTransactionId) {
/* 2130 */     this.transactionId = newTransactionId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getConsumerId() {
/* 2137 */     return this.consumerId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConsumerId(String consId) {
/* 2144 */     this.consumerId = consId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isMessageConsumed() {
/* 2151 */     return this.messageConsumed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMessageConsumed(boolean messageConsumed) {
/* 2158 */     this.messageConsumed = messageConsumed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void prepareMessageBody() throws JMSException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void convertBodyToBytes() throws IOException {
/* 2176 */     if (this.bodyAsBytes == null) {
/* 2177 */       ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
/* 2178 */       DataOutputStream dataOut = new DataOutputStream(bytesOut);
/* 2179 */       writeBody(dataOut);
/* 2180 */       dataOut.flush();
/* 2181 */       this.bodyAsBytes = bytesOut.toByteArray();
/* 2182 */       dataOut.close();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void buildBodyFromBytes() throws IOException {
/* 2192 */     if (this.bodyAsBytes != null) {
/* 2193 */       ByteArrayInputStream bytesIn = new ByteArrayInputStream(this.bodyAsBytes);
/* 2194 */       DataInputStream dataIn = new DataInputStream(bytesIn);
/* 2195 */       readBody(dataIn);
/* 2196 */       dataIn.close();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void writeBody(DataOutput dataOut) throws IOException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void readBody(DataInput dataIn) throws IOException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getBodyAsBytes() throws IOException {
/* 2227 */     if (this.bodyAsBytes == null) {
/* 2228 */       convertBodyToBytes();
/*      */     }
/* 2230 */     return this.bodyAsBytes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBodyAsBytes(byte[] bodyAsBytes) {
/* 2237 */     this.bodyAsBytes = bodyAsBytes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void writeMapProperties(Hashtable table, DataOutput dataOut) throws IOException {
/* 2249 */     if (table != null) {
/* 2250 */       dataOut.writeShort(table.size());
/* 2251 */       for (Enumeration iter = table.keys(); iter.hasMoreElements(); ) {
/* 2252 */         String key = iter.nextElement().toString();
/* 2253 */         dataOut.writeUTF(key);
/* 2254 */         Object value = table.get(key);
/*      */         
/* 2256 */         if (value instanceof byte[]) {
/* 2257 */           byte[] data = (byte[])value;
/* 2258 */           dataOut.write(3);
/* 2259 */           if (data != null) {
/* 2260 */             dataOut.writeInt(data.length);
/* 2261 */             dataOut.write(data);
/*      */             continue;
/*      */           } 
/* 2264 */           dataOut.writeInt(-1);
/*      */           continue;
/*      */         } 
/* 2267 */         if (value instanceof Byte) {
/* 2268 */           dataOut.write(7);
/* 2269 */           Byte v = (Byte)value;
/* 2270 */           dataOut.writeByte(v.byteValue()); continue;
/*      */         } 
/* 2272 */         if (value instanceof Boolean) {
/* 2273 */           dataOut.write(5);
/* 2274 */           Boolean v = (Boolean)value;
/* 2275 */           dataOut.writeBoolean(v.booleanValue()); continue;
/*      */         } 
/* 2277 */         if (value instanceof String) {
/* 2278 */           dataOut.write(4);
/* 2279 */           dataOut.writeUTF(value.toString()); continue;
/*      */         } 
/* 2281 */         if (value instanceof Character) {
/* 2282 */           dataOut.write(6);
/* 2283 */           Character v = (Character)value;
/* 2284 */           dataOut.writeChar(v.charValue()); continue;
/*      */         } 
/* 2286 */         if (value instanceof Number) {
/* 2287 */           Number v = (Number)value;
/*      */           
/* 2289 */           if (value instanceof Long) {
/* 2290 */             dataOut.write(10);
/* 2291 */             dataOut.writeLong(v.longValue()); continue;
/*      */           } 
/* 2293 */           if (value instanceof Integer) {
/* 2294 */             dataOut.write(9);
/* 2295 */             dataOut.writeInt(v.intValue()); continue;
/*      */           } 
/* 2297 */           if (value instanceof Short) {
/* 2298 */             dataOut.write(8);
/* 2299 */             dataOut.writeShort(v.shortValue()); continue;
/*      */           } 
/* 2301 */           if (value instanceof Float) {
/* 2302 */             dataOut.write(11);
/* 2303 */             dataOut.writeFloat(v.floatValue()); continue;
/*      */           } 
/* 2305 */           if (value instanceof Double) {
/* 2306 */             dataOut.write(12);
/* 2307 */             dataOut.writeDouble(v.doubleValue());
/*      */           } 
/*      */           continue;
/*      */         } 
/* 2311 */         throw new RuntimeException("Do not know how to parse value of type: " + value.getClass());
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 2317 */       dataOut.writeShort(-1);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Hashtable readMapProperties(DataInput dataIn) throws IOException {
/* 2327 */     Hashtable result = null;
/* 2328 */     int size = dataIn.readShort();
/* 2329 */     if (size > -1) {
/* 2330 */       result = new Hashtable();
/* 2331 */       for (int i = 0; i < size; i++) {
/* 2332 */         String key = dataIn.readUTF();
/* 2333 */         Object value = null;
/* 2334 */         int type = dataIn.readByte();
/* 2335 */         if (type == 3) {
/* 2336 */           byte[] data = null;
/* 2337 */           int dataSize = dataIn.readInt();
/* 2338 */           if (dataSize > -1) {
/* 2339 */             data = new byte[dataSize];
/* 2340 */             dataIn.readFully(data);
/*      */           } 
/* 2342 */           value = data;
/*      */         }
/* 2344 */         else if (type == 7) {
/* 2345 */           value = new Byte(dataIn.readByte());
/*      */         }
/* 2347 */         else if (type == 5) {
/* 2348 */           value = dataIn.readBoolean() ? Boolean.TRUE : Boolean.FALSE;
/*      */         }
/* 2350 */         else if (type == 4) {
/* 2351 */           value = dataIn.readUTF();
/*      */         }
/* 2353 */         else if (type == 6) {
/* 2354 */           value = new Character(dataIn.readChar());
/*      */         }
/* 2356 */         else if (type == 10) {
/* 2357 */           value = new Long(dataIn.readLong());
/*      */         }
/* 2359 */         else if (type == 9) {
/* 2360 */           value = new Integer(dataIn.readInt());
/*      */         }
/* 2362 */         else if (type == 8) {
/* 2363 */           value = new Short(dataIn.readShort());
/*      */         }
/* 2365 */         else if (type == 11) {
/* 2366 */           value = new Float(dataIn.readFloat());
/*      */         }
/* 2368 */         else if (type == 12) {
/* 2369 */           value = new Double(dataIn.readDouble());
/*      */         } else {
/*      */           
/* 2372 */           throw new RuntimeException("Do not know how to parse type: " + type);
/*      */         } 
/* 2374 */         result.put(key, value);
/*      */       } 
/*      */     } 
/* 2377 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isXaTransacted() {
/* 2384 */     return this.xaTransacted;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setXaTransacted(boolean xaTransacted) {
/* 2391 */     this.xaTransacted = xaTransacted;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ActiveMQDestination getJMSActiveMQDestination() {
/* 2398 */     return this.jmsDestination;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MessageIdentity getJMSMessageIdentity() {
/* 2406 */     if (this.jmsMessageIdentity == null) {
/* 2407 */       this.jmsMessageIdentity = new MessageIdentity(getId());
/*      */     }
/* 2409 */     return this.jmsMessageIdentity;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setJMSMessageIdentity(MessageIdentity messageIdentity) {
/* 2416 */     this.jmsMessageIdentity = messageIdentity;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEntryBroker(String brokerName) {
/* 2425 */     boolean result = (this.entryBrokerName != null && brokerName != null && this.entryBrokerName.equals(brokerName));
/* 2426 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEntryCluster(String clusterName) {
/* 2435 */     boolean result = (this.entryClusterName != null && clusterName != null && this.entryClusterName.equals(clusterName));
/* 2436 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTransientConsumed() {
/* 2443 */     return this.transientConsumed;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTransientConsumed(boolean transientConsumed) {
/* 2449 */     this.transientConsumed = transientConsumed;
/*      */   }
/*      */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\ActiveMQMessage.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */