package com.system.db.schema.table;


import com.system.db.entity.named.NamedEntity;

import javax.persistence.Entity;

/**
 * The <class>SchemaTable</class> defines database tables.
 * <p>
 * This is a way to track within the database itself which tables have been created
 * and meta-data associated with them.
 *
 * @author Andrew
 */
@Entity
public class SchemaTable extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SchemaTable() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

}