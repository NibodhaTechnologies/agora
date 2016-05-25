/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.agora.services.cm;

import com.nibodha.agora.env.PlatformEnvironment;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

/**
 * @author gibugeorge on 24/05/16.
 * @version 1.0
 */
public class ConfigurationManagementPropertySourceLocator implements PropertySourceLocator {

    private final CuratorFramework curatorFramework;
    private final ConfigurationManagementProperties configurationManagementProperties;
    private String context;

    public ConfigurationManagementPropertySourceLocator(final CuratorFramework curatorFramework, final ConfigurationManagementProperties configurationManagementProperties) {
        this.curatorFramework = curatorFramework;
        this.configurationManagementProperties = configurationManagementProperties;
    }

    @Override
    public PropertySource<?> locate(final Environment environment) {
        if (environment instanceof PlatformEnvironment) {
            final PlatformEnvironment platformEnvironment = (PlatformEnvironment) environment;
            final String applicationName = platformEnvironment.getProperty("agora.application.name");
            final String profile = platformEnvironment.getProperty("agora.application.profile");
            final String root = configurationManagementProperties.getRoot();
            context = "/" + root + "/" + applicationName + "/" + profile;
            System.out.println("************* " + root);
            return new ConfigurationManagementPropertySource(context, this.curatorFramework);
        }
        return null;
    }
}
