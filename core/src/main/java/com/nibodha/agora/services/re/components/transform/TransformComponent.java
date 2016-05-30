package com.nibodha.agora.services.re.components.transform;

import com.nibodha.agora.exceptions.PlatformRuntimeException;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.util.CamelContextHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class TransformComponent extends DefaultComponent {

    private static final String BEAN_PREFIX = "bean:";

    public TransformComponent(final CamelContext camelContext) {
        super(camelContext);
    }

    @Override
    protected Endpoint createEndpoint(final String uri, final String remaining, final Map<String, Object> map) throws Exception {
        if (remaining.startsWith(BEAN_PREFIX)) {
            String beanId = StringUtils.substringAfter(remaining, "bean:");
            if (beanId.startsWith("//")) {
                beanId = StringUtils.substringAfter(beanId, "//");
            }
            return CamelContextHelper.mandatoryLookup(getCamelContext(), beanId, TransformEndPoint.class);
        } else {
            throw new PlatformRuntimeException("Transformation Endpoint bean  must be provided!");
        }
    }

}
