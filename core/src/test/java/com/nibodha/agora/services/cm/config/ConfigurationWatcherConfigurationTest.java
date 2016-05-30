/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.agora.services.cm.config;

import com.nibodha.agora.env.PlatformEnvironment;
import com.nibodha.agora.services.cm.ConfigurationManagementPropertySourceLocator;
import com.nibodha.agora.services.cm.ConfigurationWatcher;
import com.nibodha.agora.services.zookeeper.ZookeeperProperties;
import com.nibodha.agora.services.zookeeper.config.ZookeeperConfiguration;
import net.jcip.annotations.NotThreadSafe;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author gibugeorge on 28/05/16.
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ConfigurationWatcherConfigurationTest.TestConfig.class, ConfigurationWatcherConfiguration.class})
@NotThreadSafe
public class ConfigurationWatcherConfigurationTest {


    @BeforeClass
    public static void setup() {
        System.setProperty("agora.zookeeper.enabled", "true");
        System.setProperty("agora.configuration-management.enabled", "true");
    }

    @Autowired(required = false)
    private ConfigurationWatcher configurationWatcher;



    @Test
    public void testConfigurationWatcher() {
        Assert.assertNotNull(configurationWatcher);

    }

    @Import({ZookeeperConfiguration.class, ConfigurationManagementConfiguration.class})
    public static class TestConfig {
        @Autowired
        private ConfigurationManagementPropertySourceLocator locator;
        @Inject
        private CuratorFramework curatorFramework;


        @Bean
        public boolean locatePropertySource() throws Exception {
            curatorFramework.create().creatingParentsIfNeeded().forPath("/config/agora/local/test", "test".getBytes());
            locator.locate(new PlatformEnvironment());
            return true;
        }

        @Bean
        public TestingServer testingServer() throws Exception {
            return new TestingServer(2183, true);
        }

        @Bean
        public ZookeeperProperties zookeeperProperties() {
            final ZookeeperProperties zookeeperProperties = new ZookeeperProperties();
            zookeeperProperties.setConnectString("localhost:2183");
            zookeeperProperties.setEnabled(true);
            zookeeperProperties.setMaxRetries(2);
            zookeeperProperties.setMaxSleepMs(500);
            zookeeperProperties.setBaseSleepTimeMs(50);
            zookeeperProperties.setBlockUntilConnectedUnit(TimeUnit.SECONDS);
            zookeeperProperties.setBlockUntilConnectedWait(5);
            return zookeeperProperties;
        }
    }



}
