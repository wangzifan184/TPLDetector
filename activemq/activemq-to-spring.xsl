<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet
  xmlns:xsl='http://www.w3.org/1999/XSL/Transform'
  version='1.0'>

  <xsl:output method="xml" indent="yes" encoding="ISO-8859-1"
    doctype-public="-//SPRING//DTD BEAN//EN"
    doctype-system="http://www.springframework.org/dtd/spring-beans.dtd"/>


  <xsl:param name="brokerName"/>

  <xsl:template match="*">
    <xsl:copy>
      <xsl:copy-of select="attribute::*"/>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="broker">
    <xsl:variable name="type">
      <xsl:choose>
        <xsl:when test="@class">
          <xsl:value-of select="@class"/>
        </xsl:when>
        <xsl:otherwise>org.codehaus.activemq.broker.impl.BrokerContainerImpl</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <bean id="broker" class="{$type}" destroy-method="stop">
      <constructor-arg>
        <description>Unique Name of Broker</description>
        <value>
          <xsl:choose>
            <xsl:when test="@name">
              <xsl:value-of select="@name"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="$brokerName"/>
            </xsl:otherwise>
          </xsl:choose>
        </value>
      </constructor-arg>
      <property name="transportConnectors">
        <list>
          <xsl:apply-templates select="connector"/>
        </list>
      </property>
      <property name="networkConnectors">
        <list>
          <xsl:apply-templates select="networkConnector|discoveryNetworkConnector"/>
        </list>
      </property>
      <xsl:apply-templates select="discoveryAgent"/>
      <xsl:apply-templates select="persistence"/>
      <xsl:apply-templates select="redeliveryPolicy"/>
      <xsl:apply-templates select="security"/>
    </bean>
  </xsl:template>


  <!-- transport connectors -->
  <xsl:template match="connector">
    <xsl:variable name="type">
      <xsl:choose>
        <xsl:when test="@class">
          <xsl:value-of select="@class"/>
        </xsl:when>
        <xsl:otherwise>org.codehaus.activemq.broker.impl.BrokerConnectorImpl</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <bean name="connector" class="{$type}" autowire="constructor">
      <constructor-arg index="0">
        <description>Broker</description>
        <ref bean="broker"/>
      </constructor-arg>
      <constructor-arg index="1">
        <description>Transport Server Channel</description>
        <xsl:apply-templates/>
      </constructor-arg>
    </bean>
  </xsl:template>

  <xsl:template match="serverTransport|tcpServerTransport">
    <xsl:choose>
      <xsl:when test="@class">
        <!-- lets use the Spring way to initialise the transport -->
        <bean name="serverTransport" class="{@class}" autowire="constructor">
          <constructor-arg index="0">
            <xsl:call-template name="makeWireFormat"/>
          </constructor-arg>
          <constructor-arg index="1">
            <bean class="java.net.URI">
              <constructor-arg>
                <value>
                  <xsl:value-of select="@uri"/>
                </value>
              </constructor-arg>
            </bean>
          </constructor-arg>
          <xsl:apply-templates select="@*[local-name() != 'uri']|*" mode="addProperties"/>
        </bean>
      </xsl:when>
      <xsl:otherwise>
        <!-- lets use the factory method -->
        <bean name="serverTransport" class="org.codehaus.activemq.transport.TransportServerChannelProvider"
          factory-method="newInstance">
          <constructor-arg index="0">
            <xsl:call-template name="makeWireFormat"/>
          </constructor-arg>
          <constructor-arg index="1">
            <value>
              <xsl:value-of select="@uri"/>
            </value>
          </constructor-arg>
          <xsl:apply-templates select="@*[local-name() != 'uri']|*" mode="addProperties"/>
        </bean>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>


  <!-- network connectors -->
  <xsl:template match="networkConnector">
    <xsl:variable name="type">
      <xsl:choose>
        <xsl:when test="@class">
          <xsl:value-of select="@class"/>
        </xsl:when>
        <xsl:otherwise>org.codehaus.activemq.transport.NetworkConnector</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <bean name="networkConnector" class="{$type}" autowire="constructor">
      <constructor-arg index="0">
        <description>Broker</description>
        <ref bean="broker"/>
      </constructor-arg>
      <xsl:apply-templates select="@*" mode="addProperties"/>
      <property name="networkChannels">
        <list>
          <xsl:apply-templates select="networkChannel|bean"/>
        </list>
      </property>
    </bean>
  </xsl:template>

  <xsl:template match="networkChannel">
    <xsl:variable name="type">
      <xsl:choose>
        <xsl:when test="@class">
          <xsl:value-of select="@class"/>
        </xsl:when>
        <xsl:otherwise>org.codehaus.activemq.transport.NetworkChannel</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <bean name="networkChannel" class="{$type}" autowire="constructor">
      <xsl:apply-templates select="@*|*" mode="addProperties"/>
    </bean>
  </xsl:template>

  <xsl:template match="discoveryNetworkConnector">
    <xsl:variable name="type">
      <xsl:choose>
        <xsl:when test="@class">
          <xsl:value-of select="@class"/>
        </xsl:when>
        <xsl:otherwise>org.codehaus.activemq.transport.DiscoveryNetworkConnector</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <bean name="discoveryNetworkConnector" class="{$type}" autowire="constructor">
      <constructor-arg index="0">
        <description>Broker</description>
        <ref bean="broker"/>
      </constructor-arg>
      <xsl:apply-templates select="@*" mode="addProperties"/>
    </bean>
  </xsl:template>


  <!-- discovery agents -->
  <xsl:template match="discoveryAgent">
    <property name="discoveryAgent">
      <xsl:apply-templates select="activeClusterDiscovery|zeroconfDiscovery|bean"/>
    </property>
  </xsl:template>

  <xsl:template match="activeClusterDiscovery">
    <xsl:variable name="type">
      <xsl:choose>
        <xsl:when test="@class">
          <xsl:value-of select="@class"/>
        </xsl:when>
        <xsl:otherwise>org.codehaus.activecluster.activemq.ActiveMQDiscoveryAgent</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <bean name="activeClusterDiscovery" class="{$type}" factory-method="newInstance">
      <constructor-arg index="0">
        <value>
          <xsl:value-of select="@uri"/>
        </value>
      </constructor-arg>
      <constructor-arg index="1">
        <value>
          <xsl:value-of select="@subject"/>
        </value>
      </constructor-arg>
      <xsl:apply-templates select="@*[local-name() != 'uri' and local-name() != 'subject']|*" mode="addProperties"/>
    </bean>
  </xsl:template>

  <xsl:template match="zeroconfDiscovery">
    <xsl:variable name="type">
      <xsl:choose>
        <xsl:when test="@class">
          <xsl:value-of select="@class"/>
        </xsl:when>
        <xsl:otherwise>org.codehaus.activemq.transport.zeroconf.ZeroconfDiscoveryAgent</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <bean name="zeroconfDiscovery" class="{$type}" autowire="constructor">
      <xsl:apply-templates select="@*|*" mode="addProperties"/>
    </bean>
  </xsl:template>



  <!-- security adapter -->
  <xsl:template match="security">
    <property name="securityAdapter">
      <xsl:apply-templates select="*"/>
    </property>
  </xsl:template>

  <!-- redelivery policy -->
  <xsl:template match="redeliveryPolicy">
    <property name="redeliveryPolicy">
      <xsl:call-template name="makeBean">
        <xsl:with-param name="defaultType">org.codehaus.activemq.service.RedeliveryPolicy</xsl:with-param>
      </xsl:call-template>
    </property>
  </xsl:template>


  <!-- persistence adapter -->
  <xsl:template match="persistence">
    <property name="persistenceAdapter">
      <xsl:apply-templates select="*"/>
    </property>
  </xsl:template>

  <xsl:template match="berkeleyDbPersistence">
    <xsl:call-template name="makeBean">
      <xsl:with-param name="defaultType">org.codehaus.activemq.store.bdb.BDbPersistenceAdapter</xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="jdbmPersistence">
    <xsl:call-template name="makeBean">
      <xsl:with-param name="defaultType">org.codehaus.activemq.store.jdbm.JdbmPersistenceAdapter</xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="jdbcPersistence">
    <bean name="jdbcPersistence" class="org.codehaus.activemq.store.jdbc.JDBCPersistenceAdapter" autowire="constructor">
      <property name="dataSource">
        <ref bean="{@dataSourceRef}"/>
      </property>
      <xsl:apply-templates select="wireFormat" mode="addProperties"/>
    </bean>
  </xsl:template>

  <xsl:template match="vmPersistence">
    <xsl:call-template name="makeBean">
      <xsl:with-param name="defaultType">org.codehaus.activemq.store.vm.VMPersistenceAdapter</xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  <xsl:template match="howlPersistence">
    <xsl:param name="defaultType"/>
    <xsl:variable name="type">
      <xsl:choose>
        <xsl:when test="@class">
          <xsl:value-of select="@class"/>
        </xsl:when>
        <xsl:otherwise>org.codehaus.activemq.store.howl.HowlPersistenceAdapter</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <bean name="{local-name()}" class="{$type}" autowire="constructor">
      <xsl:apply-templates select="@*" mode="addProperties"/>
      <property name="longTermPersistence">
        <xsl:apply-templates select="*"/>
      </property>
    </bean>
  </xsl:template>

  <xsl:template match="journalPersistence">
    <xsl:param name="defaultType"/>
    <xsl:variable name="type">
      <xsl:choose>
        <xsl:when test="@class">
          <xsl:value-of select="@class"/>
        </xsl:when>
        <xsl:otherwise>org.codehaus.activemq.store.journal.JournalPersistenceAdapter</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <bean name="{local-name()}" class="{$type}" autowire="constructor">
      <xsl:apply-templates select="@*" mode="addProperties"/>
      <property name="longTermPersistence">
        <xsl:apply-templates select="*"/>
      </property>
    </bean>
  </xsl:template>

  <xsl:template match="cachePersistence">
    <xsl:param name="defaultType"/>
    <xsl:variable name="type">
      <xsl:choose>
        <xsl:when test="@class">
          <xsl:value-of select="@class"/>
        </xsl:when>
        <xsl:otherwise>org.codehaus.activemq.store.cache.SimpleCachePersistenceAdapter</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <bean name="{local-name()}" class="{$type}" autowire="constructor">
      <xsl:apply-templates select="@*" mode="addProperties"/>
      <property name="longTermPersistence">
        <xsl:apply-templates select="*"/>
      </property>
    </bean>
  </xsl:template>

  <!-- wire formats -->
  <xsl:template name="makeWireFormat">
    <xsl:choose>
      <xsl:when test="wireFormat">
        <apply-template select="wireFormat"/>
      </xsl:when>
      <xsl:otherwise>
        <bean class="org.codehaus.activemq.message.DefaultWireFormat"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="wireFormat">
    <xsl:variable name="type">
      <xsl:choose>
        <xsl:when test="@class">
          <xsl:value-of select="@class"/>
        </xsl:when>
        <xsl:otherwise>org.codehaus.activemq.broker.message.DefaultWireFormat</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <bean name="wireFormat" class="{$type}">
      <xsl:apply-templates select="@*|*" mode="addProperties"/>
    </bean>
  </xsl:template>


  <!-- general utilities -->
  <xsl:template name="makeBean">
    <xsl:param name="defaultType"/>
    <xsl:variable name="type">
      <xsl:choose>
        <xsl:when test="@class">
          <xsl:value-of select="@class"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$defaultType"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <bean name="{local-name()}" class="{$type}" autowire="constructor">
      <xsl:apply-templates select="@*|*" mode="addProperties"/>
    </bean>
  </xsl:template>


  <xsl:template match="*|@*" mode="addProperties">
    <property name="{local-name()}">
      <value>
        <xsl:value-of select="."/>
      </value>
    </property>
  </xsl:template>

  <xsl:template match="@class|wireFormat" mode="addProperties"/>

</xsl:stylesheet>
