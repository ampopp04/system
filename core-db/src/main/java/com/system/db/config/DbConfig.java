package com.system.db.config;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The <class>DbConfig</class> defines the base
 * Spring configuration for the db project
 *
 * @author Andrew
 */
@EnableJpaRepositories(basePackages = "com.system")
@EntityScan(basePackages = "com.system")
@PropertySource(value = {"classpath:application-core-db.properties"})
@Configuration
public class DbConfig {
}
