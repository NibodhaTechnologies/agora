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

package com.nibodha.agora.services.cm;

import com.nibodha.agora.env.PlatformEnvironment;
import com.nibodha.agora.exceptions.PlatformRuntimeException;
import org.apache.commons.lang3.StringUtils;
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
        final PlatformEnvironment platformEnvironment = (PlatformEnvironment) environment;
        String applicationName = platformEnvironment.getProperty("agora.application.name");
        if (StringUtils.isEmpty(applicationName)) {
            applicationName = "agora";
        }
        String profile = platformEnvironment.getProperty("agora.application.profile");
        if (StringUtils.isEmpty(profile)) {
            profile = "local";
        }
        final String root = configurationManagementProperties.getRoot();
        context = "/" + root + "/" + applicationName + "/" + profile;
        return new ConfigurationManagementPropertySource(context, this.curatorFramework);
    }

    public String getContext() {
        return context;
    }
}
