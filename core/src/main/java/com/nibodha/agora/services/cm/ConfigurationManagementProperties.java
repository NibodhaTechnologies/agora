/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.agora.services.cm;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author gibugeorge on 24/05/16.
 * @version 1.0
 */
@ConfigurationProperties(prefix = "agora.configuration-management")
public class ConfigurationManagementProperties {

    private boolean enabled = true;

    private String root = "config";

    private String defaultContext = "agora";

    private String profileSeparator = ",";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getDefaultContext() {
        return defaultContext;
    }

    public void setDefaultContext(String defaultContext) {
        this.defaultContext = defaultContext;
    }

    public String getProfileSeparator() {
        return profileSeparator;
    }

    public void setProfileSeparator(String profileSeparator) {
        this.profileSeparator = profileSeparator;
    }
}
