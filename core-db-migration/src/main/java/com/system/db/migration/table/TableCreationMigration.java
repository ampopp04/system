/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.db.migration.table;

import com.system.db.entity.Entity;
import com.system.db.migration.data.BaseDataMigration;
import com.system.db.migration.table.configuration.TableConfiguration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

import static com.system.db.util.entity.EntityUtils.getJpaProperties;
import static com.system.util.collection.CollectionUtils.*;

/**
 * The <class>TableCreationMigration</class> defines the entry point for
 * table based creation migrations.
 * <p>
 * Any class extending this will receive the opportunity to perform
 * table creation and data insertions.
 *
 * @author Andrew
 */
public abstract class TableCreationMigration extends BaseDataMigration {

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Properties                                                //////////
    //////////////////////////////////////////////////////////////////////

    @Lazy
    @Autowired
    private DataSource dataSource;

    @Lazy
    @Autowired
    private JpaVendorAdapter jpaVendorAdapter;

    @Lazy
    @Autowired
    private StandardEnvironment env;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Abstract Methods                                                //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Returns the array of classes for which tables should be created for
     *
     * @return
     */
    protected abstract List<Class<? extends Entity>> getEntityClasses();

    ///////////////////////////////////////////////////////////////////////
    ////////                                               Migration Methods                                             //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Set the table creation entities to the configuration
     */
    private List<String> getManagedClassNameList() {
        List<String> managedClassNames = newList();

        List<Class<? extends Entity>> entityClassList = this.getEntityClasses();
        iterate(iterable(entityClassList), entityClass -> {
            managedClassNames.add(entityClass.getName());
        });

        TableConfiguration.addDependentPersistentClasses(entityClassList, managedClassNames);

        return managedClassNames;
    }

    /**
     * Perform the table creation migration
     *
     * @throws Exception
     */
    @Override
    public void migrate() throws Exception {
        EntityManagerFactory migrationEntityFactory = entityManagerFactory();
        EntityManager entityManager = migrationEntityFactory.createEntityManager();
        super.migrate();
    }

    public EntityManagerFactory entityManagerFactory() {
        final List<String> managedClassNameList = getManagedClassNameList();
        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
        lef.setDataSource(dataSource);
        lef.setJpaVendorAdapter(jpaVendorAdapter);
        lef.setPersistenceUnitPostProcessors((PersistenceUnitPostProcessor) pui -> {
            pui.getManagedClassNames().clear();
            pui.getManagedClassNames().addAll(managedClassNameList);
        });
        Properties props = getJpaProperties(env);

        props.setProperty("hibernate.hbm2ddl.auto", "update");
        props.setProperty("hibernate.hbm2dll.create_namespaces", "true");
        props.setProperty("javax.persistence.create-database-schemas", "true");
        props.remove("hibernate.ddl-auto");
        // props.setProperty("hibernate.implicit_naming_strategy", "default");
        props.setProperty("hibernate.show_sql", "false");
        props.setProperty("hibernate.use_sql_comments", "false");

        lef.setJpaProperties(props);
        lef.setEntityManagerInterface(Session.class);
        lef.setEntityManagerFactoryInterface(SessionFactory.class);
        lef.setPackagesToScan(new String[0]);
        lef.afterPropertiesSet();
        return lef.getObject();
    }

}
