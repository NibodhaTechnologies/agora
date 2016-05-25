package com.nibodha.agora.services.objectmapper.support;

public abstract class ConverterContextHolder {

    private static final ThreadLocal<ConverterContext> parameter = new ThreadLocal<>();

    public static void setContext(final ConverterContext context) {
        parameter.set(context);
    }

    public static ConverterContext getContext() {
        return parameter.get();
    }

}
