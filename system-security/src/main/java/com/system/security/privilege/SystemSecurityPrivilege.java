/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.security.privilege;


import com.system.db.entity.named.NamedEntity;
import com.system.security.role.SystemSecurityRole;

import javax.persistence.ManyToMany;
import java.util.Collection;


/**
 * The <class>SystemSecurityPrivilege</class> defines
 * security privileges.
 *
 * @author Andrew
 * @see SystemSecurityPrivileges
 */
public class SystemSecurityPrivilege extends NamedEntity<Long> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    @ManyToMany(mappedBy = "privileges")
    private Collection<SystemSecurityRole> roles;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Constructor                                                   //////////
    //////////////////////////////////////////////////////////////////////

    public SystemSecurityPrivilege() {
        super();
    }

    public SystemSecurityPrivilege(final String name) {
        super();
        setName(name);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public Collection<SystemSecurityRole> getRoles() {
        return roles;
    }

    public void setRoles(Collection<SystemSecurityRole> roles) {
        this.roles = roles;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Comparison Methods                                         //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SystemSecurityPrivilege privilege = (SystemSecurityPrivilege) obj;
        if (!privilege.equals(privilege.getName())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Privilege [name=").append(getName()).append("]").append("[id=").append(getId()).append("]");
        return builder.toString();
    }
}