package com.system.db.web;

import org.h2.server.web.WebServlet;
import org.h2.tools.Server;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
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
    @ConditionalOnExpression("'${spring.datasource.url:true}' == 'true'")
    public Server h2TcpServer(EntityManagerFactory entityManagerFactory) throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092").start();
    }

    /**
     * Add a mapping to open up the database console
     * to web access
     */
    @Bean
    @ConditionalOnExpression("'${spring.datasource.url:true}' == 'true'")
    public ServletRegistrationBean h2servletRegistration(EntityManagerFactory entityManagerFactory) {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }
}