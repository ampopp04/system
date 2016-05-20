package com.system.db.migration.base;


import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.callback.FlywayCallback;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;

import java.sql.Connection;

public abstract class BaseMigration implements FlywayCallback, SpringJdbcMigration {

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
