package com.system.db.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import static com.system.db.util.entity.EntityUtils.getJpaProperties;
import static com.system.inversion.util.InversionUtils.SYSTEM_PACKAGE_ROOT;

/**
 * The <class>DbConfig</class> defines the base
 * Spring configuration for the db project
 *
 * @author Andrew
 */
@EnableSystemRepositories(basePackages = SYSTEM_PACKAGE_ROOT)
@EnableTransactionManagement(proxyTargetClass = true)
@PropertySource(value = {"classpath:application-core-db.properties"})
@Configuration
public class DbConfig {

    /**
     * Custom create the entity manager factory so we can override it's base packages to scan
     *
     * @param dataSource
     * @param jpaVendorAdapter
     * @param env
     * @return
     */
    @Bean
    public EntityManagerFactory entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter, Environment env) {
        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
        lef.setDataSource(dataSource);
        lef.setJpaVendorAdapter(jpaVendorAdapter);
        lef.setPackagesToScan(SYSTEM_PACKAGE_ROOT);
        lef.setJpaPropertyMap(getJpaProperties(env));
        lef.afterPropertiesSet();
        return lef.getObject();
    }
}
