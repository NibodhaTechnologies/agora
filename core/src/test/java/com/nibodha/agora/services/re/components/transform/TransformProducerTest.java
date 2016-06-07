package com.nibodha.agora.services.re.components.transform;

import com.nibodha.agora.services.objectmapper.AgoraObjectMapper;
import com.nibodha.agora.services.objectmapper.support.ConverterContext;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransformProducerTest extends CamelTestSupport {

    protected Exchange exchange;
    @Mock
    private AgoraObjectMapper objectMapper;

    @InjectMocks
    private TransformEndPoint transformEndPoint;

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;


    @Override
    protected void doPostSetup() throws Exception {
        exchange = new DefaultExchange(this.context());
    }

    @Test
    public void testCreateProducer() throws Exception {
        Map<String, String> sample = new HashMap<>();
        sample.put("key", "value");
        exchange.getIn().setBody(sample);
        when(objectMapper.mapToBean(sample, "xls-to-bean")).thenReturn("sample");
        TransformProducer producer = new TransformProducer(transformEndPoint, objectMapper, null, "xls-to-bean");
        producer.process(exchange);
        final String response = exchange.getIn().getBody(String.class);
        Mockito.verify(objectMapper).mapToBean(sample, "xls-to-bean");
        assertNotNull(response);
        assertEquals("sample", response);
    }

    @Test
    public void testCreateProducerWithConverters() throws Exception {
        Map<String, String> sample = new HashMap<>();
        sample.put("key", "value");
        exchange.getIn().setBody(sample);
        final Map<String, Object> converterParams = new HashMap<>();
        when(objectMapper.mapToBean(anyObject(), anyString(), any(ConverterContext.class))).thenReturn("sample");
        TransformProducer producer = new TransformProducer(transformEndPoint, objectMapper, converterParams, "xls-to-bean");
        producer.process(exchange);
        final String response = exchange.getIn().getBody(String.class);
        Mockito.verify(objectMapper).mapToBean(anyObject(), anyString(), any(ConverterContext.class));
        assertNotNull(response);
        assertEquals("sample", response);
    }
}
