package com.system.db.migration.base;


import org.springframework.jdbc.core.JdbcTemplate;

/**
 * The <class>SystemMigration</class> defines a database
 * migration for the system
 *
 * @author Andrew
 */
public interface SystemMigration {
    /**
     * Executes this migration. The execution will automatically take place within a transaction, when the underlying
     * database supports it.
     *
     * @param jdbcTemplate The jdbcTemplate to use to execute statements.
     * @throws Exception when the migration failed.
     */
    void migrate(JdbcTemplate jdbcTemplate) throws Exception;
}