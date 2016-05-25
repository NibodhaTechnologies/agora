package com.nibodha.agora.services.objectmapper.support;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class MapperObject {

    private Object object;
    private EvaluationContext context;
    private static final SpelParserConfiguration CONFIG;
    private static final ExpressionParser PARSER;

    static {
        CONFIG = new SpelParserConfiguration(true, true);
        PARSER = new SpelExpressionParser(CONFIG);
    }

    public MapperObject(final Object object) {
        this.object = object;
        this.context = new StandardEvaluationContext(object);
    }

    public void setValue(final String expression, final Object value) {
        final Expression exp = PARSER.parseExpression(expression);
        exp.setValue(context, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(final String expression) {
        final Expression exp = PARSER.parseExpression(expression);
        return (T) exp.getValue(context);
    }

    public Class<?> getFieldType(final String expression) {
        final Expression exp = PARSER.parseExpression(expression);
        return exp.getValueType(context);
    }

    public Class<?> getFieldCollectionElementType(final String expression) {
        final Expression exp = PARSER.parseExpression(expression);
        return exp.getValueTypeDescriptor(context).getElementTypeDescriptor().getType();
    }

    public Object getObject() {
        return object;
    }

}
