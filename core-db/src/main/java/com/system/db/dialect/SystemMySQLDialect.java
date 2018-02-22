package com.system.db.dialect;

import org.hibernate.dialect.MySQL5Dialect;

/**
 * The <class>SystemMySQLDialect</class> defines
 * an overridden version of the MySQL5Dialect
 * that can be used to change default values
 * for the System implementation.
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

}
