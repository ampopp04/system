/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.security.user.detail;

import com.system.security.privilege.SystemSecurityPrivilege;
import com.system.security.role.SystemSecurityRole;
import com.system.security.user.SystemSecurityUser;
import com.system.security.user.SystemSecurityUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.system.util.collection.CollectionUtils.iterate;

/**
 * The <class>UserRepositoryUserDetailsService</class> defines
 * loadUserByUsername which determines if a specific
 * user is granted access to the system
 *
 * @author Andrew
 * @see com.system.security.user.SystemSecurityUser
 * @see SystemSecurityUserDetail
 * @see SystemSecurityRole
 * @see SystemSecurityPrivilege
 */
@Service
@Transactional
public class UserRepositoryUserDetailsService implements UserDetailsService {

    /**
     * Our user repository to access users from the database
     */
    private SystemSecurityUserRepository userRepository;

    @Autowired
    public UserRepositoryUserDetailsService(@Lazy SystemSecurityUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Determine if the user can be loaded
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        try {
            final SystemSecurityUser user = userRepository.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("No user found with username: " + username);
            }
            return new SystemSecurityUserDetail(user.getUsername(), user.getPassword(), user.getEnabled(), true, true, true, getAuthorities(user.getRoles()));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get privileges allowed for specified roles
     *
     * @param roles
     * @return
     */
    public final Collection<? extends GrantedAuthority> getAuthorities(final Collection<SystemSecurityRole> roles) {
        final List<GrantedAuthority> authorities = new ArrayList<>();
        iterate(roles, role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return authorities;
    }
}