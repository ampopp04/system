/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.db.migration.resolver.executor;


import com.system.db.migration.base.SystemMigration;
import org.flywaydb.core.api.FlywayException;

import java.sql.Connection;

/**
 * The <class>SystemMigrationExecutor</class> defines the executor
 * for running system migrations
 *
 * @author Andrew
 */
public class SystemMigrationExecutor implements org.flywaydb.core.api.resolver.MigrationExecutor {

    private final SystemMigration systemMigration;

    public SystemMigrationExecutor(SystemMigration systemMigration) {
        this.systemMigration = systemMigration;
    }

    @Override
    public void execute(Connection connection) {
        try {
            systemMigration.migrate();
        } catch (Exception e) {
            throw new FlywayException("Migration failed !", e);
        }
    }

    @Override
    public boolean executeInTransaction() {
        return true;
    }

    public SystemMigration getSystemMigration() {
        return systemMigration;
    }
}
