/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.agora.launcher;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author gibugeorge on 29/05/16.
 * @version 1.0
 */
public class PlatformLauncherTest {

    private final PlatformLauncher platformLauncher = new PlatformLauncher();

    @BeforeClass
    public static void setup() {
        System.setProperty("config.location", "classpath:");
        System.setProperty("agora.loader.path", System.getProperty("user.home"));
    }

    @Test
    public void testPlatformLauncher() {
        platformLauncher.run(new String[]{""});
    }
}
