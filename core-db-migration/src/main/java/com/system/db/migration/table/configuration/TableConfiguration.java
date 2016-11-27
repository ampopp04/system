package com.system.db.migration.table.configuration;

import com.system.db.entity.Entity;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.mapping.Table;

import javax.persistence.ManyToMany;
import java.lang.reflect.Field;
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
            for (Class<? extends Entity> clazz : iterable(getDependentPersistentClassList(entityClass, new HashSet<>()))) {
                if (!entityClassList.contains(clazz)) {
                    this.dependentEntityNames.add(ImprovedNamingStrategy.INSTANCE.classToTableName(clazz.getSimpleName()));
                    this.addAnnotatedClass(clazz);
                }
            }
        }
    }

    public Set<Class<? extends Entity>> getDependentPersistentClassList(Class<? extends Entity> entity, Set<Class<? extends Entity>> dependentEntitySet) {
        for (Field field : iterable(entity.getDeclaredFields())) {
            handleFieldTypeDependencies(field, dependentEntitySet);
            handleFieldAnnotationTypeDependencies(field, dependentEntitySet);
        }

        return dependentEntitySet;
    }

    private void handleFieldAnnotationTypeDependencies(Field field, Set<Class<? extends Entity>> dependentEntitySet) {
        ManyToMany[] annotations = field.getAnnotationsByType(ManyToMany.class);

        if (annotations != null) {
            for (ManyToMany annotation : annotations) {
                Class clazz = annotation.targetEntity();
                if (clazz != null) {
                    if (Entity.class.isAssignableFrom(clazz)) {
                        Class<? extends Entity> dependentEntity = (Class<? extends Entity>) clazz;
                        if (dependentEntitySet.contains(dependentEntity)) {
                            //Cycle;
                        }

                        dependentEntitySet.add(dependentEntity);
                        dependentEntitySet.addAll(getDependentPersistentClassList(dependentEntity, dependentEntitySet));
                    }
                }
            }
        }
    }

    private void handleFieldTypeDependencies(Field field, Set<Class<? extends Entity>> dependentEntitySet) {
        if (Entity.class.isAssignableFrom(field.getType())) {
            Class<? extends Entity> dependentEntity = (Class<? extends Entity>) field.getType();
            if (dependentEntitySet.contains(dependentEntity)) {
                //Cycle detected, we are done...
                return;
            }

            dependentEntitySet.add(dependentEntity);
            dependentEntitySet.addAll(getDependentPersistentClassList(dependentEntity, dependentEntitySet));
        }
    }

    private boolean tablePreviouslyMigrated(String tableName) {
        return migratedEntityNames.contains(tableName);
    }

}