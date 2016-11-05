package com.system.db.migration.table;

import com.system.db.entity.Entity;
import com.system.db.migration.data.BaseDataMigration;
import com.system.db.migration.table.configuration.TableConfiguration;
import com.system.db.util.entity.EntityUtils;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.dialect.Dialect;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static com.system.util.collection.CollectionUtils.iterable;
import static com.system.util.collection.CollectionUtils.iterate;

/**
 * The <class>TableCreationMigration</class> defines the entry point for
 * table based creation migrations.
 * <p>
 * Any class extending this will receive the opportunity to perform
 * table creation and data insertions.
 *
 * @author Andrew
 */
public abstract class TableCreationMigration extends BaseDataMigration {

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Properties                                                //////////
    //////////////////////////////////////////////////////////////////////

    private TableConfiguration configuration;

    @Autowired
    private Environment environment;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Abstract Methods                                                //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Returns the array of classes for which tables should be created for
     *
     * @return
     */
    protected abstract List<Class<? extends Entity>> getEntityClasses();

    ///////////////////////////////////////////////////////////////////////
    ////////                                               Migration Methods                                             //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Set the table creation entities to the configuration
     *
     * @param configuration
     * @see Configuration#addAnnotatedClass(Class)
     */
    private void setEntityClasses(TableConfiguration configuration) {
        List<Class<? extends Entity>> entityClassList = getEntityClasses();
        iterate(iterable(entityClassList), entityClass -> {
            configuration.addAnnotatedClass(entityClass);
        });

        configuration.addDependentPersistentClasses(entityClassList);
    }

    /**
     * Perform the table creation migration
     *
     * @param jdbcTemplate
     * @throws Exception
     */
    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        //Perform table creation
        TableConfiguration config = getConfigurationWithEntities();

        SchemaExport export = new SchemaExport(config, jdbcTemplate.getDataSource().getConnection());
        export.execute(true, true, false, true);

        config.addMigratedEntities(getEntityClasses());

        //Perform any data insertion
        super.migrate(jdbcTemplate);
    }

    /**
     * Create the hibernate configuration used to perform the underlying table creation sql
     *
     * @return
     * @see TableConfiguration
     */
    private TableConfiguration getConfiguration() {
        if (configuration == null) {
            configuration = new TableConfiguration();
            configuration.setProperties(EntityUtils.getJpaProperties(environment));
            configuration.setNamingStrategy(ImprovedNamingStrategy.INSTANCE);
            configuration.generateSchemaCreationScript(Dialect.getDialect(configuration.getProperties()));
        }
        return configuration;
    }

    private TableConfiguration getConfigurationWithEntities() {
        TableConfiguration config = getConfiguration();
        setEntityClasses(config);
        return config;
    }
}
