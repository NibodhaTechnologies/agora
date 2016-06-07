package com.nibodha.agora.services.objectmapper.transformer.pojo;

import com.nibodha.agora.exceptions.PlatformRuntimeException;
import com.nibodha.agora.services.objectmapper.config.Field;
import com.nibodha.agora.services.objectmapper.config.Mapping;
import com.nibodha.agora.services.objectmapper.registry.TypeConverterRegistry;
import com.nibodha.agora.services.objectmapper.support.AgoraObjectMapperResourceLoader;
import com.nibodha.agora.services.objectmapper.support.MapperObject;
import com.nibodha.agora.services.objectmapper.transformer.AbstractTypeConverter;
import com.nibodha.agora.services.objectmapper.transformer.Transformer;
import com.nibodha.agora.services.objectmapper.util.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class ObjectToObjectTransformer implements Transformer<Object, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectToObjectTransformer.class);

    @Inject
    private AgoraObjectMapperResourceLoader resourceLoader;

    @Inject
    private TypeConverterRegistry typeConverterRegistry;

    @Override
    public Object transform(final Object source, final Mapping mappingConfig) throws Exception {
        Validate.notNull(mappingConfig, "Mapping configuration must be specified");
        final Object destination = createObject(mappingConfig.getDestination());
        return transform(source, destination, mappingConfig);
    }

    private Object createObject(final String clazz) {
        return ObjectUtils.newInstance(clazz);
    }

    private Object transform(final Object source, final Object destination, final Mapping mappingConfig) {
        final MapperObject sourceObj = new MapperObject(source);
        final MapperObject destObj = new MapperObject(destination);

        for (final Field field : mappingConfig.getFields().getField()) {
            final Class<?> fieldType = destObj.getFieldType(field.getDestination());
            final Object resultValue;
            if (field.getCustomConverterRef() != null) {
                resultValue = getCustomConvertedValue(sourceObj, destObj, field);
            } else if (BeanUtils.isSimpleProperty(fieldType)) {
                resultValue = transformSimpleProperty(sourceObj, field);
            } else if (isCollection(fieldType)) {
                resultValue = transformCollection(sourceObj, destObj, field);
            } else {
                resultValue = transformObject(sourceObj, destObj, field);
            }
            destObj.setValue(field.getDestination(), resultValue);
        }

        return destObj.getObject();
    }

    private Object getCustomConvertedValue(final MapperObject sourceObj, final MapperObject destObj, final Field field) {
        final AbstractTypeConverter converter = typeConverterRegistry.getConverter(field.getCustomConverterRef());
        return converter.convert(sourceObj.getValue(field.getSource()), destObj.getObject());
    }

    private Object transformSimpleProperty(final MapperObject sourceObj, final Field field) {
        final Object resultValue;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Transforming from {} to {}", field.getSource(), field.getDestination());
        }
        if (field.getConstantValue() != null) {
            resultValue = field.getConstantValue();
        } else {
            resultValue = sourceObj.getValue(field.getSource());
        }
        return resultValue;
    }

    private Object transformObject(final MapperObject sourceObj, final MapperObject destinationObj, final Field field) {
        final Object sourceValue = sourceObj.getValue(field.getSource());
        Object destinationValue = destinationObj.getValue(field.getDestination());
        if (destinationValue == null) {
            final Class<?> fieldType = destinationObj.getFieldType(field.getDestination());
            destinationValue = ObjectUtils.newInstance(fieldType);
        }

        final Mapping mappingConfig = getMandatoryMappingConfig(field);

        return transform(sourceValue, destinationValue, mappingConfig);
    }

    private Object transformCollection(final MapperObject sourceObj, final MapperObject destinationObj, final Field field) {
        final Object sourceValues = sourceObj.getValue(field.getSource());
        final Class<?> destCollectionElementType = destinationObj.getFieldCollectionElementType(field.getDestination());

        if (BeanUtils.isSimpleProperty(destCollectionElementType)) {
            return sourceValues;
        }

        Collection<Object> destinationValues = destinationObj.getValue(field.getDestination());

        if (destinationValues == null) {
            final Class<?> fieldType = destinationObj.getFieldType(field.getDestination());
            destinationValues = fieldType.isInterface() ? new ArrayList<>() : ObjectUtils.newInstance(fieldType);
        }

        final Mapping mappingConfig = getMandatoryMappingConfig(field);

        //noinspection unchecked
        for (final Object srcVal : (Collection<Object>) sourceValues) {//TODO: array to collection and vice versa support
            final Object resultObject = ObjectUtils.newInstance(destCollectionElementType);
            destinationValues.add(transform(srcVal, resultObject, mappingConfig));
        }

        return destinationValues;
    }

    private Mapping getMandatoryMappingConfig(final Field field) {
        final Mapping mappingConfig = resourceLoader.getConfiguration(field.getMappingIdRef());

        if (mappingConfig == null) {
            throw new PlatformRuntimeException(String.format("Cannot find configuration for object mapping from %s to %s", field.getSource(), field.getDestination()));
        }
        return mappingConfig;
    }

    private boolean isCollection(final Class<?> fieldType) {
        return fieldType.isArray() || Collection.class.isAssignableFrom(fieldType);
    }

}
