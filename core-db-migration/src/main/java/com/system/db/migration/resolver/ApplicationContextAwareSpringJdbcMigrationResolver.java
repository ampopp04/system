package com.system.db.migration.resolver;

import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.MigrationType;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.configuration.FlywayConfiguration;
import org.flywaydb.core.api.migration.MigrationChecksumProvider;
import org.flywaydb.core.api.migration.MigrationInfoProvider;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.flywaydb.core.api.resolver.ResolvedMigration;
import org.flywaydb.core.internal.resolver.MigrationInfoHelper;
import org.flywaydb.core.internal.resolver.ResolvedMigrationComparator;
import org.flywaydb.core.internal.resolver.ResolvedMigrationImpl;
import org.flywaydb.core.internal.resolver.spring.SpringJdbcMigrationExecutor;
import org.flywaydb.core.internal.resolver.spring.SpringJdbcMigrationResolver;
import org.flywaydb.core.internal.util.ClassUtils;
import org.flywaydb.core.internal.util.Location;
import org.flywaydb.core.internal.util.Pair;
import org.flywaydb.core.internal.util.StringUtils;
import org.flywaydb.core.internal.util.scanner.Scanner;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;


/**
 * * Migration resolver for {@link SpringJdbcMigration}s which are registered in the given {@link ApplicationContext}.
 * This resolver provides the ability to use other beans registered in the {@link ApplicationContext} and reference
 * them via Spring's dependency injection facility inside the {@link SpringJdbcMigration}s.
 */
public class ApplicationContextAwareSpringJdbcMigrationResolver extends SpringJdbcMigrationResolver {

    private final ApplicationContext applicationContext;

    public ApplicationContextAwareSpringJdbcMigrationResolver(Scanner scanner, Location location, FlywayConfiguration configuration, ApplicationContext applicationContext) {
        super(scanner, location, configuration);
        this.applicationContext = applicationContext;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<ResolvedMigration> resolveMigrations() {
        // get all beans of type SpringJdbcMigration from the application context
        Map<String, SpringJdbcMigration> springJdbcMigrationBeans =
                (Map<String, SpringJdbcMigration>) this.applicationContext.getBeansOfType(SpringJdbcMigration.class);

        ArrayList<ResolvedMigration> resolvedMigrations = new ArrayList<ResolvedMigration>();

        // resolve the migration and populate it with the migration info
        for (SpringJdbcMigration springJdbcMigrationBean : springJdbcMigrationBeans.values()) {
            ResolvedMigrationImpl resolvedMigration = extractMigrationInfo(springJdbcMigrationBean);
            resolvedMigration.setPhysicalLocation(ClassUtils.getLocationOnDisk(springJdbcMigrationBean.getClass()));
            resolvedMigration.setExecutor(new SpringJdbcMigrationExecutor(springJdbcMigrationBean));

            resolvedMigrations.add(resolvedMigration);
        }

        Collections.sort(resolvedMigrations, new ResolvedMigrationComparator());
        return resolvedMigrations;
    }

    ResolvedMigrationImpl extractMigrationInfo(SpringJdbcMigration springJdbcMigration) {
        Integer checksum = null;
        if (springJdbcMigration instanceof MigrationChecksumProvider) {
            MigrationChecksumProvider version = (MigrationChecksumProvider) springJdbcMigration;
            checksum = version.getChecksum();
        }

        String description;
        MigrationVersion version1;
        if (springJdbcMigration instanceof MigrationInfoProvider) {
            MigrationInfoProvider resolvedMigration = (MigrationInfoProvider) springJdbcMigration;
            version1 = resolvedMigration.getVersion();
            description = resolvedMigration.getDescription();
            if (!StringUtils.hasText(description)) {
                throw new FlywayException("Missing description for migration " + version1);
            }
        } else {
            String resolvedMigration1 = ClassUtils.getShortName(springJdbcMigration.getClass());
            if (!resolvedMigration1.startsWith("V") && !resolvedMigration1.startsWith("R")) {
                throw new FlywayException("Invalid Jdbc migration class name: " + springJdbcMigration.getClass()
                        .getName() + " => ensure it starts with V or R," + " or implement org.flywaydb.core.api.migration.MigrationInfoProvider for non-default naming");
            }

            String prefix = resolvedMigration1.substring(0, 1);
            Pair info = MigrationInfoHelper.extractVersionAndDescription(resolvedMigration1, prefix, "__", "");
            version1 = (MigrationVersion) info.getLeft();
            description = (String) info.getRight();
        }

        ResolvedMigrationImpl resolvedMigration2 = new ResolvedMigrationImpl();
        resolvedMigration2.setVersion(version1);
        resolvedMigration2.setDescription(description);
        resolvedMigration2.setScript(springJdbcMigration.getClass().getName());
        resolvedMigration2.setChecksum(checksum);
        resolvedMigration2.setType(MigrationType.SPRING_JDBC);
        return resolvedMigration2;
    }
}