package com.system.db.migration.resolver.migration.strategy;

import com.system.db.migration.resolver.callback.ApplicationContextAwareSpringJdbcCallbackResolver;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.ApplicationContext;

/**
 * The <class>SystemMigrationStrategy</class> defines the migration
 * strategy to use for executing system migrations
 *
 * @author Andrew
 */
public class SystemMigrationStrategy implements FlywayMigrationStrategy {

    @Autowired
    private ApplicationContext context;

    @Override
    public void migrate(Flyway flyway) {
        flyway.setSkipDefaultCallbacks(true);
        flyway.setCallbacks(new ApplicationContextAwareSpringJdbcCallbackResolver(context).resolveCallbacks());
        flyway.setBaselineOnMigrate(true);
        flyway.setBaselineVersionAsString("0");
        flyway.migrate();
    }
}
