package com.nibodha.agora.services.objectmapper.support;

import java.util.HashMap;
import java.util.Map;

public class ConverterContext {

    private final Map<String, Object> parameters;

    public ConverterContext() {
        this.parameters = new HashMap<>(0);
    }

    public ConverterContext put(final String key, final Object value) {
        parameters.put(key, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(final String key) {
        return (T) parameters.get(key);
    }

}
