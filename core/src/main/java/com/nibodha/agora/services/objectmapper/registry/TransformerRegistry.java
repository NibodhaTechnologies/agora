package com.nibodha.agora.services.objectmapper.registry;

import com.nibodha.agora.exceptions.PlatformRuntimeException;
import com.nibodha.agora.services.objectmapper.transformer.Transformer;
import com.nibodha.agora.services.objectmapper.transformer.pojo.ObjectToObjectTransformer;
import com.nibodha.agora.services.objectmapper.transformer.xls.XlsToPojoTransformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TransformerRegistry implements ApplicationContextAware, InitializingBean {

    private static final String SEPARATOR = "://";
    private ApplicationContext applicationContext;

    private Map<String, Class<? extends Transformer>> transformerRegistry;

    public Transformer getTransformer(final String source) {
        Validate.notEmpty(source, "Source must be provided!");
        final String schemeName = getScheme(source);
        final Class<? extends Transformer> transformerType = transformerRegistry.get(schemeName);
        if (transformerType == null) {
            throw new PlatformRuntimeException("Cannot find desired transformer for" + source + "with scheme" + schemeName);
        }
        return applicationContext.getBean(transformerType);
    }

    public void registerTransformer(final String typeScheme, final Class<? extends Transformer> transformer) {
        transformerRegistry.put(typeScheme, transformer);
    }

    public void registerTransformers(final Map<String, Class<? extends Transformer>> transformers) {
        transformerRegistry.putAll(transformers);
    }

    private String getScheme(final String source) {
        return StringUtils.upperCase(StringUtils.substringBefore(source, SEPARATOR));
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        transformerRegistry = new ConcurrentHashMap<>();
        registerDefaultConverters();
    }

    private void registerDefaultConverters() {
        transformerRegistry.put("XLS", XlsToPojoTransformer.class);
        transformerRegistry.put("POJO", ObjectToObjectTransformer.class);
    }

}
