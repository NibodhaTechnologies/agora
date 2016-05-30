/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.agora.services.cm.config;

import com.nibodha.agora.services.cm.ConfigurationManagementPropertySourceLocator;
import com.nibodha.agora.services.zookeeper.config.ZookeeperConfiguration;
import com.nibodha.agora.services.zookeeper.config.ZookeeperTestConfiguration;
import net.jcip.annotations.NotThreadSafe;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ZookeeperTestConfiguration.class, ZookeeperConfiguration.class, ConfigurationManagementConfiguration.class})
@NotThreadSafe
public class ConfigurationManagementConfigurationTest {

    @Inject
    private TestingServer testingServer;

    @BeforeClass
    public static void setup() {
        System.setProperty("agora.zookeeper.enabled", "true");
        System.setProperty("agora.configuration-management.enabled", "true");
    }

    @Autowired(required = false)
    private ConfigurationManagementPropertySourceLocator locator;

    @Test
    public void testConfigurationManagementConfig() {
        Assert.assertNotNull(locator);
    }

    @After
    public void tearDown() throws IOException {
        testingServer.close();
    }

}
