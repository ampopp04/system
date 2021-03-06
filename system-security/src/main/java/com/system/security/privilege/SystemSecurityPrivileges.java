/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.security.privilege;

/**
 * The <class>SystemSecurityPrivileges</class> defines
 * security privileges.
 *
 * @author Andrew
 */
public enum SystemSecurityPrivileges {
    WRITE_PRIVILEGE("WRITE_PRIVILEGE"),
    READ_PRIVILEGE("READ_PRIVILEGE");

    private final String name;

    private SystemSecurityPrivileges(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
