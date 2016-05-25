/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.agora.services.discovery;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author gibugeorge on 16/05/16.
 * @version 1.0
 */
@ConfigurationProperties(prefix = "agora.service-discovery")
public class ServiceDiscoveryProperties {

    private boolean enabled = true;

    @NotEmpty
    private String connectString = "localhost:2181";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }
}
