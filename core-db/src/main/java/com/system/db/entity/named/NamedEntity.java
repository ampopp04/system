package com.system.db.entity.named;

import com.system.db.entity.base.BaseEntity;

import javax.persistence.MappedSuperclass;

/**
 * The <class>NamedEntity</class> defines an entity
 * that has a name and description.
 * <p>
 * Any class extending this will inherit these attributes
 *
 * @author Andrew
 */
@MappedSuperclass
public abstract class NamedEntity<T extends Number> extends BaseEntity<T> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * The name associated with this entity
     */
    private String name;
    /**
     * The description associated with this entity
     */
    private String description;

    ///////////////////////////////////////////////////////////////////////
    ////////                                        Advanced Getter/Setters                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String lastname) {
        this.description = lastname;
    }
}