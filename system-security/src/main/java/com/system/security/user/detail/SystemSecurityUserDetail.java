package com.system.security.user.detail;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * The <class>SystemSecurityUserDetail</class> defines
 * a holder for our user details to be used throughout the system
 * for security purposes.
 *
 * @author Andrew
 * @see com.system.security.user.SystemSecurityUser
 * @see UserRepositoryUserDetailsService
 */
public class SystemSecurityUserDetail extends User {

    public SystemSecurityUserDetail(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public SystemSecurityUserDetail(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}