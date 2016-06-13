package com.system.ws.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

/**
 * The <class>WsConfig</class> defines the base
 * Spring configuration for this project
 *
 * @author Andrew
 */
@Configuration
public class WsConfig extends RepositoryRestConfigurerAdapter {

    @Autowired
    private ApplicationContext context;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.setBasePath("/api");
    }

    @Bean
    public Repositories repositories() {
        return new Repositories(context);
    }
}