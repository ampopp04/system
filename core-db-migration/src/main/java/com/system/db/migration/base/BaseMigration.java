package com.system.db.migration.base;


import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.callback.FlywayCallback;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;

/**
 * The <class>BaseMigration</class> defines the entry point for
 * flyway migrations
 * <p>
 * Any class extending this will receive a migration call
 * with callbacks at various states
 *
 * @author Andrew
 * @see SpringJdbcMigration#migrate(JdbcTemplate)
 * @see FlywayCallback
 */
public abstract class BaseMigration implements FlywayCallback, SpringJdbcMigration {

    /**
     * Runs before the clean task executes.
     *
     * @param connection A valid connection to the database.
     */
    @Override
    public void beforeClean(Connection connection) {
    }


    /**
     * Runs after the clean task executes.
     *
     * @param connection A valid connection to the database.
     */
    @Override
    public void afterClean(Connection connection) {
    }

    /**
     * Runs before the migrate task executes.
     *
     * @param connection A valid connection to the database.
     */
    @Override
    public void beforeMigrate(Connection connection) {
    }

    /**
     * Runs after the migrate task executes.
     *
     * @param connection A valid connection to the database.
     */
    @Override
    public void afterMigrate(Connection connection) {
    }

    /**
     * Runs before each migration script is executed.
     *
     * @param connection A valid connection to the database.
     * @param info       The current MigrationInfo for this migration.
     */
    @Override
    public void beforeEachMigrate(Connection connection, MigrationInfo info) {
    }

    /**
     * Runs after each migration script is executed.
     *
     * @param connection A valid connection to the database.
     * @param info       The current MigrationInfo for this migration.
     */
    @Override
    public void afterEachMigrate(Connection connection, MigrationInfo info) {
    }

    /**
     * Runs before the validate task executes.
     *
     * @param connection A valid connection to the database.
     */
    @Override
    public void beforeValidate(Connection connection) {
    }

    /**
     * Runs after the validate task executes.
     *
     * @param connection A valid connection to the database.
     */
    @Override
    public void afterValidate(Connection connection) {
    }

    /**
     * Runs before the baseline task executes.
     *
     * @param connection A valid connection to the database.
     */
    @Override
    public void beforeBaseline(Connection connection) {
    }

    /**
     * Runs after the baseline task executes.
     *
     * @param connection A valid connection to the database.
     */
    @Override
    public void afterBaseline(Connection connection) {
    }

    /**
     * Runs before the repair task executes.
     *
     * @param connection A valid connection to the database.
     */
    @Override
    public void beforeRepair(Connection connection) {
    }

    /**
     * Runs after the repair task executes.
     *
     * @param connection A valid connection to the database.
     */
    @Override
    public void afterRepair(Connection connection) {
    }

    /**
     * Runs before the info task executes.
     *
     * @param connection A valid connection to the database.
     */
    @Override
    public void beforeInfo(Connection connection) {
    }

    /**
     * Runs after the info task executes.
     *
     * @param connection A valid connection to the database.
     */
    @Override
    public void afterInfo(Connection connection) {
    }
}
