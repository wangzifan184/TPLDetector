/*    */ package org.codehaus.activemq.message;
/*    */ 
/*    */ import java.util.Properties;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BrokerInfo
/*    */   extends AbstractPacket
/*    */ {
/*    */   private String brokerName;
/*    */   private String clusterName;
/*    */   private long startTime;
/*    */   private Properties properties;
/*    */   
/*    */   public int getPacketType() {
/* 41 */     return 21;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getBrokerName() {
/* 49 */     return this.brokerName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setBrokerName(String newBrokerName) {
/* 56 */     this.brokerName = newBrokerName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getClusterName() {
/* 63 */     return this.clusterName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setClusterName(String newClusterName) {
/* 70 */     this.clusterName = newClusterName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Properties getProperties() {
/* 77 */     return this.properties;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setProperties(Properties newProperties) {
/* 84 */     this.properties = newProperties;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long getStartTime() {
/* 91 */     return this.startTime;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setStartTime(long newStartTime) {
/* 98 */     this.startTime = newStartTime;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\message\BrokerInfo.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */