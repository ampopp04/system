package com.system.db.entity.base;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
public abstract class BaseEntity<ID extends Number> implements com.system.db.entity.Entity<ID> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
