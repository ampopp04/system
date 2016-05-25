package com.system.db.migration.data;

import com.system.db.migration.base.BaseMigration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * The <class>BaseDataMigration</class> defines the entry point for
 * data based migrations.
 * <p>
 * Any class extending this will receive the opportunity to perform
 * data insertions.
 *
 * @author Andrew
 */
public abstract class BaseDataMigration extends BaseMigration {

    /**
     * Insert Data
     */
    protected abstract void insertData();

    /**
     * Insert data into the migration
     *
     * @param jdbcTemplate
     * @throws Exception
     */
    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        insertData();
    }
}
