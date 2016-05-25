package com.system.db.migration.schema.table;

import com.system.db.entity.named.NamedEntity;

import javax.persistence.Entity;

/**
 * The <class>SystemTable</class> defines database tables.
 * <p>
 * This is a way to track within the database itself which tables have been created
 * and meta-data associated with them.
 *
 * @author Andrew
 */
@Entity
public class SystemTable extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemTable() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

}