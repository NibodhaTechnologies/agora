package com.nibodha.agora.services.objectmapper.util;

import com.nibodha.agora.exceptions.PlatformRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectUtils.class);

    private ObjectUtils() {
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> loadClass(final String className) {
        try {
            return (Class<T>) Class.forName(className);
        } catch (final ClassNotFoundException e) {
            LOGGER.error("Cannot find destination class {}", className);
            throw new PlatformRuntimeException("Cannot find class" + className, e);
        }
    }

    public static <T> T newInstance(final String className) {
        final Class<T> type = ObjectUtils.loadClass(className);
        return newInstance(type);
    }

    public static <T> T newInstance(final Class<?> type) {
        try {
            return (T) type.newInstance();
        } catch (InstantiationException e) {
            throw new PlatformRuntimeException("Cannot Instantiate object for " + type, e);
        } catch (IllegalAccessException e) {
            throw new PlatformRuntimeException("Failed to create new instance for " + type, e);
        }
    }

}
