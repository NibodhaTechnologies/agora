package com.nibodha.agora.services.re.components.transform;

import org.apache.camel.Endpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TransformComponentTest extends CamelSpringTestSupport {

    @Test
    public void testCreateEndPoint() throws Exception {
        final String endPointUri = "transform://bean:xlsTransformer";
        TransformComponent component = new TransformComponent(context);
        Endpoint endPoint = component.createEndpoint(endPointUri);
        assertNotNull(endPoint);
        assertTrue(endPoint instanceof TransformEndPoint);
    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/transform-component-test-context.xml");
    }

}