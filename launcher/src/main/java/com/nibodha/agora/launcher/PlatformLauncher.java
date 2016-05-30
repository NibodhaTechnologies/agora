/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.agora.launcher;

import com.nibodha.agora.env.PlatformEnvironment;
import com.nibodha.agora.services.file.AbstractDirectoryWatcher;
import com.nibodha.agora.services.jdbc.config.DatasourceConfiguration;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * Created by gibugeorge on 28/11/15.
 */
@Configuration
@EnableAutoConfiguration(exclude = {PropertyPlaceholderAutoConfiguration.class, BatchAutoConfiguration.class, HibernateJpaAutoConfiguration.class,
        DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, ActiveMQAutoConfiguration.class,
        MongoDataAutoConfiguration.class, CacheAutoConfiguration.class, JmsAutoConfiguration.class, DataSourceAutoConfiguration.class,
        SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class, ThymeleafAutoConfiguration.class
})

public class PlatformLauncher {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformLauncher.class);

    public static void main(final String[] args) {

        printSystemProperties();
        final PlatformLauncher launcher = new PlatformLauncher();
        launcher.run(args);

    }

    private static void printSystemProperties() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Java System Properties");
            final Properties properties = System.getProperties();
            final Enumeration<String> names = (Enumeration<String>) properties.propertyNames();
            while (names.hasMoreElements()) {
                final String name = names.nextElement();
                LOGGER.info(name + " = " + properties.getProperty(name));
            }
        }
    }

    public void run(final String[] args) {

        final SpringApplication application = createSpringApplication(new PlatformEnvironment());
        final ConfigurableApplicationContext configurableApplicationContext = application.run(args);
        final Map<String, AbstractDirectoryWatcher> directoryWatchers = configurableApplicationContext.getBeansOfType(AbstractDirectoryWatcher.class);
        final Set<String> keySet = directoryWatchers.keySet();
        for (final String key : keySet) {
            AbstractDirectoryWatcher abstractDirectoryWatcher = directoryWatchers.get(key);
            abstractDirectoryWatcher.start();
        }


    }

    private SpringApplication createSpringApplication(final ConfigurableEnvironment environment) {
        final SpringApplication application = new SpringApplication(PlatformLauncher.class);
        application.setEnvironment(environment);
        setContextClassLoader(environment.getProperty("agora.loader.path"));
        application.addInitializers(new ApplicationContextInitializer<ConfigurableApplicationContext>() {
            @Override
            public void initialize(final ConfigurableApplicationContext applicationContext) {
                applicationContext.addBeanFactoryPostProcessor(new DatasourceConfiguration(applicationContext.getEnvironment()));
            }
        });
        application.setRegisterShutdownHook(true);
        application.setWebEnvironment(true);
        application.setLogStartupInfo(true);
        return application;
    }

    private void setContextClassLoader(final String agoraLoaderPath) {
        if (StringUtils.isEmpty(agoraLoaderPath)) {
            return;
        }
        final File loaderDir = new File(agoraLoaderPath);
        if (loaderDir.exists() && loaderDir.isDirectory()) {
            final Collection<File> listOfFiles = FileUtils.listFiles(loaderDir, new String[]{"jar"}, false);
            final URLClassLoader contextClassLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
            final URL[] urls = contextClassLoader.getURLs();
            final List<URL> urlList = new ArrayList<>(Arrays.asList(urls));
            for (final File file : listOfFiles) {
                try {
                    urlList.add(file.toURI().toURL());
                } catch (MalformedURLException e) {
                    LOGGER.error("Error getting url", e);
                }
            }
            final URL[] newUrls = new URL[urlList.size()];
            Thread.currentThread().setContextClassLoader(new URLClassLoader(urlList.toArray(newUrls), contextClassLoader));
        }
    }

}
