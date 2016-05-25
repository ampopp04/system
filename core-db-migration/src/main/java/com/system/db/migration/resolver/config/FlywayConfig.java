package com.system.db.migration.resolver.config;

import com.system.db.migration.resolver.ApplicationContextAwareSpringJdbcMigrationResolver;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.internal.dbsupport.DbSupport;
import org.flywaydb.core.internal.dbsupport.h2.H2DbSupport;
import org.flywaydb.core.internal.dbsupport.mysql.MySQLDbSupport;
import org.flywaydb.core.internal.resolver.sql.SqlMigrationResolver;
import org.flywaydb.core.internal.util.Location;
import org.flywaydb.core.internal.util.PlaceholderReplacer;
import org.flywaydb.core.internal.util.scanner.Scanner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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
@ComponentScan("db.migration")
public class FlywayConfig {

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
                    ApplicationContextAwareSpringJdbcMigrationResolver resolver = new ApplicationContextAwareSpringJdbcMigrationResolver(
                            new Scanner(Thread.currentThread().getContextClassLoader()),
                            new Location("classpath:db/migration"),
                            context.getBean(org.flywaydb.core.api.configuration.FlywayConfiguration.class),
                            context);

                    SqlMigrationResolver sqlMigrationResolver = null;
                    try {
                        sqlMigrationResolver = new SqlMigrationResolver(
                                getDbSupport(),
                                new Scanner(Thread.currentThread().getContextClassLoader()),
                                new Location("classpath:db/migration"),
                                PlaceholderReplacer.NO_PLACEHOLDERS,
                                "UTF-8",
                                "V",
                                "R",
                                "__",
                                ".sql");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    flyway.setResolvers(sqlMigrationResolver, resolver);
                }
                return o;
            }

            private DbSupport getDbSupport() throws SQLException {
                DataSource dataSource = context.getBean(DataSource.class);
                if (((org.apache.tomcat.jdbc.pool.DataSource) dataSource).getDriverClassName().equals("org.h2.Driver")) {
                    return new H2DbSupport(dataSource.getConnection());
                } else {
                    return new MySQLDbSupport(dataSource.getConnection());
                }
            }
        };
    }
}