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

package com.nibodha.agora.services.config;

import com.nibodha.agora.services.cm.ConfigurationManagementPropertySource;
import com.nibodha.agora.services.cm.ConfigurationManagementPropertySourceLocator;
import com.nibodha.agora.services.re.spring.spi.PlatformPropertyPlaceholderConfigurer;
import com.nibodha.agora.services.zookeeper.config.ZookeeperConfiguration;
import org.infinispan.factories.annotations.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

/**
 * @author gibugeorge on 20/01/16.
 * @version 1.0
 */
@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@AutoConfigureAfter(ZookeeperConfiguration.class)
public class PlatformPlaceHolderConfiguration implements ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    @Autowired(required = false)
    private ConfigurationManagementPropertySourceLocator configurationManagementPropertySourceLocator;

    @Bean
    public PlatformPropertyPlaceholderConfigurer platformPropertyPlaceholderConfigurer(final Environment environment) throws IOException {

        final Resource configLocation = resourceLoader.getResource(System.getProperty("config.location"));
        PlatformPropertyPlaceholderConfigurer platformPropertyPlaceholderConfigurer = null;
        if (configurationManagementPropertySourceLocator != null) {
            final PropertySource<?> propertySource = configurationManagementPropertySourceLocator.locate(environment);
            platformPropertyPlaceholderConfigurer = new PlatformPropertyPlaceholderConfigurer(propertySource);
        } else {
            platformPropertyPlaceholderConfigurer = new PlatformPropertyPlaceholderConfigurer();
        }
        platformPropertyPlaceholderConfigurer.setConfigFileLocation(configLocation);
        platformPropertyPlaceholderConfigurer.setFileEncoding("UTF-8");
        platformPropertyPlaceholderConfigurer.setIgnoreResourceNotFound(true);
        platformPropertyPlaceholderConfigurer.setFileNames("classpath:application.properties,*.yaml,*.properties");
        return platformPropertyPlaceholderConfigurer;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
