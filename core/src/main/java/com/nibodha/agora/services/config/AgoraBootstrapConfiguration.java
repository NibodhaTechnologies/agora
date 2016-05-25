/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.agora.services.config;

import com.nibodha.agora.services.cm.config.ConfigurationManagementConfiguration;
import com.nibodha.agora.services.zookeeper.config.ZookeeperConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author gibugeorge on 23/05/16.
 * @version 1.0
 */
@Configuration
@Import({ZookeeperConfiguration.class, ConfigurationManagementConfiguration.class})
public class AgoraBootstrapConfiguration {
}
