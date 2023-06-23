package com.wingsweaver.kuja.common.boot.context;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BusinessContextTypeTest {
    @Test
    void test() {
        BusinessContext businessContext = BusinessContext.create();
        BusinessContextAccessor accessor = new BusinessContextAccessor(businessContext);

        assertEquals("Other", BusinessContextType.OTHER.toString());
        accessor.setContextType(BusinessContextType.OTHER);
        assertEquals(BusinessContextType.OTHER, accessor.getContextType());

        assertEquals("Web", BusinessContextType.Web.WEB.toString());
        accessor.setContextType(BusinessContextType.Web.WEB);
        assertEquals(BusinessContextType.Web.WEB, accessor.getContextType());
        assertTrue(accessor.isContextType(BusinessContextType.Web.class));

        assertEquals("Web.Blocked.Servlet", BusinessContextType.Web.Blocked.SERVLET.toString());
        accessor.setContextType(BusinessContextType.Web.Blocked.SERVLET);
        assertEquals(BusinessContextType.Web.Blocked.SERVLET, accessor.getContextType());
        assertTrue(accessor.isContextType(BusinessContextType.Web.class));
        assertTrue(accessor.isContextType(BusinessContextType.Blocked.class));
        assertTrue(accessor.isContextType(BusinessContextType.Web.Blocked.class));

        assertEquals("Web.Blocked.Servlet.J2EE", BusinessContextType.Web.Blocked.J2EE.SERVLET.toString());
        accessor.setContextType(BusinessContextType.Web.Blocked.J2EE.SERVLET);
        assertEquals(BusinessContextType.Web.Blocked.J2EE.SERVLET, accessor.getContextType());
        assertTrue(accessor.isContextType(BusinessContextType.Web.class));
        assertTrue(accessor.isContextType(BusinessContextType.Blocked.class));
        assertTrue(accessor.isContextType(BusinessContextType.Web.Blocked.class));
        assertTrue(accessor.isContextType(BusinessContextType.J2EE.class));
        assertTrue(accessor.isContextType(BusinessContextType.Web.Blocked.J2EE.class));

        assertEquals("Web.Blocked.Servlet.JakartaEE", BusinessContextType.Web.Blocked.JakartaEE.SERVLET.toString());
        accessor.setContextType(BusinessContextType.Web.Blocked.JakartaEE.SERVLET);
        assertEquals(BusinessContextType.Web.Blocked.JakartaEE.SERVLET, accessor.getContextType());
        assertTrue(accessor.isContextType(BusinessContextType.Web.class));
        assertTrue(accessor.isContextType(BusinessContextType.Blocked.class));
        assertTrue(accessor.isContextType(BusinessContextType.Web.Blocked.class));
        assertTrue(accessor.isContextType(BusinessContextType.JakartaEE.class));
        assertTrue(accessor.isContextType(BusinessContextType.Web.Blocked.JakartaEE.class));

        assertEquals("Web.Reactive.WebFlux", BusinessContextType.Web.Reactive.WEB_FLUX.toString());
        accessor.setContextType(BusinessContextType.Web.Reactive.WEB_FLUX);
        assertEquals(BusinessContextType.Web.Reactive.WEB_FLUX, accessor.getContextType());
        assertTrue(accessor.isContextType(BusinessContextType.Web.class));
        assertTrue(accessor.isContextType(BusinessContextType.Reactive.class));
        assertTrue(accessor.isContextType(BusinessContextType.Web.Reactive.class));

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