package com.system.db.migration.table;

import com.system.db.entity.Entity;
import com.system.db.migration.base.SystemMigration;
import com.system.db.migration.callback.BaseMigrationCallback;
import com.system.db.migration.resolver.executor.SystemMigrationExecutor;
import com.system.util.collection.CollectionUtils;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.resolver.MigrationExecutor;
import org.flywaydb.core.internal.info.MigrationInfoImpl;

import java.sql.Connection;
import java.util.List;

/**
 * The <class>TableCreationMigrationCallback</class> defines
 * callback logic for how to process table creations
 *
 * @author Andrew
 * @see TableCreationMigration
 */
public abstract class TableCreationMigrationCallback extends BaseMigrationCallback {

    ///////////////////////////////////////////////////////////////////////
    ////////                                            Abstract Method Call                                           //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Callback invoked after new table creation
     *
     * @param tableEntityClass is the new table being created
     */
    protected abstract void afterTableCreation(Class<? extends Entity> tableEntityClass);

    ///////////////////////////////////////////////////////////////////////
    ////////                                                   Constructor                                                       //////////
    //////////////////////////////////////////////////////////////////////

    public TableCreationMigrationCallback() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Migration Callback                                               //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Iterates over each table entity being created to send to the callback
     *
     * @param connection
     * @param info
     */
    @Override
    public void afterEachMigrate(Connection connection, MigrationInfo info) {
        if (info instanceof MigrationInfoImpl) {
            for (Class<? extends Entity> tableEntityClass : CollectionUtils.iterable(getTableCreationList((MigrationInfoImpl) info))) {
                afterTableCreation(tableEntityClass);
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                    Getter Methods                                               //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Extracts out the table entities being created from the migration
     *
     * @param info
     * @return
     */
    private List<Class<? extends Entity>> getTableCreationList(MigrationInfoImpl info) {
        MigrationExecutor migrationExecutor = info.getResolvedMigration().getExecutor();
        if (migrationExecutor instanceof SystemMigrationExecutor) {
            SystemMigration systemMigration = ((SystemMigrationExecutor) migrationExecutor).getSystemMigration();
            if (systemMigration instanceof TableCreationMigration) {
                return ((TableCreationMigration) systemMigration).getEntityClasses();
            }
        }
        return null;
    }
}