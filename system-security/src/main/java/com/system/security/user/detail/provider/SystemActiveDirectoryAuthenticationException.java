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
