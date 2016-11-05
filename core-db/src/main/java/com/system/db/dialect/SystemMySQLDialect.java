package com.system.db.dialect;

import org.hibernate.dialect.MySQL5Dialect;

/**
 * The <class>SystemMySQLDialect</class> defines
 * an overridden version of the MySQL5Dialect
 * that changes the query sequence table string to be upper-case
 * to fit with Spring table access conventions.
 *
 * @author Andrew
 * @see org.hibernate.cfg.ImprovedNamingStrategy
 */
public class SystemMySQLDialect extends MySQL5Dialect {

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemMySQLDialect() {
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
