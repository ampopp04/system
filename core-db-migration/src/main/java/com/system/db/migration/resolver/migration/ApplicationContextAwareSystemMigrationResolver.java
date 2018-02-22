package com.system.db.migration.resolver.migration;

import com.system.db.migration.base.SystemMigration;
import com.system.db.migration.resolver.executor.SystemMigrationExecutor;
import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.MigrationType;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.migration.MigrationChecksumProvider;
import org.flywaydb.core.api.migration.MigrationInfoProvider;
import org.flywaydb.core.api.resolver.MigrationResolver;
import org.flywaydb.core.api.resolver.ResolvedMigration;
import org.flywaydb.core.internal.resolver.MigrationInfoHelper;
import org.flywaydb.core.internal.resolver.ResolvedMigrationComparator;
import org.flywaydb.core.internal.resolver.ResolvedMigrationImpl;
import org.flywaydb.core.internal.util.ClassUtils;
import org.flywaydb.core.internal.util.Pair;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;


/**
 * * Migration resolver for {@link SystemMigration}s which are registered in the given {@link ApplicationContext}.
 * This resolver provides the ability to use other beans registered in the {@link ApplicationContext} and reference
 * them via Spring's dependency injection facility inside the {@link SystemMigration}s.
 */
public class ApplicationContextAwareSystemMigrationResolver implements MigrationResolver {

    private final ApplicationContext applicationContext;

    public ApplicationContextAwareSystemMigrationResolver(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<ResolvedMigration> resolveMigrations() {
        // get all beans of type SystemMigration from the application context
        Map<String, SystemMigration> systemMigrationBeans =
                (Map<String, SystemMigration>) this.applicationContext.getBeansOfType(SystemMigration.class);

        ArrayList<ResolvedMigration> resolvedMigrations = new ArrayList<ResolvedMigration>();

        // resolve the migration and populate it with the migration info
        for (SystemMigration systemMigrationBean : systemMigrationBeans.values()) {
            ResolvedMigrationImpl resolvedMigration = extractMigrationInfo(systemMigrationBean);
            resolvedMigration.setPhysicalLocation(ClassUtils.getLocationOnDisk(systemMigrationBean.getClass()));
            resolvedMigration.setExecutor(new SystemMigrationExecutor(systemMigrationBean));

            resolvedMigrations.add(resolvedMigration);
        }
        Collections.sort(resolvedMigrations, new ResolvedMigrationComparator());
        return resolvedMigrations;
    }

    ResolvedMigrationImpl extractMigrationInfo(SystemMigration systemMigration) {
        Integer checksum = null;
        if (systemMigration instanceof MigrationChecksumProvider) {
            MigrationChecksumProvider version = (MigrationChecksumProvider) systemMigration;
            checksum = version.getChecksum();
        }

        String description;
        MigrationVersion version1;
        if (systemMigration instanceof MigrationInfoProvider) {
            MigrationInfoProvider resolvedMigration = (MigrationInfoProvider) systemMigration;
            version1 = resolvedMigration.getVersion();
            description = resolvedMigration.getDescription();
            if (!StringUtils.hasText(description)) {
                throw new FlywayException("Missing description for migration " + version1);
            }
        } else {
            String resolvedMigration1 = ClassUtils.getShortName(systemMigration.getClass());
            if (!resolvedMigration1.startsWith("V") && !resolvedMigration1.startsWith("R")) {
                throw new FlywayException("Invalid Jdbc migration class name: " + systemMigration.getClass()
                        .getName() + " => ensure it starts with V or R," + " or implement org.flywaydb.core.api.migration.MigrationInfoProvider for non-default naming");
            }

            String prefix = resolvedMigration1.substring(0, 1);
            Pair info = MigrationInfoHelper.extractVersionAndDescription(resolvedMigration1, prefix, "__", "", false);
            version1 = (MigrationVersion) info.getLeft();
            description = (String) info.getRight();
        }

        ResolvedMigrationImpl resolvedMigration2 = new ResolvedMigrationImpl();
        resolvedMigration2.setVersion(version1);
        resolvedMigration2.setDescription(description);
        resolvedMigration2.setScript(systemMigration.getClass().getName());
        resolvedMigration2.setChecksum(checksum);
        resolvedMigration2.setType(MigrationType.SPRING_JDBC);
        return resolvedMigration2;
    }

}