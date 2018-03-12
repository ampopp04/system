/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.db.migration.sql;

import com.system.db.migration.base.BaseMigration;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import static com.system.util.collection.CollectionUtils.iterable;
import static com.system.util.collection.CollectionUtils.iterate;

/**
 * The <class>SqlQueryMigration</class> defines a migration
 * that executes a string of SQL
 *
 * @author Andrew
 */
public abstract class SqlQueryMigration extends BaseMigration {
    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Abstract Methods                                                //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Insert Data
     */
    protected abstract List<String> sqlQuery();

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Migration                                                      //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Execute the SQL Query string
     */
    @Override
    public void migrate() throws Exception {
        EntityManager session = getEntityManagerFactory().createEntityManager();
        try {
            session.getTransaction().begin();
            iterate(iterable(sqlQuery()), query ->
                    session.createNativeQuery(query).executeUpdate()
            );
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            if (session.isOpen()) session.close();
        }
    }


    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
}
