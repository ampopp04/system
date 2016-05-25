package com.system.db.web;

import org.h2.server.web.WebServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The <class>WebConsoleConfig</class> defines the base
 * Spring configuration for the web access portion  of this project
 *
 * @author Andrew
 */
@Configuration
public class WebConsoleConfig {

    /**
     * Add a mapping to open up the database console
     * to web access
     *
     * @return
     */
    @Bean
    ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }
}