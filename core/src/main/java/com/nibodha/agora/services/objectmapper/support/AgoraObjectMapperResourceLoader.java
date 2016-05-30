package com.nibodha.agora.services.objectmapper.support;

import com.nibodha.agora.exceptions.PlatformRuntimeException;
import com.nibodha.agora.services.objectmapper.config.Mapping;
import com.nibodha.agora.services.objectmapper.config.MappingConfiguration;
import org.apache.commons.lang3.Validate;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
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

    private Map<String, Mapping> mappingCache;
    private ResourceLoader resourceLoader;
    private static final JAXBContext JAXB_CONTEXT;
    @Autowired(required = false)
    private EmbeddedCacheManager cacheManager;

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
            throw new PlatformRuntimeException("Cannot find mapping file: " + mappingFile, e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initializeCache();
        Validate.notNull(resourceLoader);
    }

    private void initializeCache() {
        if (cacheManager != null) {
            mappingCache = cacheManager.getCache();
        } else {
            mappingCache = new ConcurrentHashMap<>();
        }
    }

    @Override
    public void setResourceLoader(final ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

}
