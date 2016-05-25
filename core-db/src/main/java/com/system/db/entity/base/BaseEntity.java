package com.system.db.entity.base;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * The <class>BaseEntity</class> defines the base entity
 * that has an id
 * <p>
 * Any class extending this will inherit this attribute
 *
 * @author Andrew
 */
@MappedSuperclass
public abstract class BaseEntity<T extends Number> implements com.system.db.entity.Entity<T> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Id
    @GeneratedValue
    private T id;

    ///////////////////////////////////////////////////////////////////////
    ////////                                        Advanced Getter/Setters                                       //////////
    //////////////////////////////////////////////////////////////////////

    public boolean isNew() {
        return getId() == null;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}
