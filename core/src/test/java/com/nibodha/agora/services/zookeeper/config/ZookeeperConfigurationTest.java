/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.agora.services.zookeeper.config;

import net.jcip.annotations.NotThreadSafe;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.test.TestingServer;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.io.IOException;

/**
 * @author gibugeorge on 28/05/16.
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ZookeeperTestConfiguration.class, ZookeeperConfiguration.class})
@NotThreadSafe
public class ZookeeperConfigurationTest {

    @Autowired(required = false)
    private CuratorFramework curatorFramework;

    @Autowired
    private TestingServer testingServer;

    @BeforeClass
    public static void setup() {
        System.setProperty("agora.zookeeper.enabled", "true");
    }


    @Test
    public void whenZookeeperIsEnabledCuratorFrameworkShouldNotBeNUll() {
        Assert.assertNotNull(curatorFramework);
    }

    @After
    public void tearDown() throws IOException {
        testingServer.close();
    }
}
