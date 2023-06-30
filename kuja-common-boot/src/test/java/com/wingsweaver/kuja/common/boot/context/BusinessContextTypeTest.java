package com.wingsweaver.kuja.common.boot.context;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BusinessContextTypeTest {
    @Test
    void test() {
        BusinessContext businessContext = new MapBusinessContext();
        BusinessContextAccessor accessor = new BusinessContextAccessor(businessContext);

        BusinessContextTypeSetter contextTypeSetter = (BusinessContextTypeSetter) businessContext;
        assertEquals("Other", BusinessContextType.OTHER.toString());
        contextTypeSetter.setContextType(BusinessContextType.OTHER);
        assertEquals(BusinessContextType.OTHER, businessContext.getContextType());

        assertEquals("Web", BusinessContextType.Web.WEB.toString());
        contextTypeSetter.setContextType(BusinessContextType.Web.WEB);
        assertEquals(BusinessContextType.Web.WEB, businessContext.getContextType());
        assertTrue(businessContext.isContextType(BusinessContextType.Web.class));

        assertEquals("Web.Blocked.Servlet", BusinessContextType.Web.Blocked.SERVLET.toString());
        contextTypeSetter.setContextType(BusinessContextType.Web.Blocked.SERVLET);
        assertEquals(BusinessContextType.Web.Blocked.SERVLET, businessContext.getContextType());
        assertTrue(businessContext.isContextType(BusinessContextType.Web.class));
        assertTrue(businessContext.isContextType(BusinessContextType.Blocked.class));
        assertTrue(businessContext.isContextType(BusinessContextType.Web.Blocked.class));

        assertEquals("Web.Blocked.Servlet.J2EE", BusinessContextType.Web.Blocked.J2EE.SERVLET.toString());
        contextTypeSetter.setContextType(BusinessContextType.Web.Blocked.J2EE.SERVLET);
        assertEquals(BusinessContextType.Web.Blocked.J2EE.SERVLET, businessContext.getContextType());
        assertTrue(businessContext.isContextType(BusinessContextType.Web.class));
        assertTrue(businessContext.isContextType(BusinessContextType.Blocked.class));
        assertTrue(businessContext.isContextType(BusinessContextType.Web.Blocked.class));
        assertTrue(businessContext.isContextType(BusinessContextType.J2EE.class));
        assertTrue(businessContext.isContextType(BusinessContextType.Web.Blocked.J2EE.class));

        assertEquals("Web.Blocked.Servlet.JakartaEE", BusinessContextType.Web.Blocked.JakartaEE.SERVLET.toString());
        contextTypeSetter.setContextType(BusinessContextType.Web.Blocked.JakartaEE.SERVLET);
        assertEquals(BusinessContextType.Web.Blocked.JakartaEE.SERVLET, businessContext.getContextType());
        assertTrue(businessContext.isContextType(BusinessContextType.Web.class));
        assertTrue(businessContext.isContextType(BusinessContextType.Blocked.class));
        assertTrue(businessContext.isContextType(BusinessContextType.Web.Blocked.class));
        assertTrue(businessContext.isContextType(BusinessContextType.JakartaEE.class));
        assertTrue(businessContext.isContextType(BusinessContextType.Web.Blocked.JakartaEE.class));

        assertEquals("Web.Reactive.WebFlux", BusinessContextType.Web.Reactive.WEB_FLUX.toString());
        contextTypeSetter.setContextType(BusinessContextType.Web.Reactive.WEB_FLUX);
        assertEquals(BusinessContextType.Web.Reactive.WEB_FLUX, businessContext.getContextType());
        assertTrue(businessContext.isContextType(BusinessContextType.Web.class));
        assertTrue(businessContext.isContextType(BusinessContextType.Reactive.class));
        assertTrue(businessContext.isContextType(BusinessContextType.Web.Reactive.class));

        assertEquals("Messaging", BusinessContextType.MessagingContext.MESSAGING.toString());
        assertEquals("Messaging.Kafka", BusinessContextType.MessagingContext.KAFKA.toString());
        assertEquals("Messaging.Pulsar", BusinessContextType.MessagingContext.PULSAR.toString());
        assertEquals("Messaging.RocketMQ", BusinessContextType.MessagingContext.ROCKETMQ.toString());
        assertEquals("Messaging.JMS", BusinessContextType.MessagingContext.JMS.JMS.toString());
        assertEquals("Messaging.JMS.ActiveMQ", BusinessContextType.MessagingContext.JMS.ACTIVEMQ.toString());
        assertEquals("Messaging.AMQP", BusinessContextType.MessagingContext.AMQP.AMQP.toString());
        assertEquals("Messaging.AMQP.RabbitMQ", BusinessContextType.MessagingContext.AMQP.RABBITMQ.toString());
    }
}