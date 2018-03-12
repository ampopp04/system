/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.db.entity.base;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * The <class>BaseEntity</class> defines the base entity
 * that has an id
 * <p>
 * Any class extending this will inherit this attribute
 *
 * @author Andrew
 */
@MappedSuperclass
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public abstract class BaseEntity<ID extends Number> implements com.system.db.entity.Entity<ID> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;

    ///////////////////////////////////////////////////////////////////////
    ////////                                        Advanced Getter/Setters                                       //////////
    //////////////////////////////////////////////////////////////////////

    public boolean isNew() {
        return getId() == null;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
