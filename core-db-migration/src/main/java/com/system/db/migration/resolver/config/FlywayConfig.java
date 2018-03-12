/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.db.migration.resolver.config;

import com.system.db.migration.resolver.migration.ApplicationContextAwareSystemMigrationResolver;
import com.system.db.migration.resolver.migration.strategy.SystemMigrationStrategy;
import org.flywaydb.core.Flyway;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The <class>FlywayConfig</class> defines Flyway
 * behavior that is used to autowire
 * Spring dependencies into Flyway migrations.
 *
 * @author Andrew
 */
@Configuration
public class FlywayConfig {

    @Bean
    public SystemMigrationStrategy getSystemMigrationStrategy() {
        return new SystemMigrationStrategy();
    }

    @Bean
    public BeanPostProcessor postProcessFlyway(ApplicationContext context) {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
                return o;
            }

            @Override
            public Object postProcessAfterInitialization(Object o, String s) throws BeansException {

                if (o instanceof Flyway) {

                    Flyway flyway = (Flyway) o;
                    ApplicationContextAwareSystemMigrationResolver resolver = new ApplicationContextAwareSystemMigrationResolver(context);
                    flyway.setResolvers(resolver);

                }

                return o;
            }

        };
    }
}