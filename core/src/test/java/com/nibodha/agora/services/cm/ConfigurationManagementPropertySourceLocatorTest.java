/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.agora.services.cm;

import com.nibodha.agora.env.PlatformEnvironment;
import com.nibodha.agora.services.cm.config.ConfigurationManagementConfiguration;
import com.nibodha.agora.services.zookeeper.ZookeeperProperties;
import com.nibodha.agora.services.zookeeper.config.ZookeeperConfiguration;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.test.TestingServer;
import org.apache.zookeeper.KeeperException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author gibugeorge on 28/05/16.
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ConfigurationManagementPropertySourceLocatorTest.TestConfig.class, ZookeeperConfiguration.class, ConfigurationManagementConfiguration.class})
public class ConfigurationManagementPropertySourceLocatorTest {


    @Inject
    private ConfigurationManagementPropertySourceLocator locator;

    @Inject
    private ConfigurationManagementProperties properties;

    private CuratorFramework curator;


    @BeforeClass
    public static void preSetup() {
        System.setProperty("agora.zookeeper.enabled", "true");
        System.setProperty("agora.configuration-management.enabled", "true");
    }

    private void setupZookeeperConfiguration(boolean withData) throws Exception {
        curator = CuratorFrameworkFactory.builder()
                .retryPolicy(new RetryOneTime(500))
                .connectString("localhost:2184")
                .build();
        curator.start();
        List<String> children = curator.getChildren().forPath("/");
        for (String child : children) {
            if (child.startsWith(properties.getRoot())) {
                delete("/" + child);
            }
        }
        if (withData) {
            create();
        }
        curator.close();

    }

    private void create() throws Exception {
        curator.create().creatingParentsIfNeeded().forPath("/config/agora/local/test", "test".getBytes());
        curator.create().creatingParentsIfNeeded().forPath("/config/agora/local/sampleNode/test", "inSampleNode".getBytes());
    }

    @Test
    public void propertySourceLocatorReturnsPropertySource() throws Exception {
        setupZookeeperConfiguration(true);
        final PropertySource propertySource = locator.locate(new PlatformEnvironment());
        Assert.assertNotNull(propertySource);
        Assert.assertTrue(propertySource instanceof ConfigurationManagementPropertySource);
        Assert.assertNotNull(locator.getContext());
        Assert.assertEquals("test", propertySource.getProperty("test"));
        Assert.assertEquals(2, ((ConfigurationManagementPropertySource) propertySource).getPropertyNames().length);
    }

    @Test
    public void propertySourceLocatorSetupWithOutZookeeperData() throws Exception {
        setupZookeeperConfiguration(false);
        final PropertySource propertySource = locator.locate(new PlatformEnvironment());
        Assert.assertNotNull(propertySource);
    }


    public void delete(String path) throws Exception {
        try {
            this.curator.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (KeeperException e) {
            if (e.code() != KeeperException.Code.NONODE) {
                throw e;
            }
        }
    }

    public static class TestConfig {
        @Bean
        public TestingServer testingServer() throws Exception {
            return new TestingServer(2184, true);
        }

        @Bean
        public ZookeeperProperties zookeeperProperties() {
            final ZookeeperProperties zookeeperProperties = new ZookeeperProperties();
            zookeeperProperties.setConnectString("localhost:2184");
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
