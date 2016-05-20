package com.system.db.entity.named;

import com.system.db.entity.base.BaseEntity;

import javax.persistence.MappedSuperclass;


@MappedSuperclass
public abstract class NamedEntity extends BaseEntity {

    private String name;
    private String description;

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

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}