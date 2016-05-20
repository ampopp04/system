package com.system.db.migration.table;

import com.system.db.migration.base.BaseMigration;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.dialect.Dialect;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.jdbc.core.JdbcTemplate;


public abstract class TableCreationMigration extends BaseMigration {

    protected abstract Class[] getEntityClasses();

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        SchemaExport export = new SchemaExport(getConfiguration(), jdbcTemplate.getDataSource().getConnection());
        export.create(true, true);
    }

    private Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        setEntityClasses(configuration);

        configuration.setNamingStrategy(ImprovedNamingStrategy.INSTANCE);
        configuration.generateSchemaCreationScript(Dialect.getDialect(configuration.getProperties()));
        return configuration;
    }

    private void setEntityClasses(Configuration configuration) {
        for (Class entityClass : getEntityClasses()) {
            configuration.addAnnotatedClass(entityClass);
        }
    }
}
