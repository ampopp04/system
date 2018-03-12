/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.security.user.detail.provider;

import com.system.security.user.SystemSecurityUser;
import com.system.security.user.SystemSecurityUserRepository;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.AbstractLdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static com.system.util.string.StringUtils.isEmpty;

public class SystemSecurityUserDetailActiveDirectoryProvider extends AbstractLdapAuthenticationProvider {

    private final String managerDn;
    private final String managerPassword;

    private final String url;
    private String searchFilter = "(&(objectClass=user)(samAccountName={0}))";

    private SystemSecurityUserRepository systemSecurityUserRepository;


    public SystemSecurityUserDetailActiveDirectoryProvider(String url, String managerDn, String managerPassword, SystemSecurityUserRepository systemSecurityUserRepository) {
        Assert.isTrue(StringUtils.hasText(url), "Url cannot be empty");
        this.url = url;
        this.managerDn = managerDn;
        this.managerPassword = managerPassword;
        this.systemSecurityUserRepository = systemSecurityUserRepository;
    }

    private DefaultSpringSecurityContextSource buildContext(String userDn, String password) {
        DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(url);
        contextSource.setUserDn(managerDn);
        contextSource.setPassword(managerPassword);
        contextSource.setUserDn(userDn);
        contextSource.setPassword(password);
        contextSource.afterPropertiesSet();
        return contextSource;
    }

    @Override
    protected DirContextOperations doAuthentication(UsernamePasswordAuthenticationToken auth) {
        String username = auth.getName();

        DirContextOperations ctx = bindAsUser(auth);

        if (logger.isDebugEnabled()) {
            logger.debug("Bind Authentication complete for [" + username + "]");
        }

        return ctx;
    }

    /**
     * Creates the user authority list from the values of the {@code memberOf} attribute
     * obtained from the user's Active Directory entry.
     */
    @Override
    protected Collection<? extends GrantedAuthority> loadUserAuthorities(DirContextOperations userData, String username, String password) {
        String[] groups = userData.getStringAttributes("memberOf");

        if (groups == null) {
            logger.warn("No values for 'memberOf' attribute for username=[" + username + "]");
            return AuthorityUtils.NO_AUTHORITIES;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("'memberOf' attribute values: " + Arrays.asList(groups));
        }

        ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(
                groups.length);

        for (String group : groups) {
            authorities.add(new SimpleGrantedAuthority(new DistinguishedName(group)
                    .removeLast().getValue()));
        }

        return authorities;
    }

    private DirContextOperations bindAsUser(UsernamePasswordAuthenticationToken auth) {
        String username = auth.getName();
        String password = (String) auth.getCredentials();

        if (logger.isDebugEnabled()) {
            logger.debug("Building Bind Authenticator for [" + username + "]");
        }

        String userDn = getUserDn(username);

        DefaultSpringSecurityContextSource contextSource = buildContext(userDn, password);

        BindAuthenticator bindAuthenticator = new BindAuthenticator(contextSource);
        bindAuthenticator.setUserSearch(new FilterBasedLdapUserSearch("", searchFilter, contextSource));
        bindAuthenticator.setUserDnPatterns(new String[]{userDn});

        if (logger.isDebugEnabled()) {
            logger.debug("Bind Authenticator created for [" + username + "], attempting to authenticate.");
        }

        return bindAuthenticator.authenticate(auth);
    }

    private String getUserDn(String username) {
        final SystemSecurityUser user = systemSecurityUserRepository.findByUsername(username);

        if (user == null) {
            return username;
        }

        return isEmpty(user.getDistinguishedName()) ? username : user.getDistinguishedName();
    }

}
