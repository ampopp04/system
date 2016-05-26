package com.system.db.migration.table;

import com.system.db.migration.callback.BaseMigrationCallback;
import org.flywaydb.core.api.MigrationInfo;
import org.springframework.stereotype.Component;

import java.sql.Connection;

/**
 * The <class>TableCreationMigrationCallback</class> defines
 * callback logic for how to process table creations
 *
 * @author Andrew
 * @see TableCreationMigration
 */
@Component
public class TableCreationMigrationCallback extends BaseMigrationCallback {

    /**
     * Runs before each migration script is executed.
     *
     * @param connection A valid connection to the database.
     * @param info       The current MigrationInfo for this migration.
     */
    @Override
    public void beforeEachMigrate(Connection connection, MigrationInfo info) {
        System.err.println("-------------------beforeEachMigrate");
    }

    /**
     * Runs after each migration script is executed.
     *
     * @param connection A valid connection to the database.
     * @param info       The current MigrationInfo for this migration.
     */
    @Override
    public void afterEachMigrate(Connection connection, MigrationInfo info) {
        System.err.println("-------------------afterEachMigrate");
    }
}