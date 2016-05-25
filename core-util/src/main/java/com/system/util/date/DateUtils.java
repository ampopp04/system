package com.system.util.date;


import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * The <class>DateUtils</class> defines
 * date related utility methods
 *
 * @author Andrew
 */
public class DateUtils {
    /**
     * Default data format
     */
    public static final String BASIC_DATE_FORMAT = "dd-MMM-yyyy";

    /**
     * Retrieve current date as a string
     *
     * @return
     */
    public static String getToday() {
        return DateFormatUtils.format(new Date(), BASIC_DATE_FORMAT);
    }
}
