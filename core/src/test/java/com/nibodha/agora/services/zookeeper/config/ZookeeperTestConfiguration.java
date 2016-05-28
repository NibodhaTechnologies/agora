/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.agora.services.zookeeper.config;

import org.apache.curator.test.TestingServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gibugeorge on 28/05/16.
 * @version 1.0
 */
@Configuration
public class ZookeeperTestConfiguration {
    @Bean
    public TestingServer testingServer() throws Exception {
        return new TestingServer();
    }
}
