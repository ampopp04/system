package com.system.db.migration.resolver.config;

import com.system.db.migration.resolver.migration.ApplicationContextAwareSystemMigrationResolver;
import com.system.db.migration.resolver.migration.strategy.SystemMigrationStrategy;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.internal.dbsupport.DbSupport;
import org.flywaydb.core.internal.dbsupport.h2.H2DbSupport;
import org.flywaydb.core.internal.dbsupport.mysql.MySQLDbSupport;
import org.flywaydb.core.internal.resolver.sql.SqlMigrationResolver;
import org.flywaydb.core.internal.util.Location;
import org.flywaydb.core.internal.util.Locations;
import org.flywaydb.core.internal.util.PlaceholderReplacer;
import org.flywaydb.core.internal.util.scanner.Scanner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * The <class>FlywayConfig</class> defines Flyway
 * behavior that is used to autowire
 * Spring dependencies into Flyway migrations.
 *
 * @author Andrew
 */
@Configuration
public class FlywayConfig {

    @Bean
    public SystemMigrationStrategy getSystemMigrationStrategy() {
        return new SystemMigrationStrategy();
    }

    @Bean
    public BeanPostProcessor postProcessFlyway(ApplicationContext context) {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
                return o;
            }

            @Override
            public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
                if (o instanceof Flyway) {
                    Flyway flyway = (Flyway) o;
                    flyway.setSkipDefaultResolvers(true);
                    ApplicationContextAwareSystemMigrationResolver resolver = new ApplicationContextAwareSystemMigrationResolver(
                            new Scanner(Thread.currentThread().getContextClassLoader()),
                            new Location("classpath:db/migration"),
                            context.getBean(org.flywaydb.core.api.configuration.FlywayConfiguration.class),
                            context);

                    SqlMigrationResolver sqlMigrationResolver = null;
                    try {
                        Flyway config = new Flyway();

                        config.setEncoding("UTF-8");
                        config.setSqlMigrationPrefix("V");
                        config.setRepeatableSqlMigrationPrefix("R");
                        config.setSqlMigrationSeparator("__");
                        config.setSqlMigrationSuffix(".sql");

                        sqlMigrationResolver = new SqlMigrationResolver(
                                getDbSupport(),
                                new Scanner(Thread.currentThread().getContextClassLoader()),
                                new Locations("classpath:db/migration"),
                                PlaceholderReplacer.NO_PLACEHOLDERS,
                                config
                        );
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    flyway.setResolvers(sqlMigrationResolver, resolver);
                }
                return o;
            }

            private DbSupport getDbSupport() throws SQLException {
                DataSource dataSource = context.getBean(DataSource.class);
                if (((HikariDataSource) dataSource).getDriverClassName().equals("org.h2.Driver")) {
                    return new H2DbSupport(dataSource.getConnection());
                } else {
                    return new MySQLDbSupport(dataSource.getConnection());
                }
            }
        };
    }
}