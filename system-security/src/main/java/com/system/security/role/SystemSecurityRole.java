package com.system.security.role;


import com.system.db.entity.named.NamedEntity;
import com.system.security.privilege.SystemSecurityPrivilege;
import com.system.security.user.SystemSecurityUser;

import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.Collection;


/**
 * The <class>SystemSecurityRole</class> defines
 * security roles.
 *
 * @author Andrew
 */
public class SystemSecurityRole extends NamedEntity<Long> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    @ManyToMany(mappedBy = "roles")
    private Collection<SystemSecurityUser> users;

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "role_id", referencedColumnName = ID_TYPE_NAME_LOWERCASE), inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = ID_TYPE_NAME_LOWERCASE))
    private Collection<SystemSecurityPrivilege> privileges;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Constructor                                                   //////////
    //////////////////////////////////////////////////////////////////////

    public SystemSecurityRole() {
        super();
    }

    public SystemSecurityRole(final String name) {
        super();
        setName(name);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public Collection<SystemSecurityUser> getUsers() {
        return users;
    }

    public void setUsers(final Collection<SystemSecurityUser> users) {
        this.users = users;
    }

    public Collection<SystemSecurityPrivilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Collection<SystemSecurityPrivilege> privileges) {
        this.privileges = privileges;
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
        final SystemSecurityRole role = (SystemSecurityRole) obj;
        if (!role.equals(role.getName())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Role [name=").append(getName()).append("]").append("[id=").append(getId()).append("]");
        return builder.toString();
    }
}