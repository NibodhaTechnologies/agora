/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.agora.services.cm.config;

import com.nibodha.agora.services.cm.ConfigurationManagementPropertySourceLocator;
import com.nibodha.agora.services.cm.ConfigurationWatcher;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gibugeorge on 28/05/16.
 * @version 1.0
 */
@Configuration
public class ConfigurationWatcherConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "agora.configuration-management", value = "enabled", havingValue = "true", matchIfMissing = false)
    public ConfigurationWatcher configurationWatcher(final ConfigurationManagementPropertySourceLocator locator, final CuratorFramework curatorFramework) {
        return new ConfigurationWatcher(locator.getContext(), curatorFramework);
    }
}
