/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.db.migration.base;


import javax.transaction.Transactional;

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
    @Transactional
    void migrate() throws Exception;
}