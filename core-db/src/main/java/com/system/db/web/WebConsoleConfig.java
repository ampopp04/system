package com.system.db.web;

import org.h2.server.web.WebServlet;
import org.h2.tools.Server;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

/**
 * The <class>WebConsoleConfig</class> defines the base
 * Spring configuration for the web access portion  of this project
 *
 * @author Andrew
 */
@Configuration
public class WebConsoleConfig {

    /**
     * TCP connection to connect with SQL clients to the embedded h2 database.
     * <p>
     * Connect to "jdbc:h2:tcp://localhost:9092/mem:testdb", username "sa", password empty.
     */
    @Bean
    public Server h2TcpServer() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092").start();
    }

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