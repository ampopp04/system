package com.system.ws.config;

import com.system.db.entity.Entity;
import com.system.inversion.util.InversionUtils;
import com.system.util.collection.CollectionUtils;
import com.system.ws.projection.resolver.DynamicEntityResourceArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.PersistentEntityResourceAssemblerArgumentResolver;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import java.util.List;

/**
 * The <class>WsConfig</class> defines the base
 * Spring configuration for this project
 *
 * @author Andrew
 */
@Configuration
public class WsConfig extends RepositoryRestMvcConfiguration {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    @Autowired
    private ApplicationContext applicationContext;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Methods                                                        //////////
    /////////////////////////////////////////////////////////////////////

    /**
     * Ensure that IDs are send in the json response for all entities in the system
     *
     * @param config
     */
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.setBasePath("/api");
        config.exposeIdsFor(InversionUtils.getAssignableClassList(Entity.class).toArray(new Class[0]));
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
        List<HandlerMethodArgumentResolver> defaultArgumentResolverList = super.defaultMethodArgumentResolvers();

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
        projectionFactory.setResourceLoader(applicationContext);

        return new DynamicEntityResourceArgumentResolver(
                persistentEntities(), entityLinks(), config().getProjectionConfiguration(), projectionFactory,
                resourceMappings());
    }
}