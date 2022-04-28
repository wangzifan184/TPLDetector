/*     */ package org.codehaus.activemq.transport;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.broker.BrokerContainer;
/*     */ import org.codehaus.activemq.service.Service;
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
/*     */ public class NetworkConnector
/*     */   implements Service
/*     */ {
/*  43 */   private static final Log log = LogFactory.getLog(NetworkConnector.class);
/*     */   
/*     */   private BrokerContainer brokerContainer;
/*     */   private TransportChannelListener transportChannelListener;
/*  47 */   private List networkChannels = new ArrayList();
/*     */   
/*     */   public NetworkConnector(BrokerContainer brokerContainer) {
/*  50 */     this.brokerContainer = brokerContainer;
/*     */   }
/*     */   
/*     */   public void start() throws JMSException {
/*  54 */     for (Iterator iter = this.networkChannels.iterator(); iter.hasNext(); ) {
/*  55 */       NetworkChannel networkChannel = iter.next();
/*  56 */       networkChannel.setBrokerContainer(getBrokerContainer());
/*  57 */       networkChannel.start();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stop() throws JMSException {
/*  62 */     for (Iterator iter = this.networkChannels.iterator(); iter.hasNext(); ) {
/*  63 */       NetworkChannel networkChannel = iter.next();
/*     */       try {
/*  65 */         networkChannel.stop();
/*     */       }
/*  67 */       catch (JMSException e) {
/*  68 */         log.warn("Failed to stop network channel: " + e, (Throwable)e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setTransportChannelListener(TransportChannelListener listener) {
/*  74 */     this.transportChannelListener = listener;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BrokerContainer getBrokerContainer() {
/*  81 */     return this.brokerContainer;
/*     */   }
/*     */   
/*     */   public List getNetworkChannels() {
/*  85 */     return this.networkChannels;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNetworkChannels(List networkChannels) {
/*  94 */     this.networkChannels = networkChannels;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NetworkChannel addNetworkChannel(String uri) {
/* 104 */     NetworkChannel networkChannel = new NetworkChannel(this.brokerContainer, uri);
/* 105 */     addNetworkChannel(networkChannel);
/* 106 */     return networkChannel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNetworkChannel(NetworkChannel networkChannel) {
/* 113 */     this.networkChannels.add(networkChannel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeNetworkChannel(NetworkChannel networkChannel) {
/* 120 */     this.networkChannels.remove(networkChannel);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\NetworkConnector.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */