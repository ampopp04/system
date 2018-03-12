/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.db.migration.config;

import org.springframework.context.annotation.ComponentScan;
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
@ComponentScan("db.migration")
@PropertySource(value = {"classpath:application-core-db-migration.properties"})
public class DbMigrationConfig {
}
