/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.db.entity.identity;


import com.system.db.entity.Entity;

import java.io.Serializable;

/**
 * The <interface>EntityIdentity</interface> represents an entity Identifier
 * <p>
 * Any class  implementing this is considered to have an identifier
 *
 * @author Andrew
 */
public interface EntityIdentity<ID extends Serializable> extends Serializable {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Properties                                                    //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * A reference to the string representation of the ID generic type of this class
     */
    String ID_TYPE_NAME_UPPERCASE = "ID";

    /**
     * A reference to the string representation of the ID generic type of this class in lowercase form.
     * <p>
     * This can also be used as the column reference for the id column name
     */
    String ID_TYPE_NAME_LOWERCASE = "id";

    ///////////////////////////////////////////////////////////////////////
    ////////                                                 Method Definitions                                          //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Get the ID
     *
     * @return
     * @see Entity#isNew()
     */
    public ID getId();

    /**
     * Set the ID
     *
     * @param id
     */
    public void setId(ID id);

}