/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.agora.services.cm.config;

import com.nibodha.agora.env.PlatformEnvironment;
import com.nibodha.agora.services.cm.ConfigurationManagementPropertySourceLocator;
import com.nibodha.agora.services.cm.ConfigurationWatcher;
import com.nibodha.agora.services.zookeeper.config.ZookeeperConfiguration;
import com.nibodha.agora.services.zookeeper.config.ZookeeperTestConfiguration;
import net.jcip.annotations.NotThreadSafe;
import org.apache.curator.test.TestingServer;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.inject.Inject;
import java.io.IOException;

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

    @Inject
    private TestingServer testingServer;


    @Test
    public void testConfigurationWatcher() {
        Assert.assertNotNull(configurationWatcher);

    }

    @Import({ZookeeperTestConfiguration.class, ZookeeperConfiguration.class, ConfigurationManagementConfiguration.class})
    public static class TestConfig {
        @Autowired
        private ConfigurationManagementPropertySourceLocator locator;

        @Bean
        public boolean locatePropertySource() {
            locator.locate(new PlatformEnvironment());
            return true;
        }
    }

    @After
    public void tearDown() throws IOException {
        testingServer.close();
    }

}
