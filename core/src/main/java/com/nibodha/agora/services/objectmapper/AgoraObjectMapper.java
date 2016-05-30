package com.nibodha.agora.services.objectmapper;

import com.nibodha.agora.exceptions.PlatformRuntimeException;
import com.nibodha.agora.services.objectmapper.config.Mapping;
import com.nibodha.agora.services.objectmapper.registry.TransformerRegistry;
import com.nibodha.agora.services.objectmapper.support.AgoraObjectMapperResourceLoader;
import com.nibodha.agora.services.objectmapper.support.ConverterContext;
import com.nibodha.agora.services.objectmapper.support.ConverterContextHolder;
import com.nibodha.agora.services.objectmapper.transformer.Transformer;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.InitializingBean;

import javax.inject.Inject;

public class AgoraObjectMapper implements InitializingBean {

    @Inject
    private AgoraObjectMapperResourceLoader resourceLoader;

    @Inject
    private TransformerRegistry transformerRegistry;

    private String mappingFile;

    public <T> T mapToBean(final Object source, final String mappingId) throws Exception {
        return transform(source, mappingId, null);
    }

    public <T> T mapToBean(final Object source, final String mappingId, final ConverterContext context) throws Exception {
        return transform(source, mappingId, context);
    }

    @SuppressWarnings("unchecked")
    private <T> T transform(final Object source, final String mappingId, final ConverterContext context) throws Exception {
        if (source == null) {
            return null;
        }

        Validate.notEmpty(mappingId, "Mapping Id must be provided!");

        final Mapping mappingConfig = resourceLoader.getConfiguration(mappingFile, mappingId);

        if (mappingConfig == null) {
            throw new PlatformRuntimeException("A mapping configuration should be provided!");
        }

        final Transformer transformer = transformerRegistry.getTransformer(mappingConfig.getSource());

        if (transformer == null) {
            throw new PlatformRuntimeException("Cannot find desired transformer for" + mappingConfig.getSource());
        }

        ConverterContextHolder.setContext(context);

        return (T) transformer.transform(source, mappingConfig);
    }

    public void setMappingFile(final String mappingFile) {
        this.mappingFile = mappingFile;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Validate.notEmpty(mappingFile, "Mapping file must be provided!");
    }
}
