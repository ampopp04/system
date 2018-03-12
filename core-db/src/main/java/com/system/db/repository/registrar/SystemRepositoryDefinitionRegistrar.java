/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.db.repository.registrar;

import com.system.db.config.EnableSystemRepositories;
import com.system.db.entity.named.NamedEntity;
import com.system.db.repository.registrar.configuration.AnnotationSystemRepositoryConfigurationSource;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;
import org.springframework.data.repository.config.*;

import java.lang.annotation.Annotation;

/**
 * The <interface>SystemRepositoryDefinitionRegistrar</interface> injects
 * new instances of {@link com.system.db.repository.base.entity.SystemRepository}
 * and {@link com.system.db.repository.base.named.NamedEntityRepository}
 * into the application context for each database {@link com.system.db.entity.Entity}
 * based on whether it is a {@link NamedEntity} or {@link com.system.db.entity.base.BaseEntity}
 * <p>
 * If the base repository retrieval methods are not sufficient these repositories can be extended
 * in which case no auto-injection will take place as it is assumed these entity specific repositories will take
 * precedence.
 *
 * @author Andrew
 * @see AnnotationSystemRepositoryConfigurationSource
 * @see com.system.db.repository.base.entity.SystemRepository
 * @see com.system.db.repository.base.named.NamedEntityRepository
 * @see NamedEntity
 * @see com.system.db.entity.base.BaseEntity
 */
public class SystemRepositoryDefinitionRegistrar extends RepositoryBeanDefinitionRegistrarSupport {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                      Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    private ResourceLoader resourceLoader;
    private Environment environment;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                   Implementation                                                  //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Process the {@link BeanDefinitionRegistry}   and iterate over each of it's meta models entities to ensure
     * they have a repository defined within the registry for use.
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        // Guard against calls for sub-classes
        if (annotationMetadata.getAnnotationAttributes(getAnnotation().getName()) == null) return;

        AnnotationRepositoryConfigurationSource configurationSource = new AnnotationSystemRepositoryConfigurationSource(annotationMetadata, getAnnotation(), resourceLoader, environment, registry);
        RepositoryConfigurationExtension extension = getExtension();
        RepositoryConfigurationUtils.exposeRegistration(extension, registry, configurationSource);

        RepositoryConfigurationDelegate delegate = new RepositoryConfigurationDelegate(configurationSource, resourceLoader, environment);

        delegate.registerRepositoriesIn(registry, extension);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                          Getters                                                         //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    protected RepositoryConfigurationExtension getExtension() {
        return new JpaRepositoryConfigExtension();
    }

    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return EnableSystemRepositories.class;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                           Setters                                                         //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}