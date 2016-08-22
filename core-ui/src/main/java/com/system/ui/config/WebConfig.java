package com.system.ui.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * The <class>WebConfig</class> defines the base
 * Spring configuration for this project
 *
 * @author Andrew
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    /**
     * Redirect any requests sent to root / to /index
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }
}