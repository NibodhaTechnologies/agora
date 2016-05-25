package com.nibodha.agora.services.objectmapper.support;

import com.nibodha.agora.exceptions.PlatformRuntimeException;
import com.nibodha.agora.services.objectmapper.config.Mapping;
import com.nibodha.agora.services.objectmapper.config.MappingConfiguration;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AgoraObjectMapperResourceLoader implements InitializingBean, ResourceLoaderAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgoraObjectMapperResourceLoader.class);

    private final Map<String, Mapping> mappingCache = new ConcurrentHashMap<>();
    private ResourceLoader resourceLoader;
    private static final JAXBContext JAXB_CONTEXT;

    static {
        try {
            JAXB_CONTEXT = JAXBContext.newInstance("com.nibodha.agora.services.objectmapper.config");
        } catch (final JAXBException e) {
            throw new PlatformRuntimeException("JAXBContext initialization failed!", e);
        }
    }

    public Mapping getConfiguration(final String mappingFile, final String mappingId) {
        Mapping mapping = mappingCache.get(mappingId);
        if (mapping == null) {
            loadMappingFile(mappingFile);
            mapping = mappingCache.get(mappingId);
        }
        return mapping;
    }

    public Mapping getConfiguration(final String mappingId) {
        return mappingCache.get(mappingId);
    }

    private void loadMappingFile(final String mappingFile) {
        final Resource resource = this.resourceLoader.getResource(mappingFile);
        try {

            final Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
            final MappingConfiguration mapConfigs = (MappingConfiguration) unmarshaller.unmarshal(resource.getInputStream());
            final List<Mapping> mapConfigsMapping = mapConfigs.getMapping();
            mapConfigsMapping.forEach(mapping -> mappingCache.put(mapping.getMappingId(), mapping));

        } catch (final IOException | JAXBException e) {
            LOGGER.error("Cannot find mapping file {}", mappingFile);
            throw new PlatformRuntimeException("Cannot find mapping file", e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Validate.notNull(resourceLoader);
    }

    @Override
    public void setResourceLoader(final ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

}
