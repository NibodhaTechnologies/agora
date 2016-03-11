/*
 * Copyright 2016 Nibodha Technologies Pvt. Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nibodha.ip.services.camel.processor;

import com.nibodha.ip.domain.ErrorInfo;
import com.nibodha.ip.exceptions.ExceptionType;
import com.nibodha.ip.exceptions.PlatformRuntimeException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultProducerTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gibugeorge on 08/03/16.
 * @version 1.0
 */
public class DefaultErrorHandler implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultErrorHandler.class);

    @Override
    public void process(final Exchange exchange) throws Exception {
        final Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        LOGGER.error("Exception in route {}", exchange.getFromRouteId());
        LOGGER.error("Exception is ", exception);
        final String deadLetterUri = exchange.getProperty(RoutingEngineErrorHandler.DEAD_LETTER_URI, String.class);
        if (StringUtils.isNotEmpty(deadLetterUri)) {
            final ProducerTemplate producerTemplate = new DefaultProducerTemplate(exchange.getContext());
            producerTemplate.send(deadLetterUri, exchange);
            return;
        }
        ExceptionType type = PlatformRuntimeException.Type.GENERIC;
        if (exception instanceof PlatformRuntimeException) {
            type = ((PlatformRuntimeException) exception).getType();
        }
        final ErrorInfo errorInfo = new ErrorInfo(type, exception.getMessage());
        exchange.getIn().setBody(errorInfo);
    }
}
