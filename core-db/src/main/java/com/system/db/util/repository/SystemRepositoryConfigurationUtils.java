/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.db.util.repository;

import com.system.db.entity.Entity;
import com.system.db.entity.base.BaseEntity;
import com.system.db.entity.named.NamedEntity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.ArrayList;
import java.util.List;

import static com.system.db.util.repository.RepositoryRegistrationUtils.registerRepositoryImplBeanDefinition;
import static com.system.db.util.repository.RepositoryUtils.getRepositoryInterfaceName;
import static com.system.db.util.repository.RepositoryUtils.getRepositoryName;
import static com.system.inversion.util.InversionUtils.getAssignableClassList;
import static com.system.util.collection.CollectionUtils.add;
import static com.system.util.collection.CollectionUtils.iterate;
import static com.system.util.string.StringUtils.capitalize;

/**
 * The <class>SystemRepositoryConfigurationUtils</class> defines
 * repository configuration related utility methods
 *
 * @author Andrew
 */
public class SystemRepositoryConfigurationUtils {

    /**
     * Process each entity within the classpath
     *
     * @param registry
     * @param preprocessedEntityRepositoryList
     * @return
     */
    public static List<BeanDefinition> processCustomEntities(BeanDefinitionRegistry registry, List<String> preprocessedEntityRepositoryList) {
        List<BeanDefinition> repositoryDefinitionList = new ArrayList<>();

        iterate(getAssignableClassList(Entity.class),
                (entityClass) ->
                        add(repositoryDefinitionList,
                                processEntityType(registry, entityClass, preprocessedEntityRepositoryList)));

        return repositoryDefinitionList;
    }


    /**
     * Process a specific entity type, this would be any database {@link com.system.db.entity.Entity} that extends
     * {@link NamedEntity} or {@link BaseEntity}
     * <p>
     * These will only be processed if they currently do not have an applicable repository defined in the context for use.
     *
     * @param registry
     * @param entityType
     */
    private static BeanDefinition processEntityType(BeanDefinitionRegistry registry, Class<?> entityType, List<String> preprocessedRepositoryNameList) {
        String repositoryBeanName = getRepositoryName(entityType);
        String repositoryInterfaceName = getRepositoryInterfaceName(entityType);
        return (!registry.containsBeanDefinition(repositoryBeanName) && !preprocessedRepositoryNameList.contains(capitalize(repositoryBeanName)))
                ? registerRepositoryImplBeanDefinition(entityType) : null;
    }
}
