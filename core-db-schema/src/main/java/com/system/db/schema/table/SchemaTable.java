package com.system.db.schema.table;


import com.system.db.entity.named.NamedEntity;

import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * The <class>SchemaTable</class> defines database tables.
 * <p>
 * This is a way to track within the database itself which tables have been created
 * and meta-data associated with them.
 *
 * @author Andrew
 */
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})},
        indexes = {@Index(columnList = "name")}
)
public class SchemaTable extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * The entity class path
     * <p>
     * Ex. com.system.security.user.SystemSecurityUser
     */
    private String entityClass;

    /**
     * Determines whether entities associated to this table should
     * be cached by default
     */
    private Boolean cacheEnabled = false;

    /**
     * Allow items to be cached for 9 hours by default
     */
    private Integer cacheTimeToLiveInSeconds = 32400;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SchemaTable() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public String getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }

    public Boolean getCacheEnabled() {
        return cacheEnabled;
    }

    public void setCacheEnabled(Boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public Integer getCacheTimeToLiveInSeconds() {
        return cacheTimeToLiveInSeconds;
    }

    public void setCacheTimeToLiveInSeconds(Integer cacheTimeToLiveInSeconds) {
        this.cacheTimeToLiveInSeconds = cacheTimeToLiveInSeconds;
    }
}