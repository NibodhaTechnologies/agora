/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.agora.services.cm.config;

import com.nibodha.agora.services.cm.ConfigurationManagementProperties;
import com.nibodha.agora.services.cm.ConfigurationManagementPropertySourceLocator;
import com.nibodha.agora.services.cm.ConfigurationWatcher;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gibugeorge on 24/05/16.
 * @version 1.0
 */
@Configuration
@ConditionalOnProperty(prefix = "agora.zookeeper", value = "enabled", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties(ConfigurationManagementProperties.class)
public class ConfigurationManagementConfiguration {


    @Bean
    @ConditionalOnProperty(prefix = "agora.configuration-management", value = "enabled", havingValue = "true", matchIfMissing = false)
    public ConfigurationManagementPropertySourceLocator configurationManagementPropertySourceLocator(final CuratorFramework curatorFramework, final ConfigurationManagementProperties configurationManagementProperties) {
        return new ConfigurationManagementPropertySourceLocator(curatorFramework, configurationManagementProperties);
    }


}
