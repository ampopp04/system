package com.system.db.migration.base;


/**
 * The <class>SystemMigration</class> defines a database
 * migration for the system
 *
 * @author Andrew
 */
public interface SystemMigration {
    /**
     * Executes this migration. The execution will automatically take place within a transaction, when the underlying
     * database supports it.
     *
     * @throws Exception when the migration failed.
     */
    void migrate() throws Exception;
}