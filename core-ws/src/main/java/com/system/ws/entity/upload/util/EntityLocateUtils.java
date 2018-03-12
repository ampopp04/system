/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.ws.entity.upload.util;

import com.system.db.entity.base.BaseEntity;
import com.system.db.entity.named.NamedEntity;
import com.system.db.repository.base.entity.SystemRepository;
import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.db.repository.event.types.SystemRepositoryEventTypes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mapping.SimplePropertyHandler;
import org.springframework.data.rest.webmvc.RootResourceInformation;

import javax.persistence.Column;
import java.util.List;

import static com.system.db.repository.event.types.SystemRepositoryEventTypes.INSERT_OR_DELETE;
import static com.system.util.collection.CollectionUtils.newList;

/**
 * The <class>EntityLocateUtils</class> defines
 * utility methods for locating entities.
 *
 * @author Andrew
 */
public class EntityLocateUtils {

    public static <T extends BaseEntity> T findExistingEntityIfRequiredByUploadType(T entity, SystemRepository<T, Integer> entityRepository, SystemRepositoryEventTypes uploadType, RootResourceInformation resourceInformation) {
        if (uploadType != INSERT_OR_DELETE && (uploadType.isUpdate() || uploadType.isDelete())) {
            return findExistingEntityByExample(entity, entityRepository, resourceInformation);
        }
        return null;
    }

    /**
     * Given a parsed example of an entity we will make an attempt to resolve if that
     * entity already exists in the database.
     * <p>
     * 1) If it has an ID query by id
     * 2) If it has a name then findByName
     * 3) If it has neither a name or ID or we cannot find an existing entity by these then we will try by Example.
     */
    public static <T extends BaseEntity> T findExistingEntityByExample(T entity, SystemRepository<T, Integer> entityRepository, RootResourceInformation resourceInformation) {
        T existingEntity = null;

        if (!entity.isNew()) {
            //Entity has an ID, perform ID lookup
            existingEntity = entityRepository.findById((Integer) entity.getId()).get();
        }

        if (existingEntity == null && entity instanceof NamedEntity && StringUtils.isNotBlank(((NamedEntity) entity).getName())) {
            //Did not find by ID but Entity has a name, let's try to resolve existing entity by name
            String name = ((NamedEntity) entity).getName();
            try {
                existingEntity = (T) ((NamedEntityRepository) entityRepository).findByName(name.trim());
            } catch (Exception e) {
                //Might have duplicates since it's not technically a unique column
            }
        }

        if (existingEntity == null) {

            //An entity cannot be found by either ID or name therefore try to find one by Example
            final List<String> ignorePropertyList = newList();
            resourceInformation.getPersistentEntity().doWithProperties((SimplePropertyHandler) property -> {
                if (!property.isAnnotationPresent(Column.class) || !property.getRequiredAnnotation(Column.class).unique()) {
                    ignorePropertyList.add(property.getName());
                }
            });

            //Match any entity that has at least one unique property matching this entity
            ExampleMatcher matcher = ExampleMatcher.matchingAny().withIgnoreNullValues().withIgnoreCase().withIgnorePaths(ignorePropertyList.toArray(new String[0]));
            try {
                existingEntity = entityRepository.findOne(Example.of(entity, matcher)).get();
            } catch (Exception e) {
                //Might not exist.
            }
        }

        return existingEntity;
    }
}
