package com.nibodha.agora.services.objectmapper.transformer;

import com.nibodha.agora.services.objectmapper.support.ConverterContext;
import com.nibodha.agora.services.objectmapper.support.ConverterContextHolder;

public abstract class AbstractTypeConverter implements TypeConverter {

    protected <T> T getParameter(final String parameterKey) {
        final ConverterContext context = ConverterContextHolder.getContext();
        return context != null ? context.get(parameterKey) : null;
    }

}
