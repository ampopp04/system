/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.db.migration.table.configuration;

import com.system.db.entity.Entity;
import org.hibernate.cfg.Configuration;

import javax.persistence.ManyToMany;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.system.util.collection.CollectionUtils.iterable;

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

    public static void addDependentPersistentClasses(List<Class<? extends Entity>> entityClassList, List<String> managedClassNames) {
        for (Class<? extends Entity> entityClass : iterable(entityClassList)) {
            for (Class<? extends Entity> clazz : iterable(getDependentPersistentClassList(entityClass, new HashSet<>()))) {
                if (!entityClassList.contains(clazz)) {
                    managedClassNames.add(clazz.getName());
                }
            }
        }
    }

    public static Set<Class<? extends Entity>> getDependentPersistentClassList(Class<? extends Entity> entity, Set<Class<? extends Entity>> dependentEntitySet) {
        for (Field field : iterable(entity.getDeclaredFields())) {
            handleFieldTypeDependencies(field, dependentEntitySet);
            handleFieldAnnotationTypeDependencies(field, dependentEntitySet);
        }

        return dependentEntitySet;
    }

    private static void handleFieldAnnotationTypeDependencies(Field field, Set<Class<? extends Entity>> dependentEntitySet) {
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

    private static void handleFieldTypeDependencies(Field field, Set<Class<? extends Entity>> dependentEntitySet) {
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

}