/*     */ package org.codehaus.activemq.util;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import javax.jms.Connection;
/*     */ import javax.jms.ConnectionFactory;
/*     */ import javax.jms.JMSException;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NamingException;
/*     */ import org.apache.log4j.helpers.LogLog;
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
/*     */ public class JndiJmsLogAppender
/*     */   extends JmsLogAppenderSupport
/*     */ {
/*     */   private String jndiName;
/*     */   private String userName;
/*     */   private String password;
/*     */   private String initialContextFactoryName;
/*     */   private String providerURL;
/*     */   private String urlPkgPrefixes;
/*     */   private String securityPrincipalName;
/*     */   private String securityCredentials;
/*     */   
/*     */   public String getJndiName() {
/*  52 */     return this.jndiName;
/*     */   }
/*     */   
/*     */   public void setJndiName(String jndiName) {
/*  56 */     this.jndiName = jndiName;
/*     */   }
/*     */   
/*     */   public String getUserName() {
/*  60 */     return this.userName;
/*     */   }
/*     */   
/*     */   public void setUserName(String userName) {
/*  64 */     this.userName = userName;
/*     */   }
/*     */   
/*     */   public String getPassword() {
/*  68 */     return this.password;
/*     */   }
/*     */   
/*     */   public void setPassword(String password) {
/*  72 */     this.password = password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInitialContextFactoryName() {
/*  79 */     return this.initialContextFactoryName;
/*     */   }
/*     */   
/*     */   public void setInitialContextFactoryName(String initialContextFactoryName) {
/*  83 */     this.initialContextFactoryName = initialContextFactoryName;
/*     */   }
/*     */   
/*     */   public String getProviderURL() {
/*  87 */     return this.providerURL;
/*     */   }
/*     */   
/*     */   public void setProviderURL(String providerURL) {
/*  91 */     this.providerURL = providerURL;
/*     */   }
/*     */   
/*     */   public String getUrlPkgPrefixes() {
/*  95 */     return this.urlPkgPrefixes;
/*     */   }
/*     */   
/*     */   public void setUrlPkgPrefixes(String urlPkgPrefixes) {
/*  99 */     this.urlPkgPrefixes = urlPkgPrefixes;
/*     */   }
/*     */   
/*     */   public String getSecurityPrincipalName() {
/* 103 */     return this.securityPrincipalName;
/*     */   }
/*     */   
/*     */   public void setSecurityPrincipalName(String securityPrincipalName) {
/* 107 */     this.securityPrincipalName = securityPrincipalName;
/*     */   }
/*     */   
/*     */   public String getSecurityCredentials() {
/* 111 */     return this.securityCredentials;
/*     */   }
/*     */   
/*     */   public void setSecurityCredentials(String securityCredentials) {
/* 115 */     this.securityCredentials = securityCredentials;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Connection createConnection() throws JMSException, NamingException {
/* 121 */     InitialContext context = createInitialContext();
/* 122 */     LogLog.debug("Looking up ConnectionFactory with jndiName: " + this.jndiName);
/* 123 */     ConnectionFactory factory = (ConnectionFactory)context.lookup(this.jndiName);
/* 124 */     if (factory == null) {
/* 125 */       throw new JMSException("No such ConnectionFactory for name: " + this.jndiName);
/*     */     }
/* 127 */     if (this.userName != null) {
/* 128 */       return factory.createConnection(this.userName, this.password);
/*     */     }
/*     */     
/* 131 */     return factory.createConnection();
/*     */   }
/*     */ 
/*     */   
/*     */   protected InitialContext createInitialContext() throws NamingException {
/* 136 */     if (this.initialContextFactoryName == null) {
/* 137 */       return new InitialContext();
/*     */     }
/*     */     
/* 140 */     Hashtable env = new Hashtable();
/* 141 */     env.put("java.naming.factory.initial", this.initialContextFactoryName);
/* 142 */     if (this.providerURL != null) {
/* 143 */       env.put("java.naming.provider.url", this.providerURL);
/*     */     } else {
/*     */       
/* 146 */       LogLog.warn("You have set InitialContextFactoryName option but not the ProviderURL. This is likely to cause problems.");
/*     */     } 
/*     */     
/* 149 */     if (this.urlPkgPrefixes != null) {
/* 150 */       env.put("java.naming.factory.url.pkgs", this.urlPkgPrefixes);
/*     */     }
/*     */     
/* 153 */     if (this.securityPrincipalName != null) {
/* 154 */       env.put("java.naming.security.principal", this.securityPrincipalName);
/* 155 */       if (this.securityCredentials != null) {
/* 156 */         env.put("java.naming.security.credentials", this.securityCredentials);
/*     */       } else {
/*     */         
/* 159 */         LogLog.warn("You have set SecurityPrincipalName option but not the SecurityCredentials. This is likely to cause problems.");
/*     */       } 
/*     */     } 
/*     */     
/* 163 */     LogLog.debug("Looking up JNDI context with environment: " + env);
/* 164 */     return new InitialContext(env);
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\activemq-1.1.jar!\org\codehaus\activem\\util\JndiJmsLogAppender.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */