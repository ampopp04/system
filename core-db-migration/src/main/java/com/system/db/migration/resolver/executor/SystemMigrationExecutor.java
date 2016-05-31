package com.system.db.migration.resolver.executor;


import com.system.db.migration.base.SystemMigration;
import org.flywaydb.core.api.FlywayException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

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
            systemMigration.migrate(new org.springframework.jdbc.core.JdbcTemplate(
                    new SingleConnectionDataSource(connection, true)));
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
