package com.system.security.user;


import com.system.db.entity.base.BaseEntity;
import com.system.security.role.SystemSecurityRole;
import com.system.security.user.detail.SystemSecurityUserDetail;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.Collection;


/**
 * The <class>SystemSecurityUser</class> defines
 * a users security in the system
 *
 * @author Andrew
 * @see SystemSecurityUserDetail
 */
public class SystemSecurityUser extends BaseEntity<Long> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    @NotEmpty(message = "First name is required.")
    private String firstName;

    @NotEmpty(message = "Last name is required.")
    private String lastName;

    @NotEmpty(message = "Username is required.")
    @Column(unique = true, nullable = false)
    private String username;

    @NotEmpty(message = "Password is required.")
    private String password;

    private boolean enabled;

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<SystemSecurityRole> roles;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Constructor                                                   //////////
    //////////////////////////////////////////////////////////////////////

    public SystemSecurityUser() {
        super();
        this.enabled = false;
    }

    public SystemSecurityUser(SystemSecurityUser securityUser) {
        this.firstName = securityUser.firstName;
        this.lastName = securityUser.lastName;
        this.username = securityUser.username;
        this.password = securityUser.password;
        this.enabled = securityUser.enabled;
        this.roles = securityUser.roles;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

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
        result = (prime * result) + ((username == null) ? 0 : username.hashCode());
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
        final SystemSecurityUser user = (SystemSecurityUser) obj;
        if (!username.equals(user.username)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("User [firstName=").append(firstName).append("]").append("[lastName=").append(lastName).append("]").append("[username").append(getUsername()).append("]");
        return builder.toString();
    }
}