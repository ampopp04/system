/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.db.dialect;

import org.hibernate.dialect.H2Dialect;

/**
 * The <class>SystemH2Dialect</class> defines
 * an overridden version of the H2Dialect
 * that changes the query sequence table string to be upper-case
 * to fit with Spring table access conventions.
 *
 * @author Andrew
 * @see org.hibernate.cfg.ImprovedNamingStrategy
 */
public class SystemH2Dialect extends H2Dialect {

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemH2Dialect() {
        super();
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                        Getter/Setters                                            //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    public String getQuerySequencesString() {
        return super.getQuerySequencesString().replace("information_schema", "INFORMATION_SCHEMA");
    }
}
