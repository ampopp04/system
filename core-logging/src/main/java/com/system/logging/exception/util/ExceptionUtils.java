/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.logging.exception.util;


import com.system.logging.exception.SystemException;

/**
 * The <class>ExceptionUtils</class> defines
 * methods related to handling examining
 * or producing various exceptions throughout the system
 *
 * @author Andrew
 */
public class ExceptionUtils {

    /**
     * A simple wrapper method for making it easy to throw system exceptions
     */
    public static SystemException throwSystemException(String message, Throwable cause) {
        throw SystemException.newInstance(message, cause);
    }

    /**
     * A simple wrapper method for making it easy to throw system exceptions
     */
    public static SystemException throwSystemException(String message) {
        throw SystemException.newInstance(message);
    }

}
