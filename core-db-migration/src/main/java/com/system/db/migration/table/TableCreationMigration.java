package com.system.db.migration.table;

import com.system.db.entity.Entity;
import com.system.db.migration.data.BaseDataMigration;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.dialect.Dialect;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

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

    /**
     * Returns the array of classes for which tables should be created for
     *
     * @return
     */
    protected abstract List<Class<? extends Entity>> getEntityClasses();

    /**
     * Set the table creation entities to the configuration
     *
     * @param configuration
     * @see Configuration#addAnnotatedClass(Class)
     */
    private void setEntityClasses(Configuration configuration) {
        getEntityClasses().forEach(configuration::addAnnotatedClass);
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
        SchemaExport export = new SchemaExport(getConfiguration(), jdbcTemplate.getDataSource().getConnection());
        export.create(true, true);

        //Perform any data insertion
        super.migrate(jdbcTemplate);
    }

    /**
     * Create the hibernate configuration used to perform the underlying table creation sql
     *
     * @return
     * @see Configuration
     */
    private Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        setEntityClasses(configuration);

        configuration.setNamingStrategy(ImprovedNamingStrategy.INSTANCE);
        configuration.generateSchemaCreationScript(Dialect.getDialect(configuration.getProperties()));
        return configuration;
    }
}
