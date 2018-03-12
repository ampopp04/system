/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.db.schema.version;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * The <class>SchemaVersion</class> defines database migration versions.
 * <p>
 * This is a way to track within the database itself which database migrations have ran
 * and meta-data associated with them
 * <p>
 * This table entity is a wrapper around the Flyway schema version table that is created
 *
 * @author Andrew
 */
@Entity
@Table(name = "SCHEMA_VERSION")
public class SchemaVersion implements com.system.db.entity.Entity<String> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @GeneratedValue
    @Id
    @Column(name = "\"version\"")
    private String id;

    @Column(name = "\"installed_rank\"")
    private Integer installedRank;

    @Column(name = "\"description\"")
    private String description;

    @Column(name = "\"type\"")
    private String type;

    @Column(name = "\"script\"")
    private String script;

    @Column(name = "\"checksum\"")
    private Integer checksum;

    @Column(name = "\"installed_by\"")
    private String installedBy;

    @Column(name = "\"installed_on\"")
    private java.sql.Timestamp installedOn;

    @Column(name = "\"execution_time\"")
    private Integer executionTime;

    @Column(name = "\"success\"")
    private Boolean success;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SchemaVersion() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    public boolean isNew() {
        return getId() == null;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Integer getInstalledRank() {
        return installedRank;
    }

    public void setInstalledRank(Integer installedRank) {
        this.installedRank = installedRank;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Integer getChecksum() {
        return checksum;
    }

    public void setChecksum(Integer checksum) {
        this.checksum = checksum;
    }

    public String getInstalledBy() {
        return installedBy;
    }

    public void setInstalledBy(String installedBy) {
        this.installedBy = installedBy;
    }

    public Timestamp getInstalledOn() {
        return installedOn;
    }

    public void setInstalledOn(Timestamp installedOn) {
        this.installedOn = installedOn;
    }

    public Integer getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Integer executionTime) {
        this.executionTime = executionTime;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}