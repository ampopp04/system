/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.db.config;

import com.system.db.repository.base.entity.BaseEntityRepositoryImpl;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import static com.system.db.util.entity.EntityUtils.getJpaProperties;
import static com.system.inversion.util.InversionUtils.SYSTEM_PACKAGE_ROOT;

/**
 * The <class>DbConfig</class> defines the base
 * Spring configuration for the db project
 *
 * @author Andrew
 */
@EnableSystemRepositories(basePackages = SYSTEM_PACKAGE_ROOT, repositoryBaseClass = BaseEntityRepositoryImpl.class)
@EnableTransactionManagement(proxyTargetClass = true)
@PropertySource(value = {"classpath:application-core-db.properties"})
@Configuration
@EnableCaching
@CacheConfig(cacheManager = "EhCacheCacheManager")
public class DbConfig {

    /*@Bean
    public DropwizardMeterRegistry dropwizardMeterRegistry() {
        return new DropwizardMeterRegistry(HierarchicalNameMapper.DEFAULT, Clock.SYSTEM);
    }

    @Bean
    public HealthCheckRegistry healthCheckRegistry() {
        return SharedHealthCheckRegistries.setDefault("default");
    }

    @Bean
    public MicrometerMetricsTrackerFactory micrometerMetricsTrackerFactory() {
        return new MicrometerMetricsTrackerFactory(dropwizardMeterRegistry());
    }
*/
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariDataSource dataSource(DataSourceProperties properties) {//, MicrometerMetricsTrackerFactory micrometerMetricsTrackerFactory, HealthCheckRegistry healthCheckRegistry) {
        HikariDataSource dataSource = properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();

        //dataSource.setMetricsTrackerFactory(micrometerMetricsTrackerFactory);
        // dataSource.setHealthCheckRegistry(healthCheckRegistry);

        if (properties.getName() != null) {
            dataSource.setPoolName(properties.getName());
        }
        return dataSource;
    }

    /**
     * Custom create the entity manager factory so we can override it's base packages to scan
     */
    @Bean
    public EntityManagerFactory entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter, Environment env) {
        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();

        lef.setDataSource(dataSource);
        lef.setJpaVendorAdapter(jpaVendorAdapter);
        lef.setPackagesToScan(SYSTEM_PACKAGE_ROOT);
        lef.setJpaProperties(getJpaProperties(env));
        lef.afterPropertiesSet();
        return lef.getObject();
    }

}
