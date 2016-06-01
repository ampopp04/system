package com.system.db.entity;


import com.system.db.entity.identity.EntityIdentity;

import java.io.Serializable;

/**
 * The <interface>Entity</interface> defines the Entity interface
 * <p>
 * Any class  implementing this Entity is considered for database association
 *
 * @author Andrew
 */
public interface Entity<ID extends Serializable> extends EntityIdentity<ID> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                 Method Definitions                                          //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Determines whether or not this entity is new.
     * <p>
     * Ex: If an Entity has been saved to the database or not
     *
     * @return
     */
    public boolean isNew();

}
