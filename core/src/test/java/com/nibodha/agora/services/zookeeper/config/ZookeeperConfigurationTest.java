/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.agora.services.zookeeper.config;

import com.nibodha.agora.services.zookeeper.ZookeeperProperties;
import net.jcip.annotations.NotThreadSafe;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.test.TestingServer;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author gibugeorge on 28/05/16.
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ZookeeperConfigurationTest.TestConfig.class, ZookeeperConfiguration.class})
@NotThreadSafe
public class ZookeeperConfigurationTest {

    @Autowired(required = false)
    private CuratorFramework curatorFramework;

    @BeforeClass
    public static void setup() {
        System.setProperty("agora.zookeeper.enabled", "true");
    }


    @Test
    public void whenZookeeperIsEnabledCuratorFrameworkShouldNotBeNUll() {
        Assert.assertNotNull(curatorFramework);
    }


    public static class TestConfig{
        @Bean
        public TestingServer testingServer() throws Exception {
            return new TestingServer(2181, true);
        }

        @Bean
        public ZookeeperProperties zookeeperProperties() {
            final ZookeeperProperties zookeeperProperties = new ZookeeperProperties();
            zookeeperProperties.setConnectString("localhost:2181");
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
