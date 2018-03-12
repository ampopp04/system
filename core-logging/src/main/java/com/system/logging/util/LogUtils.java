/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system.logging.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.Util;

/**
 * The <class>LogUtils</class> defines
 * a centralized utility class for logging information throughout the system.
 *
 * @author Andrew
 */
public class LogUtils {

    /**
     * Log info level message
     */
    public static void logInfo(String msg) {
        Logger logger = LoggerFactory.getLogger(Util.getCallingClass());
        logger.info(msg);
    }

}