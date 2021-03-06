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

import com.nibodha.agora.exceptions.PlatformRuntimeException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.EnumerablePropertySource;

import java.nio.charset.Charset;
import java.util.*;

/**
 * @author gibugeorge on 24/05/16.
 * @version 1.0
 */
public class ConfigurationManagementPropertySource extends EnumerablePropertySource<CuratorFramework> {

    private final String context;

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationManagementPropertySource.class);

    private final Map<String, String> properties = new LinkedHashMap<>();

    public ConfigurationManagementPropertySource(final String context, final CuratorFramework source) {
        super(context, source);
        this.context = context;
        loadProperties(this.context);
    }

    @Override
    public String[] getPropertyNames() {
        final String[] propertyNames = new String[properties.size()];
        return properties.keySet().toArray(propertyNames);
    }

    @Override
    public Object getProperty(String s) {
        return properties.get(s);
    }

    private void loadProperties(final String context) {
        try {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("entering findProperties for path: {}", context);
            }
            final List<String> children = getChildren(context);
            if (CollectionUtils.isEmpty(children)) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("no properties for path: {}", context);
                }
                return;
            }
            for (final String child : children) {
                String childPath = context + "/" + child;
                byte[] bytes = getPropertyBytes(childPath);
                if (ArrayUtils.isEmpty(bytes)) {
                    loadProperties(childPath);
                } else {
                    final String key = sanitizeKey(childPath);
                    this.properties.put(key, new String(bytes, Charset.forName("UTF-8")));
                }
            }
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("leaving findProperties for path: {}", context);
            }
        } catch (Exception exception) {
            throw new PlatformRuntimeException("Exception while loading properties from zookeeper", exception);
        }
    }

    private List<String> getChildren(final String context) {
        List<String> children = null;
        try {
            children = this.getSource().getChildren().forPath(context);
        } catch (KeeperException e) {
            if (e.code() != KeeperException.Code.NONODE) { // not found
                throw new PlatformRuntimeException("Exception getting children for path " + context, e);
            }
        } catch (Exception e) {
            throw new PlatformRuntimeException("Exception getting children for path " + context, e);
        }
        return children;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    private byte[] getPropertyBytes(String fullPath) {
        byte[] bytes = null;
        try {
            bytes = this.getSource().getData().forPath(fullPath);
        } catch (KeeperException e) {
            if (e.code() != KeeperException.Code.NONODE) { // not found
                throw new PlatformRuntimeException("Exception getting data from path " + fullPath, e);
            }
        } catch (Exception e) {
            throw new PlatformRuntimeException("Exception getting data from path " + fullPath, e);
        }
        return bytes;
    }

    private String sanitizeKey(String path) {
        return path.replace(this.context + "/", "").replace('/', '.');
    }
}
