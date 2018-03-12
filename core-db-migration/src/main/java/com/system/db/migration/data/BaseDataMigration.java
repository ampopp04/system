/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.db.migration.data;

import com.system.db.migration.base.BaseMigration;

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
     * @throws Exception
     */
    @Override
    public void migrate() throws Exception {
        insertData();
    }
}
