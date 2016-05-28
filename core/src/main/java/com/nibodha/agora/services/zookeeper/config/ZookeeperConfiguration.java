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
package com.nibodha.agora.services.zookeeper.config;

import com.nibodha.agora.services.zookeeper.ZookeeperProperties;
import org.apache.curator.ensemble.exhibitor.Exhibitors;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

/**
 * @author gibugeorge on 24/05/16.
 * @version 1.0
 */
@Configuration
@ConditionalOnProperty(value = "agora.zookeeper.enabled", matchIfMissing = false)
@EnableConfigurationProperties(ZookeeperProperties.class)
public class ZookeeperConfiguration {

    @Inject
    private ZookeeperProperties zookeeperProperties;

    @Bean
    public CuratorFramework curatorFramework() {
        final CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
        CuratorFramework curatorFramework = builder.connectString(zookeeperProperties.getConnectString()).
                retryPolicy(new ExponentialBackoffRetry(zookeeperProperties.getBaseSleepTimeMs(),
                        zookeeperProperties.getMaxRetries(), zookeeperProperties.getMaxSleepMs())).build();
        curatorFramework.start();
        return curatorFramework;
    }
}
