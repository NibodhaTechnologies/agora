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

package com.nibodha.agora.services.re.spring.spi;

import com.nibodha.agora.services.cm.ConfigurationManagementPropertySource;
import com.nibodha.agora.services.cm.ConfigurationManagementPropertySourceLocator;
import com.nibodha.agora.yaml.YamlPropertiesLoader;
import org.apache.camel.spring.spi.BridgePropertyPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author gibugeorge on 17/12/15.
 * @version 1.0
 */
public class PlatformPropertyPlaceholderConfigurer extends BridgePropertyPlaceholderConfigurer {


    private Resource configFileLocation;
    private final ResourcePatternResolver resourcePatternResolver;
    private String fileEncoding;
    private Resource[] locations;
    private Boolean ignoreResourceNotFound;
    private final YamlPropertiesLoader yamlPropertiesLoader;
    private Properties properties;
    private String fileNames;
    private PropertySource<?> propertySource;

    public PlatformPropertyPlaceholderConfigurer() {
        resourcePatternResolver = new PathMatchingResourcePatternResolver();
        yamlPropertiesLoader = new YamlPropertiesLoader();
        this.setFileEncoding("UTF-8");
    }

    public PlatformPropertyPlaceholderConfigurer(final PropertySource<?> propertySource) throws IOException {
        this();
        this.propertySource = propertySource;
    }

    public void setConfigFileLocation(final Resource configFileLocation) throws IOException {
        this.configFileLocation = configFileLocation;
    }

    public void setFileNames(final String fileNames) throws IOException {
        this.fileNames = fileNames;
        final String[] fileNamesArray = fileNames.split(",");
        final List<Resource> locationsList = new ArrayList<>();
        for (final String fileName : fileNamesArray) {

            final String pattern;
            if (fileName.startsWith("classpath:")) {
                pattern = fileName;
            } else {
                pattern = configFileLocation.getURL().toString() + File.separator + fileName;

            }
            final Resource[] resources = resourcePatternResolver.getResources(pattern);
            locationsList.addAll(Arrays.asList(resources));
        }
        final Resource[] resources = new Resource[locationsList.size()];
        this.locations = locationsList.toArray(resources);
        super.setLocations(this.locations);
    }

    @Override
    public void loadProperties(final Properties props) throws IOException {
        if (propertySource != null) {
            final Map<String, String> configurationProps = ((ConfigurationManagementPropertySource) propertySource).getProperties();
            props.putAll(configurationProps);
        }
        if (this.locations != null) {
            for (final Resource location : this.locations) {
                if (logger.isInfoEnabled()) {
                    logger.info("Loading properties file from " + location);
                }
                try {
                    doLoadProperties(props, location);
                    properties = props;
                } catch (IOException ex) {
                    if (this.ignoreResourceNotFound) {
                        if (logger.isWarnEnabled()) {
                            logger.warn("Could not load properties from " + location + ": " + ex.getMessage());
                        }
                    } else {
                        throw ex;
                    }
                }
            }
        }
    }

    private void doLoadProperties(final Properties props, final Resource location) throws IOException {
        if (location.getFilename().endsWith(".yaml")) {
            yamlPropertiesLoader.setResources(location);
            props.putAll(yamlPropertiesLoader.createProperties());
        } else {
            PropertiesLoaderUtils.fillProperties(
                    props, new EncodedResource(location, this.fileEncoding));
        }
    }

    @Override
    public void setFileEncoding(final String encoding) {
        super.setFileEncoding(encoding);
        this.fileEncoding = encoding;
    }

    @Override
    public void setIgnoreResourceNotFound(final boolean ignoreResourceNotFound) {
        super.setIgnoreResourceNotFound(ignoreResourceNotFound);
        this.ignoreResourceNotFound = ignoreResourceNotFound;
    }


    public void refresh() throws IOException {
        this.setFileNames(this.fileNames);
        this.loadProperties(properties);
    }

    public Properties getProperties() {
        return properties;
    }
}
