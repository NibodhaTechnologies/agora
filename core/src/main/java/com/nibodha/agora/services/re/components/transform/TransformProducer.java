package com.nibodha.agora.services.re.components.transform;

import com.nibodha.agora.services.objectmapper.AgoraObjectMapper;
import com.nibodha.agora.services.objectmapper.support.ConverterContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.camel.model.language.LanguageExpression;
import org.apache.camel.model.language.SimpleExpression;
import org.apache.commons.lang3.Validate;

import java.util.Map;
import java.util.Objects;

public class TransformProducer extends DefaultProducer {

    private final AgoraObjectMapper objectMapper;
    private final Map<String, Object> converterParams;
    private final String mappingId;

    public TransformProducer(final TransformEndPoint endpoint, final AgoraObjectMapper objectMapper,
                             final Map<String, Object> converterParams, final String mappingId) {
        super(endpoint);
        this.objectMapper = objectMapper;
        this.converterParams = converterParams;
        this.mappingId = mappingId;
    }

    public void process(final Exchange exchange) throws Exception {
        final Object body = exchange.getIn().getBody();
        final Object result;

        if (converterParams == null) {
            result = objectMapper.mapToBean(body, mappingId);
        } else {
            result = objectMapper.mapToBean(body, mappingId, createConverterContext(exchange));
        }

        exchange.getIn().setBody(result);
    }

    private ConverterContext createConverterContext(final Exchange exchange) {
        final ConverterContext context = new ConverterContext();
        Objects.requireNonNull(converterParams).forEach((key, value) -> {
            context.put(key, evaluateExpression(exchange, value));
        });
        return context;
    }

    private Object evaluateExpression(final Exchange exchange, final Object expression) {
        if (exchange instanceof LanguageExpression) {
            return ((LanguageExpression) expression).evaluate(exchange);
        }
        Validate.isInstanceOf(String.class, expression, "Expression value should be either LanguageExpression or String type");
        return new SimpleExpression((String) expression).evaluate(exchange, Object.class);
    }

}
