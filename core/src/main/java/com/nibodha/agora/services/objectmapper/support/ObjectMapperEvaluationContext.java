package com.nibodha.agora.services.objectmapper.support;

import org.springframework.expression.spel.support.StandardEvaluationContext;

class ObjectMapperEvaluationContext extends StandardEvaluationContext {

    ObjectMapperEvaluationContext(final Object object) {
        super(object);
    }

    @Override
    public Object lookupVariable(final String name) {
        Object variable = super.lookupVariable(name);
        if (variable == null) {
            final ConverterContext context = ConverterContextHolder.getContext();
            variable = context != null ? context.get(name) : null;
        }
        return variable;
    }

}
