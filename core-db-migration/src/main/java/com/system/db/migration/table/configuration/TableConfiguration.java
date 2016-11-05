package com.system.db.migration.table.configuration;

import com.system.db.entity.Entity;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.mapping.Table;

import java.lang.reflect.Field;
import java.util.*;

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
     * The dependent entities that should not be migrated but are already created and referenced in new entities
     */
    private Set<String> dependentEntityNames = new HashSet<>();

    /**
     * Maintain the list that have been migrated
     *
     * @return
     */
    @Override
    public Iterator<Table> getTableMappings() {
        List<Table> tableList = newList();
        for (String tableName : iterable(tables)) {
            if (!tablePreviouslyMigrated(tableName) && !dependentEntityNames.contains(tableName)) {
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

    public void addDependentPersistentClasses(List<Class<? extends Entity>> entityClassList) {
        for (Class<? extends Entity> entityClass : iterable(entityClassList)) {
            for (Class<? extends Entity> clazz : iterable(getDependentPersistentClassList(entityClass, new ArrayList<>()))) {
                if (!entityClassList.contains(clazz)) {
                    this.dependentEntityNames.add(ImprovedNamingStrategy.INSTANCE.classToTableName(clazz.getSimpleName()));
                    this.addAnnotatedClass(clazz);
                }
            }
        }
    }


    public List<Class<? extends Entity>> getDependentPersistentClassList(Class<? extends Entity> entity, List<Class<? extends Entity>> dependentEntityList) {
        for (Field field : iterable(entity.getDeclaredFields())) {
            if (Entity.class.isAssignableFrom(field.getType())) {
                Class<? extends Entity> dependentEntity = (Class<? extends Entity>) field.getType();
                if (dependentEntityList.contains(dependentEntity)) {
                    //Cycle detected, we are done...
                    return dependentEntityList;
                }

                dependentEntityList.add(dependentEntity);
                dependentEntityList.addAll(getDependentPersistentClassList(dependentEntity, dependentEntityList));
            }
        }

        return dependentEntityList;
    }


    private boolean tablePreviouslyMigrated(String tableName) {
        return migratedEntityNames.contains(tableName);
    }

}