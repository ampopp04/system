package com.system.db.migration.callback;

import com.system.inversion.component.InversionComponent;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.callback.FlywayCallback;

import java.sql.Connection;

/**
 * The <class>BaseMigrationCallback</class> defines the
 * base migration callbacks that can be extended
 *
 * @author Andrew
 * @see org.flywaydb.core.api.callback.FlywayCallback
 */
@InversionComponent
public abstract class BaseMigrationCallback implements FlywayCallback {

    @Override
    public void beforeClean(Connection connection) {
    }

    @Override
    public void afterClean(Connection connection) {
    }

    @Override
    public void beforeMigrate(Connection connection) {
    }

    @Override
    public void afterMigrate(Connection connection) {
    }

    @Override
    public void beforeEachMigrate(Connection connection, MigrationInfo info) {
    }

    @Override
    public void afterEachMigrate(Connection connection, MigrationInfo info) {
    }

    @Override
    public void beforeValidate(Connection connection) {
    }

    @Override
    public void afterValidate(Connection connection) {
    }

    @Override
    public void beforeBaseline(Connection connection) {
    }

    @Override
    public void afterBaseline(Connection connection) {
    }

    @Override
    public void beforeRepair(Connection connection) {
    }

    @Override
    public void afterRepair(Connection connection) {
    }

    @Override
    public void beforeInfo(Connection connection) {
    }

    @Override
    public void afterInfo(Connection connection) {
    }
}
