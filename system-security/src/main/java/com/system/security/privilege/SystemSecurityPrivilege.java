package com.system.security.privilege;


import com.system.db.entity.base.BaseEntity;
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
public class SystemSecurityPrivilege extends BaseEntity<Long> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    private String name;

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
        this.name = name;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Collection<SystemSecurityRole> getRoles() {
        return roles;
    }

    public void setRoles(final Collection<SystemSecurityRole> roles) {
        this.roles = roles;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Comparison Methods                                         //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        if (!privilege.equals(privilege.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Privilege [name=").append(name).append("]").append("[id=").append(getId()).append("]");
        return builder.toString();
    }
}