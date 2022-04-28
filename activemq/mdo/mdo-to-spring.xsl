<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet
  xmlns:xsl='http://www.w3.org/1999/XSL/Transform'
  version='1.0'>

  <xsl:output method="xml" indent="yes" encoding="ISO-8859-1"
    doctype-public="-//SPRING//DTD BEAN//EN"
    doctype-system="http://www.springframework.org/dtd/spring-beans.dtd"/>


  <xsl:template match="*">
    <xsl:copy>
      <xsl:copy-of select="attribute::*"/>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="container">
    <!-- TODO allow class to be customized -->
    <bean id="brokerConnector" class="org.codehaus.activemq.broker.impl.BrokerContainerImpl" init-method="start" destroy-method="stop">
      <property name="connectors">
        <list>
          <xsl:apply-templates/>
        </list>
      </property>
    </bean>
  </xsl:template>

  <xsl:template match="connector">
    <!-- TODO allow class to be customized -->
    <bean class="org.codehaus.activemq.broker.impl.broker.impl.BrokerConnectorImpl" autowire="constructor" init-method="start" destroy-method="stop">
      <constructor-arg>
        <description>Broker</description>
        <ref bean="brokerConnector"/>
      </constructor-arg>
      <constructor-arg>
        <description>URI to connect to</description>
        <value>
          <xsl:value-of select="@uri"/>
        </value>
      </constructor-arg>
      <constructor-arg>
        <description>Wire protocol</description>

        <!-- TODO allow wire protocol to be customized -->
        <xsl:choose>
          <xsl:when test="wireFormat">
            <apply-template select="wireFormat"/>
          </xsl:when>
          <xsl:otherwise>
            <bean class="org.codehaus.activemq.message.DefaultWireFormat"/>
          </xsl:otherwise>
        </xsl:choose>
      </constructor-arg>

      <xsl:apply-templates/>
    </bean>
  </xsl:template>

</xsl:stylesheet>
