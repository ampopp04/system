/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.security.role;

/**
 * The <class>SystemSecurityRoles</class> defines
 * security roles.
 *
 * @author Andrew
 */
public enum SystemSecurityRoles {
    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER");

    private final String name;

    private SystemSecurityRoles(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
