package com.system.security.user;


import com.system.db.entity.named.NamedEntity;
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
public class SystemSecurityUser extends NamedEntity<Integer> {

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

    private String distinguishedName;

    private String userPrincipalName;

    private String password;

    private Boolean enabled;

    @Column(length = 5000)
    private String memberOf;

    private Boolean admin = false;

    @ManyToMany(targetEntity = SystemSecurityRole.class)
    @JoinTable(joinColumns = @JoinColumn(name = "user_id", referencedColumnName = ID_TYPE_NAME_LOWERCASE), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = ID_TYPE_NAME_LOWERCASE))
    private Collection<SystemSecurityRole> roles;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Constructor                                                   //////////
    //////////////////////////////////////////////////////////////////////

    public SystemSecurityUser() {
        super();
        this.enabled = false;
    }

    public SystemSecurityUser(SystemSecurityUser securityUser) {
        this.setFirstName(securityUser.firstName);
        this.setLastName(securityUser.lastName);
        this.setUsername(securityUser.username);
        this.setPassword(securityUser.password);
        this.setEnabled(securityUser.enabled);
        this.setDistinguishedName(securityUser.distinguishedName);
        this.setRoles(securityUser.roles);
        this.setUserPrincipalName(securityUser.userPrincipalName);
        this.setAdmin(securityUser.getAdmin());
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        String lastName = getLastName();
        setName(firstName + (lastName == null ? "" : (" " + getLastName())));
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        String firstName = getFirstName();
        setName((firstName == null ? "" : (getFirstName() + " ")) + lastName);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDistinguishedName() {
        return distinguishedName;
    }

    public void setDistinguishedName(String distinguishedName) {
        this.distinguishedName = distinguishedName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Collection<SystemSecurityRole> getRoles() {
        return roles;
    }

    public void setRoles(Collection<SystemSecurityRole> roles) {
        this.roles = roles;
    }

    public String getUserPrincipalName() {
        return userPrincipalName;
    }

    public void setUserPrincipalName(String userPrincipalName) {
        this.userPrincipalName = userPrincipalName;
    }

    public String getMemberOf() {
        return memberOf;
    }

    public void setMemberOf(String memberOf) {
        this.memberOf = memberOf;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
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
        final StringBuilder sb = new StringBuilder("SystemSecurityUser{");
        sb.append("super='").append(super.toString()).append('\'');
        sb.append("firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", username='").append(username).append('\'');
        sb.append(", distinguishedName='").append(distinguishedName).append('\'');
        sb.append(", userPrincipalName='").append(userPrincipalName).append('\'');
        sb.append(", enabled=").append(enabled);
        sb.append('}');
        return sb.toString();
    }
}