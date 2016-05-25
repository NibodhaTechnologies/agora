/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.agora.services.discovery;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @author gibugeorge on 17/05/16.
 * @version 1.0
 */
@ConditionalOnProperty(value = "agora.service-discovery.enabled", matchIfMissing = true)
public class ServiceDiscoveryConfiguration {

}
