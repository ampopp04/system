/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.security.user.detail.provider;

import org.springframework.security.core.AuthenticationException;

public class SystemActiveDirectoryAuthenticationException extends AuthenticationException {
    private final String dataCode;

    SystemActiveDirectoryAuthenticationException(String dataCode, String message,
                                                 Throwable cause) {
        super(message, cause);
        this.dataCode = dataCode;
    }

    public String getDataCode() {
        return dataCode;
    }
}
