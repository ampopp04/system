package com.system.db.migration.table.configuration;

import com.system.db.entity.Entity;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.mapping.Table;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.system.util.collection.CollectionUtils.iterable;
import static com.system.util.collection.CollectionUtils.newList;

/**
 * The <class>TableConfiguration</class> manages
 * the configuration of tables to be migrated. It allows for
 * keeping track of which entities have been migrated versus which that have not.
 * <p>
 * This enables an easy to use two pass migration system.
 *
 * @author Andrew
 */
public class TableConfiguration extends Configuration {

    /**
     * Entity names that have been migrated
     */
    private Set<String> migratedEntityNames = new HashSet();

    /**
     * Maintain the list that have been migrated
     *
     * @return
     */
    @Override
    public Iterator<Table> getTableMappings() {
        List<Table> tableList = newList();
        for (String tableName : iterable(tables)) {
            if (!tablePreviouslyMigrated(tableName)) {
                tableList.add(tables.get(tableName));
            }
        }
        return tableList.iterator();
    }

    /**
     * Add an entity as being migrated
     *
     * @param migratedEntityList
     */
    public void addMigratedEntities(List<Class<? extends Entity>> migratedEntityList) {
        for (Class clazz : iterable(migratedEntityList)) {
            migratedEntityNames.add(ImprovedNamingStrategy.INSTANCE.classToTableName(clazz.getSimpleName()));
        }
    }

    private boolean tablePreviouslyMigrated(String tableName) {
        return migratedEntityNames.contains(tableName);
    }
}