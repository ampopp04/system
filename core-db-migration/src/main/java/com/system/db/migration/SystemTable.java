package com.system.db.migration;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SystemTable {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String description;

    public SystemTable() {
    }

    public SystemTable(String name, String description) {
        this.name = name;
        this.description = description;
    }

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
        return "SystemTable [name=" + this.name + ", description=" + this.description
                + "]";
    }
}