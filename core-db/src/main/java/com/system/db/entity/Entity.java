package com.system.db.entity;


import com.system.db.entity.identity.EntityIdentity;

import java.io.Serializable;

/**
 * The <interface>Entity</interface> defines the Entity interface
 * <p>
 * Any class  implementing this Entity is considered for database association
 *
 * @author Andrew
 * @see com.system.db.entity.agent.EntityAgent#getTransformer
 */
public interface Entity<T extends Serializable> extends EntityIdentity<T> {

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
