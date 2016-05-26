package com.system.db.migration.base;


import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * The <class>BaseMigration</class> defines the entry point for
 * flyway migrations
 * <p>
 *
 * @author Andrew
 * @see SpringJdbcMigration#migrate(JdbcTemplate)
 */
public abstract class BaseMigration implements SpringJdbcMigration {
}