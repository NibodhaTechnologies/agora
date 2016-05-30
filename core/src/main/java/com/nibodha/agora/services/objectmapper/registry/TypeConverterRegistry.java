package com.nibodha.agora.services.objectmapper.registry;

import com.nibodha.agora.exceptions.PlatformRuntimeException;
import com.nibodha.agora.services.objectmapper.transformer.AbstractTypeConverter;
import net.logstash.logback.encoder.org.apache.commons.lang.Validate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class TypeConverterRegistry implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @SuppressWarnings("unchecked")
    public AbstractTypeConverter getConverter(final String beanRef) {
        Validate.notEmpty(beanRef, "beanRef must be provided!");
        final AbstractTypeConverter converter = applicationContext.getBean(beanRef, AbstractTypeConverter.class);
        if (converter == null) {
            throw new PlatformRuntimeException("Cannot find converter for " + beanRef);
        }
        return converter;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
