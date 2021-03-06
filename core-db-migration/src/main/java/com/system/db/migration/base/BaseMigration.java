/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.db.migration.base;


import com.system.inversion.component.InversionComponent;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * The <class>BaseMigration</class> defines the entry point for
 * flyway migrations
 * <p>
 *
 * @author Andrew
 * @see SpringJdbcMigration#migrate(JdbcTemplate)
 */
@InversionComponent
public abstract class BaseMigration implements SystemMigration {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

}