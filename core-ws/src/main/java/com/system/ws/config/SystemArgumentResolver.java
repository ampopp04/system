/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.ws.config;

import com.system.util.collection.CollectionUtils;
import com.system.ws.config.domain.object.reader.EntityAssociationDomainObjectReader;
import com.system.ws.projection.resolver.DynamicEntityResourceArgumentResolver;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.mapping.PersistentEntitiesResourceMappings;
import org.springframework.data.rest.webmvc.config.PersistentEntityResourceAssemblerArgumentResolver;
import org.springframework.data.rest.webmvc.config.PersistentEntityResourceHandlerMethodArgumentResolver;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.data.rest.webmvc.json.JacksonMappingAwareSortTranslator;
import org.springframework.data.rest.webmvc.json.MappingAwareDefaultedPageableArgumentResolver;
import org.springframework.data.rest.webmvc.json.MappingAwarePageableArgumentResolver;
import org.springframework.data.rest.webmvc.json.MappingAwareSortArgumentResolver;
import org.springframework.data.rest.webmvc.mapping.Associations;
import org.springframework.data.rest.webmvc.support.DomainClassResolver;
import org.springframework.data.rest.webmvc.support.HttpMethodHandlerMethodArgumentResolver;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The <class>WsConfig</class> defines the base
 * Spring configuration for this project
 *
 * @author Andrew
 */
@Configuration
public class SystemArgumentResolver extends RepositoryRestMvcConfiguration {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    @Autowired
    private ApplicationContext applicationContext;

    public SystemArgumentResolver(ApplicationContext context,
                                  @Qualifier("mvcConversionService") ObjectFactory<ConversionService> conversionService) {
        super(context, conversionService);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Methods                                                        //////////
    /////////////////////////////////////////////////////////////////////

    /**
     * Enforce that pagination starts at 1 and not 0.
     */
    @Override
    @Bean
    public HateoasPageableHandlerMethodArgumentResolver pageableResolver() {
        HateoasPageableHandlerMethodArgumentResolver resolver = super.pageableResolver();
        resolver.setOneIndexedParameters(true);
        resolver.setMaxPageSize(10000);
        return resolver;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        super.setBeanClassLoader(classLoader);
        this.beanClassLoader = classLoader;
    }

    private ClassLoader beanClassLoader;


    protected List<HandlerMethodArgumentResolver> defaultEntityMethodArgumentResolvers() {

        SpelAwareProxyProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();
        projectionFactory.setBeanFactory(applicationContext);
        projectionFactory.setBeanClassLoader(beanClassLoader);

        PersistentEntityResourceAssemblerArgumentResolver peraResolver = new PersistentEntityResourceAssemblerArgumentResolver(
                persistentEntities(), selfLinkProvider(), repositoryRestConfiguration().getProjectionConfiguration(),
                projectionFactory, associationLinks());

        PageableHandlerMethodArgumentResolver pageableResolver = pageableResolver();

        /**
         * GET RID OF ASSOCIATIONS SO NESTED PROPERTY SORT PARSING WILL WORK!!!!!
         *
         * 	if (associations.isLinkableAssociation(persistentProperty)) {
         return Collections.emptyList();
         }
         *
         * Should be ! in front of the conditional...
         */
        JacksonMappingAwareSortTranslator sortTranslator = new JacksonMappingAwareSortTranslator(objectMapper(),
                repositories(), DomainClassResolver.of(repositories(), resourceMappings(), baseUri()), persistentEntities(),
                new Associations(new PersistentEntitiesResourceMappings(new PersistentEntities(new ArrayList<>())), repositoryRestConfiguration()));

        HandlerMethodArgumentResolver sortResolver = new MappingAwareSortArgumentResolver(sortTranslator, sortResolver());
        HandlerMethodArgumentResolver jacksonPageableResolver = new MappingAwarePageableArgumentResolver(sortTranslator,
                pageableResolver);
        HandlerMethodArgumentResolver defaultedPageableResolver = new MappingAwareDefaultedPageableArgumentResolver(
                sortTranslator, pageableResolver);

        return Arrays.asList(defaultedPageableResolver, jacksonPageableResolver, sortResolver,
                serverHttpRequestMethodArgumentResolver(), repoRequestArgumentResolver(), persistentEntityArgumentResolver(),
                resourceMetadataHandlerMethodArgumentResolver(), HttpMethodHandlerMethodArgumentResolver.INSTANCE, peraResolver,
                backendIdHandlerMethodArgumentResolver(), eTagArgumentResolver());
    }

    @Bean
    @Override
    public PersistentEntityResourceHandlerMethodArgumentResolver persistentEntityArgumentResolver() {

        return new PersistentEntityResourceHandlerMethodArgumentResolver(defaultMessageConverters(),
                repoRequestArgumentResolver(), backendIdHandlerMethodArgumentResolver(),
                new EntityAssociationDomainObjectReader(persistentEntities(), associationLinks()));
    }

    /**
     * Enable our repository bean
     *
     * @return
     */
    @Bean
    public Repositories repositories() {
        return new Repositories(applicationContext);
    }

    @Override
    protected List<HandlerMethodArgumentResolver> defaultMethodArgumentResolvers() {
        List<HandlerMethodArgumentResolver> defaultArgumentResolverList = defaultEntityMethodArgumentResolvers();

        for (int i = 0; i < CollectionUtils.size(defaultArgumentResolverList); i++) {
            if (defaultArgumentResolverList.get(i) instanceof PersistentEntityResourceAssemblerArgumentResolver) {
                defaultArgumentResolverList.set(i, getDynamicEntityResourceArgumentResolver());
                break;
            }
        }

        return defaultArgumentResolverList;
    }

    private PersistentEntityResourceAssemblerArgumentResolver getDynamicEntityResourceArgumentResolver() {
        SpelAwareProxyProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();
        projectionFactory.setBeanFactory(applicationContext);

        return new DynamicEntityResourceArgumentResolver(
                persistentEntities(), selfLinkProvider(), repositoryRestConfiguration().getProjectionConfiguration(), projectionFactory,
                associationLinks());
    }
}