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
