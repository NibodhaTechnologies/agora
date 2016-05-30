package com.nibodha.agora.services.re.components.transform;

import com.nibodha.agora.services.objectmapper.AgoraObjectMapper;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.commons.lang3.Validate;

import java.util.Map;


@UriEndpoint(scheme = "transform", title = "Transform", syntax = "transform:beanId", label = "transformation")
public class TransformEndPoint extends DefaultEndpoint {

    private AgoraObjectMapper objectMapper;
    private Map<String, Object> converterParams;

    @UriParam
    private String mappingId;


    @Override
    public Producer createProducer() throws Exception {
        Validate.notNull(objectMapper, "Object mapper must be specified!");
        return new TransformProducer(this, objectMapper, converterParams, mappingId);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        throw new UnsupportedOperationException("Transformation consumer not supported!");
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public void setObjectMapper(final AgoraObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setConverterParams(final Map<String, Object> converterParams) {
        this.converterParams = converterParams;
    }

    public void setMappingId(String mappingId) {
        this.mappingId = mappingId;
    }
}
