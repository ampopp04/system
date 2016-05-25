package com.system.db.entity.identity;


import java.io.Serializable;

/**
 * The <interface>EntityIdentity</interface> represents an entity Identifier
 * <p>
 * Any class  implementing this is considered to have an identifier
 *
 * @author Andrew
 * @see com.system.db.entity.agent.EntityAgent#getTransformer
 */
public interface EntityIdentity<T extends Serializable> extends Serializable {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                 Method Definitions                                          //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Get the ID
     *
     * @return
     */
    public T getId();

    /**
     * Set the ID
     *
     * @param id
     */
    public void setId(T id);

}