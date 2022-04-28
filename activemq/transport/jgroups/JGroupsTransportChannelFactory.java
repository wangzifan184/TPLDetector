/*     */ package org.codehaus.activemq.transport.jgroups;
/*     */ 
/*     */ import java.net.URI;
/*     */ import javax.jms.JMSException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.activemq.message.WireFormat;
/*     */ import org.codehaus.activemq.transport.TransportChannel;
/*     */ import org.codehaus.activemq.transport.TransportChannelFactorySupport;
/*     */ import org.codehaus.activemq.util.JMSExceptionHelper;
/*     */ import org.jgroups.Channel;
/*     */ import org.jgroups.ChannelException;
/*     */ import org.jgroups.ChannelFactory;
/*     */ import org.jgroups.JChannelFactory;
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
/*     */ public class JGroupsTransportChannelFactory
/*     */   extends TransportChannelFactorySupport
/*     */ {
/*  40 */   private static final Log log = LogFactory.getLog(JGroupsTransportChannelFactory.class);
/*     */   
/*  42 */   private ChannelFactory channelFactory = (ChannelFactory)new JChannelFactory();
/*     */   private Object channelConfiguration;
/*  44 */   private String channelName = "ActiveMQ";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JGroupsTransportChannelFactory(ChannelFactory channelFactory, Object channelConfiguration, String channelName) {
/*  50 */     this.channelFactory = channelFactory;
/*  51 */     this.channelConfiguration = channelConfiguration;
/*  52 */     this.channelName = channelName;
/*     */   }
/*     */   
/*     */   public TransportChannel create(WireFormat wireFormat, URI remoteLocation) throws JMSException {
/*     */     try {
/*  57 */       Channel channel = createChannel(remoteLocation);
/*  58 */       channel.setOpt(5, Boolean.TRUE);
/*  59 */       channel.connect(this.channelName);
/*  60 */       return populateProperties((TransportChannel)new JGroupsTransportChannel(wireFormat, channel, null), remoteLocation);
/*     */     }
/*  62 */     catch (ChannelException e) {
/*  63 */       throw JMSExceptionHelper.newJMSException("Failed to construct JGroups Channel: " + e, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public TransportChannel create(WireFormat wireFormat, URI remoteLocation, URI localLocation) throws JMSException {
/*  68 */     return create(wireFormat, remoteLocation);
/*     */   }
/*     */   
/*     */   public boolean requiresEmbeddedBroker() {
/*  72 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFactory getChannelFactory() {
/*  80 */     return this.channelFactory;
/*     */   }
/*     */   
/*     */   public void setChannelFactory(ChannelFactory channelFactory) {
/*  84 */     this.channelFactory = channelFactory;
/*     */   }
/*     */   
/*     */   public Object getChannelConfiguration() {
/*  88 */     return this.channelConfiguration;
/*     */   }
/*     */   
/*     */   public void setChannelConfiguration(Object channelConfiguration) {
/*  92 */     this.channelConfiguration = channelConfiguration;
/*     */   }
/*     */   
/*     */   public String getChannelName() {
/*  96 */     return this.channelName;
/*     */   }
/*     */   
/*     */   public void setChannelName(String channelName) {
/* 100 */     this.channelName = channelName;
/*     */   }
/*     */   
/*     */   protected Channel createChannel(URI remoteLocation) throws ChannelException {
/* 104 */     Object config = this.channelConfiguration;
/* 105 */     if (config == null) {
/*     */       
/* 107 */       String text = remoteLocation.getSchemeSpecificPart();
/* 108 */       if (!text.equalsIgnoreCase("default")) {
/* 109 */         config = text;
/*     */       }
/*     */     } 
/* 112 */     log.info("Configuring JGroups with: " + config);
/* 113 */     return this.channelFactory.createChannel(config);
/*     */   }
/*     */   
/*     */   public JGroupsTransportChannelFactory() {}
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activemq\transport\jgroups\JGroupsTransportChannelFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */