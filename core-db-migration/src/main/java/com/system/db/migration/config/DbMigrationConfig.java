package com.system.db.migration.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * The <class>DbMigrationConfig</class> defines the base
 * Spring configuration for the db migration project
 *
 * @author Andrew
 * @see PropertySource
 * @see Configuration
 */
@Configuration
@PropertySource(value = {"classpath:application-core-db-migration.properties"})
public class DbMigrationConfig {
}
